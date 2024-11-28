package au.edu.rmit.sept.webapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;

import au.edu.rmit.sept.webapp.models.ActivePrescription;
import au.edu.rmit.sept.webapp.models.DeliveryInformation;
import au.edu.rmit.sept.webapp.models.Prescription;
import au.edu.rmit.sept.webapp.repositories.ActivePrescriptionRepository;
import au.edu.rmit.sept.webapp.repositories.DeliveryRepository;
import au.edu.rmit.sept.webapp.repositories.PrescriptionRepository;
import au.edu.rmit.sept.webapp.repositories.UserAccountRepository;

@SpringBootTest
public class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private ActivePrescriptionRepository activePrescriptionRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock SesEmailService sesEmailService;

    @InjectMocks
    private DeliveryService deliveryService;

    @Test
    void testGetDeliveryInformationByUser() throws Exception {
        Long userId = 2L;

        // Create mock prescription and delivery objects
        Prescription mockPrescription = new Prescription();
        mockPrescription.setId(1L);
        mockPrescription.setMedicineName("Test Medicine");

        ActivePrescription mockActivePrescription = new ActivePrescription();
        mockActivePrescription.setId(1L);
        mockActivePrescription.setPrescription(mockPrescription);

        DeliveryInformation mockDelivery = new DeliveryInformation();
        mockDelivery.setActivePrescription(mockActivePrescription);
        mockDelivery.setDeliveryStatus("Delivered");
        mockDelivery.setDeliveryAddress("123 Test Street");
        mockDelivery.setActualDeliveryDate(Date.valueOf(LocalDate.now()));

        // Mock a list of deliveries
        List<DeliveryInformation> mockDeliveries = new ArrayList<>();
        mockDeliveries.add(mockDelivery);

        // Mock repository call to return the list of deliveries
        when(deliveryRepository.getDeliveryInformationByUser(userId)).thenReturn(mockDeliveries);

        // Call the service method
        List<DeliveryInformation> deliveries = deliveryService.getDeliveryInformationByUser(userId);

        // Validate the results
        assertNotNull(deliveries);
        assertEquals(1, deliveries.size());
        assertEquals("Delivered", deliveries.get(0).getDeliveryStatus());
        assertEquals("123 Test Street", deliveries.get(0).getDeliveryAddress());
        assertEquals("Test Medicine", deliveries.get(0).getActivePrescription().getPrescription().getMedicineName());
    }

    @Test
    void testRequestDelivery() throws Exception {
        Long prescriptionId = 1L;
        Long activePrescriptionId = 1L;
        Long mockedUserId = 2L;

        String deliveryAddress = "456 Test Avenue";
        String medicineName = "New Medicine";
        String orderStatus = "Ordered";

        // Create a mock prescription
        Prescription mockPrescription = new Prescription();
        mockPrescription.setId(prescriptionId);
        mockPrescription.setMedicineName(medicineName);

        ActivePrescription mockActivePrescription = new ActivePrescription();
        mockActivePrescription.setId(1L);
        mockActivePrescription.setPrescription(mockPrescription);

        // Mock the active prescription repository call
        when(activePrescriptionRepository.getActivePrescriptionById(activePrescriptionId)).thenReturn(mockActivePrescription);

        // Capture the DeliveryInformation argument
        ArgumentCaptor<DeliveryInformation> captor = ArgumentCaptor.forClass(DeliveryInformation.class);

        // Call the service method to request delivery
        deliveryService.requestDelivery(activePrescriptionId, deliveryAddress, mockedUserId);

        // Capture the delivery passed to saveDeliveryInformation because the REPO is mocked. 
        verify(deliveryRepository).saveDeliveryInformation(captor.capture());

        // Validate the results
        DeliveryInformation capturedDelivery = captor.getValue();
        assertNotNull(capturedDelivery);
        //Checks Name, Order Status, Delivery Address, Order Date and Estimated Date
        assertEquals(medicineName, capturedDelivery.getActivePrescription().getPrescription().getMedicineName());
        assertEquals(orderStatus, capturedDelivery.getDeliveryStatus());
        assertEquals(deliveryAddress, capturedDelivery.getDeliveryAddress());
        assertEquals(Date.valueOf(LocalDate.now()), capturedDelivery.getOrderDate());
        assertEquals(Date.valueOf(LocalDate.now().plusDays(3)), capturedDelivery.getEstimatedDeliveryDate());
        
    }
}

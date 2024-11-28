package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.ActivePrescription;
import au.edu.rmit.sept.webapp.repositories.ActivePrescriptionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ActivePrescriptionServiceTest {

    @Mock
    private ActivePrescriptionRepository activePrescriptionRepository;

    @InjectMocks
    private ActivePrescriptionService activePrescriptionService;

    // Test getActivePrescriptionsByUserId method
    @Test
    void testGetActivePrescriptionsByUserId() throws SQLException {
        Long userId = 1L;

        // Mock the repository response
        List<ActivePrescription> mockPrescriptions = new ArrayList<>();
        ActivePrescription prescription = new ActivePrescription();
        prescription.setId(1L);
        prescription.setPrescriptionId(100L);
        prescription.setPetId(2L);
        mockPrescriptions.add(prescription);

        when(activePrescriptionRepository.getActivePrescriptionsByUserId(userId)).thenReturn(mockPrescriptions);

        // Call the service method
        List<ActivePrescription> result = activePrescriptionService.getActivePrescriptionsByUserId(userId);

        // Verify the result
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getPrescriptionId());
    }

    // Test getActivePrescriptionById method
    @Test
    void testGetActivePrescriptionById() throws SQLException {
        Long prescriptionId = 1L;

        // Mock the repository response
        ActivePrescription mockPrescription = new ActivePrescription();
        mockPrescription.setId(prescriptionId);
        mockPrescription.setPrescriptionId(100L);
        mockPrescription.setPetId(2L);

        when(activePrescriptionRepository.getActivePrescriptionById(prescriptionId)).thenReturn(mockPrescription);

        // Call the service method
        ActivePrescription result = activePrescriptionService.getActivePrescriptionById(prescriptionId);

        // Verify the result
        assertNotNull(result);
        assertEquals(prescriptionId, result.getId());
        assertEquals(100L, result.getPrescriptionId());
    }

    // Test setExpirationDateUponDelivery method with delivery date
    @Test
    void testSetExpirationDateUponDelivery() throws SQLException {
        Long activePrescriptionId = 1L;
        Date deliveryDate = Date.valueOf(LocalDate.now());  // Use the current date as delivery date
        LocalDate expectedExpirationDate = deliveryDate.toLocalDate().plusMonths(2);  
        // Capture the argument passed to updateExpirationDate
        ArgumentCaptor<Date> expirationDateCaptor = ArgumentCaptor.forClass(Date.class);

        // Call the service method with the delivery date
        activePrescriptionService.setExpirationDateUponDelivery(activePrescriptionId, deliveryDate);

        // Verify the repository method was called with the correct expiration date
        verify(activePrescriptionRepository).updateExpirationDate(eq(activePrescriptionId), expirationDateCaptor.capture());

        // Verify that the expiration date is 2 months after the delivery date
        assertEquals(Date.valueOf(expectedExpirationDate), expirationDateCaptor.getValue());
    }

    // Test exception handling in getActivePrescriptionsByUserId
    @Test
    void testGetActivePrescriptionsByUserIdWithException() throws SQLException {
        Long userId = 1L;

        // Mock the repository to throw an SQLException
        when(activePrescriptionRepository.getActivePrescriptionsByUserId(userId)).thenThrow(SQLException.class);

        // Call the service method and assert that it returns null due to the exception
        List<ActivePrescription> result = activePrescriptionService.getActivePrescriptionsByUserId(userId);
        assertNull(result);

        // Verify that the exception was caught
        verify(activePrescriptionRepository, times(1)).getActivePrescriptionsByUserId(userId);
    }

    // Test exception handling in getActivePrescriptionById
    @Test
    void testGetActivePrescriptionByIdWithException() throws SQLException {
        Long prescriptionId = 1L;

        // Mock the repository to throw an SQLException
        when(activePrescriptionRepository.getActivePrescriptionById(prescriptionId)).thenThrow(SQLException.class);

        // Call the service method and assert that it returns null due to the exception
        ActivePrescription result = activePrescriptionService.getActivePrescriptionById(prescriptionId);
        assertNull(result);

        // Verify that the exception was caught
        verify(activePrescriptionRepository, times(1)).getActivePrescriptionById(prescriptionId);
    }

}

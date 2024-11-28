package au.edu.rmit.sept.webapp.controllers;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;

import au.edu.rmit.sept.webapp.models.ActivePrescription;
import au.edu.rmit.sept.webapp.models.DeliveryInformation;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.Prescription;
import au.edu.rmit.sept.webapp.services.PrescriptionService;
import au.edu.rmit.sept.webapp.services.ActivePrescriptionService;
import au.edu.rmit.sept.webapp.services.DeliveryService;
import au.edu.rmit.sept.webapp.services.GetCurrentUserIDService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class PrescriptionControllerTest {

    @Autowired
    private PrescriptionController controller;

    // Mock the services
    @MockBean
    private ActivePrescriptionService activePrescriptionService;

    @MockBean
    private PrescriptionService prescriptionService;

    @MockBean
    private DeliveryService deliveryService;

    @MockBean
    private GetCurrentUserIDService getCurrentUserIDService;

    @Autowired
    private MockMvc mvc;

    // Test that the context is creating the controller
    @Test
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    // Test that the "/prescriptions" page loads successfully and displays key content
    @Test
    void shouldDisplayActivePrescriptions() throws Exception {
        // Mocking the logged-in user ID
        when(getCurrentUserIDService.getCurrentUserID()).thenReturn(2);

        // Mocking a list of active prescriptions
        List<ActivePrescription> mockActivePrescriptions = new ArrayList<>();
        ActivePrescription activePrescription = new ActivePrescription();
        
        Prescription prescription = new Prescription();
        prescription.setId(1L);
        prescription.setMedicineName("Test Medicine");
        prescription.setDosage("100mg");
        prescription.setFrequency("1 time daily");

        activePrescription.setPrescription(prescription);
        activePrescription.setPetId(1L);
        activePrescription.setExpirationDate(java.sql.Date.valueOf("2024-12-31"));

        mockActivePrescriptions.add(activePrescription);

         // Mocking service call
         int currentUserId = getCurrentUserIDService.getCurrentUserID();
         when(activePrescriptionService.getActivePrescriptionsByUserId((long) currentUserId)).thenReturn(mockActivePrescriptions);

        // Perform the GET request and verify the response
        mvc.perform(get("/prescriptions"))
            .andExpect(status().isOk()) // Status is OK
            .andExpect(content().string(containsString("Manage Prescriptions"))) // Page contains this string
            .andExpect(content().string(containsString("Test Medicine"))) // Prescription name is displayed
            .andExpect(content().string(containsString("100mg"))) // Dosage is displayed
            .andExpect(content().string(containsString("31-12-2024"))); // Expiration date is displayed
    }

    @Test
    void shouldHandleNoActivePrescriptions() throws Exception {
    // Mocking the logged-in user ID
    when(getCurrentUserIDService.getCurrentUserID()).thenReturn(2);

    // Mocking an empty list of active prescriptions
    List<ActivePrescription> mockActivePrescriptions = new ArrayList<>();

    // Mocking service call to return an empty list
    when(activePrescriptionService.getActivePrescriptionsByUserId(2L)).thenReturn(mockActivePrescriptions);

    // Perform the GET request and verify the response
    mvc.perform(get("/prescriptions"))
            .andExpect(status().isOk()) // Status is OK
            .andExpect(content().string(containsString("You have no active prescriptions registered with us")));
}

    // Test that the "/prescriptions" page loads successfully and displays delivery table correctly
    @Test
    void shouldLoadDeliveries() throws Exception {
        // Mocking the logged-in user ID
        when(getCurrentUserIDService.getCurrentUserID()).thenReturn(2);

        // Mocking an active prescription and delivery information
        ActivePrescription activePrescription = new ActivePrescription();
        Prescription prescription = new Prescription();
        prescription.setId(1L);
        prescription.setMedicineName("Delivery Medicine");
        activePrescription.setPrescription(prescription);

        // Mocking a list of delivery orders
        List<DeliveryInformation> mockDeliveries = new ArrayList<>();
        DeliveryInformation delivery = new DeliveryInformation();
        delivery.setDeliveryStatus("Delivered");
        delivery.setActualDeliveryDate(java.sql.Date.valueOf("2024-09-15"));
        delivery.setDeliveryAddress("123 Test Street");
        delivery.setActivePrescription(activePrescription); // Link this delivery to the mocked active prescription

        mockDeliveries.add(delivery);

        // Mocking service call to return the list of delivery orders
        int currentUserId = getCurrentUserIDService.getCurrentUserID();
        when(deliveryService.getDeliveryInformationByUser((long) currentUserId)).thenReturn(mockDeliveries);

        // Perform the GET request and verify the response
        mvc.perform(get("/prescriptions"))
                .andExpect(status().isOk()) // Status is OK
                .andExpect(content().string(containsString("123 Test Street")))
                .andExpect(content().string(containsString("Delivered")))
                .andExpect(content().string(containsString("15-09-2024")))
                .andExpect(content().string(containsString("Delivery Medicine")));
    }


    // Test for error message when no deliveries exist
    @Test
    void shouldHandleNoDeliveries() throws Exception {
    // Mocking the logged-in user ID
    when(getCurrentUserIDService.getCurrentUserID()).thenReturn(2);

    // Mocking an empty list of deliveries
    List<DeliveryInformation> mockDeliveries = new ArrayList<>();

    // Mocking service call to return an empty list
    when(deliveryService.getDeliveryInformationByUser(2L)).thenReturn(mockDeliveries);

    // Perform the GET request and verify the response
    mvc.perform(get("/prescriptions"))
            .andExpect(status().isOk()) // Status is OK
            .andExpect(content().string(containsString("You have no past or current deliveries")));
}

    //Test Should display confirmation message upon successful delivery request
    @Test
    void shouldProcessSuccessfulDeliveryRequest() throws Exception {
        // Mocking the logged-in user ID
        when(getCurrentUserIDService.getCurrentUserID()).thenReturn(2);

        // Mocking a valid active prescription and its associated prescription
        Prescription prescription = new Prescription();
        prescription.setId(1L);
        prescription.setMedicineName("Valid Medicine");
        
        ActivePrescription activePrescription = new ActivePrescription();
        activePrescription.setId(1L);
        activePrescription.setPrescription(prescription);

        when(activePrescriptionService.getActivePrescriptionById(1L)).thenReturn(activePrescription);
        when(deliveryService.requestDelivery(1L, "123 Test Street", 2L)).thenReturn(true);

        // Perform the POST request and verify success response
        mvc.perform(post("/prescriptions/requestDelivery")
                .param("activePrescriptionId", "1")
                .param("deliveryAddress", "123 Test Street"))
                .andExpect(status().is3xxRedirection()) // Redirection to confirmation
                .andExpect(flash().attributeExists("message", "prescriptionName", "deliveryAddress", "estimatedDeliveryTime"))
                .andExpect(flash().attribute("message", "Delivery request placed successfully."))
                .andExpect(flash().attribute("prescriptionName", "Valid Medicine"))
                .andExpect(flash().attribute("deliveryAddress", "123 Test Street"))
                .andExpect(flash().attribute("estimatedDeliveryTime", "2-3 Business Days"));
    }


    //Test should display error message on unsuccsessful delivery request. 
    @Test
    void shouldHandleInvalidPrescriptionIdInDeliveryRequest() throws Exception {
        // Mocking the logged-in user ID
        when(getCurrentUserIDService.getCurrentUserID()).thenReturn(2);

        // Mocking the delivery service to throw an exception for invalid prescription ID
        when(deliveryService.requestDelivery(999L, "123 Test Street", 2L)).thenThrow(new IllegalArgumentException("Prescription not found"));

        // Perform the POST request and verify the error handling
        mvc.perform(post("/prescriptions/requestDelivery")
                .param("activePrescriptionId", "999")
                .param("deliveryAddress", "123 Test Street"))
                .andExpect(status().is3xxRedirection()) // Redirection after error
                .andExpect(flash().attributeExists("message")) // Error message is set
                .andExpect(flash().attribute("message", "Error placing delivery request."));
    }


}



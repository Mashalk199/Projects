package au.edu.rmit.sept.webapp.repositories;

import au.edu.rmit.sept.webapp.models.ActivePrescription;
import au.edu.rmit.sept.webapp.models.DeliveryInformation;
import au.edu.rmit.sept.webapp.models.Prescription;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeliveryRepositoryTest {

    @Autowired
    DeliveryRepository repo;

    @Autowired
    private Flyway flyway;

    Long userId = 1L;
    Long deliveryId = 1L; // Assuming ID has a valid delivery for this test 

    @BeforeEach
    void setUp() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void testSaveDeliveryInformation() throws SQLException {
        // Prepare mock ActivePrescription and DeliveryInformation
        ActivePrescription activePrescription = new ActivePrescription();
        activePrescription.setId(1L);
        activePrescription.setPetId(2L);

        Prescription prescription = new Prescription();
        prescription.setId(1L);
        prescription.setMedicineName("Medicine a");
        activePrescription.setPrescription(prescription);

        DeliveryInformation delivery = new DeliveryInformation();
        delivery.setActivePrescription(activePrescription);
        delivery.setUserId(1L);
        delivery.setDeliveryStatus("Ordered");
        delivery.setDeliveryAddress("123 Save Test Street");
        delivery.setOrderDate(new Date());
        delivery.setEstimatedDeliveryDate(java.sql.Date.valueOf(LocalDate.now().plusDays(3)));

        // Save the delivery information
        repo.saveDeliveryInformation(delivery);

        List<DeliveryInformation> deliveries = repo.getDeliveryInformationByUser(userId);
        assertNotNull(deliveries);
        assertFalse(deliveries.isEmpty());
        
        // Fetch the newly inserted delivery and validate
        DeliveryInformation savedDelivery = deliveries.get(0);
        assertNotNull(savedDelivery);
        assertEquals("Ordered", savedDelivery.getDeliveryStatus());
        assertEquals("123 Save Test Street", savedDelivery.getDeliveryAddress());
        assertEquals("Medicine a", savedDelivery.getActivePrescription().getPrescription().getMedicineName());
    }


    @Test
    void testGetDeliveryInformationByUser() throws SQLException {
        List<DeliveryInformation> deliveries = repo.getDeliveryInformationByUser(userId);
        assertNotNull(deliveries);
        assertFalse(deliveries.isEmpty());

        DeliveryInformation delivery = deliveries.get(0);
        
        // General checks
        assertEquals("21 Apple Street", delivery.getDeliveryAddress()); // Address check

        // Calculate days since the order was placed
        LocalDate orderDate = new java.sql.Date(delivery.getOrderDate().getTime()).toLocalDate();
        LocalDate today = LocalDate.now();
        long daysSinceOrder = java.time.temporal.ChronoUnit.DAYS.between(orderDate, today);

        // Validate ActivePrescription details
        ActivePrescription activePrescription = delivery.getActivePrescription();
        assertNotNull(activePrescription);
        assertEquals(1L, activePrescription.getId()); // Check that the active prescription ID is correct

        Prescription prescription = activePrescription.getPrescription();
        assertNotNull(prescription);
        assertEquals("Medicine a", prescription.getMedicineName()); // Prescription medicine name check

        // Check automatic status updates based on days since the order
        if (daysSinceOrder == 0) {
            assertEquals("Ordered", delivery.getDeliveryStatus()); // Day 0 status check
        } else if (daysSinceOrder == 1) {
            assertEquals("Shipped", delivery.getDeliveryStatus()); // Day 1 status check
            assertNotNull(delivery.getShipDate()); // Check ship date is set
        } else if (daysSinceOrder >= 3) {
            assertEquals("Delivered", delivery.getDeliveryStatus()); // Day 3+ status check
            assertNotNull(delivery.getActualDeliveryDate()); // Check delivery date is set
        }
    }

    @Test
    void testUpdateDeliveryStatusToShipped() throws SQLException {
        // Fetch delivery information
        DeliveryInformation delivery = repo.getById(deliveryId);
        assertNotNull(delivery);
    
        // Validate that associated ActivePrescription and Prescription exist
        ActivePrescription activePrescription = delivery.getActivePrescription();
        assertNotNull(activePrescription);
 
        Prescription prescription = activePrescription.getPrescription();
        assertNotNull(prescription);

        // Update the status to "Shipped" and set the ship date to today
        delivery.setDeliveryStatus("Shipped");
        delivery.setShipDate(new Date()); // Set the ship date to the current date

        // Save the updated delivery information
        repo.updateDeliveryInformation(delivery);

        // Fetch the delivery information again to check if the update was successful
        DeliveryInformation updatedDelivery = repo.getById(deliveryId);
        assertNotNull(updatedDelivery);
        assertEquals("Shipped", updatedDelivery.getDeliveryStatus());
        assertNotNull(updatedDelivery.getShipDate()); // Verify ship date is set
    }

    @Test
    void testUpdateDeliveryStatusToDelivered() throws SQLException {
        // Fetch delivery information
        DeliveryInformation delivery = repo.getById(deliveryId);
        assertNotNull(delivery);

        // Validate that associated ActivePrescription and Prescription exist
        ActivePrescription activePrescription = delivery.getActivePrescription();
        assertNotNull(activePrescription);

        Prescription prescription = activePrescription.getPrescription();
        assertNotNull(prescription);

        // Update the status to "Delivered" and set the actual delivery date to today
        delivery.setDeliveryStatus("Delivered");
        delivery.setActualDeliveryDate(new Date()); // Set the actual delivery date to today

        // Save the updated delivery information
        repo.updateDeliveryInformation(delivery);

        // Fetch the delivery information again to check if the update was successful
        DeliveryInformation updatedDelivery = repo.getById(deliveryId);
        assertNotNull(updatedDelivery);
        assertEquals("Delivered", updatedDelivery.getDeliveryStatus());
        assertNotNull(updatedDelivery.getActualDeliveryDate()); // Verify actual delivery date is set
    }




}

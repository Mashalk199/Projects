package au.edu.rmit.sept.webapp.repositories;

import au.edu.rmit.sept.webapp.models.Prescription;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PrescriptionRepositoryTest {

    @Autowired
    PrescriptionRepository repo;

    @Autowired
    private Flyway flyway;

    Long userId = 2L;
    Long prescriptionId = 1L;

    @BeforeEach
    void setUp() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void testGetPrescriptionById() {
        try {
            Prescription prescription = repo.getPrescriptionById(prescriptionId);
            assertNotNull(prescription);
            assertEquals("Medicine a", prescription.getMedicineName());
            assertEquals("100mg", prescription.getDosage());
            assertEquals("Twice daily", prescription.getFrequency());
        } catch (SQLException e) {
            assertTrue(false);
        }
        
    }

    @Test
    void testGetPrescriptionsByUserId() {
        try {
            List<Prescription> prescriptions = repo.getPrescriptionsByUserId(userId);
            assertNotNull(prescriptions);
            assertFalse(prescriptions.isEmpty());
            assertEquals("Medicine b", prescriptions.get(0).getMedicineName()); // Check the first prescription
        } catch (SQLException e) {
            assertTrue(false);
        }
        
    }
}

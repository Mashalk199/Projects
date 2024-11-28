package au.edu.rmit.sept.webapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.Prescription;
import au.edu.rmit.sept.webapp.repositories.PrescriptionRepository;

@SpringBootTest
public class PrescriptionServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @InjectMocks
    private PrescriptionService prescriptionService;

    @Test
    void testGetPrescriptionById() throws Exception {
        // Create valid prescription
        Long prescriptionId = 1L;
        Prescription mockPrescription = new Prescription();
        mockPrescription.setId(prescriptionId);
        mockPrescription.setMedicineName("Test Medicine");

        // Retrieve prescriptions from Mocked DB
        when(prescriptionRepository.getPrescriptionById(prescriptionId)).thenReturn(mockPrescription);

        // Checks that the service returned the correct prescriptions
        Prescription prescription = prescriptionService.getPrescriptionById(prescriptionId);
        assertNotNull(prescription);
        assertEquals("Test Medicine", prescription.getMedicineName()); // check that the name matches
    }

    @Test
    void testGetPrescriptionsByUserId() throws Exception {
        Long userId = 2L;

        // Create a list of prescriptions
        List<Prescription> mockPrescriptions = new ArrayList<>();
        Prescription prescription = new Prescription();
        prescription.setId(1L);
        prescription.setMedicineName("User Medicine");
        prescription.setDosage("100mg");
        prescription.setFrequency("2 times daily");

        Pet pet = new Pet();
        pet.setName("User Pet");
        prescription.setPet(pet);

        mockPrescriptions.add(prescription);

        // Mock the repository call
        when(prescriptionRepository.getPrescriptionsByUserId(userId)).thenReturn(mockPrescriptions);

        // Call the service method
        List<Prescription> prescriptions = prescriptionService.getPrescriptionsByUserId(userId);

        // Validate the results
        assertNotNull(prescriptions);
        assertEquals(1, prescriptions.size());
        assertEquals("User Medicine", prescriptions.get(0).getMedicineName());
        assertEquals("User Pet", prescriptions.get(0).getPet().getName());
    }
}

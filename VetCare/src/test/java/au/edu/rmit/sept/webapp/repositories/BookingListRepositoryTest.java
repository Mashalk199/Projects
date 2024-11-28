package au.edu.rmit.sept.webapp.repositories;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.text.ParseException;

import org.junit.jupiter.api.BeforeEach;
import org.flywaydb.core.Flyway;
import org.javatuples.Septet;
import org.javatuples.Sextet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.models.MedicalRecords;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.Vaccinations;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest

public class BookingListRepositoryTest {
    
    @Autowired
    private Flyway flyway;

    @Autowired
    BookingListRepository repo;

    Long userId = 1L;
    Long vetId = 1L;

    List<Appointment> appointments = new ArrayList<>();

    @BeforeEach
    public void setUp() throws ParseException, SQLException {

        // Clean database before every test
        flyway.clean();
        flyway.migrate();

    }
    @Test
    void testGetUserAppointments() {
        // Create a mock appointment
        Appointment appointment = new Appointment(7L, 5L, 3L, userId, 2L, "2024-09-25", "17:30");
        
        // Create mock data
        List<Appointment> expectedAppointments = new ArrayList<>(List.of(appointment));
        List<String> expectedPetNames = new ArrayList<>(List.of("Dobby"));
        List<String> expectedVetNames = new ArrayList<>(List.of("Amanda Gray"));
        List<String> expectedClinicNames = new ArrayList<>(List.of("Beta Pet Clinic"));
        List<String> expectedClinicAddresses = new ArrayList<>(List.of("456 B Street, B Town"));
        List<Float> expectedPrices = new ArrayList<>(List.of(130.0f));
    
        Sextet<List<Appointment>, List<String>, List<String>, List<String>, List<String>, List<Float>> actualData = repo.getUserAppointments(userId);
    
        // Verify the appointment details
        assertEquals(expectedAppointments.size(), actualData.getValue0().size(), "Number of appointments should match");
        for (int i = 0; i < expectedAppointments.size(); i++) {
            Appointment expectedAppointment = expectedAppointments.get(i);
            Appointment actualAppointment = actualData.getValue0().get(i);
            
            // Compare the fields of Appointment
            assertEquals(expectedAppointment.getAppointment_id(), actualAppointment.getAppointment_id(), "Appointment ID should match");
            assertEquals(expectedAppointment.getPet_id(), actualAppointment.getPet_id(), "Pet ID should match");
            assertEquals(expectedAppointment.getVet_id(), actualAppointment.getVet_id(), "Vet ID should match");
            assertEquals(expectedAppointment.getUser_id(), actualAppointment.getUser_id(), "User ID should match");
            assertEquals(expectedAppointment.getClinic_id(), actualAppointment.getClinic_id(), "Clinic ID should match");
            assertEquals(expectedAppointment.getAppointment_date(), actualAppointment.getAppointment_date(), "Appointment date should match");
            assertEquals(expectedAppointment.getAppointment_time(), actualAppointment.getAppointment_time(), "Appointment time should match");
        }
    
        // Verify other fields
        assertEquals(expectedPetNames, actualData.getValue1(), "Pet names should match");
        assertEquals(expectedVetNames, actualData.getValue2(), "Vet names should match");
        assertEquals(expectedClinicNames, actualData.getValue3(), "Clinic names should match");
        assertEquals(expectedClinicAddresses, actualData.getValue4(), "Clinic addresses should match");
        assertEquals(expectedPrices, actualData.getValue5(), "Prices should match");
    }
    @Test
    void testGetVetAppointments() {
        // Create a mock appointment
        Appointment appointment1 = new Appointment(1L, 3L, vetId, 3L, 1L, "2025-05-12", "12:00");
        Appointment appointment2 = new Appointment(2L, 3L, vetId, 3L, 1L, "2025-05-12", "12:30");

        // Create mock data
        List<Appointment> expectedAppointments = new ArrayList<>(List.of(appointment1, appointment2));
        List<String> expectedPetNames = new ArrayList<>(List.of("Augustus","Augustus"));
        List<String> expectedVetNames = new ArrayList<>(List.of("Samantha Whitley", "Samantha Whitley"));
        List<String> expectedClinicNames = new ArrayList<>(List.of("Alpha Pet Clinic", "Alpha Pet Clinic"));
        List<String> expectedClinicAddresses = new ArrayList<>(List.of("123 A Street, A City", "123 A Street, A City"));
        List<Float> expectedPrices = new ArrayList<>(List.of(125.0f, 125f));
        List<String> expectedPatientNames = new ArrayList<>(List.of("Mx Candlestickermaker", "Mx Candlestickermaker"));

        Septet<List<Appointment>, List<String>, List<String>, List<String>, List<String>, List<Float>, List<String>> realData = 
            repo.getVetAppointments(vetId, userId);

        // Compare appointments manually
        assertEquals(expectedAppointments.size(), realData.getValue0().size(), "Number of appointments should match");
        for (int i = 0; i < expectedAppointments.size(); i++) {
            Appointment expectedAppointment = expectedAppointments.get(i);
            Appointment actualAppointment = realData.getValue0().get(i);

            assertEquals(expectedAppointment.getAppointment_id(), actualAppointment.getAppointment_id(), "Appointment ID should match");
        }

        // Compare other fields directly
        assertEquals(expectedPetNames, realData.getValue1(), "Pet names should match");
        assertEquals(expectedVetNames, realData.getValue2(), "Vet names should match");
        assertEquals(expectedClinicNames, realData.getValue3(), "Clinic names should match");
        assertEquals(expectedClinicAddresses, realData.getValue4(), "Clinic addresses should match");
        assertEquals(expectedPrices, realData.getValue5(), "Prices should match");
        assertEquals(expectedPatientNames, realData.getValue6(), "Patient names should match");
    }

}
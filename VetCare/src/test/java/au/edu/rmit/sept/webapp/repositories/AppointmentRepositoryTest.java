package au.edu.rmit.sept.webapp.repositories;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.text.DateFormat;
import java.text.ParseException;

import org.junit.jupiter.api.BeforeEach;
import org.flywaydb.core.Flyway;
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
class AppointmentRepositoryTest {
    

    @Autowired
    private Flyway flyway;

    @Autowired
    AppointmentRepository repo;

    Long userId = 1L;

    List<Appointment> appointments = new ArrayList<>();

    @BeforeEach
    public void setUp() throws ParseException, SQLException {

        // Clean database before every test
        flyway.clean();
        flyway.migrate();

        // Create appointment to test
        Appointment mockAppointment = new Appointment(1L, 2L, 2L, userId, 1L, "09-11-2024", "10:30");

        // Add mock appointment to list of appointments
        appointments.add(mockAppointment);

    }
    @Test
    void testBookAppointment() {
        // Create a mock appointment
        Appointment appointment = new Appointment(1L, 2L, 2L, 1L, 1L, "2024-11-09", "10:30");

        // Book the appointment 
        repo.bookAppointment(appointment);

        // Fetch all booked appointments for a specific date, clinic, and vet
        LocalDate appointmentDate = LocalDate.parse("2024-11-09");
        List<LocalTime> bookedTimes = repo.getAllBookedAppointments(appointmentDate, 1, 2);

        // Assert the appointment is in the list of booked times
        assertEquals(1, bookedTimes.size());
        assertEquals(LocalTime.parse("10:30"), bookedTimes.get(0));
    }

    @Test
    void testGetUserPetsNamesIds() {
        // Assume the database already has a user with pets (you might need to insert test data for this)
        Long userId = 2L;

        // Fetch pets' names and IDs
        Pair<List<String>, List<Integer>> pets = repo.getUserPetsNamesIds(userId);


        assertNotNull(pets);
        assertFalse(pets.getLeft().isEmpty());
        assertFalse(pets.getRight().isEmpty());

        // Check if correct pets are retrieved
        assertEquals("Bob Jr", pets.getLeft().get(0)); 
        assertEquals(2, pets.getRight().get(0));
        assertEquals("Katrina", pets.getLeft().get(1)); 
        assertEquals(4, pets.getRight().get(1));
    }
    @Test
    void testGetClinicsDetails() {
        // Fetch clinic details
        Triple<List<String>, List<Integer>, List<Float>> clinics = repo.getClinicsDetails();

        assertNotNull(clinics);
        assertFalse(clinics.getLeft().isEmpty());
        assertFalse(clinics.getMiddle().isEmpty());
        assertFalse(clinics.getRight().isEmpty());

        // Check if correct clinics are retrieved
        assertEquals("Alpha Pet Clinic", clinics.getLeft().get(0));
        assertEquals(1, clinics.getMiddle().get(0)); 
        assertEquals(125f, clinics.getRight().get(0)); 
    }
    @Test
    void testGetVetNamesClinicIds() {
        // Fetch vet names and clinic IDs
        Triple<List<String>, List<Integer>, List<Integer>> vets = repo.getVetNamesClinicIds();

        assertNotNull(vets);
        assertFalse(vets.getLeft().isEmpty());
        assertFalse(vets.getMiddle().isEmpty());
        assertFalse(vets.getRight().isEmpty());

        // Checks the correct vet and corresponding clinic ID has been retrieved
        assertEquals("Samantha Whitley", vets.getLeft().get(0));
        assertEquals(1, vets.getMiddle().get(0));
        // Asserts that the clinic associated with Samantha is the first one
        assertEquals(1, vets.getRight().get(0)); 
    }
    @Test
    void testRescheduleAppointment() {
        // Create and book an appointment
        Appointment appointment = new Appointment(10L, 2L, 2L, 1L, 1L, "2024-11-09", "10:30");
        repo.bookAppointment(appointment);
        
        // Reschedule the appointment
        appointment.setAppointment_date("2024-11-10");
        appointment.setAppointment_time("14:00");
        repo.rescheduleAppointment(appointment);

        // Fetch all booked appointments for the new date
        LocalDate appointmentDate = LocalDate.parse("2024-11-10");
        List<LocalTime> bookedTimes = repo.getAllBookedAppointments(appointmentDate, 1, 2);

        // Assert the appointment is now scheduled for the new time
        assertEquals(1, bookedTimes.size());
        assertEquals(LocalTime.parse("14:00"), bookedTimes.get(0));
    }

}
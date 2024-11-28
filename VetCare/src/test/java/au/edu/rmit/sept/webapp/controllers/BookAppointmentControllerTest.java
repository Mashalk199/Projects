package au.edu.rmit.sept.webapp.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.services.AppointmentService;
import au.edu.rmit.sept.webapp.services.GetCurrentUserIDService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class BookAppointmentControllerTest {

    private static String URL = "/bookAppointment";

    @Autowired
    private MockMvc mvc;

    @MockBean
    AppointmentService service;
    @MockBean
    private GetCurrentUserIDService getCurrentUserIDService;

    @Autowired
    private BookAppointmentController controller;

    // Create appointment entity before all tests
    static List<Appointment> appointmentsTest = new ArrayList<>();
    static List<LocalTime> bookedTimesTest = new ArrayList<>();
    static int selectedVet;
    static int selectedClinic;

    @BeforeAll
    public static void setup() {
        // Set selected vet and clinic
        selectedVet = 1;
        selectedClinic = 1;
        // Create Appointments
        Appointment appointment1Test = new Appointment(
                1L,
                101L,
                1L,
                301L,
                1L,
                "2024-10-25",
                "16:00");
        Appointment appointment2Test = new Appointment(
                2L,
                102L,
                1L,
                302L,
                1L,
                "2024-10-25",
                "10:00");
        appointmentsTest.add(appointment1Test);
        appointmentsTest.add(appointment2Test);
        bookedTimesTest.add(LocalTime.parse(appointment1Test.getAppointment_time()));
        bookedTimesTest.add(LocalTime.parse(appointment2Test.getAppointment_time()));

    }

    // Test that the context is creating the controller
    @Test
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }
    @Test
    void shouldDisplayTitle() throws Exception {
        // Mock current user ID
        when(getCurrentUserIDService.getCurrentUserID()).thenReturn(1);

        // Mock pets data
        when(service.getUserPetsNamesIds(1L)).thenReturn(Pair.of(List.of("Pet 1", "Pet 2"), List.of(1, 2)));

        // Mock clinic details
        when(service.getClinicsDetails()).thenReturn(Triple.of(List.of("Clinic Alpha", "Clinic Beta"), List.of(1, 2), List.of(50.00f, 75.50f)));

        // Mock vet details
        when(service.getVetNamesClinicIds()).thenReturn(Triple.of(List.of("Vet 1", "Vet 2"), List.of(1, 2), List.of(1, 2)));

        // Perform request and verify output
        mvc.perform(get(URL))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Book appointment")));
    }
    

    // If a time is booked, do not display it
    @Test
    void shouldNotDisplayBookedAppointmentTimes() throws Exception {
        when(this.service.getAllBookedAppointments(
                LocalDate.parse("2024-10-25"), selectedClinic, selectedVet)).thenReturn(bookedTimesTest);

        // Perform request to /available-times and verify that certain booked times are NOT in the response
        MvcResult result = mvc.perform(get(URL + "/available-times")
                .param("date", "2024-10-25")
                .param("clinic", "1")
                .param("vet", "1"))
                .andExpect(status().isOk())  // Check the status is OK
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        // Ensure that the booked times are not in the available times
        assertThat(responseBody).doesNotContain("16:00");
        assertThat(responseBody).doesNotContain("10:00");

        System.out.println("Response Body: " + responseBody);
    }
    @Test
    void shouldDisplayAvailableAppointmentTimes() throws Exception {
        when(this.service.getAllBookedAppointments(
                LocalDate.parse("2024-10-25"), selectedClinic, selectedVet)).thenReturn(bookedTimesTest);

        // Perform request to /available-times and verify that certain booked times are NOT in the response
        MvcResult result = mvc.perform(get(URL + "/available-times")
                .param("date", "2024-10-25")
                .param("clinic", "1")
                .param("vet", "1"))
                .andExpect(status().isOk())  // Check the status is OK
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        // Ensure that the available times except for booked times of 16:00 and 10:00 are in the displayed times
        assertThat(responseBody).contains("09:00");
        assertThat(responseBody).contains("09:30");
        assertThat(responseBody).contains("10:30");
        assertThat(responseBody).contains("11:00");
        assertThat(responseBody).contains("11:30");
        assertThat(responseBody).contains("12:00");
        assertThat(responseBody).contains("12:30");
        assertThat(responseBody).contains("13:00");
        assertThat(responseBody).contains("13:30");
        assertThat(responseBody).contains("14:00");
        assertThat(responseBody).contains("14:30");
        assertThat(responseBody).contains("15:00");
        assertThat(responseBody).contains("15:30");
        assertThat(responseBody).contains("16:30");
        

        System.out.println("Response Body: " + responseBody);
    }


    @Test
    void shouldDisplayAllBookingOptions() throws Exception {
        // Mock the service call to return Pair of test data
        List<String> testPetNames = List.of("Pet 1", "Pet 2");
        List<Integer> testPetIds = List.of(1, 2);
        when(this.service.getUserPetsNamesIds(1L)).thenReturn(Pair.of(testPetNames, testPetIds));
        // Mock the current user ID
        when(getCurrentUserIDService.getCurrentUserID()).thenReturn(1);
        // Gets all appointments for 2024-10-25, the date when test appointments were
        // booked
        when(this.service.getAllBookedAppointments(
                LocalDate.parse("2024-10-25"), selectedClinic, selectedVet)).thenReturn(bookedTimesTest);

        // Mock clinic details
        when(service.getClinicsDetails()).thenReturn(Triple.of(List.of("Clinic Alpha", "Clinic Beta"), List.of(1, 2), List.of(50.00f, 75.50f)));

        // Mock vet details
        when(service.getVetNamesClinicIds()).thenReturn(Triple.of(List.of("Vet 1", "Vet 2"), List.of(1, 2), List.of(1, 2)));

        MvcResult result = mvc.perform(get(URL)) 
            .andExpect(status().isOk()) 
            .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).contains("Pet 1");
        assertThat(responseBody).contains("Pet 2");
        assertThat(responseBody).contains("Vet 1");
        assertThat(responseBody).contains("Vet 2");
        assertThat(responseBody).contains("Clinic Alpha $50.00/hour");
        assertThat(responseBody).contains("Clinic Beta $75.50/hour");

        System.out.println("Response Body: " + responseBody);
    }

    @Test
    void shouldDisplayNoPet() throws Exception {
        // Mock empty set of pets
        List<String> testPetNames = new ArrayList<>();
        List<Integer> testPetIds = new ArrayList<>();
        when(this.service.getUserPetsNamesIds(1L)).thenReturn(Pair.of(testPetNames, testPetIds));
        // Mock the current user ID
        when(getCurrentUserIDService.getCurrentUserID()).thenReturn(1);
        // Gets all appointments for 2024-10-25, the date when test appointments were
        // booked
        when(this.service.getAllBookedAppointments(
                LocalDate.parse("2024-10-25"), selectedClinic, selectedVet)).thenReturn(bookedTimesTest);

        // Mock clinic details
        when(service.getClinicsDetails()).thenReturn(Triple.of(List.of("Clinic Alpha", "Clinic Beta"), List.of(1, 2), List.of(50.00f, 75.50f)));

        // Mock vet details
        when(service.getVetNamesClinicIds()).thenReturn(Triple.of(List.of("Vet 1", "Vet 2"), List.of(1, 2), List.of(1, 2)));

        MvcResult result = mvc.perform(get(URL)) 
            .andExpect(status().isOk()) 
            .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).contains("No Pet");

    }
}
package au.edu.rmit.sept.webapp.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.ArrayList;

import org.javatuples.Quintet;
import org.javatuples.Septet;
import org.javatuples.Sextet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.services.BookingListService;
import au.edu.rmit.sept.webapp.services.GetCurrentUserIDService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class ViewBookingsControllerTest {

    private static String URL = "/viewBookings";

    @Autowired
    private MockMvc mvc;

    @MockBean
    BookingListService service;

    @MockBean
    GetCurrentUserIDService getCurrentUserIDService;

    @Autowired
    private ViewBookingsController controller;

    // Create appointment entity before all tests
    List<Appointment> appointmentsTest = new ArrayList<>();
    List<String> petNamesTest = new ArrayList<>();
    List<String> vetNamesTest = new ArrayList<>();
    List<String> clinicNamesTest = new ArrayList<>();
    List<String> clinicAddressesTest = new ArrayList<>();
    List<Float> prices = new ArrayList<>();
    List<String> patient_names = new ArrayList<>();

    @BeforeEach
    public  void setup() {
        // Create Mock Appointments
        Appointment appointment1Test = new Appointment(
                100L,
                2L,
                4L,
                2L,
                2L,
                "2024-10-25",
                "17:00");
        Appointment appointment2Test = new Appointment(
                200L,
                4L,
                1L,
                2L,
                1L,
                "2024-09-25",
                "16:00");
        appointmentsTest.add(appointment1Test);
        appointmentsTest.add(appointment2Test);

        // Mock pet and vet names
        petNamesTest.add("Bob jr");
        petNamesTest.add("Katrina");

        vetNamesTest.add("Benjamin Carter");
        vetNamesTest.add("Samantha Whitley");

        clinicNamesTest.add("Beta Pet Clinic");
        clinicNamesTest.add("Alpha Pet Clinic");

        clinicAddressesTest.add("456 B Street, B Town");
        clinicAddressesTest.add("123 A Street, A City");

        prices.add(130.0f);
        prices.add(125.0f);

        patient_names.add("Bob");
        patient_names.add("John");
    }

    // Test that the context is creating the controller
    @Test
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    void shouldDisplayTitle() throws Exception {
        // Mock user ID
        when(getCurrentUserIDService.getCurrentUserID()).thenReturn(3);
        when(getCurrentUserIDService.getCurrentUserVetID()).thenReturn(null);

        // Mock the service to return the test appointments
        when(service.getUserAppointments(3L)).thenReturn(
            Sextet.with(appointmentsTest, petNamesTest, vetNamesTest, clinicNamesTest, clinicAddressesTest, prices)
        );

        mvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Bookings List")));
    }

    // Test that page is correctly displaying appointment bookings for patient user
    @Test
    void shouldDisplayListOfBookingsForPatient() throws Exception {
        // Mock user ID
        when(getCurrentUserIDService.getCurrentUserID()).thenReturn(3);
        when(getCurrentUserIDService.getCurrentUserVetID()).thenReturn(null);

        // Mock the service to return the test appointments
        when(service.getUserAppointments(3L)).thenReturn(
            Sextet.with(appointmentsTest, petNamesTest, vetNamesTest, clinicNamesTest, clinicAddressesTest, prices)
        );

        mvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Booking ID: <span>100</span>")))
                .andExpect(content().string(containsString("Pet Name: <span>Bob jr</span>")))
                .andExpect(content().string(containsString("Vet Name: <span>Benjamin Carter</span>")))
                .andExpect(content().string(containsString("Clinic Name: <span>Beta Pet Clinic</span>")))
                .andExpect(content().string(containsString("Clinic Address: <span>456 B Street, B Town</span>")))
                .andExpect(content().string(containsString("Date: <span>2024-10-25</span>")))
                .andExpect(content().string(containsString("Time: <span>17:00</span>")))
                .andExpect(content().string(containsString("Price: $<span>65.0</span>")))
                // Assert second booking is displayed as well
                .andExpect(content().string(containsString("Booking ID: <span>200</span>")))
                .andExpect(content().string(containsString("Pet Name: <span>Katrina</span>")))
                .andExpect(content().string(containsString("Vet Name: <span>Samantha Whitley</span>")))
                .andExpect(content().string(containsString("Clinic Name: <span>Alpha Pet Clinic</span>")))
                .andExpect(content().string(containsString("Clinic Address: <span>123 A Street, A City</span>")))
                .andExpect(content().string(containsString("Date: <span>2024-09-25</span>")))
                .andExpect(content().string(containsString("Time: <span>16:00</span>")))
                .andExpect(content().string(containsString("Price: $<span>62.5</span>")));
    }

    // Test that page is correctly displaying appointment bookings for vet user
    @Test
    void shouldDisplayListOfBookingsForVet() throws Exception {
        // Mock user ID
        when(getCurrentUserIDService.getCurrentUserID()).thenReturn(3);
        when(getCurrentUserIDService.getCurrentUserVetID()).thenReturn(4L);

        // Mock the service to return the test appointments that have been booked with this vet
        when(service.getVetAppointments(4L,3L)).thenReturn(
            Septet.with(appointmentsTest, petNamesTest, vetNamesTest, clinicNamesTest, clinicAddressesTest, prices, patient_names)
        );

        // Mock the service to return no user appointments
        when(service.getUserAppointments(3L)).thenReturn(
            Sextet.with(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
        );

        mvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Booking ID: <span>100</span>")))
                .andExpect(content().string(containsString("Patient Name: <span>Bob</span>")))
                .andExpect(content().string(containsString("Pet Name: <span>Bob jr</span>")))
                .andExpect(content().string(containsString("Vet Name: <span>Benjamin Carter</span>")))
                .andExpect(content().string(containsString("Clinic Name: <span>Beta Pet Clinic</span>")))
                .andExpect(content().string(containsString("Clinic Address: <span>456 B Street, B Town</span>")))
                .andExpect(content().string(containsString("Date: <span>2024-10-25</span>")))
                .andExpect(content().string(containsString("Time: <span>17:00</span>")))
                .andExpect(content().string(containsString("Price: $<span>65.0</span>")))
                // Assert second booking is displayed as well
                .andExpect(content().string(containsString("Booking ID: <span>200</span>")))
                .andExpect(content().string(containsString("Patient Name: <span>John</span>")))
                .andExpect(content().string(containsString("Pet Name: <span>Katrina</span>")))
                .andExpect(content().string(containsString("Vet Name: <span>Samantha Whitley</span>")))
                .andExpect(content().string(containsString("Clinic Name: <span>Alpha Pet Clinic</span>")))
                .andExpect(content().string(containsString("Clinic Address: <span>123 A Street, A City</span>")))
                .andExpect(content().string(containsString("Date: <span>2024-09-25</span>")))
                .andExpect(content().string(containsString("Time: <span>16:00</span>")))
                .andExpect(content().string(containsString("Price: $<span>62.5</span>")));
        MvcResult result = mvc.perform(get(URL))
            .andExpect(status().isOk()) 
            .andReturn();

        // String responseBody = result.getResponse().getContentAsString();

        // System.out.println("Response Body: " + responseBody);
        }
    @Test
    void shouldDisplayNoBookingsWhenThereAreNone() throws Exception {
        // Mock user ID and vet ID
        when(getCurrentUserIDService.getCurrentUserID()).thenReturn(4);
        when(getCurrentUserIDService.getCurrentUserVetID()).thenReturn(4L);

        // Mock service to return no bookings for both vet and user appointments
        when(service.getVetAppointments(4L,4L)).thenReturn(
            Septet.with(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
        );
        when(service.getUserAppointments(4L)).thenReturn(
            Sextet.with(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
        );

        // Verify the "No bookings" message is displayed
        mvc.perform(get("/viewBookings"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("No appointments")));
    }
    @Test
    void shouldDisplayVetOwnBookingWithoutDuplication() throws Exception {
        // Mock vet user ID and vet ID
        when(getCurrentUserIDService.getCurrentUserID()).thenReturn(4); 
        when(getCurrentUserIDService.getCurrentUserVetID()).thenReturn(4L); 

        // Create an appointment where the vet has booked with themselves
        Appointment vetAppointment = new Appointment(1L, 2L, 2L, 4L, 1L, 
        "09-11-2024", "10:30");
        
        // Ensure all mocked lists are mutable by using new ArrayList<>()
        List<Appointment> vetAppointments = new ArrayList<>(List.of(vetAppointment));
        List<String> petNamesTest = new ArrayList<>(List.of("Bob jr"));
        List<String> vetNamesTest = new ArrayList<>(List.of("Jonathan Rhodes"));
        List<String> clinicNamesTest = new ArrayList<>(List.of("Alpha Pet Clinic"));
        List<String> clinicAddressesTest = new ArrayList<>(List.of("123 A Street, A City"));
        List<Float> prices = new ArrayList<>(List.of(65.0f));
        List<String> namesTest = new ArrayList<>(List.of("Bob"));

        // Mock the service to return the vet's own appointment and patient appointments (including the vet's appointment)
        when(service.getVetAppointments(4L,4L)).thenReturn(
            Septet.with(vetAppointments, petNamesTest, vetNamesTest, clinicNamesTest, clinicAddressesTest, prices, namesTest)
        );

        // Mock the service to return user appointments as well (which excludes the same appointment)
        when(service.getUserAppointments(4L)).thenReturn(
            Sextet.with(new ArrayList<Appointment>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(), new ArrayList<Float>())
        );

         // Perform request and capture the response body
        MvcResult result = mvc.perform(get(URL))
                .andExpect(status().isOk())
                // Verify that the booking appears at least somewhere on the page
                .andExpect(content().string(containsString("Booking ID: <span>1</span>")))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        // Count occurrences of booking ID in the response body
        int occurrenceCount = StringUtils.countOccurrencesOf(responseBody, "Booking ID: <span>1</span>");

        // Assert that the booking only appears once
        assertEquals(1, occurrenceCount, "The booking should appear only once in the page");

    }


}

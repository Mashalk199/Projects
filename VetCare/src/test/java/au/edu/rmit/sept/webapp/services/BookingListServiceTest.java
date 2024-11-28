package au.edu.rmit.sept.webapp.services;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.ArrayList;

import org.javatuples.Septet;
import org.javatuples.Sextet;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.repositories.BookingListRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingListServiceTest {

    @MockBean
    BookingListService service;

    @Test
    void testGetUserAppointments() throws Exception {

        // given
        Long userId = 2L;
        Appointment mockAppointment = new Appointment(1L, 2L, 2L, userId, 1L, "09-11-2024", "10:30");
        Appointment mockAppointment2 = new Appointment(2L, 2L, 2L, userId, 1L, "09-11-2024", "11:00");
        List<Appointment> appointmentList = new ArrayList<>();
        appointmentList.add(mockAppointment);
        appointmentList.add(mockAppointment2);

        List<String> mock_pet_names = List.of("Bob jr", "Bob jr");
        List<String> mock_vet_names = List.of("Jonathan Rhodes", "Jonathan Rhodes");
        List<String> mock_clinic_names = List.of("Alpha Pet Clinic", "Alpha Pet Clinic");
        List<String> mock_clinic_addresses = List.of("123 A Street, A City", "123 A Street, A City");
        List<Float> mock_prices = List.of(125f, 125f);

        Sextet sextet = Sextet.with(appointmentList, mock_pet_names, mock_vet_names, mock_clinic_names, mock_clinic_addresses, mock_prices);
        

        // when
        BookingListRepository mockR = mock(BookingListRepository.class);
        BookingListService bookingListService = new BookingListService(mockR);
        when(mockR.getUserAppointments(userId)).thenReturn(sextet);

        // then
        assertTrue(bookingListService.getUserAppointments(userId) == sextet);
    }

    @Test
    void testGetVetAppointments() throws Exception {

        // given
        Long userId = 3L;
        Long vetId = 2L;
        Appointment mockAppointment = new Appointment(1L, 2L, 2L, userId, 1L, "09-11-2024", "10:30");
        Appointment mockAppointment2 = new Appointment(2L, 2L, 2L, userId, 1L, "09-11-2024", "11:00");
        List<Appointment> vetAppointmentList = new ArrayList<>();
        vetAppointmentList.add(mockAppointment);
        vetAppointmentList.add(mockAppointment2);

        List<String> mock_pet_names = List.of("Bob jr", "Bob jr");
        List<String> mock_vet_names = List.of("Jonathan Rhodes", "Jonathan Rhodes");
        List<String> mock_clinic_names = List.of("Alpha Pet Clinic", "Alpha Pet Clinic");
        List<String> mock_clinic_addresses = List.of("123 A Street, A City", "123 A Street, A City");
        List<Float> mock_prices = List.of(125f, 125f);
        List<String> mock_names = List.of("Bob", "Bob");

        Septet septet = Septet.with(vetAppointmentList, mock_pet_names, mock_vet_names, mock_clinic_names, mock_clinic_addresses, mock_prices,mock_names);
        

        // when
        BookingListRepository mockR = mock(BookingListRepository.class);
        BookingListService bookingListService = new BookingListService(mockR);
        when(mockR.getVetAppointments(vetId,userId)).thenReturn(septet);

        // then
        assertTrue(bookingListService.getVetAppointments(vetId,userId) == septet);
    }
}
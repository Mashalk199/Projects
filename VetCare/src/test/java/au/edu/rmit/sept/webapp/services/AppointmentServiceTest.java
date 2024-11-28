package au.edu.rmit.sept.webapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.repositories.AppointmentRepository;

class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBookedAppointments() {
        // given
        LocalDate date = LocalDate.of(2024, 11, 9);
        int clinicId = 1;
        int vetId = 2;
        List<LocalTime> mockBookedTimes = List.of(LocalTime.of(10, 30), LocalTime.of(11, 30));

        when(appointmentRepository.getAllBookedAppointments(date, clinicId, vetId)).thenReturn(mockBookedTimes);

        // when
        List<LocalTime> result = appointmentService.getAllBookedAppointments(date, clinicId, vetId);

        // then
        assertEquals(mockBookedTimes, result);
        verify(appointmentRepository, times(1)).getAllBookedAppointments(date, clinicId, vetId);
    }

    @Test
    void testBookAppointment() {
        // given
        Appointment mockAppointment = new Appointment(1L, 2L, 1L, 1L, 2L, "2024-11-09", "10:30");

        // when
        appointmentService.bookAppointment(mockAppointment);

        // then
        verify(appointmentRepository, times(1)).bookAppointment(mockAppointment);
    }

    @Test
    void testBookAppointmentWithoutPet() {
        // given
        Appointment mockAppointment = new Appointment(null, 2L, 1L, 1L, 2L, "2024-11-09", "10:30");

        // when
        appointmentService.bookAppointmentWithoutPet(mockAppointment);

        // then
        verify(appointmentRepository, times(1)).bookAppointmentWithoutPet(mockAppointment);
    }

    @Test
    void testGetUserPetsNamesIds() {
        // given
        Long userId = 1L;
        List<String> mockPetNames = List.of("Dobby", "Dobby");
        List<Integer> mockPetIds = List.of(5, 5);
        Pair<List<String>, List<Integer>> mockResult = Pair.of(mockPetNames, mockPetIds);

        when(appointmentRepository.getUserPetsNamesIds(userId)).thenReturn(mockResult);

        // when
        Pair<List<String>, List<Integer>> result = appointmentService.getUserPetsNamesIds(userId);

        // then
        assertEquals(mockResult, result);
        verify(appointmentRepository, times(1)).getUserPetsNamesIds(userId);
    }

    @Test
    void testGetClinicsDetails() {
        // given
        List<String> mockClinicNames = List.of("Alpha Pet Clinic", "Beta Pet Clinic");
        List<Integer> mockClinicIds = List.of(1, 2);
        List<Float> mockPrices = List.of(125f, 130f);
        Triple<List<String>, List<Integer>, List<Float>> mockResult = Triple.of(mockClinicNames, mockClinicIds, mockPrices);

        when(appointmentRepository.getClinicsDetails()).thenReturn(mockResult);

        // when
        Triple<List<String>, List<Integer>, List<Float>> result = appointmentService.getClinicsDetails();

        // then
        assertEquals(mockResult, result);
        verify(appointmentRepository, times(1)).getClinicsDetails();
    }

    @Test
    void testGetVetNamesClinicIds() {
        // given
        List<String> mockVetNames = List.of("Samantha Whitley", "Amanda Gray");
        List<Integer> mockVetIds = List.of(1, 3);
        List<Integer> mockClinicIds = List.of(1,2);
        Triple<List<String>, List<Integer>, List<Integer>> mockResult = Triple.of(mockVetNames, mockVetIds, mockClinicIds);

        when(appointmentRepository.getVetNamesClinicIds()).thenReturn(mockResult);

        // when
        Triple<List<String>, List<Integer>, List<Integer>> result = appointmentService.getVetNamesClinicIds();

        // then
        assertEquals(mockResult, result);
        verify(appointmentRepository, times(1)).getVetNamesClinicIds();
    }

    @Test
    void testRescheduleAppointment() {
        // given
        Appointment mockAppointment = new Appointment(1L, 2L, 1L, 1L, 2L, "2024-11-09", "10:30");

        // when
        appointmentService.rescheduleAppointment(mockAppointment);

        // then
        verify(appointmentRepository, times(1)).rescheduleAppointment(mockAppointment);
    }
}

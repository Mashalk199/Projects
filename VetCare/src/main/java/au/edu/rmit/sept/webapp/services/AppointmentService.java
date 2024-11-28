package au.edu.rmit.sept.webapp.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.repositories.AppointmentRepository;

@Service
public class AppointmentService {

  private final AppointmentRepository appointmentRepository;

  public AppointmentService(AppointmentRepository appointmentRepository) {
    this.appointmentRepository = appointmentRepository;
  }

  public List<LocalTime> getAllBookedAppointments(LocalDate date, int clinic_id, int vet_id) {
    return appointmentRepository.getAllBookedAppointments(date, clinic_id, vet_id);
  }

  public void bookAppointment(Appointment new_appointment) {
    appointmentRepository.bookAppointment(new_appointment);
  }

  public void bookAppointmentWithoutPet(Appointment new_appointment) {
    appointmentRepository.bookAppointmentWithoutPet(new_appointment);
  }

  public Pair<List<String>, List<Integer>> getUserPetsNamesIds(Long userId) {
    return appointmentRepository.getUserPetsNamesIds(userId);
  }

  public Triple<List<String>, List<Integer>, List<Float>> getClinicsDetails() {
    return appointmentRepository.getClinicsDetails();
  }
  public Triple<List<String>, List<Integer>, List<Integer>> getVetNamesClinicIds() {
    return appointmentRepository.getVetNamesClinicIds();
  }
  public void rescheduleAppointment(Appointment new_appointment) {
    appointmentRepository.rescheduleAppointment(new_appointment);
  }
}

package au.edu.rmit.sept.webapp.services;

import java.util.List;

import org.javatuples.Triplet;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.repositories.BookingListRepository;

import org.javatuples.Octet;
import org.javatuples.Quintet;
import org.javatuples.Septet;
import org.javatuples.Sextet;

@Service
public class BookingListService {

  private final BookingListRepository bookingListRepository;

  public BookingListService(BookingListRepository bookingListRepository) {
    this.bookingListRepository = bookingListRepository;
  }

    public Sextet<List<Appointment>, List<String>, List<String>, List<String>, List<String>, List<Float>> getUserAppointments(Long userId) {
    return bookingListRepository.getUserAppointments(userId);
  }
  public Septet<List<Appointment>, List<String>, List<String>, List<String>, List<String>, 
    List<Float>, List<String>> getVetAppointments(Long vetId, Long user_id) {
    return bookingListRepository.getVetAppointments(vetId, user_id);
  }

  public void cancelAppointment(Long appointment_id) {
    this.bookingListRepository.cancelAppointment(appointment_id);
  }
}
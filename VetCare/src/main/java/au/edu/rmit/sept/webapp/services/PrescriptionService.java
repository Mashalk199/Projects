package au.edu.rmit.sept.webapp.services;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.models.Prescription;
import au.edu.rmit.sept.webapp.repositories.PrescriptionRepository;

@Service
public class PrescriptionService {

  private final PrescriptionRepository prescriptionRepository;

  public PrescriptionService(PrescriptionRepository prescriptionRepository) {
    this.prescriptionRepository = prescriptionRepository;
  }

  public void handleException(SQLException e) {
    System.err.println("SQL request failed.");
    System.err.println("SQL State: " + e.getSQLState());
    System.err.println("Error Code: " + e.getErrorCode());
    System.err.println("Message: " + e.getMessage());
    e.printStackTrace();
  }

  public List<Prescription> getPrescriptionsByPetId(Long petId) {
    try {
      return prescriptionRepository.getPrescriptionsByPetId(petId);
    } 
    catch (SQLException e) {
      handleException(e);
      return null;
    }
  }

  public List<Prescription> getPrescriptionsByUserId(Long userId) {
    try {
      return prescriptionRepository.getPrescriptionsByUserId(userId);
    } 
    catch (SQLException e) {
      handleException(e);
      return null;
    }
  }

  public Prescription getPrescriptionById(Long prescriptionId) {
    try {
      return prescriptionRepository.getPrescriptionById(prescriptionId);
    } 
    catch (SQLException e) {
      handleException(e);
      return null;
    }
  }
  public List<Prescription> getAllPrescriptions() {
    try {
      return prescriptionRepository.getAllPrescriptions();
    } 
    catch (SQLException e) {
      handleException(e);
      return null;
    }
  }
}
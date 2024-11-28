package au.edu.rmit.sept.webapp.models;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "prescriptions")
public class Prescription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String medicineName;
  private String dosage;
  private String frequency;

  //Relations
  private Pet pet;
  private MedicalRecords medicalRecord;

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMedicineName() {
    return medicineName;
  }

  public void setMedicineName(String medicineName) {
    this.medicineName = medicineName;
  }

  public String getDosage() {
    return dosage;
  }

  public void setDosage(String dosage) {
    this.dosage = dosage;
  }

  public String getFrequency() {
    return frequency;
  }

  public void setFrequency(String frequency) {
    this.frequency = frequency;
  }

  public Pet getPet() {
    return pet;
  }

  public void setPet(Pet pet) {
    this.pet = pet;
  }

  public MedicalRecords getMedicalRecord() {
    return medicalRecord;
  }

  public void setMedicalRecord(MedicalRecords medicalRecord) {
    this.medicalRecord = medicalRecord;
  }
}

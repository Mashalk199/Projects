package au.edu.rmit.sept.webapp.models;

import java.util.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "active_prescriptions")
public class ActivePrescription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long prescriptionId;
  private Long petId;
  private String petName; 
  private Date expirationDate;

  private Prescription prescription; //Associated prescription to get static info - med name, dosage and frequency

  //Relations
//   private Pet pet;
//   private MedicalRecords medicalRecord;
//   private Prescription prescription;


  // Getters and Setters
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getPrescriptionId() {
        return prescriptionId;
    }
    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Prescription getPrescription() {
        return this.prescription;
    }
    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public Long getPetId() {
        return this.petId;
    }
    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

  public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }
  
// MIGHT include later
//   public Date getIssueDate() {
//     return issueDate;
//   }

//   public void setIssueDate(Date issueDate) {
//     this.issueDate = issueDate;
//   }

}

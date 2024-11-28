package au.edu.rmit.sept.webapp.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;

@Entity
@Table(name = "medical_records")
public class MedicalRecords {

    @Id
    @GeneratedValue
    private Long record_id;
    private Long pet_id;    //Fk
    private Long clinic_id; //FK
    private String clinic_name;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date date;

    private Long activePrescriptionId; //FK
    private Long prescriptionId;
    private String medicine_name; 
    private String description; 
    private String diagnosis;
    private String treatment_plan;

    public MedicalRecords() {
    }

    public MedicalRecords(Long record_id, Long pet_id, Long clinic_id, String clinic_name, Date date, String description
                            ,Long activePrescriptionId, Long prescriptionId, String medicine_name, String diagnosis, String treatment_plan) {
        this.record_id = record_id;
        this.pet_id = pet_id;
        this.clinic_id = clinic_id;
        this.clinic_name = clinic_name;
        this.date = date;
        this.description = description;
        this.activePrescriptionId = activePrescriptionId;
        this.prescriptionId = prescriptionId;
        this.medicine_name = medicine_name;
        this.diagnosis = diagnosis;
        this.treatment_plan = treatment_plan;
    }

    // Getters and Setters
    public Long getRecordId() {
        return record_id;
    }

    public void setRecordId(Long record_id) {
        this.record_id = record_id;
    }

    public Long getPetId() {
        return pet_id;
    }

    public void setPetId(Long pet_id) {
        this.pet_id = pet_id;
    }

    public Long getClinicId() {
        return clinic_id;
    }

    public void setClinicId(Long clinic_id) {
        this.clinic_id = clinic_id;
    }

    public String getClinicName() {
        return clinic_name;
    }

    public void setClinicName(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public Long getActivePrescriptionId() {
        return activePrescriptionId;
    }

    public void setActivePrescriptionId(Long activePrescriptionId) {
        this.activePrescriptionId = activePrescriptionId;
    }

    public Long getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Long prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getMedicineName() {
        return medicine_name;
    }

    public void setMedicineName(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment_plan;
    }

    public void setTreatment(String treatment_plan) {
        this.treatment_plan = treatment_plan;
    }

}
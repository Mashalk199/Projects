package au.edu.rmit.sept.webapp.models;

import jakarta.persistence.*;

@Entity
@Table(name = "clinics")
public class Clinic {

    @Id
    @GeneratedValue
    private Long clinic_id;
    private String clinic_name;
    private String address;

    public Clinic() {
    }

    public Clinic(Long clinic_id, String clinic_name, String address) {
        this.clinic_id = clinic_id;
        this.clinic_name = clinic_name;
        this.address = address;
    }

    // Getters and Setters
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

    public String getClinicAddress() {
        return address;
    }

    public void setClinicAddress(String address) {
        this.address = address;
    }
}

package au.edu.rmit.sept.webapp.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "pets")
public class Vet {

    @Id
    @GeneratedValue
    private Long vet_id;
    private String first_name;
    private String last_name;
    private String phone_number;
    private String email;
    private Long clinic_id;

    public Vet() {
    }

    public Vet(Long vet_id, String first_name, String last_name, String phone_number, String email, Long clinic_id) {
        this.vet_id = vet_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.email = email;
        this.clinic_id = clinic_id;
    }

    // Getters and Setters
    public Long getVetId() {
        return vet_id;
    }

    public void setVetId(Long pet_id) {
        this.vet_id = pet_id;
    }

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }
    public String getLastName() {
        return last_name;
    }

    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getClinicId() {
        return clinic_id;
    }

    public void setClinicId(Long clinic_id) {
        this.clinic_id = clinic_id;
    }

}


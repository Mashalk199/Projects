package au.edu.rmit.sept.webapp.models;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "vaccinations")
public class Vaccinations {

    @Id
    @GeneratedValue
    private Long vaccination_id;
    private String vaccination_name;
    private Long pet_id;
    private Date date;
    private String description;

    public Vaccinations() {
    }

    public Vaccinations(Long vaccination_id, String vaccination_name, Long pet_id, Date date, String description) {
        this.vaccination_id = vaccination_id;
        this.pet_id = pet_id;
        this.date = date;
        this.description = description;
    }

    // Getters and Setters
    public Long getVaccinationId() {
        return vaccination_id;
    }

    public void setVaccinationId(Long vaccination_id) {
        this.vaccination_id = vaccination_id;
    }

    public String getVaccinationName() {
        return vaccination_name;
    }

    public void setVaccinationName(String vaccination_name) {
        this.vaccination_name = vaccination_name;
    }

    public Long getPetId() {
        return pet_id;
    }

    public void setPetId(Long pet_id) {
        this.pet_id = pet_id;
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

}

package au.edu.rmit.sept.webapp.models;

import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "treatment_plan")
public class TreatmentPlan {

    @Id
    @GeneratedValue
    private Long treatment_plan_id;
    private Long pet_id;
    private Long prescription_id;
    private Date date;
    private String description;

    public TreatmentPlan() {
    }

    public TreatmentPlan(Long treatment_plan_id, Long pet_id, Long prescription_id, Date date, String description) {
        this.treatment_plan_id = treatment_plan_id;
        this.pet_id = pet_id;
        this.prescription_id = prescription_id;
        this.date = date;
        this.description = description;
    }

    // Getters and Setters
    public Long getTreatmentPlanId() {
        return treatment_plan_id;
    }

    public void setTreatmentPlanId(Long treatment_plan_id) {
        this.treatment_plan_id = treatment_plan_id;
    }

    public Long getPetId() {
        return pet_id;
    }

    public void setPetId(Long pet_id) {
        this.pet_id = pet_id;
    }

    public Long getPrescriptionId() {
        return prescription_id;
    }

    public void setPrescriptionId(Long prescription_id) {
        this.prescription_id = prescription_id;
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

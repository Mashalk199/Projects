package au.edu.rmit.sept.webapp.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue
    private Long pet_id;
    private Long owner_id;
    private String owner_name;
    private String name;
    private String species;
    private String gender;
    private Long age;

    private List<MedicalRecords> medicalRecords;
    private List<Vaccinations> vaccinations;

    public Pet() {
    }

    public Pet(Long pet_id, Long owner_id, String name, String species, String gender, Long age) {
        this.pet_id = pet_id;
        this.owner_id = owner_id;
        this.name = name;
        this.species = species;
        this.gender = gender;
        this.age = age;
    }

    // Getters and Setters
    public Long getPetId() {
        return pet_id;
    }

    public void setPetId(Long pet_id) {
        this.pet_id = pet_id;
    }

    public Long getOwnerId() {
        return owner_id;
    }

    public void setOwnerId(Long owner_id) {
        this.owner_id = owner_id;
    }
    public String getOwnerName() {
        return owner_name;
    }

    public void setOwnerName(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public List<MedicalRecords> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecord(List<MedicalRecords> medicalRecord) {
        this.medicalRecords = medicalRecord;
    }

    public List<Vaccinations> getVaccinations() {
        return vaccinations;
    }

    public void setVaccinations(List<Vaccinations> vaccinations) {
        this.vaccinations = vaccinations;
    }

}

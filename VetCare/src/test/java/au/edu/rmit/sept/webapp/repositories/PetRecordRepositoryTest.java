package au.edu.rmit.sept.webapp.repositories;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.text.ParseException;

import au.edu.rmit.sept.webapp.models.MedicalRecords;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.Vaccinations;

import org.junit.jupiter.api.BeforeEach;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PetRecordRepositoryTest {

    @Autowired
    private Flyway flyway;

    @Autowired
    PetRecordRepository repo;

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Long userId = 1L;
    Long petId = 5L;
    List<Pet> pets = new ArrayList<>();
    List<MedicalRecords> records = new ArrayList<>();
    List<Vaccinations> vaccinations = new ArrayList<>();
    Pet expectedPet;
    MedicalRecords expectedRecord;
    Vaccinations expectedVaccination;

    @BeforeEach
    public void setUp() throws ParseException, SQLException {

        // Clean database before every test
        flyway.clean();
        flyway.migrate();

        // Create pet to test
        Pet pet = new Pet(5L, 1L, "Dobby", "Cat", "Male", 2L);

        // Create vaccination records
        Date date = format.parse("2022-01-03");
        Vaccinations vaccination_1 = new Vaccinations(6L,"Vaccination F", 5L, date, "Vaccination description");
        vaccinations.add(vaccination_1);

        // Create Medical record
        date = format.parse("2022-05-26");
        MedicalRecords medicalRecord = new MedicalRecords(1L,5L,3L,"Gamma Pet Clinic", date, "test_description", 
                                                            1L, 1L, "Medicine a","Some Illness", "Some Treatment");
        records.add(medicalRecord);

        // Set pet vaccinations and records
        pet.setVaccinations(vaccinations);
        pet.setMedicalRecord(records);

        // Add pet to list of pets
        pets.add(pet);

    }

    @Test
    void testAddPet()  {
        Pet petToAdd = new Pet(999L, 2L, "Oliver", "Lizard", "Female", 12L);
        try {
            repo.createNewPet(petToAdd);
        } catch (SQLException e) {
            assertTrue(false);
        }
    }

    @Test
    void testCreateRecord() throws ParseException{
        Date date = format.parse("2022-01-03");
        MedicalRecords recordToAdd = new MedicalRecords(4L, 999L, 2L, "Gamma Pet Clinic", date, "Obesity", 1L, 2L, "Medicine A", "Exercise and diet changes", "");

        try {
            repo.createRecord(recordToAdd);

            // Reset after deleting record
        } catch (SQLException e) {
            assertTrue(false);
        }
    }

    @Test
    void testGetUserPetsAsEntity() {
        try {
            assertEquals(pets.size(), repo.getUserPetsAsEntity(userId).size());
            assertEquals(pets.get(0).getName(), repo.getUserPetsAsEntity(userId).get(0).getName());
        } catch (SQLException e) {
            assertTrue(false);
        }
    }

    @Test
    void 
    testGetAllRecordsForPet() {

        try {
            assertEquals(records.size(), repo.getAllRecordsForPet(petId).size());
            assertEquals(records.get(0).getRecordId(), repo.getAllRecordsForPet(petId).get(0).getRecordId());
        } catch (SQLException e) {
            assertTrue(false);
        }
    }

    @Test
    void testGetAllVaccinationsForPet() {

        try {
            assertEquals(vaccinations.size(), repo.getAllVaccinationsForPet(petId).size());
            assertEquals(vaccinations.get(0).getVaccinationId(),
                    repo.getAllVaccinationsForPet(petId).get(0).getVaccinationId());
        } catch (SQLException e) {
            assertTrue(false);
        }
    }

    @Test
    void testDeletePet() {
        try {
            repo.deletePetByPetId(1L);

        } catch (SQLException e) {
            assertTrue(false);
        }
    }

    @Test
    void testUpdateExistingPet() {
        Pet petToUpdate = new Pet(5L, 2L, "Oliver", "Lizard", "Female", 12L);
        try {
            repo.updateExistingPet(2L, petToUpdate);

        } catch (SQLException e) {
            assertTrue(false);
        }
    }

    @Test
    void testDeleteRecord() {
        try {
            repo.deleteRecord(1L);

        } catch (SQLException e) {
            assertTrue(false);
        }
    }
    @Test
    void testUpdateRecord() throws ParseException {
        Date date = format.parse("2025-01-03");
        MedicalRecords recordToUpdate = new MedicalRecords(1L,5L,3L,"Gamma Pet Clinic", date, "test_description", 
                                                            1L, 1L, "Medicine a","Some Illness updated", "Some Treatment updated");
        try {
            repo.updateRecord(records.get(0).getRecordId(), recordToUpdate);

        } catch (SQLException e) {
            assertTrue(false);
        }
    }

    @Test
    void testGetAllClinics() {

        try {
            assertEquals(4, repo.getAllClinics().size());
        } catch (SQLException e) {
            assertTrue(false);
        }
    }

}

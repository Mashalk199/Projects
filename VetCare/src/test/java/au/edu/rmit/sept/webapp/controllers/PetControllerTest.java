package au.edu.rmit.sept.webapp.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import au.edu.rmit.sept.webapp.models.Clinic;
import au.edu.rmit.sept.webapp.models.MedicalRecords;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.Vaccinations;
import au.edu.rmit.sept.webapp.services.GetCurrentUserIDService;
import au.edu.rmit.sept.webapp.services.PetRecordService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTest {

    private static String URL = "/records";

    @Autowired
    private MockMvc mvc;

    @MockBean
    PetRecordService service;
    @MockBean
    GetCurrentUserIDService getCurrentUserIDService;


    @Autowired
    private PetController controller;

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Long userId = 1L;
    int currentUser = 1;
    Long petId = 5L;
    List<Pet> pets = new ArrayList<>();
    List<MedicalRecords> records = new ArrayList<>();
    List<Vaccinations> vaccinations = new ArrayList<>();
    List<Clinic> clinics = new ArrayList<>();
    Pet expectedPet;
    MedicalRecords expectedRecord;
    Vaccinations expectedVaccination;

    @BeforeEach
     public void setUp() throws ParseException, SQLException {

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

    // Test that the context is creating the controller
    @Test
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    // Test that the context is creating the controller
    @Test
    void shouldDisplayTitle() throws Exception {
        mvc.perform(get(URL)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Medical Records")));
    }

    // Test that page is correctly displaying pets
    @Test
    void shouldDisplayPets() throws Exception {

        when(this.getCurrentUserIDService.getCurrentUserID()).thenReturn(currentUser);
        when(this.service.getUserPets(userId)).thenReturn(pets);
        when(this.getCurrentUserIDService.getCurrentUserVetID()).thenReturn(null);
        mvc.perform(get(URL)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Dobby")))
                .andExpect(content().string(containsString("Cat")))
                .andExpect(content().string(containsString("Male")))
                .andExpect(content().string(containsString("2")));

    }

    // Test that page is showing message if no pets exist
    @Test
    void shouldDisplayMessageIfNoPets() throws Exception {

        when(this.getCurrentUserIDService.getCurrentUserID()).thenReturn(currentUser);
        when(this.service.getUserPets(userId)).thenReturn(null);
        mvc.perform(get(URL)).andExpect(status().isOk())
                .andExpect(content().string(containsString("No pets registered under current user")));
    }

    // Test that page is correctly displaying medical records
    @Test
    void shouldDisplayMedicalRecordsTable() throws Exception {

        // Mock clinic list
        List<Vaccinations> vaccinations = new ArrayList<>();
        Date date = format.parse("2022-01-03");
        Vaccinations vaccination = new Vaccinations(1L, "Vaccination F", 5L, date, "Vaccination description");
        vaccinations.add(vaccination);

        Clinic clinic = new Clinic(1L, "Alpha Pet Clinic", "123 A Street, A City");
        clinics.add(clinic);

        when(this.getCurrentUserIDService.getCurrentUserID()).thenReturn(currentUser);
        when(this.service.getUserPets(userId)).thenReturn(pets);
        when(this.getCurrentUserIDService.getCurrentUserVetID()).thenReturn(null);
        when(this.service.getClinics()).thenReturn(clinics);
        mvc.perform(get(URL)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Gamma Pet Clinic")))
                .andExpect(content().string(containsString(pets.get(0).getVaccinations().get(0).getDate().toString())))
                .andExpect(content().string(containsString("test_description")))
                .andExpect(content().string(containsString("Some Treatment")));
    }

    // Test that page is correctly displaying treatment plans
    @Test
    void shouldDisplayVaccinationsIfExists() throws Exception {

        when(this.getCurrentUserIDService.getCurrentUserID()).thenReturn(currentUser);
        when(this.service.getUserPets(userId)).thenReturn(pets);
        when(this.getCurrentUserIDService.getCurrentUserVetID()).thenReturn(null);
        mvc.perform(get(URL)).andExpect(status().isOk())
                .andExpect(content().string(containsString("Vaccinations")))
                .andExpect(content().string(containsString("Vaccination Name")))
                .andExpect(content().string(containsString("Date Administered")));
    }

    // Test that page correctly finds delete record mapping
    @Test
    void testDeletePetMapping() throws Exception {

        mvc.perform(delete(URL + "/delete/pet/2"))
                .andExpect(status().isFound());
    }

    // Test that create pet mapping is found
    @Test
    void testCreatePetMapping() throws Exception {

        mvc.perform(post(URL + "/create/pet"))
                .andExpect(status().isFound());
    }

    // Test that update pet mapping is found
    @Test
    void testUpdatePetMapping() throws Exception {
        mvc.perform(patch(URL + "/update/pet/2"))
                .andExpect(status().isFound());
    }

    // Test that page correctly finds delete record mapping
    @Test
    void testDeleteRecordMapping() throws Exception {

        mvc.perform(delete(URL + "/delete/record/2"))
                .andExpect(status().isFound());
    }

    // Test that create record mapping is found
    @Test
    void testCreateRecordMapping() throws Exception {

        mvc.perform(post(URL + "/create/record"))
                .andExpect(status().isFound());
    }

    // Test that update record mapping is found
    @Test
    void testUpdateRecordMapping() throws Exception {
        mvc.perform(patch(URL + "/update/record/2"))
                .andExpect(status().isFound());
    }

}

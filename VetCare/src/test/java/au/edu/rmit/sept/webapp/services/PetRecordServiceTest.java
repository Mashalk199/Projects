package au.edu.rmit.sept.webapp.services;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import au.edu.rmit.sept.webapp.models.Clinic;
import au.edu.rmit.sept.webapp.models.MedicalRecords;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.Vaccinations;
import au.edu.rmit.sept.webapp.repositories.PetRecordRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class PetRecordServiceTest {

    @MockBean
    PetRecordService service;

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    void testGetUserPets() throws Exception {

        // given
        Long userId = 2L;
        Pet mockPet = new Pet(1L, 2L, "Oliver", "Lizard", "Female", 12L);
        Pet mockPet2 = new Pet(2L, 2L, "Jeff", "Dog", "Male", 2L);
        List<Pet> petList = new ArrayList<>();
        petList.add(mockPet);
        petList.add(mockPet2);

        // when
        PetRecordRepository mockR = mock(PetRecordRepository.class);
        PetRecordService petRecordService = new PetRecordService(mockR);
        when(mockR.getUserPetsAsEntity(userId)).thenReturn(petList);

        // then
        assertTrue(petRecordService.getUserPets(userId) == petList);
    }

    @Test
    void testPetRecords() throws Exception {

        // given
        Long petId = 2L;
        Date date = new Date(2020 - 01 - 01);
        MedicalRecords mockRecord = new MedicalRecords(1L, 2L, 2L, "Beta Pet Clinic", date, "testDescription", null,
                petId, "testDiagnosis", "testTreatment", null);
        List<MedicalRecords> recordList = new ArrayList<>();
        recordList.add(mockRecord);

        // when
        PetRecordRepository mockR = mock(PetRecordRepository.class);
        PetRecordService petRecordService = new PetRecordService(mockR);
        when(mockR.getAllRecordsForPet(petId)).thenReturn(recordList);

        // then
        assertTrue(petRecordService.getPetRecords(petId).contains(mockRecord));
    }

    @Test
    void testGetPetTreatmentPlans() throws Exception {

        // given
        Long petId = 2L;
        Date date = new Date(2020 - 01 - 01);
        Vaccinations mockPlan = new Vaccinations(1L, "Vaccination_Name", 2L,
                date, "testDescription");
        List<Vaccinations> planList = new ArrayList<>();
        planList.add(mockPlan);

        // when
        PetRecordRepository mockR = mock(PetRecordRepository.class);
        PetRecordService petRecordService = new PetRecordService(mockR);
        when(mockR.getAllVaccinationsForPet(petId)).thenReturn(planList);

        // then
        assertTrue(petRecordService.getPetVaccinations(petId).contains(mockPlan));
    }

    @Test
    void testDeletePet() throws Exception {

        // given
        Pet mockPet = new Pet(1L, 2L, "Oliver", "Lizard", "Female", 12L);

        // when
        PetRecordRepository mockR = mock(PetRecordRepository.class);
        PetRecordService petRecordService = new PetRecordService(mockR);
        assertTrue(petRecordService.deletePet(mockPet.getPetId()));

    }

    @Test
    void testCreatePet() throws Exception {

        // given
        Pet mockPet = new Pet(1L, 2L, "Oliver", "Lizard", "Female", 12L);

        // when
        PetRecordRepository mockR = mock(PetRecordRepository.class);
        PetRecordService petRecordService = new PetRecordService(mockR);
        assertTrue(petRecordService.createPet(mockPet));

    }

    @Test
    void testUpdatePet() throws Exception {

        // given
        Pet mockPet = new Pet(1L, 2L, "Oliver", "Lizard", "Female", 12L);

        // when
        PetRecordRepository mockR = mock(PetRecordRepository.class);
        PetRecordService petRecordService = new PetRecordService(mockR);
        assertTrue(petRecordService.updatePet(2L, mockPet));

    }

    @Test
    void testDeleteRecord() throws Exception {

        // given
        Date date = format.parse("2022-01-03");
        MedicalRecords mockRecord = new MedicalRecords(1L, 2L, 2L, "Beta Pet Clinic", date, "testDescription", null,
                1L, "testDiagnosis", "testTreatment", null);

        // when
        PetRecordRepository mockR = mock(PetRecordRepository.class);
        PetRecordService petRecordService = new PetRecordService(mockR);
        assertTrue(petRecordService.deleteRecord(mockRecord.getRecordId()));

    }

    @Test
    void testCreateRecord() throws Exception {

        // given
        Date date = format.parse("2022-01-03");
        MedicalRecords mockRecord = new MedicalRecords(1L, 2L, 2L, "Beta Pet Clinic", date, "testDescription", null,
                1L, "testDiagnosis", "testTreatment", null);

        // when
        PetRecordRepository mockR = mock(PetRecordRepository.class);
        PetRecordService petRecordService = new PetRecordService(mockR);
        assertTrue(petRecordService.createRecord(mockRecord));

    }

    @Test
    void testUpdateRecord() throws Exception {

        // given
        Date date = format.parse("2022-01-03");
        MedicalRecords mockRecord = new MedicalRecords(1L, 2L, 2L, "Beta Pet Clinic", date, "testDescription", null,
                1L, "testDiagnosis", "testTreatment", null);

        // when
        PetRecordRepository mockR = mock(PetRecordRepository.class);
        PetRecordService petRecordService = new PetRecordService(mockR);
        assertTrue(petRecordService.updateRecord(mockRecord.getRecordId(), mockRecord));

    }

    @Test
    void testGetClinics() throws Exception {

        // given
        List<Clinic> clinics = new ArrayList<>();
        clinics.add(new Clinic(1L, "Alpha Pet Clinic", "123 A Street, A City"));

        // when
        PetRecordRepository mockR = mock(PetRecordRepository.class);
        PetRecordService petRecordService = new PetRecordService(mockR);
        when(mockR.getAllClinics()).thenReturn(clinics);
        assertTrue(petRecordService.getClinics() == clinics);

    }

}

package au.edu.rmit.sept.webapp.services;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import au.edu.rmit.sept.webapp.models.Clinic;
import au.edu.rmit.sept.webapp.models.MedicalRecords;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.Vaccinations;
import au.edu.rmit.sept.webapp.models.Vet;
import au.edu.rmit.sept.webapp.repositories.PetRecordRepository;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class PetRecordService {

  private PetRecordRepository repository;

  public PetRecordService(PetRecordRepository repository) {
    this.repository = repository;
  }

  public void handleException(SQLException e) {
    System.err.println("SQL request failed.");
    System.err.println("SQL State: " + e.getSQLState());
    System.err.println("Error Code: " + e.getErrorCode());
    System.err.println("Message: " + e.getMessage());
    e.printStackTrace();
  }

  public List<Pet> getUserPets(Long userId) {
    try {
      List<Pet> pets = repository.getUserPetsAsEntity(userId);
      return pets;
    } catch (SQLException e) {
      handleException(e);
      return null;
    }
  }
  public List<Pet> getVetPets(Long vetId) {
    try {
      List<Pet> pets = repository.getVetPetsAsEntity(vetId);
      return pets;
    } catch (SQLException e) {
      handleException(e);
      return null;
    }
  }

  public List<MedicalRecords> getPetRecords(Long petId) {
    try {
      List<MedicalRecords> records = repository.getAllRecordsForPet(petId);
      return records;
    } catch (SQLException e) {
      handleException(e);
      return null;
    }
  }

  public List<Vaccinations> getPetVaccinations(Long petId) {
    try {
      List<Vaccinations> vaccinations = repository.getAllVaccinationsForPet(petId);
      return vaccinations;
    } catch (SQLException e) {
      handleException(e);
      return null;
    }
  }

  public boolean deletePet(Long petId) {
    try {
      repository.deletePetByPetId(petId);
      return true;
    } catch (SQLException e) {
      handleException(e);
      return false;
    }
  }

  public boolean createPet(Pet pet) {
    try {
      repository.createNewPet(pet);
      return true;
    } catch (SQLException e) {
      handleException(e);
      return false;
    }
  }

  public boolean updatePet(Long petId, Pet pet) {
    try {
      repository.updateExistingPet(petId, pet);
      return true;
    } catch (SQLException e) {
      handleException(e);
      return false;
    }
  }

  public boolean deleteRecord(Long recordId) {
    try {
      repository.deleteRecord(recordId);
      return true;
    } catch (SQLException e) {
      handleException(e);
      return false;
    }
  }

  public boolean createRecord(MedicalRecords record) {
    try {
      repository.createRecord(record);
      return true;
    } catch (SQLException e) {
      handleException(e);
      return false;
    }
  }

  public boolean updateRecord(Long recordId, MedicalRecords record) {
    try {
      repository.updateRecord(recordId, record);
      return true;
    } catch (SQLException e) {
      handleException(e);
      return false;
    }
  }
  public boolean updateActivePrescription(MedicalRecords record) {
    try {
      repository.updateActivePrescription(record);
      return true;
    } catch (SQLException e) {
      handleException(e);
      return false;
    }
  }
  public Long createActivePrescription(MedicalRecords record) {
    try {
      Long createdActivePrescription = repository.createActivePrescription(record);
      return createdActivePrescription;
    } catch (SQLException e) {
      handleException(e);
      return 0L;
    }
  }
  public boolean deleteActivePrescription(Long activePrescriptionId) {
    try {
      repository.deleteActivePrescription(activePrescriptionId);
      return true;
    } catch (SQLException e) {
      handleException(e);
      return false;
    }
  }

  
  public List<Clinic> getClinics() {
    try {
      List<Clinic> clinics = repository.getAllClinics();
      return clinics;
    } catch (SQLException e) {
      handleException(e);
      return null;
    }
  }

  public void exportPDF(HttpServletResponse response, Long id, Long current_user) throws IOException {

    // Get all users pets
    List<Pet> pets = new ArrayList<>();
    pets = getUserPets(current_user);
    Pet pet = new Pet();
    for (int i = 0; i<pets.size(); i++) {
      if (pets.get(i).getPetId() == id) {
        pet = pets.get(i);
      }
    }
    // Get All records for user pets
    List<MedicalRecords> records = new ArrayList<>();
    records = getPetRecords(id);

    // Get list of clinics to get name
    List<Clinic> clinics = new ArrayList<>();
    clinics = getClinics();

    Document document = new Document(PageSize.A4);
    PdfWriter.getInstance(document, response.getOutputStream());

    document.open();

    // PDF Header
    Font fontHeader = FontFactory.getFont(FontFactory.TIMES_BOLD);
    fontHeader.setSize(22);
    Paragraph headerParagraph = new Paragraph("Medical Records for " + pet.getName(), fontHeader);
    headerParagraph.setAlignment(Paragraph.ALIGN_CENTER);

    // Pet Information Header
    Font infoHeader = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC);
    infoHeader.setSize(16);
    Paragraph infoParagraph = new Paragraph("\nPet Information:", infoHeader);

    // Pet Basic Information
    Font fontParagraph = FontFactory.getFont(FontFactory.TIMES);
    Paragraph pdfParagraph = new Paragraph("", fontParagraph);
    pdfParagraph.setAlignment(Paragraph.ALIGN_LEFT);
    pdfParagraph.add("\nName: " + pet.getName() + "\n");
    pdfParagraph.add("Species: " + pet.getSpecies() + "\n");
    pdfParagraph.add("Gender: " + pet.getGender() + "\n");
    pdfParagraph.add("Age: " + pet.getAge() + "\n\n\n");
    
    // Medical Records Table Header
    Font tableHeader = FontFactory.getFont(FontFactory.TIMES_BOLDITALIC);
    tableHeader.setSize(16);
    Paragraph tableParagraph = new Paragraph("Medical Records Table:", tableHeader);

    // Records Table
    Table table = new Table(5);
    table.addCell("Clinic ID");
    table.addCell("Clinic Name");
    table.addCell("Date");
    table.addCell("Diagnosis");
    table.addCell("Treatment");

    for (int i = 0; i < records.size(); i++) {
      table.addCell(records.get(i).getClinicId().toString());
      for (int j = 0; j < clinics.size(); j++) { //TODO FIND BETTER METHOD TO GET CLINIC NAME
        if (records.get(i).getClinicId() == clinics.get(j).getClinicId()) {
          table.addCell(clinics.get(j).getClinicName().toString());
        }
      }
      table.addCell(records.get(i).getDate().toString());
      table.addCell(records.get(i).getDiagnosis().toString());
      table.addCell(records.get(i).getTreatment().toString());
    }
    
   
      
    document.add(headerParagraph);
    document.add(infoParagraph);
    document.add(pdfParagraph);
    document.add(tableParagraph);
    document.add(table);
    document.close();
    }

 
  public static final String STORAGE_DIRECTORY = "D:\\Storage";

  public void saveFile(MultipartFile fileToSave) throws IOException {
      if (fileToSave == null) {
          throw new NullPointerException("fileToSave is null");
      }
      var targetFile = new File(STORAGE_DIRECTORY + File.separator + fileToSave.getOriginalFilename());
      if (!Objects.equals(targetFile.getParent(), STORAGE_DIRECTORY)) {
          throw new SecurityException("Unsupported filename!");
      }
      Files.copy(fileToSave.getInputStream(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
  }

  public List<Vet> getAllVets() {
    try {
      List<Vet> vets = repository.getAllVets();
      return vets;
    } catch (SQLException e) {
      handleException(e);
      return null;
    }
  }
  

}

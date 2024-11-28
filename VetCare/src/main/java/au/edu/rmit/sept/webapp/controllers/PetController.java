package au.edu.rmit.sept.webapp.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import au.edu.rmit.sept.webapp.models.Clinic;
import au.edu.rmit.sept.webapp.models.MedicalRecords;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.Prescription;
import au.edu.rmit.sept.webapp.models.Vet;
import au.edu.rmit.sept.webapp.services.GetCurrentUserIDService;
import au.edu.rmit.sept.webapp.services.PetRecordService;
import au.edu.rmit.sept.webapp.services.PrescriptionService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.nio.file.Path;
import java.io.File;

import org.springframework.core.io.Resource;

@Controller
public class PetController {

  private PetRecordService service;
  private PrescriptionService prescriptionService;
  private final GetCurrentUserIDService getCurrentUserIDService;

  public PetController(PetRecordService service, PrescriptionService prescriptionService,
      GetCurrentUserIDService getCurrentUserIDService) {
    this.service = service;
    this.prescriptionService = prescriptionService;
    this.getCurrentUserIDService = getCurrentUserIDService;
  }

  @GetMapping("/records")
  public String petrecords(Model model) {

    // Get the current user ID using GetCurrentUserIDService
    Long user_id = Long.valueOf(this.getCurrentUserIDService.getCurrentUserID());

    // Check if the user is not signed in
    if (user_id == -1) {
      return "redirect:/sign-in";
    }

    // Check if user is vet
    Long vetId = this.getCurrentUserIDService.getCurrentUserVetID();
    model.addAttribute("vetId", vetId);

    List<Pet> petEntitys = new ArrayList<>();

    // If user is not a vet, show all users pets
    if (vetId == null) {
      // Get pets as list of entitys (each entity contains an array of records)
      petEntitys = service.getUserPets(user_id);
      model.addAttribute("petEntitys", petEntitys);
    }
    // If user is a vet, get all pets the vet has access to
    else {
      petEntitys = service.getVetPets(vetId);
      model.addAttribute("petEntitys", petEntitys);
    }

    // Get List of clinics
    List<Clinic> clinics = service.getClinics();
    model.addAttribute("clinics", clinics);

    // Get list of prescriptions
    List<Prescription> prescriptions = prescriptionService.getAllPrescriptions();
    model.addAttribute("allPrescriptions", prescriptions);

    // Get list of vets
    List<Vet> vets = service.getAllVets();
    model.addAttribute("vets", vets);

    // Prepare to store records and their files
    Map<Long, List<MedicalRecords>> petRecordsMap = new HashMap<>();
    Map<Long, Map<Long, List<String>>> recordFilesMap = new HashMap<>();

    for (Pet pet : petEntitys) {
      List<MedicalRecords> records = service.getPetRecords(pet.getPetId()); // Adjust as per your method
      petRecordsMap.put(pet.getPetId(), records);

      // Initialize a map for each pet in the recordFilesMap
      recordFilesMap.put(pet.getPetId(), new HashMap<>());

      // List files for each record
      for (MedicalRecords record : records) {
          String UPLOAD_DIR = "src/main/resources/uploads/record-" + record.getRecordId() + "/";
          File uploadDir = new File(UPLOAD_DIR);

          // Initialize a list for files for the current record
          List<String> fileNames = new ArrayList<>();

          if (uploadDir.exists() && uploadDir.isDirectory()) {
              String[] files = uploadDir.list();
              if (files != null) {
                  fileNames.addAll(Arrays.asList(files));
              }
          }

          // Store the file names (could be empty if no files found)
          recordFilesMap.get(pet.getPetId()).put(record.getRecordId(), fileNames);
      }
    }

    model.addAttribute("petRecordsMap", petRecordsMap);
    model.addAttribute("recordFilesMap", recordFilesMap);

    return "medicalrecords.html";

  }

  // PET CRUD FUNCTIONS
  @DeleteMapping("records/delete/pet/{id}")
  public String deletePet(@PathVariable Long id) {
    service.deletePet(id);
    return "redirect:/records";

  }

  @PostMapping("records/create/pet")
  public String createPet(@ModelAttribute Pet pet) {

    pet.setOwnerId(Long.valueOf(this.getCurrentUserIDService.getCurrentUserID()));
    service.createPet(pet);
    return "redirect:/records";

  }

  @PatchMapping("records/update/pet/{id}")
  public String updatePet(@PathVariable Long id, @ModelAttribute Pet pet) {
    service.updatePet(id, pet);
    return "redirect:/records";

  }

  // RECORD CRUD FUNCTIONS
  @DeleteMapping("records/delete/record/{id}")
  public String deleteRecord(@PathVariable Long id) {
    service.deleteRecord(id);
    return "redirect:/records";

  }

  @PostMapping("records/create/record")
  public String createRecord(@ModelAttribute MedicalRecords record) {
    
    if (record.getPrescriptionId() != null) {
      record.setActivePrescriptionId(service.createActivePrescription(record));
    }
    service.createRecord(record);
    return "redirect:/records";
  }

  @PatchMapping("records/update/record/{id}")
  public String updateRecord(@PathVariable Long id, @ModelAttribute MedicalRecords record) {

    /*If user changes an existing medicine to none: 
    change active_prescription_id of record to 0
    change prescription_id to 0
    change medicine_name to 0
    delete active_prescription from table
    */
   
    // If just changing medicine to another medicine, update record
    if (record.getActivePrescriptionId() != null && record.getPrescriptionId() != null) {
      service.updateActivePrescription(record);
    }

    // If removing medicine, delete active prescription and set record active prescription to null
    else if (record.getActivePrescriptionId() != null && record.getPrescriptionId() == null) {
      Long apId = record.getActivePrescriptionId();
      record.setActivePrescriptionId(null);
      service.deleteActivePrescription(apId);
    }

    // If adding medicine to existing record, create new active prescription and tie id to record
    else if (record.getActivePrescriptionId() == null && record.getPrescriptionId() != null) {
      record.setActivePrescriptionId(service.createActivePrescription(record));;
    }
    service.updateRecord(id, record);
    return "redirect:/records";

  }

  

  // DOWNLOAD RECORD FUNCTION
  @GetMapping("/records/openpdf/export/{id}")
  public void createPDF(HttpServletResponse response, @PathVariable Long id) throws IOException {
    response.setContentType("application/pdf");
    DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
    String currentDateTime = dateFormatter.format(new Date());

    String headerKey = "Content-Disposition";
    String headerValue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
    response.setHeader(headerKey, headerValue);

        service.exportPDF(response, id, Long.valueOf(this.getCurrentUserIDService.getCurrentUserID()));
  }

  // DOWNLOAD FILES FUNCTION
  @GetMapping("/records/files/download")
  public ResponseEntity<Resource> downloadFile(@RequestParam Long id, @RequestParam String filename) {
    String UPLOAD_DIR = "src/main/resources/uploads/record-" + id + "/";
    Path path = Paths.get(UPLOAD_DIR + filename);
     Resource resource;

    try {
        resource = new UrlResource(path.toUri());
    } catch (MalformedURLException e) {
        // Handle the exception, for example, return a not found response
        return ResponseEntity.notFound().build();
    }

    if (!resource.exists() || !resource.isReadable()) {
        return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
  }

  // UPLOAD FILE FUNCTION
  @PostMapping("/records/upload/file/{id}")
  public String uploadFile(@RequestParam("file") MultipartFile file, 
                            RedirectAttributes redirectAttributes, 
                            @PathVariable Long id, @ModelAttribute MedicalRecords record) {
    String UPLOAD_DIR = "src/main/resources/uploads/record-" + id.toString() + "/";
    if (file.isEmpty()) {
        redirectAttributes.addFlashAttribute("message", "File is empty");
        return "redirect:/records"; // Redirect on error
    }
    try {
        // Create the uploads directory if it doesn't exist
        Files.createDirectories(Paths.get(UPLOAD_DIR));

        // Save the file
        Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
        Files.write(path, file.getBytes());

        redirectAttributes.addFlashAttribute("message", "File uploaded successfully: " + file.getOriginalFilename());
        return "redirect:/records"; // Redirect on success
    } catch (IOException e) {
        redirectAttributes.addFlashAttribute("message", "Failed to upload file");
        return "redirect:/records"; // Redirect on error
    }
  }

  @PostMapping("/records/delete/file/{id}")
  public String deleteFile(@RequestParam("filename") String filename,
                          RedirectAttributes redirectAttributes,
                          @PathVariable Long id) {
    String UPLOAD_DIR = "src/main/resources/uploads/record-" + id.toString() + "/";
    Path path = Paths.get(UPLOAD_DIR + filename);

    try {
        // Check if the file exists and delete it
        if (Files.exists(path)) {
            Files.delete(path);
            redirectAttributes.addFlashAttribute("message", "File deleted successfully: " + filename);
        } else {
            redirectAttributes.addFlashAttribute("message", "File not found: " + filename);
        }
    } catch (IOException e) {
        redirectAttributes.addFlashAttribute("message", "Failed to delete file: " + filename);
    }

    return "redirect:/records"; // Redirect after the operation
  }
  @PostMapping("/records/share")
  public String sharePetRecords(@ModelAttribute Vet vet) {
      
  return "redirect:/records";
  }
}
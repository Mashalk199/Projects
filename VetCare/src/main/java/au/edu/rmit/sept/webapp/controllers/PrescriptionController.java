package au.edu.rmit.sept.webapp.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import au.edu.rmit.sept.webapp.models.DeliveryInformation;
import au.edu.rmit.sept.webapp.models.Prescription;
import au.edu.rmit.sept.webapp.models.ActivePrescription;
import au.edu.rmit.sept.webapp.services.PrescriptionService;
import au.edu.rmit.sept.webapp.services.ActivePrescriptionService;
import au.edu.rmit.sept.webapp.services.DeliveryService;
import au.edu.rmit.sept.webapp.services.GetCurrentUserIDService;
import au.edu.rmit.sept.webapp.services.PetRecordService;

@Controller
@RequestMapping("/prescriptions")
public class PrescriptionController {


  @Autowired
  private ActivePrescriptionService activePrescriptionService;
  @Autowired
  private PrescriptionService prescriptionService;
  @Autowired
  private PetRecordService petRecordService;
  @Autowired
  private DeliveryService deliveryService;
  @Autowired
  private GetCurrentUserIDService getCurrentUserIDService;

  public PrescriptionController(PrescriptionService prescriptionService, PetRecordService petRecordService,
      DeliveryService deliveryService) {
    this.prescriptionService = prescriptionService;
    this.petRecordService = petRecordService;
    this.deliveryService = deliveryService;

  }
  
  @GetMapping
  public String loadActivePrescriptions(Model model) {
	// Get the current user ID
	int currentUserId = getCurrentUserIDService.getCurrentUserID();

	// Redirect to sign in if user is not signed in
    if (currentUserId == -1) {
        return "redirect:/sign-in";
    }

	// Get delivery orders for the user.
    List<DeliveryInformation> deliveries = deliveryService.getDeliveryInformationByUser((long) currentUserId);

    // Check if deliveries is not null or empty before passing it to the view
    if (deliveries != null && !deliveries.isEmpty()) {
        model.addAttribute("deliveries", deliveries);
    } else {
        // Log the case where there are no deliveries
        System.out.println("No delivery orders found for user ID: " + currentUserId);
    }

    // Get active prescriptions for the user
	List<ActivePrescription> activePrescriptions = activePrescriptionService.getActivePrescriptionsByUserId((long) currentUserId);
	
	    // Check if activePrescriptions is not null or empty before passing it to the view
		if (activePrescriptions != null && !activePrescriptions.isEmpty()) {
			model.addAttribute("activePrescriptions", activePrescriptions);
		} else {
			// Log the case where there are no active prescriptions
			System.out.println("No active prescriptions found for user ID: " + currentUserId);
		}


    return "prescriptions/list";
  }

  @PostMapping("/requestDelivery")
  public String requestDelivery(@RequestParam("activePrescriptionId") Long activePrescriptionId,
      @RequestParam("deliveryAddress") String deliveryAddress,
      RedirectAttributes redirectAttributes) {
    try {

      // Get the current user ID
      int currentUserId = getCurrentUserIDService.getCurrentUserID();
      // Check if the user is logged in
      if (currentUserId == -1) {
        return "redirect:/sign-in";
      }

      // Process the delivery request
      deliveryService.requestDelivery(activePrescriptionId, deliveryAddress, (long) currentUserId);

      // Get Prescription to get prescription name
	    ActivePrescription activePrescription = activePrescriptionService.getActivePrescriptionById(activePrescriptionId);
      Prescription prescription = activePrescription.getPrescription();  // Get associated prescription details

      // Add summary details to redirect attributes
      redirectAttributes.addFlashAttribute("message", "Delivery request placed successfully.");
      redirectAttributes.addFlashAttribute("prescriptionName", prescription.getMedicineName());
      redirectAttributes.addFlashAttribute("deliveryAddress", deliveryAddress);
      redirectAttributes.addFlashAttribute("estimatedDeliveryTime", "2-3 Business Days");

    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", "Error placing delivery request.");
    }

    return "redirect:/prescriptions/confirmation"; // Redirect to the confirmation page
  }

  @GetMapping("/confirmation")
  public String showConfirmationPage(Model model, @RequestHeader(value = "referer", required = false) String referer) {
    model.addAttribute("previousPage", referer); // Pass the referer (previous page URL) to the view
    return "deliveries/confirmation"; // Render the confirmation page
  }

}
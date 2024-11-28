package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.ActivePrescription;
import au.edu.rmit.sept.webapp.models.DeliveryInformation;
import au.edu.rmit.sept.webapp.models.Prescription;
import au.edu.rmit.sept.webapp.repositories.ActivePrescriptionRepository;
import au.edu.rmit.sept.webapp.repositories.DeliveryRepository;
import au.edu.rmit.sept.webapp.repositories.PrescriptionRepository;
import au.edu.rmit.sept.webapp.repositories.UserAccountRepository;

import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final ActivePrescriptionRepository activePrescriptionRepository;
    private final UserAccountRepository userAccountRepository;

    private final SesEmailService emailService;


    public DeliveryService(DeliveryRepository deliveryRepository, ActivePrescriptionRepository activePrescriptionRepository, UserAccountRepository userAccountRepository,
                            SesEmailService emailService) {
        this.deliveryRepository = deliveryRepository;
        this.activePrescriptionRepository = activePrescriptionRepository;
        this.userAccountRepository = userAccountRepository;
        this.emailService = emailService;}
    public void handleException(SQLException e) {
        System.err.println("SQL request failed.");
        System.err.println("SQL State: " + e.getSQLState());
        System.err.println("Error Code: " + e.getErrorCode());
        System.err.println("Message: " + e.getMessage());
        e.printStackTrace();
      }

    // Request Delivery for a prescription
    public boolean requestDelivery(Long activePrescriptionId, String deliveryAddress, Long userId) {
        try {
            ActivePrescription activePrescription = activePrescriptionRepository.getActivePrescriptionById(activePrescriptionId);
            if (activePrescription  == null) {
                throw new IllegalArgumentException("Prescription not found");
            }

            DeliveryInformation delivery = new DeliveryInformation();
            delivery.setActivePrescription(activePrescription);
            delivery.setUserId(userId);
            delivery.setDeliveryStatus("Ordered");
            delivery.setDeliveryAddress(deliveryAddress);

            // Set the order date to the current date
            delivery.setOrderDate(java.sql.Date.valueOf(LocalDate.now()));

            // Set an estimated delivery date (e.g., 3 days after order date)
            LocalDate estimatedDeliveryDate = LocalDate.now().plus(3, ChronoUnit.DAYS);
            delivery.setEstimatedDeliveryDate(java.sql.Date.valueOf(estimatedDeliveryDate));

            // Ship date and actual delivery date are initially null
            delivery.setShipDate(null);
            delivery.setActualDeliveryDate(null);

            deliveryRepository.saveDeliveryInformation(delivery);
            // Send confirmation email to the user
            Prescription prescription = activePrescription.getPrescription();
            if (prescription != null) {
                
                // Get Current Users email
                String userUsername = userAccountRepository.getCurrentUsername();
                //TODO: REMOVE test email and use db email before submission
                //String userEmail = userAccountRepository.getAccount(userUsername).email();; // Fetch the user’s email via a UserRepository or related service
                String userEmail = "s3944263@student.rmit.edu.au";
                emailService.sendOrderConfirmationEmail(userEmail, prescription.getMedicineName(), LocalDate.now().toString(), deliveryAddress);
            }

            return true; 
        } 
        catch (SQLException e) {
            handleException(e);
            return false;
        }
    }

    // Get delivery information for a user
    public List<DeliveryInformation> getDeliveryInformationByUser(Long userId) {
        try {
            return deliveryRepository.getDeliveryInformationByUser(userId);
        } catch (SQLException e) {
            handleException(e);
            return null;
        }
    }
}

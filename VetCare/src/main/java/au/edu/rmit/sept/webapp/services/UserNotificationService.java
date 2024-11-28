package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.ActivePrescription;
import au.edu.rmit.sept.webapp.repositories.ActivePrescriptionRepository;
import au.edu.rmit.sept.webapp.repositories.UserAccountRepository;
import org.springframework.stereotype.Service;
import java.sql.SQLException;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserNotificationService {

    private final ActivePrescriptionRepository activePrescriptionRepository;
    private final UserAccountRepository userAccountRepository;

    public UserNotificationService(ActivePrescriptionRepository activePrescriptionRepository, 
                                   UserAccountRepository userAccountRepository) {
        this.activePrescriptionRepository = activePrescriptionRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public String checkForMedicationReminders(Long userId) {
        try {
            List<ActivePrescription> activePrescriptions = activePrescriptionRepository.getActivePrescriptionsByUserId(userId);
            LocalDate now = LocalDate.now();
            LocalDate thresholdDate = now.plusDays(7); // Notification threshold: 7 days before expiration
            StringBuilder notificationMessage = new StringBuilder();

            for (ActivePrescription activePrescription : activePrescriptions) {
                Date expirationDate = (Date) activePrescription.getExpirationDate();
                if (expirationDate != null && expirationDate.toLocalDate().isBefore(thresholdDate)) {
                    notificationMessage.append("Your prescription for ")
                            .append(activePrescription.getPrescription().getMedicineName())
                            .append(" is expiring on ")
                            .append(expirationDate.toLocalDate().toString())
                            .append(".\n");
                }
            }

            return notificationMessage.toString();

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error fetching medication reminders.";
        }
    }



    // // Check if prescriptions are nearing expiration and send notifications
    // public void checkForMedicationReminders(Long userId) throws Exception {
    //     List<ActivePrescription> activePrescriptions = activePrescriptionRepository.getActivePrescriptionsByUserId(userId);

    //     for (ActivePrescription prescription : activePrescriptions) {
    //         Date expirationDate = (Date) prescription.getExpirationDate();
    //         if (isNearExpiration(expirationDate)) {
    //             sendNotification(userId, prescription);
    //         }
    //     }
    // }
    //     // Helper function to check if the prescription is near expiration. (Current Threshold is 7 Days)
    //     private boolean isNearExpiration(Date expirationDate) {
    //         LocalDate expiration = expirationDate.toLocalDate();
    //         LocalDate now = LocalDate.now();
    //         return expiration.isBefore(now.plusDays(7)) && expiration.isAfter(now);
    //     }

    //     // Mock function to simulate sending a notification
    //     private void sendNotification(Long userId, ActivePrescription prescription) throws Exception {
    //         String username = userAccountRepository.getCurrentUsername();
    //         //TODO IMPLEMENT EMAIL NOTIFICATION METHOD
    //         System.out.println("Notification sent to " + username + " for prescription: " + 
    //                             prescription.getPrescription().getMedicineName() + " (near expiration)");
    //     }
}

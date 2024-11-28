package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.ActivePrescription;
import au.edu.rmit.sept.webapp.repositories.ActivePrescriptionRepository;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ActivePrescriptionService {

    private final ActivePrescriptionRepository activePrescriptionRepository;

    public List<ActivePrescription> getActivePrescriptionsByUserId(Long userId) {
        try {
            return activePrescriptionRepository.getActivePrescriptionsByUserId(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ActivePrescriptionService(ActivePrescriptionRepository activePrescriptionRepository) {
        this.activePrescriptionRepository = activePrescriptionRepository;
    }

    public ActivePrescription getActivePrescriptionById(Long prescriptionId) {
        try {
            return activePrescriptionRepository.getActivePrescriptionById(prescriptionId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Calculate the expiration date as 2 months from the current date
    public void setExpirationDateUponDelivery(Long activePrescriptionId, Date deliveryDate) {
        // Convert java.sql.Date to java.time.LocalDate
        LocalDate localDeliveryDate = deliveryDate.toLocalDate();

        // Calculate expiration date as 2 months after the delivery date
        LocalDate expirationLocalDate = localDeliveryDate.plusMonths(2);

        // Convert LocalDate back to java.sql.Date for database storage
        Date expirationDate = Date.valueOf(expirationLocalDate);
        try {
            activePrescriptionRepository.updateExpirationDate(activePrescriptionId, expirationDate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

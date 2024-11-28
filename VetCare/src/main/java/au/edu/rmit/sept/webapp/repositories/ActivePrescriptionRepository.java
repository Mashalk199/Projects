package au.edu.rmit.sept.webapp.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import au.edu.rmit.sept.webapp.models.ActivePrescription;
import au.edu.rmit.sept.webapp.models.Prescription;
import au.edu.rmit.sept.webapp.utilities.DatabaseMethods;
import org.springframework.stereotype.Repository;

@Repository
public class ActivePrescriptionRepository {

    DatabaseMethods db = new DatabaseMethods();

    // Set expiration date for active prescription.
    public void updateExpirationDate(Long activePrescriptionId, java.sql.Date expirationDate) throws SQLException {
        String query = String.format(
                "UPDATE active_prescriptions SET \"expiration_date\" = '%tF' WHERE \"active_prescription_id\" = %d",
                expirationDate, activePrescriptionId);
        db.SQLQueryResultSet(query);
    }
    
    // Fetch all active prescriptions by userId (derived via pet ownership) with prescription details
    public List<ActivePrescription> getActivePrescriptionsByUserId(Long userId) throws SQLException {
        String query = String.format(
                "SELECT ap.\"active_prescription_id\", p.\"prescription_id\", p.\"medicine_name\", p.\"dosage\", p.\"frequency\", " +
                "ap.\"expiration_date\", pet.\"pet_id\", pet.\"name\" AS \"pet_name\" " +
                "FROM active_prescriptions ap " +
                "JOIN prescriptions p ON ap.\"prescription_id\" = p.\"prescription_id\" " +
                "JOIN pets pet ON ap.\"pet_id\" = pet.\"pet_id\" " +
                "WHERE pet.\"owner_id\" = %d", userId);

        ResultSet rs = db.SQLQueryResultSet(query);
        List<ActivePrescription> activePrescriptions = new ArrayList<>();

        while (rs != null && rs.next()) {
            ActivePrescription activePrescription = new ActivePrescription();
            activePrescription.setId(rs.getLong("active_prescription_id"));
            activePrescription.setPrescriptionId(rs.getLong("prescription_id"));
            activePrescription.setPetId(rs.getLong("pet_id"));
            activePrescription.setExpirationDate(rs.getDate("expiration_date"));
            activePrescription.setPetName(rs.getString("pet_name"));

            // Set prescription details using setters
            Prescription prescription = new Prescription();
            prescription.setMedicineName(rs.getString("medicine_name"));
            prescription.setDosage(rs.getString("dosage"));
            prescription.setFrequency(rs.getString("frequency"));

            activePrescription.setPrescription(prescription);
            activePrescriptions.add(activePrescription);
        }
        return activePrescriptions;
    }

    // Used in Service Test
    public ActivePrescription getActivePrescriptionById(Long activePrescriptionId) throws SQLException {
        String query = String.format(
                "SELECT ap.\"active_prescription_id\", p.\"prescription_id\", p.\"medicine_name\", p.\"dosage\", p.\"frequency\", " +
                "ap.\"expiration_date\", pet.\"pet_id\", pet.\"name\" AS \"pet_name\" " +
                "FROM active_prescriptions ap " +
                "JOIN prescriptions p ON ap.\"prescription_id\" = p.\"prescription_id\" " +
                "JOIN pets pet ON ap.\"pet_id\" = pet.\"pet_id\" " +
                "WHERE ap.\"active_prescription_id\" = %d", activePrescriptionId);

        ResultSet rs = db.SQLQueryResultSet(query);

        if (rs != null && rs.next()) {
            ActivePrescription activePrescription = new ActivePrescription();
            activePrescription.setId(rs.getLong("active_prescription_id"));
            activePrescription.setPrescriptionId(rs.getLong("prescription_id"));
            activePrescription.setPetId(rs.getLong("pet_id"));
            activePrescription.setExpirationDate(rs.getDate("expiration_date"));
            activePrescription.setPetName(rs.getString("pet_name"));

            // Set prescription details using setters
            Prescription prescription = new Prescription();
            prescription.setMedicineName(rs.getString("medicine_name"));
            prescription.setDosage(rs.getString("dosage"));
            prescription.setFrequency(rs.getString("frequency"));

            activePrescription.setPrescription(prescription);
            return activePrescription;
        } else {
            return null;
        }
    }

    // Fetch all active prescriptions for a user by petId with prescription details
    // TODO: Remove before submission if not required
    public List<ActivePrescription> getActivePrescriptionsByPetId(Long petId) throws SQLException {
        String query = String.format(
                "SELECT ap.\"active_prescription_id\", p.\"prescription_id\", p.\"medicine_name\", p.\"dosage\", p.\"frequency\", " +
                "ap.\"expiration_date\", pet.\"pet_id\", pet.\"name\" AS \"pet_name\" " +
                "FROM active_prescriptions ap " +
                "JOIN prescriptions p ON ap.\"prescription_id\" = p.\"prescription_id\" " +
                "JOIN pets pet ON ap.\"pet_id\" = pet.\"pet_id\" " +
                "WHERE pet.\"pet_id\" = %d", petId);

        ResultSet rs = db.SQLQueryResultSet(query);
        List<ActivePrescription> activePrescriptions = new ArrayList<>();

        while (rs != null && rs.next()) {
            ActivePrescription activePrescription = new ActivePrescription();
            activePrescription.setId(rs.getLong("active_prescription_id"));
            activePrescription.setPrescriptionId(rs.getLong("prescription_id"));
            activePrescription.setPetId(rs.getLong("pet_id"));
            activePrescription.setExpirationDate(rs.getDate("expiration_date"));
            activePrescription.setPetName(rs.getString("pet_name"));

            // Set prescription details using setters
            Prescription prescription = new Prescription();
            prescription.setMedicineName(rs.getString("medicine_name"));
            prescription.setDosage(rs.getString("dosage"));
            prescription.setFrequency(rs.getString("frequency"));

            activePrescription.setPrescription(prescription);
            activePrescriptions.add(activePrescription);
        }
        return activePrescriptions;
    }


}
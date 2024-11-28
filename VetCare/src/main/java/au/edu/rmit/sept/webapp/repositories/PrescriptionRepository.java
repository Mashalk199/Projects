package au.edu.rmit.sept.webapp.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.datasource.init.UncategorizedScriptException;
import org.springframework.stereotype.Repository;

import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.Prescription;
import au.edu.rmit.sept.webapp.utilities.DatabaseMethods;

@Repository
public class PrescriptionRepository {

    DatabaseMethods db = new DatabaseMethods();

    public Prescription getPrescriptionById(Long prescriptionId) throws SQLException {
        Prescription prescription = null;
        ResultSet rs = db.SQLQueryResultSet(
            String.format(
                "SELECT p.\"prescription_id\", p.\"medicine_name\", p.\"dosage\", p.\"frequency\", " +
                "ap.\"expiration_date\", pet.\"name\" AS \"pet_name\" " +
                "FROM active_prescriptions ap " +
                "JOIN prescriptions p ON ap.\"prescription_id\" = p.\"prescription_id\" " +
                "JOIN pets pet ON ap.\"pet_id\" = pet.\"pet_id\" " +
                "WHERE p.\"prescription_id\" = %d", prescriptionId)
            );
                // Raw SQL Query:
                // SELECT p.prescription_id, p.medicine_name, p.dosage, p.frequency, ap.expiration_date, pet.name AS pet_name
                // FROM active_prescriptions ap
                // JOIN prescriptions p ON ap.prescription_id = p.prescription_id
                // JOIN pets pet ON ap.pet_id = pet.pet_id
                // WHERE p.prescription_id = ?
            if (rs.next()) {
                prescription = new Prescription();
                prescription.setId(rs.getLong("prescription_id"));
                prescription.setMedicineName(rs.getString("medicine_name"));
                prescription.setDosage(rs.getString("dosage"));
                prescription.setFrequency(rs.getString("frequency"));

                Pet pet = new Pet();
                pet.setName(rs.getString("pet_name"));
                prescription.setPet(pet);
            }
        return prescription;
    }

    public List<Prescription> getPrescriptionsByPetId(Long petId) throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();
        ResultSet rs = db.SQLQueryResultSet(
            String.format(
                "SELECT p.\"prescription_id\", p.\"medicine_name\", p.\"dosage\", p.\"frequency\", pet.\"name\" AS \"pet_name\" " +
                "FROM active_prescriptions ap " +
                "JOIN prescriptions p ON ap.\"prescription_id\" = p.\"prescription_id\" " +
                "JOIN pets pet ON ap.\"pet_id\" = pet.\"pet_id\" " +
                "WHERE pet.\"pet_id\" = %d", petId)
            );
                // Raw SQL Query:
                // SELECT p.prescription_id, p.medicine_name, p.dosage, p.frequency, pet.name AS pet_name
                // FROM active_prescriptions ap
                // JOIN prescriptions p ON ap.prescription_id = p.prescription_id
                // JOIN pets pet ON ap.pet_id = pet.pet_id
                // WHERE pet.pet_id = ?
            while (rs.next()) {
                Prescription p = new Prescription();
                p.setId(rs.getLong("prescription_id"));
                p.setMedicineName(rs.getString("medicine_name"));
                p.setDosage(rs.getString("dosage"));
                p.setFrequency(rs.getString("frequency"));

                Pet pet = new Pet();
                // pet.pet_id = rs.getLong("pet_id");
                pet.setName(rs.getString("pet_name"));
                p.setPet(pet);

                prescriptions.add(p);
            }
        return prescriptions;
    }

    public List<Prescription> getPrescriptionsByUserId(Long userId) throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();
        ResultSet rs = db.SQLQueryResultSet(
            String.format("SELECT p.\"prescription_id\", p.\"medicine_name\", p.\"dosage\", p.\"frequency\", pet.\"name\" AS \"pet_name\" " +
                "FROM active_prescriptions ap " +
                "JOIN prescriptions p ON ap.\"prescription_id\" = p.\"prescription_id\" " +
                "JOIN pets pet ON ap.\"pet_id\" = pet.\"pet_id\" " +
                "WHERE pet.\"owner_id\" = %d", userId)
        );
                // Raw SQL Query:
                // SELECT p.prescription_id, p.medicine_name, p.dosage, p.frequency, pet.name AS pet_name
                // FROM active_prescriptions ap
                // JOIN prescriptions p ON ap.prescription_id = p.prescription_id
                // JOIN pets pet ON ap.pet_id = pet.pet_id
                // WHERE pet.owner_id = ?
            while (rs.next()) {
                Prescription p = new Prescription();
                p.setId(rs.getLong("prescription_id"));
                p.setMedicineName(rs.getString("medicine_name"));
                p.setDosage(rs.getString("dosage"));
                p.setFrequency(rs.getString("frequency"));

                Pet pet = new Pet();
                // pet.pet_id = rs.getLong("pet_id");
                pet.setName(rs.getString("pet_name"));
                p.setPet(pet);

                prescriptions.add(p);
            }
        return prescriptions;
    }
    public List<Prescription> getAllPrescriptions() throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();
        ResultSet rs = db.SQLQueryResultSet(
            String.format("SELECT * from PRESCRIPTIONS")
        );
            while (rs.next()) {
                Prescription p = new Prescription();
                p.setId(rs.getLong("prescription_id"));
                p.setMedicineName(rs.getString("medicine_name"));
                p.setDosage(rs.getString("dosage"));
                p.setFrequency(rs.getString("frequency"));
                prescriptions.add(p);
            }
        return prescriptions;
    }
}

//     //Set expiration date from active prescriptions 
//     public void setExpirationDate(Long userId, ???) throws SQLException {
//     }
// }
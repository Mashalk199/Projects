package au.edu.rmit.sept.webapp.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.jdbc.object.SqlQuery;
import org.springframework.stereotype.Repository;

import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.models.Vaccinations;
import au.edu.rmit.sept.webapp.models.Vet;
import au.edu.rmit.sept.webapp.utilities.DatabaseMethods;
import au.edu.rmit.sept.webapp.models.Clinic;
import au.edu.rmit.sept.webapp.models.MedicalRecords;

@Repository
public class PetRecordRepository {

    DatabaseMethods db = new DatabaseMethods();

    public List<Pet> getUserPetsAsEntity(Long userId) throws SQLException {

        ResultSet rs = db.SQLQueryResultSet(
                String.format("SELECT * FROM pets where" + '"' + "owner_id" + '"' + "= %s", userId));

        List<Pet> outStrings = new ArrayList<>();

        // Create new pet entity while row exists and add to pets list
        while (rs.next()) {
            Pet pet = new Pet();
            pet.setPetId(rs.getLong("pet_id"));
            pet.setOwnerId(rs.getLong("owner_id"));
            pet.setName(rs.getString("name"));
            pet.setSpecies(rs.getString("species"));
            pet.setGender(rs.getString("gender"));
            pet.setAge(rs.getLong("age"));
            pet.setMedicalRecord(getAllRecordsForPet(rs.getLong("pet_id")));
            pet.setVaccinations(getAllVaccinationsForPet(rs.getLong("pet_id")));
            outStrings.add(pet);
        }
        return outStrings;
    }

    public List<Pet> getVetPetsAsEntity(Long vetId) throws SQLException {

        ResultSet rs = db.SQLQueryResultSet(
                String.format("SELECT p.*, u.\"first_name\", u.\"last_name\" FROM pets p " +
                "JOIN USERS u ON u.\"user_id\" = p.\"owner_id\"" +
                "JOIN VET_ACCESS va ON p.\"pet_id\"" +
                " = va.\"pet_id\" WHERE va.\"vet_id\" = %d", vetId));

        List<Pet> outStrings = new ArrayList<>();

        // Create new pet entity while row exists and add to pets list
        while (rs.next()) {
            Pet pet = new Pet();
            pet.setPetId(rs.getLong("pet_id"));
            pet.setOwnerId(rs.getLong("owner_id"));
            pet.setOwnerName(rs.getString("first_name") + " " + rs.getString("last_name"));
            pet.setName(rs.getString("name"));
            pet.setSpecies(rs.getString("species"));
            pet.setGender(rs.getString("gender"));
            pet.setAge(rs.getLong("age"));
            pet.setMedicalRecord(getAllRecordsForPet(rs.getLong("pet_id")));
            pet.setVaccinations(getAllVaccinationsForPet(rs.getLong("pet_id")));
            outStrings.add(pet);
        }
        return outStrings;
    }

    public List<MedicalRecords> getAllRecordsForPet(Long petId) throws SQLException {
        // ResultSet rs = db.SQLQueryResultSet(String.format("SELECT * FROM medical_records where \"pet_id\"" +
        //         "= %d ORDER BY \"date\" DESC", petId));
        ResultSet rs = db.SQLQueryResultSet(
            String.format(
                "SELECT mr.\"record_id\", mr.\"pet_id\", mr.\"clinic_id\", c.\"clinic_name\", " +
                "mr.\"date\", mr.\"description\", mr.\"active_prescription_id\", ac.\"prescription_id\", " +
                "p.\"medicine_name\", mr.\"diagnosis\", mr.\"treatment\"" +
                "FROM medical_records mr " +
                "LEFT JOIN clinics c on mr.\"clinic_id\" = c.\"clinic_id\"" +
                "LEFT JOIN active_prescriptions ac on ac.\"active_prescription_id\" = mr.\"active_prescription_id\"" +
                "LEFT JOIN prescriptions p on ac.\"prescription_id\" = p.\"prescription_id\"" +
                "WHERE mr.\"pet_id\" = %d", petId)
            );

        List<MedicalRecords> outStrings = new ArrayList<>();

        // Create new record entity while row exists and add to medical records list
        while (rs.next()) {
            MedicalRecords record = new MedicalRecords();
            record.setRecordId(rs.getLong("record_id"));
            record.setPetId(rs.getLong("pet_id"));
            record.setClinicId(rs.getLong("clinic_id"));
            record.setClinicName(rs.getString("clinic_name"));
            record.setDate(rs.getDate("date"));
            record.setDescription(rs.getString("description"));

            // Check if record has a prescription attacked, if not, set to null
            if(rs.getLong("active_prescription_id") == 0) {
                record.setActivePrescriptionId(null);
            }
            else {
                record.setActivePrescriptionId(rs.getLong("active_prescription_id"));
                record.setPrescriptionId(rs.getLong("prescription_id"));
                record.setMedicineName(rs.getString("medicine_name"));
            }
            record.setDiagnosis(rs.getString("diagnosis"));
            record.setTreatment(rs.getString("treatment"));
            outStrings.add(record);
        }
        return outStrings;
    }

    public List<Vaccinations> getAllVaccinationsForPet(Long petId) throws SQLException {
        ResultSet rs = db.SQLQueryResultSet(String.format("SELECT * FROM vaccinations where \"pet_id\" = %d", petId));

        List<Vaccinations> outStrings = new ArrayList<>();

        // Create new vaccinations entity while row exists and add to list to pet entity
        // list
        while (rs.next()) {
            Vaccinations vaccinations = new Vaccinations();
            vaccinations.setVaccinationId(rs.getLong("vaccination_id"));
            vaccinations.setVaccinationName(rs.getString("vaccination_name"));
            vaccinations.setPetId(rs.getLong("pet_id"));
            vaccinations.setDate(rs.getDate("vaccination_date"));
            vaccinations.setDescription(rs.getString("description"));
            outStrings.add(vaccinations);
        }
        return outStrings;
    }

    // DELETE CREATE UPDATE METHODS
    public void deletePetByPetId(Long petId) throws SQLException {
        db.SQLQueryResultSet(String.format("DELETE FROM pets where" + '"' + "pet_id" + '"' +
                "= %d", petId));
    }

    public void createNewPet(Pet pet) throws SQLException {
        db.SQLQueryResultSet("INSERT into PETS (\"owner_id\", \"name\", \"species\", \"gender\", \"age\")" +
                "VALUES (" + pet.getOwnerId() + ",\'" + pet.getName() + "\',\'" + pet.getSpecies() +
                "\',\'" + pet.getGender() + "\',\'" + pet.getAge() + "\')");
    }

    public void updateExistingPet(Long petId, Pet pet) throws SQLException {
        db.SQLQueryResultSet("UPDATE PETS SET \"name\" = \'" + pet.getName() + "\', \"species\" = \'" + pet.getSpecies()
                +
                "\', \"gender\" = \'" + pet.getGender() + "\', \"age\" = \'" + pet.getAge() + "\' WHERE \"pet_id\"" +
                "=" + petId);
    }

    public void deleteRecord(Long recordId) throws SQLException {
        db.SQLQueryResultSet(String.format("DELETE FROM medical_records where \"record_id\"" +
                "= %d", recordId));
    }

    public void createRecord(MedicalRecords record) throws SQLException {

        // Convert Date to LocalDate
        LocalDate localDate = LocalDate.ofInstant(record.getDate().toInstant(), ZoneId.systemDefault());

        db.SQLQueryResultSet(
                "INSERT into MEDICAL_RECORDS (\"pet_id\", \"clinic_id\", \"date\", \"description\", " +
                " \"active_prescription_id\", \"diagnosis\", \"treatment\")"
                        +
                        "VALUES (" + record.getPetId() + "," + record.getClinicId() + ",\'" + localDate +
                        "\',\'" + record.getDescription() + "\'," + record.getActivePrescriptionId() +
                        ",\'" + record.getDiagnosis() + "\',\'" + record.getTreatment() + "\')");
    }

    public void updateRecord(Long recordId, MedicalRecords record) throws SQLException {
        // Convert Date to LocalDate
        LocalDate localDate = LocalDate.ofInstant(record.getDate().toInstant(), ZoneId.systemDefault());

        db.SQLQueryResultSet("UPDATE MEDICAL_RECORDS SET \"clinic_id\" = \'" + record.getClinicId() + 
                "\', \"date\" = \'" + localDate +
                "\', \"description\" = \'" + record.getDescription() +
                "\', \"active_prescription_id\" = " + record.getActivePrescriptionId() +
                ", \"diagnosis\" = \'" + record.getDiagnosis() + 
                "\', \"treatment\" = \'" + record.getTreatment() + 
                "\' WHERE \"record_id\"" +
                "=" + recordId);
    }

    public List<Clinic> getAllClinics() throws SQLException {

        ResultSet rs = db.SQLQueryResultSet(String.format("SELECT * FROM clinics"));

        List<Clinic> outStrings = new ArrayList<>();

        // Create new pet while row exists and add to pets list
        while (rs.next()) {
            Clinic clinic = new Clinic();
            clinic.setClinicId(rs.getLong(1));
            clinic.setClinicName(rs.getString(2));
            clinic.setClinicAddress(rs.getString(3));

            outStrings.add(clinic);
        }
        return outStrings;

    }
    public void updateActivePrescription(MedicalRecords record) throws SQLException {
        db.SQLQueryResultSet("UPDATE Active_PRESCRIPTIONS SET \"prescription_id\" = \'" + record.getPrescriptionId() + 
                "\' WHERE \"active_prescription_id\"" +
                "=\'" + record.getActivePrescriptionId() + "\'");
    }

    public void deleteActivePrescription(Long activePrescriptionId) throws SQLException {
        db.SQLQueryResultSet("DELETE from active_prescriptions WHERE \"active_prescription_id\"" + 
                        "= \'" + activePrescriptionId + "\'");
    }
    public long createActivePrescription(MedicalRecords record) throws SQLException {
        Long insertedActivePrescriptionID = 0L;

        // Insert active prescription into database
        db.SQLQueryResultSet(
            "INSERT into ACTIVE_PRESCRIPTIONS (\"prescription_id\", \"pet_id\")"
                + "VALUES (" + record.getPrescriptionId() + ","  + record.getPetId() + ");");

        // Get inserted active prescription ID
        // TODO Find better method to get last inserted row, this only gets highest ID of table
        ResultSet rs = db.SQLQueryResultSet(
                "SELECT \"active_prescription_id\" as \"id\" from ACTIVE_PRESCRIPTIONS ORDER BY " +
                "\"active_prescription_id\" DESC LIMIT 1;");
        while(rs.next()) {
            insertedActivePrescriptionID  = Long.valueOf(rs.getInt("id"));
        }
        return insertedActivePrescriptionID;
    }

    // VET FUNCTIONS FOR SHARING RECORDS BETWEEN VETS
    public List<Vet> getAllVets() throws SQLException {
        // Insert active prescription into database
        ResultSet rs = db.SQLQueryResultSet( "SELECT * from VETERINARIANS");

        List<Vet> vets = new ArrayList<>();

        // Create new pet while row exists and add to pets list
        while (rs.next()) {
            Vet vet = new Vet();
            vet.setVetId(rs.getLong("vet_id"));
            vet.setFirstName(rs.getString("first_name"));
            vet.setLastName(rs.getString("last_name"));
            vet.setPhoneNumber(rs.getString("phone_number"));
            vet.setEmail(rs.getString("email"));
            vet.setClinicId(rs.getLong("clinic_id"));


            vets.add(vet);
        }
        return vets;
    }


}

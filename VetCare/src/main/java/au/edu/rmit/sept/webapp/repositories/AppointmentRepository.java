package au.edu.rmit.sept.webapp.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;
import org.springframework.stereotype.Repository;

import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.utilities.DatabaseMethods;
import au.edu.rmit.sept.webapp.utilities.SQLExceptionHandler;

@Repository
public class AppointmentRepository {

    DatabaseMethods db = new DatabaseMethods();

    public List<LocalTime> getAllBookedAppointments(LocalDate date, int clinic_id, int vet_id) {
        List<LocalTime> appointmentTimes = new ArrayList<>();

        // Defines start and end dates
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        // Returns all rows of a particular appointment date, at a particular clinic
        // with a particular vet
        String query = "SELECT \"appointment_date\" FROM appointments WHERE \"appointment_date\" BETWEEN ? AND ?";
        String formattedQuery = String.format(" AND \"vet_id\" = %d AND \"clinic_id\" = %d", vet_id, clinic_id);
        query = query + formattedQuery;
        try {
            Connection connection = db.initialiseConnection();
            PreparedStatement stm = connection.prepareStatement(query);

            // Set the start and end of the day as timestamps or objects
            stm.setTimestamp(1, Timestamp.valueOf(startOfDay));
            stm.setTimestamp(2, Timestamp.valueOf(endOfDay));
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                Timestamp appointmentTimestamp = rs.getTimestamp("appointment_date");
                // Converts to localtime object
                LocalTime appointmentTime = appointmentTimestamp.toLocalDateTime().toLocalTime();
                appointmentTimes.add(appointmentTime);
            }

        } catch (SQLException e) {
            new SQLExceptionHandler(e, "Error fetching appointments for date");
        }
        return appointmentTimes;
    }

    public void bookAppointment(Appointment new_appointment) {
        // Inserts appointment information into appointments table
        db.SQLQueryResultSet(
                "INSERT into APPOINTMENTS (\"pet_id\", \"vet_id\", \"clinic_id\", \"user_id\", \"appointment_date\")"
                        +
                        "VALUES (" + new_appointment.getPet_id() + ",\'" + new_appointment.getVet_id() + "\',\'" +
                        new_appointment.getClinic_id() + "\',\'" + new_appointment.getUser_id() + "\',\'" +
                        new_appointment.getAppointment_date() + " " + new_appointment.getAppointment_time() + "\')");

    }

    // This method is the same as bookAppointment, but sometimes a user (such as a
    // new user) may not have any pets
    public void bookAppointmentWithoutPet(Appointment new_appointment) {
        // Insert appointment information into appointments table, excluding pet
        // information
        db.SQLQueryResultSet("INSERT into APPOINTMENTS (\"vet_id\", \"clinic_id\", \"user_id\", \"appointment_date\")" +
                "VALUES (\'" + new_appointment.getVet_id() + "\',\'" +
                new_appointment.getClinic_id() + "\',\'" + new_appointment.getUser_id() + "\',\'" +
                new_appointment.getAppointment_date() + " " + new_appointment.getAppointment_time() + "\')");

    }

    public Pair<List<String>, List<Integer>> getUserPetsNamesIds(Long userId) {
        try {
            // Selects all pet rows that belong to a particular owner
            ResultSet rs = db.SQLQueryResultSet(
                    String.format("SELECT * FROM pets where" + '"' + "owner_id" + '"' + "= %s", userId));

            List<String> pets = new ArrayList<>();
            List<Integer> pets_ids = new ArrayList<>();

            // Create new pet while row exists and add to pets list
            while (rs.next()) {
                pets.add(rs.getString(3));
                pets_ids.add(rs.getInt(1));
            }
            Pair<List<String>, List<Integer>> pair = Pair.of(pets, pets_ids);
            return pair;
        } catch (SQLException e) {
            new SQLExceptionHandler(e, "getUserPetsAsEntity");
            return null;
        }
    }

    public Triple<List<String>, List<Integer>, List<Float>> getClinicsDetails() {
        try {

            ResultSet rs = db.SQLQueryResultSet("SELECT * FROM clinics");

            List<String> clinics = new ArrayList<>();
            List<Integer> clinics_ids = new ArrayList<>();
            List<Float> clinics_prices = new ArrayList<>();

            // Create new pet while row exists and add to pets list
            while (rs.next()) {
                clinics.add(rs.getString(2));
                clinics_ids.add(rs.getInt(1));
                clinics_prices.add(rs.getFloat(4));
            }
            Triple<List<String>, List<Integer>, List<Float>> triplet = Triple.of(clinics, clinics_ids, clinics_prices);
            return triplet;
        } catch (SQLException e) {
            new SQLExceptionHandler(e, "getUserClinicsAsEntity");
            return null;
        }
    }
    public Triple<List<String>, List<Integer>, List<Integer>> getVetNamesClinicIds() {
        try {
            ResultSet rs = db.SQLQueryResultSet("SELECT * FROM veterinarians");

            List<String> vets = new ArrayList<>();
            List<Integer> vets_ids = new ArrayList<>();
            List<Integer> clinics_ids = new ArrayList<>();

            // Create new pet while row exists and add to pets list
            while (rs.next()) {
                vets.add(rs.getString("first_name") + " " + rs.getString("last_name"));
                vets_ids.add(rs.getInt(1));
                clinics_ids.add(rs.getInt(6));
            }
            Triple<List<String>, List<Integer>, List<Integer>> triplet = Triple.of(vets, vets_ids, clinics_ids);
            return triplet;
        } catch (SQLException e) {
            throw new UncategorizedScriptException("getUserVetsAsEntity", e);
        }
    }
    public void rescheduleAppointment(Appointment new_appointment) {
        // Inserts appointment information into appointments table, depending on whether pet_id is set or not
        String query;
        if (new_appointment.getPet_id() != null) {
            query = "UPDATE APPOINTMENTS SET \"pet_id\" = " + new_appointment.getPet_id() +", " +
            "\"vet_id\" = " + new_appointment.getVet_id() + ", \"clinic_id\" = " + new_appointment.getClinic_id() + 
            ", \"appointment_date\" = \'" + new_appointment.getAppointment_date() + " " + new_appointment.getAppointment_time() + "\' " +
            "WHERE \"appointment_id\"=" + new_appointment.getAppointment_id() + ";";
        }
        else {
            query = "UPDATE APPOINTMENTS SET " +
            "\"vet_id\" = " + new_appointment.getVet_id() + ", \"clinic_id\" = " + new_appointment.getClinic_id() + 
            ", \"appointment_date\" = \'" + new_appointment.getAppointment_date() + " " + new_appointment.getAppointment_time() + "\' " +
            "WHERE \"appointment_id\"=" + new_appointment.getAppointment_id() + ";";
        }
        db.SQLQueryResultSet(query);
    }
}

package au.edu.rmit.sept.webapp.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.javatuples.Septet;
import org.javatuples.Quintet;
import org.javatuples.Sextet;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;
import org.springframework.stereotype.Repository;

import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.utilities.DatabaseMethods;

@Repository
public class BookingListRepository {

    DatabaseMethods db = new DatabaseMethods();

    // This method returns a triplet as we want to return and display details of the appointment such as the pet and vet names
    public Sextet<List<Appointment>, List<String>, List<String>, List<String>, List<String>, List<Float>> getUserAppointments(Long userId) {
        List<Appointment> userAppointments = new ArrayList<>();
        List<String> pet_names = new ArrayList<>();
        List<String> vet_names = new ArrayList<>();
        List<String> clinic_names = new ArrayList<>();
        List<String> clinic_addresses = new ArrayList<>();
        List<Float> prices = new ArrayList<>();
        /* This query retrieves all appointments, and joins them with other tables, where pets has a left 
        join as some appointments may not have had a pet selected, so that option should be available */
        String query = "SELECT * FROM appointments as A " +
                "JOIN veterinarians as V ON V.\"vet_id\" = A.\"vet_id\"" +
                "LEFT JOIN pets AS P ON P.\"pet_id\" = A.\"pet_id\" " +
                "LEFT JOIN clinics AS C ON C.\"clinic_id\" = A.\"clinic_id\"" +
                "WHERE \"user_id\" = ?";
        //     SELECT * FROM appointments as A
        //     JOIN veterinarians as V ON V."vet_id" = A."vet_id"
        //     LEFT JOIN pets AS P ON P."pet_id" = A."pet_id"
        //    LEFT JOIN clinics AS C ON C."clinic_id" = A."clinic_id"
        try {
            Connection connection = db.initialiseConnection();
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setLong(1, userId);

            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                // Sets all appointment details, including vet and pet names
                Appointment appointment = new Appointment();
                appointment.setAppointment_id(rs.getLong("appointment_id"));
                System.out.println("Appointment ID: " + String.valueOf(appointment.getAppointment_id()));
                appointment.setPet_id(rs.getLong("pet_id"));
                appointment.setUser_id(rs.getLong("user_id"));
                appointment.setVet_id(rs.getLong("vet_id"));
                appointment.setClinic_id(rs.getLong("clinic_id"));
                appointment.setAppointment_date(rs.getString("appointment_date").split(" ")[0]);
                appointment.setAppointment_time(rs.getString("appointment_date").split(" ")[1].substring(0, 5));

                vet_names.add(rs.getString("first_name") + " " +
                        rs.getString("last_name"));
                clinic_names.add(rs.getString("clinic_name"));
                clinic_addresses.add(rs.getString("address"));
                // Check if the pet name is null, if so, add "No pet"
                String petName = rs.getString("name");
                if (petName == null || petName.isEmpty()) {
                    pet_names.add("No pet");
                } else {
                    pet_names.add(petName);
                }
                prices.add(rs.getFloat("price"));
                userAppointments.add(appointment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new UncategorizedScriptException("Error fetching appointments", e);
        }
        Sextet<List<Appointment>, List<String>, List<String>, List<String>, List<String>, List<Float>> sextet = 
        Sextet.with(userAppointments, pet_names, vet_names, clinic_names, clinic_addresses, prices);

        return sextet;
    }

    public Septet<List<Appointment>, List<String>, List<String>, List<String>, List<String>, 
    List<Float>, List<String>> getVetAppointments(Long vetId, Long user_id) {
        List<Appointment> userAppointments = new ArrayList<>();
        List<String> pet_names = new ArrayList<>();
        List<String> vet_names = new ArrayList<>();
        List<String> clinic_names = new ArrayList<>();
        List<String> clinic_addresses = new ArrayList<>();
        List<Float> prices = new ArrayList<>();
        List<String> patient_names = new ArrayList<>();
        /* This query retrieves all appointments, and joins them with other tables, where pets has a left 
        join as some appointments may not have had a pet selected, so that option should be available */
        String query = "SELECT A.\"appointment_id\",A.\"pet_id\",A.\"user_id\",A.\"vet_id\",A.\"clinic_id\",A.\"appointment_date\", " +
        "V.\"first_name\" AS \"vet_first_name\", V.\"last_name\" AS \"vet_last_name\", C.\"clinic_name\", C.\"address\", P.\"name\" AS \"pet_name\", " +
        "U.\"first_name\" AS \"patient_first_name\", U.\"last_name\" AS \"patient_last_name\",C.\"price\" FROM appointments as A " +
        "JOIN veterinarians as V ON V.\"vet_id\" = A.\"vet_id\" "+
        "LEFT JOIN pets AS P ON P.\"pet_id\" = A.\"pet_id\" " +
        "LEFT JOIN clinics AS C ON C.\"clinic_id\" = A.\"clinic_id\" " +
        "LEFT JOIN users AS U on U.\"user_id\" = A.\"user_id\" " +
                "WHERE A.\"vet_id\" = " + vetId + 
                // The following line stops the entry where the vet makes a booking with themselves from being retrieved
               " AND A.\"user_id\" != " + user_id;
        
        //   SELECT A."appointment_id",A."pet_id",A."user_id",A."vet_id",A."clinic_id",A."appointment_date", V."first_name" AS "vet_first_name", V."last_name" AS "vet_last_name", C."clinic_name", C."address", P."name" AS "pet_name", U."first_name" AS "patient_first_name", U."last_name" AS "patient_last_name" FROM appointments as A
        // JOIN veterinarians as V ON V."vet_id" = A."vet_id"
        // LEFT JOIN pets AS P ON P."pet_id" = A."pet_id"
        // LEFT JOIN clinics AS C ON C."clinic_id" = A."clinic_id"
        // LEFT JOIN users AS U on U."user_id" = A."user_id"
        
        
            ResultSet rs = db.SQLQueryResultSet(query);

            try {
                while (rs.next()) {
                    // Sets all appointment details, including vet and pet names
                    Appointment appointment = new Appointment();
                    appointment.setAppointment_id(rs.getLong("appointment_id"));
                    System.out.println("Appointment ID: " + String.valueOf(appointment.getAppointment_id()));
                    appointment.setPet_id(rs.getLong("pet_id"));
                    appointment.setUser_id(rs.getLong("user_id"));
                    appointment.setVet_id(rs.getLong("vet_id"));
                    appointment.setClinic_id(rs.getLong("clinic_id"));
                    appointment.setAppointment_date(rs.getString("appointment_date").split(" ")[0]);
                    appointment.setAppointment_time(rs.getString("appointment_date").split(" ")[1].substring(0, 5));

                    vet_names.add(rs.getString("vet_first_name") + " " +
                            rs.getString("vet_last_name"));
                    clinic_names.add(rs.getString("clinic_name"));
                    clinic_addresses.add(rs.getString("address"));
                    // Check if the pet name is null, if so, add "No pet"
                    String petName = rs.getString("pet_name");
                    if (petName == null || petName.isEmpty()) {
                        pet_names.add("No pet");
                    } 
                    else {
                        pet_names.add(petName);
                    }
                    prices.add(rs.getFloat("price"));
                    patient_names.add(rs.getString("patient_first_name") + " " +
                            rs.getString("patient_last_name"));
                    userAppointments.add(appointment);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        
            Septet<List<Appointment>, List<String>, List<String>, List<String>, List<String>, 
            List<Float>, List<String>> septet = 
            Septet.with(userAppointments, pet_names, vet_names, clinic_names, clinic_addresses, prices, patient_names);

        return septet;
    }
    
    public void cancelAppointment(Long appointment_id) {    
        // Set appointment_id parameter in query
        String query = "DELETE FROM appointments WHERE \"appointment_id\" = " + appointment_id;
    
        // Execute the DELETE appointment statement
        db.SQLQueryResultSet(query);
    }
    
    

}
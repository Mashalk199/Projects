package au.edu.rmit.sept.webapp.repositories;

import au.edu.rmit.sept.webapp.models.ActivePrescription;
import au.edu.rmit.sept.webapp.models.DeliveryInformation;
import au.edu.rmit.sept.webapp.models.Prescription;
import au.edu.rmit.sept.webapp.services.ActivePrescriptionService;
import org.springframework.jdbc.datasource.init.UncategorizedScriptException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DeliveryRepository {

    private final DataSource source;
    private final ActivePrescriptionService activePrescriptionService;

    public DeliveryRepository(DataSource source, ActivePrescriptionService activePrescriptionService) {
        this.source = source;
        this.activePrescriptionService = activePrescriptionService;
    }

    // Insert delivery information to the DB. 
    public void saveDeliveryInformation(DeliveryInformation delivery) throws SQLException {
        String query = "INSERT INTO DELIVERY_INFORMATION (\"active_prescription_id\", \"user_id\", \"delivery_status\", \"delivery_address\", \"order_date\""
                +", \"estimated_delivery_date\", \"ship_date\", \"actual_delivery_date\") "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = source.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setLong(1, delivery.getActivePrescription().getId());
            stm.setLong(2, delivery.getUserId());
            stm.setString(3, delivery.getDeliveryStatus());
            stm.setString(4, delivery.getDeliveryAddress());
            stm.setDate(5, new java.sql.Date(delivery.getOrderDate().getTime()));
            stm.setDate(6, new java.sql.Date(delivery.getEstimatedDeliveryDate().getTime()));

            // Ship date and actual delivery date can be null initially
            if (delivery.getShipDate() != null) {
                stm.setDate(7, new java.sql.Date(delivery.getShipDate().getTime()));
            } else {
                stm.setNull(7, java.sql.Types.DATE);
            }

            if (delivery.getActualDeliveryDate() != null) {
                stm.setDate(8, new java.sql.Date(delivery.getActualDeliveryDate().getTime()));
            } else {
                stm.setNull(8, java.sql.Types.DATE);
            }

            stm.executeUpdate();

        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error saving delivery information", e);
        }
    }

    // Get delivery information by userID
    public List<DeliveryInformation> getDeliveryInformationByUser(Long userId) throws SQLException {
        List<DeliveryInformation> deliveries = new ArrayList<>();
        String query = "SELECT di.\"delivery_status\", di.\"order_date\", di.\"ship_date\", di.\"estimated_delivery_date\", " +
                        "di.\"actual_delivery_date\", di.\"delivery_address\", ap.\"active_prescription_id\", p.\"medicine_name\", p.\"dosage\", p.\"frequency\", " +
                        "di.\"delivery_id\" " +
                        "FROM \"DELIVERY_INFORMATION\" di " +
                        "JOIN \"ACTIVE_PRESCRIPTIONS\" ap ON di.\"active_prescription_id\" = ap.\"active_prescription_id\" " +
                        "JOIN \"PRESCRIPTIONS\" p ON ap.\"prescription_id\" = p.\"prescription_id\" " +
                        "WHERE di.\"user_id\" = ? " +
                        "ORDER BY di.\"order_date\" DESC";


        try (Connection connection = source.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setLong(1, userId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                DeliveryInformation delivery = new DeliveryInformation();
                delivery.setDeliveryStatus(rs.getString("delivery_status"));
                delivery.setOrderDate(rs.getDate("order_date"));
                delivery.setShipDate(rs.getDate("ship_date"));
                delivery.setEstimatedDeliveryDate(rs.getDate("estimated_delivery_date"));
                delivery.setActualDeliveryDate(rs.getDate("actual_delivery_date"));
                delivery.setDeliveryAddress(rs.getString("delivery_address"));
                delivery.setId(rs.getLong("delivery_id"));
                

                // Initialize the prescription object inside activePrescription
                Prescription prescription = new Prescription();
                prescription.setMedicineName(rs.getString("medicine_name"));
                prescription.setDosage(rs.getString("dosage"));
                prescription.setFrequency(rs.getString("frequency"));

                ActivePrescription activePrescription = new ActivePrescription();
                activePrescription.setId(rs.getLong("active_prescription_id"));
                activePrescription.setPrescription(prescription);

                delivery.setActivePrescription(activePrescription);

                //Check if order status needs to be updated (TODO: change logic once HD task is started)
                LocalDate orderDate = rs.getDate("order_date").toLocalDate();
                LocalDate today = LocalDate.now();
                
                //If day after order date, set status to Shipped
                if (ChronoUnit.DAYS.between(orderDate, today) >= 1 && delivery.getDeliveryStatus().equals("Ordered")) {
                    delivery.setDeliveryStatus("Shipped");
                    delivery.setShipDate(java.sql.Date.valueOf(orderDate.plusDays(1))); // Set ship date to 1 day after order
                    delivery.setEstimatedDeliveryDate(java.sql.Date.valueOf(orderDate.plusDays(3))); // Update estimated delivery date
                }
                //If days between order date and current date is greater than 3 set status to Delivered
                if (ChronoUnit.DAYS.between(orderDate, today) >= 3 && !delivery.getDeliveryStatus().equals("Delivered")) {
                    delivery.setDeliveryStatus("Delivered");
                    delivery.setActualDeliveryDate(java.sql.Date.valueOf(orderDate.plusDays(3))); // Set delivery date to 3 days after order
                    
                    //Calculate Expiration Date of the active prescription and update it
                    // Convert java.util.Date to java.sql.Date
                    java.sql.Date sqlDeliveryDate = new java.sql.Date(delivery.getActualDeliveryDate().getTime());
                    activePrescriptionService.setExpirationDateUponDelivery(activePrescription.getId(), sqlDeliveryDate);
    
                }

                updateDeliveryInformation(delivery); // Update in DB
                deliveries.add(delivery);
            }
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error fetching delivery information", e);
        }
        return deliveries;
    }

    // Updates status, ship date, estimated delivery date and actual delivery date. Called in getDeliveryInformationByUser() when status is updated. 
    public void updateDeliveryInformation(DeliveryInformation delivery) throws SQLException {
        String query = "UPDATE DELIVERY_INFORMATION SET \"delivery_status\" = ?, \"ship_date\" = ?, \"actual_delivery_date\" = ?, \"estimated_delivery_date\" = ? "
                + "WHERE \"delivery_id\" = ?";
        try (Connection connection = source.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setString(1, delivery.getDeliveryStatus());

            // Handle ship and actual delivery dates. If dates are known (not null), will update the DB with the date. 
            if (delivery.getShipDate() != null) {
                stm.setDate(2, new java.sql.Date(delivery.getShipDate().getTime()));
            } else {
                stm.setNull(2, java.sql.Types.DATE);
            }

            if (delivery.getActualDeliveryDate() != null) {
                stm.setDate(3, new java.sql.Date(delivery.getActualDeliveryDate().getTime()));
            } else {
                stm.setNull(3, java.sql.Types.DATE);
            }

            stm.setDate(4, new java.sql.Date(delivery.getEstimatedDeliveryDate().getTime()));
            stm.setLong(5, delivery.getId());
            stm.executeUpdate();

        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error updating delivery information", e);
        }
    }

    // Get Delivery Information by ID (Currently only used in Repo Test)
    public DeliveryInformation getById(Long deliveryId) throws SQLException {
        String query = "SELECT di.*, ap.\"active_prescription_id\", p.\"medicine_name\", p.\"dosage\", p.\"frequency\" " +
                        "FROM DELIVERY_INFORMATION di " +
                        "JOIN ACTIVE_PRESCRIPTIONS ap ON di.\"active_prescription_id\" = ap.\"active_prescription_id\" " +
                        "JOIN PRESCRIPTIONS p ON ap.\"prescription_id\" = p.\"prescription_id\" " +
                        "WHERE di.\"delivery_id\" = ?";
        try (Connection connection = source.getConnection();
             PreparedStatement stm = connection.prepareStatement(query)) {

            stm.setLong(1, deliveryId);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                DeliveryInformation delivery = new DeliveryInformation();
                delivery.setId(rs.getLong("delivery_id"));
                delivery.setUserId(rs.getLong("user_id"));
                delivery.setDeliveryStatus(rs.getString("delivery_status"));
                delivery.setDeliveryAddress(rs.getString("delivery_address"));
                delivery.setOrderDate(rs.getDate("order_date"));
                delivery.setShipDate(rs.getDate("ship_date"));
                delivery.setEstimatedDeliveryDate(rs.getDate("estimated_delivery_date"));
                delivery.setActualDeliveryDate(rs.getDate("actual_delivery_date"));
                
                // Fetch the active prescription and its associated prescription details
                ActivePrescription activePrescription = new ActivePrescription();
                activePrescription.setId(rs.getLong("active_prescription_id"));
                // Fetch and set the prescription details
                Prescription prescription = new Prescription();
                prescription.setMedicineName(rs.getString("medicine_name"));
                prescription.setDosage(rs.getString("dosage"));
                prescription.setFrequency(rs.getString("frequency"));

                activePrescription.setPrescription(prescription); 
                delivery.setActivePrescription(activePrescription); 

                return delivery;
            } else {
                return null; 
            }
        } catch (SQLException e) {
            throw new UncategorizedScriptException("Error fetching delivery by ID", e);
        }
    }
}

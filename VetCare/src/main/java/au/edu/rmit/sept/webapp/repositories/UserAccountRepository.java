package au.edu.rmit.sept.webapp.repositories;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import au.edu.rmit.sept.webapp.utilities.DatabaseMethods;

import org.springframework.stereotype.Repository;

import au.edu.rmit.sept.webapp.models.UserAccount;

@Repository
public class UserAccountRepository {

    DatabaseMethods db = new DatabaseMethods();

    // Returns true if a user with the given username exists, and false otherwise.
    public boolean checkAccount(String username) throws SQLException {
        // Check that query has length 1
        return db.SQLQuery2dArray(
                String.format("SELECT * FROM users WHERE \"username\" = '%s';", username), false).size() == 1;
    }

    // Returns a user record with all fields for a given username
    public UserAccount getAccount(String username) throws SQLException {
        HashMap<String, List<String>> results = db.SQLQueryHashMap(
                String.format("SELECT * FROM users WHERE \"username\" = '%s';", username));

        if (results != null) {
            //Checks if vetId is null.
            String vetIdStr = results.get("vet_id").get(0);
            Integer vetId = null;
            // Only parse vet_id if it is not null or empty
            if (vetIdStr != null && !vetIdStr.isEmpty()) {
                vetId = Integer.parseInt(vetIdStr);
            }

            UserAccount account = new UserAccount(Integer.parseInt(results.get("user_id").get(0)),
                    results.get("username").get(0),
                    results.get("password").get(0),
                    results.get("role").get(0),
                    results.get("first_name").get(0),
                    results.get("last_name").get(0),
                    results.get("email").get(0),
                    results.get("address").get(0),
                    vetId); //vetId can be null or a value
            return account;
        }
        return null;
    }

    public void login(String username) throws SQLException {
        // Clear database and add user id
        HashMap<String, List<String>> userIdResults = db
                .SQLQueryHashMap(
                        String.format("SELECT \"user_id\" from users WHERE \"username\" = '%s';", username));
        if (userIdResults != null) {
            // Run SQL without using the returned value
            db.SQLQueryResultSet("TRUNCATE TABLE \"current_user\";");
            db.SQLQueryResultSet(String.format("INSERT INTO \"current_user\" (\"user_id\") VALUES %d;",
                    Integer.parseInt(userIdResults.get("user_id").get(0))));
        }
    }

    public void addAccount(UserAccount account) throws SQLException {
        // Run SQL without using the returned value
        db.SQLQueryResultSet(
                String.format(
                        "INSERT INTO users (\"username\", \"password\", \"role\", \"first_name\", \"last_name\", \"email\", \"address\", \"vet_id\") VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', %s);",
                        account.username(), account.password(), account.role(), account.firstName(), account.lastName(),
                        account.email(), account.address(), account.vetId()));
    }

    public int getCurrentUserID() throws SQLException {
        HashMap<String, String> userIdResults = db
                .SQLQueryHashMapSingle(String.format("SELECT * from \"current_user\""));

        // If user id exists in current user table, return it
        int userId = -1;
        if (userIdResults != null) {
            if (userIdResults.get("user_id") != null) {
                userId = Integer.parseInt(userIdResults.get("user_id"));
            }
        }
        return userId;
    }

    // Return the string of the current logged in user's username. Returns null if
    // not signed in.
    public String getCurrentUsername() throws SQLException {
        int id = this.getCurrentUserID();
        String username = null;
        if (id != -1) {
            HashMap<String, List<String>> userIdResults = db
                    .SQLQueryHashMap(String.format("SELECT \"username\" from users WHERE \"user_id\" = '%d';", id));
            if (userIdResults != null) {
                username = userIdResults.get("username").get(0);
            }
        }
        return username;

    }

    public void changePassword(String username, String email, String newPassword) throws SQLException {
        db.SQLQueryResultSet(String.format(
                "UPDATE \"USERS\" SET \"password\" = '%s' WHERE \"username\" = '%s' AND \"email\" = '%s';", newPassword,
                username, email));
    }

    public void logout() throws SQLException {
        db.SQLQueryResultSet("TRUNCATE TABLE \"current_user\"");
        // db.SQLQueryResultSet("DELETE FROM \"current_user\"");
    }

    public void deleteAccount(String username) throws SQLException {
        db.SQLQueryResultSet(String.format("DELETE FROM \"USERS\" WHERE \"username\" = '%s';", username));
    }

    public Long getCurrentUserVetID() throws SQLException {

        // Check if user is a vet. Returns -1 if user is not a vet
        Long vetId = null;
        HashMap<String, String> vetIdResults = db
                    .SQLQueryHashMapSingle(
            "SELECT u.\"vet_id\" from USERS u JOIN \"current_user\" cu on u.\"user_id\" = cu.\"user_id\"");
            if (vetIdResults != null) {
                if (vetIdResults.get("vet_id") != null) {
                    vetId = Long.valueOf(vetIdResults.get("vet_id"));
                }
            }
        return vetId;
    }

}

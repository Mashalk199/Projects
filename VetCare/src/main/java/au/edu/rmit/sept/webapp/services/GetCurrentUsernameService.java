package au.edu.rmit.sept.webapp.services;

import java.sql.SQLException;

import au.edu.rmit.sept.webapp.repositories.UserAccountRepository;

public class GetCurrentUsernameService {
    private UserAccountRepository repository;

    public GetCurrentUsernameService(UserAccountRepository repository) {
        this.repository = repository;
    }

    // Returns username of the user that's signed in. Returns null if not signed in.
    public String getCurrentUsername() {
        try {
            String user = repository.getCurrentUsername();
            return user;
        } catch (SQLException e) {
            System.err.println("SQL request failed.");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

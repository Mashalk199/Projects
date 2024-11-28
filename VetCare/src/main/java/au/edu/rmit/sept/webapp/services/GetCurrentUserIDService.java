package au.edu.rmit.sept.webapp.services;

import java.sql.SQLException;
import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.repositories.UserAccountRepository;

@Service
public class GetCurrentUserIDService {
    private UserAccountRepository repository;

    public GetCurrentUserIDService(UserAccountRepository repository) {
        this.repository = repository;
    }

    // Returns the value of the user that's signed in. Returns -1 if not signed in.
    public int getCurrentUserID() {
        try {
            int user = repository.getCurrentUserID();
            return user;
        } catch (SQLException e) {
            System.err.println("SQL request failed.");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }
    public Long getCurrentUserVetID() {
        try {
            Long vetId = repository.getCurrentUserVetID();
            return vetId;
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

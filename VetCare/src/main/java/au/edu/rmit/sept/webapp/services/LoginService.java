package au.edu.rmit.sept.webapp.services;

import java.sql.SQLException;

import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.repositories.UserAccountRepository;
import au.edu.rmit.sept.webapp.models.UserAccount;


@Service
public class LoginService {
    private UserAccountRepository repository;
    private UserNotificationService userNotificationService;

    public LoginService(UserAccountRepository repository, UserNotificationService userNotificationService) {
        this.repository = repository;
        this.userNotificationService = userNotificationService;
    }

    // Logs the user in by adding their username to the current_user table
    public boolean login(String username) {
        try {
            repository.login(username);

            // Get user ID after successful login
            int userId = repository.getCurrentUserID();
            // Trigger medication reminder check after login
            userNotificationService.checkForMedicationReminders((long) userId);
            return true;
        } catch (SQLException e) {
            System.err.println("SQL request failed.");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Error during medication reminder check.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean changePassword(String username, String email, String newPassword) {
        try {
            repository.changePassword(username, email, newPassword);
            return true;
        } catch (SQLException e) {
            System.err.println("SQL request failed.");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean logout() {
        try {
            repository.logout();
            return true;
        } catch (SQLException e) {
            System.err.println("SQL request failed.");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public UserAccount getAccount(String username) {

        try {
            return repository.getAccount(username);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        
    }
}

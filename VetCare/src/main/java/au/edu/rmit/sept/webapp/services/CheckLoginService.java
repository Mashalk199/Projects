package au.edu.rmit.sept.webapp.services;

import java.sql.SQLException;
import au.edu.rmit.sept.webapp.models.UserAccount;
import au.edu.rmit.sept.webapp.repositories.UserAccountRepository;

public class CheckLoginService {
    private UserAccountRepository repository;

    public CheckLoginService(UserAccountRepository repository) {
        this.repository = repository;
    }

    // Accepts an input of a user name and password, and returns true if:
    // - the user name exists in the database
    // - the password matches
    public boolean validateLogin(String username, String password) {
        try {
            if (repository.checkAccount(username)) {
                UserAccount account = repository.getAccount(username);
                return account != null && account.username().equals(username) && account.password().equals(password);
            }
        } catch (SQLException e) {
            System.err.println("SQL request failed.");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}

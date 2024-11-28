package au.edu.rmit.sept.webapp.services;

import java.sql.SQLException;

import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.repositories.UserAccountRepository;

@Service
public class CheckAccountService {
    private UserAccountRepository repository;

    public CheckAccountService(UserAccountRepository repository) {
        this.repository = repository;
    }

    public boolean checkAccount(String username) {
        try {
            return repository.checkAccount(username);
        } catch (SQLException e) {
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            return false;
        }
    }
}

package au.edu.rmit.sept.webapp.services;

import java.sql.SQLException;

import org.springframework.stereotype.Service;

import au.edu.rmit.sept.webapp.models.UserAccount;
import au.edu.rmit.sept.webapp.repositories.UserAccountRepository;

@Service
public class AddAccountService {
    private UserAccountRepository repository;
    private final SesEmailService sesEmailService;

    public AddAccountService(UserAccountRepository repository, SesEmailService sesEmailService) {
        this.sesEmailService = sesEmailService;
        this.repository = repository;
    }

    public boolean addAccount(String username, String password, String role, String firstName, String lastName,
            String email, String address) {
        try {
            UserAccount account = new UserAccount(0, username, password, role, firstName, lastName, email, address, 1);
            repository.addAccount(account);
            sesEmailService.sendSignUpConfirmationEmail(email);
            return true;
        } catch (SQLException e) {
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("Message: " + e.getMessage());
            return false;
        }
    }
}

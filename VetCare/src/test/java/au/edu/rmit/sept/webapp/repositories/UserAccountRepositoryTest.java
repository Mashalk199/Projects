package au.edu.rmit.sept.webapp.repositories;

import au.edu.rmit.sept.webapp.models.UserAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.flywaydb.core.Flyway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.SQLException;

@SpringBootTest
class UserAccountRepositoryTest {
    @Autowired
    private Flyway flyway;

    @Autowired
    UserAccountRepository repo;

    @BeforeEach
    void setUp() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void testAddAccount() {

        UserAccountRepository repo = new UserAccountRepository();
        UserAccount accountToAdd = new UserAccount(-1, "Robert", "4567", "admin", "Robert", "Admin",
                "robert@robert.com", "Address", 1);

        // when(mockR.getAccount("John")).thenReturn(mockAccount);
        try {
            repo.addAccount(accountToAdd);
        } catch (SQLException e) {
            assertTrue(false);
        }
    }

    @Test
    void testGetAccount() {

        UserAccountRepository repo = new UserAccountRepository();
        UserAccount expectedAccount = new UserAccount(1, "John", "1234", "admin", "Mr", "Butcher", "test@mail.com",
                "12 Third Street", null);

        try {
            assertEquals(expectedAccount, repo.getAccount("John"));
        } catch (SQLException e) {
            assertTrue(false);
        }
    }

    @Test
    void testEqualsAccount() {
        UserAccount account2 = new UserAccount(1, "John", "1234", "admin", "Mr", "Butcher", "test@mail.com",
                "12 Third Street", 1);
        UserAccount account1 = new UserAccount(1, "John", "1234", "admin", "Mr", "Butcher", "test@mail.com",
                "12 Third Street", 1);
        assertEquals(account1, account2);
    }

    @Test
    void testLoginValid() {

        UserAccountRepository repo = new UserAccountRepository();

        try {
            repo.login("John");
        } catch (SQLException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void testGetUserIDNotSignedIn() {

        UserAccountRepository repo = new UserAccountRepository();

        try {
            repo.logout();
            assertEquals(-1, repo.getCurrentUserID());
        } catch (SQLException e) {
            assertTrue(false);
        }
    }

    @Test
    void testGetUserIDSignedIn() {

        UserAccountRepository repo = new UserAccountRepository();

        try {
            repo.login("John");
            assertEquals(1, repo.getCurrentUserID());
        } catch (SQLException e) {
            assertTrue(false);
        }
    }

    @Test
    void testChangePassword() {

        UserAccountRepository repo = new UserAccountRepository();

        try {
            repo.changePassword("John", "test@mail.com", "456");

        } catch (SQLException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void testLogoutWhenLoggedOut() {
        UserAccountRepository repo = new UserAccountRepository();

        try {
            repo.logout();
            assertTrue(repo.getCurrentUserID() == -1);
            repo.logout();
            assertTrue(repo.getCurrentUserID() == -1);

        } catch (SQLException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void testLogoutWhenLoggedIn() {
        UserAccountRepository repo = new UserAccountRepository();

        try {
            repo.login("John");
            assertTrue(repo.getCurrentUserID() != -1);
            repo.logout();
            assertTrue(repo.getCurrentUserID() == -1);

        } catch (SQLException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void testDeleteAccount() {
        UserAccountRepository repo = new UserAccountRepository();

        try {
            repo.deleteAccount("John");
            assertFalse(repo.checkAccount("John"));

        } catch (SQLException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void testResetPassword() {
        UserAccountRepository repo = new UserAccountRepository();

        try {
            repo.changePassword("John", "test@mail.com", "pwd");
            // repo.validateLogin("John", "pwd");

        } catch (SQLException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

}
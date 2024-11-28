package au.edu.rmit.sept.webapp.services;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import au.edu.rmit.sept.webapp.repositories.UserAccountRepository;

public class AddAccountServiceTest {
    @Test
    void testAddAccount() {

        UserAccountRepository mockR = mock(UserAccountRepository.class);
        SesEmailService mockSES = mock(SesEmailService.class);
        AddAccountService addAccountService = new AddAccountService(mockR, mockSES);
        // when(mockR.getAccount("John")).thenReturn(mockAccount);
        assertTrue(addAccountService.addAccount("Robert", "4567", "admin", "Robert", "Admin", "robert@robert.com",
                "Address"));
    }
}

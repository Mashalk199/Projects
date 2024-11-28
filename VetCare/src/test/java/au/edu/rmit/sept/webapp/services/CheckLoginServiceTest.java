package au.edu.rmit.sept.webapp.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import au.edu.rmit.sept.webapp.models.UserAccount;
import au.edu.rmit.sept.webapp.repositories.UserAccountRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class CheckLoginServiceTest {
  // private static String URL = "/signin";

  @MockBean
  CheckLoginService service;

  private UserAccount mockAccount = new UserAccount(1, "John", "1234", "admin", "Robert", "Admin",
      "robert@robert.com", "Address", 1);
  // @Test
  // void shouldDisplayTitle() throws Exception {
  // mvc.perform(get(URL)).andExpect(status().isOk())
  // .andExpect(content().string(containsString("Our movie selection")));
  // }

  @Test
  void testValidPassword() throws Exception {
    UserAccountRepository mockR = mock(UserAccountRepository.class);
    CheckLoginService checkLoginService = new CheckLoginService(mockR);
    when(mockR.getAccount("John")).thenReturn(this.mockAccount);
    assertTrue(checkLoginService.validateLogin("John", "1234"));

    // mvc.perform(get(URL)).andExpect(status().isOk())
    // .andExpect(content().string(containsString("Reservoir")))
    // .andExpect(content().string(containsString("Leon")));
  }

  @Test
  void testInvalidUsername() throws Exception {
    UserAccountRepository mockR = mock(UserAccountRepository.class);
    CheckLoginService checkLoginService = new CheckLoginService(mockR);
    when(mockR.getAccount("John")).thenReturn(this.mockAccount);
    assertFalse(checkLoginService.validateLogin("Tim", "1234"));
  }

  @Test
  void testInvalidPassword() throws Exception {
    UserAccountRepository mockR = mock(UserAccountRepository.class);
    CheckLoginService checkLoginService = new CheckLoginService(mockR);
    when(mockR.getAccount("John")).thenReturn(this.mockAccount);
    assertFalse(checkLoginService.validateLogin("John", "xyz"));
  }

}
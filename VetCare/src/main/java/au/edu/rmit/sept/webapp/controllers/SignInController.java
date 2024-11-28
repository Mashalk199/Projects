package au.edu.rmit.sept.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import au.edu.rmit.sept.webapp.models.UserAccount;
import au.edu.rmit.sept.webapp.repositories.UserAccountRepository;
import au.edu.rmit.sept.webapp.services.CheckLoginService;
import au.edu.rmit.sept.webapp.services.LoginService;
import au.edu.rmit.sept.webapp.services.UserNotificationService;

@Controller
@RequestMapping("/sign-in")
public class SignInController {
  private final LoginService loginService;
  private final UserNotificationService userNotificationService;

  public SignInController(LoginService loginService, UserNotificationService userNotificationService) {
    this.loginService = loginService;
    this.userNotificationService = userNotificationService;
  }

  @GetMapping
  public String index(Model model) {
    // model.addAttribute("errorMessage", "errorMessage");
    return "sign-in.html";

  }

  @PostMapping
  public String submitForm(@ModelAttribute("username") String username,
      @ModelAttribute("password") String password, Model model, RedirectAttributes redirectAttributes) {

    CheckLoginService validation = new CheckLoginService(new UserAccountRepository());
    String errorMessage = "";
    if (!username.equals("") && !password.equals("")) {
      if (validation.validateLogin(username, password)) {
        if(loginService.login(username)) {

          UserAccount loginUser = loginService.getAccount(username);

          String notification = userNotificationService.checkForMedicationReminders((long)loginUser.userId());
            if (!notification.isEmpty()) {
                redirectAttributes.addFlashAttribute("notification", notification);
            }
          return "redirect:/";
        }
        
      } else {
        errorMessage += "Account details incorrect.\n";
      }
    } else {
      errorMessage += "Please enter username and password\n";
    }
    // errorMessage = "Login unsuccessful.";
    model.addAttribute("errorMessage", errorMessage);

    return "sign-in.html";
  }

}

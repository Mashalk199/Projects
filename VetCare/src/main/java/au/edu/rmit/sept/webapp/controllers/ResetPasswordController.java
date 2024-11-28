package au.edu.rmit.sept.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import au.edu.rmit.sept.webapp.repositories.UserAccountRepository;
import au.edu.rmit.sept.webapp.services.CheckAccountService;
import au.edu.rmit.sept.webapp.services.LoginService;

@Controller
@RequestMapping("/reset-password")
public class ResetPasswordController {


  private final LoginService service;

  public ResetPasswordController(LoginService service) {
    this.service = service;
  }
  @GetMapping
  public String index(Model model) {
    // model.addAttribute("errorMessage", "errorMessage");
    return "/reset-password.html";

  }

  @PostMapping
  public String submitForm(@ModelAttribute("username") String username,
      @ModelAttribute("password") String password, @ModelAttribute("email") String email, Model model) {

    CheckAccountService findAccount = new CheckAccountService(new UserAccountRepository());

    String errorMessage = "";
    if (!username.equals("") && !email.equals("") && !password.equals("")) {
      if (findAccount.checkAccount(username)) {
        if (service.changePassword(username, email, password)) {
          errorMessage = "Password updated.";
        } else {
          errorMessage += "Email incorrect";
        }

      } else {
        errorMessage += "Account name not found.\n";
      }
    } else {
      errorMessage += "Please enter all details\n";
    }
    // errorMessage = "Login unsuccessful.";
    model.addAttribute("errorMessage", errorMessage);

    return "/reset-password.html";
  }

}

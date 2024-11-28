package au.edu.rmit.sept.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import au.edu.rmit.sept.webapp.repositories.UserAccountRepository;
import au.edu.rmit.sept.webapp.services.AddAccountService;
import au.edu.rmit.sept.webapp.services.CheckAccountService;
import au.edu.rmit.sept.webapp.services.LoginService;
import au.edu.rmit.sept.webapp.services.SesEmailService;

@Controller
@RequestMapping("/sign-up")
public class SignUpController {

  private final LoginService service;
  private final SesEmailService sesEmailService;

  public SignUpController(LoginService service, SesEmailService sesEmailService) {
    this.service = service;
    this.sesEmailService = sesEmailService;

  }
  
  @GetMapping
  public String index(Model model) {
    return "sign-up.html";

  }

  @PostMapping
  public String submitForm(@ModelAttribute("username") String username,
      @ModelAttribute("password") String password,
      @ModelAttribute("confirmPassword") String confirmPassword,
      @ModelAttribute("firstName") String firstName,
      @ModelAttribute("lastName") String lastName,
      @ModelAttribute("email") String email,
      @ModelAttribute("address") String address,
      Model model,
      RedirectAttributes redirectAttributes) {

    String errorMessage = "An error occured";
    if (password.equals(confirmPassword)) {
      AddAccountService addAccountService = new AddAccountService(new UserAccountRepository(), sesEmailService);
      CheckAccountService checkAccountService = new CheckAccountService(new UserAccountRepository());

      if (!checkAccountService.checkAccount(username)) {
        if (addAccountService.addAccount(username, password, "user", firstName, lastName, email, address)) {
          service.login(username);
          return "redirect:/";
        } else {
          errorMessage = "An SQL error occured";
        }
      } else {
        errorMessage = "Username taken";
      }
    } else {
      errorMessage = "Passwords do not match";
    }

    redirectAttributes.addFlashAttribute("errorMessage", errorMessage);

    return "redirect:/sign-up";
  }

}

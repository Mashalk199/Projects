package au.edu.rmit.sept.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import au.edu.rmit.sept.webapp.repositories.UserAccountRepository;
import au.edu.rmit.sept.webapp.services.LoginService;

@Controller
@RequestMapping("/account-management")
public class AccountManagementController {

    private final LoginService service;

    public AccountManagementController(LoginService service) {
      this.service = service;
    }

    @GetMapping
    public String index(Model model) {
        return "account-management.html";

    }

    @PostMapping
    public String submitForm() {

        service.logout();

        return "redirect:/";
    }
}

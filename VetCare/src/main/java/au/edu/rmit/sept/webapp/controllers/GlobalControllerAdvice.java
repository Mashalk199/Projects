package au.edu.rmit.sept.webapp.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import au.edu.rmit.sept.webapp.repositories.UserAccountRepository;

import org.springframework.ui.Model;

import au.edu.rmit.sept.webapp.services.GetCurrentUsernameService;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addGlobalAttributes(Model model) {
        GetCurrentUsernameService service = new GetCurrentUsernameService(new UserAccountRepository());
        String username = service.getCurrentUsername();
        // System.out.println("HEY! username is currently: " + (username == null ?
        // "null" : "not null :("));
        // System.out.println("HEY! username is currently: " + username);
        model.addAttribute("navbarUsername", username);
    }
}
package au.edu.rmit.sept.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

  @GetMapping({ "/", "/index" })
  public String index(Model model) {
    model.addAttribute("message",
        "addAttribute message src\\main\\java\\au\\edu\\rmit\\sept\\webapp\\controllers\\HomeController.java");
    return "home.html";

  }
}

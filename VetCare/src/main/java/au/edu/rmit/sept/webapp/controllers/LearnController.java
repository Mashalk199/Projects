package au.edu.rmit.sept.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LearnController {

  @GetMapping("/learn")
  public String learn(Model model) {
    return "learn.html";
  }
}

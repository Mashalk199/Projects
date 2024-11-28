package au.edu.rmit.sept.webapp.controllers;

import java.util.List;
import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.services.BookingListService;
import au.edu.rmit.sept.webapp.services.GetCurrentUserIDService;

import org.javatuples.Octet;
import org.javatuples.Septet;
import org.javatuples.Sextet;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/viewBookings")
public class ViewBookingsController {
    private BookingListService service;
    private final GetCurrentUserIDService getCurrentUserIDService;

    public ViewBookingsController(BookingListService service, GetCurrentUserIDService getCurrentUserIDService) {
        this.service = service;
        this.getCurrentUserIDService = getCurrentUserIDService;

    }

    @GetMapping
    public String index(Model model) {
        // Get the current user ID using GetCurrentUserIDService
        int user_id = this.getCurrentUserIDService.getCurrentUserID();
        // Check if the user is not signed in, if not, redirect to sign-in page
        if (user_id == -1) {
            return "redirect:/sign-in";
        }
        // This string stores the redirection url the user will go to
        String redirection;
        Long vet_id = this.getCurrentUserIDService.getCurrentUserVetID();
        if (vet_id == null) {
            redirection = "/viewBookings/index.html";
        }
        else {
            // Similar function to above one, but returns all appointments related to logged-in Vet
            Septet<List<Appointment>, List<String>, List<String>, List<String>, List<String>, 
    List<Float>, List<String>> septet = this.service.getVetAppointments(Long.valueOf(vet_id), Long.valueOf(user_id));
            List<Appointment> vetAppointments = septet.getValue0();
            List<String> vet_pet_names = septet.getValue1();
            List<String> vet_vet_names = septet.getValue2();
            List<String> vet_clinic_names = septet.getValue3();
            List<String> vet_clinic_addresses = septet.getValue4();
            List<Float> vet_prices = septet.getValue5();
            for (int i = 0; i < vet_prices.size(); ++i) {
                vet_prices.set(i, vet_prices.get(i) / 2);
            }
            List<String> patient_names = septet.getValue6();
            model.addAttribute("vet_appointments", vetAppointments);
            model.addAttribute("vet_pet_names", vet_pet_names);
            model.addAttribute("vet_vet_names", vet_vet_names);
            model.addAttribute("vet_clinic_names", vet_clinic_names);
            model.addAttribute("vet_clinic_addresses", vet_clinic_addresses);
            model.addAttribute("vet_prices", vet_prices);
            model.addAttribute("patient_names", patient_names);
            redirection = "/viewBookings/vetBookings.html";
        }
            /* Retrieves tuple of user appointment objects and other information in that exact order
            for thymeleaf to unpack in the html file */
            Sextet<List<Appointment>, List<String>, List<String>, List<String>, List<String>, List<Float>> sextet = this.service
                    .getUserAppointments(Long.valueOf(user_id));
            List<Appointment> userAppointments = sextet.getValue0();
            List<String> user_pet_names = sextet.getValue1();
            List<String> user_vet_names = sextet.getValue2();
            List<String> user_clinic_names = sextet.getValue3();
            List<String> user_clinic_addresses = sextet.getValue4();
            List<Float> user_prices = sextet.getValue5();
            for (int i = 0; i < user_prices.size(); ++i) {
                user_prices.set(i, user_prices.get(i) / 2);
            }
            model.addAttribute("user_appointments", userAppointments);
            model.addAttribute("user_pet_names", user_pet_names);
            model.addAttribute("user_vet_names", user_vet_names);
            model.addAttribute("user_clinic_names", user_clinic_names);
            model.addAttribute("user_clinic_addresses", user_clinic_addresses);
            model.addAttribute("user_prices", user_prices);
        return redirection;

    }


    @GetMapping("/reschedule/{id}")
    public String reschedule(Model model, @PathVariable Long id) {
        return "redirect:/bookAppointment/reschedulePage/" + Long.toString(id);
    }
    @DeleteMapping("/cancel/{id}")
    public String cancelAppointment(@PathVariable Long id) {
        this.service.cancelAppointment(id);
        return "redirect:/viewBookings";

    }
}
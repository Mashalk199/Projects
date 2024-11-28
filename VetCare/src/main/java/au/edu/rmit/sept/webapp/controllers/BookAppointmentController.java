package au.edu.rmit.sept.webapp.controllers;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import au.edu.rmit.sept.webapp.models.Appointment;
import au.edu.rmit.sept.webapp.models.Pet;
import au.edu.rmit.sept.webapp.services.AppointmentService;
import au.edu.rmit.sept.webapp.services.GetCurrentUserIDService;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/bookAppointment")
public class BookAppointmentController {

    private final AppointmentService service;
    private final GetCurrentUserIDService getCurrentUserIDService;

    public BookAppointmentController(AppointmentService service, GetCurrentUserIDService getCurrentUserIDService) {
        this.service = service;
        this.getCurrentUserIDService = getCurrentUserIDService;
    }

    @GetMapping
    public String index(Model model) {
        // Get the current user ID using GetCurrentUserIDService
        int user_id = this.getCurrentUserIDService.getCurrentUserID();
        // Check if the user is not signed in, then redirect to sign-in page
        if (user_id == -1) {
            return "redirect:/sign-in";
        }
        // Fetch the pet names and ID's for the current user
        Pair<List<String>, List<Integer>> pair = this.service.getUserPetsNamesIds((long) user_id);
        List<String> pets_names = pair.getLeft();
        List<Integer> pets_ids = pair.getRight();
      
        // Fetch the clinic names and ID's
        Triple<List<String>, List<Integer>, List<Float>> clinic_triplet = this.service.getClinicsDetails();
        List<String> clinics_names = clinic_triplet.getLeft();
        List<Integer> clinics_ids = clinic_triplet.getMiddle();
        List<Float> clinics_prices = clinic_triplet.getRight();

        // Format prices with two decimal places
        DecimalFormat df = new DecimalFormat("#.00"); 
        List<String> formattedPrices = new ArrayList<>();
        for (Float price : clinics_prices) {
            formattedPrices.add(df.format(price));
        }

        // Fetch the vet names and ID's and clinics
        Triple<List<String>, List<Integer>, List<Integer>> vet_triplet = this.service.getVetNamesClinicIds();
        List<String> vet_names = vet_triplet.getLeft();
        List<Integer> vet_ids = vet_triplet.getMiddle();
        List<Integer> vet_clinics = vet_triplet.getRight();
        // Add the pets' names to the model for rendering in the Thymeleaf template
        model.addAttribute("pets_names", pets_names);
        model.addAttribute("pets_ids", pets_ids);  
        // Add the clinics' names to the model for rendering in the Thymeleaf template
        model.addAttribute("clinics_names", clinics_names);
        model.addAttribute("clinics_ids", clinics_ids);
        model.addAttribute("clinics_prices", formattedPrices);
        // Add the vets' names to the model for rendering in the Thymeleaf template
        model.addAttribute("vet_names", vet_names);
        model.addAttribute("vet_ids", vet_ids);
        // Adds corresponding clinic ids to the vets
        model.addAttribute("vet_clinics", vet_clinics);
        return "/bookAppointment/index.html";
    }

    /*
     * This method fetches all the available times for a selected date. So when user
     * switches
     * dates, clinics or veterinarians, the list of available times change as well
     */
    @GetMapping("/available-times")
    @ResponseBody
    public List<String> getAvailableTimes(@RequestParam("date") String date,
            @RequestParam("clinic") String clinic,
            @RequestParam("vet") String vet) {
        vet = vet.trim();
        clinic = clinic.trim();
        int vet_id = Integer.parseInt(vet);
        int clinic_id = Integer.parseInt(clinic);

        LocalDate selectedDate = LocalDate.parse(date);

        List<LocalTime> bookedTimes = this.service.getAllBookedAppointments(selectedDate, clinic_id, vet_id);
        List<LocalTime> availableTimes = generateAvailableTimeSlots();

        // Removes booked times from available times
        removeBookedTimes(availableTimes, bookedTimes);

        // Get today's date
        LocalDate currentDate = LocalDate.now();
        if (selectedDate.equals(currentDate)) {
            // Get current time
            LocalTime currentTime = LocalTime.now();
            Iterator<LocalTime> iterator = availableTimes.iterator();

            while (iterator.hasNext()) {
                LocalTime time = iterator.next();
                // Remove time slots that are before the current time
                if (time.isBefore(currentTime)) {
                    iterator.remove();
                }
            }
        }
        // Convert LocalDateTime to strings
        List<String> availableTimesAsString = new ArrayList<>();
        String formattedTime;
        for (LocalTime time : availableTimes) {
            // Format each LocalDateTime as a string and add it to the new list
            formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm"));
            availableTimesAsString.add(formattedTime);
        }
        return availableTimesAsString;
    }

    // return redirection;
    // }
    @PostMapping("/confirmBooking")
    public String handleConfirmBooking(@RequestParam("bookingDate") String date,
                                        @RequestParam("dropdownTimeOption") String time,
                                         @RequestParam("vet_id") String vet,
                                         @RequestParam("clinic_id") String clinic,
                                         @RequestParam("pet_id") String pet,
                                         Model model) {
        if (time.isEmpty()) {
            model.addAttribute("errorMessage",
                    "Please select a time for the appointment. It might have reset after selecting a vet or clinic");
            return index(model); // Re-render the booking page with the error message
        } else {
            vet = vet.trim();
            clinic = clinic.trim();
            int vet_id = Integer.parseInt(vet);
            int clinic_id = Integer.parseInt(clinic);
            int pet_id = Integer.parseInt(pet);
            // Create an Appointment object and set all data
            Appointment new_appointment = new Appointment();
            new_appointment.setAppointment_date(date);
            new_appointment.setAppointment_time(time);
            new_appointment.setClinic_id(Long.valueOf(clinic_id));
            new_appointment.setVet_id(Long.valueOf(vet_id));
            int user_id = this.getCurrentUserIDService.getCurrentUserID();
            new_appointment.setUser_id(Long.valueOf(user_id));

            // If pets were registered and therefore selected
            if (pet_id != -1) {
                new_appointment.setPet_id(Long.valueOf(pet));
                // Book/save appointment to the database
                this.service.bookAppointment(new_appointment);
            } else {
                this.service.bookAppointmentWithoutPet(new_appointment);
            }

        }
        // Redirect to a confirmation/home page
        return "redirect:/bookAppointment/appointmentSuccess";
    }

    // Success page after saving appointment
    @GetMapping("/appointmentSuccess")
    public String appointmentSuccess() {
        return "bookAppointment/success.html";
    }
    
    @GetMapping("reschedulePage/{id}")
    public String reschedulePage(Model model, @PathVariable Long id) {
        // Get the current user ID using GetCurrentUserIDService
        int user_id = this.getCurrentUserIDService.getCurrentUserID();
        // Fetch the pet names and ID's for the current user
        Pair<List<String>, List<Integer>> pair = this.service.getUserPetsNamesIds((long) user_id);
        List<String> pets_names = pair.getLeft();
        List<Integer> pets_ids = pair.getRight();
      
        // Fetch the clinic names and ID's
        Triple<List<String>, List<Integer>, List<Float>> clinic_triplet = this.service.getClinicsDetails();
        List<String> clinics_names = clinic_triplet.getLeft();
        List<Integer> clinics_ids = clinic_triplet.getMiddle();
        List<Float> clinics_prices = clinic_triplet.getRight();

        // Format prices with two decimal places
        DecimalFormat df = new DecimalFormat("#.00"); 
        List<String> formattedPrices = new ArrayList<>();
        for (Float price : clinics_prices) {
            formattedPrices.add(df.format(price));
        }

        // Fetch the vet names and ID's and clinics
        Triple<List<String>, List<Integer>, List<Integer>> vet_triplet = this.service.getVetNamesClinicIds();
        List<String> vet_names = vet_triplet.getLeft();
        List<Integer> vet_ids = vet_triplet.getMiddle();
        List<Integer> vet_clinics = vet_triplet.getRight();
        // Add the pets' names to the model for rendering in the Thymeleaf template
        model.addAttribute("pets_names", pets_names);
        model.addAttribute("pets_ids", pets_ids);  
        // Add the clinics' names to the model for rendering in the Thymeleaf template
        model.addAttribute("clinics_names", clinics_names);
        model.addAttribute("clinics_ids", clinics_ids);
        model.addAttribute("clinics_prices", formattedPrices);
        // Add the vets' names to the model for rendering in the Thymeleaf template
        model.addAttribute("vet_names", vet_names);
        model.addAttribute("vet_ids", vet_ids);
        // Adds corresponding clinic ids to the vets
        model.addAttribute("vet_clinics", vet_clinics);

        model.addAttribute("appointment_id", id);
        return "bookAppointment/reschedule.html";

    }
    @PatchMapping("/rescheduleBooking")
    public String rescheduleAppointment(
            @RequestParam("appointment_id") Long appointment_id,
            @RequestParam("bookingDate") String date,
            @RequestParam("dropdownTimeOption") String time,
            @RequestParam("vet_id") String vet,
            @RequestParam("clinic_id") String clinic,
            @RequestParam("pet_id") String pet,
            Model model) {

        if (time.isEmpty()) {
            model.addAttribute("errorMessage",
                    "Please select a time for the appointment. It might have reset after selecting a vet or clinic");
            return index(model); // Re-render the booking page with the error message
        }

        // Create new appointment object
        Appointment appointment = new Appointment();
        appointment.setAppointment_id(appointment_id);
        appointment.setAppointment_date(date);
        appointment.setAppointment_time(time);
        appointment.setClinic_id(Long.valueOf(clinic));
        appointment.setVet_id(Long.valueOf(vet));

        if (!pet.equals("-1")) {
            appointment.setPet_id(Long.valueOf(pet));
        }

        // Save the rescheduled appointment
        this.service.rescheduleAppointment(appointment);

        return "redirect:/bookAppointment/appointmentSuccess";
    }


    // Method to generate available LocalTime slots (00:00, 00:30... 09:30... 17:30)
    public static List<LocalTime> generateAvailableTimeSlots() {
        List<LocalTime> availableTimes = new ArrayList<>();
        // Sets available times as 9am-5pm
        for (int hour = 9; hour < 17; hour++) {
            // Create LocalTime for each hour at minutes 00 and 30
            availableTimes.add(LocalTime.of(hour, 0));
            availableTimes.add(LocalTime.of(hour, 30));
        }

        return availableTimes;
    }

    // Method to remove booked times from the available time slots
    public static void removeBookedTimes(List<LocalTime> availableTimes, List<LocalTime> bookedTimes) {
        Iterator<LocalTime> iterator = availableTimes.iterator();

        while (iterator.hasNext()) {
            LocalTime time = iterator.next();
            // If the current time is in the booked times, remove it from available times
            if (bookedTimes.contains(time)) {
                iterator.remove();
            }
        }
    }
}


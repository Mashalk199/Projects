package au.edu.rmit.sept.webapp.services;

import au.edu.rmit.sept.webapp.models.ActivePrescription;
import au.edu.rmit.sept.webapp.models.Prescription;
import au.edu.rmit.sept.webapp.repositories.ActivePrescriptionRepository;
import au.edu.rmit.sept.webapp.repositories.UserAccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
public class UserNotificationServiceTest {

    @Mock
    private ActivePrescriptionRepository activePrescriptionRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserNotificationService userNotificationService;

    @Test
    void shouldSendNotificationWhenPrescriptionIsNearExpiration() throws Exception {
        Long userId = 1L;

        // Mock the ActivePrescription object with a near-expiration date
        ActivePrescription activePrescription = new ActivePrescription();
        activePrescription.setId(1L);
        activePrescription.setExpirationDate(Date.valueOf(LocalDate.now().plusDays(3)));  // 3 days to expiration

        // Mocking the Prescription object within the ActivePrescription
        Prescription mockPrescription = new Prescription();
        mockPrescription.setMedicineName("Test Medicine");
        activePrescription.setPrescription(mockPrescription);  // Set the Prescription object
        // Mocking the repository response
        List<ActivePrescription> prescriptions = new ArrayList<>();
        prescriptions.add(activePrescription);

        when(activePrescriptionRepository.getActivePrescriptionsByUserId(userId)).thenReturn(prescriptions);
        when(userAccountRepository.getCurrentUsername()).thenReturn("John");

        // Call the service method to check for medication reminders
        userNotificationService.checkForMedicationReminders(userId);

        // Verify that the notification method was invoked
        verify(userAccountRepository, times(1)).getCurrentUsername(); // getCurrentUsername() is in the sendNotification method. 
        // The actual notification sending is mocked with a print statement, so we assume it works. //TODO Update test once email implemented
    }

    @Test
    void shouldNotSendNotificationWhenPrescriptionIsNotNearExpiration() throws Exception {
        Long userId = 1L;

        // Mock the ActivePrescription object with an expiration date far in the future
        ActivePrescription activePrescription = new ActivePrescription();
        activePrescription.setId(1L);
        activePrescription.setExpirationDate(Date.valueOf(LocalDate.now().plusMonths(3)));  // Far in the future

        // Mocking the repository response
        List<ActivePrescription> prescriptions = new ArrayList<>();
        prescriptions.add(activePrescription);

        when(activePrescriptionRepository.getActivePrescriptionsByUserId(userId)).thenReturn(prescriptions);

        // Call the service method to check for medication reminders
        userNotificationService.checkForMedicationReminders(userId);

        // Verify that the notification method was not invoked
        verify(userAccountRepository, never()).getCurrentUsername(); 
        // The actual notification sending is mocked with a print statement, so we assume it works. //TODO Update test once email implemented

    }
}

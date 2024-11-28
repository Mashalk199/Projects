package au.edu.rmit.sept.webapp.services;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class SesEmailService {
    private final SesClient sesClient;
    private final String fromEmail = "noreply@vetcare.space"; // Sender email (must be verified)

    public SesEmailService(SesClient sesClient) {
        this.sesClient = sesClient;
    }

    // Send sign-up confirmation email
    public void sendSignUpConfirmationEmail(String toEmail) {
        String subject = "Sign Up Confirmation";
        String bodyText = "Thank you for signing up with us! You have successfully created an account.";

        sendEmail(toEmail, subject, bodyText);
    }
    // Send order confirmation email
    public void sendOrderConfirmationEmail(String toEmail, String prescriptionName, String orderTime, String deliveryAddress) {
        String subject = "Prescription Order Confirmation";
        String bodyText = "Your prescription for " + prescriptionName + " has been successfully ordered.\n\n" +
                          "Delivery Address: " + deliveryAddress + "\n" +
                          "Order Time: " + orderTime + "\n\n" +
                          "Estimated delivery time is 2-3 business days. Thank you for your order!";

        sendEmail(toEmail, subject, bodyText);
    }
    // Send medication expiration reminder email
    public void sendMedicationReminderEmail(String toEmail, String prescriptionName, String expirationDate) {
        String subject = "Medication Expiration Reminder";
        String bodyText = "This is a friendly reminder that your prescription for " + prescriptionName + 
                          " will expire on " + expirationDate + ".\n\n" +
                          "Please take necessary action to renew or refill your medication before the expiration date.";

        sendEmail(toEmail, subject, bodyText);
    }


    // Helper method to send the email
    private void sendEmail(String toEmail, String subject, String bodyText) {
        try {
            SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                    .destination(Destination.builder().toAddresses(toEmail).build())
                    .message(Message.builder()
                            .subject(Content.builder().data(subject).build())
                            .body(Body.builder()
                                    .text(Content.builder().data(bodyText).build())
                                    .build())
                            .build())
                    .source(fromEmail)
                    .build();

            sesClient.sendEmail(sendEmailRequest);
            System.out.println("Email sent to " + toEmail + " with subject: " + subject);
        } catch (SesException e) {
            System.err.println("Error sending email: " + e.awsErrorDetails().errorMessage());
        }
    }   

}

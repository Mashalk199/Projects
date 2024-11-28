package au.edu.rmit.sept.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

@Configuration
public class AwsSesConfig {

    @Bean
    public SesClient sesClient() {
        // Initialize the SES client with your desired AWS region
        return SesClient.builder()
                .region(Region.US_EAST_1) // Replace with your region
                .build();
    }
}

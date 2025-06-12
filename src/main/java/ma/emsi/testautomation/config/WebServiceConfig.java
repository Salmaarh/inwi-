package ma.emsi.testautomation.config;

import ma.emsi.testautomation.entity.*;
import ma.emsi.testautomation.registry.WebServiceRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class WebServiceConfig {

   @Bean
    public CommandLineRunner registerWebServices(WebServiceRegistry registry) {
        return args -> {
            registry.registerWebService("CreateSubscriber", new CreateSubscriber());
            registry.registerWebService("DeleteSubscriber", new DeleteSubscriber());
            registry.registerWebService("AdjustAccount", new AdjustAccount());
            registry.registerWebService("ActiveFirst", new ActiveFirst());
           registry.registerWebService("IntegrationEnq", new IntegrationEnq());
        };
   }
}


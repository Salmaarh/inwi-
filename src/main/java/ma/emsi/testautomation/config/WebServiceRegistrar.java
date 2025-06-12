package ma.emsi.testautomation.config;
import ma.emsi.testautomation.registry.WebServiceRegistry;
import jakarta.annotation.PostConstruct;
import ma.emsi.testautomation.service.ActiveFirstService;
import ma.emsi.testautomation.service.createsubscriberService;
import ma.emsi.testautomation.service.DeleteSubscriberService;
import ma.emsi.testautomation.service.IntegrationEnqService;
import ma.emsi.testautomation.service.AdjustAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebServiceRegistrar {

    @Autowired
    private WebServiceRegistry registry;

    @Autowired
    private createsubscriberService createSubscriber;
    @Autowired
    private DeleteSubscriberService deleteSubscriber;
    @Autowired
    private AdjustAccountService adjustAccount;
    @Autowired
    private ActiveFirstService activeFirst;
    @Autowired
    private IntegrationEnqService integrationEnq;


    @PostConstruct
    public void register() {
        registry.register("CreateSubscriber", createSubscriber);
        registry.register("DeleteSubscriber", deleteSubscriber);


    }
}

package iess.pt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import iess.pt.service.SendMessages;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
@EnableJpaRepositories 
@EnableKafka
//@EnableWebMvc
@EnableAsync
public class IessServiceApplication implements CommandLineRunner {
    private final SetupStartUpDate setupStartUpDate;
    private SendMessages sendMessages;

    @Autowired
    public IessServiceApplication(SetupStartUpDate setupStartUpDate, SendMessages sendMessages) {
        this.setupStartUpDate = setupStartUpDate;
        this.sendMessages = sendMessages;
    }

    public static void main(String[] args) {
        SpringApplication.run(IessServiceApplication.class, args);
    }

    @Override
    public void run(String... args)throws Exception {
        setupStartUpDate.setup();
        //sendMessages.sendProducts("IndexID");
        sendMessages.sendEmployees("IndexID");
    }


}

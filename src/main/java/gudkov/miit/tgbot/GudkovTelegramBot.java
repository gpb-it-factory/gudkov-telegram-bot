package gudkov.miit.tgbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GudkovTelegramBot {
    public static void main(String[] args) {
        SpringApplication.run(GudkovTelegramBot.class, args);
    }
}

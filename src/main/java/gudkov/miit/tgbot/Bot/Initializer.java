package gudkov.miit.tgbot.Bot;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.time.LocalDateTime;

/**
 * Bot initializing class
 */
@Component
public class Initializer {

    private static final Logger log = LoggerFactory.getLogger(Initializer.class);

    private final Bot bot;

    public Initializer(Bot bot){
        this.bot = bot;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error("Caught exception: \" {} \" at: {}", e.getMessage(), LocalDateTime.now());
        }
    }
}


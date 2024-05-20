package gudkov.miit.tgbot.Bot;

import gudkov.miit.tgbot.Handlers.CommandHandler;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;


/**
 * Main Bot class. Contains 4 fields:
 * Logger - object for logging
 * CommandHandler - Main message handling class (for now)
 * botName - bot's name
 * botToken - bot's token
 */

@Component
public class Bot extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(Bot.class);

    private final CommandHandler commandHandler;

    private final String botName;

    private final String botToken;

    public Bot(CommandHandler commandHandler,
               @Value("${bot.token}") String botToken,
               @Value("${bot.name}") String botName){
        this.commandHandler = commandHandler;
        this.botName = botName;
        this.botToken = botToken;
    }

    /**
     * Method that watches new updates. If there is some text inside - handling, if not - afk.
     * @param update - Object from Telegram
     */

    @Override
    public void onUpdateReceived(@NonNull Update update) {
        messageValidation(update);
    }

    /**
     * Method that validates incoming update. Based on update's content type selects .
     * For now its all handled by CommandHandler but if there will be reasons to handle images - new handler will be created
     * @param update - still object from telegram passed from onUpdateReceived()
     */

    private void messageValidation(@NotNull Update update){
        if (update.getMessage().hasPhoto()){
            sendReply(CommandHandler.defaultPhotoMessgeResponce(update.getMessage().getChatId()));
        }
        if(update.getMessage().hasText() && update.hasMessage()){
            sendReply(commandHandler.handle(update));
        }
    }

    /**
     * Wrapping method to hide try-catch block
     * @param sendMessage - message that we send to user
     */

    public void sendReply(SendMessage sendMessage){
        try{
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Caught new error: {} at: {}", e.getMessage(), LocalDateTime.now());
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    /**
     * IDEA says that method is deprecated but bot isnt initializing without it
     */
    @Override
    public String getBotToken(){
        return botToken;
    }
}

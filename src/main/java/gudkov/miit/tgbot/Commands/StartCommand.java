package gudkov.miit.tgbot.Commands;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


/**
 * Bean that manages reply to /start command
 */
@Component
public class StartCommand implements Command{
    @Override
    public SendMessage reply(@NotNull Update update) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(update.getMessage().getChatId());
        replyMessage.setText("Добро пожаловать в `GudkovTelegramBank`.");
        return replyMessage;
    }

    @Override
    public String getCommand() {
        return "/start";
    }
}

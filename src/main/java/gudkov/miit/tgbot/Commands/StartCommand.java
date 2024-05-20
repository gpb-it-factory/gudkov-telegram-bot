package gudkov.miit.tgbot.Commands;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


/**
 * Bean that manages reply to /start command
 */
@Component
public class StartCommand implements Command{
    @Override
    public SendMessage reply(@NotNull long chatId) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(chatId);
        replyMessage.setText("Добро пожаловать в `GudkovTelegramBank`.");
        return replyMessage;
    }
}

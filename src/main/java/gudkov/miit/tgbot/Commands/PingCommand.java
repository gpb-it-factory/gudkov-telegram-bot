package gudkov.miit.tgbot.Commands;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Bean that manages reply to /ping command
 */
@Component
public class PingCommand implements Command{

    @Override
    public SendMessage reply(@NotNull long chatId) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(chatId);
        replyMessage.setText("pong");
        return replyMessage;
    }

}

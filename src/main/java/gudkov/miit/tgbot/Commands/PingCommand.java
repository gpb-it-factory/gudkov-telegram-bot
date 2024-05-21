package gudkov.miit.tgbot.Commands;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Bean that manages reply to /ping command
 */
@Component
public class PingCommand implements Command{

    @Override
    public SendMessage reply(@NotNull Update update) {
        SendMessage replyMessage = new SendMessage();
        replyMessage.setChatId(update.getMessage().getChatId());
        replyMessage.setText("pong");
        return replyMessage;
    }

    @Override
    public String getCommand() {
        return "/ping";
    }

}

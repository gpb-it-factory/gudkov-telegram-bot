package gudkov.miit.tgbot.Commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Interface for all bot's commands (for now)
 */
public interface Command {
    SendMessage reply(Update update);
    String getCommand();

}

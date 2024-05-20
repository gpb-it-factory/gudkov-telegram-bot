package gudkov.miit.tgbot.Commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Interface for all bot's commands (for now)
 */
public interface Command {
    SendMessage reply(long chatId);
    String getCommand();
}

package gudkov.miit.tgbot.Handlers;

import gudkov.miit.tgbot.Commands.Command;

import gudkov.miit.tgbot.Commands.PingCommand;
import gudkov.miit.tgbot.Commands.StartCommand;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
public class CommandHandler {

    private final Map<String, Command> commands;

    public CommandHandler(@Autowired PingCommand pingCommand,
                          @Autowired StartCommand startCommand){
        this.commands = Map.of(
                "/ping", pingCommand,
                "/start", startCommand);
    }

    public SendMessage handle(Update update){
        String command = update.getMessage().getText().split(" ")[0];
        Command replyCommand = commands.get(command);
        if(replyCommand != null){
            return replyCommand.reply(update.getMessage().getChatId());
        } else {
            return defaultResponce(update.getMessage().getChatId());
        }
    }

    public static SendMessage defaultResponce(@NotNull long chatId){
        SendMessage defaultReply = new SendMessage();
        defaultReply.setChatId(chatId);
        defaultReply.setText("Прости, я тебя не понимаю :(");
        return defaultReply;
    }
}

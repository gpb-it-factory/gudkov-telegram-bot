package gudkov.miit.tgbot.Handlers;

import gudkov.miit.tgbot.Commands.Command;


import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CommandHandler {

    private final Map<String, Command> commands;

    public CommandHandler(List<Command> commandList){
        this.commands = commandList.stream().collect(
                Collectors.toMap(
                        Command::getCommand,
                        Function.identity()
                ));
    }

    public SendMessage handle(@NotNull Update update){
        Command replyCommand = commands.get(getCommandFromUpdate(update));
        if(replyCommand != null){
            return replyCommand.reply(update);
        } else {
            return defaultResponce(update.getMessage().getChatId());
        }
    }

    private SendMessage defaultResponce(@NotNull long chatId){
        SendMessage defaultReply = new SendMessage();
        defaultReply.setChatId(chatId);
        defaultReply.setText("Прости, я тебя не понимаю :(");
        return defaultReply;
    }

    private String getCommandFromUpdate(Update update){
        return update.getMessage().getText().split(" ")[0];
    }

    public static SendMessage defaultPhotoMessgeResponce(@NotNull long chatId){
        SendMessage defaultReply = new SendMessage();
        defaultReply.setChatId(chatId);
        defaultReply.setText("Картинка хороша, но она мне не нужна");
        return defaultReply;
    }
}

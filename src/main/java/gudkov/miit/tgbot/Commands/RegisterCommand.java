package gudkov.miit.tgbot.Commands;

import feign.FeignException;
import gudkov.miit.tgbot.Feign.MiddleServiceApi;
import org.openapi.example.model.CreateUserRequestV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;

/**
 * Bean that manages "/register" command.
 * Inside this bean there is integration with middle-service using FeignClient.
 */

@Component
public class RegisterCommand implements Command{

    private static final Logger log = LoggerFactory.getLogger(RegisterCommand.class);

    private final MiddleServiceApi middleServiceApi;

    public RegisterCommand(MiddleServiceApi middleServiceApi){
        this.middleServiceApi = middleServiceApi;
    }

    @Override
    public SendMessage reply(Update update) {
        String username = update.getMessage().getChat().getUserName();
        long userId = update.getMessage().getFrom().getId();
        SendMessage replyMessage = SendMessage.builder().chatId(update.getMessage().getChatId()).text("").build();

        try {
            CreateUserRequestV2 createUserRequestV2 = new CreateUserRequestV2(userId,username);
            middleServiceApi.createUserV2(createUserRequestV2);
            replyMessage.setText("Вы успешно зарегистрированы в системе банка.");
        } catch (FeignException feignException){
            log.error("CreateAccountFlow exception: {} triggered at {}. Check connection with middle.", feignException.getMessage(), LocalDateTime.now());
            replyMessage.setText(handleMiddleCreateAccountError(feignException));
            return replyMessage;
        }
        log.info("User {} successfully registered at {}",username, LocalDateTime.now());
        return replyMessage;
    }

    private String handleMiddleCreateAccountError(FeignException exception){
        switch (exception.status()){
            case 409:
                return "Вы уже зарегистрированы, нет необходимости делать это снова.";
            default:
                return "Непредвиденная ошибка вышла. Попробуйте позже.";

        }
    }

    @Override
    public String getCommand() {
        return "/register";
    }
}

package gudkov.miit.tgbot.Commands;

import feign.FeignException;
import gudkov.miit.tgbot.Feign.MiddleServiceApi;
import org.openapi.example.model.CreateUserRequestV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;

/**
 * Bean that manages "/register" command.
 * Inside this bean there is integration with middle-service using FeignClient.
 * todo: add feignException response status parsing
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
        //todo; make this more readable
        SendMessage replyMessage = SendMessage.builder().chatId(update.getMessage().getChatId()).text("").build();
        try {
            CreateUserRequestV2 createUserRequestV2 = new CreateUserRequestV2(update.getMessage().getFrom().getId(),username);
            ResponseEntity<?> backend_response = middleServiceApi.createUserV2(createUserRequestV2);
            replyMessage.setText("Вы успешно зарегистрированы в системе банка.");
            log.info("User {} successfully registered at {}",username, LocalDateTime.now());
        } catch (FeignException feignException){
            replyMessage.setText("Ошибочка вышла.");
            log.error("Registration exception: {} triggered at {}", feignException.getMessage(), LocalDateTime.now());
        }
        return replyMessage;
    }

    @Override
    public String getCommand() {
        return "/register";
    }
}

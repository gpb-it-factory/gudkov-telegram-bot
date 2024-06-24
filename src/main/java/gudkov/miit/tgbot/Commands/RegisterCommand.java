package gudkov.miit.tgbot.Commands;

import feign.FeignException;
import gudkov.miit.tgbot.Feign.MiddleServiceApi;
import gudkov.miit.tgbot.Handlers.MiddleResponseHandler;
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
    private final MiddleResponseHandler responseHandler;

    public RegisterCommand(MiddleServiceApi middleServiceApi,
                           MiddleResponseHandler responseHandler){
        this.middleServiceApi = middleServiceApi;
        this.responseHandler = responseHandler;
    }

    @Override
    public SendMessage reply(Update update) {
        String username = update.getMessage().getChat().getUserName();
        long userId = update.getMessage().getFrom().getId();
        SendMessage replyMessage = SendMessage.builder().chatId(update.getMessage().getChatId()).text("").build();

        replyMessage = tryCreateUserRequest(replyMessage, userId, username);

        return replyMessage;
    }

    private SendMessage tryCreateUserRequest(SendMessage replyMessage,
                                             long userId,
                                             String username){
        try {
            //trying to access MiddleService trough FeignClient and handling response
            CreateUserRequestV2 createUserRequestV2 = new CreateUserRequestV2(userId,username);
            replyMessage = responseHandler.handleBackendCreateUserResponse(middleServiceApi.createUserV2(createUserRequestV2), replyMessage);
        } catch (FeignException feignException){
            replyMessage = responseHandler.handleFeignExceptionUserRegistration(feignException, replyMessage);
            log.error("Registration exception: {} triggered at {}", feignException.getMessage(), LocalDateTime.now());
            return replyMessage;
        }
        log.info("User {} successfully registered at {}",username, LocalDateTime.now());
        return replyMessage;
    }

    @Override
    public String getCommand() {
        return "/register";
    }
}

package gudkov.miit.tgbot.Commands;

import feign.FeignException;
import gudkov.miit.tgbot.Feign.MiddleServiceApi;
import gudkov.miit.tgbot.Handlers.MiddleResponseHandler;
import org.openapi.example.model.CreateAccountRequestV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;


/**
 * Bean that manages "/createAccount" command.
 * Inside this bean there is integration with middle-service using FeignClient.
 */
@Component
public class CreateAccountCommand implements Command{

    private static final Logger log = LoggerFactory.getLogger(CreateAccountCommand.class);
    private final MiddleServiceApi middleServiceApi;
    private final MiddleResponseHandler responseHandler;

    public CreateAccountCommand(MiddleServiceApi middleServiceApi,
                                MiddleResponseHandler responseHandler){
        this.middleServiceApi = middleServiceApi;
        this.responseHandler = responseHandler;
    }

    @Override
    public SendMessage reply(Update update) {
        String username = update.getMessage().getChat().getUserName();
        long userId = update.getMessage().getChat().getId();
        SendMessage replyMessage = SendMessage.builder().chatId(update.getMessage().getChatId()).text("").build();

        replyMessage = tryCreateAccountRequest(userId,replyMessage);

        log.info("User {} successfully created an account at {}",username, LocalDateTime.now());
        return replyMessage;
    }

    @Override
    public String getCommand() {
        return "/createAccount";
    }

    private SendMessage tryCreateAccountRequest(long userId, SendMessage replyMessage){
        try {
            CreateAccountRequestV2 createAccountRequestV2 = new CreateAccountRequestV2("My first and only awesome account");
            replyMessage = responseHandler.handleBackendCreateAccountResponse(middleServiceApi.createUserAccountV2(userId, createAccountRequestV2),replyMessage);
        } catch (FeignException feignException){
            replyMessage = responseHandler.handleFeignExceptionAccountCreation(feignException,replyMessage);
            log.error("CreateAccountFlow exception: {} triggered at {}. Check connection with middle.", feignException.getMessage(), LocalDateTime.now());
            return replyMessage;
        }
        return replyMessage;
    }
}

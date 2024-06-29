package gudkov.miit.tgbot.Commands;

import feign.FeignException;
import gudkov.miit.tgbot.Feign.MiddleServiceApi;
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

    public CreateAccountCommand(MiddleServiceApi middleServiceApi){
        this.middleServiceApi = middleServiceApi;
    }

    @Override
    public SendMessage reply(Update update) {
        String username = update.getMessage().getChat().getUserName();
        long userId = update.getMessage().getChat().getId();
        SendMessage replyMessage = SendMessage.builder().chatId(update.getMessage().getChatId()).text("").build();

        try {
            CreateAccountRequestV2 createAccountRequestV2 = new CreateAccountRequestV2("My first and only awesome account");
            middleServiceApi.createUserAccountV2(userId,createAccountRequestV2);
            replyMessage.setText("Счет создан. Посмотрите что там.");
        } catch (FeignException feignException){
            log.error("CreateAccountFlow exception: {} triggered at {}. Check connection with middle.", feignException.getMessage(), LocalDateTime.now());
            replyMessage.setText(handleMiddleCreateAccountError(feignException));
            return replyMessage;
        }

        log.info("User {} successfully created an account at {}",username, LocalDateTime.now());
        return replyMessage;
    }

    @Override
    public String getCommand() {
        return "/createAccount";
    }


    private String handleMiddleCreateAccountError(FeignException exception){
        switch (exception.status()){
            case 409:
                return "Такой счет у данного пользователя уже есть. А зачем Вам еще?";
            default:
                return "Непредвиденная ошибка вышла. Попробуйте позже.";

        }
    }
}

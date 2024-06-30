package gudkov.miit.tgbot.Commands;


import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import gudkov.miit.tgbot.Feign.MiddleServiceApi;
import org.openapi.example.model.AccountsListResponseV2Inner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDateTime;

@Component
public class CurrentBalanceCommand implements Command{

    private static final Logger log = LoggerFactory.getLogger(CurrentBalanceCommand.class);
    private final MiddleServiceApi middleServiceApi;
    private static final AccountsListResponseV2Inner[] BLANK_ACCOUNTS_LIST = {};


    public CurrentBalanceCommand(MiddleServiceApi middleServiceApi){
        this.middleServiceApi = middleServiceApi;
    }

    @Override
    public SendMessage reply(Update update) {
        long userId = update.getMessage().getChat().getId();
        SendMessage replyMessage = SendMessage.builder().chatId(update.getMessage().getChatId()).text("").build();

        try {
            replyMessage.setText(buildAccountListTextMessage(buildAccountListArray(middleServiceApi.getUsersAccountsV2(userId))));
            log.info("User with TG-id: {} successfully got his accountList at {}",userId, LocalDateTime.now());
            return replyMessage;
        } catch (FeignException feignException){
            log.error("CreateAccountFlow exception: {} triggered at {}. Check connection with middle.", feignException.getMessage(), LocalDateTime.now());
            replyMessage.setText("Непредвиденная ошибка. Попробуйте позже.");
            return replyMessage;
        }
    }


    private AccountsListResponseV2Inner[] buildAccountListArray(ResponseEntity<?> middleResponse){
        try {
            return new ObjectMapper().convertValue(middleResponse.getBody(), AccountsListResponseV2Inner[].class);
        } catch (Exception e) {
            log.error("JSON couldn't response parse into array while buildAccountListArray(). Ended with exception: {} at {}", e.getMessage(), LocalDateTime.now());
            return BLANK_ACCOUNTS_LIST;
        }
    }

    private String buildAccountListTextMessage(AccountsListResponseV2Inner[] accountList){
        StringBuilder responseBuilder = new StringBuilder("Ваши открытые счета: \n");
        if(accountList.length == 0){
            return "У вас пока нет ни одного счета. Скорее откройте!";
        } else {
            for(AccountsListResponseV2Inner account : accountList){
                responseBuilder.append(account.getAccountName()).append(": ").append(account.getAmount()).append(" руб. \n");
            }
            return responseBuilder.toString();
        }
    }

    @Override
    public String getCommand() {
        return "/currentBalance";
    }
}

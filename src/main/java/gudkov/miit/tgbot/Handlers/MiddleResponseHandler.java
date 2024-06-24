package gudkov.miit.tgbot.Handlers;

import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Component that changes replyMessage text based on HttpStatus of response.
 * Inside there is also functions that parse FeignException status code to change replyMessage text based on value;
 */
@Component
public class MiddleResponseHandler {


    public SendMessage handleBackendCreateAccountResponse(ResponseEntity<?> response, SendMessage message){
        switch (response.getStatusCode().value()){
            case 204:
                message.setText("Счет создан. Посмотрите что там.");
                break;
        }
        return message;
    }

    public SendMessage handleFeignExceptionAccountCreation(FeignException exception, SendMessage message){
        switch (exception.status()){
            case 409:
                message.setText("Такой счет у данного пользователя уже есть. А зачем Вам еще?");
                return message;
            default:
                message.setText("Непредвиденная ошибка вышла. Попробуйте позже.");
                return message;
        }
    }


    public SendMessage handleFeignExceptionUserRegistration(FeignException exception, SendMessage message){
        switch (exception.status()){
            case 409:
                message.setText("Вы уже зарегистрированы, нет необходимости делать это снова.");
                return message;
            default:
                message.setText("Непредвиденная ошибка вышла. Попробуйте позже.");
                return message;
        }
    }

    public SendMessage handleBackendCreateUserResponse(ResponseEntity<?> response, SendMessage message){
        switch (response.getStatusCode().value()){
            case 204:
                message.setText("Вы успешно зарегистрированы в системе банка.");
                break;
        }
        return message;
    }
}

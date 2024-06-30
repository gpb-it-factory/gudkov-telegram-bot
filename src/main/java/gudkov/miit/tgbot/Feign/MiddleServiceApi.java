package gudkov.miit.tgbot.Feign;

import jakarta.validation.Valid;
import org.openapi.example.model.CreateUserRequestV2;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Feign-client to communicate with middle-service
 * todo: add docker-compose for all microservices
 */
@Component
@FeignClient(name = "middleServiceAPI", url = "${middle.server.url}")
public interface MiddleServiceApi {

    @RequestMapping(method = RequestMethod.POST,
            value ="/v2/users/",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> createUserV2(@Valid @RequestBody CreateUserRequestV2 createUserRequestV2);

    @RequestMapping(method = RequestMethod.GET,
            value ="/v2/users/{id}/accounts",
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> getUsersAccountsV2(@PathVariable("id") long id);
}

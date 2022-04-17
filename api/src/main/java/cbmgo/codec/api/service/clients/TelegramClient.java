package cbmgo.codec.api.service.clients;

import feign.Headers;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "telegramService", url = "${codec.service.telegram}")
public interface TelegramClient {

    @PostMapping("/tokens/inject")
    String injectAuthenticationContext(String json);//org.springframework.security.core.Authentication authentication);

    @Headers("Authorization: {token}")
    @GetMapping("/tokens/inject3")
    String injectAuthenticationContext2(@Param("token") String token);//org.springframework.security.core.Authentication authentication);



}

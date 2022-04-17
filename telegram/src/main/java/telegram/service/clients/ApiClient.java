//package telegram.service.clients;
//
//import comum.model.dto.security.LoginFormTelegram;
//import comum.model.persistent.telegram.TradeMessage;
//import feign.Headers;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//
//@FeignClient(name = "apiService", url = "${codec.service.api}")
//public interface ApiClient {
//
//    String AUTH_TOKEN = "Authorization";
//
//    String TOKEN_TYPE = "Bearer";
//
//    @PostMapping("/auth/telegram/signin")
//    TradeMessage authenticateUser(@RequestBody LoginFormTelegram loginRequest);
//
//
//    @GetMapping("/auth/username")
//    @Headers("Content-Type: application/json")
//    String findUsernameByToken(@RequestHeader(AUTH_TOKEN) String bearerToken);
//
//
//
//}

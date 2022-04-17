package cbmgo.codec.api.service.clients;


import comum.model.dto.security.KeycloakLogin;
import comum.model.dto.security.TokenRicardo;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "KeycloakClient", url = "${codec.service.keycloak}", configuration = UrlEncodedConfiguration.class)
public interface KeyCloakClient {

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenRicardo getToken(@RequestBody Map<String, ?> codeForm);
}
package telegram.service.clients;


import comum.model.dto.security.TokenRicardo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "KeycloakClientDC", url = "${codec.service.keycloakDC}", configuration = UrlEncodedConfiguration.class)
public interface KeyCloakClientDC {

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenRicardo getToken(@RequestBody Map<String, ?> codeForm);
}
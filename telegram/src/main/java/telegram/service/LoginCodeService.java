package telegram.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import comum.model.dto.security.KeycloakLogin;
import comum.model.dto.security.TokenRicardo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telegram.service.clients.KeyCloakClientDC;

import java.util.Map;

@Service
public class LoginCodeService {

    @Autowired
    KeyCloakClientDC clientDC;

    public void getTokenRicardoDC(String code){

        KeycloakLogin login = KeycloakLogin.builder()
                .client_id("SCI")
                .client_secret("940e8973-baaf-4491-b114-2812d56920b2")
                .grant_type("authorization_code")
                .redirect_uri("http://localhost:8081/dashboard.xhtml")
                .code(code)
                .build();

        ObjectMapper oMapper = new ObjectMapper();

        // object -> Map
        Map<String, Object> map = oMapper.convertValue(login, Map.class);

        TokenRicardo token = clientDC.getToken(map);
        System.out.println(token);
    }

}

package cbmgo.codec.api.service;

import cbmgo.codec.api.service.clients.KeyCloakClient;
import cbmgo.codec.api.service.clients.KeyCloakClientDC;
import com.fasterxml.jackson.databind.ObjectMapper;
import comum.model.dto.security.KeycloakLogin;
import comum.model.dto.security.TokenRicardo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginCodeService {

    @Autowired
    KeyCloakClient client;
    @Autowired
    KeyCloakClientDC clientDC;

    public void getTokenRicardo(String code){

        KeycloakLogin login = KeycloakLogin.builder()
                .client_id("broker_react")
                .client_secret("367afb37-8884-42c3-b5b6-b455b9b7db59")
                .grant_type("authorization_code")
                .redirect_uri("http://localhost:8080/auth/home")
                .code(code)
                .build();

        ObjectMapper oMapper = new ObjectMapper();

        // object -> Map
        Map<String, Object> map = oMapper.convertValue(login, Map.class);

        TokenRicardo token = client.getToken(map);
        System.out.println(token);
    }

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

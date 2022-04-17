package telegram.ws;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import telegram.controller.AlertaController;
import telegram.controller.MensagemPadraoController;

import javax.ws.rs.ApplicationPath;

@Configuration
@Component
@ApplicationPath("tbot")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(AuthEndpoint.class);
        register(MensagemPadraoController.class);
        register(AlertaController.class);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

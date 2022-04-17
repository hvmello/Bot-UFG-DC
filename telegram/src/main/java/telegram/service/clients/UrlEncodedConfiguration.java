package telegram.service.clients;

import feign.codec.Encoder;
import feign.form.FormEncoder;
import org.springframework.context.annotation.Bean;

public class UrlEncodedConfiguration {

    @Bean
    public Encoder feignFormEncoder() {
        return new FormEncoder();
    }

}

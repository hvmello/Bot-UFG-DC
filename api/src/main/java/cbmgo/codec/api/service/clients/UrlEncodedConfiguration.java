package cbmgo.codec.api.service.clients;

import feign.codec.Encoder;
import feign.form.FormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;

public class UrlEncodedConfiguration {

    @Bean
    public Encoder feignFormEncoder() {
        return new FormEncoder();
    }

}

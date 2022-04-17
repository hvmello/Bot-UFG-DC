package cbmgo.codec.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CorsConfiguration implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    //TODO revisar esta segurança antes de por em produção
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "PATCH", "CONNECT")
                .maxAge(MAX_AGE_SECS)
                .allowedHeaders("*")
                .allowedOrigins("*")
//                .allowedOrigins("http://localhost:3000", "http://homebroker-react.herokuapp.com", "https://mercado-bolsa-b3.netlify.app")
                .allowCredentials(true)
        ;
    }

}


package telegram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.telegram.telegrambots.ApiContextInitializer;
import telegram.config.BotProperties;

@EnableConfigurationProperties(BotProperties.class)
@EntityScan(basePackages = {"comum.model"})
@EnableJpaRepositories(basePackages = {"comum.repository"})
@SpringBootApplication//(scanBasePackageClasses = {AlertaController.class, MensagemPadraoController.class, })
@EnableFeignClients
public class TelegramApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(TelegramApplication.class, args);
    }

}

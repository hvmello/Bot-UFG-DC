package cbmgo.codec.contatos;

import cbmgo.codec.contatos.service.ContatosBMService;
import comum.model.persistent.UserSicad;
import comum.repository.UserSicadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories (basePackages = "comum.repository")
@EntityScan(basePackages = {"comum.model.persistent"})
@EnableFeignClients
@SpringBootApplication
public class ContatosApplication {

    @Autowired
    UserSicadRepository userSicadRepository;
    @Autowired
    ContatosBMService contatosBMService;

    public static void main(String[] args) {
        SpringApplication.run(ContatosApplication.class, args);
    }

//    @PostConstruct
    public void create(){
        List<UserSicad> users = userSicadRepository.findAll();
        contatosBMService.createUsersKeyCloak(users);
    }

}

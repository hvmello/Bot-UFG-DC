package comum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = "comum.repository")
@EntityScan(basePackages = {"comum.model.persistent"})
@SpringBootApplication
public class ComumApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComumApplication.class, args);
	}

}

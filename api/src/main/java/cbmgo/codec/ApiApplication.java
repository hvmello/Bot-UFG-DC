package cbmgo.codec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"comum.model"})
@EnableJpaRepositories(basePackages = {"comum.repository"})
@EnableFeignClients
@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}




//	@Bean
//	@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
//	public AccessToken getAccessToken() {
//		return ((KeycloakPrincipal) getRequest().getUserPrincipal()).getKeycloakSecurityContext().getToken();
//	}
//
//	@Bean
//	@Scope(scopeName = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
//	public KeycloakSecurityContext getKeycloakSecurityContext() {
//		return ((KeycloakPrincipal) getRequest().getUserPrincipal()).getKeycloakSecurityContext();
//	}
//
//	private HttpServletRequest getRequest() {
//		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//	}

}

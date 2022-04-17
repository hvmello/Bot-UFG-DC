package telegram.service.clients;

import comum.model.dto.security.HasRole;
import comum.model.dto.security.LoginForm;
import comum.model.dto.security.SignUpForm;
import comum.model.dto.security.Token;
import comum.model.persistent.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "authService", url = "${codec.service.auth}")
public interface AuthClient {

    @PostMapping("/roles/check")
    Boolean hasRole(HasRole validate);

    @PostMapping("/signup")
    String registerUser(SignUpForm signUpRequest);

    @PostMapping("/signin")
    Token signin(LoginForm loginRequest);

    @GetMapping("/user/info/{username}")
    User getUser(@PathVariable("username") String username);




}

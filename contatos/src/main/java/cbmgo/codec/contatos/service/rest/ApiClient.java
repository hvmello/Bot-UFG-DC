package cbmgo.codec.contatos.service.rest;

import comum.model.dto.security.SignUpForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "apiService", url = "${codec.service.api}")
public interface ApiClient {

    @PostMapping("/auth/signup")
    String registerUser(@RequestBody SignUpForm signUpRequest);

}

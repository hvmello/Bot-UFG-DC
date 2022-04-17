package comum.model.dto.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginForm {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public LoginForm(SignUpForm signUpRequest){
        this.username = signUpRequest.getUsername();
        this.password = signUpRequest.getPassword();
    }

}
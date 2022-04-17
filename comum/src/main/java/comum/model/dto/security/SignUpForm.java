package comum.model.dto.security;

import comum.model.persistent.UserSicad;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {

    private String id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    @Size(min = 3, max = 50)
    private String lastName;

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(max = 60)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 3, max = 40)
    private String password;


    public SignUpForm(UserSicad u){
        this.name       = u.getNome().split(" ")[0];
        this.lastName   = u.getNome().substring(u.getNome().indexOf(" ")+1);
        this.username   = u.getUsername();
        this.password   = u.getUsername();
        this.email      = u.getEmail();
    }

}
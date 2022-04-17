package comum.model.dto.security;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HasRole {

    @NotBlank
    private String username;  //keycloak username

    @NotBlank
    private String clientId;  //Keycloak client ID

    @NotBlank
    private String role;    //Role to check
}

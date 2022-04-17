package comum.model.dto.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLogin {

    private String  keycloakId;
    private String  username;
    private String  email;
    private String  name;
    private String  nickName;
    private String  accessToken;
    private String  refreshToken;
    private String  tokenType = "Bearer";
    private Long    expiresIn;
    private Long    refreshExpiresIn;
    private String  sessionState;

}

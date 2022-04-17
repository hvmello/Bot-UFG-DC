package comum.model.dto.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenRicardo {

    @JsonProperty(value="access_token")
    private String accessToken;

    @JsonProperty(value="refresh_token")
    private String refreshToken;

    @JsonProperty(value="token_type")
    private String tokenType = "Bearer";

    @JsonProperty(value="id_token")
    private String idToken;

    @JsonProperty(value="not-before-policy")
    private String notBeforePolicy;

    @JsonProperty(value="scope")
    private String scope;

    @JsonProperty(value="expires_in")
    private Long expiresIn;

    @JsonProperty(value="refresh_expires_in")
    private Long refreshExpiresIn;

    @JsonProperty(value="session_state")
    private String sessionState;
}

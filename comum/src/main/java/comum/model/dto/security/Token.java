package comum.model.dto.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Token {

    private String userId;
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";

    private Long expiresIn;
    private Long refreshExpiresIn;
    private String sessionState;


    public Token(String token){
          this.accessToken = token;
    }

}

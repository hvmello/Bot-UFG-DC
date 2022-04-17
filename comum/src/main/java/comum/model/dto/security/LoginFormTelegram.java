package comum.model.dto.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginFormTelegram {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private Long telegramId;

}
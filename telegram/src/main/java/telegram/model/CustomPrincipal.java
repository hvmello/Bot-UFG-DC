package telegram.model;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.security.Principal;

@Value
@ToString
@AllArgsConstructor
public class CustomPrincipal implements Principal {

	private String name;

	private String password;

}

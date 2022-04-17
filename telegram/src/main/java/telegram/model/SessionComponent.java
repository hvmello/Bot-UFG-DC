package telegram.model;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionComponent implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer telegramId;

	private Authentication auth;

	public void setTelegramId(Integer telegramId) {
		this.telegramId = telegramId;
	}

	public Integer getTelegramId() {
		return telegramId;
	}

	public void setAuth(Authentication auth){this.auth = auth;}

	public Authentication getAuth() {return auth;}

}

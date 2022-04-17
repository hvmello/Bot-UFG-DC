package telegram.resource;


import comum.model.persistent.User;
import comum.repository.UserRepository;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;

public abstract class AbstractResource {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsManager manager;

    protected User authenticateUser(String username){
        UserDetails userDetails = manager.loadUserByUsername (username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername (),userDetails.getPassword (),userDetails.getAuthorities ());
        authentication.getName();

        if (authentication != null) {
            if (authentication.getPrincipal() instanceof KeycloakPrincipal) {
                AccessToken token = ((KeycloakPrincipal) authentication.getPrincipal()).getKeycloakSecurityContext().getToken();
                //Obtendo os atributos do usuário
                User user = new User();
                user.setName(token.getName());
                user.setUsername(token.getPreferredUsername());
                user.setEmail(token.getEmail());
                user.setKeycloakId(token.getSubject());

                return user;
            }
        }

        return null;
    }


    protected String getUserIdKeyCloak() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof KeycloakPrincipal) {
                KeycloakPrincipal<KeycloakSecurityContext> kcPrincipal =  (KeycloakPrincipal) authentication.getPrincipal();

                String userIdKeyCloak = kcPrincipal.getKeycloakSecurityContext().getToken().getSubject();
                return userIdKeyCloak;
            }
        }
        return null;
    }

    protected User getUserComplete() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof KeycloakPrincipal) {
                KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) authentication.getPrincipal();
                //Obtendo o usuário
                User user = userRepository.findUserByUsername( kp.getKeycloakSecurityContext().getToken().getPreferredUsername());
                return user;
            }
        }
        return null;
    }

    protected String getAcessToken() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof KeycloakPrincipal) {
                KeycloakPrincipal<KeycloakSecurityContext> kp = (KeycloakPrincipal<KeycloakSecurityContext>) authentication.getPrincipal();
                //Obtendo o token
                String token = kp.getKeycloakSecurityContext().getTokenString();
                return token;
            }
        }
        return null;
    }

}

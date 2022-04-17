package cbmgo.codec.api.resource;


import cbmgo.codec.api.service.LoginCodeService;
import cbmgo.codec.api.service.UserService;
import cbmgo.codec.api.service.clients.AuthClient;
import comum.model.dto.security.*;
import comum.model.persistent.User;
import comum.model.persistent.telegram.TradeMessage;
import comum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthenticationResource extends AbstractResource {

    @Autowired
    AuthClient authClient;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    LoginCodeService loginCodeService;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String sucessfull(HttpServletResponse httpServletResponse) {
        return "Logado com sucesso!";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public void getLogin(HttpServletResponse httpServletResponse) {
        String projectUrl = "https://auth.rendacontinua.com/auth/realms/auth_sso/protocol/openid-connect/auth?" +
                "response_type=code&client_id=broker_react&scope=openid&state=367afb37-8884-42c3-b5b6-b455b9b7db59" +
                "&redirect_uri=http://localhost:8080/auth/callback";

//
        httpServletResponse.setHeader("Location", projectUrl);
        httpServletResponse.setStatus(302);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(HttpServletResponse httpServletResponse) {
        String uri = "https://auth.rendacontinua.com/auth/realms/auth_sso/protocol/openid-connect/logout?redirect_uri=http://localhost:8080/auth/logoutok";
        httpServletResponse.setHeader("Location", uri);
        httpServletResponse.setStatus(302);
    }

    @RequestMapping(value = "/logoutok", method = RequestMethod.GET)
    public String logoutOk(HttpServletResponse httpServletResponse) {
        return "logout ok!";
    }



    @GetMapping(value = "/callback")
    public void getToken(HttpServletRequest HttpServletRequest, @RequestParam("code") String code,
//                         @RequestParam("action") String action,
                          @RequestParam("state") String state,
                         @RequestParam("session_state") String session_state ){

        loginCodeService.getTokenRicardo(code);
    }

//	@PostMapping(path = "/logged")
//	public ResponseEntity<TokenRicardo> exchangeCode(@RequestBody KeycloakLogin loginRequest) {
//		return  ResponseEntity.ok().body(keyCloakService.getToken(loginRequest));
//	}


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) throws Exception {

        Token token = authClient.signin(loginRequest);
        if(token != null ){
            UserLogin userLogin= userService.getUserLogin(loginRequest.getUsername(), token);
            return ResponseEntity.ok(userLogin);
        }
        return new ResponseEntity<>("Erro ao efetuar Login", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/telegram/signin")
    public TradeMessage authenticateUserTelegram(@Valid @RequestBody LoginFormTelegram loginRequest) {

        String retorno = null;
        Token token = authClient.signin(new LoginForm(loginRequest.getUsername(), loginRequest.getPassword()));
        if(token != null ){
            return userService.createTelegramAndUserIfNotExist(loginRequest);
        }else{
            return TradeMessage.builder()  .text("Usuário ou Senha inválidos. Não foi possível concluir sua autenticação.")
                    .chatId(loginRequest.getTelegramId())
                    .sucess(false)
                    .build();
        }
    }

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {

        try{
            String keyCloakId = authClient.registerUser(signUpRequest);
            userService.save(new User(signUpRequest, keyCloakId));  //criar usuario no sistema a partir o user_security
        }catch(Exception e){
//            Token token = authClient.signin(new LoginForm(signUpRequest));
//            if(token != null && !token.getAccessToken().isBlank()){
//
//                userRepository.save(new User(signUpRequest, token.));  //criar usuario no sistema a partir o user_security
//            }
            System.out.println(e);
        }

        return ResponseEntity.ok().body("Usuário Registrado com Sucesso!");
    }


    @GetMapping(value = "/info/{username}")
    @ResponseBody
    public User getUserInfo(@PathVariable("username") String username) {
        return authClient.getUser(username);
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserNameSimple(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        System.out.println(principal.getName());
        return principal.getName();
    }



}

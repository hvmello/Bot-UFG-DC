package telegram.resource;


import com.fasterxml.jackson.core.JsonProcessingException;
import comum.model.dto.security.*;
import comum.model.persistent.User;
import comum.model.persistent.telegram.TradeMessage;
import comum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import telegram.service.LoginCodeService;
import telegram.service.UserService;
import telegram.service.clients.AuthClient;
import telegram.service.clients.TelegramClient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthResourceDC extends AbstractResource {


    @Autowired
    AuthClient authClient;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    LoginCodeService loginCodeService;
    @Autowired
    TelegramClient telegramClient;

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String sucessfull(HttpServletResponse httpServletResponse) {
        return "Logado com sucesso!";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public void getLogin(HttpServletResponse httpServletResponse) {

        String projectUrl = "https://auth.adriano.website/auth/realms/dc_auth/protocol" +
                "/openid-connect/auth?response_type=code&scope=openid&client_id=" +
                "SCI&state=940e8973baaf4491b1142812d56920b2&redirect_uri=http://localhost:8080/callback";
        httpServletResponse.setHeader("Location", projectUrl);
        httpServletResponse.setStatus(302);
    }

    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "/";
    }

//    @RequestMapping(value = "/logout2", method = RequestMethod.GET)
//    public void logout2(@RequestParam String redirect_uri, HttpServletResponse httpServletResponse) {
//        String uri = "https://auth.rendacontinua.com/auth/realms/auth_sso/protocol/openid-connect/logout?redirect_uri="+redirect_uri;
//        httpServletResponse.setHeader("Location", uri);
//        httpServletResponse.setStatus(200);
//    }

    @RequestMapping(value = "/logoutok", method = RequestMethod.GET)
    public String logoutOk(HttpServletResponse httpServletResponse) {
        return "logout ok!";
    }



    @GetMapping(value = "/callback")
    public void getToken(@RequestParam("code") String code,
//                         @RequestParam("action") String action,
                          @RequestParam("state") String state,
                         @RequestParam("session_state") String session_state ){

        loginCodeService.getTokenRicardoDC(code);
    }


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



    @GetMapping("/telegram/login")
    public void customers(Principal principal, HttpServletResponse httpServletResponse,
                          Model model) throws JsonProcessingException, URISyntaxException {

        model.addAttribute("principal",principal);
//
//        System.out.println(principal.getName());
        String uri = "http://localhost:8080/username";
        httpServletResponse.setHeader("Location", uri);
        httpServletResponse.setStatus(301);

//
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//
////        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String json = objectMapper.writeValueAsString(auth);

        System.out.println("User ID KeyCloak"+getUserIdKeyCloak());
//        telegramClient.injectAuthenticationContext("Bearer "+getAcessToken());
//        postTelegram("Bearer "+getAcessToken());
        getTelegram("Bearer "+getAcessToken());
    }

    @GetMapping("/telegram/login2")
    public void telegramLogin(Principal principal, HttpServletResponse httpServletResponse,
                          Model model) throws JsonProcessingException, URISyntaxException {

        model.addAttribute("principal",principal);
        String uri = "http://localhost:8083/tokens/inject3";
        httpServletResponse.setHeader("Location", uri);
        httpServletResponse.setHeader("Authorization", "Bearer "+getAcessToken());
        httpServletResponse.setStatus(301);

        System.out.println(getAcessToken());


    }

    public void postTelegram(String accessToken) throws URISyntaxException {

        String url = "http://localhost:8083/tokens/inject3";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        var restTemplate = new RestTemplate();
        var resp = restTemplate.exchange(RequestEntity.get(new URI(url)).headers(headers).build(), String.class);
        System.out.println(resp);
    }

    public void getTelegram(String accessToken) throws URISyntaxException {

        String url = "http://localhost:8083/tokens/inject3";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        var restTemplate = new RestTemplate();
        var resp = restTemplate.getForObject(url, String.class);
        System.out.println(resp);
    }





    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {

        try{
            String keyCloakId = authClient.registerUser(signUpRequest);
            userRepository.save(new User(signUpRequest, keyCloakId));  //criar usuario no sistema a partir o user_security
        }catch(Exception e){
//            Token token = authClient.signin(new LoginForm(signUpRequest));
//            if(token != null && !token.getAccessToken().isBlank()){
//
//                userRepository.save(new User(signUpRequest, token.));  //criar usuario no sistema a partir o user_security
//            }
        }

        return ResponseEntity.ok().body("Usuário Registrado com Sucesso!");
    }


    @GetMapping(value = "/info/{username}")
    @ResponseBody
    public User getUserInfo(@PathVariable("username") String username) {
        return authClient.getUser(username);
    }



    @RequestMapping(value = "/logged", method = RequestMethod.GET)
    @ResponseBody
    public String setToken(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return principal.getName();
    }


}

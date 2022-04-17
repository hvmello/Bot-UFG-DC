//package telegram.resource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.session.SessionRegistry;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.inject.Qualifier;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//@RestController
//@RequestMapping(value = "/auth")
//public class AuthResource extends AbstractResource {
//
//    @Autowired
//    private SessionRegistry sessionRegistry;
//
//
//    @RequestMapping(value = "/all", method = RequestMethod.GET)
//    public void checkPermission() {
//
//
//        List<Object> principals = sessionRegistry.getAllPrincipals();
//
//        List<String> usersNamesList = new ArrayList<String>();
//
//        for (Object principal: principals) {
//            if (principal instanceof User) {
//                usersNamesList.add(((User) principal).getUsername());
//            }
//        }
//    }
//
//
//
//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public void getLogin(HttpServletResponse httpServletResponse) {
//
////        String projectUrl = "https://auth.adriano.website/auth/realms/dc_auth/protocol" +
////                "/openid-connect/auth?response_type=code&scope=openid&client_id=" +
////                "SCI&state=940e8973baaf4491b1142812d56920b2&redirect_uri=http://localhost:8083/telegram/logged";
//
//        String urlLogin = "https://auth.adriano.website/auth/realms/dc_auth/protocol/openid-connect/auth?" +
//                "client_id=codec-cli&response_mode=fragment&response_type=code&login=true&redirect_uri=http://localhost:8083/auth/logged";
//        httpServletResponse.setHeader("Location", urlLogin);
//        httpServletResponse.setStatus(302);
//    }
//
//    @RequestMapping(value = "/logged", method = RequestMethod.GET)
//    public String writeLogged(HttpServletResponse httpServletResponse, HttpServletRequest request) throws MalformedURLException {
//
//
//        HttpSession session = request.getSession();
//        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
//
//
//
//        URL myURL = new URL("https://t.me/DefesaCivilCodecBot");
//
//        return "Login realizado com sucesso! Clique aqui para voltar ao Telegram\n" +
//                "<a href="+myURL+">DefesaCivilCodecBot</a>";
//    }
//
//
//
//    @GetMapping(path = "/logout")
//    public void logout(HttpServletResponse httpServletResponse) throws ServletException {
//        String uri = "https://auth.adriano.website/auth/realms/dc_auth/protocol/openid-connect/logout?redirect_uri=http://localhost:8083/auth/logoutok";
//        httpServletResponse.setHeader("Location", uri);
//        httpServletResponse.setStatus(302);
//    }
//
//    @RequestMapping(value = "/logoutok", method = RequestMethod.GET)
//    public String logoutOk(HttpServletResponse httpServletResponse) {
//        return "logout ok!";
//    }
//
//
//
//
////    setar manualmente login do usuario
////    public void login(HttpServletRequest req, String user, String pass) {
////        UsernamePasswordAuthenticationToken authReq
////                = new UsernamePasswordAuthenticationToken(user, pass);
////        Authentication auth = authManager.authenticate(authReq);
////
////        SecurityContext sc = SecurityContextHolder.getContext();
////        sc.setAuthentication(auth);
////        HttpSession session = req.getSession(true);
////        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
////    }
//
//
//
//
//
//}

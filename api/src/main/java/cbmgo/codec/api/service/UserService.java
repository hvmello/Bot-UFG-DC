package cbmgo.codec.api.service;


import cbmgo.codec.api.resource.AbstractResource;
import cbmgo.codec.api.service.clients.AuthClient;
import comum.model.dto.security.LoginFormTelegram;
import comum.model.dto.security.Token;
import comum.model.dto.security.UserLogin;
import comum.model.persistent.Telegram;
import comum.model.persistent.User;
import comum.model.persistent.telegram.TradeMessage;
import comum.repository.TelegramRepository;
import comum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService extends AbstractResource {


    @Autowired
    UserRepository userRepository;
    @Autowired
    TelegramRepository telegramRepository;
    @Autowired
    AuthClient authClient;




    public User findUserByUsername(String username){
        return userRepository.findUserByUsername(username.toLowerCase());
    }

    public UserLogin getUserLogin(String username, Token token ){
        User user = findUserByUsername(username);

        return UserLogin.builder()
                .keycloakId(user.getKeycloakId())
                .username(username)
                .nickName(user.getNickname())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .expiresIn(token.getExpiresIn())
                .refreshExpiresIn(token.getRefreshExpiresIn())
                .sessionState(token.getSessionState())
                .tokenType("Bearer")
                .build();
    }

    @Transactional
    public TradeMessage createTelegramAndUserIfNotExist(LoginFormTelegram loginRequest){


        try{

            //buscar o usuário vinculado ao telegram ou cadastra-lo
            User user = Optional.ofNullable(findUserByUsername(loginRequest.getUsername()))
                        .orElseGet(() -> save(authClient.getUser(loginRequest.getUsername())));

            //vincular o telegram ao usuário
            Optional<User> userTelegramCheck = userRepository.findByTelegramId(loginRequest.getTelegramId());
            if(userTelegramCheck.isPresent() && !user.getId().equals(userTelegramCheck.get().getId())){
                return TradeMessage.builder() .text("Lamento. Não foi possível vincular seu Telegram a este usuário!" +
                        "\nEste Telegram já está vinculado ao usuário "+userTelegramCheck.get().getUsername()+"."+
                        "\nPara vincular este Telegram ao usuário "+loginRequest.getUsername()+" é necessário excluir o vínculo anterior através do Sistema(pelo website)." +
                        "\nQualquer dúvida, procure o Administrador do Sistema.")
                        .chatId(loginRequest.getTelegramId())
                        .sucess(false)
                        .build();
            }


            Telegram telegram = Optional.ofNullable(telegramRepository.findByUserIdAndTelegramId(user.getId(), loginRequest.getTelegramId()))
                    .orElseGet(() -> telegramRepository.save(new Telegram(loginRequest,user)));
            if(telegram.getTelegramId() != null) {
                return TradeMessage.builder()  .text("Seja bem-vindo(a) "+ user.getName() +"!")
                                        .chatId(loginRequest.getTelegramId())
                                        .sucess(true)
                                        .build();
            }
        }catch (Exception e){



        }

        return TradeMessage.builder()  .text("Lamento. Não foi possível vincular seu Telegram a este usuário. Procure o Administrador do Sistema.")
                .chatId(loginRequest.getTelegramId())
                .sucess(false)
                .build();
    }

    public User save(User user){

        return userRepository.save(user);
    }


}

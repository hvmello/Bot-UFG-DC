package comum.repository;


import comum.model.persistent.User;
import feign.Param;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u FROM User u JOIN Telegram t WHERE t.id =:telegram_id AND u = t.user")
    Optional<User> findByTelegramId(@Param("telegram_id") Long telegram_id);

    User findUserByKeycloakId(String KeycloakId);

    User findUserByUsername(String username);

    @Query(value = "SELECT u.* FROM public.users u WHERE u.email = :email", nativeQuery = true)
    User findUserByEmail(@Param("email") String email);


}

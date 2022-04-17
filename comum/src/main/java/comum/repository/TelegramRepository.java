package comum.repository;


import comum.model.persistent.Telegram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

@Repository
public interface TelegramRepository extends JpaRepository<Telegram, Long> {

    List<Telegram> findAllByUser_Id(Long userId);

    @Query(value = "SELECT * FROM public.telegram t WHERE t.user_pk = ?1", nativeQuery = true)
    Optional<Telegram> findByUserId(@Param("userId") Long userId);

    Telegram findByUserIdAndTelegramId(Long userId, Long telegramId);

    @Query("SELECT t.telegramId FROM Telegram t  JOIN t.user u  WHERE u.id = t.user.id AND u.id =:userId")
    List<Long> findTelegramIdsByUserId(Long userId);

    Telegram findByTelegramId(Long telegramId);

}

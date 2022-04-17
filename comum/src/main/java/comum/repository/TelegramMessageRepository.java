package comum.repository;


import comum.model.persistent.telegram.TelegramMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramMessageRepository extends JpaRepository<TelegramMessage, Long> {

    TelegramMessage findFirstByChatIdOrderByInsertedDesc(String chatId);
}

package comum.repository;


import comum.model.persistent.telegram.TelegramEditMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramEditMessageRepository extends JpaRepository<TelegramEditMessage, Long> {

    TelegramEditMessage findFirstByChatIdAndMessageIdOrderByIdDesc(Long chatId, Integer messageId);

}

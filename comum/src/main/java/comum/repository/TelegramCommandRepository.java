package comum.repository;


import comum.model.persistent.telegram.TelegramCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramCommandRepository extends JpaRepository<TelegramCommand, Long> {


    public TelegramCommand findByCommand(String command);
}

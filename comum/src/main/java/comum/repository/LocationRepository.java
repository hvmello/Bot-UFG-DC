package comum.repository;


import comum.model.persistent.Alerta;
import comum.model.persistent.User;
import comum.model.persistent.telegram.Location;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(value = "SELECT * FROM codec.chatbot.telegram_location WHERE latitude <= :latTop and latitude >= :latBot" +
            " and longitude >= :longLeft and longitude <= :longRight" , nativeQuery = true)
    List<Location> findAllLocationsInsideRectangle(@Param("latTop") Float latTop,
                                                   @Param("latBot") Float latBot,
                                                   @Param("longLeft") Float longLeft,
                                                   @Param("longRight")Float longRight);

}

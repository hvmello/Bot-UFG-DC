package comum.repository;


import comum.model.persistent.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    @Query(value = "SELECT * FROM codec.public.alerta ORDER BY id_alerta DESC LIMIT 5", nativeQuery = true)
    List<Alerta> findTop5ByIdAlerta();

}

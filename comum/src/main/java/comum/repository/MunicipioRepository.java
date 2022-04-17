package comum.repository;

import comum.model.persistent.Municipio;
import comum.model.persistent.rural.Propriedade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Long> {



}

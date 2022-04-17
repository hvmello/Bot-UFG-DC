package comum.repository;


import comum.model.persistent.rural.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColaboradorRepository  extends JpaRepository<Colaborador, Long> {

}

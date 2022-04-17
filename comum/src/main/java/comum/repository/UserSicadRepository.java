package comum.repository;


import comum.model.persistent.UserSicad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSicadRepository extends JpaRepository<UserSicad, Long> {


}

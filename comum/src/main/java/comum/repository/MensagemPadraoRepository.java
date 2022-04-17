package comum.repository;


import comum.model.enums.EnumTipoMensagem;
import comum.model.persistent.Alerta;
import comum.model.persistent.MensagemPadrao;
import comum.model.persistent.User;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensagemPadraoRepository extends JpaRepository<MensagemPadrao, Long> {


    @Query(value = "SELECT * FROM codec.public.mensagempadrao where tipoalerta ILIKE :padraoMensagem ", nativeQuery = true)
    MensagemPadrao findMensagemPadraoByTipo(@Param("padraoMensagem") String padraoMensagem);
}

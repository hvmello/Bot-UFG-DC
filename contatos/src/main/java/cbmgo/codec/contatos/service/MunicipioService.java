package cbmgo.codec.contatos.service;

import comum.model.persistent.Municipio;
import comum.repository.MunicipioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MunicipioService {

    @Autowired
    MunicipioRepository repository;

    String findMunicipioNome(Long id){

        Optional<Municipio> municipio = repository.findById(id);
        if(municipio.isPresent()){
            return municipio.get().getMunicipio();
        }else{
            return null;
        }

    }
}

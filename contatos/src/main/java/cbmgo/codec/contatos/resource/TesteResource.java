package cbmgo.codec.contatos.resource;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contatos")
public class TesteResource {



    @GetMapping
    public String getFromContatos(){
        return "Acessou os contatos";
    }
}

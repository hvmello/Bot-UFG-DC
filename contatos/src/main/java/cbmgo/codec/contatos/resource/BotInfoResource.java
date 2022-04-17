package cbmgo.codec.contatos.resource;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Log4j2
@RestController
@RequestMapping("/bot")
public class BotInfoResource {


    @PostMapping(path = "/find/rg")
    @ResponseStatus(HttpStatus.CREATED)
    public String findRG(@RequestBody String json, HttpServletResponse response) throws Exception {

        log.info(json);
        return "Buscar RG";
    }
}

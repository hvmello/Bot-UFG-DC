package cbmgo.codec.contatos.resource;

import cbmgo.codec.contatos.service.ContatosBMService;
import cbmgo.codec.contatos.service.ContatosRuralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/users")
public class UserResource {

    @Autowired
    ContatosBMService contatosBMService;
    @Autowired
    ContatosRuralService contatosRuralService;

    //Dados fornecidos pelo QLINK/BM6 (extraidos pelo CAP Ricardo)
    @PostMapping(value = {"/sicad", "/sicad/{obm}"}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@RequestPart(value = "file") MultipartFile file, @PathVariable(name = "obm", required = false) String obm) {
        System.out.println("Use feign Call service, file upload");
        return contatosBMService.saveContatos(file, obm);
    }

    //Dados da Patrulha Rural da Polícia Militar
    @Transactional
    @PostMapping(value = "/rural/propriedades/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadPropriedadesRurais(@RequestPart(value = "file") MultipartFile file) {
        System.out.println("Import Propriedades Rurais");
        return contatosRuralService.savePropriedadesRurais(file);
    }

    @PostMapping(value = "/rural/colaboradores/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadColaboradoresRurais(@RequestPart(value = "file") MultipartFile file) {
        System.out.println("Import Colaboradores Rurais PM");
        return contatosRuralService.saveColaboradores(file);
    }


    //Dados do IBGE
    @PostMapping(value = "/ibge/municipios/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadMunicipiosIBGE(@RequestPart(value = "file") MultipartFile file) {
        System.out.println("Import Municipios do IBGE");
        return contatosRuralService.saveMunicipiosFromIBGE(file);
    }



    @GetMapping(path = "/{nome}")
    public String getTelefone(@RequestParam String nome){
        return "Retorno da API Contatos => "+nome;
    }

//    @PostMapping(value = "/user/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    String upload(@Param("name") String name, @Param("file") File file){
//        System.out.println("Use feign Call service, file upload");
//        contatosService.saveContatos(file);
//
//        return "Acessou contatos. Excel chegou!";
//    }
}

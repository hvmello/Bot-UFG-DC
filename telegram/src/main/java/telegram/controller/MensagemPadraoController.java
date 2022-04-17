package telegram.controller;

import comum.model.persistent.MensagemPadrao;
import comum.repository.MensagemPadraoRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import telegram.responses.Response;
import telegram.service.TelegramBotService;

import javax.validation.Valid;
import javax.ws.rs.*;
import java.util.List;
import java.util.Optional;


@RestController
@Path("/")
@CrossOrigin(allowedHeaders = "*", maxAge = 86400, origins = "*")
public class MensagemPadraoController {

    @Autowired
    private MensagemPadraoRepository mensagemPadraoRepository;

    @GET
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Localiza todos as mensagens", notes = "Localiza todas as mensagens", response = MensagemPadrao.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Mensagens listadas com sucesso"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
    @RequestMapping(value = "/mensagempadrao", method = RequestMethod.GET)
    public List<MensagemPadrao> Get() {
        return mensagemPadraoRepository.findAll();
    }

    @GET
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Localiza uma Mensagem Padrao pelo Código", notes = "Localiza uma Mensagem Padrao pelo Código", response = MensagemPadrao.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "MensagemPadrao localizado com sucesso")
    })
    @RequestMapping(value = "/mensagempadrao/{id}", method = RequestMethod.GET)
    public ResponseEntity<MensagemPadrao> GetById(@PathVariable(value = "id") long id) {

        Optional<MensagemPadrao> mensagemPadrao = mensagemPadraoRepository.findById(id);
        return mensagemPadrao.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @POST
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Insere uma Mensagem Padrão", notes = "Insere uma Mensagem Padrão", response = MensagemPadrao.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Inserção da Mensagem Padrão feita com sucesso")
    })
    @RequestMapping(value = "/mensagempadrao", method = RequestMethod.POST)
    public ResponseEntity<Response<MensagemPadrao>> Post(@Valid @RequestBody MensagemPadrao mensagemPadrao, BindingResult result) throws Exception {
        Response<MensagemPadrao> response = new Response<MensagemPadrao>();
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }
        mensagemPadraoRepository.save(mensagemPadrao);
        response.setData(mensagemPadrao);
        return ResponseEntity.ok(response);
    }

    @PUT
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Atualizar uma Mensagem Padrão", notes = "Atualizar uma Mensagem Padrão", response = MensagemPadrao.class)
    @ApiResponses(value = {
            @ApiResponse(code = 203, message = "Atualização do Mensagem Padrão feita com sucesso"),
    })
    @RequestMapping(value = "/mensagempadrao/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Response<MensagemPadrao>> Put(@PathVariable(value = "id") long id, @Valid @RequestBody
            MensagemPadrao newAlerta, BindingResult result) {
        Optional<MensagemPadrao> oldAlerta = mensagemPadraoRepository.findById(id);
        Response<MensagemPadrao> response = new Response<MensagemPadrao>();
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }
        if (oldAlerta.isPresent()) {
            MensagemPadrao mensagemPadrao = oldAlerta.get();
            mensagemPadrao.setDescricao(newAlerta.getDescricao());
            response.setData(mensagemPadrao);
            mensagemPadraoRepository.save(mensagemPadrao);
            return ResponseEntity.ok(response);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DELETE
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Exclui uma Mensagem Padrao", notes = "Exclui uma Mensagem Padrão", response = MensagemPadrao.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Exclusão da Mensagem Padrão feita com sucesso"),
    })
    @RequestMapping(value = "/mensagempadrao/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> Delete(@PathVariable(value = "id") long id) {
        Optional<MensagemPadrao> mensagemPadrao = mensagemPadraoRepository.findById(id);
        if (mensagemPadrao.isPresent()) {
            mensagemPadraoRepository.delete(mensagemPadrao.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}	
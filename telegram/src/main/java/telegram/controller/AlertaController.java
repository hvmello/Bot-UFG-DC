package telegram.controller;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;

import comum.model.persistent.Alerta;
import comum.repository.AlertaRepository;
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


@RestController
@Path("/alerta")
public class AlertaController {

    @Autowired
    private AlertaRepository alertaRepository;

    TelegramBotService telegramBotService;

    @GET
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Localiza todos os alertas", notes = "Localiza todos os alertas", response = Alerta.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Alertas listados com sucesso"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")})
    @RequestMapping(value = "/alerta", method = RequestMethod.GET)
    public List<Alerta> Get() {
        return alertaRepository.findAll();
    }

    @GET
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Localiza um Alerta pelo Código", notes = "Localiza um Alerta pelo Código", response = Alerta.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Alerta localizado com sucesso")
    })
    @RequestMapping(value = "/alerta/{id}", method = RequestMethod.GET)
    public ResponseEntity<Alerta> GetById(@PathVariable(value = "id") long id) {

        Optional<Alerta> alerta = alertaRepository.findById(id);
        return alerta.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @POST
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Insere um Alerta", notes = "Insere um Alerta", response = Alerta.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Inserção do Alerta feita com sucesso")
    })
    @RequestMapping(value = "/alerta", method = RequestMethod.POST)
    public ResponseEntity<Response<Alerta>> Post(@Valid @RequestBody Alerta alerta, BindingResult result) throws Exception {
        Response<Alerta> response = new Response<>();
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }
        telegramBotService.createTextForMessageAndSendAllUsers(alerta);
        alertaRepository.save(alerta);
        response.setData(alerta);
        return ResponseEntity.ok(response);
    }

    @PUT
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Atualizar um Alerta", notes = "Atualizar um Alerta", response = Alerta.class)
    @ApiResponses(value = {
            @ApiResponse(code = 203, message = "Atualização do Alerta feita com sucesso"),
    })
    @RequestMapping(value = "/alerta/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Response<Alerta>> Put(@PathVariable(value = "id") long id, @Valid @RequestBody
            Alerta newAlerta, BindingResult result) {
        Optional<Alerta> oldAlerta = alertaRepository.findById(id);
        Response<Alerta> response = new Response<>();
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }
        if (oldAlerta.isPresent()) {
            Alerta alerta = oldAlerta.get();
            alerta.setTextoAlerta(newAlerta.getTextoAlerta());
            response.setData(alerta);
            alertaRepository.save(alerta);
            return ResponseEntity.ok(response);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DELETE
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Exclui um Alerta", notes = "Exclui um Alerta", response = Alerta.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Exclusão do Alerta feita com sucesso"),
    })
    @RequestMapping(value = "/alerta/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> Delete(@PathVariable(value = "id") long id) {
        Optional<Alerta> alerta = alertaRepository.findById(id);
        if (alerta.isPresent()) {
            alertaRepository.delete(alerta.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}	
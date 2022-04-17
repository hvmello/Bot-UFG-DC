package telegram.service.clients;

import comum.model.dto.rasa.RasaMessage;
import comum.model.dto.rasa.RasaResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "rasaService", url = "${codec.service.rasa}")
public interface RasaClient {


    @PostMapping("/webhooks/rest/webhook")
    List<RasaResponse> sendMessage(@RequestBody RasaMessage message);



}

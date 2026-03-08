package mi.banco.crm_clientes.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mi.banco.crm_clientes.config.RabbitMqConstants;
import mi.banco.crm_clientes.dto.messaging.ClienteInfoRequestMessage;
import mi.banco.crm_clientes.dto.messaging.ClienteInfoResponseMessage;
import mi.banco.crm_clientes.dto.response.ClienteResponseDTO;
import mi.banco.crm_clientes.exception.ResourceNotFoundException;
import mi.banco.crm_clientes.service.ClienteService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClienteInfoRequestListener {

    private final ClienteService clienteService;

    @RabbitListener(queues = RabbitMqConstants.CLIENTE_REQUEST_QUEUE)
    public ClienteInfoResponseMessage onClienteInfoRequest(@Payload ClienteInfoRequestMessage request) {
        Long clienteId = request.getClienteId();
        try {
            ClienteResponseDTO cliente = clienteService.getClienteById(clienteId);
            return ClienteInfoResponseMessage.builder()
                    .clienteId(cliente.getClienteId())
                    .nombre(cliente.getNombre())
                    .identificacion(cliente.getIdentificacion())
                    .build();
        } catch (ResourceNotFoundException ex) {
            log.warn("Cliente no encontrado para solicitud RabbitMQ: {}", clienteId);
            return ClienteInfoResponseMessage.builder().build();
        }
    }
}

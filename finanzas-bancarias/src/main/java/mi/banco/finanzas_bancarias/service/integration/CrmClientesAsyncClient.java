package mi.banco.finanzas_bancarias.service.integration;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.RabbitConverterFuture;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mi.banco.finanzas_bancarias.config.RabbitMqConstants;
import mi.banco.finanzas_bancarias.dto.messaging.ClienteInfoRequestMessage;
import mi.banco.finanzas_bancarias.dto.messaging.ClienteInfoResponseMessage;
import mi.banco.finanzas_bancarias.exception.ResourceNotFoundException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrmClientesAsyncClient {

    private final AsyncRabbitTemplate asyncRabbitTemplate;

    public ClienteInfoResponseMessage getClienteById(Long clienteId) {
        ClienteInfoRequestMessage request = ClienteInfoRequestMessage.builder()
                .clienteId(clienteId)
                .build();

        try {
            RabbitConverterFuture<ClienteInfoResponseMessage> future = asyncRabbitTemplate
                    .convertSendAndReceive(
                            RabbitMqConstants.CLIENTE_EXCHANGE,
                            RabbitMqConstants.CLIENTE_REQUEST_ROUTING_KEY,
                            request);

            ClienteInfoResponseMessage response = future.get(5, TimeUnit.SECONDS);
            if (response == null || response.getClienteId() == null) {
                throw new ResourceNotFoundException("Cliente no encontrado: " + clienteId);
            }

            return response;
        } catch (ResourceNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error consultando cliente {} por RabbitMQ", clienteId, ex);
            throw new IllegalStateException("No fue posible consultar el cliente en crm-clientes", ex);
        }
    }
}

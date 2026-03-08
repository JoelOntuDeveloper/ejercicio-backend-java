package mi.banco.crm_clientes.config;

import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mi.banco.crm_clientes.dto.messaging.ClienteInfoRequestMessage;
import mi.banco.crm_clientes.dto.messaging.ClienteInfoResponseMessage;

@Configuration
public class RabbitMqConfig {

    @Bean
    DirectExchange clienteExchange() {
        return new DirectExchange(RabbitMqConstants.CLIENTE_EXCHANGE);
    }

    @Bean
    Queue clienteInfoRequestQueue() {
        return new Queue(RabbitMqConstants.CLIENTE_REQUEST_QUEUE, true);
    }

    @Bean
    Binding clienteInfoBinding(Queue clienteInfoRequestQueue, DirectExchange clienteExchange) {
        return BindingBuilder.bind(clienteInfoRequestQueue)
                .to(clienteExchange)
                .with(RabbitMqConstants.CLIENTE_REQUEST_ROUTING_KEY);
    }

    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("mi.banco.crm_clientes.dto.messaging", "mi.banco.finanzas_bancarias.dto.messaging");
        classMapper.setIdClassMapping(Map.of(
                "clienteInfoRequest", ClienteInfoRequestMessage.class,
                "clienteInfoResponse", ClienteInfoResponseMessage.class,
                "mi.banco.finanzas_bancarias.dto.messaging.ClienteInfoRequestMessage", ClienteInfoRequestMessage.class,
                "mi.banco.finanzas_bancarias.dto.messaging.ClienteInfoResponseMessage", ClienteInfoResponseMessage.class));

        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(classMapper);
        return converter;
    }
}

package mi.banco.finanzas_bancarias.config;

public final class RabbitMqConstants {

    private RabbitMqConstants() {
    }

    public static final String CLIENTE_EXCHANGE = "cliente.exchange";
    public static final String CLIENTE_REQUEST_ROUTING_KEY = "cliente.info.request";
    public static final String CLIENTE_REQUEST_QUEUE = "cliente.info.request.queue";
}

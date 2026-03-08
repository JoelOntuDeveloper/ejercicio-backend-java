package mi.banco.finanzas_bancarias.dto.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteInfoResponseMessage {
    private Long clienteId;
    private String nombre;
    private String identificacion;
}

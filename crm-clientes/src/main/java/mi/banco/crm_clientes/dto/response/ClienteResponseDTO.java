package mi.banco.crm_clientes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mi.banco.crm_clientes.enums.EnumEstadosCliente;
import mi.banco.crm_clientes.enums.EnumGenero;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {

    private Long clienteId;
    private String identificacion;
    private String nombre;
    private EnumGenero genero;
    private Integer edad;
    private String direccion;
    private String telefono;
    private EnumEstadosCliente estado;
}
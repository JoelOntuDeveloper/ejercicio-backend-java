package mi.banco.crm_clientes.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteUpdateRequestDTO {
    
    @Min(value = 18, message = "El cliente debe ser mayor de edad")
    private Integer edad;
    private String direccion;
    private String telefono;

}

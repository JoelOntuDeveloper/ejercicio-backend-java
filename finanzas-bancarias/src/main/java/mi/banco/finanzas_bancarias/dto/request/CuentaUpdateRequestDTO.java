package mi.banco.finanzas_bancarias.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mi.banco.finanzas_bancarias.enums.EnumEstadoCuenta;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CuentaUpdateRequestDTO {
    
    private EnumEstadoCuenta estadoCuenta;
}

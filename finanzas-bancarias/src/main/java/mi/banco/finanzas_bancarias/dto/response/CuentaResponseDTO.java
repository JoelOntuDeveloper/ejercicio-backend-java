package mi.banco.finanzas_bancarias.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mi.banco.finanzas_bancarias.enums.EnumEstadoCuenta;
import mi.banco.finanzas_bancarias.enums.EnumTipoCuenta;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CuentaResponseDTO {

    private Long id;
    private Long clienteId;
    private String numeroCuenta;
    private EnumTipoCuenta tipoCuenta;
    private EnumEstadoCuenta estado;
    private BigDecimal saldoInicial;
}

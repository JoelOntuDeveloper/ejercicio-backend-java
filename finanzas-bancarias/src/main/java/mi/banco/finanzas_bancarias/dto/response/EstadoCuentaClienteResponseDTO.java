package mi.banco.finanzas_bancarias.dto.response;

import java.math.BigDecimal;
import java.util.List;

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
public class EstadoCuentaClienteResponseDTO {

    private Long clienteId;
    private String nombreCliente;
    private String identificacionCliente;
    private String numeroCuenta;
    private EnumTipoCuenta tipoCuenta;
    private EnumEstadoCuenta estado;
    private BigDecimal saldoInicial;
    private BigDecimal saldoActual;
    private List<MovimientoReporteResponseDTO> movimientos;
}
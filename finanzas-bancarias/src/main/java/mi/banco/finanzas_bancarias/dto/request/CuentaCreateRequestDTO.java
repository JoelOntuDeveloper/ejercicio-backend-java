package mi.banco.finanzas_bancarias.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mi.banco.finanzas_bancarias.enums.EnumTipoCuenta;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CuentaCreateRequestDTO {

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;
    @NotNull(message = "El tipo de cuenta es obligatorio")
    private EnumTipoCuenta tipoCuenta;
    @Min(value = 0, message = "El saldo inicial no puede ser negativo")
    private BigDecimal saldoInicial;
}

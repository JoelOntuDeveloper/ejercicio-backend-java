package mi.banco.finanzas_bancarias.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mi.banco.finanzas_bancarias.enums.EnumTipoMovimiento;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoReporteResponseDTO {
    
    private LocalDateTime fecha;
    private EnumTipoMovimiento tipoMovimiento;
    private BigDecimal valor;
    private BigDecimal saldo;
}

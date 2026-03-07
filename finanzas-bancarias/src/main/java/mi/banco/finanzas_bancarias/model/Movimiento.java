package mi.banco.finanzas_bancarias.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "MOVIMIENTO")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MovimientoId")
    private Long movimientoId;

    @NotNull(message = "La fecha es obligatoria")
    @Column(name = "Fecha", nullable = false)
    private LocalDateTime fecha;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Column(name = "TipoMovimiento", nullable = false, length = 30)
    private String tipoMovimiento;

    @NotNull(message = "El valor es obligatorio")
    @DecimalMin(value = "0.01", message = "El valor debe ser mayor a 0")
    @Column(name = "Valor", nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @NotNull(message = "El saldo es obligatorio")
    @Column(name = "Saldo", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CuentaId", nullable = false)
    private Cuenta cuenta;
}
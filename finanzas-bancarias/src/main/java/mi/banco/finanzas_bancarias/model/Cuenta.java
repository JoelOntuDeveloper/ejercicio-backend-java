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
import mi.banco.finanzas_bancarias.enums.EnumEstadoCuenta;
import mi.banco.finanzas_bancarias.enums.EnumTipoCuenta;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "CUENTA")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CuentaId")
    private Long cuentaId;

    @NotBlank(message = "El número de cuenta es obligatorio")
    @Column(name = "NumeroCuenta", nullable = false, unique = true, length = 30)
    private String numeroCuenta;

    @Enumerated(EnumType.STRING)
    private EnumTipoCuenta tipoCuenta;

    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.00", message = "El saldo inicial no puede ser negativo")
    @Column(name = "SaldoInicial", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldoInicial;

    @Enumerated(EnumType.STRING)
    private EnumEstadoCuenta estado;

    @Column(name = "ClienteId", nullable = false)
    private Long clienteId;
}
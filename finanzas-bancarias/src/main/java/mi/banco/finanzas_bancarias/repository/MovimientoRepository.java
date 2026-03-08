package mi.banco.finanzas_bancarias.repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;

import mi.banco.finanzas_bancarias.model.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuenta_CuentaIdOrderByFechaDesc(Long cuentaId);
    List<Movimiento> findByCuenta_CuentaIdAndFechaBetweenOrderByFechaAsc(
            Long cuentaId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin);
    Optional<Movimiento> findFirstByCuenta_CuentaIdAndFechaBeforeOrderByFechaDescMovimientoIdDesc(
            Long cuentaId,
            LocalDateTime fecha);

    Optional<Movimiento> findFirstByCuenta_CuentaIdOrderByFechaDescMovimientoIdDesc(Long cuentaId);
}

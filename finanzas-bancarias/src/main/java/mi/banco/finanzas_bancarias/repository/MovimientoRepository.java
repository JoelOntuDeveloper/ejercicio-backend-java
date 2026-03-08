package mi.banco.finanzas_bancarias.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mi.banco.finanzas_bancarias.model.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuenta_CuentaIdOrderByFechaDesc(Long cuentaId);

    Optional<Movimiento> findFirstByCuenta_CuentaIdOrderByFechaDescMovimientoIdDesc(Long cuentaId);
}

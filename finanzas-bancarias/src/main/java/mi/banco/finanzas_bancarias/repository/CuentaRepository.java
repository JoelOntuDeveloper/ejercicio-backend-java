package mi.banco.finanzas_bancarias.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mi.banco.finanzas_bancarias.model.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    boolean existsByNumeroCuenta(String numeroCuenta);
}

package mi.banco.crm_clientes.repository;

import mi.banco.crm_clientes.model.Cliente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByPersonaIdentificacion(String identificacion);

    boolean existsByPersonaIdentificacion(String identificacion);
}

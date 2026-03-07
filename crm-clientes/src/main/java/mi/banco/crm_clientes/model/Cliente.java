package mi.banco.crm_clientes.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mi.banco.crm_clientes.enums.EnumEstadosCliente;

@Getter
@Setter
@Entity
@Table(name = "CLIENTE")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clienteId;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

    @Enumerated(EnumType.STRING)
    private EnumEstadosCliente estado;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "PersonaId", referencedColumnName = "PersonaId", nullable = false)
    private Persona persona;
}

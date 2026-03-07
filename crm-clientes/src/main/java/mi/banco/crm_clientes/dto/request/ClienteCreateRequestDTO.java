package mi.banco.crm_clientes.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mi.banco.crm_clientes.enums.EnumGenero;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ClienteCreateRequestDTO {

    @NotBlank(message = "La identificación es obligatoria")
    private String identificacion;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private EnumGenero genero;
    
    @Min(value = 18, message = "El cliente debe ser mayor de edad")
    @NotNull(message = "La edad es obligatoria")
    private Integer edad;

    private String direccion;

    private String telefono;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 4, message = "La contraseña debe tener al menos 4 caracteres")
    private String contrasena;
}

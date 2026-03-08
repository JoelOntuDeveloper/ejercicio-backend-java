package mi.banco.finanzas_bancarias.service;

import java.time.LocalDateTime;
import java.util.List;

import mi.banco.finanzas_bancarias.dto.response.EstadoCuentaClienteResponseDTO;

public interface ReporteService {

    List<EstadoCuentaClienteResponseDTO> getEstadoCuentaCliente(
            Long clienteId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin);
}

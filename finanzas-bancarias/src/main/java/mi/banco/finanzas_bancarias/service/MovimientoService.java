package mi.banco.finanzas_bancarias.service;

import java.util.List;

import mi.banco.finanzas_bancarias.dto.request.MovimientoCreateRequestDTO;
import mi.banco.finanzas_bancarias.dto.response.MovimientoResponseDTO;

public interface MovimientoService {
    
    List<MovimientoResponseDTO> getMovimientosByCuentaId(Long cuentaId);
    MovimientoResponseDTO createMovimiento(MovimientoCreateRequestDTO request);
}

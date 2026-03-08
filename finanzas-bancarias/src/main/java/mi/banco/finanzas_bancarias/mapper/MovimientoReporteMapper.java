package mi.banco.finanzas_bancarias.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import mi.banco.finanzas_bancarias.dto.response.MovimientoReporteResponseDTO;
import mi.banco.finanzas_bancarias.model.Movimiento;

@Mapper(componentModel = "spring")
public interface MovimientoReporteMapper {

    MovimientoReporteResponseDTO toResponseDTO(Movimiento movimiento);

    List<MovimientoReporteResponseDTO> toResponseDTOList(List<Movimiento> movimientos);
}

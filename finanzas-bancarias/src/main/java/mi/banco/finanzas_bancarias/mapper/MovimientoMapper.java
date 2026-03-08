package mi.banco.finanzas_bancarias.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import mi.banco.finanzas_bancarias.dto.request.MovimientoCreateRequestDTO;
import mi.banco.finanzas_bancarias.dto.response.MovimientoResponseDTO;
import mi.banco.finanzas_bancarias.model.Cuenta;
import mi.banco.finanzas_bancarias.model.Movimiento;

@Mapper(componentModel = "spring")
public interface MovimientoMapper {

    @BeforeMapping
    default void ensureCuentaExists(@MappingTarget Movimiento movimiento) {
        if (movimiento.getCuenta() == null) {
            movimiento.setCuenta(new Cuenta());
        }
    }

    @Mapping(target = "movimientoId", ignore = true)
    @Mapping(target = "fecha", ignore = true)
    @Mapping(target = "tipoMovimiento", ignore = true)
    @Mapping(target = "saldo", ignore = true)
    Movimiento toEntity(MovimientoCreateRequestDTO dto);

    @Mapping(target = "id", source = "movimientoId")
    MovimientoResponseDTO toResponseDTO(Movimiento movimiento);

    List<MovimientoResponseDTO> toResponseDTOList(List<Movimiento> movimientos);

    @Named("doubleToBigDecimal")
    default BigDecimal doubleToBigDecimal(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    @Named("bigDecimalToDouble")
    default Double bigDecimalToDouble(BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }
}

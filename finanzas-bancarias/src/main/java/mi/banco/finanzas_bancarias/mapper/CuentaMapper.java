package mi.banco.finanzas_bancarias.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import mi.banco.finanzas_bancarias.dto.request.CuentaCreateRequestDTO;
import mi.banco.finanzas_bancarias.dto.request.CuentaUpdateRequestDTO;
import mi.banco.finanzas_bancarias.dto.response.CuentaResponseDTO;
import mi.banco.finanzas_bancarias.model.Cuenta;

@Mapper(componentModel = "spring")
public interface CuentaMapper {

    @Mapping(target = "cuentaId", ignore = true)
    @Mapping(target = "numeroCuenta", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "saldoInicial", source = "saldoInicial", qualifiedByName = "doubleToBigDecimal")
    Cuenta toEntity(CuentaCreateRequestDTO dto);

    @Mapping(target = "id", source = "cuentaId")
    @Mapping(target = "saldoInicial", source = "saldoInicial", qualifiedByName = "bigDecimalToDouble")
    CuentaResponseDTO toResponseDTO(Cuenta cuenta);

    List<CuentaResponseDTO> toResponseDTOList(List<Cuenta> cuentas);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            ignoreByDefault = true)
    @Mapping(target = "estado", source = "estadoCuenta")
    void updateCuentaFromDto(CuentaUpdateRequestDTO dto, @MappingTarget Cuenta cuenta);

    @Named("doubleToBigDecimal")
    default BigDecimal doubleToBigDecimal(Double value) {
        return value == null ? null : BigDecimal.valueOf(value);
    }

    @Named("bigDecimalToDouble")
    default Double bigDecimalToDouble(BigDecimal value) {
        return value == null ? null : value.doubleValue();
    }
}

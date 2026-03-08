package mi.banco.finanzas_bancarias.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import mi.banco.finanzas_bancarias.dto.messaging.ClienteInfoResponseMessage;
import mi.banco.finanzas_bancarias.dto.response.EstadoCuentaClienteResponseDTO;
import mi.banco.finanzas_bancarias.dto.response.MovimientoReporteResponseDTO;
import mi.banco.finanzas_bancarias.model.Cuenta;

@Mapper(componentModel = "spring")
public interface EstadoCuentaClienteMapper {

    @Mapping(target = "clienteId", source = "cuenta.clienteId")
    @Mapping(target = "nombreCliente", source = "cliente.nombre")
    @Mapping(target = "identificacionCliente", source = "cliente.identificacion")
    @Mapping(target = "numeroCuenta", source = "cuenta.numeroCuenta")
    @Mapping(target = "tipoCuenta", source = "cuenta.tipoCuenta")
    @Mapping(target = "estado", source = "cuenta.estado")
    @Mapping(target = "saldoInicial", source = "saldoInicialPeriodo")
    @Mapping(target = "saldoActual", source = "saldoActual")
    @Mapping(target = "movimientos", source = "movimientos")
    EstadoCuentaClienteResponseDTO toResponseDTO(
            Cuenta cuenta,
            ClienteInfoResponseMessage cliente,
            BigDecimal saldoInicialPeriodo,
            BigDecimal saldoActual,
            List<MovimientoReporteResponseDTO> movimientos);
}

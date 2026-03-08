package mi.banco.finanzas_bancarias.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mi.banco.finanzas_bancarias.dto.messaging.ClienteInfoResponseMessage;
import mi.banco.finanzas_bancarias.dto.response.EstadoCuentaClienteResponseDTO;
import mi.banco.finanzas_bancarias.dto.response.MovimientoReporteResponseDTO;
import mi.banco.finanzas_bancarias.exception.ResourceNotFoundException;
import mi.banco.finanzas_bancarias.mapper.EstadoCuentaClienteMapper;
import mi.banco.finanzas_bancarias.mapper.MovimientoReporteMapper;
import mi.banco.finanzas_bancarias.model.Cuenta;
import mi.banco.finanzas_bancarias.model.Movimiento;
import mi.banco.finanzas_bancarias.repository.CuentaRepository;
import mi.banco.finanzas_bancarias.repository.MovimientoRepository;
import mi.banco.finanzas_bancarias.service.ReporteService;
import mi.banco.finanzas_bancarias.service.integration.CrmClientesAsyncClient;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final CrmClientesAsyncClient crmClientesAsyncClient;
    private final EstadoCuentaClienteMapper estadoCuentaClienteMapper;
    private final MovimientoReporteMapper movimientoReporteMapper;

    @Override
    public List<EstadoCuentaClienteResponseDTO> getEstadoCuentaCliente(
            Long clienteId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {

        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Fecha de inicio y Fecha Fin son obligatorias");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("Fecha Inicio no puede ser mayor a Fecha Fin");
        }

        ClienteInfoResponseMessage cliente = crmClientesAsyncClient.getClienteById(clienteId);

        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);
        if (cuentas.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron cuentas para el cliente: " + clienteId);
        }

        log.info("Generando estado de cuenta para cliente {} entre {} y {}", clienteId, fechaInicio, fechaFin);

        return cuentas.stream()
                .map(cuenta -> mapEstadoCuenta(cuenta, cliente, fechaInicio, fechaFin))
                .toList();
    }

    private EstadoCuentaClienteResponseDTO mapEstadoCuenta(
            Cuenta cuenta,
            ClienteInfoResponseMessage cliente,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {

        List<Movimiento> movimientos = movimientoRepository
                .findByCuenta_CuentaIdAndFechaBetweenOrderByFechaAsc(cuenta.getCuentaId(), fechaInicio, fechaFin);

        BigDecimal saldoInicialPeriodo = movimientoRepository
                .findFirstByCuenta_CuentaIdAndFechaBeforeOrderByFechaDescMovimientoIdDesc(cuenta.getCuentaId(), fechaInicio)
                .map(Movimiento::getSaldo)
                .orElse(cuenta.getSaldoInicial());

        BigDecimal saldoActual = movimientos.isEmpty()
                ? saldoInicialPeriodo
                : movimientos.get(movimientos.size() - 1).getSaldo();

        List<MovimientoReporteResponseDTO> movimientosDto = movimientoReporteMapper.toResponseDTOList(movimientos);

        return estadoCuentaClienteMapper.toResponseDTO(
                cuenta,
                cliente,
                saldoInicialPeriodo,
                saldoActual,
                movimientosDto);
    }
}

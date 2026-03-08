package mi.banco.finanzas_bancarias.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mi.banco.finanzas_bancarias.dto.request.MovimientoCreateRequestDTO;
import mi.banco.finanzas_bancarias.dto.response.MovimientoResponseDTO;
import mi.banco.finanzas_bancarias.enums.EnumTipoMovimiento;
import mi.banco.finanzas_bancarias.exception.InsufficientFundsException;
import mi.banco.finanzas_bancarias.exception.ResourceNotFoundException;
import mi.banco.finanzas_bancarias.mapper.MovimientoMapper;
import mi.banco.finanzas_bancarias.model.Cuenta;
import mi.banco.finanzas_bancarias.model.Movimiento;
import mi.banco.finanzas_bancarias.repository.CuentaRepository;
import mi.banco.finanzas_bancarias.repository.MovimientoRepository;
import mi.banco.finanzas_bancarias.service.MovimientoService;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private static final String SALDO_NO_DISPONIBLE = "Saldo no disponible";

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoMapper movimientoMapper;

    @Override
    public List<MovimientoResponseDTO> getMovimientosByCuentaId(Long cuentaId) {
        log.info("Buscando movimientos para la cuenta {}", cuentaId);

        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        List<Movimiento> movimientos = movimientoRepository.findByCuenta_CuentaIdOrderByFechaDesc(cuenta.getCuentaId());
        return movimientoMapper.toResponseDTOList(movimientos);
    }

    @Transactional
    @Override
    public MovimientoResponseDTO createMovimiento(MovimientoCreateRequestDTO request) {
        log.info("Creando movimiento para la cuenta {}", request.getNumeroCuenta());

        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(request.getNumeroCuenta())
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        BigDecimal valorRequest = request.getValor();
        BigDecimal valorAbsoluto = valorRequest.abs();

        Movimiento ultimoMovimiento = movimientoRepository
                .findFirstByCuenta_CuentaIdOrderByFechaDescMovimientoIdDesc(cuenta.getCuentaId())
                .orElse(null);

        BigDecimal saldoAnterior = ultimoMovimiento != null
                ? ultimoMovimiento.getSaldo()
                : cuenta.getSaldoInicial();

        EnumTipoMovimiento tipoMovimiento = valorRequest.signum() >= 0
                ? EnumTipoMovimiento.DEPOSITO
                : EnumTipoMovimiento.RETIRO;

        if (tipoMovimiento == EnumTipoMovimiento.RETIRO && 
            saldoAnterior.compareTo(valorAbsoluto) < 0) {
            throw new InsufficientFundsException(SALDO_NO_DISPONIBLE);
        }

        BigDecimal saldoNuevo = tipoMovimiento == EnumTipoMovimiento.DEPOSITO
                ? saldoAnterior.add(valorAbsoluto)
                : saldoAnterior.subtract(valorAbsoluto);

        Movimiento movimiento = movimientoMapper.toEntity(request);
        movimiento.setCuenta(cuenta);
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipoMovimiento(tipoMovimiento);
        movimiento.setValor(valorAbsoluto);
        movimiento.setSaldo(saldoNuevo);

        movimiento = movimientoRepository.save(movimiento);
        return movimientoMapper.toResponseDTO(movimiento);
    }
}

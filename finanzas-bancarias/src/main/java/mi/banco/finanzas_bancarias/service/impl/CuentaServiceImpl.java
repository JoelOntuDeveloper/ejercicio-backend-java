package mi.banco.finanzas_bancarias.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mi.banco.finanzas_bancarias.dto.request.CuentaCreateRequestDTO;
import mi.banco.finanzas_bancarias.dto.request.CuentaUpdateRequestDTO;
import mi.banco.finanzas_bancarias.dto.response.CuentaResponseDTO;
import mi.banco.finanzas_bancarias.enums.EnumEstadoCuenta;
import mi.banco.finanzas_bancarias.exception.ResourceAlreadyExistsException;
import mi.banco.finanzas_bancarias.exception.ResourceNotFoundException;
import mi.banco.finanzas_bancarias.mapper.CuentaMapper;
import mi.banco.finanzas_bancarias.model.Cuenta;
import mi.banco.finanzas_bancarias.repository.CuentaRepository;
import mi.banco.finanzas_bancarias.service.CuentaService;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final CuentaMapper cuentaMapper;

    @Override
    public CuentaResponseDTO getCuentaById(Long cuentaId) {
        log.info("Buscando cuenta con id {}", cuentaId);
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
        return cuentaMapper.toResponseDTO(cuenta);
    }

    @Override
    public CuentaResponseDTO getCuentaByNumeroCuenta(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
        return cuentaMapper.toResponseDTO(cuenta);
    }

    @Override
    public List<CuentaResponseDTO> getAllCuentas() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        return cuentaMapper.toResponseDTOList(cuentas);
    }

    @Override
    public List<CuentaResponseDTO> getCuentasByClienteId(Long clienteId) {
        List<Cuenta> cuentas = cuentaRepository.findAll().stream()
                .filter(cuenta -> clienteId.equals(cuenta.getClienteId()))
                .toList();
        return cuentaMapper.toResponseDTOList(cuentas);
    }

    @Transactional
    @Override
    public CuentaResponseDTO createCuenta(CuentaCreateRequestDTO request) {
        String numeroCuenta = generarNumeroCuenta();
        if (cuentaRepository.existsByNumeroCuenta(numeroCuenta)) {
            throw new ResourceAlreadyExistsException("Ya existe una cuenta con numero: " + numeroCuenta);
        }

        Cuenta cuenta = cuentaMapper.toEntity(request);
        cuenta.setNumeroCuenta(numeroCuenta);
        cuenta.setEstado(EnumEstadoCuenta.ACTIVO);
        cuenta = cuentaRepository.save(cuenta);
        return cuentaMapper.toResponseDTO(cuenta);
    }

    @Transactional
    @Override
    public CuentaResponseDTO updateCuenta(Long cuentaId, CuentaUpdateRequestDTO request) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
        cuentaMapper.updateCuentaFromDto(request, cuenta);
        cuenta = cuentaRepository.save(cuenta);
        return cuentaMapper.toResponseDTO(cuenta);
    }

    @Transactional
    @Override
    public void deleteCuenta(Long cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
        cuenta.setEstado(EnumEstadoCuenta.INACTIVO);
        cuentaRepository.save(cuenta);
    }

    private String generarNumeroCuenta() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
}

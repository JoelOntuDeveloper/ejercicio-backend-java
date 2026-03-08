package mi.banco.finanzas_bancarias.service;

import java.util.List;

import mi.banco.finanzas_bancarias.dto.request.CuentaCreateRequestDTO;
import mi.banco.finanzas_bancarias.dto.request.CuentaUpdateRequestDTO;
import mi.banco.finanzas_bancarias.dto.response.CuentaResponseDTO;

public interface CuentaService {
    
    CuentaResponseDTO getCuentaById(Long cuentaId);
    CuentaResponseDTO getCuentaByNumeroCuenta(String numeroCuenta);
    List<CuentaResponseDTO> getAllCuentas();
    List<CuentaResponseDTO> getCuentasByClienteId(Long clienteId);
    CuentaResponseDTO createCuenta(CuentaCreateRequestDTO request);
    CuentaResponseDTO updateCuenta(Long cuentaId, CuentaUpdateRequestDTO request);
    void deleteCuenta(Long cuentaId);
}

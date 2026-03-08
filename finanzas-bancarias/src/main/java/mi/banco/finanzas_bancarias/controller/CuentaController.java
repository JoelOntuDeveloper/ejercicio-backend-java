package mi.banco.finanzas_bancarias.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mi.banco.finanzas_bancarias.dto.request.CuentaCreateRequestDTO;
import mi.banco.finanzas_bancarias.dto.request.CuentaUpdateRequestDTO;
import mi.banco.finanzas_bancarias.dto.response.CuentaResponseDTO;
import mi.banco.finanzas_bancarias.service.CuentaService;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> getCuenta(@PathVariable Long id) {

        CuentaResponseDTO cuenta = cuentaService.getCuentaById(id);
        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/numero-cuenta/{numeroCuenta}")
    public ResponseEntity<CuentaResponseDTO> getCuentaByNumeroCuenta(@PathVariable String numeroCuenta) {

        CuentaResponseDTO cuenta = cuentaService.getCuentaByNumeroCuenta(numeroCuenta);
        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CuentaResponseDTO>> getCuentasByClienteId(@PathVariable Long clienteId) {

        return ResponseEntity.ok(cuentaService.getCuentasByClienteId(clienteId));
    }

    @GetMapping
    public ResponseEntity<List<CuentaResponseDTO>> getAllCuentas() {

        return ResponseEntity.ok(cuentaService.getAllCuentas());
    }

    @PostMapping
    public ResponseEntity<CuentaResponseDTO> createCuenta(
            @Valid @RequestBody CuentaCreateRequestDTO request) {

        CuentaResponseDTO cuenta = cuentaService.createCuenta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cuenta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaResponseDTO> updateCuenta(
            @PathVariable Long id,
            @Valid @RequestBody CuentaUpdateRequestDTO request) {

        CuentaResponseDTO cuenta = cuentaService.updateCuenta(id, request);
        return ResponseEntity.ok(cuenta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable Long id) {

        cuentaService.deleteCuenta(id);
        return ResponseEntity.noContent().build();
    }
}

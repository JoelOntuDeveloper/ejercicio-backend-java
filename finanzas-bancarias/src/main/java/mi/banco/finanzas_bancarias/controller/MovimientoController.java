package mi.banco.finanzas_bancarias.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mi.banco.finanzas_bancarias.dto.request.MovimientoCreateRequestDTO;
import mi.banco.finanzas_bancarias.dto.response.MovimientoResponseDTO;
import mi.banco.finanzas_bancarias.service.MovimientoService;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<MovimientoResponseDTO>> getMovimientosByCuentaId(@PathVariable Long cuentaId) {

        return ResponseEntity.ok(movimientoService.getMovimientosByCuentaId(cuentaId));
    }

    @PostMapping
    public ResponseEntity<MovimientoResponseDTO> createMovimiento(
            @Valid @RequestBody MovimientoCreateRequestDTO request) {

        MovimientoResponseDTO movimiento = movimientoService.createMovimiento(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimiento);
    }
}

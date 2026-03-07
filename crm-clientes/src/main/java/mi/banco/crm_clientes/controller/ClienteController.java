package mi.banco.crm_clientes.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mi.banco.crm_clientes.dto.request.ClienteCreateRequestDTO;
import mi.banco.crm_clientes.dto.request.ClienteUpdateRequestDTO;
import mi.banco.crm_clientes.dto.response.ClienteResponseDTO;
import mi.banco.crm_clientes.service.ClienteService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> getCliente(@PathVariable Long id) {

        ClienteResponseDTO cliente = clienteService.getClienteById(id);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping("/identificacion/{identificacion}")
    public ResponseEntity<ClienteResponseDTO> getClienteByIdentificacion(@PathVariable String identificacion) {

        ClienteResponseDTO cliente = clienteService.getClienteByIdentificacion(identificacion);
        return ResponseEntity.ok(cliente);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> getAllClientes() {

        return ResponseEntity.ok(clienteService.getAllClientes());
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> createCliente(
            @Valid @RequestBody ClienteCreateRequestDTO request) {

        ClienteResponseDTO cliente = clienteService.createCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> updateCliente(
            @PathVariable Long id,
            @Valid @RequestBody ClienteUpdateRequestDTO request) {

        ClienteResponseDTO cliente = clienteService.updateCliente(id, request);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> logicDeleteCliente(@PathVariable Long id) {

        clienteService.logicDeleteCliente(id);
        return ResponseEntity.noContent().build();
    }
}

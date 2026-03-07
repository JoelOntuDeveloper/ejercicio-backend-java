package mi.banco.crm_clientes.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mi.banco.crm_clientes.dto.request.ClienteCreateRequestDTO;
import mi.banco.crm_clientes.dto.request.ClienteUpdateRequestDTO;
import mi.banco.crm_clientes.dto.response.ClienteResponseDTO;
import mi.banco.crm_clientes.enums.EnumEstadosCliente;
import mi.banco.crm_clientes.exception.ResourceAlreadyExistsException;
import mi.banco.crm_clientes.exception.ResourceNotFoundException;
import mi.banco.crm_clientes.mapper.ClienteMapper;
import mi.banco.crm_clientes.model.Cliente;
import mi.banco.crm_clientes.repository.ClienteRepository;
import mi.banco.crm_clientes.service.ClienteService;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    public ClienteResponseDTO getClienteById(Long id) {

        log.info("Buscando cliente con id {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        return clienteMapper.toResponseDTO(cliente);
    }

    @Override
    public ClienteResponseDTO getClienteByIdentificacion(String identificacion) {

        Cliente cliente = clienteRepository.findByPersonaIdentificacion(identificacion)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        return clienteMapper.toResponseDTO(cliente);
    }

    @Override
    public List<ClienteResponseDTO> getAllClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clienteMapper.toResponseDTOList(clientes);
    }

    @Transactional
    @Override
    public ClienteResponseDTO createCliente(ClienteCreateRequestDTO request) {

        if (clienteRepository.existsByPersonaIdentificacion(request.getIdentificacion())) {
            throw new ResourceAlreadyExistsException(
                    "Ya existe un cliente con identificación: " + request.getIdentificacion());
        }

        Cliente cliente = clienteMapper.toEntity(request);
        cliente.setEstado(EnumEstadosCliente.ACTIVO);
        cliente = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(cliente);
    }

    @Transactional
    @Override
    public ClienteResponseDTO updateCliente(Long id, ClienteUpdateRequestDTO request) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        clienteMapper.updateClienteFromDto(request, cliente);
        cliente = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(cliente);
    }

    @Transactional
    @Override
    public void logicDeleteCliente(Long id) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        cliente.setEstado(EnumEstadosCliente.INACTIVO);
        clienteRepository.save(cliente);
    }
}

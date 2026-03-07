package mi.banco.crm_clientes.service;

import java.util.List;

import mi.banco.crm_clientes.dto.request.ClienteCreateRequestDTO;
import mi.banco.crm_clientes.dto.request.ClienteUpdateRequestDTO;
import mi.banco.crm_clientes.dto.response.ClienteResponseDTO;

public interface ClienteService {

    ClienteResponseDTO getClienteById(Long clienteId);

    ClienteResponseDTO getClienteByIdentificacion(String identificacion);

    List<ClienteResponseDTO> getAllClientes();

    ClienteResponseDTO createCliente(ClienteCreateRequestDTO request);    

    ClienteResponseDTO updateCliente(Long clienteId, ClienteUpdateRequestDTO request);

    void logicDeleteCliente(Long clienteId);
}
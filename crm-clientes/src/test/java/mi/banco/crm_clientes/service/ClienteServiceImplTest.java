package mi.banco.crm_clientes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mi.banco.crm_clientes.dto.request.ClienteCreateRequestDTO;
import mi.banco.crm_clientes.dto.request.ClienteUpdateRequestDTO;
import mi.banco.crm_clientes.dto.response.ClienteResponseDTO;
import mi.banco.crm_clientes.enums.EnumEstadosCliente;
import mi.banco.crm_clientes.exception.ResourceAlreadyExistsException;
import mi.banco.crm_clientes.exception.ResourceNotFoundException;
import mi.banco.crm_clientes.mapper.ClienteMapper;
import mi.banco.crm_clientes.model.Cliente;
import mi.banco.crm_clientes.repository.ClienteRepository;
import mi.banco.crm_clientes.service.impl.ClienteServiceImpl;
import mi.banco.crm_clientes.testdata.ClienteTestData;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    void shouldSaveCliente_whenRequestIsValid() {

        // Arrange
        ClienteCreateRequestDTO request = ClienteTestData.defaultClienteRequest();
        Cliente cliente = ClienteTestData.cliente();
        ClienteResponseDTO response = ClienteTestData.clienteResponse();
        Cliente savedCliente = ClienteTestData.cliente().toBuilder().clienteId(1L).build();

        when(clienteMapper.toEntity(request)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(savedCliente);
        when(clienteMapper.toResponseDTO(savedCliente)).thenReturn(response);

        // Act
        ClienteResponseDTO result = clienteService.createCliente(request);

        // Assert
        assertNotNull(result);
        assertEquals("Jose Lema", result.getNombre());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void shouldThrowException_whenClienteAlreadyExists() {

        // Arrange
        ClienteCreateRequestDTO request = ClienteTestData.defaultClienteRequest();

        String identificacion = request.getIdentificacion();
        when(clienteRepository.existsByPersonaIdentificacion(identificacion)).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> clienteService.createCliente(request));
        verify(clienteMapper, never()).toEntity(any());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void shouldSetActivoState_whenCreateCliente() {

        // Arrange
        ClienteCreateRequestDTO request = ClienteTestData.defaultClienteRequest();
        Cliente cliente = ClienteTestData.cliente();
        Cliente savedCliente = cliente.toBuilder().clienteId(10L).build();
        ClienteResponseDTO response = ClienteResponseDTO.builder()
                .clienteId(10L)
                .identificacion("1724363538")
                .nombre("Jose Lema")
                .estado(EnumEstadosCliente.ACTIVO)
                .build();

        when(clienteRepository.existsByPersonaIdentificacion(request.getIdentificacion())).thenReturn(false);
        when(clienteMapper.toEntity(request)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(savedCliente);
        when(clienteMapper.toResponseDTO(savedCliente)).thenReturn(response);

        // Act
        ClienteResponseDTO result = clienteService.createCliente(request);

        // Assert
        assertNotNull(result);
        assertEquals(EnumEstadosCliente.ACTIVO, cliente.getEstado());
    }

    @Test
    void shouldReturnClienteById_whenExists() {

        // Arrange
        Long id = 1L;
        Cliente cliente = ClienteTestData.cliente().toBuilder().clienteId(id).build();
        ClienteResponseDTO response = ClienteTestData.clienteResponse();

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toResponseDTO(cliente)).thenReturn(response);

        // Act
        ClienteResponseDTO result = clienteService.getClienteById(id);

        // Assert
        assertSame(response, result);
        verify(clienteRepository).findById(id);
        verify(clienteMapper).toResponseDTO(cliente);
    }

    @Test
    void shouldThrowNotFound_whenGetClienteByIdAndNotExists() {

        // Arrange
        Long id = 99L;
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> clienteService.getClienteById(id));
        verify(clienteMapper, never()).toResponseDTO(any());
    }

    @Test
    void shouldReturnClienteByIdentificacion_whenExists() {

        // Arrange
        String identificacion = "1724363538";
        Cliente cliente = ClienteTestData.cliente();
        ClienteResponseDTO response = ClienteTestData.clienteResponse();

        when(clienteRepository.findByPersonaIdentificacion(identificacion)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toResponseDTO(cliente)).thenReturn(response);

        // Act
        ClienteResponseDTO result = clienteService.getClienteByIdentificacion(identificacion);

        // Assert
        assertSame(response, result);
        verify(clienteRepository).findByPersonaIdentificacion(identificacion);
        verify(clienteMapper).toResponseDTO(cliente);
    }

    @Test
    void shouldThrowNotFound_whenGetClienteByIdentificacionAndNotExists() {

        // Arrange
        String identificacion = "0000000000";
        when(clienteRepository.findByPersonaIdentificacion(identificacion)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> clienteService.getClienteByIdentificacion(identificacion));
        verify(clienteMapper, never()).toResponseDTO(any());
    }

    @Test
    void shouldReturnAllClientes() {

        // Arrange
        List<Cliente> clientes = List.of(
                ClienteTestData.cliente().toBuilder().clienteId(1L).build(),
                ClienteTestData.cliente().toBuilder().clienteId(2L).build());
        List<ClienteResponseDTO> responses = List.of(
                ClienteResponseDTO.builder().clienteId(1L).identificacion("1724363538").nombre("Jose Lema").build(),
                ClienteResponseDTO.builder().clienteId(2L).identificacion("1724363538").nombre("Jose Lema").build());

        when(clienteRepository.findAll()).thenReturn(clientes);
        when(clienteMapper.toResponseDTOList(clientes)).thenReturn(responses);

        // Act
        List<ClienteResponseDTO> result = clienteService.getAllClientes();

        // Assert
        assertEquals(2, result.size());
        assertSame(responses, result);
        verify(clienteRepository).findAll();
        verify(clienteMapper).toResponseDTOList(clientes);
    }

    @Test
    void shouldUpdateCliente_whenExists() {

        // Arrange
        Long id = 1L;
        ClienteUpdateRequestDTO request = ClienteUpdateRequestDTO.builder()
                .edad(35)
                .direccion("Nueva direccion")
                .telefono("0999999999")
                .build();
        Cliente cliente = ClienteTestData.cliente().toBuilder().clienteId(id).build();
        Cliente updatedCliente = ClienteTestData.cliente().toBuilder().clienteId(id).build();
        ClienteResponseDTO response = ClienteResponseDTO.builder()
                .clienteId(id)
                .identificacion("1724363538")
                .nombre("Jose Lema")
                .build();

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenReturn(updatedCliente);
        when(clienteMapper.toResponseDTO(updatedCliente)).thenReturn(response);

        // Act
        ClienteResponseDTO result = clienteService.updateCliente(id, request);

        // Assert
        assertSame(response, result);
        verify(clienteMapper).updateClienteFromDto(request, cliente);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void shouldThrowNotFound_whenUpdateClienteAndNotExists() {

        // Arrange
        Long id = 55L;
        ClienteUpdateRequestDTO request = ClienteUpdateRequestDTO.builder().edad(40).build();
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> clienteService.updateCliente(id, request));
        verify(clienteMapper, never()).updateClienteFromDto(any(), any());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void shouldLogicDeleteCliente_whenExists() {

        // Arrange
        Long id = 3L;
        Cliente cliente = ClienteTestData.cliente().toBuilder().clienteId(id).estado(EnumEstadosCliente.ACTIVO).build();

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        // Act
        clienteService.logicDeleteCliente(id);

        // Assert
        assertEquals(EnumEstadosCliente.INACTIVO, cliente.getEstado());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void shouldThrowNotFound_whenLogicDeleteClienteAndNotExists() {

        // Arrange
        Long id = 4L;
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> clienteService.logicDeleteCliente(id));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }
}

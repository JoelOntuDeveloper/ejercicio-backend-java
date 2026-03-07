package mi.banco.crm_clientes.mapper;

import mi.banco.crm_clientes.dto.request.ClienteCreateRequestDTO;
import mi.banco.crm_clientes.dto.request.ClienteUpdateRequestDTO;
import mi.banco.crm_clientes.dto.response.ClienteResponseDTO;
import mi.banco.crm_clientes.model.Cliente;
import mi.banco.crm_clientes.model.Persona;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    @BeforeMapping
    default void ensurePersonaExists(@MappingTarget Cliente cliente) {
        if (cliente.getPersona() == null) {
            cliente.setPersona(new Persona());
        }
    }

    @Mapping(source = "nombre", target = "persona.nombre")
    @Mapping(source = "identificacion", target = "persona.identificacion")
    @Mapping(source = "direccion", target = "persona.direccion")
    @Mapping(source = "genero", target = "persona.genero")
    @Mapping(source = "edad", target = "persona.edad")
    @Mapping(source = "telefono", target = "persona.telefono")
    Cliente toEntity(ClienteCreateRequestDTO dto);

    @Mapping(source = "clienteId", target = "clienteId")
    @Mapping(source = "persona.nombre", target = "nombre")
    @Mapping(source = "persona.identificacion", target = "identificacion")
    @Mapping(source = "persona.direccion", target = "direccion")
    @Mapping(source = "persona.genero", target = "genero")
    @Mapping(source = "persona.edad", target = "edad")
    @Mapping(source = "persona.telefono", target = "telefono")
    ClienteResponseDTO toResponseDTO(Cliente cliente);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "direccion", target = "persona.direccion")
    @Mapping(source = "edad", target = "persona.edad")
    @Mapping(source = "telefono", target = "persona.telefono")
    void updateClienteFromDto(
            ClienteUpdateRequestDTO dto,
            @MappingTarget Cliente cliente);
    
    List<ClienteResponseDTO> toResponseDTOList(List<Cliente> clientes);
}

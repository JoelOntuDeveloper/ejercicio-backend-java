package mi.banco.crm_clientes.testdata;

import mi.banco.crm_clientes.dto.request.ClienteCreateRequestDTO;
import mi.banco.crm_clientes.dto.response.ClienteResponseDTO;
import mi.banco.crm_clientes.enums.EnumEstadosCliente;
import mi.banco.crm_clientes.enums.EnumGenero;
import mi.banco.crm_clientes.model.Cliente;
import mi.banco.crm_clientes.model.Persona;

public final class ClienteTestData {

    private ClienteTestData() {
    }

    public static ClienteCreateRequestDTO defaultClienteRequest() {
        return ClienteCreateRequestDTO.builder()
                .identificacion("1724363538")
                .nombre("Jose Lema")
                .genero(EnumGenero.MASCULINO)
                .edad(30)
                .direccion("Otavalo sn y principal")
                .telefono("098254785")
                .contrasena("1234")
                .build();
    }

    public static Cliente cliente() {
        Persona persona = Persona.builder()
                .identificacion("1724363538")
                .nombre("Jose Lema")
                .edad(30)
                .direccion("Otavalo sn y principal ")
                .telefono("098254785")
                .build();

        Cliente cliente = Cliente.builder()
                .persona(persona)
                .contrasena("1234")
                .build();

        return cliente;
    }

    public static ClienteResponseDTO clienteResponse() {
        return ClienteResponseDTO.builder()
                .clienteId(1L)
                .identificacion("1724363538")
                .nombre("Jose Lema")
                .genero(EnumGenero.MASCULINO)
                .edad(30)
                .estado(EnumEstadosCliente.ACTIVO)
                .build();
    }
}

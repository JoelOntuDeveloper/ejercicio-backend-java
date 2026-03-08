# Ejercicio Backend Java

Proyecto de microservicios para gestión bancaria, compuesto por:

- `crm-clientes`: administración de clientes.
- `finanzas-bancarias`: cuentas, movimientos y reporte de estado de cuenta.

## Arquitectura

Arquitectura de microservicios, con responsabilidades separadas:

- `crm-clientes` expone CRUD de clientes y responde consultas de cliente por mensajería.
- `finanzas-bancarias` expone CRUD de cuentas, registro de movimientos y generación de reportes.
- Integración entre servicios mediante RabbitMQ (patrón request/reply).
- Persistencia en PostgreSQL, con una base por microservicio.

Flujo de integración para reporte:

1. `finanzas-bancarias` recibe `GET /api/reportes` con `clienteId` y rango de fechas.
2. Publica un mensaje en `cliente.exchange` con routing key `cliente.info.request`.
3. `crm-clientes` consume en `cliente.info.request.queue` y responde la información del cliente.
4. `finanzas-bancarias` combina datos de cliente + cuentas + movimientos y retorna el estado de cuenta consolidado.

## Tecnologías usadas

- Java 21
- Spring Boot 3.5.11
- Spring Web
- Spring Data JPA
- Spring AMQP (RabbitMQ)
- PostgreSQL
- MapStruct 1.6.3
- Lombok
- Maven Wrapper (`mvnw` / `mvnw.cmd`)

## Estructura del proyecto

```text
.
├─ crm-clientes/
├─ finanzas-bancarias/
├─ BaseDatos.sql
├─ Ejercicio-backend-java.postman_collection.json
└─ README.md
```

## Base de datos

El script [BaseDatos.sql](BaseDatos.sql) crea dos bases:

- `crm_clientes`
- `finanzas_bancarias`

Y crea estas tablas:

- En `crm_clientes`: `persona`, `cliente`
- En `finanzas_bancarias`: `cuenta`, `movimiento`

### Ejecutar script con PostgreSQL local

```bash
psql -U postgres -d postgres -f ./BaseDatos.sql
```

### Ejecutar script en un contenedor Docker

```bash
docker exec -it <nombre_contenedor_postgres> psql -U postgres -d postgres -f /tmp/BaseDatos.sql
```

Nota: en Docker, primero copia el archivo al contenedor o monta el volumen para que la ruta exista.

## Propiedades de configuración

Cada microservicio usa su propio `application.properties`.

### `crm-clientes/src/main/resources/application.properties`

- `spring.application.name=crm-clientes`
- `server.port=8081`
- `spring.datasource.url=jdbc:postgresql://localhost:5432/crm_clientes`
- `spring.datasource.username=postgres`
- `spring.datasource.password=root`
- `spring.jpa.hibernate.ddl-auto=none`
- `spring.jpa.show-sql=true`
- Propiedades RabbitMQ:
  - `spring.rabbitmq.host=localhost`
  - `spring.rabbitmq.port=5672`
  - `spring.rabbitmq.username=guest`
  - `spring.rabbitmq.password=guest`

### `finanzas-bancarias/src/main/resources/application.properties`

- `spring.application.name=finanzas-bancarias`
- `server.port=8084`
- `spring.datasource.url=jdbc:postgresql://localhost:5432/finanzas_bancarias`
- `spring.datasource.username=postgres`
- `spring.datasource.password=root`
- `spring.jpa.hibernate.ddl-auto=none`
- `spring.jpa.show-sql=true`
- Propiedades RabbitMQ:
  - `spring.rabbitmq.host=localhost`
  - `spring.rabbitmq.port=5672`
  - `spring.rabbitmq.username=guest`
  - `spring.rabbitmq.password=guest`

Importante: `ddl-auto=none` implica que el esquema debe existir antes de iniciar las aplicaciones.

## Uso de RabbitMQ

Ambos microservicios comparten constantes y topología:

- Exchange directo: `cliente.exchange`
- Routing key: `cliente.info.request`
- Queue: `cliente.info.request.queue`

Implementación:

- `finanzas-bancarias` usa `AsyncRabbitTemplate` para enviar la solicitud y esperar respuesta con timeout de 5 segundos (`CrmClientesAsyncClient`).
- `crm-clientes` usa `@RabbitListener` (`ClienteInfoRequestListener`) para consumir solicitudes y responder con `ClienteInfoResponseMessage`.
- El convertidor JSON (`Jackson2JsonMessageConverter`) mapea tipos de mensajes en ambos servicios para compatibilidad.

## Funcionamiento de los microservicios

## `crm-clientes`

Responsabilidad: gestionar clientes (datos personales y estado lógico).

Comportamiento principal:

- Crea cliente con estado inicial `ACTIVO`.
- Valida unicidad por identificación.
- Eliminación lógica: cambia estado a `INACTIVO` (no borra físicamente).
- También atiende solicitudes de información de cliente por RabbitMQ.

Enums relevantes:

- `EnumGenero`: `MASCULINO`, `FEMENINO`, `OTRO`
- `EnumEstadosCliente`: `ACTIVO`, `INACTIVO`, `BLOQUEADO`

### Endpoints `crm-clientes` (puerto 8081)

- `GET /api/clientes`
- `GET /api/clientes/{id}`
- `GET /api/clientes/identificacion/{identificacion}`
- `POST /api/clientes`
- `PUT /api/clientes/{id}`
- `DELETE /api/clientes/{id}`

Ejemplo de creación:

```json
{
  "identificacion": "1748596235",
  "nombre": "Joel Ontuña",
  "direccion": "QUITO",
  "telefono": "098874587",
  "edad": 30,
  "genero": "MASCULINO",
  "contrasena": "1235"
}
```

## `finanzas-bancarias`

Responsabilidad: gestionar cuentas, movimientos y reportes de estado de cuenta.

Comportamiento principal:

- Crea cuentas con número generado automáticamente y estado `ACTIVO`.
- Eliminación lógica de cuenta: estado `INACTIVO`.
- Registra movimientos:
  - valor positivo => `DEPOSITO`
  - valor negativo => `RETIRO`
- Rechaza retiros sin fondos (`Saldo no disponible`).
- Genera reporte por cliente y rango de fechas, consultando datos del cliente por RabbitMQ.

Enums relevantes:

- `EnumTipoCuenta`: `AHORROS`, `CORRIENTE`
- `EnumEstadoCuenta`: `ACTIVO`, `INACTIVO`
- `EnumTipoMovimiento`: `DEPOSITO`, `RETIRO`

### Endpoints `finanzas-bancarias` (puerto 8084)

Cuentas:

- `GET /api/cuentas`
- `GET /api/cuentas/{id}`
- `GET /api/cuentas/numero-cuenta/{numeroCuenta}`
- `GET /api/cuentas/cliente/{clienteId}`
- `POST /api/cuentas`
- `PUT /api/cuentas/{id}`
- `DELETE /api/cuentas/{id}`

Movimientos:

- `GET /api/movimientos/cuenta/{cuentaId}`
- `POST /api/movimientos`

Reportes:

- `GET /api/reportes?clienteId={id}&fechaInicio={yyyy-MM-ddTHH:mm:ss}&fechaFin={yyyy-MM-ddTHH:mm:ss}`

Ejemplo creación de cuenta:

```json
{
  "clienteId": 3,
  "tipoCuenta": "AHORROS",
  "saldoInicial": 1000.00
}
```

Ejemplo movimiento (retiro):

```json
{
  "numeroCuenta": "FEAC3570",
  "valor": -5000.00
}
```

## Levantar el entorno

## 1) Requisitos

- JDK 21
- PostgreSQL
- RabbitMQ

## 2) Crear bases y tablas

Ejecuta `BaseDatos.sql` antes de arrancar los servicios.

## 3) Iniciar RabbitMQ

Ejemplo con Docker:

```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

Panel web: `http://localhost:15672` (usuario/clave por defecto: `guest/guest`).

## 4) Iniciar microservicios

Desde `crm-clientes/`:

```bash
./mvnw spring-boot:run
```

Desde `finanzas-bancarias/`:

```bash
./mvnw spring-boot:run
```

En Windows PowerShell puedes usar `mvnw.cmd`.

## Colección de Postman

Archivo: [Ejercicio-backend-java.postman_collection.json](Ejercicio-backend-java.postman_collection.json)

La colección contiene carpetas para:

- `crm-clientes`
- `finanzas-bancarias` (`Cuentas`, `Movimientos`, `Estado de cuenta`)

Define variables de entorno en Postman:

- `crm-clientes = http://localhost:8081`
- `finanzas-bancarias = http://localhost:8084`

Orden sugerido de pruebas:

1. Crear cliente.
2. Crear cuenta asociada al `clienteId`.
3. Registrar movimientos.
4. Consultar estado de cuenta con rango de fechas.

## Manejo de errores

Ambos microservicios devuelven errores estructurados con:

- `timestamp`
- `status`
- `error`
- `message`
- `path`

Códigos frecuentes:

- `400`: validaciones o argumentos inválidos.
- `404`: recurso no encontrado.
- `409`: conflicto (duplicados, saldo no disponible).
- `500`: error inesperado.

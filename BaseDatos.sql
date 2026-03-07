DO $$
BEGIN
    IF NOT EXISTS (
        SELECT FROM pg_database WHERE datname = 'crm_clientes'
    ) THEN
        CREATE DATABASE crm_clientes;
    END IF;
END
$$;

\c crm_clientes;

CREATE TABLE IF NOT EXISTS persona (
    persona_id BIGSERIAL PRIMARY KEY,
    identificacion VARCHAR(13) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(20) NOT NULL,
    edad INT NOT NULL,
    direccion VARCHAR(200),
    telefono VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS cliente (
    cliente_id BIGSERIAL PRIMARY KEY,
    contrasena VARCHAR(100) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    persona_id BIGINT NOT NULL,
    CONSTRAINT fk_cliente_persona
        FOREIGN KEY (persona_id)
        REFERENCES persona(persona_id)
);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT FROM pg_database WHERE datname = 'finanzas_bancarias'
    ) THEN
        CREATE DATABASE finanzas_bancarias;
    END IF;
END
$$;

\c finanzas_bancarias;

CREATE TABLE IF NOT EXISTS cuenta (
    cuenta_id BIGSERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(30) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(30) NOT NULL,
    saldo_inicial NUMERIC(15,2) NOT NULL DEFAULT 0.00,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    cliente_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS movimiento (
    movimiento_id BIGSERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL,
    tipo_movimiento VARCHAR(30) NOT NULL,
    valor NUMERIC(15,2) NOT NULL,
    saldo NUMERIC(15,2) NOT NULL,
    cuenta_id BIGINT NOT NULL,
    CONSTRAINT fk_movimiento_cuenta
        FOREIGN KEY (cuenta_id)
        REFERENCES cuenta(cuenta_id)
);
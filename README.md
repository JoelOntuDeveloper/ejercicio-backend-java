### Base de Datos

[Script de base de datos](BaseDatos.sql)

Crear base de datos con PostgresSql:

```bash
psql -U postgres -d postgres -f .\BaseDatos.sql
```

Crear la base con Docker:

```bash
docker exec -it nombre_contenedor psql -U postgres -d postgres -f .\BaseDatos.sql

```
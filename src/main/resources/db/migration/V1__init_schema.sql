CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    ciudad_base VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE rutas_favoritas (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT REFERENCES usuarios(id),
    nombre VARCHAR(100),
    origen VARCHAR(100),
    destino VARCHAR(100),
    tipo_transporte VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE historial_busquedas (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT REFERENCES usuarios(id),
    origen VARCHAR(100),
    destino VARCHAR(100),
    fecha_consulta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    num_resultados INTEGER
);

CREATE TABLE estaciones_favoritas (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT REFERENCES usuarios(id),
    estacion_id_externo VARCHAR(100),
    nombre_estacion VARCHAR(150),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
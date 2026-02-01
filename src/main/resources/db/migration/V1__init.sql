CREATE TABLE usuarios (
    id_usuario BIGSERIAL PRIMARY KEY,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasenia VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL CHECK (rol IN ('CLIENTE', 'EMPRESA', 'VETERINARIO')),
    estado BOOLEAN DEFAULT TRUE,
    email_verificado BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE perfiles_clientes (
    id_perfil BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apepa VARCHAR(100) NOT NULL,
    apema VARCHAR(100),
    telefono VARCHAR(20),
    direccion VARCHAR(200),
    ciudad VARCHAR(100),
    pais VARCHAR(100),
    foto_perfil VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

CREATE TABLE tipo_empresa (
    id_tipo_empresa BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE empresas (
    id_empresa BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    tipo_empresa_id BIGINT NOT NULL,
    nombre_comercial VARCHAR(150) NOT NULL,
    descripcion TEXT,
    horario_atencion VARCHAR(100),
    telefono VARCHAR(20),
    ciudad VARCHAR(100),
    pais VARCHAR(100),
    direccion VARCHAR(200),
    banner_url VARCHAR(255),
    logo_url VARCHAR(255),
    latitud DECIMAL(10,8),
    longitud DECIMAL(11,8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (tipo_empresa_id) REFERENCES tipo_empresa(id_tipo_empresa)
);

CREATE TABLE veterinarios (
    id_veterinario BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apepa VARCHAR(100) NOT NULL,
    apema VARCHAR(100),
    telefono VARCHAR(20),
    especialidad VARCHAR(100),
    anios_experiencia INT CHECK (anios_experiencia >= 0),
    foto_perfil VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario)
);

CREATE TABLE mascotas (
    id_mascota BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    especie VARCHAR(50) NOT NULL,
    raza VARCHAR(50),
    sexo VARCHAR(10) CHECK (sexo IN ('MACHO','HEMBRA')),
    fecha_nacimiento DATE,
    peso DECIMAL(5,2),
    color VARCHAR(30),
    foto_url VARCHAR(255),
    observaciones TEXT,
    estado BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario) ON DELETE CASCADE
);

CREATE TABLE categorias (
    id_categoria BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE subcategorias (
    id_subcategoria BIGSERIAL PRIMARY KEY,
    categoria_id BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id_categoria)
);

CREATE TABLE productos (
    id_producto BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    subcategoria_id BIGINT NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
    stock INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    imagen_url VARCHAR(255),
    estado VARCHAR(20) DEFAULT 'ACTIVO'
        CHECK (estado IN ('ACTIVO','INACTIVO')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (empresa_id) REFERENCES empresas(id_empresa),
    FOREIGN KEY (subcategoria_id) REFERENCES subcategorias(id_subcategoria)
);

CREATE TABLE servicios_empresa (
    id_servicio BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    nombre_servicio VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (empresa_id) REFERENCES empresas(id_empresa)
);

CREATE TABLE servicios_veterinario (
    id_servicio_vet BIGSERIAL PRIMARY KEY,
    veterinario_id BIGINT NOT NULL,
    nombre_servicio VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
    modalidad VARCHAR(30)
        CHECK (modalidad IN ('PRESENCIAL','VIRTUAL','DOMICILIO')),
    duracion_minutos INT DEFAULT 30 CHECK (duracion_minutos > 0),
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (veterinario_id) REFERENCES veterinarios(id_veterinario)
);

CREATE TABLE ordenes (
    id_orden BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    empresa_id BIGINT,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL CHECK (total >= 0),
    estado_orden VARCHAR(20)
        CHECK (estado_orden IN ('PENDIENTE','PAGADO','CANCELADO','FINALIZADO')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario)
);

CREATE TABLE detalle_orden (
    id_detalle BIGSERIAL PRIMARY KEY,
    orden_id BIGINT NOT NULL,
    tipo_item VARCHAR(20) NOT NULL
        CHECK (tipo_item IN ('PRODUCTO','SERV_EMPRESA','SERV_VET')),
    item_id BIGINT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1 CHECK (cantidad > 0),
    precio_unitario DECIMAL(10,2) NOT NULL CHECK (precio_unitario >= 0),
    FOREIGN KEY (orden_id) REFERENCES ordenes(id_orden) ON DELETE CASCADE
);

CREATE TABLE reservas_servicios (
    id_reserva BIGSERIAL PRIMARY KEY,
    detalle_orden_id BIGINT NOT NULL,
    fecha_reserva DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    estado VARCHAR(20)
        CHECK (estado IN ('CONFIRMADA','CANCELADA','COMPLETADA')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (detalle_orden_id) REFERENCES detalle_orden(id_detalle)
);

CREATE TABLE publicaciones_adopcion (
    id_adopcion BIGSERIAL PRIMARY KEY,
    mascota_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    descripcion_historia TEXT,
    requisitos_adopcion TEXT,
    estado VARCHAR(20)
        CHECK (estado IN ('DISPONIBLE','CERRADA')),
    fecha_publicacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (mascota_id) REFERENCES mascotas(id_mascota),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario)
);

CREATE TABLE solicitudes_adopcion (
    id_solicitud BIGSERIAL PRIMARY KEY,
    publicacion_id BIGINT NOT NULL,
    interesado_usuario_id BIGINT NOT NULL,
    mensaje TEXT,
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20)
        CHECK (estado IN ('PENDIENTE','APROBADA','RECHAZADA')),
    FOREIGN KEY (publicacion_id) REFERENCES publicaciones_adopcion(id_adopcion),
    FOREIGN KEY (interesado_usuario_id) REFERENCES usuarios(id_usuario)
);

ALTER TABLE usuarios DROP CONSTRAINT usuarios_rol_check;
ALTER TABLE usuarios ADD CONSTRAINT usuarios_rol_check
    CHECK (rol IN ('CLIENTE', 'EMPRESA', 'VETERINARIO', 'ADMIN'));

ALTER TABLE veterinarios
    ADD COLUMN numero_colegiatura VARCHAR(50) UNIQUE,
    ADD COLUMN documento_identidad_url VARCHAR(255),
    ADD COLUMN estado_registro VARCHAR(20) DEFAULT 'PENDIENTE'
        CHECK (estado_registro IN ('PENDIENTE', 'VERIFICADO', 'RECHAZADO')),
    ADD COLUMN verificado_at TIMESTAMP,
    ADD COLUMN verificado_por BIGINT;


ALTER TABLE veterinarios
    ADD CONSTRAINT fk_veterinario_verificador
    FOREIGN KEY (verificado_por) REFERENCES usuarios(id_usuario);

ALTER TABLE empresas
    ADD COLUMN verificado_por BIGINT;

ALTER TABLE empresas
    ADD CONSTRAINT fk_empresa_verificador
    FOREIGN KEY (verificado_por) REFERENCES usuarios(id_usuario);
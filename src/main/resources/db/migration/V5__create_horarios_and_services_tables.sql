CREATE TABLE horarios_empresa (
    id_horario BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    dia_semana INT NOT NULL CHECK (dia_semana BETWEEN 1 AND 7),
    hora_apertura TIME,
    hora_cierre TIME,
    esta_cerrado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (empresa_id) REFERENCES empresas(id_empresa) ON DELETE CASCADE
);

CREATE INDEX idx_servicios_empresa_id ON servicios_empresa(empresa_id);
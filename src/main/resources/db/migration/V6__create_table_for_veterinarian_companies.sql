CREATE TABLE empresa_veterinarios (
    id_empresa_vet BIGSERIAL PRIMARY KEY,
    empresa_id BIGINT NOT NULL,
    veterinario_id BIGINT NOT NULL,
    cargo VARCHAR(100),
    estado_vinculo VARCHAR(20) DEFAULT 'ACTIVO'
        CHECK (estado_vinculo IN ('ACTIVO', 'INACTIVO')),
    fecha_vinculo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_empresa_vinculo
        FOREIGN KEY (empresa_id) REFERENCES empresas(id_empresa) ON DELETE CASCADE,

    CONSTRAINT fk_veterinario_vinculo
        FOREIGN KEY (veterinario_id) REFERENCES veterinarios(id_veterinario) ON DELETE CASCADE,

    CONSTRAINT unique_empresa_veterinario UNIQUE(empresa_id, veterinario_id)
);

CREATE INDEX idx_empresa_vet_empresa ON empresa_veterinarios(empresa_id);
CREATE INDEX idx_empresa_vet_vet ON empresa_veterinarios(veterinario_id);
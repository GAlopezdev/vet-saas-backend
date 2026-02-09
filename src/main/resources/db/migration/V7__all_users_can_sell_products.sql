ALTER TABLE productos
DROP CONSTRAINT productos_empresa_id_fkey;

ALTER TABLE productos
DROP COLUMN empresa_id;

ALTER TABLE productos
ADD COLUMN usuario_id BIGINT NOT NULL;

ALTER TABLE productos
ADD CONSTRAINT fk_productos_usuario
FOREIGN KEY (usuario_id) REFERENCES usuarios(id_usuario) ON DELETE CASCADE;

CREATE INDEX idx_productos_usuario ON productos(usuario_id);
CREATE INDEX idx_productos_estado ON productos(estado);
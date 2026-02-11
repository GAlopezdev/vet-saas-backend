-- ============================================
-- FLYWAY MIGRATION V10
-- Fecha: 2026-02-11
-- Descripción: Mejoras para integración de Mercado Pago
--              - Tabla webhook_logs para idempotencia
--              - Campo fecha_pago en ordenes
--              - Nuevos estados en ordenes
--              - Índices de optimización
-- ============================================

-- 1. Agregar campo fecha_pago a ordenes
ALTER TABLE ordenes
ADD COLUMN fecha_pago TIMESTAMP;

-- Comentario para documentación
COMMENT ON COLUMN ordenes.fecha_pago IS 'Fecha en que se confirmó el pago (cuando estado pasa a PAGADO)';

-- Crear índice para búsquedas por fecha de pago
CREATE INDEX idx_ordenes_fecha_pago ON ordenes(fecha_pago);

-- 2. Actualizar constraint de estado_orden para incluir nuevos estados
ALTER TABLE ordenes DROP CONSTRAINT IF EXISTS ordenes_estado_orden_check;

ALTER TABLE ordenes ADD CONSTRAINT ordenes_estado_orden_check
    CHECK (estado_orden IN (
        'PENDIENTE',
        'PAGADO',
        'CANCELADO',
        'FINALIZADO',
        'RECHAZADO',
        'REEMBOLSADO',
        'FRAUDE',
        'ERROR_PROCESAMIENTO'
    ));

-- 3. Crear tabla webhook_logs para idempotencia y auditoría
CREATE TABLE webhook_logs (
    payment_id VARCHAR(100) PRIMARY KEY,
    procesado_en TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(50) NOT NULL,
    payment_status VARCHAR(50),
    orden_id BIGINT,
    request_id VARCHAR(200),
    intentos INTEGER NOT NULL DEFAULT 1,
    mensaje_error TEXT,

    CONSTRAINT fk_webhook_orden
        FOREIGN KEY (orden_id)
        REFERENCES ordenes(id_orden)
        ON DELETE SET NULL
);

-- Comentarios para documentación
COMMENT ON TABLE webhook_logs IS 'Registro de webhooks de Mercado Pago para idempotencia y auditoría';
COMMENT ON COLUMN webhook_logs.payment_id IS 'ID del pago en Mercado Pago (clave primaria para idempotencia)';
COMMENT ON COLUMN webhook_logs.estado IS 'Estado del procesamiento: PROCESADO, ERROR, DUPLICADO, PENDIENTE, NO_MANEJADO, RECHAZADO, FRAUDE';
COMMENT ON COLUMN webhook_logs.intentos IS 'Número de veces que MP intentó enviar este webhook';

-- Índices para webhook_logs
CREATE INDEX idx_webhook_logs_orden ON webhook_logs(orden_id);
CREATE INDEX idx_webhook_logs_estado ON webhook_logs(estado);
CREATE INDEX idx_webhook_logs_request ON webhook_logs(request_id);
CREATE INDEX idx_webhook_logs_fecha ON webhook_logs(procesado_en);

-- 4. Índices adicionales en ordenes para optimizar búsquedas
CREATE INDEX IF NOT EXISTS idx_ordenes_usuario ON ordenes(usuario_id);
CREATE INDEX IF NOT EXISTS idx_ordenes_estado ON ordenes(estado_orden);
CREATE INDEX IF NOT EXISTS idx_ordenes_payment_mp ON ordenes(payment_id_mp);
CREATE INDEX IF NOT EXISTS idx_ordenes_created_at ON ordenes(created_at);

-- 5. Trigger para actualizar fecha_pago automáticamente
CREATE OR REPLACE FUNCTION update_orden_fecha_pago()
RETURNS TRIGGER AS $$
BEGIN
    -- Cuando una orden pasa a estado PAGADO, registrar la fecha automáticamente
    IF NEW.estado_orden = 'PAGADO' AND (OLD.estado_orden IS NULL OR OLD.estado_orden != 'PAGADO') THEN
        NEW.fecha_pago = CURRENT_TIMESTAMP;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_orden_fecha_pago
    BEFORE UPDATE ON ordenes
    FOR EACH ROW
    EXECUTE FUNCTION update_orden_fecha_pago();

-- 6. Índice para empresa_id en ordenes (si se usa para búsquedas)
CREATE INDEX IF NOT EXISTS idx_ordenes_empresa ON ordenes(empresa_id);
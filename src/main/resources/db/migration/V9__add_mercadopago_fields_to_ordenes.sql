ALTER TABLE ordenes
ADD COLUMN preference_id VARCHAR(255),
ADD COLUMN payment_id_mp VARCHAR(255),
ADD COLUMN payment_status VARCHAR(50),
ADD COLUMN merchant_order_id VARCHAR(100);

CREATE INDEX idx_ordenes_preference_id ON ordenes(preference_id);
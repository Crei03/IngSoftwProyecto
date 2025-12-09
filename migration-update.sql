-- Script de actualización de tablas para SeguraTuAuto
-- Agrega columnas faltantes necesarias para la aplicación

USE segura_tu_auto;

-- Actualizar tabla clientes (agregar columnas una por una, ignorando errores si ya existen)
ALTER TABLE clientes ADD COLUMN password VARCHAR(255) NOT NULL DEFAULT '$2a$10$dummyHashedPasswordDefault';
ALTER TABLE clientes ADD COLUMN verificado BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE clientes ADD COLUMN token_verificacion VARCHAR(64) UNIQUE;
ALTER TABLE clientes ADD COLUMN token_expira DATETIME;
ALTER TABLE clientes ADD COLUMN fecha_verificacion DATETIME;
ALTER TABLE clientes ADD COLUMN reset_password_token VARCHAR(64) UNIQUE;
ALTER TABLE clientes ADD COLUMN reset_token_expira DATETIME;

-- Actualizar tabla agentes
ALTER TABLE agentes ADD COLUMN password VARCHAR(255) NOT NULL DEFAULT '$2a$10$dummyHashedPasswordDefault';

-- Actualizar tabla polizas (agregar columnas vehiculares)
ALTER TABLE polizas ADD COLUMN marca VARCHAR(100);
ALTER TABLE polizas ADD COLUMN modelo VARCHAR(100);
ALTER TABLE polizas ADD COLUMN anio_vehiculo DATE;

-- Actualizar tabla reclamaciones
ALTER TABLE reclamaciones MODIFY COLUMN estado ENUM('REGISTRADA', 'EN_EVALUACION', 'APROBADA', 'RECHAZADA', 'PAGADA') NOT NULL DEFAULT 'REGISTRADA';

SELECT 'Tablas actualizadas correctamente' AS mensaje;

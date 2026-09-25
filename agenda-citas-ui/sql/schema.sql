-- ============================================
-- schema.sql — Agenda de citas (Variante C)
-- Base de datos: MariaDB
-- ============================================

CREATE DATABASE IF NOT EXISTS agenda_citas_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE agenda_citas_db;

-- Decisiones de diseño:
-- - id: PK autoincremental, asignada por el sistema (no la ingresa el usuario).
-- - fecha_hora: una sola columna DATETIME (no fecha y hora separadas),
--   porque siempre se usan juntas para ordenar y comparar contra "ahora".
-- - estado: VARCHAR(20) en vez de ENUM de MySQL. La restricción a solo
--   {pendiente, confirmada, cancelada} se aplica en Java con un enum
--   (EstadoCita), no como ENUM nativo de SQL, para mantener la validación
--   centralizada en la capa de aplicación y no atada a un motor específico.
-- - cliente y servicio: NOT NULL, no pueden quedar vacíos (regla de negocio,
--   reforzada también en Java antes del INSERT).
-- - duracion_minutos: NOT NULL; la regla "> 0" se valida en Java antes de
--   tocar la base de datos.

CREATE TABLE IF NOT EXISTS citas (
    id                INT AUTO_INCREMENT PRIMARY KEY,
    cliente           VARCHAR(100)  NOT NULL,
    fecha_hora        DATETIME      NOT NULL,
    servicio          VARCHAR(150)  NOT NULL,
    duracion_minutos  INT           NOT NULL,
    estado            VARCHAR(20)   NOT NULL DEFAULT 'pendiente'
    requiere_confirmacion_llamada  BOOLEAN NOT NULL DEFAULT FALSE,
    es_primera_visita              BOOLEAN NOT NULL DEFAULT FALSE
);

-- Datos de ejemplo (opcional, para probar el listado)
INSERT INTO citas (cliente, fecha_hora, servicio, duracion_minutos, estado) VALUES
    ('Maria Fernanda Lopez', '2026-09-20 14:30:00', 'Corte de cabello', 45, 'pendiente'),
    ('Jose Ramirez',         '2026-09-21 09:00:00', 'Cambio de aceite', 60, 'confirmada'),
    ('Ana Castillo',         '2026-09-18 16:00:00', 'Consulta general', 30, 'cancelada');
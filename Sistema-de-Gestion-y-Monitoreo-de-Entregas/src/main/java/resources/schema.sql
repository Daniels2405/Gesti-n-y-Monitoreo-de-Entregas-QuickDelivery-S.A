-- Sistema QuickDelivery S.A.
-- Base de Datos - SC-303 - Grupo 2
-- Autores: Alberto Chavarría Gaspar / Daniel Barrientos Salas

DROP DATABASE IF EXISTS quickdelivery;
CREATE DATABASE quickdelivery CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE quickdelivery;

-- =====================================================================
--  TABLAS
-- =====================================================================

-- -------------------TABLA 1: ROL----------------------
CREATE TABLE rol (
    id_rol  INT          AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(50)  NOT NULL UNIQUE
);

-- ------------------TABLA 2: USUARIO-------------------
CREATE TABLE usuario (
    id_usuario  INT          AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    username    VARCHAR(50)  NOT NULL UNIQUE,
    pass_hash   VARCHAR(255) NOT NULL,
    estado      ENUM('activo', 'inactivo', 'suspendido') DEFAULT 'activo',
    id_rol      INT          NOT NULL,
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);

-- ---------------TABLA 3: SESION_LOGIN-----------------
CREATE TABLE sesion_login (
    id_sesion   INT       AUTO_INCREMENT PRIMARY KEY,
    id_usuario  INT       NOT NULL,
    inicio      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fin         TIMESTAMP NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE CASCADE
);

-- ---------------TABLA 4: AUDITORIA_LOG----------------
CREATE TABLE auditoria_log (
    id_log      INT          AUTO_INCREMENT PRIMARY KEY,
    id_usuario  INT,
    accion      VARCHAR(255) NOT NULL,
    fecha       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario) ON DELETE SET NULL
);

-- ---------------TABLA 5: TIPO_VEHICULO----------------
CREATE TABLE tipo_vehiculo (
    id_tipo INT         AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(50) NOT NULL UNIQUE
);

-- -------------TABLA 6: ESTADO_VEHICULO----------------
CREATE TABLE estado_vehiculo (
    id_estado INT         AUTO_INCREMENT PRIMARY KEY,
    nombre    VARCHAR(50) NOT NULL UNIQUE
);

-- -----------------TABLA 7: VEHICULO-------------------
CREATE TABLE vehiculo (
    id_vehiculo  INT           AUTO_INCREMENT PRIMARY KEY,
    placa        VARCHAR(20)   NOT NULL UNIQUE,
    capacidad    DECIMAL(10,2) NOT NULL,   -- capacidad máxima en kg
    id_tipo      INT           NOT NULL,
    id_estado    INT           NOT NULL,
    id_conductor INT,
    FOREIGN KEY (id_tipo)      REFERENCES tipo_vehiculo(id_tipo),
    FOREIGN KEY (id_estado)    REFERENCES estado_vehiculo(id_estado),
    FOREIGN KEY (id_conductor) REFERENCES usuario(id_usuario) ON DELETE SET NULL
);

-- ------------TABLA 8: UBICACION_VEHICULO--------------
CREATE TABLE ubicacion_vehiculo (
    id_ubic     INT            AUTO_INCREMENT PRIMARY KEY,
    id_vehiculo INT            NOT NULL,
    fecha_hora  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    latitud     DECIMAL(10,8),
    longitud    DECIMAL(11,8),
    FOREIGN KEY (id_vehiculo) REFERENCES vehiculo(id_vehiculo) ON DELETE CASCADE
);

-- ------------------TABLA 9: CLIENTE-------------------
CREATE TABLE cliente (
    id_cliente INT          AUTO_INCREMENT PRIMARY KEY,
    nombre     VARCHAR(100) NOT NULL,
    telefono   VARCHAR(20),
    email      VARCHAR(100)
);

-- -----------------TABLA 10: DIRECCION-----------------
CREATE TABLE direccion (
    id_dir     INT         AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT         NOT NULL,
    detalle    TEXT        NOT NULL,
    provincia  VARCHAR(50),
    canton     VARCHAR(50),
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente) ON DELETE CASCADE
);

-- -----------------TABLA 11: PRIORIDAD-----------------
CREATE TABLE prioridad (
    id_prio INT         AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(50) NOT NULL UNIQUE
);

-- --------------TABLA 12: ESTADO_PAQUETE---------------
CREATE TABLE estado_paquete (
    id_estado INT         AUTO_INCREMENT PRIMARY KEY,
    nombre    VARCHAR(50) NOT NULL UNIQUE
);

-- -----------------TABLA 13: PAQUETE-------------------
CREATE TABLE paquete (
    id_paquete    INT            AUTO_INCREMENT PRIMARY KEY,
    descripcion   TEXT           NOT NULL,
    peso          DECIMAL(10,2)  NOT NULL,   -- peso en kg; requerido para asignacion (HU-05)
    fecha_reg     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    id_cliente    INT            NOT NULL,
    id_dir        INT            NOT NULL,
    id_prio       INT            NOT NULL,
    id_estado     INT            NOT NULL,
    id_despachador INT,
    FOREIGN KEY (id_cliente)     REFERENCES cliente(id_cliente),
    FOREIGN KEY (id_dir)         REFERENCES direccion(id_dir),
    FOREIGN KEY (id_prio)        REFERENCES prioridad(id_prio),
    FOREIGN KEY (id_estado)      REFERENCES estado_paquete(id_estado),
    FOREIGN KEY (id_despachador) REFERENCES usuario(id_usuario) ON DELETE SET NULL
);

-- -----------------TABLA 14: ASIGNACION----------------
CREATE TABLE asignacion (
    id_asig     INT       AUTO_INCREMENT PRIMARY KEY,
    id_paquete  INT       NOT NULL,
    id_vehiculo INT       NOT NULL,
    fecha_asig  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado      ENUM('pendiente', 'en_ruta', 'completada', 'cancelada') DEFAULT 'pendiente',
    FOREIGN KEY (id_paquete)  REFERENCES paquete(id_paquete)  ON DELETE CASCADE,
    FOREIGN KEY (id_vehiculo) REFERENCES vehiculo(id_vehiculo) ON DELETE CASCADE
);

-- ----------------TABLA 15: INCIDENCIA-----------------
CREATE TABLE incidencia (
    id_incid     INT       AUTO_INCREMENT PRIMARY KEY,
    id_paquete   INT       NOT NULL,
    id_conductor INT       NOT NULL,
    descripcion  TEXT      NOT NULL,
    fecha        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_paquete)   REFERENCES paquete(id_paquete)  ON DELETE CASCADE,
    FOREIGN KEY (id_conductor) REFERENCES usuario(id_usuario)
);

-- =====================================================================
--  DATOS SEMILLA — catálogos y usuario administrador inicial
-- =====================================================================

-- Roles del sistema
INSERT INTO rol (nombre) VALUES
('administrador'),
('despachador'),
('conductor');

-- Tipos de vehículo  (alineado con jerarquía Java: Moto.java, Camion.java, Furgon.java)
INSERT INTO tipo_vehiculo (nombre) VALUES
('Moto'),
('Camión'),
('Furgón');

-- Estados de vehículo
INSERT INTO estado_vehiculo (nombre) VALUES
('disponible'),
('en_ruta'),
('mantenimiento'),
('fuera_servicio');

-- Prioridades de paquete
INSERT INTO prioridad (nombre) VALUES
('Express'),
('Normal'),
('Económica');

-- Estados de paquete
INSERT INTO estado_paquete (nombre) VALUES
('registrado'),
('en_bodega'),
('asignado'),
('en_transito'),
('entregado'),
('incidencia'),
('devuelto');

-- Administrador inicial  (contraseña: Admin2026* — SHA-256; cambiar en primer inicio)
INSERT INTO usuario (nombre, username, pass_hash, estado, id_rol) VALUES
('Administrador', 'admin',
 '9b8769a4a742959a2d0298c36fb70623f2a2d34a21699a3e8c9b1b4c67e0b7f2',
 'activo', 1);

-- =====================================================================
--  VISTAS
-- =====================================================================

-- Vista completa de paquetes
CREATE VIEW vista_paquetes AS
SELECT
    p.id_paquete,
    p.descripcion,
    p.peso,
    c.nombre                              AS cliente,
    c.telefono                            AS telefono_cliente,
    CONCAT(d.provincia, ', ', d.canton)   AS destino,
    pr.nombre                             AS prioridad,
    ep.nombre                             AS estado,
    p.fecha_reg,
    u.nombre                              AS despachador
FROM paquete p
JOIN cliente        c  ON p.id_cliente     = c.id_cliente
JOIN direccion      d  ON p.id_dir         = d.id_dir
JOIN prioridad      pr ON p.id_prio        = pr.id_prio
JOIN estado_paquete ep ON p.id_estado      = ep.id_estado
LEFT JOIN usuario   u  ON p.id_despachador = u.id_usuario;

-- Vista completa de vehículos
CREATE VIEW vista_vehiculos AS
SELECT
    v.id_vehiculo,
    v.placa,
    tv.nombre  AS tipo,
    ev.nombre  AS estado,
    u.nombre   AS conductor,
    v.capacidad
FROM vehiculo        v
JOIN tipo_vehiculo   tv ON v.id_tipo     = tv.id_tipo
JOIN estado_vehiculo ev ON v.id_estado   = ev.id_estado
LEFT JOIN usuario    u  ON v.id_conductor = u.id_usuario;

-- Vista de asignaciones activas (pendiente o en ruta)
CREATE VIEW vista_asignaciones_activas AS
SELECT
    a.id_asig,
    p.id_paquete,
    p.descripcion                         AS paquete,
    p.peso,
    v.placa,
    v.capacidad,
    u.nombre                              AS conductor,
    a.estado,
    a.fecha_asig,
    c.nombre                              AS cliente,
    CONCAT(d.provincia, ', ', d.canton)   AS destino
FROM asignacion a
JOIN paquete        p  ON a.id_paquete    = p.id_paquete
JOIN vehiculo       v  ON a.id_vehiculo   = v.id_vehiculo
LEFT JOIN usuario   u  ON v.id_conductor  = u.id_usuario
JOIN cliente        c  ON p.id_cliente    = c.id_cliente
JOIN direccion      d  ON p.id_dir        = d.id_dir
WHERE a.estado IN ('pendiente', 'en_ruta');

-- =====================================================================
--  USUARIO DE APLICACIÓN (mínimo privilegio)
-- =====================================================================

CREATE USER IF NOT EXISTS 'quickdelivery_srv'@'localhost' IDENTIFIED BY 'd3l1veryServ30*';
GRANT SELECT, INSERT, UPDATE, DELETE ON quickdelivery.* TO 'quickdelivery_srv'@'localhost';
FLUSH PRIVILEGES;

-- =====================================================================
--  VERIFICACIÓN FINAL
-- =====================================================================

SELECT 'BASE DE DATOS CREADA EXITOSAMENTE' AS resultado;
SELECT TABLE_NAME AS tabla, TABLE_ROWS AS filas
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'quickdelivery' AND TABLE_TYPE = 'BASE TABLE'
ORDER BY TABLE_NAME;

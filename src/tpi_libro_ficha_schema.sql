-- Conexión al servidor se hace con:
-- mysql -u tu_usuario -p < tpi_libro_ficha_schema.sql

-- Borrar la base si ya existe
DROP DATABASE IF EXISTS tpi_libro_ficha;

-- Crear base de datos
CREATE DATABASE tpi_libro_ficha CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Seleccionar base de datos
USE tpi_libro_ficha;

-- Tabla Libro (A)
CREATE TABLE libro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN DEFAULT FALSE,
    titulo VARCHAR(150) NOT NULL,
    autor VARCHAR(120) NOT NULL,
    editorial VARCHAR(100),
    anioEdicion INT
);

-- Tabla FichaBibliografica (B)
CREATE TABLE ficha_bibliografica (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    eliminado BOOLEAN DEFAULT FALSE,
    isbn VARCHAR(17) UNIQUE NOT NULL,
    clasificacionDewey VARCHAR(20),
    estanteria VARCHAR(20),
    idioma VARCHAR(30),
    libro_id BIGINT NOT NULL UNIQUE, -- Relación 1 a 1 con libro
    CONSTRAINT fk_ficha_libro FOREIGN KEY (libro_id)
        REFERENCES libro(id)
        ON DELETE CASCADE
);
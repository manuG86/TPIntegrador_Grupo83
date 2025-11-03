-- Conexión al servidor se hace con:
-- mysql -u tu_usuario -p < tpi_libro_ficha_schema.sql

-- === Reiniciar todo desde cero ===
DROP DATABASE IF EXISTS tpi_libro_ficha;
CREATE DATABASE tpi_libro_ficha CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE tpi_libro_ficha;

-- === Tabla: libro (lado A) ===
CREATE TABLE libro (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  eliminado    BOOLEAN NOT NULL DEFAULT FALSE,
  titulo       VARCHAR(150) NOT NULL,
  autor        VARCHAR(120) NOT NULL,
  editorial    VARCHAR(100),
  anioEdicion  INT
) ENGINE=InnoDB;

-- (Índices sencillos para búsquedas)
CREATE INDEX idx_libro_titulo ON libro (titulo);
CREATE INDEX idx_libro_autor  ON libro (autor);

-- === Tabla: ficha_bibliografica (lado B) ===
CREATE TABLE ficha_bibliografica (
  id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
  eliminado           BOOLEAN NOT NULL DEFAULT FALSE,
  isbn                VARCHAR(17) NOT NULL,
  clasificacionDewey  VARCHAR(20),
  estanteria          VARCHAR(20),
  idioma              VARCHAR(30),
  libro_id            BIGINT NOT NULL,
  CONSTRAINT uq_ficha_isbn  UNIQUE (isbn),       -- cada ISBN una sola vez
  CONSTRAINT uq_ficha_libro UNIQUE (libro_id),   -- relación 1:1 con libro
  CONSTRAINT fk_ficha_libro FOREIGN KEY (libro_id)
    REFERENCES libro(id)
    ON DELETE CASCADE
) ENGINE=InnoDB;

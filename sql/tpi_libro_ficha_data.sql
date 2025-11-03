USE tpi_libro_ficha;
SET NAMES utf8mb4;

-- Libros (A)
INSERT INTO libro (eliminado, titulo, autor, editorial, anioEdicion)
VALUES (FALSE, 'Introducción a la Programación', 'Juan Pérez', 'Editorial Alfa', 2018);
SET @libro1 := LAST_INSERT_ID();

INSERT INTO libro (eliminado, titulo, autor, editorial, anioEdicion)
VALUES (FALSE, 'Bases de Datos Avanzadas', 'María Gómez', 'TechBooks', 2020);
SET @libro2 := LAST_INSERT_ID();

INSERT INTO libro (eliminado, titulo, autor, editorial, anioEdicion)
VALUES (FALSE, 'Algoritmos y Estructuras de Datos', 'Carlos López', 'DataPress', 2019);
SET @libro3 := LAST_INSERT_ID();

-- Fichas (B) 1→1 con cada libro
INSERT INTO ficha_bibliografica
(eliminado, isbn, clasificacionDewey, estanteria, idioma, libro_id)
VALUES
(FALSE, '978-9876543210', '005.1',  'A1', 'Español', @libro1),
(FALSE, '978-1234567890', '005.74', 'B2', 'Español', @libro2),
(FALSE, '978-1112223334', '005.13', 'C3', 'Inglés',  @libro3);

USE tpi_libro_ficha;

-- Insertar Libros
INSERT INTO libro (titulo, autor, editorial, anioEdicion)
VALUES 
('Introducción a la Programación', 'Juan Pérez', 'Editorial Alfa', 2018),
('Bases de Datos Avanzadas', 'María Gómez', 'TechBooks', 2020),
('Algoritmos y Estructuras de Datos', 'Carlos López', 'DataPress', 2019);

-- Insertar Fichas Bibliográficas (con relación 1→1)
INSERT INTO ficha_bibliografica (isbn, clasificacionDewey, estanteria, idioma, libro_id)
VALUES
('978-9876543210', '005.1', 'A1', 'Español', 1),
('978-1234567890', '005.74', 'B2', 'Español', 2),
('978-1112223334', '005.13', 'C3', 'Inglés', 3);

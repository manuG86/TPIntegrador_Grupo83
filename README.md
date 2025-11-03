NOTA:
Este archivo pertenece al TFI de Bases de Datos I.
Sin embargo, los mismos scripts se utilizan también en el TFI de Programación 2
como soporte para las pruebas del sistema Libro–Ficha.

# Proyecto: Base de datos tpi_libro_ficha

Este proyecto contiene los scripts SQL necesarios para crear y poblar la base de datos **tpi_libro_ficha**.

## Archivos incluidos

- **tpi_libro_ficha_schema.sql**
  - Crea la base de datos `tpi_libro_ficha`.
  - Define las tablas `libro` y `ficha_bibliografica`.
  - Establece claves primarias, foráneas y restricciones de unicidad.
  - Incluye la relación 1→1 entre `libro` y `ficha_bibliografica`.

- **tpi_libro_ficha_data.sql**
  - Inserta datos de prueba en las tablas creadas.
  - Permite verificar la estructura y la relación 1→1 entre las entidades.

## Requisitos

- MySQL Server instalado.
- Usuario con permisos para crear bases de datos y tablas.

## Instrucciones de uso

1. **Conectarse al servidor MySQL**  
   Ejecuta el siguiente comando en la terminal (reemplaza `usuario` por tu nombre de usuario):

   ```bash
   mysql -u usuario -p < tpi_libro_ficha_schema.sql
   ```

   Esto eliminará la base de datos anterior (si existiera), creará una nueva y configurará las tablas.

2. **Cargar los datos de prueba**  
   Una vez creada la base de datos y las tablas, carga los datos de prueba con:

   ```bash
   mysql -u usuario -p < tpi_libro_ficha_data.sql
   ```

3. **Verificación**  
   Puedes conectarte a MySQL y verificar los datos con:

   ```sql
   USE tpi_libro_ficha;
   SELECT * FROM libro;
   SELECT * FROM ficha_bibliografica;
   ```

   Debes ver tres registros de libros y sus fichas bibliográficas asociadas.

## Notas

- La relación 1→1 se implementa con una clave foránea única en `ficha_bibliografica.libro_id` apuntando a `libro.id` con `ON DELETE CASCADE`.
- La codificación utilizada para la base de datos es `utf8mb4` para soportar caracteres especiales y emojis.

---


🧩 TFI – Programación 2
Tecnicatura Universitaria en Programación a Distancia – UTN Grupo 83 Año lectivo: 2025

📘 Proyecto: Sistema “Libro–Ficha Bibliográfica”
Este trabajo final integrador implementa una aplicación Java por capas que gestiona libros y sus fichas bibliográficas, manteniendo la relación 1→1 entre ambas entidades. El sistema se conecta a una base de datos MySQL y demuestra conceptos de persistencia, validación, transacciones y atomicidad.

⚙️ Arquitectura de Capas
Capa	Paquete	Descripción
Entidad (Modelo)	LibroFicha_entities	Define las clases Libro y FichaBibliografica con sus atributos y métodos.
DAO (Acceso a Datos)	LibroFicha_dao	Implementa operaciones CRUD mediante PreparedStatement.
Service (Lógica de Negocio)	LibroFicha_service	Aplica validaciones y maneja transacciones (commit / rollback).
Configuración	LibroFicha_config	Contiene DatabaseConnection que lee las credenciales de db.properties.
Presentación	LibroFicha_main	Interfaz de consola (AppMenu y Main).
Integración BD I	SeguridadBD.integracionBDI	Clases del TFI de Bases de Datos I usadas para consultas seguras y DTOs (únicamente referenciales).
🧠 Características Principales
CRUD completo sobre la entidad Libro.
Inserción compuesta Libro + FichaBibliográfica en una transacción atómica (A→B).
Rollback simulado para demostrar atomicidad.
Baja lógica coordinada (1→1).
Búsqueda por ISBN (consulta JOIN Libro–Ficha).
Validaciones de negocio y manejo de excepciones controladas.
Separación estricta de capas y uso de PreparedStatement (sin concatenar SQL).
🗄️ Base de Datos
Schema: tpi_libro_ficha
Motor: MySQL 8.0
Scripts: en /sql/
mysql -u root -p < sql/tpi_libro_ficha_schema.sql
mysql -u root -p < sql/tpi_libro_ficha_data.sql
Configuración de conexión → /resources/db.properties:

url=jdbc:mysql://localhost:3306/tpi_libro_ficha
user=root
password=tu_clave
▶️ Ejecución del Sistema
Ejecutar los scripts SQL para crear la base.
Iniciar la aplicación con LibroFicha_main.Main.
Se verifica la conexión y aparece el menú principal:
1) Listar Libros  
2) Insertar Libro + Ficha (transacción A→B)  
3) Actualizar Libro  
4) Eliminar (baja lógica)  
5) Buscar por ID  
6) Buscar por ISBN  
0) Salir
💾 Estructura del Proyecto
TPIntegrador_Grupo83/
├── src/
│   ├── LibroFicha_entities/
│   ├── LibroFicha_dao/
│   ├── LibroFicha_service/
│   ├── LibroFicha_config/
│   ├── LibroFicha_main/
|   ├── db.properties
│   └── SeguridadBD/integracionBDI/
│
├── sql/
│   ├── tpi_libro_ficha_schema.sql
│   ├── tpi_libro_ficha_data.sql
│   └── README_crear_BBDD.txt
│
└── docs/
    ├── UML_final.png  
    ├── Informe_TFI_P2.pdf  
    └── README.md
🎥 Video Demostrativo
El video muestra:

Alta compuesta A→B (con commit).
Rollback simulado por fallo lógico.
Búsqueda por ISBN con JOIN.
Baja lógica coordinada Libro–Ficha.
Justificación del orden A→B (por restricciones FK no diferibles en MySQL).
👥 Integrantes – Grupo 83
Integrante	Rol / Etapa
Manuel Galarza	Diseño del dominio y UML, creación del modelo relacional y scripts SQL, pruebas en MySQL.
Gabriel Etchegoyen	Implementación de Entities y DAO, validaciones de campos y documentación técnica del acceso a datos.
Damián Nogueira	Implementación de Services, manejo de transacciones y rollback, desarrollo de AppMenu y conclusiones.
🗣️ Participación en la presentación del video
Manuel: Presenta el dominio, el UML y la base de datos.
Gabriel: Explica las entidades, los DAO y las validaciones.
Damián: Demuestra el funcionamiento del sistema, las transacciones y realiza el cierre con las conclusiones.
📅 Fecha de entrega
Noviembre 2025

🧩 Notas Finales
Este proyecto integra conceptos vistos en Bases de Datos I y Programación 2. Las clases en SeguridadBD.integracionBDI pertenecen al TFI de Bases de Datos I y se mantienen solo como material de integración. El sistema Java ejecutable corresponde al TFI de Programación 2, cumpliendo con todos los criterios de arquitectura, transacciones, validaciones y documentación exigidos por la rúbrica.
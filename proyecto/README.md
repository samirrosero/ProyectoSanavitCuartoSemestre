# SANAVIT - Sistema de Gestión de Citas Médicas

## Descripción
SANAVIT es un sistema de gestión de citas médicas desarrollado en Java que permite administrar pacientes, médicos, citas médicas, historias clínicas y recetas médicas. El sistema cuenta con una interfaz gráfica intuitiva y está respaldado por una base de datos MySQL para el almacenamiento seguro de la información.

## Equipo de Desarrollo
- Valeri Sofia Solis Borja
- Samir Rosero
- José Julian Peñaloza
- Luis Fernando Piamba

## Tecnologías Utilizadas

### Dependencias Principales
1. **MySQL Connector/J (8.0.33)**
   - Conector para la integración con bases de datos MySQL
   - Permite la gestión de conexiones y operaciones con la base de datos

2. **JFreeChart (1.0.19)**
   - Biblioteca para la creación de gráficos y visualizaciones
   - Utilizada para generar reportes y estadísticas

3. **JCalendar (1.4)**
   - Componente de calendario para Java Swing
   - Facilita la selección de fechas en la interfaz gráfica

## Estructura del Proyecto
El proyecto sigue una arquitectura MVC (Modelo-Vista-Controlador):

- **Modelo**: Clases para la lógica de negocio y acceso a datos
  - Gestión de entidades (Paciente, Médico, Cita, etc.)
  - DAOs para operaciones con la base de datos

- **Vista**: Interfaces gráficas en Java Swing
  - Paneles y ventanas para diferentes funcionalidades
  - Formularios de registro y gestión

- **Controlador**: Clases que manejan la lógica entre el modelo y la vista
  - Controladores para cada entidad principal
  - Manejo de eventos y validaciones

## Características Principales
- Gestión de usuarios y roles
- Agendamiento de citas médicas
- Manejo de historias clínicas
- Gestión de recetas médicas
- Documentos anexos
- Reportes y estadísticas
- Interfaz gráfica intuitiva

## Requisitos del Sistema
- Java 17 o superior
- MySQL Server
- Maven para la gestión de dependencias

## Configuración del Proyecto
1. Clonar el repositorio
2. Configurar la base de datos MySQL usando el script en `src/main/resources/db.sql`
3. Ajustar las credenciales de la base de datos en el archivo de configuración
4. Ejecutar el proyecto usando Maven:
   ```bash
   mvn clean install
   mvn exec:java -Dexec.mainClass="com.proyecto.sanavit.Main"
   ```

## Licencia
Este proyecto es parte del trabajo académico del cuarto semestre.

---
© 2025 SANAVIT - Todos los derechos reservados
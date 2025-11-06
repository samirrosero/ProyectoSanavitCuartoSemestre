CREATE DATABASE historia_clinica_sanavit_proyec;
use historia_clinica_sanavit_proyec;

CREATE TABLE rol 
(id_rol INT PRIMARY KEY auto_increment, 
nombre_rol VARCHAR(100) not null);
desc rol;

CREATE TABLE usuario 
(id_usuario INT auto_increment primary KEY,
id_rol INT,
nombre_usuario VARCHAR(100),
contraseña VARCHAR(100),
FOREIGN KEY (id_rol) REFERENCES rol(id_rol));
desc usuario;

CREATE TABLE portafolio
(id_portafolio INT primary key auto_increment,
salud varchar(50),
convenios varchar(50),
afiliaciones varchar(50));
desc portafolio;

CREATE TABLE paciente 
(id_paciente INT auto_increment primary KEY,
id_portafolio int,
nombre VARCHAR(100),
correo VARCHAR(100),
edad INT,
telefono varchar(100),
sexo VARCHAR(100),
direccion VARCHAR(100),
identificacion varchar(100),
id_usuario int unique, 
foreign key(id_usuario) references usuario(id_usuario),
foreign key (id_portafolio) references portafolio(id_portafolio));
desc paciente;


CREATE TABLE medico 
(id_medico INT auto_increment primary KEY,
nombre VARCHAR(100),
especialidad VARCHAR(100),
id_usuario int unique not null,
foreign key (id_usuario) references usuario (id_usuario));
desc medico;

CREATE TABLE estado_cita
(id_estado_cita INT primary key auto_increment,
nombre_estado varchar (100));
desc estado_cita;

CREATE TABLE modalidad
(id_modalidad INT primary key auto_increment,
nombre_modalidad varchar(50));
desc modalidad;


CREATE TABLE cita 
(id_cita INT auto_increment primary KEY,
id_medico INT,
id_paciente INT,
id_estado_cita INT,
id_modalidad INT,
fecha_cita DATE,
hora_cita TIME,
FOREIGN KEY (id_medico) REFERENCES medico(id_medico),
FOREIGN KEY (id_paciente) REFERENCES paciente(id_paciente),
FOREIGN KEY (id_estado_cita) REFERENCES estado_cita(id_estado_cita),
FOREIGN KEY (id_modalidad) REFERENCES modalidad(id_modalidad));
desc cita;

CREATE TABLE ejecucionCita (
  id_ejecucionCita INT AUTO_INCREMENT PRIMARY KEY,
  id_cita INT,
  fecha_hora_ingreso DATETIME,
  fecha_salida DATETIME,
  duracion INT,
  FOREIGN KEY (id_cita) REFERENCES cita(id_cita));
desc ejecucionCita;


CREATE TABLE historia_clinica (
  id_historia_clinica INT AUTO_INCREMENT PRIMARY KEY,
  id_ejecucionCita INT,
  motivo_consulta TEXT,
  enfermedad_actual TEXT,
  antecedentes TEXT,
  diagnostico TEXT,
  tratamiento TEXT,
  evolucion TEXT,
  observaciones TEXT,
  FOREIGN KEY (id_ejecucionCita) REFERENCES ejecucionCita(id_ejecucionCita)
);


CREATE TABLE documento_anexo
(id_documento INT primary key auto_increment,
id_historia_clinica INT,
tipo VARCHAR(100),
ruta_archivo VARCHAR(200),
FOREIGN KEY (id_historia_clinica) REFERENCES historia_clinica(id_historia_clinica));
desc documento_anexo;

CREATE TABLE receta_medica 
(id_receta INT primary key auto_increment,
id_historia_clinica INT, 
medicamento TEXT,
indicaciones TEXT,
FOREIGN KEY (id_historia_clinica) REFERENCES historia_clinica(id_historia_clinica));
desc receta_medica;

-- Roles
INSERT INTO rol (nombre_rol) VALUES 
('Administrador'),
('Medico'),
('Gestor de Citas'),
('Paciente');
select * from rol;

-- Estados de citas
INSERT INTO estado_cita (nombre_estado)
VALUES ('Pendiente'), ('Confirmada'), ('Cancelada'), ('Finalizada');
select * from estado_cita;
-- modalidades de la cita
INSERT INTO modalidad (nombre_modalidad)
VALUES ('Presencial'), ('Virtual');
select * from modalidad;


-- ==========================================
-- CREACIÓN DE PROCEDIMIENTOS ALMACENADOS
-- ==========================================

-- Insertar usuario
DELIMITER //
CREATE PROCEDURE insertar_usuario (
    IN p_id_rol INT,
    IN p_nombre_usuario VARCHAR(100),
    IN p_contraseña VARCHAR(100)
)
BEGIN
    INSERT INTO usuario (id_rol, nombre_usuario, contraseña)
    VALUES (p_id_rol, p_nombre_usuario, p_contraseña);

    SELECT LAST_INSERT_ID() AS id_generado;
END //
DELIMITER ;

-- autenticar usuario
DELIMITER //
CREATE PROCEDURE autenticar_usuario (
    IN p_nombre_usuario VARCHAR(100),
    IN p_contraseña VARCHAR(100)
)
BEGIN
    SELECT u.id_usuario, u.id_rol, u.nombre_usuario, u.contraseña, r.nombre_rol AS nombreRol
    FROM usuario u
    JOIN rol r ON u.id_rol = r.id_rol
    WHERE u.nombre_usuario = p_nombre_usuario
      AND u.contraseña = p_contraseña;
END //
DELIMITER ;

-- obtener usuario
DELIMITER //
CREATE PROCEDURE obtener_usuario (
    IN p_nombre_usuario VARCHAR(100),
    IN p_contraseña VARCHAR(100)
)
BEGIN
    SELECT * FROM usuario
    WHERE nombre_usuario = p_nombre_usuario
      AND contraseña = p_contraseña;
END //
DELIMITER ;

-- burcar usuario por nombre
DELIMITER //
CREATE PROCEDURE buscar_usuario_por_nombre (
    IN p_nombre_usuario VARCHAR(100)
)
BEGIN
    SELECT * FROM usuario
    WHERE nombre_usuario = p_nombre_usuario;
END //
DELIMITER ;

-- obtener id de rol por nombre
DELIMITER //
CREATE PROCEDURE obtener_id_rol_por_nombre (
    IN p_nombre_rol VARCHAR(100)
)
BEGIN
    SELECT id_rol
    FROM rol
    WHERE LOWER(nombre_rol) = LOWER(p_nombre_rol);
END //
DELIMITER ;

-- cambiar contraseña
DELIMITER //
CREATE PROCEDURE cambiar_contraseña (
    IN p_id_usuario INT,
    IN p_nueva_contraseña VARCHAR(100)
)
BEGIN
    UPDATE usuario
    SET contraseña = p_nueva_contraseña
    WHERE id_usuario = p_id_usuario;
END //
DELIMITER ;

-- obtener roles
DELIMITER //
CREATE PROCEDURE obtener_roles ()
BEGIN
    SELECT id_rol, nombre_rol FROM rol;
END //
DELIMITER ;


-- insertar cita
DELIMITER //
CREATE PROCEDURE insertar_cita (
    IN p_id_medico INT,
    IN p_id_paciente INT,
    IN p_id_estado_cita INT,
    IN p_id_modalidad INT,
    IN p_fecha_cita DATE,
    IN p_hora_cita TIME
)
BEGIN
    INSERT INTO cita (id_medico, id_paciente, id_estado_cita, id_modalidad, fecha_cita, hora_cita)
    VALUES (p_id_medico, p_id_paciente, p_id_estado_cita, p_id_modalidad, p_fecha_cita, p_hora_cita);

    SELECT LAST_INSERT_ID() AS id_generado;
END //
DELIMITER ;

-- actualizar cita
DELIMITER //
CREATE PROCEDURE actualizar_cita (
    IN p_id_cita INT,
    IN p_id_medico INT,
    IN p_id_paciente INT,
    IN p_id_estado_cita INT,
    IN p_id_modalidad INT,
    IN p_fecha_cita DATE,
    IN p_hora_cita TIME
)
BEGIN
    UPDATE cita
    SET id_medico = p_id_medico,
        id_paciente = p_id_paciente,
        id_estado_cita = p_id_estado_cita,
        id_modalidad = p_id_modalidad,
        fecha_cita = p_fecha_cita,
        hora_cita = p_hora_cita
    WHERE id_cita = p_id_cita;
END //
DELIMITER ;

-- actualizar estado cita
DELIMITER //
CREATE PROCEDURE actualizar_estado_cita (
    IN p_id_cita INT,
    IN p_nuevo_estado INT
)
BEGIN
    UPDATE cita
    SET id_estado_cita = p_nuevo_estado
    WHERE id_cita = p_id_cita;
END //
DELIMITER ;

-- eliminar cita
DELIMITER //
CREATE PROCEDURE eliminar_cita (
    IN p_id_cita INT
)
BEGIN
    DELETE FROM cita WHERE id_cita = p_id_cita;
END //
DELIMITER ;

-- listar citas
DELIMITER //
CREATE PROCEDURE listar_citas ()
BEGIN
    SELECT * FROM cita;
END //
DELIMITER ;

-- obtener cita por id
DELIMITER //
CREATE PROCEDURE obtener_cita_por_id (
    IN p_id_cita INT
)
BEGIN
    SELECT * FROM cita WHERE id_cita = p_id_cita;
END //
DELIMITER ;

-- obtener citas por paciente
DELIMITER //
CREATE PROCEDURE obtener_citas_por_paciente (
    IN p_id_paciente INT
)
BEGIN
    SELECT * FROM cita WHERE id_paciente = p_id_paciente;
END //
DELIMITER ;

-- obtener citas por medicos
DELIMITER //
CREATE PROCEDURE obtener_citas_por_medico (
    IN p_id_medico INT
)
BEGIN
    SELECT * FROM cita WHERE id_medico = p_id_medico;
END //
DELIMITER ;



-- insertar paciente
DELIMITER //
CREATE PROCEDURE insertar_paciente (
    IN p_nombre VARCHAR(100),
    IN p_correo VARCHAR(100),
    IN p_edad INT,
    IN p_telefono VARCHAR(20),
    IN p_sexo VARCHAR(10),
    IN p_direccion VARCHAR(150),
    IN p_identificacion VARCHAR(20),
    IN p_id_usuario INT,
    OUT p_id_paciente INT
)
BEGIN
    INSERT INTO paciente (nombre, correo, edad, telefono, sexo, direccion, identificacion, id_usuario)
    VALUES (p_nombre, p_correo, p_edad, p_telefono, p_sexo, p_direccion, p_identificacion, p_id_usuario);
    
    SET p_id_paciente = LAST_INSERT_ID();
END$$

DELIMITER ;

-- actualizar paciente
DELIMITER //
CREATE PROCEDURE actualizar_paciente (
    IN p_id_paciente INT,
    IN p_nombre VARCHAR(100),
    IN p_correo VARCHAR(100),
    IN p_edad INT,
    IN p_telefono VARCHAR(20),
    IN p_sexo VARCHAR(10),
    IN p_direccion VARCHAR(150),
    IN p_identificacion VARCHAR(20),
    IN p_id_usuario INT
)
BEGIN
    UPDATE paciente
    SET nombre = p_nombre,
        correo = p_correo,
        edad = p_edad,
        telefono = p_telefono,
        sexo = p_sexo,
        direccion = p_direccion,
        identificacion = p_identificacion,
        id_usuario = p_id_usuario
    WHERE id_paciente = p_id_paciente;
END$$

DELIMITER ;

-- eliminar paciente
DELIMITER //
CREATE PROCEDURE eliminar_paciente (
    IN p_id_paciente INT
)
BEGIN
    DELETE FROM paciente
    WHERE id_paciente = p_id_paciente;
END$$

DELIMITER ;

-- obtener paciente por id
DELIMITER //
CREATE PROCEDURE obtener_paciente_por_id (
    IN p_id_paciente INT
)
BEGIN
    SELECT * FROM paciente
    WHERE id_paciente = p_id_paciente;
END$$

DELIMITER ;

-- obtener paciente por id usuario
DELIMITER //
CREATE PROCEDURE obtener_paciente_por_id_usuario (
    IN p_id_usuario INT
)
BEGIN
    SELECT * FROM paciente
    WHERE id_usuario = p_id_usuario;
END$$

DELIMITER ;

-- listar pacientes
DELIMITER //
CREATE PROCEDURE listar_pacientes()
BEGIN
    SELECT * FROM paciente;
END$$

DELIMITER ;

-- buscar paciente por nombre
DELIMITER //
CREATE PROCEDURE buscar_paciente_por_nombre (
    IN p_nombre VARCHAR(100)
)
BEGIN
    SELECT * FROM paciente
    WHERE nombre LIKE CONCAT('%', p_nombre, '%');
END$$

DELIMITER ;

-- obtener paciente por identificacion
DELIMITER //
CREATE PROCEDURE obtener_paciente_por_identificacion (
    IN p_identificacion VARCHAR(20)
)
BEGIN
    SELECT * FROM paciente
    WHERE identificacion = p_identificacion;
END$$

DELIMITER ;


-- Insertar médico
DELIMITER //
CREATE PROCEDURE insertar_medico(
    IN p_nombre VARCHAR(100),
    IN p_especialidad VARCHAR(100),
    IN p_id_usuario INT
)
BEGIN
    INSERT INTO medico (nombre, especialidad, id_usuario)
    VALUES (p_nombre, p_especialidad, p_id_usuario);
    SELECT LAST_INSERT_ID() AS id_medico;
END $$
DELIMITER ;

-- Listar todos los médicos
DELIMITER //
CREATE PROCEDURE listar_medicos()
BEGIN
    SELECT * FROM medico;
END $$
DELIMITER ;

-- Obtener médico por ID
DELIMITER //
CREATE PROCEDURE obtener_medico_por_id(IN p_id_medico INT)
BEGIN
    SELECT * FROM medico WHERE id_medico = p_id_medico;
END $$
DELIMITER ;

-- Obtener médico por ID de usuario
DELIMITER //
CREATE PROCEDURE obtener_medico_por_id_usuario(IN p_id_usuario INT)
BEGIN
    SELECT * FROM medico WHERE id_usuario = p_id_usuario;
END $$
DELIMITER ;

-- Actualizar médico
DELIMITER //
CREATE PROCEDURE actualizar_medico(
    IN p_id_medico INT,
    IN p_nombre VARCHAR(100),
    IN p_especialidad VARCHAR(100)
)
BEGIN
    UPDATE medico
    SET nombre = p_nombre,
        especialidad = p_especialidad
    WHERE id_medico = p_id_medico;
END $$
DELIMITER ;

-- Eliminar médico
DELIMITER //
CREATE PROCEDURE eliminar_medico(IN p_id_medico INT)
BEGIN
    DELETE FROM medico WHERE id_medico = p_id_medico;
END $$
DELIMITER ;

-- Buscar médico por nombre
DELIMITER //
CREATE PROCEDURE obtener_medico_por_nombre(IN p_nombre VARCHAR(100))
BEGIN
    SELECT * FROM medico WHERE nombre = p_nombre;
END $$
DELIMITER ;



-- Insertar historia clínica
DELIMITER //
CREATE PROCEDURE sp_insertar_historia_clinica(
    IN p_id_ejecucionCita INT,
    IN p_motivo_consulta VARCHAR(255),
    IN p_enfermedad_actual TEXT,
    IN p_antecedentes TEXT,
    IN p_diagnostico TEXT,
    IN p_tratamiento TEXT,
    IN p_evolucion TEXT,
    IN p_observaciones TEXT
)
BEGIN
    INSERT INTO historia_clinica (
        id_ejecucionCita, motivo_consulta, enfermedad_actual, antecedentes,
        diagnostico, tratamiento, evolucion, observaciones
    ) VALUES (
        p_id_ejecucionCita, p_motivo_consulta, p_enfermedad_actual,
        p_antecedentes, p_diagnostico, p_tratamiento, p_evolucion, p_observaciones
    );
END //
DELIMITER ;

-- Actualizar historia clínica
DELIMITER //
CREATE PROCEDURE sp_actualizar_historia_clinica(
    IN p_id_historia_clinica INT,
    IN p_id_ejecucionCita INT,
    IN p_motivo_consulta VARCHAR(255),
    IN p_enfermedad_actual TEXT,
    IN p_antecedentes TEXT,
    IN p_diagnostico TEXT,
    IN p_tratamiento TEXT,
    IN p_evolucion TEXT,
    IN p_observaciones TEXT
)
BEGIN
    UPDATE historia_clinica
    SET id_ejecucionCita = p_id_ejecucionCita,
        motivo_consulta = p_motivo_consulta,
        enfermedad_actual = p_enfermedad_actual,
        antecedentes = p_antecedentes,
        diagnostico = p_diagnostico,
        tratamiento = p_tratamiento,
        evolucion = p_evolucion,
        observaciones = p_observaciones
    WHERE id_historia_clinica = p_id_historia_clinica;
END //
DELIMITER ;

-- Eliminar historia clínica
DELIMITER //
CREATE PROCEDURE sp_eliminar_historia_clinica(IN p_id_historia_clinica INT)
BEGIN
    DELETE FROM historia_clinica WHERE id_historia_clinica = p_id_historia_clinica;
END //
DELIMITER ;

-- Listar todas las historias clínicas
DELIMITER //
CREATE PROCEDURE sp_listar_historias_clinicas()
BEGIN
    SELECT * FROM historia_clinica;
END //
DELIMITER ;

-- Obtener historia clínica por paciente
DELIMITER //
CREATE PROCEDURE sp_historia_por_paciente(IN p_id_paciente INT)
BEGIN
    SELECT * FROM historia_clinica WHERE id_paciente = p_id_paciente;
END //
DELIMITER ;



-- Insertar receta
DELIMITER //
CREATE PROCEDURE sp_insertar_receta(
    IN p_id_historia_clinica INT,
    IN p_medicamento VARCHAR(255),
    IN p_indicaciones TEXT
)
BEGIN
    INSERT INTO receta_medica (id_historia_clinica, medicamento, indicaciones)
    VALUES (p_id_historia_clinica, p_medicamento, p_indicaciones);
END //
DELIMITER ;

-- Actualizar receta
DELIMITER //
CREATE PROCEDURE sp_actualizar_receta(
    IN p_id_receta INT,
    IN p_id_historia_clinica INT,
    IN p_medicamento VARCHAR(255),
    IN p_indicaciones TEXT
)
BEGIN
    UPDATE receta_medica
    SET id_historia_clinica = p_id_historia_clinica,
        medicamento = p_medicamento,
        indicaciones = p_indicaciones
    WHERE id_receta = p_id_receta;
END //
DELIMITER ;

-- Eliminar receta
DELIMITER //
CREATE PROCEDURE sp_eliminar_receta(IN p_id_receta INT)
BEGIN
    DELETE FROM receta_medica WHERE id_receta = p_id_receta;
END //
DELIMITER ;

-- Listar recetas
DELIMITER //
CREATE PROCEDURE sp_listar_recetas()
BEGIN
    SELECT * FROM receta_medica;
END //
DELIMITER ;


-- Insertar documento
DELIMITER //
CREATE PROCEDURE sp_insertar_documento(
    IN p_id_historia_clinica INT,
    IN p_tipo VARCHAR(100),
    IN p_ruta_archivo VARCHAR(255)
)
BEGIN
    INSERT INTO documento_anexo (id_historia_clinica, tipo, ruta_archivo)
    VALUES (p_id_historia_clinica, p_tipo, p_ruta_archivo);
END //
DELIMITER ;

-- Actualizar documento
DELIMITER //
CREATE PROCEDURE sp_actualizar_documento(
    IN p_id_documento INT,
    IN p_id_historia_clinica INT,
    IN p_tipo VARCHAR(100),
    IN p_ruta_archivo VARCHAR(255)
)
BEGIN
    UPDATE documento_anexo
    SET id_historia_clinica = p_id_historia_clinica,
        tipo = p_tipo,
        ruta_archivo = p_ruta_archivo
    WHERE id_documento = p_id_documento;
END //
DELIMITER ;

-- Eliminar documento
DELIMITER //
CREATE PROCEDURE sp_eliminar_documento(IN p_id_documento INT)
BEGIN
    DELETE FROM documento_anexo WHERE id_documento = p_id_documento;
END //
DELIMITER ;

-- Listar documentos por historia clínica
DELIMITER //
CREATE PROCEDURE sp_listar_documentos_por_historia(IN p_id_historia_clinica INT)
BEGIN
    SELECT * FROM documento_anexo WHERE id_historia_clinica = p_id_historia_clinica;
END //
DELIMITER ;



-- Insertar ejecución de cita
DELIMITER //
CREATE PROCEDURE sp_insertar_ejecucion_cita(
    IN p_id_cita INT,
    IN p_fecha_ingreso DATETIME,
    IN p_fecha_salida DATETIME,
    IN p_duracion INT
)
BEGIN
    INSERT INTO ejecucionCita (id_cita, fecha_hora_ingreso, fecha_salida, duracion)
    VALUES (p_id_cita, p_fecha_ingreso, p_fecha_salida, p_duracion);
END //
DELIMITER ;

-- Actualizar ejecución
DELIMITER //
CREATE PROCEDURE sp_actualizar_ejecucion_cita(
    IN p_id_ejecucionCita INT,
    IN p_id_cita INT,
    IN p_fecha_ingreso DATETIME,
    IN p_fecha_salida DATETIME,
    IN p_duracion INT
)
BEGIN
    UPDATE ejecucionCita
    SET id_cita = p_id_cita,
        fecha_hora_ingreso = p_fecha_ingreso,
        fecha_salida = p_fecha_salida,
        duracion = p_duracion
    WHERE id_ejecucionCita = p_id_ejecucionCita;
END //
DELIMITER ;

-- Eliminar ejecución
DELIMITER //
CREATE PROCEDURE sp_eliminar_ejecucion_cita(IN p_id_ejecucionCita INT)
BEGIN
    DELETE FROM ejecucionCita WHERE id_ejecucionCita = p_id_ejecucionCita;
END //
DELIMITER ;

-- Listar ejecuciones
DELIMITER //
CREATE PROCEDURE sp_listar_ejecuciones_cita()
BEGIN
    SELECT * FROM ejecucionCita;
END //
DELIMITER ;

select * from paciente;
select * from medico;
select * from usuario;
select * from cita;
select * from ejecucionCita;
select * from historia_clinica;
select * from documento_anexo;
select * from receta_medica;
select * from portafolio;
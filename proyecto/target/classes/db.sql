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

CREATE TABLE paciente 
(id_paciente INT auto_increment primary KEY,
nombre VARCHAR(100),
correo VARCHAR(100),
edad INT,
telefono varchar(100),
sexo VARCHAR(100),
direccion VARCHAR(100),
identificacion varchar(100),
id_usuario int unique, 
foreign key(id_usuario) references usuario(id_usuario));
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

CREATE TABLE portafolio
(id_portafolio INT primary key auto_increment,
salud varchar(50),
convenios varchar(50),
afiliaciones varchar(50));
desc portafolio;

CREATE TABLE cita 
(id_cita INT auto_increment primary KEY,
id_medico INT,
id_paciente INT,
id_estado_cita INT,
id_modalidad INT,
id_portafolio INT,
fecha_cita DATE,
hora_cita TIME,
FOREIGN KEY (id_medico) REFERENCES medico(id_medico),
FOREIGN KEY (id_paciente) REFERENCES paciente(id_paciente),
FOREIGN KEY (id_estado_cita) REFERENCES estado_cita(id_estado_cita),
FOREIGN KEY (id_modalidad) REFERENCES modalidad(id_modalidad),
FOREIGN KEY (id_portafolio) REFERENCES portafolio(id_portafolio));
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
('Médico'),
('Gestor de Citas'),
('Paciente');
select * from rol;


-- === CRUD USUARIO ===
DELIMITER //
CREATE PROCEDURE insertarUsuario(
    IN p_id_rol INT,
    IN p_nombre_usuario VARCHAR(100),
    IN p_contraseña VARCHAR(100)
)
BEGIN
    INSERT INTO usuario (id_rol, nombre_usuario, contraseña)
    VALUES (p_id_rol, p_nombre_usuario, p_contraseña);

    SELECT CAST(CONCAT('Ahora hay ', COUNT(id_usuario), ' usuarios') AS CHAR) AS resultado
    FROM usuario;
END //
DELIMITER ;

CALL insertarUsuario();
select * from usuario;

DELIMITER //
CREATE PROCEDURE mostrarUsuario()
BEGIN
    SELECT * FROM usuario;
END //
DELIMITER ;

Call mostrarUsuario();

DELIMITER //
CREATE PROCEDURE actualizarUsuario(
    IN p_id_usuario INT,
    IN p_nombre_usuario VARCHAR(100),
    IN p_contraseña VARCHAR(100)
)
BEGIN
    UPDATE usuario
    SET nombre_usuario = p_nombre_usuario,
        contraseña = p_contraseña
    WHERE id_usuario = p_id_usuario;
END //
DELIMITER ;

CALL actualizarUsuario();
select * from usuario;

DELIMITER //
CREATE PROCEDURE eliminarUsuario(IN p_id_usuario INT)
BEGIN
    DELETE FROM usuario WHERE id_usuario = p_id_usuario;
END //
DELIMITER ;

CALL eliminarUsuario();
select * from usuario;



-- === CRUD PACIENTE ===
DELIMITER //
CREATE PROCEDURE insertarPaciente(
    IN p_nombre VARCHAR(100),
    IN p_correo VARCHAR(100),
    IN p_edad INT,
    IN p_telefono VARCHAR(100),
    IN p_sexo VARCHAR(100),
    IN p_direccion VARCHAR(100),
    IN p_identificacion VARCHAR(100),
    IN p_id_usuario INT
)
BEGIN
    INSERT INTO paciente (nombre, correo, edad, telefono, sexo, direccion, identificacion, id_usuario)
    VALUES (p_nombre, p_correo, p_edad, p_telefono, p_sexo, p_direccion, p_identificacion, p_id_usuario);
    
	SELECT CAST(CONCAT('Ahora hay ', COUNT(id_paciente), ' Paciente') AS CHAR) AS resultado
    FROM paciente;
END //
DELIMITER ;

CALL insertarPaciente();
select * from paciente;

DELIMITER //
CREATE PROCEDURE mostrarPaciente()
BEGIN
    SELECT * FROM paciente;
END //
DELIMITER ;

Call mostrarPaciente();

DELIMITER //
CREATE PROCEDURE actualizarPaciente(
    IN p_id_paciente INT,
	IN p_nombre VARCHAR(100),
    IN p_correo VARCHAR(100),
    IN p_edad INT,
    IN p_telefono VARCHAR(100),
    IN p_sexo VARCHAR(100),
    IN p_direccion VARCHAR(100),
    IN p_identificacion VARCHAR(100),
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
END //
DELIMITER ;

CALL actualizarPaciente();
select * from paciente;

DELIMITER //
CREATE PROCEDURE eliminarPaciente(IN p_id_paciente INT)
BEGIN
    DELETE FROM paciente WHERE id_paciente = p_id_paciente;
END //
DELIMITER ;

CALL eliminarPaciente();
select * from paciente;





-- ==== CRUD MEDICO ====
DELIMITER //
CREATE PROCEDURE insertarMedico(
    IN p_nombre VARCHAR(100),
    IN p_especialidad VARCHAR(100),
    IN p_id_usuario INT
)
BEGIN
    INSERT INTO medico (nombre, especialidad, id_usuario)
    VALUES (p_nombre, p_especialidad, p_id_usuario);
    SELECT CAST(CONCAT('Ahora hay ', COUNT(id_medico), ' Medico') AS CHAR) AS resultado
    FROM medico;
END //
DELIMITER ;

CALL insertarMedico();
select * from medico;

DELIMITER //
CREATE PROCEDURE mostrarMedico()
BEGIN
    SELECT * FROM medico;
END //
DELIMITER ;
Call mostrarMedico();


DELIMITER //
CREATE PROCEDURE actualizarMedico(
    IN p_id_medico INT,
    IN p_especialidad VARCHAR(100)
)
BEGIN
    UPDATE medico
    SET especialidad = p_especialidad
    WHERE id_medico = p_id_medico;
END //
DELIMITER ;
CALL actualizarMedico();
select * from medico;

DELIMITER //
CREATE PROCEDURE eliminarMedico(IN p_id_medico INT)
BEGIN
    DELETE FROM medico WHERE id_medico = p_id_medico;
END //
DELIMITER ;
CALL eliminarMedico();
select * from medico;




-- == ESTADO CITA ==
DELIMITER //
CREATE PROCEDURE insertarEstadoCita(
IN p_nombre_estado VARCHAR(30)

)
BEGIN
   INSERT INTO estado_cita (nombre_estado)
   VALUES (p_nombre_estado);
   SELECT CAST(CONCAT('Ahora hay ', COUNT(id_estado_cita), ' Estado Cita') AS CHAR) AS resultado
    FROM estado_cita;
END //
DELIMITER //

CALL insetarEstadoCita();
select * from estado_cita;

DELIMITER //
CREATE PROCEDURE mostrarEstadoCita()
BEGIN
    SELECT * FROM estado_cita;
END //
DELIMITER ;

CALL mostrarEstadoCita();


DELIMITER //
CREATE PROCEDURE actualizarEstadoCita(
IN p_id_estado_cita INT,
IN p_nombre_estado VARCHAR(30)

)
BEGIN
    UPDATE estado_cita
    SET nombre_estado = p_estado_cita
    WHERE id_estado_cita = p_id_estado_cita;
END //
DELIMITER ;

CALL actualizarEstadoCita();
select * from estado_cita;

DELIMITER //
CREATE PROCEDURE eliminarEstadoCita(IN p_id_estado_cita INT)
BEGIN
    DELETE FROM estado_cita WHERE id_estado_cita = p_id_estado_cita;
END //
DELIMITER ;

CALL eliminarEstadoCita();
select * from estado_cita;




-- == MODALIDAD ==
DELIMITER //
CREATE PROCEDURE insetarModalidad(
	IN P_id_modalidad INT,
    IN p_nombre_modalidad VARCHAR(50)
)
BEGIN 
    INSERT INTO modalidad (id_modalidad, nombre_modalidad)
    VALUES (p_id_modalidad, p_nombre_modalidad);
END //
DELIMITER ;

CALL insertarModalidad();
select * from modalidad;

DELIMITER //
CREATE PROCEDURE mostrarModalidad()
BEGIN
    SELECT * FROM modalidad;
END //
DELIMITER ;

CALL mostrarModalidad();

DELIMITER //
CREATE PROCEDURE actualizarModalidad(
IN p_id_modalidad INT,
IN p_nombre_modalidad VARCHAR(50)

)
BEGIN
    UPDATE modalidad
    SET nombre_modalidad = p_nombre_modalidad
    WHERE id_modelidad = p_id_modalidad;
END //
DELIMITER ;

CALL actualizarModalidad();
select * from modalidad;

DELIMITER //
CREATE PROCEDURE eliminarModalidad(IN p_id_modalidad INT)
BEGIN
    DELETE FROM modalidad WHERE id_modalidad = p_id_modalidad;
END //
DELIMITER ;

CALL eliminarModalidad();
select * from modalidad;




-- == PORTAFOLIO ==
DELIMITER //
CREATE PROCEDURE insertarPortafolio(
    IN p_salud VARCHAR(50),
    IN p_convenios VARCHAR(50),
    IN p_afiliaciones VARCHAR(50)
)
BEGIN
    INSERT INTO portafolio (salud, convenios, afiliaciones)
    VALUES (p_salud, p_convenios, p_afiliaciones);
END //
DELIMITER ;
CALL insertarPortafolio();

DELIMITER //
CREATE PROCEDURE mostrarPortafolio()
BEGIN
    SELECT * FROM portafolio;
END //
DELIMITER ;
CALL mostrarPortafolio();


DELIMITER //
CREATE PROCEDURE actualizarPortafolio(
    IN p_id_portafolio INT,
    IN p_salud VARCHAR(50),
    IN p_convenios VARCHAR(50),
    IN p_afiliaciones VARCHAR(50)
)
BEGIN
    UPDATE portafolio
    SET salud = p_salud,
        convenios = p_convenios,
        afiliaciones = p_afiliaciones
    WHERE id_portafolio = p_id_portafolio;
END //
DELIMITER ;
CALL actualizarPortafolio();


DELIMITER //
CREATE PROCEDURE eliminarPortafolio(IN p_id_portafolio INT)
BEGIN
    DELETE FROM portafolio WHERE id_portafolio = p_id_portafolio;
END //
DELIMITER ;
CALL eliminarPortafolio();




-- == CITA ==
DELIMITER //
CREATE PROCEDURE insertarCita(
    IN p_id_medico INT,
    IN p_id_paciente INT,
    IN p_id_estado_cita INT,
    IN p_id_modalidad INT,
    IN p_id_portafolio INT,
    IN p_fecha_cita DATE,
    IN p_hora_cita TIME
)
BEGIN
    INSERT INTO cita (id_medico, id_paciente, id_estado_cita, id_modalidad, id_portafolio, fecha_cita, hora_cita)
    VALUES (p_id_medico, p_id_paciente, p_id_estado_cita, p_id_modalidad, p_id_portafolio, p_fecha_cita, p_hora_cita);
END //
DELIMITER ;


DELIMITER //
CREATE PROCEDURE mostrarCitas()
BEGIN
    SELECT * FROM cita;
END //
DELIMITER ;


DELIMITER //
CREATE PROCEDURE actualizarCita(
    IN p_id_cita INT,
    IN p_id_medico INT,
    IN p_id_paciente INT,
    IN p_id_estado_cita INT,
    IN p_id_modalidad INT,
    IN p_id_portafolio INT,
    IN p_fecha_cita DATE,
    IN p_hora_cita TIME
)
BEGIN
    UPDATE cita
    SET id_medico = p_id_medico,
        id_paciente = p_id_paciente,
        id_estado_cita = p_id_estado_cita,
        id_modalidad = p_id_modalidad,
        id_portafolio = p_id_portafolio,
        fecha_cita = p_fecha_cita,
        hora_cita = p_hora_cita
    WHERE id_cita = p_id_cita;
END //
DELIMITER ;


DELIMITER //
CREATE PROCEDURE eliminarCita(
    IN p_id_cita INT
)
BEGIN
    DELETE FROM cita WHERE id_cita = p_id_cita;
END //
DELIMITER ;



-- == EJECUCION CITA ==
DELIMITER //
CREATE PROCEDURE insertarEjecucionCita(
    IN p_id_cita INT,
    IN p_fecha_hora_ingreso DATETIME,
    IN p_fecha_salida DATETIME,
    IN p_duracion INT
)
BEGIN
    INSERT INTO ejecucionCita (id_cita, fecha_hora_ingreso, fecha_salida, duracion)
    VALUES (p_id_cita, p_fecha_hora_ingreso, p_fecha_salida, p_duracion);
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE mostrarEjecucionCitas()
BEGIN
    SELECT * FROM ejecucionCita;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE actualizarEjecucionCita(
    IN p_id_ejecucionCita INT,
    IN p_id_cita INT,
    IN p_fecha_hora_ingreso DATETIME,
    IN p_fecha_salida DATETIME,
    IN p_duracion INT
)
BEGIN
    UPDATE ejecucionCita
    SET id_cita = p_id_cita,
        fecha_hora_ingreso = p_fecha_hora_ingreso,
        fecha_salida = p_fecha_salida,
        duracion = p_duracion
    WHERE id_ejecucionCita = p_id_ejecucionCita;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE eliminarEjecucionCita(IN p_id_ejecucionCita INT)
BEGIN
    DELETE FROM ejecucionCita WHERE id_ejecucionCita = p_id_ejecucionCita;
END //
DELIMITER ;



-- ==== HISTORIA CLÍNICA ====
DELIMITER //
CREATE PROCEDURE insertarHistoriaClinica(
    IN p_id_ejecucionCita INT,
    IN p_motivo_consulta TEXT,
    IN p_enfermedad_actual TEXT,
    IN p_antecedentes TEXT,
    IN p_diagnostico TEXT,
    IN p_tratamiento TEXT,
    IN p_evolucion TEXT,
    IN p_observaciones TEXT
)
BEGIN
    INSERT INTO historia_clinica (id_ejecucionCita, motivo_consulta, enfermedad_actual, antecedentes, diagnostico, tratamiento, evolucion, observaciones)
    VALUES (p_id_ejecucionCita, p_motivo_consulta, p_enfermedad_actual, p_antecedentes, p_diagnostico, p_tratamiento, p_evolucion, p_observaciones);
END //
DELIMITER ;
CALL insertarHistoriaClinica();
select * from historia_clinica;

DELIMITER //
CREATE PROCEDURE mostrarHistoriasClinicas()
BEGIN
    SELECT * FROM historia_clinica;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE actualizarHistoriaClinica(
    IN p_id_historia_clinica INT,
    IN p_id_ejecucionCita INT,
    IN p_motivo_consulta TEXT,
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

DELIMITER //
CREATE PROCEDURE eliminarHistoriaClinica(IN p_id_historia_clinica INT)
BEGIN
    DELETE FROM historia_clinica WHERE id_historia_clinica = p_id_historia_clinica;
END //
DELIMITER ;


-- == DOCUMENTO ANEXO ==
DELIMITER //
CREATE PROCEDURE insertarDocumentoAnexo(
    IN p_id_historia_clinica INT,
    IN p_tipo VARCHAR(100),
    IN p_ruta_archivo VARCHAR(200)
)
BEGIN
    INSERT INTO documento_anexo (id_historia_clinica, tipo, ruta_archivo)
    VALUES (p_id_historia_clinica, p_tipo, p_ruta_archivo);
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE mostrarDocumentosAnexos()
BEGIN
    SELECT * FROM documento_anexo;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE actualizarDocumentoAnexo(
    IN p_id_documento INT,
    IN p_id_historia_clinica INT,
    IN p_tipo VARCHAR(100),
    IN p_ruta_archivo VARCHAR(200)
)
BEGIN
    UPDATE documento_anexo
    SET id_historia_clinica = p_id_historia_clinica,
        tipo = p_tipo,
        ruta_archivo = p_ruta_archivo
    WHERE id_documento = p_id_documento;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE eliminarDocumentoAnexo(IN p_id_documento INT)
BEGIN
    DELETE FROM documento_anexo WHERE id_documento = p_id_documento;
END //
DELIMITER ;


-- == RECETA MEDICA ==
DELIMITER //
CREATE PROCEDURE insertarRecetaMedica(
    IN p_id_historia_clinica INT,
    IN p_medicamento TEXT,
    IN p_indicaciones TEXT
)
BEGIN
    INSERT INTO receta_medica (id_historia_clinica, medicamento, indicaciones)
    VALUES (p_id_historia_clinica, p_medicamento, p_indicaciones);
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE mostrarRecetasMedicas()
BEGIN
    SELECT * FROM receta_medica;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE actualizarRecetaMedica(
    IN p_id_receta INT,
    IN p_id_historia_clinica INT,
    IN p_medicamento TEXT,
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

DELIMITER //
CREATE PROCEDURE eliminarRecetaMedica(IN p_id_receta INT)
BEGIN
    DELETE FROM receta_medica WHERE id_receta = p_id_receta;
END //
DELIMITER ;

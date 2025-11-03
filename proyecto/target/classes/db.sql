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


select * from paciente;
select * from medico;
select * from usuario;
select * from cita;
select * from ejecucionCita;
select * from historia_clinica;
select * from documento_anexo;
select * from receta_medica;
select * from portafolio;
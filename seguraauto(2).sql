-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         10.4.32-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.11.0.7065
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para segura_tu_auto
CREATE DATABASE IF NOT EXISTS `segura_tu_auto` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `segura_tu_auto`;

-- Volcando estructura para tabla segura_tu_auto.agentes
CREATE TABLE IF NOT EXISTS `agentes` (
  `id_agente` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `codigo` varchar(20) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_modificacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id_agente`),
  UNIQUE KEY `codigo` (`codigo`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_agente_codigo` (`codigo`),
  KEY `idx_agente_email` (`email`),
  KEY `idx_agente_nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla segura_tu_auto.agentes: ~10 rows (aproximadamente)
INSERT INTO `agentes` (`id_agente`, `nombre`, `codigo`, `email`, `telefono`, `fecha_creacion`, `fecha_modificacion`) VALUES
	(11, 'Javier Jáquez Mota', 'AG0001', 'mariajose.carrera@hotmail.com', '910-209-629', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(12, 'Sr. Elena Nazario Verduzco', 'AG0002', 'elisa.verduzco@gmail.com', '976 439 593', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(13, 'Hernán Candelaria Mares', 'AG0003', 'jesus.munoz@hotmail.com', '909745181', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(14, 'Diana Arreola Rangel', 'AG0004', 'jose.romero@hotmail.com', '914-237-509', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(15, 'Lorenzo Almaraz Ochoa', 'AG0005', 'octavio.ortiz@hotmail.com', '900-329-837', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(16, 'Gustavo Llamas Carmona', 'AG0006', 'leonor.monroy@gmail.com', '999703330', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(17, 'Sra. María Eugenia Chapa Navarrete', 'AG0007', 'silvia.maya@hotmail.com', '958.535.736', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(18, 'Alejandro Medina Loya', 'AG0008', 'santiago.deleon@gmail.com', '919916558', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(19, 'Soledad Roque Garay', 'AG0009', 'irene.avalos@hotmail.com', '946.837.190', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(20, 'Sra. Laura Saldaña Mondragón', 'AG0010', 'rodrigo.cotto@hotmail.com', '949.929.045', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(21, 'Carlos Eduardo Silva', 'AG0011', 'carlos.silva@unique.com', '555-999-1122', '2025-07-28 06:37:59', '2025-07-28 06:37:59');

-- Volcando estructura para tabla segura_tu_auto.clientes
CREATE TABLE IF NOT EXISTS `clientes` (
  `id_cliente` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(150) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_modificacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_cliente_email` (`email`),
  KEY `idx_cliente_nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla segura_tu_auto.clientes: ~50 rows (aproximadamente)
INSERT INTO `clientes` (`id_cliente`, `nombre`, `email`, `telefono`, `fecha_creacion`, `fecha_modificacion`) VALUES
	(51, 'Manuel Botello Vaca', 'octavio.jaimes@gmail.com', '959-391-011', '2025-07-28 05:55:56', '2025-07-28 05:55:56'),
	(52, 'Salvador Villaseñor Elizondo', 'joaquin.guillen@yahoo.com', '931098568', '2025-07-28 05:55:56', '2025-07-28 05:55:56'),
	(53, 'Mariano Palacios Mesa', 'samuel.hurtado@gmail.com', '947 156 857', '2025-07-28 05:55:56', '2025-07-28 05:55:56'),
	(54, 'César Bernal Vaca', 'octavio.gastelum@yahoo.com', '938 715 575', '2025-07-28 05:55:56', '2025-07-28 05:55:56'),
	(55, 'Dolores Moya Mata', 'guadalupe.zapata@yahoo.com', '978-316-769', '2025-07-28 05:55:56', '2025-07-28 05:55:56'),
	(56, 'María Teresa Bahena Castañeda', 'elena.olmos@hotmail.com', '951 350 840', '2025-07-28 05:55:56', '2025-07-28 05:55:56'),
	(57, 'Lola Ruiz Luna', 'veronica.posada@yahoo.com', '927798989', '2025-07-28 05:55:56', '2025-07-28 05:55:56'),
	(58, 'Virginia Nieto Esparza', 'amalia.lopez@gmail.com', '979.885.558', '2025-07-28 05:55:56', '2025-07-28 05:55:56'),
	(59, 'Vicente Gamboa Llamas', 'pablo.llamas@hotmail.com', '941632528', '2025-07-28 05:55:56', '2025-07-28 05:55:56'),
	(60, 'Ramiro Villagómez Alcaraz', 'lilia.blanco@hotmail.com', '997.219.461', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(61, 'Miguel Trejo Escobedo', 'berta.zapata@hotmail.com', '931 196 775', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(62, 'Tomás Mireles Sedillo', 'fernando.barrera@yahoo.com', '996.600.386', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(63, 'Sta. Miguel Canales Noriega', 'claudia.cortez@hotmail.com', '901.117.820', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(64, 'Rosa Ocasio Canales', 'santiago.saldivar@yahoo.com', '909.650.181', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(65, 'Teodoro Santillán Alcántar', 'marilu.palomo@hotmail.com', '945.650.145', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(66, 'Joaquín Bonilla Rico', 'pablo.castaneda@gmail.com', '976 534 317', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(67, 'Ignacio Ledesma Solorio', 'ernesto.maldonado@gmail.com', '983-810-032', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(68, 'Bernardo Garibay Carmona', 'luismiguel.nino@hotmail.com', '932667560', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(69, 'Sra. Óscar Collado Medrano', 'natalia.segovia@hotmail.com', '983328056', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(70, 'Jerónimo Briones Quintanilla', 'miguelangel.cazares@gmail.com', '905.617.430', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(71, 'Irene Carvajal Araña', 'elisa.armas@gmail.com', '942.382.524', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(72, 'Pedro Solano Badillo', 'mariaelena.carrillo@yahoo.com', '965-401-367', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(73, 'Sta. Esteban Zelaya Pulido', 'rosario.palomino@yahoo.com', '966-811-524', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(74, 'Sra. Laura Sisneros Paredes', 'lorena.ozuna@hotmail.com', '929.736.414', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(75, 'Jorge Luis Arriaga Valle', 'teresa.longoria@yahoo.com', '911-494-132', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(76, 'Alejandro Canales Córdova', 'teresa.tello@hotmail.com', '978.186.703', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(77, 'Ángela Rivero Cardenas', 'sara.borrego@gmail.com', '937.989.009', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(78, 'María Eugenia Velasco Guajardo', 'gerardo.caraballo@yahoo.com', '991 377 122', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(79, 'Sta. Óscar Córdova Rubio', 'marcoantonio.torres@yahoo.com', '907-993-297', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(80, 'Carolina Garay Reyes', 'javier.munguia@yahoo.com', '959322670', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(81, 'Guillermo Villarreal Gaona', 'ester.armenta@gmail.com', '962-297-444', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(82, 'Cristian Alba Regalado', 'ana.leyva@gmail.com', '937.928.675', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(83, 'Benjamín Balderas Yáñez', 'jaime.pabon@yahoo.com', '985-199-545', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(84, 'Catalina Olivo Treviño', 'sonia.barajas@hotmail.com', '986.035.148', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(85, 'Sta. Olivia Santacruz Ramón', 'salvador.lugo@hotmail.com', '952 013 761', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(86, 'Diana Delgado Zarate', 'cristobal.villasenor@yahoo.com', '968.388.309', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(87, 'Sra. Débora Corrales Deleón', 'norma.melgar@gmail.com', '954230975', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(88, 'Berta Saiz Godoy', 'rocio.cepeda@gmail.com', '912-409-886', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(89, 'Sr. Alfredo Solorzano Herrera', 'leonor.banuelos@hotmail.com', '996472132', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(90, 'Armando Palomino Rosario', 'miguelangel.delarosa@yahoo.com', '949.272.025', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(91, 'Sr. Lucas Torres Cantú', 'francisco.arenas@gmail.com', '995.166.242', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(92, 'Sofia Briones Villalpando', 'diana.haro@hotmail.com', '943 359 340', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(93, 'Ana Huerta Olivo', 'angela.frias@hotmail.com', '915-194-484', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(94, 'Cristian Cabrera Almanza', 'angela.salgado@hotmail.com', '930-861-108', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(95, 'Alfredo Serna Torres', 'sofia.castillo@gmail.com', '925278774', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(96, 'César Ríos Hernández', 'analuisa.colon@gmail.com', '956.111.980', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(97, 'Ana Luisa Orta Griego', 'lucas.tamayo@yahoo.com', '953 083 448', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(98, 'Octavio Paredes Ceballos', 'pedro.deleon@yahoo.com', '975227606', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(99, 'Clara Irizarry Escalante', 'ignacio.espinosa@gmail.com', '923290076', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(100, 'Bernardo Zamora Hinojosa', 'lucas.dominquez@gmail.com', '968501339', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(101, 'Ana Patricia Lopez', 'ana.lopez@test.com', '555-777-9999', '2025-07-28 06:31:32', '2025-07-28 06:31:32');

-- Volcando estructura para tabla segura_tu_auto.descuentos
CREATE TABLE IF NOT EXISTS `descuentos` (
  `id_descuento` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `tipo` enum('PORCENTAJE','MONTO_FIJO') NOT NULL,
  `valor` decimal(10,2) NOT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id_descuento`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla segura_tu_auto.descuentos: ~10 rows (aproximadamente)
INSERT INTO `descuentos` (`id_descuento`, `nombre`, `tipo`, `valor`, `activo`, `created_at`) VALUES
	(11, 'Múltiples vehículos', 'PORCENTAJE', 9.00, 1, '2025-07-28 05:55:57'),
	(12, 'Múltiples vehículos', 'MONTO_FIJO', 230.00, 1, '2025-07-28 05:55:57'),
	(13, 'Cliente frecuente', 'MONTO_FIJO', 170.00, 1, '2025-07-28 05:55:57'),
	(14, 'Sin siniestros', 'PORCENTAJE', 5.00, 1, '2025-07-28 05:55:57'),
	(15, 'Tercera edad', 'PORCENTAJE', 23.00, 1, '2025-07-28 05:55:57'),
	(16, 'Tercera edad', 'PORCENTAJE', 26.00, 1, '2025-07-28 05:55:57'),
	(17, 'Múltiples vehículos', 'PORCENTAJE', 22.00, 1, '2025-07-28 05:55:57'),
	(18, 'Cliente frecuente', 'PORCENTAJE', 7.00, 1, '2025-07-28 05:55:57'),
	(19, 'Sin siniestros', 'MONTO_FIJO', 182.00, 1, '2025-07-28 05:55:57'),
	(20, 'Estudiante', 'PORCENTAJE', 26.00, 1, '2025-07-28 05:55:57');


-- Volcando datos para la tabla segura_tu_auto.documentos_reclamacion: ~0 rows (aproximadamente)

-- Volcando estructura para tabla segura_tu_auto.evaluadores
CREATE TABLE IF NOT EXISTS `evaluadores` (
  `id_evaluador` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `codigo` varchar(20) DEFAULT NULL,
  `email` varchar(150) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `especialidad` varchar(100) DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `fecha_ingreso` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id_evaluador`),
  UNIQUE KEY `codigo` (`codigo`),
  KEY `idx_evaluador_codigo` (`codigo`),
  KEY `idx_evaluador_email` (`email`),
  KEY `idx_evaluador_activo` (`activo`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla segura_tu_auto.evaluadores: ~8 rows (aproximadamente)
INSERT INTO `evaluadores` (`id_evaluador`, `nombre`, `codigo`, `email`, `telefono`, `especialidad`, `activo`, `fecha_ingreso`) VALUES
	(9, 'Guillermo Villaseñor Ulibarri', 'EV0001', 'ramon.delvalle@gmail.com', '943.956.975', 'Daños Vehiculares', 1, '2022-12-04 00:55:57'),
	(10, 'José Emilio Sedillo Roybal', 'EV0002', 'debora.sotelo@yahoo.com', '947.849.102', 'Responsabilidad Civil', 1, '2023-08-20 00:55:57'),
	(11, 'José Emilio Alonzo Olivárez', 'EV0003', 'mariadelcarmen.llamas@gmail.com', '922878756', 'Responsabilidad Civil', 1, '2025-06-02 00:55:57'),
	(12, 'Rubén Armas Nieto', 'EV0004', 'jesus.melendez@gmail.com', '918699823', 'Peritaje General', 1, '2024-09-03 00:55:57'),
	(13, 'Alejandro Gutiérrez Lemus', 'EV0005', 'pedro.rivero@yahoo.com', '971 579 582', 'Daños Vehiculares', 1, '2024-02-15 00:55:57'),
	(14, 'Luz Ortiz Casas', 'EV0006', 'diego.carrera@hotmail.com', '990 437 403', 'Daños Vehiculares', 1, '2022-11-19 00:55:57'),
	(15, 'Rubén Guevara Orozco', 'EV0007', 'luismiguel.pena@yahoo.com', '935-945-913', 'Daños Vehiculares', 1, '2023-04-17 00:55:57'),
	(16, 'Estela Cotto Trejo', 'EV0008', 'florencia.sosa@yahoo.com', '958201330', 'Daños Vehiculares', 1, '2022-11-22 00:55:57');

-- Volcando estructura para tabla segura_tu_auto.factores_riesgo
CREATE TABLE IF NOT EXISTS `factores_riesgo` (
  `id_factor` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `multiplicador` decimal(5,3) NOT NULL,
  `activo` tinyint(1) DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id_factor`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla segura_tu_auto.factores_riesgo: ~15 rows (aproximadamente)
INSERT INTO `factores_riesgo` (`id_factor`, `nombre`, `descripcion`, `multiplicador`, `activo`, `created_at`) VALUES
	(16, 'Edad del conductor 1', 'Sed consequatur quis est nulla.', 0.768, 1, '2025-07-28 05:55:57'),
	(17, 'Tipo de uso 2', 'Corrupti eos est nostrum quisquam et est dolores.', 0.860, 1, '2025-07-28 05:55:57'),
	(18, 'Zona de circulación 3', 'Similique adipisci nostrum odit et soluta voluptas architecto.', 1.906, 1, '2025-07-28 05:55:57'),
	(19, 'Historial de accidentes 4', 'Quis dignissimos rerum ad et.', 0.933, 1, '2025-07-28 05:55:57'),
	(20, 'Edad del conductor 5', 'Distinctio a alias iste enim voluptas non sit.', 1.076, 1, '2025-07-28 05:55:57'),
	(21, 'Historial de accidentes 6', 'Quo reprehenderit quasi.', 1.102, 1, '2025-07-28 05:55:57'),
	(22, 'Zona de circulación 7', 'Quia voluptates mollitia cumque dolorem in quibusdam.', 0.876, 1, '2025-07-28 05:55:57'),
	(23, 'Antigüedad del vehículo 8', 'Magni dolore omnis molestiae at consequatur dolorum.', 0.527, 1, '2025-07-28 05:55:57'),
	(24, 'Tipo de uso 9', 'Quis provident atque ratione qui doloribus autem quibusdam.', 1.193, 1, '2025-07-28 05:55:57'),
	(25, 'Zona de circulación 10', 'Ipsa et autem non itaque.', 1.004, 1, '2025-07-28 05:55:57'),
	(26, 'Edad del conductor 11', 'Et voluptatem alias ab.', 1.931, 1, '2025-07-28 05:55:57'),
	(27, 'Tipo de uso 12', 'Doloremque at quis in commodi.', 0.692, 1, '2025-07-28 05:55:57'),
	(28, 'Edad del conductor 13', 'Repudiandae eaque rem delectus esse.', 0.546, 1, '2025-07-28 05:55:57'),
	(29, 'Antigüedad del vehículo 14', 'Laboriosam excepturi eius eum nihil quisquam ut.', 1.651, 1, '2025-07-28 05:55:57'),
	(30, 'Edad del conductor 15', 'Repellendus ad omnis sunt reiciendis porro nam.', 1.367, 1, '2025-07-28 05:55:57');

-- Volcando estructura para tabla segura_tu_auto.polizas
CREATE TABLE IF NOT EXISTS `polizas` (
  `id_poliza` int(11) NOT NULL AUTO_INCREMENT,
  `numero_poliza` varchar(50) DEFAULT NULL,
  `fecha_emision` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_vencimiento` timestamp NULL DEFAULT NULL,
  `estado` enum('APROBADA','CANCELADA','PENDIENTE','RECHAZADA') NOT NULL,
  `cliente_id` int(11) NOT NULL,
  `agente_id` int(11) NOT NULL,
  `prima` decimal(10,2) DEFAULT NULL CHECK (`prima` >= 0),
  `tipo_seguro` varchar(100) DEFAULT NULL,
  `observaciones` text DEFAULT NULL,
  `fecha_creacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_modificacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id_poliza`),
  UNIQUE KEY `numero_poliza` (`numero_poliza`),
  KEY `cliente_id` (`cliente_id`),
  KEY `agente_id` (`agente_id`),
  KEY `estado` (`estado`),
  KEY `fecha_emision` (`fecha_emision`),
  KEY `tipo_seguro` (`tipo_seguro`),
  CONSTRAINT `polizas_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `clientes` (`id_cliente`) ON UPDATE CASCADE,
  CONSTRAINT `polizas_ibfk_2` FOREIGN KEY (`agente_id`) REFERENCES `agentes` (`id_agente`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla segura_tu_auto.polizas: ~100 rows (aproximadamente)
INSERT INTO `polizas` (`id_poliza`, `numero_poliza`, `fecha_emision`, `fecha_vencimiento`, `estado`, `cliente_id`, `agente_id`, `prima`, `tipo_seguro`, `observaciones`, `fecha_creacion`, `fecha_modificacion`) VALUES
	(101, 'POL000001', '2025-02-08 05:55:57', '2026-02-08 05:55:57', 'CANCELADA', 63, 11, 888.17, 'Terceros', 'Repudiandae ab et quis aspernatur.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(102, 'POL000002', '2025-02-15 05:55:57', '2026-02-15 05:55:57', 'APROBADA', 79, 15, 532.03, 'Responsabilidad Civil', 'Quisquam enim id velit aut aperiam ut.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(103, 'POL000003', '2025-06-30 05:55:57', '2026-06-30 05:55:57', 'APROBADA', 90, 16, 951.12, 'Todo Riesgo', 'Laboriosam et laboriosam.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(104, 'POL000004', '2025-07-20 05:55:57', '2026-07-20 05:55:57', 'RECHAZADA', 92, 16, 905.25, 'Terceros', 'Iusto sint facilis praesentium ipsa.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(105, 'POL000005', '2024-07-31 05:55:57', '2025-07-31 05:55:57', 'CANCELADA', 55, 17, 326.85, 'Responsabilidad Civil', 'Temporibus qui voluptas tempore atque quae sed.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(106, 'POL000006', '2024-08-05 05:55:57', '2025-08-05 05:55:57', 'RECHAZADA', 93, 11, 308.17, 'Todo Riesgo', 'Et ipsam est quia.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(107, 'POL000007', '2025-01-15 05:55:57', '2026-01-15 05:55:57', 'APROBADA', 92, 20, 258.94, 'Robo y Hurto', 'Aut ratione laudantium quasi.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(108, 'POL000008', '2024-09-24 05:55:57', '2025-09-24 05:55:57', 'PENDIENTE', 78, 19, 198.10, 'Terceros', 'In ut ut incidunt nesciunt eos accusantium.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(109, 'POL000009', '2024-10-24 05:55:57', '2025-10-24 05:55:57', 'PENDIENTE', 62, 18, 779.80, 'Robo y Hurto', 'Eaque itaque repudiandae dolorum dolores perspiciatis consequatur quibusdam.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(110, 'POL000010', '2025-02-10 05:55:57', '2026-02-10 05:55:57', 'RECHAZADA', 96, 11, 758.45, 'Terceros', 'Voluptate odio eum quia.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(111, 'POL000011', '2025-04-14 05:55:57', '2026-04-14 05:55:57', 'RECHAZADA', 58, 15, 965.49, 'Terceros', 'Quo nihil doloribus distinctio consectetur.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(112, 'POL000012', '2025-02-12 05:55:57', '2026-02-12 05:55:57', 'APROBADA', 94, 12, 415.59, 'Terceros', 'Consequatur corrupti ullam ullam reiciendis doloremque qui illo.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(113, 'POL000013', '2025-01-17 05:55:57', '2026-01-17 05:55:57', 'RECHAZADA', 71, 14, 169.98, 'Responsabilidad Civil', 'Et voluptatem et rerum ex ducimus unde.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(114, 'POL000014', '2024-10-05 05:55:57', '2025-10-05 05:55:57', 'CANCELADA', 59, 18, 436.21, 'Robo y Hurto', 'Nihil voluptas quam illo reprehenderit.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(115, 'POL000015', '2024-08-25 05:55:57', '2025-08-25 05:55:57', 'CANCELADA', 70, 15, 536.72, 'Robo y Hurto', 'Qui nemo nihil ullam dolor.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(116, 'POL000016', '2025-01-14 05:55:57', '2026-01-14 05:55:57', 'CANCELADA', 77, 11, 126.66, 'Robo y Hurto', 'Repellat aut cum culpa.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(117, 'POL000017', '2025-04-29 05:55:57', '2026-04-29 05:55:57', 'RECHAZADA', 93, 15, 798.71, 'Terceros', 'Esse consequatur cum sit aut laudantium.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(118, 'POL000018', '2025-02-27 05:55:57', '2026-02-27 05:55:57', 'RECHAZADA', 76, 14, 901.11, 'Responsabilidad Civil', 'Omnis inventore recusandae qui deleniti rerum quod.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(119, 'POL000019', '2025-05-02 05:55:57', '2026-05-02 05:55:57', 'CANCELADA', 52, 16, 655.59, 'Terceros', 'Et provident nihil et.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(120, 'POL000020', '2025-01-23 05:55:57', '2026-01-23 05:55:57', 'CANCELADA', 55, 14, 945.76, 'Todo Riesgo', 'Vero debitis nisi voluptatum quia.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(121, 'POL000021', '2024-08-13 05:55:57', '2025-08-13 05:55:57', 'RECHAZADA', 84, 14, 864.43, 'Responsabilidad Civil', 'Sunt excepturi fugit.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(122, 'POL000022', '2024-10-06 05:55:57', '2025-10-06 05:55:57', 'APROBADA', 97, 19, 744.19, 'Robo y Hurto', 'Cumque enim ipsam.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(123, 'POL000023', '2025-06-06 05:55:57', '2026-06-06 05:55:57', 'APROBADA', 93, 15, 986.14, 'Responsabilidad Civil', 'Sit ea dolorem culpa aliquid.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(124, 'POL000024', '2025-04-12 05:55:57', '2026-04-12 05:55:57', 'APROBADA', 72, 17, 450.63, 'Todo Riesgo', 'Ipsam impedit ea.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(125, 'POL000025', '2024-12-25 05:55:57', '2025-12-25 05:55:57', 'CANCELADA', 98, 15, 921.79, 'Robo y Hurto', 'Quia animi voluptas.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(126, 'POL000026', '2025-04-09 05:55:57', '2026-04-09 05:55:57', 'APROBADA', 78, 12, 599.94, 'Responsabilidad Civil', 'Et maxime veritatis pariatur.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(127, 'POL000027', '2024-11-04 05:55:57', '2025-11-04 05:55:57', 'CANCELADA', 61, 14, 353.21, 'Terceros', 'Quaerat perspiciatis aperiam optio distinctio ab.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(128, 'POL000028', '2025-03-24 05:55:57', '2026-03-24 05:55:57', 'APROBADA', 67, 17, 784.26, 'Responsabilidad Civil', 'Reprehenderit illum quos.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(129, 'POL000029', '2025-03-10 05:55:57', '2026-03-10 05:55:57', 'RECHAZADA', 51, 12, 751.59, 'Responsabilidad Civil', 'Est qui magnam temporibus esse.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(130, 'POL000030', '2024-10-27 05:55:57', '2025-10-27 05:55:57', 'RECHAZADA', 78, 19, 549.82, 'Terceros', 'Quos molestiae dolorum et dolor molestiae qui voluptatem.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(131, 'POL000031', '2025-01-31 05:55:57', '2026-01-31 05:55:57', 'RECHAZADA', 83, 13, 660.13, 'Terceros', 'Voluptate voluptas maxime impedit voluptatem earum.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(132, 'POL000032', '2025-06-06 05:55:57', '2026-06-06 05:55:57', 'PENDIENTE', 90, 20, 138.46, 'Robo y Hurto', 'Magni magnam facilis repellat laboriosam rerum laboriosam omnis.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(133, 'POL000033', '2025-01-04 05:55:57', '2026-01-04 05:55:57', 'PENDIENTE', 96, 16, 364.86, 'Responsabilidad Civil', 'Qui a itaque eum eius.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(134, 'POL000034', '2025-05-11 05:55:57', '2026-05-11 05:55:57', 'RECHAZADA', 52, 17, 373.58, 'Robo y Hurto', 'Soluta reiciendis autem.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(135, 'POL000035', '2025-03-10 05:55:57', '2026-03-10 05:55:57', 'PENDIENTE', 82, 13, 923.04, 'Responsabilidad Civil', 'Velit quia omnis molestias.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(136, 'POL000036', '2024-11-18 05:55:57', '2025-11-18 05:55:57', 'CANCELADA', 84, 14, 518.73, 'Todo Riesgo', 'Accusamus repellendus repellendus et.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(137, 'POL000037', '2024-09-10 05:55:57', '2025-09-10 05:55:57', 'RECHAZADA', 94, 11, 202.97, 'Todo Riesgo', 'Minus repudiandae eos mollitia.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(138, 'POL000038', '2024-12-25 05:55:57', '2025-12-25 05:55:57', 'APROBADA', 52, 20, 609.16, 'Todo Riesgo', 'Et similique maiores quis vel.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(139, 'POL000039', '2025-07-06 05:55:57', '2026-07-06 05:55:57', 'RECHAZADA', 60, 14, 394.14, 'Terceros', 'Provident ad et excepturi et aliquid.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(140, 'POL000040', '2025-07-03 05:55:57', '2026-07-03 05:55:57', 'APROBADA', 51, 16, 539.74, 'Todo Riesgo', 'Maxime velit eum non sunt dolor praesentium quis.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(141, 'POL000041', '2025-01-03 05:55:57', '2026-01-03 05:55:57', 'PENDIENTE', 67, 17, 503.82, 'Todo Riesgo', 'Ut eaque laboriosam quos.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(142, 'POL000042', '2024-10-24 05:55:57', '2025-10-24 05:55:57', 'APROBADA', 62, 14, 832.42, 'Responsabilidad Civil', 'Sed in tempora sequi ipsum dicta modi exercitationem.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(143, 'POL000043', '2025-05-04 05:55:57', '2026-05-04 05:55:57', 'APROBADA', 59, 12, 953.67, 'Robo y Hurto', 'Rem consectetur sed nemo accusamus consequatur amet.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(144, 'POL000044', '2025-05-09 05:55:57', '2026-05-09 05:55:57', 'PENDIENTE', 87, 12, 169.92, 'Todo Riesgo', 'Quasi quas et atque incidunt fugiat asperiores saepe.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(145, 'POL000045', '2025-01-18 05:55:57', '2026-01-18 05:55:57', 'APROBADA', 97, 16, 599.00, 'Terceros', 'Consequatur repellendus ut quo.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(146, 'POL000046', '2025-01-07 05:55:57', '2026-01-07 05:55:57', 'PENDIENTE', 58, 19, 402.32, 'Terceros', 'Qui accusamus aut et beatae et.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(147, 'POL000047', '2024-12-22 05:55:57', '2025-12-22 05:55:57', 'RECHAZADA', 93, 18, 125.73, 'Terceros', 'Rerum nemo animi quos eaque at omnis reiciendis.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(148, 'POL000048', '2025-03-01 05:55:57', '2026-03-01 05:55:57', 'RECHAZADA', 85, 11, 524.86, 'Terceros', 'Quia hic debitis corrupti dolorem ipsum omnis est.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(149, 'POL000049', '2025-02-24 05:55:57', '2026-02-24 05:55:57', 'APROBADA', 89, 11, 547.12, 'Todo Riesgo', 'Aut sapiente temporibus.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(150, 'POL000050', '2024-12-17 05:55:57', '2025-12-17 05:55:57', 'APROBADA', 95, 13, 395.02, 'Terceros', 'Ea et nihil.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(151, 'POL000051', '2024-09-25 05:55:57', '2025-09-25 05:55:57', 'APROBADA', 75, 20, 928.85, 'Responsabilidad Civil', 'Itaque dolore eligendi autem vero laudantium.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(152, 'POL000052', '2024-10-22 05:55:57', '2025-10-22 05:55:57', 'RECHAZADA', 99, 15, 340.77, 'Terceros', 'Natus optio ea aspernatur dignissimos ratione aut.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(153, 'POL000053', '2025-05-11 05:55:57', '2026-05-11 05:55:57', 'APROBADA', 93, 20, 917.07, 'Responsabilidad Civil', 'Est voluptas nisi ut asperiores ut sit quia.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(154, 'POL000054', '2024-11-22 05:55:57', '2025-11-22 05:55:57', 'CANCELADA', 56, 13, 587.44, 'Responsabilidad Civil', 'Autem est non delectus aut.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(155, 'POL000055', '2025-05-04 05:55:57', '2026-05-04 05:55:57', 'RECHAZADA', 80, 14, 499.46, 'Responsabilidad Civil', 'Veniam alias necessitatibus nulla voluptatem voluptates quibusdam iste.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(156, 'POL000056', '2024-09-12 05:55:57', '2025-09-12 05:55:57', 'RECHAZADA', 58, 16, 533.71, 'Terceros', 'Incidunt velit saepe eum dolorum quis et.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(157, 'POL000057', '2025-04-21 05:55:57', '2026-04-21 05:55:57', 'APROBADA', 51, 19, 394.44, 'Terceros', 'Aut ex perferendis fugiat molestiae dolorem asperiores optio.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(158, 'POL000058', '2025-02-23 05:55:57', '2026-02-23 05:55:57', 'RECHAZADA', 99, 15, 326.67, 'Responsabilidad Civil', 'Maiores ut blanditiis.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(159, 'POL000059', '2024-10-02 05:55:57', '2025-10-02 05:55:57', 'PENDIENTE', 62, 19, 895.00, 'Terceros', 'Nemo reiciendis accusamus nemo et.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(160, 'POL000060', '2025-07-22 05:55:57', '2026-07-22 05:55:57', 'CANCELADA', 77, 13, 882.89, 'Robo y Hurto', 'Repudiandae sapiente omnis corporis sit.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(161, 'POL000061', '2024-08-11 05:55:57', '2025-08-11 05:55:57', 'CANCELADA', 76, 11, 689.88, 'Todo Riesgo', 'Laudantium repudiandae quis.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(162, 'POL000062', '2024-09-30 05:55:57', '2025-09-30 05:55:57', 'PENDIENTE', 56, 12, 808.91, 'Responsabilidad Civil', 'Nihil omnis non eum commodi aut.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(163, 'POL000063', '2024-10-20 05:55:57', '2025-10-20 05:55:57', 'APROBADA', 89, 15, 462.04, 'Responsabilidad Civil', 'Animi non qui praesentium.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(164, 'POL000064', '2024-09-08 05:55:57', '2025-09-08 05:55:57', 'CANCELADA', 70, 18, 409.76, 'Responsabilidad Civil', 'Qui amet sequi magni ea reiciendis.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(165, 'POL000065', '2024-12-20 05:55:57', '2025-12-20 05:55:57', 'PENDIENTE', 69, 20, 282.64, 'Todo Riesgo', 'Maiores enim sed.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(166, 'POL000066', '2024-09-27 05:55:57', '2025-09-27 05:55:57', 'RECHAZADA', 78, 16, 285.24, 'Robo y Hurto', 'Sit labore quasi pariatur illo cum.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(167, 'POL000067', '2025-05-21 05:55:57', '2026-05-21 05:55:57', 'CANCELADA', 76, 20, 249.96, 'Robo y Hurto', 'Voluptas vero tempora.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(168, 'POL000068', '2025-01-31 05:55:57', '2026-01-31 05:55:57', 'APROBADA', 59, 15, 621.18, 'Responsabilidad Civil', 'Qui quis similique vel ad doloremque.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(169, 'POL000069', '2025-07-05 05:55:57', '2026-07-05 05:55:57', 'PENDIENTE', 90, 16, 657.00, 'Robo y Hurto', 'Tenetur est tempora molestiae sunt quos et.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(170, 'POL000070', '2025-04-11 05:55:57', '2026-04-11 05:55:57', 'PENDIENTE', 82, 19, 941.86, 'Robo y Hurto', 'Tenetur odit totam mollitia et earum.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(171, 'POL000071', '2025-05-28 05:55:57', '2026-05-28 05:55:57', 'APROBADA', 71, 14, 985.69, 'Terceros', 'Officiis dolorem consectetur maiores natus nemo.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(172, 'POL000072', '2024-10-10 05:55:57', '2025-10-10 05:55:57', 'APROBADA', 52, 12, 167.33, 'Robo y Hurto', 'Eos aliquid accusamus voluptatem officia perferendis perferendis laboriosam.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(173, 'POL000073', '2025-04-15 05:55:57', '2026-04-15 05:55:57', 'RECHAZADA', 65, 12, 518.95, 'Todo Riesgo', 'Aut velit enim dolorem earum.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(174, 'POL000074', '2025-06-04 05:55:57', '2026-06-04 05:55:57', 'RECHAZADA', 98, 20, 132.28, 'Todo Riesgo', 'Sed voluptates nihil quaerat sunt a beatae dolorum.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(175, 'POL000075', '2024-11-26 05:55:57', '2025-11-26 05:55:57', 'CANCELADA', 70, 16, 536.99, 'Todo Riesgo', 'Quasi iusto modi vero.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(176, 'POL000076', '2025-01-07 05:55:57', '2026-01-07 05:55:57', 'RECHAZADA', 74, 13, 837.76, 'Robo y Hurto', 'Adipisci hic et.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(177, 'POL000077', '2025-01-25 05:55:57', '2026-01-25 05:55:57', 'PENDIENTE', 99, 16, 857.67, 'Responsabilidad Civil', 'Blanditiis aperiam repellat voluptas est error aut.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(178, 'POL000078', '2024-12-24 05:55:57', '2025-12-24 05:55:57', 'APROBADA', 72, 19, 858.86, 'Terceros', 'Voluptas hic voluptatibus vitae vitae iusto eius cupiditate.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(179, 'POL000079', '2025-07-03 05:55:57', '2026-07-03 05:55:57', 'APROBADA', 52, 12, 636.77, 'Robo y Hurto', 'Dolore vel quia quia nisi distinctio aliquam.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(180, 'POL000080', '2024-08-07 05:55:57', '2025-08-07 05:55:57', 'RECHAZADA', 84, 19, 892.13, 'Responsabilidad Civil', 'Recusandae sapiente ratione.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(181, 'POL000081', '2025-05-26 05:55:57', '2026-05-26 05:55:57', 'PENDIENTE', 65, 20, 650.39, 'Terceros', 'Ullam temporibus quaerat repellat facere.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(182, 'POL000082', '2025-05-01 05:55:57', '2026-05-01 05:55:57', 'CANCELADA', 56, 16, 247.38, 'Robo y Hurto', 'Nihil quo vitae eum ducimus unde.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(183, 'POL000083', '2025-02-05 05:55:57', '2026-02-05 05:55:57', 'APROBADA', 54, 20, 359.01, 'Robo y Hurto', 'In voluptates quas.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(184, 'POL000084', '2024-08-30 05:55:57', '2025-08-30 05:55:57', 'CANCELADA', 66, 19, 324.13, 'Terceros', 'Et voluptatem sapiente veniam sunt aliquid consectetur.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(185, 'POL000085', '2025-06-30 05:55:57', '2026-06-30 05:55:57', 'CANCELADA', 51, 17, 601.89, 'Robo y Hurto', 'Quis sed laboriosam aut adipisci recusandae.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(186, 'POL000086', '2024-12-12 05:55:57', '2025-12-12 05:55:57', 'PENDIENTE', 63, 17, 611.91, 'Responsabilidad Civil', 'Quam quae at iste.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(187, 'POL000087', '2025-03-21 05:55:57', '2026-03-21 05:55:57', 'PENDIENTE', 86, 13, 739.67, 'Todo Riesgo', 'Eligendi itaque molestiae.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(188, 'POL000088', '2025-06-18 05:55:57', '2026-06-18 05:55:57', 'RECHAZADA', 80, 18, 849.05, 'Todo Riesgo', 'Ea voluptatem ab enim sint aut eius assumenda.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(189, 'POL000089', '2024-09-23 05:55:57', '2025-09-23 05:55:57', 'RECHAZADA', 97, 17, 266.51, 'Todo Riesgo', 'Est ipsam laudantium soluta et libero harum.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(190, 'POL000090', '2025-01-17 05:55:57', '2026-01-17 05:55:57', 'CANCELADA', 64, 11, 574.75, 'Terceros', 'Quos consequatur possimus expedita.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(191, 'POL000091', '2024-10-11 05:55:57', '2025-10-11 05:55:57', 'RECHAZADA', 100, 13, 354.17, 'Todo Riesgo', 'Perferendis aut animi.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(192, 'POL000092', '2024-09-08 05:55:57', '2025-09-08 05:55:57', 'RECHAZADA', 80, 11, 843.58, 'Robo y Hurto', 'Rerum quidem tempore eos deleniti.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(193, 'POL000093', '2025-05-31 05:55:57', '2026-05-31 05:55:57', 'CANCELADA', 76, 20, 733.73, 'Terceros', 'Ab explicabo atque voluptates.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(194, 'POL000094', '2024-11-08 05:55:57', '2025-11-08 05:55:57', 'CANCELADA', 83, 14, 568.66, 'Todo Riesgo', 'Placeat eius in laborum odit aliquid molestiae delectus.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(195, 'POL000095', '2025-06-06 05:55:57', '2026-06-06 05:55:57', 'APROBADA', 75, 15, 154.93, 'Terceros', 'Qui doloremque nemo odit consequatur.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(196, 'POL000096', '2024-12-28 05:55:57', '2025-12-28 05:55:57', 'PENDIENTE', 77, 11, 894.20, 'Terceros', 'Ipsam occaecati commodi.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(197, 'POL000097', '2025-01-25 05:55:57', '2026-01-25 05:55:57', 'PENDIENTE', 68, 15, 110.29, 'Robo y Hurto', 'Odio sed debitis eos odio sunt dolorem tempora.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(198, 'POL000098', '2025-07-16 05:55:57', '2026-07-16 05:55:57', 'CANCELADA', 96, 12, 258.10, 'Robo y Hurto', 'Minus beatae doloremque eveniet.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(199, 'POL000099', '2025-03-07 05:55:57', '2026-03-07 05:55:57', 'RECHAZADA', 57, 19, 481.50, 'Todo Riesgo', 'Et officia tempora molestiae pariatur sed velit quis.', '2025-07-28 05:55:57', '2025-07-28 05:55:57'),
	(200, 'POL000100', '2025-05-29 05:55:57', '2026-05-29 05:55:57', 'APROBADA', 52, 19, 907.84, 'Robo y Hurto', 'Unde accusantium ab corporis praesentium quae.', '2025-07-28 05:55:57', '2025-07-28 05:55:57');

-- Volcando estructura para tabla segura_tu_auto.reclamaciones
CREATE TABLE IF NOT EXISTS `reclamaciones` (
  `id_reclamacion` int(11) NOT NULL AUTO_INCREMENT,
  `poliza_id` int(11) NOT NULL,
  `numero_reclamacion` varchar(50) NOT NULL,
  `descripcion` text NOT NULL,
  `monto_reclamado` decimal(12,2) NOT NULL,
  `monto_aprobado` decimal(12,2) DEFAULT NULL,
  `estado` enum('REGISTRADA','EN_EVALUACION','APROBADA','RECHAZADA','PAGADA') DEFAULT 'REGISTRADA',
  `fecha_reclamacion` timestamp NOT NULL DEFAULT current_timestamp(),
  `fecha_evaluacion` timestamp NULL DEFAULT NULL,
  `fecha_resolucion` timestamp NULL DEFAULT NULL,
  `observaciones` text DEFAULT NULL,
  `evaluador_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id_reclamacion`),
  UNIQUE KEY `numero_reclamacion` (`numero_reclamacion`),
  KEY `poliza_id` (`poliza_id`),
  KEY `evaluador_id` (`evaluador_id`),
  CONSTRAINT `reclamaciones_ibfk_1` FOREIGN KEY (`poliza_id`) REFERENCES `polizas` (`id_poliza`),
  CONSTRAINT `reclamaciones_ibfk_2` FOREIGN KEY (`evaluador_id`) REFERENCES `evaluadores` (`id_evaluador`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla segura_tu_auto.reclamaciones: ~30 rows (aproximadamente)
INSERT INTO `reclamaciones` (`id_reclamacion`, `poliza_id`, `numero_reclamacion`, `descripcion`, `monto_reclamado`, `monto_aprobado`, `estado`, `fecha_reclamacion`, `fecha_evaluacion`, `fecha_resolucion`, `observaciones`, `evaluador_id`) VALUES
	(31, 189, 'REC000001', 'Reprehenderit voluptatem debitis totam perferendis nisi quia. Est rerum voluptatem mollitia quo enim. Recusandae omnis quo adipisci. In voluptas ut nihil unde libero doloremque recusandae. Qui aut reprehenderit rerum et.', 4067.86, 3906.15, 'EN_EVALUACION', '2025-05-14 05:55:57', '2025-05-21 05:55:57', NULL, 'Sit animi dolores accusantium est beatae quo ab.', 10),
	(32, 170, 'REC000002', 'Suscipit alias quia et. Et provident ut eos amet porro aut. Et sed ut explicabo eum ex quod fuga.', 853.46, 810.50, 'APROBADA', '2025-05-14 05:55:57', '2025-05-15 05:55:57', '2025-05-21 05:55:57', 'Sit est sapiente nostrum.', 14),
	(33, 141, 'REC000003', 'Inventore ullam quas consequuntur vel. Nihil totam illo ut maxime minus amet ut. Quis rerum ut aliquid perferendis. Harum hic fugit. Quasi quam repudiandae adipisci sit.', 2327.53, 2021.24, 'REGISTRADA', '2025-05-11 05:55:57', NULL, NULL, 'Labore ducimus fugiat rem nemo voluptas.', NULL),
	(34, 101, 'REC000004', 'Cupiditate quae sint. Omnis modi dolorem earum occaecati quia non. Aliquam mollitia unde qui praesentium et ea. Quibusdam impedit voluptatem fuga. Nam provident rem dolorem.', 2464.28, 1912.67, 'EN_EVALUACION', '2025-05-12 05:55:57', '2025-05-14 05:55:57', NULL, 'Dolor consequatur ut.', 15),
	(35, 162, 'REC000005', 'Fugit itaque qui distinctio. Recusandae quibusdam est sint. Et eius est. Autem et deserunt aut debitis enim.', 757.08, 566.85, 'RECHAZADA', '2025-05-06 05:55:57', '2025-05-08 05:55:57', '2025-05-23 05:55:57', 'In recusandae ut occaecati minus et et.', 9),
	(36, 197, 'REC000006', 'Enim porro harum magni corrupti ullam debitis illo. Id qui doloremque commodi. Vel soluta dolor voluptate. Aut incidunt repellendus velit.', 4345.35, 3209.68, 'APROBADA', '2025-06-02 05:55:57', '2025-06-08 05:55:57', '2025-06-09 05:55:57', 'Eos similique itaque est.', 12),
	(37, 141, 'REC000007', 'Repellat quia voluptatem qui. Nesciunt consequatur voluptates amet. Nesciunt atque itaque. Aspernatur possimus possimus rerum. Ea voluptatibus veritatis.', 2897.51, 2751.60, 'APROBADA', '2025-05-13 05:55:57', '2025-05-20 05:55:57', '2025-05-26 05:55:57', 'Dolore consequatur aut dolores cum ut quo eaque.', 10),
	(38, 167, 'REC000008', 'Enim molestiae et facere. Maxime fuga veniam. Consequatur nihil ut quo. Dolore quae quia.', 508.52, 370.67, 'APROBADA', '2025-07-06 05:55:57', '2025-07-16 05:55:57', '2025-07-23 05:55:57', 'Et eaque dolores iure nesciunt sit.', 10),
	(39, 104, 'REC000009', 'Sunt repudiandae alias id et aut quam. Repudiandae autem numquam possimus. Explicabo voluptas veniam accusantium facilis accusamus ullam in. Ea optio quia corrupti alias. Delectus quia dolorum.', 2209.20, 1921.51, 'APROBADA', '2025-07-15 05:55:57', '2025-07-25 05:55:57', '2025-08-01 05:55:57', 'Dignissimos voluptatibus voluptatem commodi dolorum rem ut.', 15),
	(40, 163, 'REC000010', 'Molestiae illum molestiae. Aut necessitatibus quod. Perferendis magni et natus ipsam.', 4055.39, 3123.94, 'RECHAZADA', '2025-07-13 05:55:57', '2025-07-23 05:55:57', '2025-07-28 05:55:57', 'Eum cupiditate commodi odit quaerat corrupti quia labore.', 16),
	(41, 187, 'REC000011', 'Et quo id nemo. Et nemo veniam. Fuga tempora quo repudiandae odit earum repellat. Distinctio a sunt atque consectetur sit. Voluptas magnam eveniet quae ea.', 4301.74, 3566.71, 'RECHAZADA', '2025-07-16 05:55:57', '2025-07-25 05:55:57', '2025-07-28 05:55:57', 'In sunt sunt aperiam.', 11),
	(42, 126, 'REC000012', 'Est cupiditate aut. Amet corporis ad asperiores provident. Sunt cum id quia. Ad vero dignissimos aut officiis aut corrupti. Consequatur aliquam possimus assumenda occaecati saepe id ut.', 723.22, 566.94, 'REGISTRADA', '2025-07-23 05:55:57', NULL, NULL, 'Perferendis optio quia vitae.', NULL),
	(43, 163, 'REC000013', 'Cupiditate iusto mollitia sit enim. Sint vel odit unde occaecati. Maxime alias suscipit.', 4979.75, 3521.19, 'APROBADA', '2025-06-06 05:55:57', '2025-06-10 05:55:57', '2025-06-17 05:55:57', 'Odio non aut repudiandae velit impedit enim natus.', 10),
	(44, 145, 'REC000014', 'Consequuntur rerum non dolorum sunt quia nemo debitis. Itaque possimus aliquid odit id assumenda cumque modi. Ipsum non omnis amet. Et est nulla voluptatum ad in.', 2807.75, 2767.36, 'APROBADA', '2025-07-22 05:55:57', '2025-07-31 05:55:57', '2025-08-12 05:55:57', 'Aliquam sed rerum quaerat aut suscipit dolore.', 15),
	(45, 133, 'REC000015', 'Et omnis consectetur suscipit laboriosam iste. Sit consequatur laudantium expedita minima. Incidunt accusamus ducimus error fugiat.', 3106.12, 2802.07, 'PAGADA', '2025-05-29 05:55:57', '2025-06-01 05:55:57', '2025-06-06 05:55:57', 'Et qui temporibus iure eaque facere.', 12),
	(46, 138, 'REC000016', 'Delectus qui accusantium voluptas. Ipsum accusantium voluptatem est est atque. Quidem sed asperiores ipsa et laudantium dolores. Alias accusamus harum deleniti eius ea ut repellat. Autem omnis natus atque.', 1865.10, 1336.62, 'RECHAZADA', '2025-06-02 05:55:57', '2025-06-10 05:55:57', '2025-06-12 05:55:57', 'Minus ut assumenda impedit aut sapiente quos id.', 16),
	(47, 104, 'REC000017', 'Accusamus tenetur incidunt enim eius animi. Quo aspernatur et earum quo laborum. Voluptas dicta eaque excepturi provident molestias dolor.', 4628.01, 3363.18, 'REGISTRADA', '2025-05-21 05:55:57', NULL, NULL, 'Impedit totam occaecati ab et.', NULL),
	(48, 158, 'REC000018', 'Consequatur neque saepe quaerat quod. Laboriosam ratione aspernatur atque quo reiciendis magnam assumenda. Amet ratione sint asperiores et ipsa. Ipsum voluptas aut.', 1045.65, 943.89, 'EN_EVALUACION', '2025-07-16 05:55:57', '2025-07-18 05:55:57', NULL, 'Voluptatem minima itaque nostrum minus voluptatem.', 16),
	(49, 164, 'REC000019', 'Laborum et commodi esse. Quia alias sapiente nesciunt sed. Dolor sit omnis. Voluptatibus voluptas dolores cum.', 3298.82, 2387.37, 'REGISTRADA', '2025-05-07 05:55:57', NULL, NULL, 'Voluptatem eum et voluptas libero quia omnis nesciunt.', NULL),
	(50, 145, 'REC000020', 'Dolor ut commodi quos alias dolor illo cumque. Labore laborum natus eum. Dolore maiores a atque perspiciatis voluptates iure.', 3724.64, 3534.71, 'APROBADA', '2025-06-06 05:55:57', '2025-06-14 05:55:57', '2025-06-21 05:55:57', 'Rerum dolorum facilis.', 14),
	(51, 147, 'REC000021', 'Aut asperiores quos rerum dicta repudiandae sit error. Qui ducimus dolore cupiditate minus fugit. Sit sed animi minima quibusdam eaque aut. Harum ipsam culpa ipsum tempora. Culpa consectetur laudantium quae nemo.', 2789.35, 2627.09, 'EN_EVALUACION', '2025-05-17 05:55:57', '2025-05-22 05:55:57', NULL, 'Sequi possimus omnis magni aut eos tempora.', 9),
	(52, 114, 'REC000022', 'Fugiat aut omnis sed autem enim cumque dolorem. Minus aut dicta laudantium illum vitae necessitatibus occaecati. Ad quidem ab quo voluptatem est. Non minima aut aut sint qui cum. Reiciendis unde laborum praesentium.', 1905.45, 1817.73, 'APROBADA', '2025-06-26 05:55:57', '2025-06-27 05:55:57', '2025-07-01 05:55:57', 'Placeat quia reprehenderit fuga provident.', 12),
	(53, 103, 'REC000023', 'Adipisci ea doloremque voluptate eius sunt molestiae. Esse voluptatem voluptas cupiditate corrupti. Id dolorem excepturi natus quod eius aut. Vel ea provident amet in non non.', 2058.23, 1586.70, 'EN_EVALUACION', '2025-05-11 05:55:57', '2025-05-17 05:55:57', NULL, 'Voluptas eos voluptatem.', 13),
	(54, 144, 'REC000024', 'Repellat architecto explicabo aut. Rerum eos eum expedita dicta et in. Nemo perferendis possimus optio consequatur aut. Dolore ipsam eveniet quaerat eum sint molestiae minus.', 1450.43, 1099.80, 'APROBADA', '2025-05-09 05:55:57', '2025-05-12 05:55:57', '2025-05-25 05:55:57', 'Architecto molestiae consectetur et praesentium ut.', 9),
	(55, 101, 'REC000025', 'Qui sed earum repellendus officiis rem. Neque nulla vel aut voluptatum qui. Est consequatur quis accusamus. Dolor dolor consequatur temporibus. Quibusdam dolorem sit quis.', 4950.93, 4803.79, 'EN_EVALUACION', '2025-07-23 05:55:57', '2025-07-29 05:55:57', NULL, 'Fugiat numquam eveniet aperiam.', 11),
	(56, 136, 'REC000026', 'Facere nisi voluptatum cumque. Voluptas sunt sed omnis et magni sunt quasi. Nobis qui et autem autem fuga at. Cum delectus aperiam rerum laborum voluptatem deserunt voluptatum. Minima voluptatem dolorem accusantium esse dignissimos voluptatem ipsa.', 1967.34, 1661.50, 'PAGADA', '2025-07-19 05:55:57', '2025-07-29 05:55:57', '2025-08-12 05:55:57', 'Quae excepturi accusamus ut quaerat magnam.', 10),
	(57, 112, 'REC000027', 'Aut officiis recusandae tempora cupiditate aperiam eum. Aut quidem sapiente molestiae officiis magni hic vitae. Et et ut. Facilis id sint est dolorem.', 3318.26, 2989.25, 'EN_EVALUACION', '2025-05-26 05:55:57', '2025-05-27 05:55:57', NULL, 'Veritatis itaque qui delectus et.', 10),
	(58, 194, 'REC000028', 'Unde et veniam amet. Sit corrupti eos aut consequatur et adipisci. Repudiandae eveniet rerum itaque ut consequatur veritatis. Repellendus alias consequuntur ullam et qui. Quis accusantium quibusdam sed voluptatem blanditiis a omnis.', 815.58, 758.74, 'PAGADA', '2025-07-28 05:55:57', '2025-08-05 05:55:57', '2025-08-11 05:55:57', 'Dolorem et enim id ducimus nam quam sunt.', 9),
	(59, 189, 'REC000029', 'Odit est perferendis commodi qui. Consectetur unde sint ut vel. Pariatur debitis qui.', 2384.01, 2008.97, 'RECHAZADA', '2025-07-20 05:55:57', '2025-07-26 05:55:57', '2025-07-30 05:55:57', 'Consequuntur sunt sint.', 10),
	(60, 197, 'REC000030', 'Dolor sit sed odio placeat natus iure rerum. Atque dolore ut non. Dicta sint voluptate vitae animi.', 511.95, 369.92, 'APROBADA', '2025-05-15 05:55:57', '2025-05-16 05:55:57', '2025-05-26 05:55:57', 'Quibusdam ut laudantium harum officia blanditiis.', 12);

-- Volcando estructura para tabla segura_tu_auto.tarifas_base
CREATE TABLE IF NOT EXISTS `tarifas_base` (
  `id_tarifa` int(11) NOT NULL AUTO_INCREMENT,
  `tipo_seguro` varchar(50) NOT NULL,
  `prima_base` decimal(10,2) NOT NULL,
  `vigente_desde` date NOT NULL,
  `vigente_hasta` date DEFAULT NULL,
  `activo` tinyint(1) DEFAULT 1,
  PRIMARY KEY (`id_tarifa`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla segura_tu_auto.tarifas_base: ~20 rows (aproximadamente)
INSERT INTO `tarifas_base` (`id_tarifa`, `tipo_seguro`, `prima_base`, `vigente_desde`, `vigente_hasta`, `activo`) VALUES
	(21, 'Daños Propios', 318.78, '2025-07-24', '2026-04-24', 1),
	(22, 'Todo Riesgo', 509.49, '2025-07-21', '2026-02-21', 1),
	(23, 'Daños Propios', 599.41, '2025-07-11', '2026-08-11', 1),
	(24, 'Terceros', 549.06, '2025-07-12', '2026-03-12', 1),
	(25, 'Responsabilidad Civil', 446.66, '2025-07-12', '2026-02-12', 1),
	(26, 'Todo Riesgo', 218.12, '2025-07-18', '2026-03-18', 1),
	(27, 'Terceros', 581.21, '2025-07-10', '2026-11-10', 1),
	(28, 'Terceros', 336.60, '2025-07-13', '2026-01-13', 1),
	(29, 'Daños Propios', 271.29, '2025-07-17', '2026-11-17', 1),
	(30, 'Robo y Hurto', 308.09, '2025-07-08', '2026-05-08', 1),
	(31, 'Todo Riesgo', 157.22, '2025-07-11', '2026-08-11', 1),
	(32, 'Daños Propios', 346.88, '2025-07-14', '2026-05-14', 1),
	(33, 'Todo Riesgo', 380.18, '2025-07-25', '2026-11-25', 1),
	(34, 'Daños Propios', 288.51, '2025-07-11', '2026-04-11', 1),
	(35, 'Daños Propios', 100.99, '2025-07-25', '2026-02-25', 1),
	(36, 'Todo Riesgo', 521.28, '2025-07-14', '2026-02-14', 1),
	(37, 'Todo Riesgo', 431.42, '2025-07-06', '2026-07-06', 1),
	(38, 'Robo y Hurto', 530.26, '2025-07-05', '2026-10-05', 1),
	(39, 'Todo Riesgo', 340.10, '2025-07-22', '2026-08-22', 1),
	(40, 'Daños Propios', 187.80, '2025-07-16', '2026-06-16', 1);

	-- Volcando estructura para tabla segura_tu_auto.documentos_reclamacion
CREATE TABLE IF NOT EXISTS `documentos_reclamacion` (
  `id_documento` int(11) NOT NULL AUTO_INCREMENT,
  `reclamacion_id` int(11) NOT NULL,
  `nombre_archivo` varchar(255) NOT NULL,
  `tipo_documento` varchar(50) DEFAULT NULL,
  `ruta_archivo` varchar(500) DEFAULT NULL,
  `fecha_subida` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id_documento`),
  KEY `reclamacion_id` (`reclamacion_id`),
  CONSTRAINT `documentos_reclamacion_ibfk_1` FOREIGN KEY (`reclamacion_id`) REFERENCES `reclamaciones` (`id_reclamacion`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- Volcando estructura para tabla segura_tu_auto.test_entity
CREATE TABLE IF NOT EXISTS `test_entity` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla segura_tu_auto.test_entity: ~0 rows (aproximadamente)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;

# Segura Tu Auto - Sistema de Automatización de Pólizas

🚗 Sistema desarrollado en Java 17 para la automatización del registro de pólizas de seguros automotrices con interfaz de consola interactiva.

## 📋 Características Principales

- **Java 17** como lenguaje principal
- **MySQL** para persistencia de datos
- **Patrón Singleton** para gestión de conexiones de BD
- **Interfaz de consola interactiva** con menús
- **Gestión automática de pólizas** con validaciones

## 🎯 Funcionalidades Implementadas

### ✅ RF-001: Automatización del Registro de Pólizas

- Crear nuevas pólizas con datos de cliente y agente
- Validación de datos de entrada
- Asignación automática de estado PENDIENTE
- Generación de ID único automático

### ✅ RF-002: Gestión de Estados de Pólizas

- Consultar pólizas por estado (PENDIENTE, APROBADA, RECHAZADA, CANCELADA)
- Aprobar pólizas (PENDIENTE → APROBADA)
- Rechazar pólizas (PENDIENTE → RECHAZADA)

### ✅ RF-003: Consulta y Búsqueda de Pólizas

- Consultar todas las pólizas registradas
- Buscar póliza específica por ID
- Filtrar pólizas por estado
- Mostrar información detallada

### ✅ RF-004: Gestión de Clientes y Agentes

- Registro automático de nuevos clientes
- Vinculación automática cliente-agente-póliza

## 🚧 Funcionalidades Pendientes

### ⏳ Pricing Service

- Cálculo automático de primas
- Aplicación de descuentos
- Gestión de factores de riesgo

### ⏳ Reclamaciones Service

- Registro de reclamaciones
- Workflow de aprobación
- Gestión de pagos

## �️ Patrones de Diseño

- **Singleton Pattern**: Gestión única de conexiones a MySQL

## 🚀 Cómo Ejecutar

### Prerrequisitos

- Java 17 o superior
- Maven 3.6+
- MySQL 8.0+

### Configurar MySQL

```sql
CREATE DATABASE segura_tu_auto;
```

### Ejecutar la Aplicación

```bash
# Compilar y ejecutar
mvn clean compile exec:java -Dexec.mainClass="com.seguratuauto.Main"
```

### Configuración (application.properties)

```properties
db.mysql.url=jdbc:mysql://localhost:3306/segura_tu_auto
db.mysql.username=root
db.mysql.password=password
```

## � Uso del Sistema

### Menú Principal

- **[1] Crear Nueva Póliza**: Registrar póliza con datos de cliente
- **[2] Consultar Todas las Pólizas**: Ver listado completo
- **[3] Buscar Póliza por ID**: Consultar información específica
- **[4] Aprobar Póliza**: Cambiar estado a APROBADA
- **[5] Rechazar Póliza**: Cambiar estado a RECHAZADA
- **[6] Consultar por Estado**: Filtrar pólizas por estado

### Estados de Póliza

- **PENDIENTE**: Póliza recién creada
- **APROBADA**: Póliza aprobada para emisión
- **RECHAZADA**: Póliza rechazada
- **CANCELADA**: Póliza cancelada

## 👥 Equipo de Desarrollo

Proyecto desarrollado como parte del curso de Ingeniería de Software II - UTP 2025.

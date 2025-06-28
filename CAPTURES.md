# Explicación del Contenido de las Capturas - Sistema Segura Tu Auto

Este documento explica el contenido funcional mostrado en cada captura del sistema de automatización de pólizas de seguros.

## cap_system.png - Menú Principal del Sistema

**Contenido funcional:**

- Pantalla de inicio de la aplicación de consola
- Menú principal con 6 opciones de gestión de pólizas
- Punto de entrada para todas las funcionalidades del sistema
- Navegación hacia los diferentes módulos de gestión

## cap_system (1).png - Proceso de Creación de Nueva Póliza

**Contenido funcional:**

- Formulario de registro de nueva póliza (RF-001)
- Captura de datos del cliente: nombre, email, teléfono, dirección
- Selección o creación de agente asignado
- Ingreso de detalles de la póliza: número, tipo, prima mensual, cobertura
- Asignación automática de estado inicial "PENDIENTE"
- Generación automática de ID único para la póliza

## cap_system (2).png - Consulta de Todas las Pólizas

**Contenido funcional:**

- Listado completo de pólizas registradas en el sistema (RF-003)
- Información tabular con: ID, Número de Póliza, Tipo, Estado, Prima, Fechas
- Vista general del estado del sistema y cantidad de pólizas registradas
- Navegación para volver al menú principal

## cap_system (3).png - Búsqueda de Póliza por ID

**Contenido funcional:**

- Función de búsqueda específica por ID de póliza (RF-003)
- Prompt para ingresar el ID a buscar
- Resultados detallados de la póliza encontrada
- Información completa: datos de la póliza, cliente asociado y agente responsable

## cap_system (4).png - Información Detallada de Póliza

**Contenido funcional:**

- Vista detallada de una póliza específica
- Datos completos del cliente vinculado
- Información del agente asignado
- Detalles de cobertura, fechas de creación y modificación
- Estado actual de la póliza

## cap_system (6).png - Aprobación de Pólizas

**Contenido funcional:**

- Proceso de cambio de estado de póliza (RF-002)
- Selección de póliza por ID para aprobación
- Validación de estado actual (debe estar en PENDIENTE)
- Cambio de estado de PENDIENTE a APROBADA
- Confirmación de la operación realizada

## cap_system (7).png - Consulta por Estado

**Contenido funcional:**

- Filtrado de pólizas por estado específico (RF-002)
- Selección de estado: PENDIENTE, APROBADA, RECHAZADA, CANCELADA
- Listado de pólizas que coinciden con el estado seleccionado
- Análisis del flujo de trabajo y distribución de estados

## cap_system (8).png - Rechazo de Pólizas

**Contenido funcional:**

- Proceso de rechazo de pólizas (RF-002)
- Selección de póliza por ID para rechazo
- Validación de estado actual (debe estar en PENDIENTE)
- Cambio de estado de PENDIENTE a RECHAZADA
- Registro de la fecha de modificación del estado

## Requisitos Funcionales Demostrados

### RF-001: Automatización del Registro de Pólizas

- Creación de nuevas pólizas con validación de datos
- Registro automático de clientes
- Asignación de agentes y generación de IDs

### RF-002: Gestión de Estados de Pólizas

- Consulta de pólizas por estado
- Aprobación de pólizas pendientes
- Rechazo de pólizas pendientes
- Historial de cambios con fechas

### RF-003: Consulta y Búsqueda de Pólizas

- Lista completa de pólizas
- Búsqueda específica por ID
- Información detallada de pólizas, clientes y agentes

### RF-004: Gestión de Clientes y Agentes

- Registro de nuevos clientes durante creación de póliza
- Vinculación automática de relaciones cliente-agente-póliza

## Flujo de Trabajo Demostrado

1. **Ingreso al sistema** → Menú principal
2. **Creación de póliza** → Registro de datos → Estado PENDIENTE
3. **Consulta de pólizas** → Revisión de estados
4. **Gestión de estados** → Aprobación o rechazo
5. **Seguimiento** → Consultas por estado y búsquedas específicas

# Documentación de API - SeguraTuAuto

## Descripción General

API REST para el sistema de gestión de seguros de automóviles SeguraTuAuto. Esta API permite gestionar agentes, clientes y pólizas de seguros.

**Base URL**: `http://localhost:8080/api`

## Estructura de Respuesta Estándar

Todas las respuestas de la API siguen el siguiente formato:

```json
{
  "success": boolean,
  "message": "string",
  "data": object | array | null,
  "error": "string | null"
}
```

---

## ENDPOINTS DE AUTENTICACIÓN

| Nombre             | Valor                   | Definición                                                                 |
| ------------------ | ----------------------- | -------------------------------------------------------------------------- |
| **Login usuarios** | `POST /api/auth/login`  | Autentica clientes o agentes enviando `email`, `password` y `tipoUsuario`. |
| **Recuperar contraseña** | `POST /api/password/recuperar` | Envía correo con enlace para restablecer la contraseña de un cliente. |
| **Restablecer contraseña** | `POST /api/password/restablecer` | Recibe `token` y nueva contraseña para actualizarla. |

### JSON Request - Login

```json
{
  "email": "usuario@dominio.com",
  "password": "Contraseña#Segura1",
  "tipoUsuario": "cliente" // o "agente"
}
```

### JSON Response - Login exitoso

```json
{
  "success": true,
  "message": "Inicio de sesión exitoso",
  "data": {
    "tipoUsuario": "cliente",
    "cliente": {
      "idCliente": "101",
      "nombre": "Ana Patricia Lopez",
      "email": "ana.lopez@test.com",
      "telefono": "555-777-9999",
      "verificado": true
    },
    "agente": null
  },
  "error": null
}
```

---

## ENDPOINTS DE PRICING

| Nombre                   | Valor                                       | Definición                                   |
| ------------------------ | ------------------------------------------- | -------------------------------------------- |
| **Tipos de Seguro**      | `GET /api/pricing/tipos-seguro`             | Obtiene los tipos de seguro y precios base   |
| **Precio Base por Tipo** | `GET /api/pricing/precio-base/{tipoSeguro}` | Obtiene el precio base de un tipo específico |
| **Health Check**         | `GET /api/pricing/health`                   | Verifica el estado del servicio de pricing   |

### JSON Response - Tipos de Seguro

```json
{
  "mensaje": "Tipos de seguro disponibles",
  "tiposSeguro": {
    "AUTO_FULL": 1500.0,
    "AUTO_INTERMEDIO": 1200.0,
    "MOTO": 400.0,
    "AUTO_BASICO": 800.0
  }
}
```

### JSON Response - Precio Base

```json
{
  "tipoSeguro": "AUTO_FULL",
  "precioBase": 1500.0,
  "mensaje": "Precio base obtenido exitosamente"
}
```

---

## ENDPOINTS DE RECLAMACIONES

### Base URL: `/api/reclamaciones`

| Nombre                    | Valor                                      | Definición                                        |
| ------------------------- | ------------------------------------------ | ------------------------------------------------- |
| **Registrar Reclamación** | `POST /api/reclamaciones/registrar`        | Registra una nueva reclamación con estado inicial |
| **Evaluar Reclamación**   | `POST /api/reclamaciones/{id}/evaluar`     | Evalúa una reclamación usando patrón Strategy     |
| **Aprobar Reclamación**   | `POST /api/reclamaciones/{id}/aprobar`     | Aprueba una reclamación en evaluación             |
| **Rechazar Reclamación**  | `POST /api/reclamaciones/{id}/rechazar`    | Rechaza una reclamación con motivo específico     |
| **Procesar Pago**         | `POST /api/reclamaciones/{id}/pagar`       | Procesa el pago de una reclamación aprobada       |
| **Obtener por ID**        | `GET /api/reclamaciones/{id}`              | Obtiene una reclamación específica                |
| **Obtener por Póliza**    | `GET /api/reclamaciones/poliza/{polizaId}` | Obtiene reclamaciones de una póliza específica    |
| **Obtener por Estado**    | `GET /api/reclamaciones/estado/{estado}`   | Obtiene reclamaciones filtradas por estado        |
| **Obtener Todas**         | `GET /api/reclamaciones/todas`             | Obtiene todas las reclamaciones del sistema       |
| **Health Check**          | `GET /api/reclamaciones/health`            | Verifica el estado del servicio de reclamaciones  |

### Estados Válidos de Reclamación

- `REGISTRADA`: Reclamación recién creada
- `EN_EVALUACION`: En proceso de evaluación
- `APROBADA`: Reclamación aprobada para pago
- `RECHAZADA`: Reclamación rechazada
- `PAGADA`: Reclamación procesada y pagada

### JSON Request - Registrar Reclamación

```json
{
  "polizaId": 606,
  "descripcion": "Prueba de reclamacion sin acentos",
  "montoReclamado": 1500.0
}
```

**Validaciones:**

- `polizaId`: Requerido, ID numérico de póliza existente
- `descripcion`: Requerida, no vacía
- `montoReclamado`: Requerido, mayor a 0.01

### JSON Request - Evaluar Reclamación

```json
{
  "montoAprobado": 1200.0,
  "observaciones": "Inspeccion realizada, danos confirmados",
  "evaluadorId": 72
}
```

**Validaciones:**

- `montoAprobado`: Opcional, si se proporciona debe ser >= 0
- `observaciones`: Opcional, cadena de texto libre
- `evaluadorId`: Opcional, ID numérico del evaluador (Long)

### JSON Request - Aprobar Reclamación

```json
{
  "evaluadorId": 72
}
```

**Validaciones:**

- `evaluadorId`: Requerido, ID numérico del evaluador que aprueba

### JSON Request - Rechazar Reclamación

```json
{
  "motivo": "Danos pre-existentes no cubiertos por la poliza",
  "evaluadorId": 72
}
```

**Validaciones:**

- `motivo`: Requerido, razón del rechazo
- `evaluadorId`: Requerido, ID numérico del evaluador que rechaza

### JSON Response - Reclamación Registrada (201 Created)

```json
{
  "numeroReclamacion": "REC000031",
  "reclamacion": {
    "idReclamacion": 222,
    "polizaId": 606,
    "numeroReclamacion": "REC000031",
    "descripcion": "Prueba de reclamacion sin acentos",
    "montoReclamado": 1500.0,
    "montoAprobado": null,
    "estado": "REGISTRADA",
    "fechaReclamacion": "2025-07-28T10:56:16.3679746",
    "fechaEvaluacion": null,
    "fechaResolucion": null,
    "observaciones": null,
    "evaluadorId": null,
    "porcentajeAprobacion": 0
  },
  "mensaje": "Reclamación registrada exitosamente"
}
```

### JSON Response - Reclamación Evaluada (200 OK)

```json
{
  "resultado": true,
  "reclamacion": {
    "idReclamacion": 222,
    "numeroReclamacion": "REC000031",
    "estado": "EN_EVALUACION",
    "montoReclamado": 1500.0,
    "montoAprobado": 1200.0,
    "fechaEvaluacion": "2025-07-28T11:15:00",
    "evaluadorId": 72,
    "observaciones": "Inspeccion realizada, danos confirmados",
    "porcentajeAprobacion": 80
  },
  "mensaje": "Reclamación evaluada exitosamente",
  "estrategiaUsada": "Evaluación Automática",
  "porcentajeAprobacion": 80
}
```

### JSON Response - Reclamación Aprobada (200 OK)

```json
{
  "resultado": true,
  "reclamacion": {
    "idReclamacion": 222,
    "numeroReclamacion": "REC000031",
    "estado": "APROBADA",
    "montoAprobado": 1200.0,
    "evaluadorId": 72,
    "fechaResolucion": "2025-07-28T11:30:00",
    "observaciones": "Inspeccion realizada, danos confirmados\n[APROBADA - 2025-07-28T11:30:00] Reclamacion aprobada | Evaluador: 72"
  },
  "mensaje": "Reclamación aprobada exitosamente"
}
```

### JSON Response - Reclamación Rechazada (200 OK)

```json
{
  "resultado": true,
  "reclamacion": {
    "idReclamacion": 222,
    "numeroReclamacion": "REC000031",
    "estado": "RECHAZADA",
    "montoAprobado": 0.0,
    "evaluadorId": 72,
    "fechaResolucion": "2025-07-28T11:45:00",
    "observaciones": "Inspeccion realizada, danos confirmados\n[RECHAZADA - 2025-07-28T11:45:00] Motivo: Danos pre-existentes no cubiertos por la poliza | Evaluador: 72"
  },
  "mensaje": "Reclamación rechazada exitosamente"
}
```

### JSON Response - Pago Procesado (200 OK)

```json
{
  "resultado": true,
  "reclamacion": {
    "idReclamacion": 222,
    "numeroReclamacion": "REC000031",
    "estado": "PAGADA",
    "montoAprobado": 1200.0,
    "fechaResolucion": "2025-07-28T12:00:00",
    "observaciones": "Inspeccion realizada, danos confirmados\n[APROBADA - 2025-07-28T11:30:00] Reclamacion aprobada | Evaluador: 72\n[PAGADA - 2025-07-28T12:00:00] Pago procesado por $1200.00"
  },
  "mensaje": "Pago procesado exitosamente"
}
```

### JSON Response - Obtener por Estado

```json
{
  "reclamaciones": [
    {
      "idReclamacion": 197,
      "numeroReclamacion": "REC000017",
      "estado": "EN_EVALUACION"
    }
  ],
  "estado": "EN_EVALUACION",
  "total": 1,
  "mensaje": "Reclamaciones obtenidas exitosamente"
}
```

### JSON Response - Health Check

```json
{
  "status": "OK",
  "service": "Reclamaciones Service",
  "message": "Servicio de reclamaciones funcionando correctamente"
}
```

### 🎯 Patrón Strategy - Selección Automática de Estrategias

El sistema selecciona automáticamente la estrategia según el monto reclamado:

| Monto Reclamado  | Estrategia                      | Porcentaje Aprobación Típico | Descripción                                   |
| ---------------- | ------------------------------- | ---------------------------- | --------------------------------------------- |
| ≤ $5,000         | EvaluacionAutomaticaStrategy    | 95%                          | Evaluación automática para montos menores     |
| $5,001 - $20,000 | EvaluacionManualStrategy        | 85%                          | Requiere evaluación manual                    |
| > $20,000        | EvaluacionEspecializadaStrategy | 75%                          | Requiere evaluación especializada e investig. |

**Ejemplo Práctico**:

- Monto $1,500.00 → **EvaluacionAutomaticaStrategy** (≤ $5,000)
- Porcentaje de aprobación: ~95% del monto solicitado
- Proceso: Automático, sin intervención manual requerida

### 🔄 Workflow Completo de Reclamaciones

#### 1. Flujo Exitoso (Automático ≤ $5,000)

```
REGISTRADA → EN_EVALUACION → APROBADA → PAGADA
```

#### 2. Flujo Manual ($5,001 - $20,000)

```
REGISTRADA → EN_EVALUACION → APROBADA → PAGADA
```

#### 3. Flujo Especializado (> $20,000)

```
REGISTRADA → EN_EVALUACION → APROBADA → PAGADA
```

#### 4. Flujo de Rechazo

```
REGISTRADA → EN_EVALUACION → RECHAZADA
```

### 📋 Ejemplos cURL

#### Registrar Nueva Reclamación

```bash
curl -X POST http://localhost:8080/api/reclamaciones/registrar \
  -H "Content-Type: application/json" \
  -d '{
    "polizaId": 606,
    "descripcion": "Prueba de reclamacion sin acentos",
    "montoReclamado": 1500.00
  }'
```

#### Evaluar Reclamación

```bash
curl -X POST http://localhost:8080/api/reclamaciones/222/evaluar \
  -H "Content-Type: application/json" \
  -d '{
    "montoAprobado": 1200.0,
    "observaciones": "Inspeccion realizada, danos confirmados",
    "evaluadorId": 72
  }'
```

#### Aprobar Reclamación

```bash
curl -X POST http://localhost:8080/api/reclamaciones/222/aprobar \
  -H "Content-Type: application/json" \
  -d '{
    "evaluadorId": 72
  }'
```

#### Rechazar Reclamación

```bash
curl -X POST http://localhost:8080/api/reclamaciones/222/rechazar \
  -H "Content-Type: application/json" \
  -d '{
    "motivo": "Danos pre-existentes no cubiertos por la poliza",
    "evaluadorId": 72
  }'
```

#### Procesar Pago

```bash
curl -X POST http://localhost:8080/api/reclamaciones/222/pagar
```

#### Obtener Reclamación por ID

```bash
curl -X GET http://localhost:8080/api/reclamaciones/181
```

#### Obtener por Estado

```bash
curl -X GET http://localhost:8080/api/reclamaciones/estado/EN_EVALUACION
```

### 🚀 Características Técnicas

- **Validación automática**: `@Valid` en DTOs con Jakarta Bean Validation
- **Transiciones de estado**: Solo cambios válidos del workflow
- **Trazabilidad completa**: Historial de observaciones acumulativas con timestamps
- **Numeración secuencial**: REC000001, REC000002, etc. (basada en database)
- **CORS habilitado**: `@CrossOrigin(origins = "*")`
- **Manejo de errores**: 400, 404, 500 con mensajes descriptivos
- **Generación automática de IDs**: Los números de reclamación se generan automáticamente
- **Soporte UTF-8**: Recomendado evitar caracteres especiales en las pruebas

### 🔧 Problemas Conocidos y Soluciones

#### UTF-8 Encoding

**Problema**: Errores al enviar caracteres especiales (acentos, ñ, etc.)
**Solución**: Usar texto sin acentos en las pruebas o configurar correctamente el encoding del cliente

#### Numeración Secuencial

**Problema**: ~~Conflictos con números duplicados al usar contador estático~~
**Solución**: ✅ **RESUELTO** - Implementado generador basado en database con `COUNT(*) + 1`

#### Evaluador ID

**Problema**: ~~Inconsistencia entre String y Long para evaluadorId~~
**Solución**: ✅ **RESUELTO** - Standardizado a `Long evaluadorId` en toda la aplicación

### ✅ Estado de Testing

Todos los endpoints POST de reclamaciones han sido probados y están funcionando correctamente:

| Endpoint                                | Estado         | Fecha Testing | Resultado                        |
| --------------------------------------- | -------------- | ------------- | -------------------------------- |
| `POST /api/reclamaciones/registrar`     | ✅ FUNCIONANDO | 2025-07-28    | REC000031 generado correctamente |
| `POST /api/reclamaciones/{id}/evaluar`  | ✅ FUNCIONANDO | 2025-07-28    | Strategy pattern operativo       |
| `POST /api/reclamaciones/{id}/aprobar`  | ✅ FUNCIONANDO | 2025-07-28    | Transición de estado correcta    |
| `POST /api/reclamaciones/{id}/rechazar` | ✅ FUNCIONANDO | 2025-07-28    | Motivo y evaluador registrados   |
| `POST /api/reclamaciones/{id}/pagar`    | ✅ FUNCIONANDO | 2025-07-28    | Pago procesado exitosamente      |

---

## ENDPOINTS DE EVALUADORES

| Nombre                            | Valor                                                                                            | Definición                                           |
| --------------------------------- | ------------------------------------------------------------------------------------------------ | ---------------------------------------------------- |
| **Crear Evaluador**               | `POST /api/evaluadores`                                                                          | Crea un nuevo evaluador en el sistema                |
| **Obtener Todos los Evaluadores** | `GET /api/evaluadores`                                                                           | Obtiene la lista completa de evaluadores             |
| **Obtener Evaluadores Activos**   | `GET /api/evaluadores/activos`                                                                   | Obtiene solo los evaluadores activos                 |
| **Obtener Evaluador por ID**      | `GET /api/evaluadores/{id}`                                                                      | Obtiene un evaluador específico por su ID            |
| **Obtener Evaluador por Código**  | `GET /api/evaluadores/codigo/{codigo}`                                                           | Busca un evaluador por su código único               |
| **Obtener Evaluador por Email**   | `GET /api/evaluadores/email/{email}`                                                             | Busca un evaluador por su email                      |
| **Buscar Evaluadores por Nombre** | `GET /api/evaluadores/buscar?nombre={nombre}`                                                    | Busca evaluadores que coincidan con el nombre        |
| **Buscar por Especialidad**       | `GET /api/evaluadores/especialidad/{especialidad}`                                               | Busca evaluadores por especialidad                   |
| **Búsqueda Avanzada**             | `GET /api/evaluadores/buscar-avanzado?nombre={nombre}&email={email}&especialidad={especialidad}` | Búsqueda avanzada con múltiples criterios            |
| **Actualizar Evaluador**          | `PUT /api/evaluadores/{id}`                                                                      | Actualiza los datos de un evaluador existente        |
| **Eliminar Evaluador**            | `DELETE /api/evaluadores/{id}`                                                                   | Elimina un evaluador del sistema (lógica)            |
| **Reactivar Evaluador**           | `PUT /api/evaluadores/{id}/reactivar`                                                            | Reactiva un evaluador marcado como inactivo          |
| **Verificar Existencia**          | `GET /api/evaluadores/{id}/existe`                                                               | Verifica si existe un evaluador                      |
| **Estadísticas de Evaluadores**   | `GET /api/evaluadores/estadisticas`                                                              | Obtiene estadísticas de evaluadores                  |
| **Generar Nuevo Código**          | `GET /api/evaluadores/nuevo-codigo`                                                              | Genera el siguiente código disponible para evaluador |

### JSON Request - Crear/Actualizar Evaluador

```json
{
  "nombre": "Dr. María Elena Rodríguez",
  "codigo": "EV001",
  "email": "maria.rodriguez@seguratuauto.com",
  "telefono": "+34 612 345 678",
  "especialidad": "Evaluación de Siniestros Vehiculares",
  "activo": true
}
```

**Validaciones:**

- `nombre`: Obligatorio, máximo 100 caracteres
- `codigo`: Opcional, máximo 20 caracteres, solo letras y números
- `email`: Formato email válido, máximo 150 caracteres
- `telefono`: Opcional, máximo 20 caracteres
- `especialidad`: Opcional, máximo 100 caracteres
- `activo`: Opcional, boolean (default: true)

### JSON Response - Evaluador

```json
{
  "success": true,
  "message": "Evaluador creado exitosamente",
  "data": {
    "idEvaluador": "25",
    "nombre": "Dr. María Elena Rodríguez",
    "codigo": "EV001",
    "email": "maria.rodriguez@seguratuauto.com",
    "telefono": "+34 612 345 678",
    "especialidad": "Evaluación de Siniestros Vehiculares",
    "activo": "true",
    "fechaIngreso": "2025-07-28 14:30:45"
  },
  "error": null
}
```

### JSON Response - Estadísticas de Evaluadores

```json
{
  "success": true,
  "message": "Estadísticas obtenidas exitosamente",
  "data": {
    "totalEvaluadores": 15,
    "evaluadoresActivos": 12,
    "evaluadoresInactivos": 3
  },
  "error": null
}
```

### JSON Response - Nuevo Código

```json
{
  "success": true,
  "message": "Nuevo código generado",
  "data": {
    "codigo": "EV0016"
  },
  "error": null
}
```

---

## ENDPOINTS DE AGENTES

| Nombre                        | Valor                                                                            | Definición                                        |
| ----------------------------- | -------------------------------------------------------------------------------- | ------------------------------------------------- |
| **Crear Agente**              | `POST /api/agentes`                                                              | Crea un nuevo agente en el sistema                |
| **Obtener Todos los Agentes** | `GET /api/agentes`                                                               | Obtiene la lista completa de agentes              |
| **Obtener Agentes Activos**   | `GET /api/agentes/activos`                                                       | Obtiene solo los agentes activos                  |
| **Obtener Agente por ID**     | `GET /api/agentes/{id}`                                                          | Obtiene un agente específico por su ID            |
| **Obtener Agente por Código** | `GET /api/agentes/codigo/{codigo}`                                               | Busca un agente por su código único               |
| **Obtener Agente por Email**  | `GET /api/agentes/email/{email}`                                                 | Busca un agente por su email                      |
| **Buscar Agentes por Nombre** | `GET /api/agentes/buscar?nombre={nombre}`                                        | Busca agentes que coincidan con el nombre         |
| **Búsqueda Múltiple**         | `GET /api/agentes/buscar-multiple?nombre={nombre}&codigo={codigo}&email={email}` | Búsqueda avanzada con múltiples criterios         |
| **Actualizar Agente**         | `PUT /api/agentes/{id}`                                                          | Actualiza los datos de un agente existente        |
| **Eliminar Agente**           | `DELETE /api/agentes/{id}`                                                       | Elimina un agente del sistema                     |
| **Estadísticas de Agentes**   | `GET /api/agentes/estadisticas`                                                  | Obtiene estadísticas de agentes                   |
| **Generar Nuevo Código**      | `GET /api/agentes/nuevo-codigo`                                                  | Genera el siguiente código disponible para agente |

### JSON Request - Crear/Actualizar Agente

```json
{
  "nombre": "Juan Pérez García",
  "codigo": "AG001",
  "email": "juan.perez@seguratuauto.com",
  "telefono": "+34 612 345 678"
}
```

**Validaciones:**

- `nombre`: Obligatorio, máximo 100 caracteres
- `codigo`: Opcional, máximo 20 caracteres, solo letras y números
- `email`: Formato email válido, máximo 150 caracteres
- `telefono`: Opcional, máximo 20 caracteres

### JSON Response - Agente

```json
{
  "success": true,
  "message": "Agente creado exitosamente",
  "data": {
    "idAgente": 21,
    "nombre": "Carlos Eduardo Silva",
    "codigo": "AG0011",
    "email": "carlos.silva@unique.com",
    "telefono": "555-999-1122",
    "cantidadPolizas": 0
  },
  "error": null
}
```

---

## ENDPOINTS DE CLIENTES

| Nombre                         | Valor                                                             | Definición                                  |
| ------------------------------ | ----------------------------------------------------------------- | ------------------------------------------- |
| **Crear Cliente**              | `POST /api/clientes`                                              | Crea un nuevo cliente en el sistema         |
| **Obtener Todos los Clientes** | `GET /api/clientes`                                               | Obtiene la lista completa de clientes       |
| **Obtener Cliente por ID**     | `GET /api/clientes/{id}`                                          | Obtiene un cliente específico por su ID     |
| **Obtener Cliente por Email**  | `GET /api/clientes/email/{email}`                                 | Busca un cliente por su email               |
| **Buscar Clientes por Nombre** | `GET /api/clientes/buscar?nombre={nombre}`                        | Busca clientes que coincidan con el nombre  |
| **Búsqueda Avanzada**          | `GET /api/clientes/buscar-avanzado?nombre={nombre}&email={email}` | Búsqueda con múltiples criterios            |
| **Actualizar Cliente**         | `PUT /api/clientes/{id}`                                          | Actualiza los datos de un cliente existente |
| **Eliminar Cliente**           | `DELETE /api/clientes/{id}`                                       | Elimina un cliente del sistema              |
| **Verificar Existencia**       | `GET /api/clientes/{id}/existe`                                   | Verifica si existe un cliente               |
| **Estadísticas de Clientes**   | `GET /api/clientes/estadisticas`                                  | Obtiene el total de clientes                |

### JSON Request - Crear/Actualizar Cliente

```json
{
  "nombre": "María González López",
  "email": "maria.gonzalez@email.com",
  "telefono": "+34 687 123 456"
}
```

**Validaciones:**

- `nombre`: Obligatorio, máximo 100 caracteres
- `email`: Formato email válido, máximo 150 caracteres
- `telefono`: Opcional, formato válido de teléfono, máximo 20 caracteres

````

---

## ENDPOINTS DE PÓLIZAS

| Nombre                        | Valor                                            | Definición                                  |
| ----------------------------- | ------------------------------------------------ | ------------------------------------------- |
| **Crear Póliza**              | `POST /api/polizas`                              | Crea una nueva póliza de seguro             |
| **Obtener Todas las Pólizas** | `GET /api/polizas`                               | Obtiene la lista completa de pólizas        |
| **Obtener Póliza por ID**     | `GET /api/polizas/{id}`                          | Obtiene una póliza específica por su ID     |
| **Obtener Póliza por Número** | `GET /api/polizas/numero/{numeroPoliza}`         | Busca una póliza por su número              |
| **Pólizas por Cliente**       | `GET /api/polizas/cliente/{clienteId}`           | Obtiene todas las pólizas de un cliente     |
| **Pólizas por Estado**        | `GET /api/polizas/estado/{estado}`               | Obtiene pólizas filtradas por estado        |
| **Aprobar Póliza**            | `PUT /api/polizas/{id}/aprobar`                  | Cambia el estado de la póliza a APROBADA    |
| **Rechazar Póliza**           | `PUT /api/polizas/{id}/rechazar?motivo={motivo}` | Cambia el estado de la póliza a RECHAZADA   |
| **Actualizar Póliza**         | `PUT /api/polizas/{id}`                          | Actualiza los datos de una póliza existente |

### JSON Request - Crear/Actualizar Póliza

```json
{
  "numeroPoliza": "POL-2025-001",
  "fechaEmision": "2025-07-28",
  "fechaVencimiento": "2026-07-28",
  "estado": "PENDIENTE",
  "clienteId": 101,
  "agenteId": 21,
  "prima": 1250.5,
  "tipoSeguro": "Seguro a Todo Riesgo",
  "observaciones": "Cliente con historial excelente"
}
````

**Validaciones:**

- `numeroPoliza`: Obligatorio, máximo 50 caracteres, único
- `fechaEmision`: Obligatorio, formato fecha (YYYY-MM-DD)
- `fechaVencimiento`: Opcional, formato fecha (YYYY-MM-DD)
- `estado`: Obligatorio, valores válidos: PENDIENTE, APROBADA, RECHAZADA, CANCELADA
- `clienteId`: Obligatorio, ID numérico válido del cliente
- `agenteId`: Obligatorio, ID numérico válido del agente
- `prima`: Obligatorio, valor decimal mayor a 0, máximo 8 dígitos enteros y 2 decimales
- `tipoSeguro`: Obligatorio, máximo 100 caracteres
- `observaciones`: Opcional, máximo 1000 caracteres

### JSON Response - Póliza

```json
{
  "numeroPoliza": "POL-2025-222",
  "fechaEmision": "2025-07-28",
  "fechaVencimiento": "2026-07-28",
  "estado": "PENDIENTE",
  "clienteId": "101",
  "agenteId": "21",
  "prima": 1250.5,
  "tipoSeguro": "Seguro a Todo Riesgo",
  "observaciones": "Cliente con historial excelente"
}
```

---

## ESTADOS DE EVALUADOR

| Estado       | Descripción                                     |
| ------------ | ----------------------------------------------- |
| **ACTIVO**   | Evaluador activo y disponible para asignaciones |
| **INACTIVO** | Evaluador desactivado temporalmente             |

---

## ESPECIALIDADES DE EVALUADOR (Ejemplos)

| Especialidad                             | Descripción                                |
| ---------------------------------------- | ------------------------------------------ |
| **Evaluación de Siniestros Vehiculares** | Especialista en daños de automóviles       |
| **Evaluación de Motos**                  | Especialista en siniestros de motocicletas |
| **Evaluación de Fraudes**                | Especialista en detección de fraudes       |
| **Evaluación Médica**                    | Especialista en evaluación de lesiones     |
| **Evaluación de Daños Materiales**       | Especialista en daños a terceros           |

---

## CÓDIGOS DE RESPUESTA HTTP

| Código  | Descripción           | Cuándo se usa                                               |
| ------- | --------------------- | ----------------------------------------------------------- |
| **200** | OK                    | Operación exitosa (GET, PUT)                                |
| **201** | Created               | Recurso creado exitosamente (POST)                          |
| **400** | Bad Request           | Datos de entrada inválidos o violación de reglas de negocio |
| **404** | Not Found             | Recurso no encontrado                                       |
| **500** | Internal Server Error | Error interno del servidor                                  |

---

## ESTADOS DE PÓLIZA

| Estado        | Descripción                                |
| ------------- | ------------------------------------------ |
| **PENDIENTE** | Póliza recién creada, esperando aprobación |
| **APROBADA**  | Póliza aprobada y activa                   |
| **RECHAZADA** | Póliza rechazada                           |
| **CANCELADA** | Póliza cancelada                           |

---

## EJEMPLOS DE RESPUESTAS DE ERROR

### Error de validación (400 Bad Request)

```json
{
  "success": false,
  "message": "Error en los datos: El nombre del agente es obligatorio",
  "data": null,
  "error": "Validation failed"
}
```

### Recurso no encontrado (404 Not Found)

```json
{
  "success": false,
  "message": "Agente no encontrado",
  "data": null,
  "error": null
}
```

### Error interno del servidor (500 Internal Server Error)

```json
{
  "success": false,
  "message": "Error interno del servidor",
  "data": null,
  "error": "Database connection failed"
}
```

---

## CONFIGURACIÓN CORS

La API está configurada para aceptar peticiones desde cualquier origen (`*`) durante el desarrollo. En producción se debe configurar con dominios específicos.

---

## AUTENTICACIÓN

Actualmente la API no implementa autenticación. Se recomienda implementar JWT tokens para producción.

---

## NOTAS ADICIONALES

1. **IDs**: Los IDs de entidades son números auto-incrementales (Long), no UUIDs
2. **Fechas**: Se envían en formato ISO 8601 (YYYY-MM-DD)
3. **Decimales**: Los valores monetarios usan BigDecimal con precisión de 2 decimales
4. **Encoding**: Todas las respuestas están en UTF-8
5. **Content-Type**: Siempre usar `application/json` para requests y responses
6. **Códigos de Agente**: Se generan automáticamente con formato AG#### (ej: AG0011)
7. **Códigos de Evaluador**: Se generan automáticamente con formato EV#### (ej: EV0001)
8. **Validaciones**: Email y teléfono únicos por entidad
9. **Eliminación Lógica**: Los evaluadores se marcan como inactivos en lugar de eliminarse físicamente
10. **Especialidades**: Campo libre para especificar la especialidad del evaluador

---

## PRUEBAS POSTMAN REALIZADAS

### ✅ **Endpoints Probados y Funcionando**

#### **EVALUADORES**

**GET - Obtener todos los evaluadores**

```
GET http://localhost:8080/api/evaluadores
Content-Type: application/json
```

**POST - Crear nuevo evaluador**

```
POST http://localhost:8080/api/evaluadores
Content-Type: application/json
Accept: application/json

{
  "nombre": "Dr. María Elena Rodríguez",
  "email": "maria.rodriguez@seguratuauto.com",
  "telefono": "+34 612 345 678",
  "especialidad": "Evaluación de Siniestros Vehiculares"
}
```

**GET - Buscar evaluadores por nombre**

```
GET http://localhost:8080/api/evaluadores/buscar?nombre=Maria
Content-Type: application/json
```

**GET - Buscar evaluador por código**

```
GET http://localhost:8080/api/evaluadores/codigo/EV001
Content-Type: application/json
```

**GET - Buscar por especialidad**

```
GET http://localhost:8080/api/evaluadores/especialidad/Siniestros
Content-Type: application/json
```

**GET - Obtener estadísticas de evaluadores**

```
GET http://localhost:8080/api/evaluadores/estadisticas
Content-Type: application/json
```

**GET - Generar nuevo código de evaluador**

```
GET http://localhost:8080/api/evaluadores/nuevo-codigo
Content-Type: application/json
```

**PUT - Reactivar evaluador**

```
PUT http://localhost:8080/api/evaluadores/{id}/reactivar
Content-Type: application/json
```

#### **AGENTES**

**GET - Obtener todos los agentes**

```
GET http://localhost:8080/api/agentes
Content-Type: application/json
```

**POST - Crear nuevo agente**

```
POST http://localhost:8080/api/agentes
Content-Type: application/json
Accept: application/json

{
  "nombre": "Carlos Eduardo Silva",
  "email": "carlos.silva@unique.com",
  "telefono": "555-999-1122"
}
```

**GET - Buscar agentes por nombre**

```
GET http://localhost:8080/api/agentes/buscar?nombre=Maria
Content-Type: application/json
```

**GET - Buscar agente por código**

```
GET http://localhost:8080/api/agentes/codigo/AG001
Content-Type: application/json
```

**GET - Obtener estadísticas de agentes**

```
GET http://localhost:8080/api/agentes/estadisticas
Content-Type: application/json
```

**GET - Generar nuevo código de agente**

```
GET http://localhost:8080/api/agentes/nuevo-codigo
Content-Type: application/json
```

#### **CLIENTES**

**GET - Obtener todos los clientes**

```
GET http://localhost:8080/api/clientes
Content-Type: application/json
```

**POST - Crear nuevo cliente**

```
POST http://localhost:8080/api/clientes
Content-Type: application/json
Accept: application/json

{
  "nombre": "Ana Patricia Lopez",
  "email": "ana.lopez@test.com",
  "telefono": "555-777-9999"
}
```

**GET - Buscar clientes por nombre**

```
GET http://localhost:8080/api/clientes/buscar?nombre=Carlos
Content-Type: application/json
```

**GET - Obtener estadísticas de clientes**

```
GET http://localhost:8080/api/clientes/estadisticas
Content-Type: application/json
```

#### **PÓLIZAS**

**GET - Obtener todas las pólizas**

```
GET http://localhost:8080/api/polizas
Content-Type: application/json
```

**GET - Buscar pólizas por estado PENDIENTE**

```
GET http://localhost:8080/api/polizas/estado/PENDIENTE
Content-Type: application/json
```

**GET - Buscar pólizas por estado APROBADA**

```
GET http://localhost:8080/api/polizas/estado/APROBADA
Content-Type: application/json
```

**GET - Buscar póliza por número**

```
GET http://localhost:8080/api/polizas/numero/4541
Content-Type: application/json
```

**POST - Crear nueva póliza**

```
POST http://localhost:8080/api/polizas
Content-Type: application/json
Accept: application/json

{
  "numeroPoliza": "POL-2025-001",
  "fechaEmision": "2025-07-28",
  "fechaVencimiento": "2026-07-28",
  "estado": "PENDIENTE",
  "clienteId": 101,
  "agenteId": 21,
  "prima": 1250.50,
  "tipoSeguro": "Seguro a Todo Riesgo",
  "observaciones": "Cliente con historial excelente"
}
```

#### **PRICING (Patrón Decorator)**

**GET - Obtener tipos de seguro disponibles**

```
GET http://localhost:8080/api/pricing/tipos-seguro
Content-Type: application/json
```

**GET - Obtener precio base por tipo**

```
GET http://localhost:8080/api/pricing/precio-base/AUTO_FULL
Content-Type: application/json
```

**GET - Health check del servicio**

```
GET http://localhost:8080/api/pricing/health
Content-Type: application/json
```

#### **RECLAMACIONES (Patrón Strategy)**

**POST - Registrar nueva reclamación**

```
POST http://localhost:8080/api/reclamaciones/registrar
Content-Type: application/json
Accept: application/json

{
  "polizaId": 605,
  "descripcion": "Colisión frontal en intersección",
  "montoReclamado": 12000.00
}
```

**POST - Evaluar reclamación (Patrón Strategy automático)**

```
POST http://localhost:8080/api/reclamaciones/231/evaluar
Content-Type: application/json
Accept: application/json

{
  "observaciones": "Inspección realizada, daños confirmados",
  "evaluador": "Juan Pérez"
}
```

**POST - Aprobar reclamación**

```
POST http://localhost:8080/api/reclamaciones/231/aprobar?evaluador=Juan%20Pérez
Content-Type: application/json
```

**POST - Rechazar reclamación**

```
POST http://localhost:8080/api/reclamaciones/231/rechazar
Content-Type: application/json
Accept: application/json

{
  "motivo": "Daños pre-existentes no cubiertos por la póliza",
  "evaluador": "Ana López"
}
```

**POST - Procesar pago de reclamación**

```
POST http://localhost:8080/api/reclamaciones/231/pagar
Content-Type: application/json
```

**GET - Obtener reclamación por ID**

```
GET http://localhost:8080/api/reclamaciones/181
Content-Type: application/json
```

**GET - Obtener todas las reclamaciones**

```
GET http://localhost:8080/api/reclamaciones/todas
Content-Type: application/json
```

**GET - Obtener reclamaciones por estado**

```
GET http://localhost:8080/api/reclamaciones/estado/REGISTRADA
Content-Type: application/json
```

**GET - Obtener reclamaciones por póliza**

```
GET http://localhost:8080/api/reclamaciones/poliza/605
Content-Type: application/json
```

**GET - Health check del servicio**

```
GET http://localhost:8080/api/reclamaciones/health
Content-Type: application/json
```

### ⚠️ **Problemas Identificados**

1. ~~**Error en creación de entidades**: Los endpoints POST retornan error 500 (problema con la BD)~~ **✅ SOLUCIONADO**
2. **Encoding UTF-8**: Las búsquedas con acentos fallan, usar caracteres sin acentos
3. **Formato de IDs**: Se cambió de UUIDs a IDs numéricos auto-incrementales
4. **Operaciones PUT/DELETE**: No funcionan correctamente debido a problemas con los IDs

### 📋 **Resumen de Pruebas**

| Endpoint                             | Método | Estado | Observaciones                          |
| ------------------------------------ | ------ | ------ | -------------------------------------- |
| `/api/agentes`                       | GET    | ✅     | Funciona correctamente                 |
| `/api/agentes`                       | POST   | ✅     | **FUNCIONANDO** - Problema resuelto    |
| `/api/agentes/buscar`                | GET    | ✅     | Solo sin acentos                       |
| `/api/agentes/codigo/{codigo}`       | GET    | ✅     | Funciona correctamente                 |
| `/api/agentes/estadisticas`          | GET    | ✅     | Funciona correctamente                 |
| `/api/agentes/nuevo-codigo`          | GET    | ✅     | Funciona correctamente                 |
| `/api/clientes`                      | GET    | ✅     | Funciona correctamente                 |
| `/api/clientes`                      | POST   | ✅     | **FUNCIONANDO** - Problema resuelto    |
| `/api/clientes/buscar`               | GET    | ✅     | Funciona correctamente                 |
| `/api/clientes/estadisticas`         | GET    | ✅     | Funciona correctamente                 |
| `/api/polizas`                       | GET    | ✅     | Funciona correctamente                 |
| `/api/polizas/estado/{estado}`       | GET    | ✅     | Funciona correctamente                 |
| `/api/polizas/numero/{numero}`       | GET    | ✅     | Funciona correctamente                 |
| `/api/polizas/{id}/aprobar`          | PUT    | ✅     | Funciona correctamente                 |
| `/api/pricing/tipos-seguro`          | GET    | ✅     | **Patrón Decorator funcionando**       |
| `/api/pricing/precio-base/{tipo}`    | GET    | ✅     | **Patrón Decorator funcionando**       |
| `/api/pricing/health`                | GET    | ✅     | Health check OK                        |
| `/api/reclamaciones/registrar`       | POST   | ✅     | **NUEVO** - Registra nueva reclamación |
| `/api/reclamaciones/{id}/evaluar`    | POST   | ✅     | **NUEVO** - Patrón Strategy automático |
| `/api/reclamaciones/{id}/aprobar`    | POST   | ✅     | **NUEVO** - Aprueba reclamación        |
| `/api/reclamaciones/{id}/rechazar`   | POST   | ✅     | **NUEVO** - Rechaza con motivo         |
| `/api/reclamaciones/{id}/pagar`      | POST   | ✅     | **NUEVO** - Procesa pago               |
| `/api/reclamaciones/{id}`            | GET    | ✅     | **NUEVO** - Obtiene por ID             |
| `/api/reclamaciones/todas`           | GET    | ✅     | **Patrón Strategy funcionando**        |
| `/api/reclamaciones/estado/{estado}` | GET    | ✅     | **Patrón Strategy funcionando**        |
| `/api/reclamaciones/poliza/{poliza}` | GET    | ✅     | **NUEVO** - Reclamaciones por póliza   |
| `/api/reclamaciones/health`          | GET    | ✅     | Health check OK                        |

---

_Documentación generada para SeguraTuAuto API v1.0_  
_Pruebas POSTMAN realizadas el 28 de julio de 2025_  
_Endpoints POST de Agentes y Clientes: ✅ FUNCIONANDO CORRECTAMENTE_  
_**NUEVO**: Sistema completo de reclamaciones con Patrón Strategy implementado_

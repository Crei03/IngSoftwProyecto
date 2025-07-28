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

| Nombre                 | Valor                                      | Definición                                       |
| ---------------------- | ------------------------------------------ | ------------------------------------------------ |
| **Crear Reclamación**  | `POST /api/reclamaciones`                  | Crea una nueva reclamación                       |
| **Obtener Todas**      | `GET /api/reclamaciones/todas`             | Obtiene todas las reclamaciones                  |
| **Obtener por ID**     | `GET /api/reclamaciones/{id}`              | Obtiene una reclamación específica               |
| **Obtener por Póliza** | `GET /api/reclamaciones/poliza/{polizaId}` | Obtiene reclamaciones de una póliza específica   |
| **Obtener por Estado** | `GET /api/reclamaciones/estado/{estado}`   | Obtiene reclamaciones filtradas por estado       |
| **Health Check**       | `GET /api/reclamaciones/health`            | Verifica el estado del servicio de reclamaciones |

### Estados Válidos de Reclamación

- `REGISTRADA`: Reclamación recién creada
- `EN_EVALUACION`: En proceso de evaluación
- `APROBADA`: Reclamación aprobada
- `RECHAZADA`: Reclamación rechazada
- `PAGADA`: Reclamación procesada y pagada

### JSON Response - Reclamaciones

```json
{
  "total": 0,
  "mensaje": "Todas las reclamaciones obtenidas exitosamente",
  "reclamaciones": []
}
```

### JSON Response - Por Estado

```json
{
  "estado": "REGISTRADA",
  "total": 0,
  "mensaje": "Reclamaciones obtenidas exitosamente",
  "reclamaciones": []
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
7. **Validaciones**: Email y teléfono únicos por entidad

---

## PRUEBAS POSTMAN REALIZADAS

### ✅ **Endpoints Probados y Funcionando**

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
GET http://localhost:8080/api/reclamaciones/poliza/101
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

| Endpoint                             | Método | Estado | Observaciones                       |
| ------------------------------------ | ------ | ------ | ----------------------------------- |
| `/api/agentes`                       | GET    | ✅     | Funciona correctamente              |
| `/api/agentes`                       | POST   | ✅     | **FUNCIONANDO** - Problema resuelto |
| `/api/agentes/buscar`                | GET    | ✅     | Solo sin acentos                    |
| `/api/agentes/codigo/{codigo}`       | GET    | ✅     | Funciona correctamente              |
| `/api/agentes/estadisticas`          | GET    | ✅     | Funciona correctamente              |
| `/api/agentes/nuevo-codigo`          | GET    | ✅     | Funciona correctamente              |
| `/api/clientes`                      | GET    | ✅     | Funciona correctamente              |
| `/api/clientes`                      | POST   | ✅     | **FUNCIONANDO** - Problema resuelto |
| `/api/clientes/buscar`               | GET    | ✅     | Funciona correctamente              |
| `/api/clientes/estadisticas`         | GET    | ✅     | Funciona correctamente              |
| `/api/polizas`                       | GET    | ✅     | Funciona correctamente              |
| `/api/polizas/estado/{estado}`       | GET    | ✅     | Funciona correctamente              |
| `/api/polizas/numero/{numero}`       | GET    | ✅     | Funciona correctamente              |
| `/api/pricing/tipos-seguro`          | GET    | ✅     | **Patrón Decorator funcionando**    |
| `/api/pricing/precio-base/{tipo}`    | GET    | ✅     | **Patrón Decorator funcionando**    |
| `/api/pricing/health`                | GET    | ✅     | Health check OK                     |
| `/api/reclamaciones/todas`           | GET    | ✅     | **Patrón Strategy funcionando**     |
| `/api/reclamaciones/estado/{estado}` | GET    | ✅     | **Patrón Strategy funcionando**     |
| `/api/reclamaciones/health`          | GET    | ✅     | Health check OK                     |
| `/api/polizas/{id}/aprobar`          | PUT    | ✅     | Funciona correctamente              |

---

_Documentación generada para SeguraTuAuto API v1.0_  
_Pruebas POSTMAN realizadas el 28 de julio de 2025_  
_Endpoints POST de Agentes y Clientes: ✅ FUNCIONANDO CORRECTAMENTE_

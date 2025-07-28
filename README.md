# Sistema de Gestión de Pólizas de Auto - SeguraTuAuto

## Descripción

Sistema web para la gestión automatizada de clientes, agentes y pólizas de seguros de automóviles. El proyecto incluye un backend en Spring Boot y un frontend en Vue.js.

## Estructura del Proyecto

```
seguratuauto/
├── src/                    # Backend Spring Boot
│   └── main/java/com/seguratuauto/
│       ├── api/           # Controladores REST
│       ├── model/         # Entidades JPA
│       ├── service/       # Lógica de negocio
│       └── dao/           # Repositorios
├── fe/                    # Frontend Vue.js
│   └── src/
│       ├── components/    # Componentes Vue
│       └── services/      # Servicios de API
└── build.gradle          # Configuración Gradle
```

## Funcionalidades Implementadas

### Landing Page - Página Principal
- ✅ Diseño moderno y atractivo para la aseguradora
- ✅ Formulario de registro de clientes con validación
- ✅ Sistema de login con validación de clientes existentes
- ✅ Perfil de usuario con información del cliente
- ✅ Integración completa con la base de datos
- ✅ Diseño responsivo y temas claro/oscuro

### RF-001: Gestión de Clientes y Agentes
- ✅ Registro automático de clientes
- ✅ Visualización de agentes disponibles
- ✅ Lista de clientes registrados
- ✅ Integración con API del backend

### RF-002: Automatización del Registro de Pólizas de Auto
- ✅ Creación automática de pólizas de auto
- ✅ Validación automática de datos
- ✅ Asignación automática de estado PENDIENTE
- ✅ Panel de control con estadísticas
- ✅ Filtrado por estado de pólizas
- ✅ Integración con API del backend

## Cambios Realizados

### 1. Nueva Landing Page
- ✅ Creada página principal atractiva para la aseguradora
- ✅ Formulario de registro con validación de clientes existentes
- ✅ Sistema de login que valida contra la base de datos
- ✅ Perfil de usuario con información completa del cliente
- ✅ Diseño moderno y responsivo

### 2. Integración con Base de Datos Real
- **Antes**: Los componentes mostraban datos estáticos hardcodeados
- **Ahora**: Los componentes consumen datos reales desde la API del backend

### 2. Servicio de API (`fe/src/services/apiService.js`)
- Creado servicio centralizado para manejar todas las llamadas a la API
- Métodos para clientes, agentes y pólizas
- Manejo de errores y respuestas HTTP

### 3. Componente RF001 Actualizado
- ✅ Carga datos reales de clientes y agentes desde la API
- ✅ Registro de clientes en la base de datos
- ✅ Indicadores de carga y manejo de errores
- ✅ Interfaz responsiva y moderna

### 4. Componente RF002 Actualizado
- ✅ Carga datos reales de pólizas de auto y agentes desde la API
- ✅ Creación de pólizas de auto en la base de datos
- ✅ Actualización de estados de pólizas
- ✅ Indicadores de carga y manejo de errores
- ✅ Especializado en seguros de automóviles

## Instrucciones de Ejecución

### 1. Configurar Base de Datos
```sql
-- Crear base de datos MySQL
CREATE DATABASE segura_tu_auto;
```

### 2. Iniciar Backend
```bash
# Desde la raíz del proyecto
./gradlew bootRun
```
El backend estará disponible en: `http://localhost:8080`

### 3. Iniciar Frontend
```bash
# Desde el directorio fe/
cd fe
npm install
npm run dev
```
El frontend estará disponible en: `http://localhost:5173`

## Endpoints de la API

### Clientes
- `GET /api/clientes` - Obtener todos los clientes
- `POST /api/clientes` - Crear nuevo cliente
- `GET /api/clientes/{id}` - Obtener cliente por ID

### Agentes
- `GET /api/agentes` - Obtener todos los agentes
- `GET /api/agentes/activos` - Obtener agentes activos
- `POST /api/agentes` - Crear nuevo agente

### Pólizas de Auto
- `GET /api/polizas` - Obtener todas las pólizas de auto
- `POST /api/polizas` - Crear nueva póliza de auto
- `GET /api/polizas/estado/{estado}` - Obtener pólizas por estado

## Características Técnicas

### Frontend
- **Framework**: Vue.js 3 con Composition API
- **Estilos**: CSS personalizado con temas claro/oscuro
- **Iconos**: Lucide Vue Next
- **Responsive**: Diseño adaptativo para móviles y desktop

### Backend
- **Framework**: Spring Boot 3
- **Base de Datos**: MySQL 8
- **ORM**: Hibernate/JPA
- **API**: REST con CORS habilitado

## Notas Importantes

1. **Base de Datos**: Asegúrate de que MySQL esté corriendo y la base de datos `segura_tu_auto` exista
2. **Puertos**: El backend usa el puerto 8080 por defecto
3. **CORS**: Configurado para permitir conexiones desde el frontend
4. **Datos**: Los componentes ahora muestran datos reales de la base de datos
5. **Especialización**: Sistema especializado únicamente en seguros de automóviles

## Próximos Pasos

- [ ] Implementar endpoint para actualizar estado de pólizas
- [ ] Agregar autenticación y autorización
- [ ] Implementar búsqueda y filtros avanzados
- [ ] Agregar validaciones adicionales en el frontend
- [ ] Implementar notificaciones en tiempo real 
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

### 5. Verificación de cuentas por correo (MailDev)
- ✅ Se envía un correo moderno en español al registrar un cliente
- ✅ El enlace redirige al login una vez confirmada la cuenta
- ✅ Tokens con expiración configurable para evitar fraudes
- ✅ MailDev (`localhost:1025`) simula el servidor SMTP en desarrollo

### 6. Autenticación con contraseñas seguras
- ✅ Registro de clientes y agentes con contraseña y confirmación obligatoria
- ✅ Validación de complejidad (8-16 caracteres, mayúsculas/minúsculas, número y símbolo)
- ✅ Indicador visual de fuerza y opción para mostrar/ocultar contraseñas
- ✅ Nuevo endpoint `/api/auth/login` que valida credenciales en el backend con BCrypt

### 7. Recuperación de contraseña
- ✅ Link de “¿Olvidaste tu contraseña?” exclusivo para clientes
- ✅ Flujo guiado: solicitar enlace, correo con botón y restablecimiento seguro desde el frontend
- ✅ Contraseña nueva con las mismas reglas y feedback visual del registro
- ✅ Endpoints `/api/password/recuperar` y `/api/password/restablecer`

## Instrucciones de Ejecución

### 0. Iniciar MailDev (para pruebas de correo)
```bash
npx maildev --smtp 1025 --web 1080
# también puedes usar: docker run -p 1080:1080 -p 1025:1025 maildev/maildev
```
Panel web disponible en: `http://localhost:1080`

### 1. Configurar Base de Datos
```sql
-- Crear base de datos MySQL
CREATE DATABASE segura_tu_auto;
```

### 2. Ejecutar migración (recomendado)
```bash
# Desde la raíz del proyecto
./migrate.sh        # Linux / macOS
# o
migrate.bat         # Windows
```
Esto limpia las tablas, actualiza columnas (contraseñas/verificación) y genera datos con la contraseña `Segura123!`.

### 3. Iniciar Backend
```bash
# Desde la raíz del proyecto
./gradlew bootRun
```
El backend estará disponible en: `http://localhost:8080`

### 4. Iniciar Frontend
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
- `GET /api/verificacion/confirmar?token=xxx` - Confirmar cuenta y redirigir al login
- `POST /api/auth/login` - Autenticar cliente o agente enviando `{ email, password, tipoUsuario }`

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
5. **Verificación**: El login de clientes requiere confirmar el correo recibido en MailDev
6. **Contraseñas**: Las contraseñas se almacenan cifradas con BCrypt y deben cumplir con la política descrita
7. **Contraseña por defecto (datos de prueba)**: Los clientes/agentes generados por los scripts usan `Segura123!`
8. **Especialización**: Sistema especializado únicamente en seguros de automóviles

## Próximos Pasos

- [ ] Implementar endpoint para actualizar estado de pólizas
- [ ] Agregar autenticación y autorización
- [ ] Implementar búsqueda y filtros avanzados
- [ ] Agregar validaciones adicionales en el frontend
- [ ] Implementar notificaciones en tiempo real 

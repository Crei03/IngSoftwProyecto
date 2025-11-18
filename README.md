# Sistema de Gestión de Pólizas de Auto - SeguraTuAuto

## Descripción

Sistema web para la gestión automatizada de clientes, agentes y pólizas de seguros de automóviles con autenticación y verificación de correo. El proyecto incluye un backend en Spring Boot y un frontend en Vue.js.

## Estructura del Proyecto

```
seguratuauto/
├── src/                    # Backend Spring Boot
│   └── main/java/com/seguratuauto/
│       ├── api/           # Controladores REST
│       ├── config/        # Configuración de seguridad
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

### 🔐 Autenticación y Seguridad
- ✅ Sistema de login con validación de credenciales
- ✅ Recuperación de contraseña
- ✅ Verificación de correo electrónico
- ✅ Configuración de seguridad con Spring Security
- ✅ Encriptación de contraseñas con BCrypt

### Landing Page - Página Principal
- ✅ Diseño moderno y atractivo para la aseguradora
- ✅ Formulario de registro de clientes con validación
- ✅ Sistema de login con validación de clientes existentes
- ✅ Perfil de usuario con información del cliente
- ✅ Integración completa con la base de datos
- ✅ Diseño responsivo y temas claro/oscuro

### RF-001: Gestión de Clientes y Agentes
- ✅ Registro automático de clientes
- ✅ Dashboard de agentes con estadísticas
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

## Cambios Realizados en esta Versión

### 1. Sistema de Autenticación
- Nuevos DTOs: `LoginRequest`, `LoginResponse`, `PasswordRecoveryRequest`, `PasswordResetConfirmRequest`
- Controladores: `AuthController`, `PasswordController`, `VerificacionController`
- Servicios: `EmailVerificationService`, `PasswordResetService`
- Configuración: `SecurityConfig` con Spring Security

### 2. Dashboard de Agentes
- ✅ Componente `AgenteDashboard.vue` con estadísticas
- ✅ Tabla de agentes activos
- ✅ Métricas de pólizas y agentes
- ✅ Interfaz responsiva y moderna

### 3. Actualización de Dependencias
- Spring Security para autenticación
- BCrypt para encriptación de contraseñas
- Validación mejorada de DTOs

### 4. Configuración de Email (opcional)
- Soporte para SMTP/MailDev
- Verificación de correos de usuarios
- Recuperación de contraseñas por email

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

### 4. Configurar Email (Opcional)
Si deseas usar MailDev en local:
```bash
npx maildev
```
El servidor de email estará en: `http://localhost:1080`

## Endpoints de la API

### Autenticación
- `POST /api/auth/login` - Autenticación de usuarios
- `POST /api/auth/logout` - Cerrar sesión
- `GET /api/auth/verificar-sesion` - Verificar sesión activa

### Recuperación de Contraseña
- `POST /api/password/recovery` - Solicitar recuperación
- `POST /api/password/reset-confirm` - Confirmar restablecimiento
- `GET /api/password/validar-token/{token}` - Validar token

### Verificación de Correo
- `GET /api/verificacion/verificar-email/{token}` - Verificar email
- `POST /api/verificacion/reenviar-email/{email}` - Reenviar verificación
- `GET /api/verificacion/estado/{email}` - Estado de verificación

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
- **Framework**: Spring Boot 3 con Spring Security
- **Base de Datos**: MySQL 8
- **ORM**: Hibernate/JPA
- **API**: REST con CORS habilitado
- **Autenticación**: JWT y sesiones

## Notas Importantes

1. **Base de Datos**: Asegúrate de que MySQL esté corriendo y la base de datos `segura_tu_auto` exista
2. **Puertos**: El backend usa el puerto 8080 por defecto
3. **CORS**: Configurado para permitir conexiones desde el frontend
4. **Datos**: Los componentes ahora muestran datos reales de la base de datos
5. **Especialización**: Sistema especializado únicamente en seguros de automóviles
6. **Seguridad**: Las contraseñas se almacenan encriptadas con BCrypt

## Próximos Pasos

- [ ] Implementar autenticación JWT completa
- [ ] Agregar sistema de roles y permisos
- [ ] Implementar confirmación de email real
- [ ] Agregar notificaciones en tiempo real
- [ ] Implementar búsqueda y filtros avanzados
- [ ] Agregar validaciones adicionales en el frontend
- [ ] Implementar paginación en listados

## Soporte

Para reportar bugs o sugerencias, contacta al equipo de desarrollo.

<template>
  <div :class="['login-container', themeClass]">
    <!-- Header con logo -->
    <header class="login-header">
      <div class="header-content">
        <div class="logo-section">
          <h1 class="logo-title">SeguraTuAuto</h1>
          <p class="logo-subtitle">Sistema de Gestión de Seguros</p>
        </div>
      </div>
    </header>

    <!-- Contenido principal de login -->
    <main class="login-main">
      <div class="login-card">
        <div class="login-card-header">
          <h2 class="login-title">Iniciar Sesión</h2>
          <p class="login-subtitle">Accede a tu cuenta para gestionar tus seguros</p>
        </div>

        <form @submit.prevent="handleLogin" class="login-form">
          <!-- Selector de tipo de usuario -->
          <div class="user-type-selector">
            <div class="selector-title">Tipo de Usuario:</div>
            <div class="radio-group">
              <label class="radio-option" :class="{ active: userType === 'cliente' }">
                <input
                  type="radio"
                  v-model="userType"
                  value="cliente"
                  name="userType"
                />
                <span class="radio-custom"></span>
                <span class="radio-label">Cliente</span>
              </label>
              <label class="radio-option" :class="{ active: userType === 'agente' }">
                <input
                  type="radio"
                  v-model="userType"
                  value="agente"
                  name="userType"
                />
                <span class="radio-custom"></span>
                <span class="radio-label">Agente / Administrador</span>
              </label>
            </div>
          </div>

          <!-- Campos de login -->
          <div class="form-group">
            <label class="form-label">Email</label>
            <input
              v-model="loginData.email"
              type="email"
              class="form-input"
              placeholder="Ingresa tu email"
              required
              :class="{ 'error': hasError }"
            />
          </div>

          <div class="form-group">
            <label class="form-label">
              {{ userType === 'agente' ? 'Código de Agente' : 'Teléfono' }}
            </label>
            <input
              v-model="loginData.credential"
              :type="userType === 'agente' ? 'text' : 'tel'"
              class="form-input"
              :placeholder="userType === 'agente' ? 'Ej: AG0001' : 'Ej: +1234567890'"
              required
              :class="{ 'error': hasError }"
            />
          </div>

          <!-- Mensaje de error -->
          <div v-if="errorMessage" class="error-message">
            {{ errorMessage }}
          </div>

          <!-- Botón de login -->
          <button
            type="submit"
            class="login-button"
            :disabled="loading"
          >
            {{ loading ? 'Verificando...' : 'Iniciar Sesión' }}
          </button>
        </form>

        <!-- Enlace de registro para clientes -->
        <div v-if="userType === 'cliente'" class="register-link">
          <p>¿No tienes cuenta? 
            <button @click="showRegister = true" type="button" class="link-button">
              Regístrate aquí
            </button>
          </p>
        </div>
      </div>
    </main>

    <!-- Modal de registro -->
    <div v-if="showRegister" class="modal-overlay">
      <div class="modal-content">
        <div class="modal-header">
          <h3>Registro de Cliente</h3>
          <button @click="showRegister = false" class="close-button">
            <X class="close-icon" />
          </button>
        </div>
        
        <form @submit.prevent="handleRegister" class="register-form">
          <div class="form-group">
            <label class="form-label">Nombre Completo *</label>
            <input
              v-model="registerData.nombre"
              type="text"
              class="form-input"
              placeholder="Tu nombre completo"
              required
            />
          </div>
          
          <div class="form-group">
            <label class="form-label">Email *</label>
            <input
              v-model="registerData.email"
              type="email"
              class="form-input"
              placeholder="tu@email.com"
              required
            />
          </div>
          
          <div class="form-group">
            <label class="form-label">Teléfono *</label>
            <input
              v-model="registerData.telefono"
              type="tel"
              class="form-input"
              placeholder="+1234567890"
              required
            />
          </div>
          
          <button type="submit" class="submit-button" :disabled="loading">
            {{ loading ? 'Registrando...' : 'Registrarse' }}
          </button>
        </form>
      </div>
    </div>

    <!-- Footer -->
    <footer class="login-footer">
      <div class="footer-content">
        <p>&copy; 2025 SeguraTuAuto. Todos los derechos reservados.</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { X } from 'lucide-vue-next'
import apiService from '../services/apiService.js'

const props = defineProps({
  isDark: Boolean
})

const emit = defineEmits(['loginSuccess'])

// Computed properties
const themeClass = computed(() => {
  return props.isDark ? 'theme-dark' : 'theme-light'
})

// Estado de la aplicación
const loading = ref(false)
const hasError = ref(false)
const errorMessage = ref('')
const showRegister = ref(false)
const userType = ref('cliente') // 'cliente' o 'agente'

// Datos del formulario de login
const loginData = reactive({
  email: '',
  credential: '' // telefono para cliente, codigo para agente
})

// Datos del formulario de registro
const registerData = reactive({
  nombre: '',
  email: '',
  telefono: ''
})

// Función principal de login
const handleLogin = async () => {
  loading.value = true
  hasError.value = false
  errorMessage.value = ''
  
  try {
    let userData = null
    
    if (userType.value === 'cliente') {
      // Login de cliente (email + telefono)
      userData = await loginCliente()
    } else {
      // Login de agente (email + codigo)
      userData = await loginAgente()
    }
    
    if (userData) {
      // Login exitoso
      const userWithRole = {
        ...userData,
        role: userType.value
      }
      
      localStorage.setItem('currentUser', JSON.stringify(userWithRole))
      emit('loginSuccess', userWithRole)
    }
    
  } catch (error) {
    console.error('Error en login:', error)
    hasError.value = true
    errorMessage.value = 'Error al conectar con el servidor'
  } finally {
    loading.value = false
  }
}

// Login para clientes
const loginCliente = async () => {
  const clientes = await apiService.getClientes()
  
  const cliente = clientes.find(c => 
    c.email.toLowerCase() === loginData.email.toLowerCase() &&
    c.telefono === loginData.credential
  )
  
  if (cliente) {
    return cliente
  } else {
    hasError.value = true
    errorMessage.value = 'Email o teléfono incorrectos'
    return null
  }
}

// Login para agentes
const loginAgente = async () => {
  const agentes = await apiService.getAgentes()
  
  const agente = agentes.find(a => 
    a.email.toLowerCase() === loginData.email.toLowerCase() &&
    a.codigo === loginData.credential
  )
  
  if (agente) {
    return agente
  } else {
    hasError.value = true
    errorMessage.value = 'Email o código de agente incorrectos'
    return null
  }
}

// Registro de cliente
const handleRegister = async () => {
  loading.value = true
  
  try {
    const clienteCreado = await apiService.crearCliente(registerData)
    
    // Limpiar formulario
    Object.keys(registerData).forEach(key => {
      registerData[key] = ''
    })
    
    showRegister.value = false
    
    // Auto-login después del registro
    const userWithRole = {
      ...clienteCreado,
      role: 'cliente'
    }
    
    localStorage.setItem('currentUser', JSON.stringify(userWithRole))
    emit('loginSuccess', userWithRole)
    
  } catch (error) {
    console.error('Error en registro:', error)
    alert('Error al registrar: ' + error.message)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/* Contenedor principal */
.login-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  transition: background-color 0.3s ease, color 0.3s ease;
}

/* Tema claro */
.theme-light .login-container {
  background: linear-gradient(135deg, #fef7ed 0%, #fed7aa 100%);
  color: #9a3412;
}

/* Tema oscuro */
.theme-dark .login-container {
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
  color: #e2e8f0;
}

/* Header */
.login-header {
  padding: 2rem 0;
  text-align: center;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
}

.logo-title {
  font-size: 3rem;
  font-weight: bold;
  margin: 0 0 0.5rem 0;
  transition: all 0.3s ease;
}

.logo-subtitle {
  font-size: 1.2rem;
  opacity: 0.8;
  margin: 0;
}

/* Tema claro */
.theme-light .logo-title {
  background: linear-gradient(135deg, #fb923c 0%, #f97316 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* Tema oscuro */
.theme-dark .logo-title {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* Main content */
.login-main {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;
}

.login-card {
  width: 100%;
  max-width: 500px;
  padding: 3rem;
  border-radius: 1rem;
  backdrop-filter: blur(10px);
  transition: all 0.3s ease;
}

/* Tema claro */
.theme-light .login-card {
  background: rgba(254, 247, 237, 0.9);
  border: 1px solid rgba(253, 186, 116, 0.5);
  box-shadow: 0 20px 40px rgba(251, 146, 60, 0.1);
}

/* Tema oscuro */
.theme-dark .login-card {
  background: rgba(30, 41, 59, 0.9);
  border: 1px solid rgba(71, 85, 105, 0.5);
  box-shadow: 0 20px 40px rgba(59, 130, 246, 0.1);
}

.login-card-header {
  text-align: center;
  margin-bottom: 2rem;
}

.login-title {
  font-size: 2rem;
  font-weight: 600;
  margin: 0 0 0.5rem 0;
}

.login-subtitle {
  opacity: 0.8;
  margin: 0;
}

/* Selector de tipo de usuario */
.user-type-selector {
  margin-bottom: 2rem;
}

.selector-title {
  font-weight: 500;
  margin-bottom: 1rem;
  opacity: 0.9;
}

.radio-group {
  display: flex;
  gap: 1rem;
}

.radio-option {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 1rem;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid;
}

.radio-option input[type="radio"] {
  display: none;
}

.radio-custom {
  width: 1rem;
  height: 1rem;
  border-radius: 50%;
  border: 2px solid;
  position: relative;
  transition: all 0.3s ease;
}

.radio-option.active .radio-custom::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 0.5rem;
  height: 0.5rem;
  border-radius: 50%;
}

/* Tema claro */
.theme-light .radio-option {
  border-color: rgba(253, 186, 116, 0.3);
  background: rgba(254, 247, 237, 0.3);
}

.theme-light .radio-option:hover,
.theme-light .radio-option.active {
  border-color: #fb923c;
  background: rgba(251, 146, 60, 0.1);
}

.theme-light .radio-custom {
  border-color: #fb923c;
}

.theme-light .radio-option.active .radio-custom::after {
  background: #fb923c;
}

/* Tema oscuro */
.theme-dark .radio-option {
  border-color: rgba(71, 85, 105, 0.3);
  background: rgba(51, 65, 85, 0.3);
}

.theme-dark .radio-option:hover,
.theme-dark .radio-option.active {
  border-color: #3b82f6;
  background: rgba(59, 130, 246, 0.1);
}

.theme-dark .radio-custom {
  border-color: #3b82f6;
}

.theme-dark .radio-option.active .radio-custom::after {
  background: #3b82f6;
}

/* Formulario */
.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-label {
  font-size: 1rem;
  font-weight: 500;
  opacity: 0.9;
}

.form-input {
  padding: 1rem;
  border: 1px solid;
  border-radius: 0.5rem;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.form-input:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
}

/* Tema claro */
.theme-light .form-input {
  background: rgba(254, 247, 237, 0.5);
  border-color: rgba(253, 186, 116, 0.5);
  color: #9a3412;
}

.theme-light .form-input::placeholder {
  color: rgba(154, 52, 18, 0.6);
}

.theme-light .form-input:focus {
  border-color: #fb923c;
  box-shadow: 0 0 0 2px rgba(251, 146, 60, 0.1);
  background: rgba(254, 247, 237, 0.8);
}

/* Tema oscuro */
.theme-dark .form-input {
  background: rgba(30, 41, 59, 0.5);
  border-color: rgba(71, 85, 105, 0.5);
  color: #e2e8f0;
}

.theme-dark .form-input::placeholder {
  color: rgba(226, 232, 240, 0.6);
}

.theme-dark .form-input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.1);
  background: rgba(30, 41, 59, 0.8);
}

.form-input.error {
  border-color: #ef4444;
}

.error-message {
  color: #ef4444;
  font-size: 0.875rem;
  text-align: center;
  padding: 0.5rem;
  border-radius: 0.25rem;
  background: rgba(239, 68, 68, 0.1);
}

/* Botones */
.login-button,
.submit-button {
  border: none;
  padding: 1rem 2rem;
  border-radius: 0.5rem;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.login-button:hover:not(:disabled),
.submit-button:hover:not(:disabled) {
  transform: translateY(-2px);
}

/* Tema claro */
.theme-light .login-button,
.theme-light .submit-button {
  background: linear-gradient(135deg, #fb923c 0%, #f97316 100%);
  color: #fef7ed;
  box-shadow: 0 4px 15px rgba(251, 146, 60, 0.3);
}

.theme-light .login-button:hover:not(:disabled),
.theme-light .submit-button:hover:not(:disabled) {
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  box-shadow: 0 6px 20px rgba(251, 146, 60, 0.4);
}

/* Tema oscuro */
.theme-dark .login-button,
.theme-dark .submit-button {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: #e2e8f0;
  box-shadow: 0 4px 15px rgba(59, 130, 246, 0.3);
}

.theme-dark .login-button:hover:not(:disabled),
.theme-dark .submit-button:hover:not(:disabled) {
  background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.4);
}

.login-button:disabled,
.submit-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Enlace de registro */
.register-link {
  text-align: center;
  margin-top: 1.5rem;
  padding-top: 1.5rem;
  border-top: 1px solid;
}

/* Tema claro */
.theme-light .register-link {
  border-top-color: rgba(253, 186, 116, 0.3);
}

/* Tema oscuro */
.theme-dark .register-link {
  border-top-color: rgba(71, 85, 105, 0.3);
}

.link-button {
  background: none;
  border: none;
  color: inherit;
  text-decoration: underline;
  cursor: pointer;
  font-weight: 500;
  transition: opacity 0.3s ease;
}

.link-button:hover {
  opacity: 0.8;
}

/* Modal */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  transition: all 0.3s ease;
}

.modal-content {
  border-radius: 1rem;
  padding: 2rem;
  max-width: 500px;
  width: 90%;
  max-height: 80vh;
  overflow-y: auto;
  transition: all 0.3s ease;
}

/* Tema claro */
.theme-light .modal-overlay {
  background: rgba(0, 0, 0, 0.8);
}

.theme-light .modal-content {
  background: white;
  color: #1f2937;
}

/* Tema oscuro */
.theme-dark .modal-overlay {
  background: rgba(0, 0, 0, 0.9);
}

.theme-dark .modal-content {
  background: #1e293b;
  color: #e2e8f0;
  border: 1px solid rgba(71, 85, 105, 0.5);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.modal-header h3 {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 600;
}

.close-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 0.25rem;
  transition: background-color 0.2s ease;
}

.close-button:hover {
  transition: background-color 0.2s ease;
}

.close-icon {
  width: 1.5rem;
  height: 1.5rem;
  transition: color 0.3s ease;
}

/* Tema claro */
.theme-light .close-button:hover {
  background: #f3f4f6;
}

.theme-light .close-icon {
  color: #6b7280;
}

/* Tema oscuro */
.theme-dark .close-button:hover {
  background: #374151;
}

.theme-dark .close-icon {
  color: #9ca3af;
}

.register-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

/* Footer */
.login-footer {
  padding: 2rem;
  text-align: center;
  opacity: 0.8;
  transition: all 0.3s ease;
}

/* Tema claro */
.theme-light .login-footer {
  border-top: 1px solid rgba(253, 186, 116, 0.3);
}

/* Tema oscuro */
.theme-dark .login-footer {
  border-top: 1px solid rgba(71, 85, 105, 0.3);
}

.footer-content p {
  margin: 0;
}

/* Responsive */
@media (max-width: 768px) {
  .login-card {
    padding: 2rem;
    margin: 1rem;
  }
  
  .logo-title {
    font-size: 2.5rem;
  }
  
  .radio-group {
    flex-direction: column;
  }
  
  .login-header {
    padding: 1rem 0;
  }
}

@media (max-width: 480px) {
  .login-card {
    padding: 1.5rem;
  }
  
  .logo-title {
    font-size: 2rem;
  }
  
  .login-title {
    font-size: 1.5rem;
  }
}
</style>
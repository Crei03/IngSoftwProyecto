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
        
        <div 
          v-if="verificationFeedback.show" 
          :class="['verification-alert', verificationFeedback.type]"
        >
          <component
            :is="verificationFeedback.type === 'success' ? CheckCircle2 : AlertCircle"
            class="alert-icon"
          />
          <span>{{ verificationFeedback.message }}</span>
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

          <div class="form-group password-group">
            <label class="form-label">Contraseña</label>
            <div class="password-field">
              <input
                v-model="loginData.password"
                :type="showLoginPassword ? 'text' : 'password'"
                class="form-input"
                placeholder="Ingresa tu contraseña"
                required
                :class="{ 'error': hasError }"
              />
              <button
                type="button"
                class="toggle-password"
                @click="showLoginPassword = !showLoginPassword"
                :aria-label="showLoginPassword ? 'Ocultar contraseña' : 'Mostrar contraseña'"
              >
                <component :is="showLoginPassword ? EyeOff : Eye" class="toggle-icon" />
              </button>
            </div>
          </div>
          
          <div
            v-if="userType === 'cliente'"
            class="forgot-password-link"
          >
            <button type="button" @click="abrirRecuperacionPassword">
              ¿Olvidaste tu contraseña?
            </button>
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
            <button @click="abrirModalRegistro" type="button" class="link-button">
              Regístrate aquí
            </button>
          </p>
        </div>
      </div>
    </main>

    <!-- Modal de registro -->
    <div v-if="showRegister" class="modal-overlay" @click.self="cerrarModalRegistro">
      <div class="modal-content">
        <div class="modal-header">
          <h3>Registro de Cliente</h3>
          <button @click="cerrarModalRegistro" class="close-button">
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
          
          <div class="form-group password-group">
            <div class="password-label-row">
              <label class="form-label">Contraseña *</label>
              <span class="password-strength" :class="passwordStrength.className">
                {{ passwordStrength.label }}
              </span>
            </div>
            <div class="password-field">
              <input
                v-model="registerData.password"
                :type="showRegisterPassword ? 'text' : 'password'"
                class="form-input"
                placeholder="Ingresa una contraseña segura"
                required
              />
              <button
                type="button"
                class="toggle-password"
                @click="showRegisterPassword = !showRegisterPassword"
                :aria-label="showRegisterPassword ? 'Ocultar contraseña' : 'Mostrar contraseña'"
              >
                <component :is="showRegisterPassword ? EyeOff : Eye" class="toggle-icon" />
              </button>
            </div>
          </div>
          
          <div class="form-group password-group">
            <label class="form-label">Confirmar contraseña *</label>
            <div class="password-field">
              <input
                v-model="registerData.confirmPassword"
                :type="showRegisterConfirmPassword ? 'text' : 'password'"
                class="form-input"
                placeholder="Repite tu contraseña"
                required
              />
              <button
                type="button"
                class="toggle-password"
                @click="showRegisterConfirmPassword = !showRegisterConfirmPassword"
                :aria-label="showRegisterConfirmPassword ? 'Ocultar contraseña' : 'Mostrar contraseña'"
              >
                <component :is="showRegisterConfirmPassword ? EyeOff : Eye" class="toggle-icon" />
              </button>
            </div>
          </div>
          
          <div class="password-requirements">
            <div class="requirements-title">
              <ShieldCheck class="requirements-icon" />
              <span>Tu contraseña debe incluir:</span>
            </div>
            <ul>
              <li
                v-for="req in passwordRequirements"
                :key="req.label"
                :class="{ met: req.met }"
              >
                {{ req.label }}
              </li>
            </ul>
          </div>
          
          <div v-if="registerError" class="register-error">
            <AlertCircle class="register-error-icon" />
            <span>{{ registerError }}</span>
          </div>
          
          <button type="submit" class="submit-button" :disabled="loading">
            {{ loading ? 'Registrando...' : 'Registrarse' }}
          </button>
        </form>
      </div>
    </div>
    
    <!-- Modal de verificación -->
    <div v-if="showVerificationModal" class="modal-overlay" @click.self="cerrarModalVerificacion">
      <div class="modal-content verification-modal">
        <div class="verification-header">
          <MailCheck class="verification-icon" />
          <h3>Verifica tu correo</h3>
        </div>
        <p class="verification-message">
          Enviamos un correo a <strong>{{ verificationEmail }}</strong> con un enlace para confirmar tu cuenta. 
          Una vez verificada, podrás iniciar sesión.
        </p>
        <p class="verification-message secondary">
          Si no ves el mensaje, revisa tu carpeta de spam o solicita un nuevo registro.
        </p>
        <button type="button" class="submit-button" @click="cerrarModalVerificacion">
          Entendido
        </button>
      </div>
    </div>
    
    <!-- Modal de recuperación -->
    <div
      v-if="showRecoveryModal"
      class="modal-overlay"
      @click.self="cerrarRecuperacionPassword"
    >
      <div class="modal-content recovery-modal">
        <div class="modal-header">
          <div class="recovery-header">
            <KeySquare class="recovery-icon" />
            <div>
              <h3 v-if="recoveryStep === 'email'">Recuperar contraseña</h3>
              <h3 v-else-if="recoveryStep === 'sent'">Revisa tu correo</h3>
              <h3 v-else-if="recoveryStep === 'reset'">Crea una nueva contraseña</h3>
              <h3 v-else>Contraseña actualizada</h3>
              <p v-if="recoveryStep === 'email'">
                Ingresa tu email y te enviaremos un enlace seguro.
              </p>
              <p v-else-if="recoveryStep === 'sent'">
                Sigue las instrucciones del correo para continuar.
              </p>
              <p v-else-if="recoveryStep === 'reset'">
                Usa el código del correo para crear tu nueva contraseña.
              </p>
              <p v-else>
                Ya puedes iniciar sesión con tu nueva contraseña.
              </p>
            </div>
          </div>
          <button @click="cerrarRecuperacionPassword" class="close-button">
            <X class="close-icon" />
          </button>
        </div>
        
        <div v-if="recoveryError" class="register-error">
          <AlertCircle class="register-error-icon" />
          <span>{{ recoveryError }}</span>
        </div>
        
        <div v-if="recoveryStep === 'email'" class="recovery-body">
          <form @submit.prevent="solicitarRecuperacionPassword" class="register-form">
            <div class="form-group">
              <label class="form-label">Email registrado *</label>
              <input
                v-model="recoveryForm.email"
                type="email"
                class="form-input"
                placeholder="tu@email.com"
                required
              />
            </div>
            <button type="submit" class="submit-button">
              Enviar instrucciones
            </button>
          </form>
        </div>
        
        <div v-else-if="recoveryStep === 'sent'" class="recovery-body">
          <div class="info-box">
            <p>{{ recoveryMessage || 'Te enviamos un correo con los pasos a seguir.' }}</p>
            <p class="info-secondary">
              Si no lo ves en unos minutos, revisa tu carpeta de spam o vuelve a intentarlo.
            </p>
          </div>
          <div class="recovery-actions">
            <button class="btn-secondary" @click="mostrarFormularioReset(recoveryForm.token, recoveryForm.email)">
              Ya tengo el código
            </button>
            <button class="submit-button" @click="cerrarRecuperacionPassword">
              Volver al login
            </button>
          </div>
        </div>
        
        <div v-else-if="recoveryStep === 'reset'" class="recovery-body">
          <form @submit.prevent="restablecerPassword" class="register-form">
            <div class="form-group">
              <label class="form-label">Token del correo *</label>
              <input
                v-model="recoveryForm.token"
                type="text"
                class="form-input"
                placeholder="Pega aquí el código"
                required
              />
            </div>
            
            <div class="form-group password-group">
              <div class="password-label-row">
                <label class="form-label">Nueva contraseña *</label>
                <span class="password-strength" :class="recoveryPasswordStrength.className">
                  {{ recoveryPasswordStrength.label }}
                </span>
              </div>
              <div class="password-field">
                <input
                  v-model="recoveryForm.password"
                  :type="showRecoveryPassword ? 'text' : 'password'"
                  class="form-input"
                  placeholder="Ingresa tu nueva contraseña"
                  required
                />
                <button
                  type="button"
                  class="toggle-password"
                  @click="showRecoveryPassword = !showRecoveryPassword"
                  :aria-label="showRecoveryPassword ? 'Ocultar contraseña' : 'Mostrar contraseña'"
                >
                  <component :is="showRecoveryPassword ? EyeOff : Eye" class="toggle-icon" />
                </button>
              </div>
            </div>
            
            <div class="form-group password-group">
              <label class="form-label">Confirmar contraseña *</label>
              <div class="password-field">
                <input
                  v-model="recoveryForm.confirmPassword"
                  :type="showRecoveryConfirmPassword ? 'text' : 'password'"
                  class="form-input"
                  placeholder="Repite la contraseña"
                  required
                />
                <button
                  type="button"
                  class="toggle-password"
                  @click="showRecoveryConfirmPassword = !showRecoveryConfirmPassword"
                  :aria-label="showRecoveryConfirmPassword ? 'Ocultar contraseña' : 'Mostrar contraseña'"
                >
                  <component :is="showRecoveryConfirmPassword ? EyeOff : Eye" class="toggle-icon" />
                </button>
              </div>
            </div>
            
            <div class="password-requirements">
              <div class="requirements-title">
                <ShieldCheck class="requirements-icon" />
                <span>Debe incluir:</span>
              </div>
              <ul>
                <li
                  v-for="req in recoveryPasswordRequirements"
                  :key="req.label"
                  :class="{ met: req.met }"
                >
                  {{ req.label }}
                </li>
              </ul>
            </div>
            
            <button type="submit" class="submit-button">
              Restablecer contraseña
            </button>
          </form>
        </div>
        
        <div v-else class="recovery-body">
          <div class="info-box success">
            <CheckCircle2 class="success-icon" />
            <div>
              <p>{{ recoveryMessage || 'Tu contraseña fue actualizada correctamente.' }}</p>
              <p class="info-secondary">
                Ahora puedes iniciar sesión con tus nuevas credenciales.
              </p>
            </div>
          </div>
          <button class="submit-button" @click="finalizarRecuperacion">
            Volver al login
          </button>
        </div>
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
import { ref, reactive, computed, onMounted } from 'vue'
import { X, MailCheck, CheckCircle2, AlertCircle, Eye, EyeOff, ShieldCheck, KeySquare } from 'lucide-vue-next'
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
const showVerificationModal = ref(false)
const verificationEmail = ref('')
const verificationFeedback = reactive({
  show: false,
  type: 'success',
  message: ''
})
const registerError = ref('')
const userType = ref('cliente') // 'cliente' o 'agente'
const showLoginPassword = ref(false)
const showRegisterPassword = ref(false)
const showRegisterConfirmPassword = ref(false)
const showRecoveryModal = ref(false)
const recoveryStep = ref('email') // email | sent | reset | success
const recoveryError = ref('')
const recoveryMessage = ref('')
const showRecoveryPassword = ref(false)
const showRecoveryConfirmPassword = ref(false)

// Datos del formulario de login
const loginData = reactive({
  email: '',
  password: ''
})

// Datos del formulario de registro
const registerData = reactive({
  nombre: '',
  email: '',
  telefono: '',
  password: '',
  confirmPassword: ''
})

const recoveryForm = reactive({
  email: '',
  token: '',
  password: '',
  confirmPassword: ''
})

const buildPasswordRequirements = (password = '') => {
  return [
    {
      label: '8-16 caracteres',
      met: password.length >= 8 && password.length <= 16
    },
    {
      label: 'Incluye mayúsculas y minúsculas',
      met: /[a-z]/.test(password) && /[A-Z]/.test(password)
    },
    {
      label: 'Incluye números',
      met: /\d/.test(password)
    },
    {
      label: 'Incluye un carácter especial',
      met: /[@$!%*?&.#^()_+\-=\/]/.test(password)
    }
  ]
}

const getPasswordStrength = password => {
  const pwd = password || ''
  if (!pwd) {
    return { label: 'Muy débil', className: 'strength-very-weak' }
  }
  
  let score = 0
  if (pwd.length >= 8) score++
  if (pwd.length >= 12) score++
  if (/[a-z]/.test(pwd) && /[A-Z]/.test(pwd)) score++
  if (/\d/.test(pwd)) score++
  if (/[@$!%*?&.#^()_+\-=\/]/.test(pwd)) score++
  
  if (score <= 2) return { label: 'Muy débil', className: 'strength-very-weak' }
  if (score === 3) return { label: 'Débil', className: 'strength-weak' }
  if (score === 4) return { label: 'Buena', className: 'strength-good' }
  return { label: 'Muy fuerte', className: 'strength-strong' }
}

const passwordRequirements = computed(() => buildPasswordRequirements(registerData.password))
const passwordStrength = computed(() => getPasswordStrength(registerData.password))

const recoveryPasswordRequirements = computed(() => buildPasswordRequirements(recoveryForm.password))
const recoveryPasswordStrength = computed(() => getPasswordStrength(recoveryForm.password))

const resetRegisterForm = () => {
  registerData.nombre = ''
  registerData.email = ''
  registerData.telefono = ''
  registerData.password = ''
  registerData.confirmPassword = ''
}

const abrirModalRegistro = () => {
  registerError.value = ''
  showRegister.value = true
}

const cerrarModalRegistro = () => {
  showRegister.value = false
  registerError.value = ''
}

const cerrarModalVerificacion = () => {
  showVerificationModal.value = false
}

const abrirRecuperacionPassword = () => {
  recoveryError.value = ''
  recoveryMessage.value = ''
  recoveryStep.value = 'email'
  recoveryForm.email = registerData.email || loginData.email
  showRecoveryModal.value = true
}

const cerrarRecuperacionPassword = () => {
  showRecoveryModal.value = false
  recoveryStep.value = 'email'
  recoveryForm.email = ''
  recoveryForm.token = ''
  recoveryForm.password = ''
  recoveryForm.confirmPassword = ''
  recoveryError.value = ''
  recoveryMessage.value = ''
  showRecoveryPassword.value = false
  showRecoveryConfirmPassword.value = false
}

const mostrarFormularioReset = (token, email = '') => {
  recoveryStep.value = 'reset'
  recoveryForm.token = token || ''
  recoveryForm.email = email || recoveryForm.email
  recoveryForm.password = ''
  recoveryForm.confirmPassword = ''
  showRecoveryPassword.value = false
  showRecoveryConfirmPassword.value = false
  showRecoveryModal.value = true
}

onMounted(() => {
  const params = new URLSearchParams(window.location.search)
  const status = params.get('verificacion')
  const mensaje = params.get('mensaje')
  const resetToken = params.get('resetToken')
  const emailParam = params.get('email')
  
  if (status) {
    verificationFeedback.show = true
    verificationFeedback.type = status === 'success' ? 'success' : 'error'
    verificationFeedback.message = mensaje
      ? mensaje
      : status === 'success'
        ? 'Cuenta verificada correctamente. Inicia sesión para continuar.'
        : 'No pudimos verificar tu cuenta. Solicita un nuevo registro.'
    
    params.delete('verificacion')
    params.delete('mensaje')
    const newQuery = params.toString()
    const newUrl = `${window.location.pathname}${newQuery ? `?${newQuery}` : ''}${window.location.hash || ''}`
    window.history.replaceState({}, '', newUrl)
  }
  
  if (resetToken) {
    mostrarFormularioReset(resetToken, emailParam || '')
    params.delete('resetToken')
    params.delete('email')
    const newQuery = params.toString()
    const newUrl = `${window.location.pathname}${newQuery ? `?${newQuery}` : ''}${window.location.hash || ''}`
    window.history.replaceState({}, '', newUrl)
  }
})

// Función principal de login
const handleLogin = async () => {
  loading.value = true
  hasError.value = false
  errorMessage.value = ''
  
  try {
    let userData = null
    
    if (userType.value === 'cliente') {
      userData = await loginCliente()
    } else {
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
    errorMessage.value = error.message || 'Error al iniciar sesión'
  } finally {
    loading.value = false
  }
}

// Login para clientes
const loginCliente = async () => {
  const result = await apiService.loginCliente({
    email: loginData.email,
    password: loginData.password
  })
  const cliente = result?.cliente
  if (!cliente) {
    hasError.value = true
    errorMessage.value = 'Credenciales inválidas'
    return null
  }
  return cliente
}

// Login para agentes
const loginAgente = async () => {
  const result = await apiService.loginAgente({
    email: loginData.email,
    password: loginData.password
  })
  const agente = result?.agente
  if (!agente) {
    hasError.value = true
    errorMessage.value = 'Credenciales inválidas'
    return null
  }
  return agente
}

// Registro de cliente
const handleRegister = async () => {
  loading.value = true
  registerError.value = ''
  
  try {
    if (registerData.password !== registerData.confirmPassword) {
      registerError.value = 'Las contraseñas no coinciden.'
      loading.value = false
      return
    }
    
    const payload = {
      nombre: registerData.nombre.trim(),
      email: registerData.email.trim(),
      telefono: registerData.telefono.trim(),
      password: registerData.password.trim()
    }
    
    if (!payload.nombre || !payload.email || !payload.telefono) {
      registerError.value = 'Por favor completa todos los campos obligatorios.'
      return
    }
    
    const clienteCreado = await apiService.crearCliente(payload)
    
    resetRegisterForm()
    cerrarModalRegistro()
    
    verificationEmail.value = clienteCreado?.email || payload.email
    showVerificationModal.value = true
    
  } catch (error) {
    console.error('Error en registro:', error)
    registerError.value = error.message || 'Error al registrar. Inténtalo nuevamente.'
  } finally {
    loading.value = false
  }
}

const solicitarRecuperacionPassword = async () => {
  recoveryError.value = ''
  recoveryMessage.value = ''
  
  if (!recoveryForm.email.trim()) {
    recoveryError.value = 'Ingresa tu email registrado.'
    return
  }
  
  try {
    await apiService.solicitarRecuperacionPassword(recoveryForm.email.trim())
    recoveryMessage.value = 'Te enviamos un correo con un enlace para restablecer tu contraseña.'
    recoveryStep.value = 'sent'
  } catch (error) {
    recoveryError.value = error.message || 'No pudimos procesar tu solicitud.'
  }
}

const restablecerPassword = async () => {
  recoveryError.value = ''
  recoveryMessage.value = ''
  
  if (!recoveryForm.token) {
    recoveryError.value = 'El token es obligatorio.'
    return
  }
  
  if (recoveryForm.password !== recoveryForm.confirmPassword) {
    recoveryError.value = 'Las contraseñas no coinciden.'
    return
  }
  
  const requisitosOk = recoveryPasswordRequirements.value.every(req => req.met)
  if (!requisitosOk) {
    recoveryError.value = 'La contraseña no cumple con los requisitos.'
    return
  }
  
  try {
    await apiService.restablecerPassword({
      token: recoveryForm.token,
      password: recoveryForm.password.trim()
    })
    recoveryStep.value = 'success'
    recoveryMessage.value = 'Listo, tu contraseña ha sido actualizada.'
  } catch (error) {
    recoveryError.value = error.message || 'No pudimos restablecer la contraseña.'
  }
}

const finalizarRecuperacion = () => {
  cerrarRecuperacionPassword()
  showRegister.value = false
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

.password-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.password-field {
  position: relative;
  display: flex;
  align-items: center;
}

.toggle-password {
  position: absolute;
  right: 0.75rem;
  background: none;
  border: none;
  cursor: pointer;
  color: inherit;
  padding: 0.25rem;
}

.toggle-icon {
  width: 1.25rem;
  height: 1.25rem;
}

.password-label-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.password-strength {
  font-size: 0.85rem;
  font-weight: 600;
}

.strength-very-weak {
  color: #dc2626;
}

.strength-weak {
  color: #f97316;
}

.strength-good {
  color: #fbbf24;
}

.strength-strong {
  color: #22c55e;
}

.password-requirements {
  background: rgba(148, 163, 184, 0.15);
  border-radius: 0.75rem;
  padding: 1rem 1.25rem;
  font-size: 0.9rem;
}

.requirements-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
}

.requirements-icon {
  width: 1rem;
  height: 1rem;
}

.password-requirements ul {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.password-requirements li {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  color: #475569;
  position: relative;
  padding-left: 1rem;
}

.password-requirements li::before {
  content: '•';
  position: absolute;
  left: 0;
  color: #f97316;
}

.password-requirements li.met {
  color: #16a34a;
}

.password-requirements li.met::before {
  color: #16a34a;
}

.password-field .form-input {
  width: 100%;
  padding-right: 2.5rem;
}

.theme-dark .toggle-password {
  color: #e2e8f0;
}

.theme-dark .password-requirements {
  background: rgba(51, 65, 85, 0.6);
}

.theme-dark .password-requirements li {
  color: #cbd5f5;
}

.verification-alert {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem 1.25rem;
  border-radius: 0.75rem;
  margin-bottom: 1.5rem;
  font-weight: 500;
}

.verification-alert.success {
  background: rgba(34, 197, 94, 0.15);
  border: 1px solid rgba(34, 197, 94, 0.6);
  color: #166534;
}

.verification-alert.error {
  background: rgba(248, 113, 113, 0.15);
  border: 1px solid rgba(248, 113, 113, 0.5);
  color: #7f1d1d;
}

.theme-dark .verification-alert.success {
  color: #bbf7d0;
  border-color: rgba(34, 197, 94, 0.3);
}

.theme-dark .verification-alert.error {
  color: #fecaca;
  border-color: rgba(248, 113, 113, 0.3);
}

.verification-alert .alert-icon {
  width: 1.5rem;
  height: 1.5rem;
}

.register-error {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1rem;
  border-radius: 0.75rem;
  background: rgba(248, 113, 113, 0.15);
  color: #7f1d1d;
  font-size: 0.9rem;
}

.register-error-icon {
  width: 1.25rem;
  height: 1.25rem;
}

.verification-modal {
  max-width: 420px;
  text-align: center;
  padding: 2.5rem;
}

.verification-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.verification-icon {
  width: 3rem;
  height: 3rem;
  color: #2563eb;
}

.verification-message {
  line-height: 1.6;
  margin-bottom: 1rem;
}

.verification-message.secondary {
  font-size: 0.9rem;
  opacity: 0.8;
}

.forgot-password-link {
  text-align: right;
  margin-top: -0.5rem;
}

.forgot-password-link button {
  background: none;
  border: none;
  color: #3b82f6;
  cursor: pointer;
  font-size: 0.9rem;
  text-decoration: underline;
}

.recovery-modal {
  max-width: 520px;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.recovery-header {
  display: flex;
  gap: 0.75rem;
  align-items: flex-start;
}

.recovery-icon {
  width: 2.2rem;
  height: 2.2rem;
  color: #f97316;
}

.info-box {
  background: rgba(59, 130, 246, 0.1);
  border: 1px solid rgba(59, 130, 246, 0.4);
  border-radius: 0.75rem;
  padding: 1rem;
}

.info-box.success {
  background: rgba(34, 197, 94, 0.12);
  border-color: rgba(34, 197, 94, 0.4);
}

.info-secondary {
  font-size: 0.9rem;
  color: #64748b;
  margin: 0.25rem 0 0;
}

.recovery-actions {
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
  flex-wrap: wrap;
}

.btn-secondary {
  border: none;
  padding: 0.75rem 1.25rem;
  border-radius: 0.75rem;
  background: rgba(148, 163, 184, 0.2);
  color: inherit;
  cursor: pointer;
}

.recovery-body {
  display: flex;
  flex-direction: column;
  gap: 1rem;
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

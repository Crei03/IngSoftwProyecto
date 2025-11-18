<template>
  <div v-if="showModal" class="modal-overlay" @click="cerrarModal">
    <div :class="['modal-content', modalClass]" @click.stop>
      <!-- Header del modal -->
      <div class="modal-header">
        <div class="modal-title-section">
          <Users :class="['modal-icon', iconClass]" />
          <h3 class="modal-title">Crear Nuevo Agente</h3>
        </div>
        <button @click="cerrarModal" :class="['close-button', closeButtonClass]">
          <X class="close-icon" />
        </button>
      </div>

      <!-- Formulario -->
      <form @submit.prevent="crearAgente" class="modal-form">
        <!-- Campo Nombre -->
        <div class="form-group">
          <label for="nombre" class="form-label">Nombre *</label>
          <input
            id="nombre"
            v-model="formulario.nombre"
            type="text"
            required
            maxlength="100"
            :class="['form-input', inputClass]"
            placeholder="Nombre completo del agente"
          />
        </div>

        <!-- Campo Email -->
        <div class="form-group">
          <label for="email" class="form-label">Email</label>
          <input
            id="email"
            v-model="formulario.email"
            type="email"
            maxlength="150"
            :class="['form-input', inputClass]"
            placeholder="email@seguratuauto.com"
          />
        </div>

        <!-- Campo Teléfono -->
        <div class="form-group">
          <label for="telefono" class="form-label">Teléfono</label>
          <input
            id="telefono"
            v-model="formulario.telefono"
            type="tel"
            maxlength="20"
            :class="['form-input', inputClass]"
            placeholder="+34 612 345 678"
          />
        </div>
        
        <!-- Campo Contraseña -->
        <div class="form-group password-group">
          <div class="password-label">
            <label for="password" class="form-label">Contraseña *</label>
            <span class="password-strength" :class="passwordStrength.className">
              {{ passwordStrength.label }}
            </span>
          </div>
          <div class="password-field">
            <input
              id="password"
              v-model="formulario.password"
              :type="mostrarPassword ? 'text' : 'password'"
              maxlength="16"
              minlength="8"
              :class="['form-input', inputClass]"
              placeholder="Contraseña segura"
              required
            />
            <button type="button" class="toggle-password" @click="mostrarPassword = !mostrarPassword">
              <component :is="mostrarPassword ? EyeOff : Eye" class="toggle-icon" />
            </button>
          </div>
        </div>
        
        <!-- Confirmación Contraseña -->
        <div class="form-group password-group">
          <label for="confirmPassword" class="form-label">Confirmar contraseña *</label>
          <div class="password-field">
            <input
              id="confirmPassword"
              v-model="formulario.confirmPassword"
              :type="mostrarConfirmPassword ? 'text' : 'password'"
              maxlength="16"
              minlength="8"
              :class="['form-input', inputClass]"
              placeholder="Repite la contraseña"
              required
            />
            <button type="button" class="toggle-password" @click="mostrarConfirmPassword = !mostrarConfirmPassword">
              <component :is="mostrarConfirmPassword ? EyeOff : Eye" class="toggle-icon" />
            </button>
          </div>
        </div>
        
        <div class="password-requirements" :class="inputClass">
          <div class="requirements-title">
            <ShieldCheck class="requirements-icon" />
            <span>La contraseña debe incluir:</span>
          </div>
          <ul>
            <li v-for="req in passwordRequirements" :key="req.label" :class="{ met: req.met }">
              {{ req.label }}
            </li>
          </ul>
        </div>

        <!-- Mensaje de error -->
        <div v-if="error" class="error-message">
          <p>{{ error }}</p>
        </div>

        <!-- Botones -->
        <div class="form-actions">
          <button
            type="button"
            @click="cerrarModal"
            :class="['btn-secondary', buttonSecondaryClass]"
          >
            Cancelar
          </button>
          <button
            type="submit"
            :disabled="loading || !formulario.nombre.trim() || !formulario.password || !formulario.confirmPassword"
            :class="['btn-primary', buttonPrimaryClass]"
          >
            <div v-if="loading" class="loading-spinner-small"></div>
            {{ loading ? 'Creando...' : 'Crear Agente' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { Users, X, Eye, EyeOff, ShieldCheck } from 'lucide-vue-next'
import apiService from '../services/apiService.js'

const props = defineProps({
  showModal: Boolean,
  isDark: Boolean
})

const emit = defineEmits(['close', 'agente-creado'])

// Estados del formulario
const formulario = ref({
  nombre: '',
  email: '',
  telefono: '',
  password: '',
  confirmPassword: ''
})

const loading = ref(false)
const error = ref(null)
const mostrarPassword = ref(false)
const mostrarConfirmPassword = ref(false)

const passwordRequirements = computed(() => {
  const password = formulario.value.password || ''
  return [
    { label: '8-16 caracteres', met: password.length >= 8 && password.length <= 16 },
    { label: 'Mayúsculas y minúsculas', met: /[a-z]/.test(password) && /[A-Z]/.test(password) },
    { label: 'Al menos un número', met: /\d/.test(password) },
    { label: 'Carácter especial', met: /[@$!%*?&.#^()_+\-=\/]/.test(password) }
  ]
})

const passwordStrength = computed(() => {
  const pwd = formulario.value.password || ''
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
})

// Estilos computados para tema dual
const modalClass = computed(() => {
  return props.isDark ? 'dark-modal' : 'light-modal'
})

const inputClass = computed(() => {
  return props.isDark ? 'dark-input' : 'light-input'
})

const buttonPrimaryClass = computed(() => {
  return props.isDark ? 'dark-button-primary' : 'light-button-primary'
})

const buttonSecondaryClass = computed(() => {
  return props.isDark ? 'dark-button-secondary' : 'light-button-secondary'
})

const closeButtonClass = computed(() => {
  return props.isDark ? 'dark-close-button' : 'light-close-button'
})

const iconClass = computed(() => {
  return props.isDark ? 'dark-icon' : 'light-icon'
})

// Métodos
const cerrarModal = () => {
  limpiarFormulario()
  emit('close')
}

const limpiarFormulario = () => {
  formulario.value = {
    nombre: '',
    email: '',
    telefono: '',
    password: '',
    confirmPassword: ''
  }
  error.value = null
  mostrarPassword.value = false
  mostrarConfirmPassword.value = false
}

const crearAgente = async () => {
  loading.value = true
  error.value = null

  try {
    // Validación básica
    if (!formulario.value.nombre.trim()) {
      throw new Error('El nombre es obligatorio')
    }
    
    if (!formulario.value.password || !formulario.value.confirmPassword) {
      throw new Error('Debes ingresar y confirmar la contraseña')
    }
    
    if (!passwordRequirements.value.every(req => req.met)) {
      throw new Error('La contraseña no cumple con los requisitos mínimos')
    }
    
    if (formulario.value.password !== formulario.value.confirmPassword) {
      throw new Error('Las contraseñas no coinciden')
    }

    // Preparar datos para envío
    const agenteData = {
      nombre: formulario.value.nombre.trim(),
      email: formulario.value.email.trim() || undefined,
      telefono: formulario.value.telefono.trim() || undefined,
      password: formulario.value.password.trim()
    }

    console.log('Creando agente:', agenteData)

    // Llamada a la API
    const nuevoAgente = await apiService.crearAgente(agenteData)

    console.log('Agente creado exitosamente:', nuevoAgente)

    // Emitir evento de éxito
    emit('agente-creado', nuevoAgente)

    // Cerrar modal
    cerrarModal()

    // Mensaje de éxito
    alert('Agente creado exitosamente')

  } catch (err) {
    error.value = 'Error al crear agente: ' + err.message
    console.error('Error creando agente:', err)
  } finally {
    loading.value = false
  }
}

// Limpiar formulario cuando se cierra el modal
watch(() => props.showModal, (newValue) => {
  if (!newValue) {
    limpiarFormulario()
  }
})
</script>

<style scoped>
/* Overlay del modal */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 1rem;
}

/* Contenido del modal */
.modal-content {
  width: 100%;
  max-width: 500px;
  border-radius: 1rem;
  padding: 1.5rem;
  backdrop-filter: blur(8px);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  animation: modalSlideIn 0.3s ease-out;
}

@keyframes modalSlideIn {
  from {
    opacity: 0;
    transform: translateY(-20px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

/* Header del modal */
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid;
}

.modal-title-section {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.modal-icon {
  width: 1.25rem;
  height: 1.25rem;
}

.modal-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0;
}

.close-button {
  padding: 0.5rem;
  border: none;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.close-icon {
  width: 1rem;
  height: 1rem;
}

/* Formulario */
.modal-form {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.password-group {
  position: relative;
}

.password-label {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.password-strength {
  font-size: 0.85rem;
  font-weight: 600;
}

.password-field {
  position: relative;
}

.password-field .form-input {
  width: 100%;
  padding-right: 2.5rem;
}

.toggle-password {
  position: absolute;
  right: 0.5rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  color: inherit;
  display: flex;
  align-items: center;
  justify-content: center;
}

.toggle-icon {
  width: 1.1rem;
  height: 1.1rem;
}

.password-requirements {
  margin-top: 1rem;
  padding: 1rem;
  border-radius: 0.75rem;
  font-size: 0.9rem;
}

.requirements-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
  font-weight: 600;
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
  color: #22c55e;
}

.password-requirements li.met::before {
  color: #22c55e;
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

.form-label {
  font-size: 0.875rem;
  font-weight: 500;
}

.form-input {
  padding: 0.75rem;
  border: 1px solid;
  border-radius: 0.75rem;
  font-size: 0.875rem;
  transition: all 0.2s ease;
  backdrop-filter: blur(8px);
}

.form-input:focus {
  outline: none;
  box-shadow: 0 0 0 2px;
}

.form-input::placeholder {
  opacity: 0.6;
}

/* Mensaje de error */
.error-message {
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.3);
  border-radius: 0.75rem;
  padding: 0.75rem;
  margin: 0.5rem 0;
}

.error-message p {
  color: #dc2626;
  margin: 0;
  font-size: 0.875rem;
}

/* Botones de acción */
.form-actions {
  display: flex;
  gap: 1rem;
  margin-top: 0.5rem;
}

.btn-primary,
.btn-secondary {
  flex: 1;
  padding: 0.75rem 1rem;
  border: none;
  border-radius: 0.75rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.btn-primary:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Spinner de carga pequeño */
.loading-spinner-small {
  width: 1rem;
  height: 1rem;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top: 2px solid currentColor;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Tema claro */
.light-modal {
  background: rgba(254, 247, 237, 0.95);
  border: 1px solid rgba(253, 186, 116, 0.5);
}

.light-modal .modal-header {
  border-color: rgba(253, 186, 116, 0.3);
}

.light-input {
  background: rgba(254, 247, 237, 0.7);
  border-color: rgba(253, 186, 116, 0.5);
  color: #9a3412;
}

.light-input:focus {
  border-color: #fb923c;
  box-shadow: 0 0 0 2px rgba(251, 146, 60, 0.1);
  background: rgba(254, 247, 237, 0.9);
}

.light-button-primary {
  background: linear-gradient(135deg, #fb923c 0%, #f97316 100%);
  color: #fef7ed;
  box-shadow: 0 4px 15px rgba(251, 146, 60, 0.3);
}

.light-button-primary:hover:not(:disabled) {
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  box-shadow: 0 6px 20px rgba(251, 146, 60, 0.4);
}

.light-button-secondary {
  background: rgba(71, 85, 105, 0.8);
  color: #f1f5f9;
  box-shadow: 0 4px 15px rgba(71, 85, 105, 0.3);
}

.light-button-secondary:hover {
  background: rgba(71, 85, 105, 0.9);
  box-shadow: 0 6px 20px rgba(71, 85, 105, 0.4);
}

.light-close-button {
  background: rgba(239, 68, 68, 0.1);
  color: #dc2626;
}

.light-close-button:hover {
  background: rgba(239, 68, 68, 0.2);
}

.light-icon {
  color: #fb923c;
}

/* Tema oscuro */
.dark-modal {
  background: rgba(30, 41, 59, 0.95);
  border: 1px solid rgba(71, 85, 105, 0.5);
}

.dark-modal .modal-header {
  border-color: rgba(71, 85, 105, 0.3);
}

.dark-input {
  background: rgba(30, 41, 59, 0.7);
  border-color: rgba(71, 85, 105, 0.5);
  color: #e2e8f0;
}

.dark-input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.1);
  background: rgba(30, 41, 59, 0.9);
}

.dark-button-primary {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: #e2e8f0;
  box-shadow: 0 4px 15px rgba(59, 130, 246, 0.3);
}

.dark-button-primary:hover:not(:disabled) {
  background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.4);
}

.dark-button-secondary {
  background: rgba(71, 85, 105, 0.8);
  color: #e2e8f0;
  box-shadow: 0 4px 15px rgba(71, 85, 105, 0.3);
}

.dark-button-secondary:hover {
  background: rgba(71, 85, 105, 0.9);
  box-shadow: 0 6px 20px rgba(71, 85, 105, 0.4);
}

.dark-close-button {
  background: rgba(239, 68, 68, 0.1);
  color: #f87171;
}

.dark-close-button:hover {
  background: rgba(239, 68, 68, 0.2);
}

.dark-icon {
  color: #3b82f6;
}

/* Responsive */
@media (max-width: 768px) {
  .modal-overlay {
    padding: 0.5rem;
  }
  
  .modal-content {
    padding: 1rem;
  }
  
  .form-actions {
    flex-direction: column;
  }
}
</style>

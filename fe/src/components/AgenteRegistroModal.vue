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
            :disabled="loading || !formulario.nombre.trim()"
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
import { Users, X } from 'lucide-vue-next'
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
  telefono: ''
})

const loading = ref(false)
const error = ref(null)

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
    telefono: ''
  }
  error.value = null
}

const crearAgente = async () => {
  loading.value = true
  error.value = null

  try {
    // Validación básica
    if (!formulario.value.nombre.trim()) {
      throw new Error('El nombre es obligatorio')
    }

    // Preparar datos para envío
    const agenteData = {
      nombre: formulario.value.nombre.trim(),
      email: formulario.value.email.trim() || undefined,
      telefono: formulario.value.telefono.trim() || undefined
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

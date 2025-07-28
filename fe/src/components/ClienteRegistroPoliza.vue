<template>
  <div class="cliente-poliza-container">
    <!-- Header -->
    <div :class="['header-card', cardClass]">
      <div class="header-content">
        <div :class="['icon-container', iconContainerClass]">
          <FileText class="header-icon" />
        </div>
        <div class="header-text">
          <h2 class="header-title">Registro de Póliza</h2>
          <p class="header-subtitle">Complete los datos de su vehículo para crear su póliza</p>
        </div>
      </div>
    </div>

    <!-- Indicador de carga -->
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>Procesando su póliza...</p>
    </div>

    <!-- Mensaje de error -->
    <div v-if="error" class="error-message">
      <p>{{ error }}</p>
      <button @click="limpiarError" class="retry-button">Reintentar</button>
    </div>

    <!-- Información del Cliente -->
    <div :class="['client-info-card', cardClass]">
      <div class="card-header">
        <User class="card-icon" />
        <h3 class="card-title">Información del Cliente</h3>
      </div>
      <div class="client-details">
        <div class="detail-row">
          <span class="detail-label">Nombre:</span>
          <span class="detail-value">{{ cliente.nombre }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">Email:</span>
          <span class="detail-value">{{ cliente.email }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">Teléfono:</span>
          <span class="detail-value">{{ cliente.telefono }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">ID Cliente:</span>
          <span class="detail-value">{{ cliente.id }}</span>
        </div>
      </div>
    </div>

    <!-- Formulario de Póliza -->
    <div :class="['form-card', cardClass]">
      <div class="card-header">
        <Car class="card-icon" />
        <h3 class="card-title">Datos del Vehículo</h3>
      </div>

      <form @submit.prevent="crearPoliza" class="form">
        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Marca del Vehículo *</label>
            <input
              v-model="polizaData.marcaVehiculo"
              type="text"
              :class="['form-input', inputClass]"
              placeholder="Toyota, Honda, Ford, etc."
              required
            />
          </div>
          
          <div class="form-group">
            <label class="form-label">Modelo del Vehículo *</label>
            <input
              v-model="polizaData.modeloVehiculo"
              type="text"
              :class="['form-input', inputClass]"
              placeholder="Corolla, Civic, Focus, etc."
              required
            />
          </div>
        </div>
        
        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Año del Vehículo *</label>
            <input
              v-model="polizaData.anoVehiculo"
              type="number"
              :class="['form-input', inputClass]"
              placeholder="2020"
              min="1990"
              max="2024"
              required
            />
          </div>
          
          <div class="form-group">
            <label class="form-label">Color del Vehículo</label>
            <input
              v-model="polizaData.colorVehiculo"
              type="text"
              :class="['form-input', inputClass]"
              placeholder="Blanco, Negro, Azul, etc."
            />
          </div>
        </div>
        
        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Tipo de Seguro *</label>
            <select v-model="polizaData.tipoSeguro" :class="['form-input', inputClass]" required>
              <option value="">Seleccionar tipo de seguro</option>
              <option value="AUTO">Seguro Básico</option>
              <option value="TODO_RIESGO">Seguro Todo Riesgo</option>
              <option value="TERCEROS">Seguro a Terceros</option>
              <option value="ROBO_HURTO">Seguro Robo y Hurto</option>
            </select>
          </div>
          
          <div class="form-group">
            <label class="form-label">Agente Asignado *</label>
            <select v-model="polizaData.agenteId" :class="['form-input', inputClass]" required>
              <option value="">Seleccionar agente</option>
              <option v-for="agente in agentes" :key="agente.id" :value="agente.id">
                {{ agente.nombre }} - {{ agente.codigo }}
              </option>
            </select>
          </div>
        </div>
        
        <div class="form-row">
          <div class="form-group">
            <label class="form-label">Prima Mensual ($) *</label>
            <input
              v-model="polizaData.primaMensual"
              type="number"
              step="0.01"
              :class="['form-input', inputClass]"
              placeholder="0.00"
              required
            />
          </div>
          
          <div class="form-group">
            <label class="form-label">Observaciones</label>
            <textarea
              v-model="polizaData.observaciones"
              :class="['form-input', 'form-textarea', inputClass]"
              placeholder="Información adicional sobre el vehículo..."
              rows="3"
            ></textarea>
          </div>
        </div>
        
        <button
          type="submit"
          :class="['submit-button', buttonPrimaryClass]"
          :disabled="loading"
        >
          {{ loading ? 'Creando Póliza...' : 'Crear Póliza' }}
        </button>
      </form>
    </div>

    <!-- Información de la Póliza Creada -->
    <div v-if="polizaCreada" :class="['success-card', cardClass]">
      <div class="card-header">
        <CheckCircle2 class="card-icon success-icon" />
        <h3 class="card-title">¡Póliza Creada Exitosamente!</h3>
      </div>
      <div class="poliza-details">
        <div class="detail-row">
          <span class="detail-label">Número de Póliza:</span>
          <span class="detail-value">{{ polizaCreada.numeroPoliza }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">Estado:</span>
          <span :class="['status-badge', getStatusBadgeClass(polizaCreada.estado)]">
            {{ polizaCreada.estado }}
          </span>
        </div>
        <div class="detail-row">
          <span class="detail-label">Tipo de Seguro:</span>
          <span class="detail-value">{{ polizaCreada.tipoSeguro }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">Prima Mensual:</span>
          <span class="detail-value">${{ polizaCreada.prima }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">Agente Asignado:</span>
          <span class="detail-value">{{ obtenerNombreAgente(polizaCreada.agenteId) }}</span>
        </div>
      </div>
      <div class="success-message">
        <p>Su póliza ha sido creada y está en estado PENDIENTE. Un agente se pondrá en contacto con usted pronto para completar el proceso.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { FileText, User, Car, CheckCircle2 } from 'lucide-vue-next'
import apiService from '../services/apiService.js'

const props = defineProps({
  isDark: Boolean,
  cliente: {
    type: Object,
    required: true
  }
})

const polizaData = ref({
  marcaVehiculo: '',
  modeloVehiculo: '',
  anoVehiculo: '',
  colorVehiculo: '',
  tipoSeguro: '',
  agenteId: '',
  primaMensual: '',
  observaciones: ''
})

const agentes = ref([])
const polizaCreada = ref(null)
const loading = ref(false)
const error = ref(null)

// Computed properties
const cardClass = computed(() => {
  return props.isDark ? 'dark-card' : 'light-card'
})

const inputClass = computed(() => {
  return props.isDark ? 'dark-input' : 'light-input'
})

const buttonPrimaryClass = computed(() => {
  return props.isDark ? 'dark-button-primary' : 'light-button-primary'
})

const iconContainerClass = computed(() => {
  return props.isDark ? 'dark-icon-container' : 'light-icon-container'
})

// Métodos
const cargarAgentes = async () => {
  try {
    const agentesData = await apiService.getAgentes()
    agentes.value = agentesData.map(agente => ({
      id: agente.idAgente,
      nombre: agente.nombre,
      email: agente.email,
      telefono: agente.telefono,
      codigo: agente.codigo
    }))
  } catch (err) {
    error.value = 'Error al cargar agentes: ' + err.message
    console.error('Error cargando agentes:', err)
  }
}

const validarDatos = (datos) => {
  const errores = []

  if (!datos.marcaVehiculo || datos.marcaVehiculo.length < 2) {
    errores.push('Marca del vehículo debe tener al menos 2 caracteres')
  }

  if (!datos.modeloVehiculo || datos.modeloVehiculo.length < 2) {
    errores.push('Modelo del vehículo debe tener al menos 2 caracteres')
  }

  const ano = parseInt(datos.anoVehiculo)
  if (isNaN(ano) || ano < 1990 || ano > 2024) {
    errores.push('Año del vehículo debe estar entre 1990 y 2024')
  }

  if (!datos.tipoSeguro) {
    errores.push('Debe seleccionar un tipo de seguro')
  }

  if (!datos.agenteId) {
    errores.push('Debe seleccionar un agente')
  }

  const prima = parseFloat(datos.primaMensual)
  if (isNaN(prima) || prima <= 0) {
    errores.push('Prima mensual debe ser un número mayor a 0')
  }

  return errores
}

const crearPoliza = async () => {
  // Validación automática de datos
  const errores = validarDatos(polizaData.value)

  if (errores.length > 0) {
    alert('Errores de validación:\n' + errores.join('\n'))
    return
  }

  loading.value = true
  error.value = null
  
  try {
    // Crear la póliza
    const polizaRequest = {
      numeroPoliza: null, // Se generará automáticamente en el backend
      fechaEmision: new Date().toISOString().split('T')[0], // Formato YYYY-MM-DD
      fechaVencimiento: null, // Opcional
      estado: 'PENDIENTE',
      clienteId: props.cliente.idCliente.toString(), // Convertir a string
      agenteId: polizaData.value.agenteId.toString(), // Convertir a string
      prima: parseFloat(polizaData.value.primaMensual),
      tipoSeguro: polizaData.value.tipoSeguro,
      observaciones: `Vehículo: ${polizaData.value.marcaVehiculo} ${polizaData.value.modeloVehiculo} ${polizaData.value.anoVehiculo}${polizaData.value.colorVehiculo ? ' - Color: ' + polizaData.value.colorVehiculo : ''}. ${polizaData.value.observaciones || ''}`,
      marca: polizaData.value.marcaVehiculo || null,
      modelo: polizaData.value.modeloVehiculo || null,
      anioVehiculo: polizaData.value.anoVehiculo ? `${polizaData.value.anoVehiculo}-01-01` : null // Formato YYYY-MM-DD
    }
    
    console.log('Cliente recibido:', props.cliente)
    console.log('PolizaRequest a enviar:', polizaRequest)
    
    // Validar que el cliente tenga ID
    if (!props.cliente.idCliente) {
      error.value = 'Error: Cliente sin ID válido'
      console.error('Cliente sin ID:', props.cliente)
      return
    }
    
    // Validar que el agente tenga ID válido
    if (!polizaData.value.agenteId) {
      error.value = 'Error: Debe seleccionar un agente'
      return
    }
    
    const polizaCreadaData = await apiService.crearPoliza(polizaRequest)
    
    // Guardar la póliza creada
    polizaCreada.value = polizaCreadaData
    
    // Limpiar formulario
    polizaData.value = {
      marcaVehiculo: '',
      modeloVehiculo: '',
      anoVehiculo: '',
      colorVehiculo: '',
      tipoSeguro: '',
      agenteId: '',
      primaMensual: '',
      observaciones: ''
    }
    
  } catch (err) {
    error.value = 'Error al crear póliza: ' + err.message
    console.error('Error creando póliza:', err)
  } finally {
    loading.value = false
  }
}

const obtenerNombreAgente = (agenteId) => {
  if (!agenteId) return 'No asignado'
  const agente = agentes.value.find(a => a.id === agenteId)
  return agente ? agente.nombre : 'No asignado'
}

const getStatusBadgeClass = (estado) => {
  switch (estado) {
    case 'PENDIENTE':
      return props.isDark ? 'status-pendiente-dark' : 'status-pendiente-light'
    case 'APROBADA':
      return props.isDark ? 'status-aprobada-dark' : 'status-aprobada-light'
    case 'RECHAZADA':
      return props.isDark ? 'status-rechazada-dark' : 'status-rechazada-light'
    case 'CANCELADA':
      return props.isDark ? 'status-cancelada-dark' : 'status-cancelada-light'
    default:
      return props.isDark ? 'status-default-dark' : 'status-default-light'
  }
}

const limpiarError = () => {
  error.value = null
}

onMounted(() => {
  cargarAgentes()
})
</script>

<style scoped>
/* Contenedor principal */
.cliente-poliza-container {
  display: flex;
  flex-direction: column;
  gap: 2rem;
  padding: 1rem;
  max-width: 1200px;
  margin: 0 auto;
}

/* Header */
.header-card {
  border-radius: 1rem;
  padding: 1.5rem;
  backdrop-filter: blur(8px);
}

.header-content {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
}

.icon-container {
  padding: 0.75rem;
  border-radius: 0.75rem;
}

.header-icon {
  width: 1.5rem;
  height: 1.5rem;
}

.header-title {
  font-size: 1.5rem;
  font-weight: bold;
  margin: 0;
}

.header-subtitle {
  opacity: 0.7;
  margin: 0;
}

/* Tarjetas */
.client-info-card,
.form-card,
.success-card {
  border-radius: 1rem;
  padding: 1.5rem;
  backdrop-filter: blur(8px);
}

.card-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
}

.card-icon {
  width: 1.25rem;
  height: 1.25rem;
}

.card-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0;
}

/* Información del cliente */
.client-details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1rem;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem;
  border-radius: 0.5rem;
  border: 1px solid;
}

.detail-label {
  font-weight: 500;
  opacity: 0.8;
}

.detail-value {
  font-weight: 600;
}

/* Formulario */
.form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-label {
  font-size: 0.875rem;
  font-weight: 500;
  opacity: 0.8;
}

.form-input {
  width: 100%;
  padding: 0.75rem 1rem;
  border-radius: 0.75rem;
  border: 1px solid;
  transition: all 0.2s ease;
}

.form-textarea {
  resize: vertical;
  min-height: 80px;
}

.form-input:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
}

.submit-button {
  width: 100%;
  padding: 1rem 2rem;
  border-radius: 0.75rem;
  font-weight: 600;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 1.1rem;
}

.submit-button:hover:not(:disabled) {
  transform: scale(1.02);
}

.submit-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Póliza creada */
.poliza-details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.status-badge {
  display: inline-flex;
  align-items: center;
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  font-size: 0.75rem;
  font-weight: 500;
}

.success-message {
  padding: 1rem;
  border-radius: 0.5rem;
  text-align: center;
  font-size: 1rem;
  line-height: 1.6;
}

.success-icon {
  color: #10b981;
}

/* Indicadores de carga y error */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  gap: 1rem;
}

.loading-spinner {
  width: 2rem;
  height: 2rem;
  border: 3px solid rgba(59, 130, 246, 0.3);
  border-top: 3px solid #3b82f6;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error-message {
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.3);
  border-radius: 0.75rem;
  padding: 1rem;
  margin-bottom: 1rem;
  text-align: center;
}

.error-message p {
  color: #dc2626;
  margin: 0 0 1rem 0;
}

.retry-button {
  background: #dc2626;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.retry-button:hover {
  background: #b91c1c;
}

/* Tema claro */
.light-card {
  background: rgba(254, 247, 237, 0.6);
  border: 1px solid rgba(253, 186, 116, 0.5);
  box-shadow: 0 8px 32px rgba(251, 146, 60, 0.1);
}

.light-input {
  background: rgba(254, 247, 237, 0.5);
  border-color: rgba(253, 186, 116, 0.5);
  color: #9a3412;
}

.light-input:focus {
  border-color: #fb923c;
  box-shadow: 0 0 0 2px rgba(251, 146, 60, 0.1);
  background: rgba(254, 247, 237, 0.8);
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

.light-icon-container {
  background: rgba(251, 146, 60, 0.2);
  color: #ea580c;
}

.light-card .detail-row {
  background: rgba(254, 247, 237, 0.3);
  border-color: rgba(253, 186, 116, 0.3);
}

.light-card .success-message {
  background: rgba(16, 185, 129, 0.1);
  border: 1px solid rgba(16, 185, 129, 0.2);
}

.status-pendiente-light {
  background-color: rgba(245, 158, 11, 0.2);
  color: #92400e;
  border: 1px solid rgba(245, 158, 11, 0.3);
}

.status-aprobada-light {
  background-color: rgba(34, 197, 94, 0.2);
  color: #166534;
  border: 1px solid rgba(34, 197, 94, 0.3);
}

.status-rechazada-light {
  background-color: rgba(239, 68, 68, 0.2);
  color: #dc2626;
  border: 1px solid rgba(239, 68, 68, 0.3);
}

.status-cancelada-light {
  background-color: rgba(107, 114, 128, 0.2);
  color: #374151;
  border: 1px solid rgba(107, 114, 128, 0.3);
}

.status-default-light {
  background-color: rgba(59, 130, 246, 0.2);
  color: #1e40af;
  border: 1px solid rgba(59, 130, 246, 0.3);
}

/* Tema oscuro */
.dark-card {
  background: rgba(30, 41, 59, 0.6);
  border: 1px solid rgba(71, 85, 105, 0.5);
  box-shadow: 0 8px 32px rgba(59, 130, 246, 0.1);
}

.dark-input {
  background: rgba(30, 41, 59, 0.5);
  border-color: rgba(71, 85, 105, 0.5);
  color: #e2e8f0;
}

.dark-input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.1);
  background: rgba(30, 41, 59, 0.8);
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

.dark-icon-container {
  background: rgba(59, 130, 246, 0.2);
  color: #93c5fd;
}

.dark-card .detail-row {
  background: rgba(51, 65, 85, 0.3);
  border-color: rgba(71, 85, 105, 0.3);
}

.dark-card .success-message {
  background: rgba(16, 185, 129, 0.1);
  border: 1px solid rgba(16, 185, 129, 0.2);
}

.status-pendiente-dark {
  background-color: rgba(245, 158, 11, 0.2);
  color: #fbbf24;
  border: 1px solid rgba(245, 158, 11, 0.3);
}

.status-aprobada-dark {
  background-color: rgba(34, 197, 94, 0.2);
  color: #4ade80;
  border: 1px solid rgba(34, 197, 94, 0.3);
}

.status-rechazada-dark {
  background-color: rgba(239, 68, 68, 0.2);
  color: #f87171;
  border: 1px solid rgba(239, 68, 68, 0.3);
}

.status-cancelada-dark {
  background-color: rgba(107, 114, 128, 0.2);
  color: #9ca3af;
  border: 1px solid rgba(107, 114, 128, 0.3);
}

.status-default-dark {
  background-color: rgba(59, 130, 246, 0.2);
  color: #93c5fd;
  border: 1px solid rgba(59, 130, 246, 0.3);
}

/* Responsive */
@media (max-width: 768px) {
  .cliente-poliza-container {
    padding: 0.5rem;
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .client-details,
  .poliza-details {
    grid-template-columns: 1fr;
  }
  
  .detail-row {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
}
</style> 
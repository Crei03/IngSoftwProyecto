<template>
  <div class="mis-polizas-container">
    <!-- Header -->
    <div :class="['header-card', cardClass]">
      <div class="header-content">
        <div :class="['icon-container', iconContainerClass]">
          <FileText class="header-icon" />
        </div>
        <div class="header-text">
          <h2 class="header-title">Mis Pólizas</h2>
          <p class="header-subtitle">Gestiona y revisa todas tus pólizas de seguro</p>
        </div>
      </div>
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
          <span class="detail-label">Total de Pólizas:</span>
          <span class="detail-value">{{ polizas.length }}</span>
        </div>
      </div>
    </div>

    <!-- Indicador de carga -->
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>Cargando tus pólizas...</p>
    </div>

    <!-- Mensaje de error -->
    <div v-if="error" class="error-message">
      <p>{{ error }}</p>
      <button @click="cargarPolizas" class="retry-button">Reintentar</button>
    </div>

    <!-- Filtros -->
    <div :class="['filters-card', cardClass]">
      <div class="card-header">
        <Filter class="card-icon" />
        <h3 class="card-title">Filtros</h3>
      </div>
      <div class="filters-content">
        <div class="filter-row">
          <div class="filter-group">
            <label class="filter-label">Estado</label>
            <select v-model="filtros.estado" :class="['filter-input', inputClass]">
              <option value="">Todos los estados</option>
              <option value="PENDIENTE">PENDIENTE</option>
              <option value="APROBADA">APROBADA</option>
              <option value="RECHAZADA">RECHAZADA</option>
              <option value="CANCELADA">CANCELADA</option>
            </select>
          </div>
          <div class="filter-group">
            <label class="filter-label">Tipo de Seguro</label>
            <select v-model="filtros.tipoSeguro" :class="['filter-input', inputClass]">
              <option value="">Todos los tipos</option>
              <option value="AUTO">AUTO</option>
              <option value="TODO_RIESGO">TODO RIESGO</option>
              <option value="TERCEROS">TERCEROS</option>
              <option value="ROBO_HURTO">ROBO Y HURTO</option>
            </select>
          </div>
          <div class="filter-actions">
            <button @click="limpiarFiltros" :class="['filter-button', buttonSecondaryClass]">
              Limpiar
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Lista de Pólizas -->
    <div :class="['polizas-card', cardClass]">
      <div class="card-header">
        <List class="card-icon" />
        <h3 class="card-title">Mis Pólizas ({{ polizasFiltradas.length }})</h3>
      </div>

      <div v-if="polizasFiltradas.length === 0 && !loading" class="no-polizas">
        <p>No tienes pólizas con los filtros aplicados</p>
        <button @click="navegarARegistro" :class="['action-button', buttonPrimaryClass]">
          Crear Nueva Póliza
        </button>
      </div>

      <div v-else class="polizas-list">
        <div
          v-for="poliza in polizasFiltradas"
          :key="poliza.id"
          :class="['poliza-item', itemCardClass]"
        >
          <div class="poliza-header">
            <div class="poliza-info">
              <h4 class="poliza-numero">{{ poliza.numeroPoliza }}</h4>
              <div :class="['status-badge', getStatusBadgeClass(poliza.estado)]">
                {{ poliza.estado }}
              </div>
            </div>
            <div class="poliza-actions">
              <button
                v-if="poliza.estado === 'PENDIENTE'"
                @click="editarPoliza(poliza)"
                :class="['action-button', 'edit-button', editButtonClass]"
              >
                <Edit class="action-icon" />
                Editar
              </button>
              <button
                @click="verDetalles(poliza)"
                :class="['action-button', 'view-button', viewButtonClass]"
              >
                <Eye class="action-icon" />
                Ver Detalles
              </button>
            </div>
          </div>

          <div class="poliza-details">
            <div class="detail-grid">
              <div class="detail-item">
                <span class="detail-label">Tipo:</span>
                <span class="detail-value">{{ poliza.tipoSeguro }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">Prima:</span>
                <span class="detail-value">${{ poliza.prima }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">Vehículo:</span>
                <span class="detail-value">{{ poliza.marca }} {{ poliza.modelo }} {{ poliza.anioVehiculo }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">Fecha:</span>
                <span class="detail-value">{{ formatearFecha(poliza.fechaEmision) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal de Edición -->
    <div v-if="showEditModal" class="modal-overlay" @click="cerrarModal">
      <div :class="['modal-content', cardClass]" @click.stop>
        <div class="modal-header">
          <h3>Editar Póliza</h3>
          <button @click="cerrarModal" class="close-button">
            <X class="close-icon" />
          </button>
        </div>
        
        <form @submit.prevent="guardarCambios" class="edit-form">
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">Marca del Vehículo *</label>
              <input
                v-model="polizaEditando.marca"
                type="text"
                :class="['form-input', inputClass]"
                placeholder="Toyota, Honda, Ford, etc."
                required
              />
            </div>
            
            <div class="form-group">
              <label class="form-label">Modelo del Vehículo *</label>
              <input
                v-model="polizaEditando.modelo"
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
                v-model="polizaEditando.anioVehiculo"
                type="number"
                :class="['form-input', inputClass]"
                placeholder="2020"
                min="1990"
                max="2024"
                required
              />
            </div>
            
            <div class="form-group">
              <label class="form-label">Tipo de Seguro *</label>
              <select v-model="polizaEditando.tipoSeguro" :class="['form-input', inputClass]" required>
                <option value="AUTO">Seguro Básico</option>
                <option value="TODO_RIESGO">Seguro Todo Riesgo</option>
                <option value="TERCEROS">Seguro a Terceros</option>
                <option value="ROBO_HURTO">Seguro Robo y Hurto</option>
              </select>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">Prima Mensual ($) *</label>
              <input
                v-model="polizaEditando.prima"
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
                v-model="polizaEditando.observaciones"
                :class="['form-input', 'form-textarea', inputClass]"
                placeholder="Información adicional..."
                rows="3"
              ></textarea>
            </div>
          </div>
          
          <div class="modal-actions">
            <button type="button" @click="cerrarModal" :class="['modal-button', buttonSecondaryClass]">
              Cancelar
            </button>
            <button type="submit" :class="['modal-button', buttonPrimaryClass]" :disabled="saving">
              {{ saving ? 'Guardando...' : 'Guardar Cambios' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- Modal de Detalles -->
    <div v-if="showDetailsModal" class="modal-overlay" @click="cerrarModal">
      <div :class="['modal-content', cardClass]" @click.stop>
        <div class="modal-header">
          <h3>Detalles de la Póliza</h3>
          <button @click="cerrarModal" class="close-button">
            <X class="close-icon" />
          </button>
        </div>
        
        <div class="details-content">
          <div class="detail-section">
            <h4>Información General</h4>
            <div class="detail-grid">
              <div class="detail-item">
                <span class="detail-label">Número de Póliza:</span>
                <span class="detail-value">{{ polizaSeleccionada.numeroPoliza }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">Estado:</span>
                <span :class="['status-badge', getStatusBadgeClass(polizaSeleccionada.estado)]">
                  {{ polizaSeleccionada.estado }}
                </span>
              </div>
              <div class="detail-item">
                <span class="detail-label">Tipo de Seguro:</span>
                <span class="detail-value">{{ polizaSeleccionada.tipoSeguro }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">Prima Mensual:</span>
                <span class="detail-value">${{ polizaSeleccionada.prima }}</span>
              </div>
            </div>
          </div>
          
          <div class="detail-section">
            <h4>Información del Vehículo</h4>
            <div class="detail-grid">
              <div class="detail-item">
                <span class="detail-label">Marca:</span>
                <span class="detail-value">{{ polizaSeleccionada.marca }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">Modelo:</span>
                <span class="detail-value">{{ polizaSeleccionada.modelo }}</span>
              </div>
              <div class="detail-item">
                <span class="detail-label">Año:</span>
                <span class="detail-value">{{ polizaSeleccionada.anioVehiculo }}</span>
              </div>
            </div>
          </div>
          
          <div class="detail-section">
            <h4>Fechas</h4>
            <div class="detail-grid">
              <div class="detail-item">
                <span class="detail-label">Fecha de Emisión:</span>
                <span class="detail-value">{{ formatearFecha(polizaSeleccionada.fechaEmision) }}</span>
              </div>
              <div class="detail-item" v-if="polizaSeleccionada.fechaVencimiento">
                <span class="detail-label">Fecha de Vencimiento:</span>
                <span class="detail-value">{{ formatearFecha(polizaSeleccionada.fechaVencimiento) }}</span>
              </div>
            </div>
          </div>
          
          <div class="detail-section" v-if="polizaSeleccionada.observaciones">
            <h4>Observaciones</h4>
            <p class="observaciones">{{ polizaSeleccionada.observaciones }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { FileText, User, Filter, List, Edit, Eye, X } from 'lucide-vue-next'
import apiService from '../services/apiService.js'

const props = defineProps({
  isDark: Boolean,
  cliente: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['navegarARegistro'])

const polizas = ref([])
const loading = ref(false)
const error = ref(null)
const showEditModal = ref(false)
const showDetailsModal = ref(false)
const saving = ref(false)
const polizaEditando = ref({})
const polizaSeleccionada = ref({})

// Filtros
const filtros = ref({
  estado: '',
  tipoSeguro: ''
})

// Computed properties
const polizasFiltradas = computed(() => {
  let resultado = polizas.value

  if (filtros.value.estado) {
    resultado = resultado.filter(poliza => poliza.estado === filtros.value.estado)
  }

  if (filtros.value.tipoSeguro) {
    resultado = resultado.filter(poliza => poliza.tipoSeguro === filtros.value.tipoSeguro)
  }

  return resultado
})

const cardClass = computed(() => {
  return props.isDark ? 'dark-card' : 'light-card'
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

const iconContainerClass = computed(() => {
  return props.isDark ? 'dark-icon-container' : 'light-icon-container'
})

const itemCardClass = computed(() => {
  return props.isDark ? 'dark-item-card' : 'light-item-card'
})

const editButtonClass = computed(() => {
  return props.isDark ? 'dark-edit-button' : 'light-edit-button'
})

const viewButtonClass = computed(() => {
  return props.isDark ? 'dark-view-button' : 'light-view-button'
})

// Métodos
const cargarPolizas = async () => {
  loading.value = true
  error.value = null
  
  try {
    const polizasData = await apiService.getPolizasPorCliente(props.cliente.idCliente)
    polizas.value = polizasData.map(poliza => ({
      ...poliza,
      anioVehiculo: poliza.anioVehiculo ? poliza.anioVehiculo.split('-')[0] : ''
    }))
  } catch (err) {
    error.value = 'Error al cargar pólizas: ' + err.message
    console.error('Error cargando pólizas:', err)
  } finally {
    loading.value = false
  }
}

const editarPoliza = (poliza) => {
  polizaEditando.value = { ...poliza }
  showEditModal.value = true
}

const verDetalles = (poliza) => {
  polizaSeleccionada.value = poliza
  showDetailsModal.value = true
}

const guardarCambios = async () => {
  saving.value = true
  
  try {
    console.log('Poliza a actualizar:', polizaEditando.value)
    console.log('ID de la póliza:', polizaEditando.value.idPoliza)
    
    // Crear objeto de actualización con solo los campos necesarios
    const polizaUpdateData = {
      numeroPoliza: polizaEditando.value.numeroPoliza,
      fechaEmision: polizaEditando.value.fechaEmision,
      fechaVencimiento: polizaEditando.value.fechaVencimiento,
      estado: polizaEditando.value.estado,
      clienteId: polizaEditando.value.clienteId.toString(),
      agenteId: polizaEditando.value.agenteId.toString(),
      prima: polizaEditando.value.prima,
      tipoSeguro: polizaEditando.value.tipoSeguro,
      observaciones: polizaEditando.value.observaciones,
      marca: polizaEditando.value.marca,
      modelo: polizaEditando.value.modelo,
      anioVehiculo: polizaEditando.value.anioVehiculo ? `${polizaEditando.value.anioVehiculo}-01-01` : null
    }
    
    console.log('Datos de actualización:', polizaUpdateData)
    
    const polizaActualizada = await apiService.actualizarPoliza(polizaEditando.value.idPoliza, polizaUpdateData)
    
    // Actualizar la póliza en la lista local
    const index = polizas.value.findIndex(p => p.idPoliza === polizaEditando.value.idPoliza)
    if (index !== -1) {
      polizas.value[index] = { ...polizaActualizada, anioVehiculo: polizaActualizada.anioVehiculo ? polizaActualizada.anioVehiculo.split('-')[0] : '' }
    }
    
    cerrarModal()
    alert('Póliza actualizada exitosamente')
  } catch (err) {
    error.value = 'Error al actualizar póliza: ' + err.message
    console.error('Error actualizando póliza:', err)
  } finally {
    saving.value = false
  }
}

const cerrarModal = () => {
  showEditModal.value = false
  showDetailsModal.value = false
  polizaEditando.value = {}
  polizaSeleccionada.value = {}
}

const limpiarFiltros = () => {
  filtros.value = {
    estado: '',
    tipoSeguro: ''
  }
}

const navegarARegistro = () => {
  emit('navegarARegistro')
}

const formatearFecha = (fecha) => {
  if (!fecha) return 'N/A'
  return new Date(fecha).toLocaleDateString('es-ES')
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

onMounted(() => {
  cargarPolizas()
})
</script>

<style scoped>
/* Contenedor principal */
.mis-polizas-container {
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
.filters-card,
.polizas-card {
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
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
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

/* Filtros */
.filters-content {
  margin-bottom: 1rem;
}

.filter-row {
  display: flex;
  gap: 1rem;
  align-items: end;
}

.filter-group {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.filter-label {
  font-size: 0.875rem;
  font-weight: 500;
  opacity: 0.8;
}

.filter-input {
  width: 100%;
  padding: 0.75rem 1rem;
  border-radius: 0.75rem;
  border: 1px solid;
  transition: all 0.2s ease;
}

.filter-input:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
}

.filter-actions {
  display: flex;
  gap: 0.5rem;
}

.filter-button {
  padding: 0.75rem 1.5rem;
  border-radius: 0.75rem;
  font-weight: 600;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
}

/* Lista de pólizas */
.polizas-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.poliza-item {
  border-radius: 1rem;
  padding: 1.5rem;
  backdrop-filter: blur(8px);
  border: 1px solid;
  transition: all 0.2s ease;
}

.poliza-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.poliza-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.poliza-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.poliza-numero {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0;
}

.poliza-actions {
  display: flex;
  gap: 0.5rem;
}

.action-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  border: none;
  cursor: pointer;
  font-size: 0.875rem;
  font-weight: 500;
  transition: all 0.2s ease;
}

.action-icon {
  width: 1rem;
  height: 1rem;
}

.poliza-details {
  margin-top: 1rem;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem;
  border-radius: 0.5rem;
  background: rgba(255, 255, 255, 0.1);
}

/* Status badges */
.status-badge {
  display: inline-flex;
  align-items: center;
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  font-size: 0.75rem;
  font-weight: 500;
}

/* No pólizas */
.no-polizas {
  text-align: center;
  padding: 3rem;
  opacity: 0.6;
}

.no-polizas p {
  margin-bottom: 1.5rem;
  font-size: 1.1rem;
}

/* Modales */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  border-radius: 1rem;
  padding: 2rem;
  max-width: 600px;
  width: 90%;
  max-height: 80vh;
  overflow-y: auto;
  backdrop-filter: blur(10px);
  border: 1px solid;
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
  background: rgba(255, 255, 255, 0.1);
}

.close-icon {
  width: 1.5rem;
  height: 1.5rem;
}

/* Formulario de edición */
.edit-form {
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

.modal-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 1rem;
}

.modal-button {
  padding: 0.75rem 1.5rem;
  border-radius: 0.75rem;
  font-weight: 600;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
}

.modal-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Detalles */
.details-content {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.detail-section h4 {
  margin: 0 0 1rem 0;
  font-size: 1.1rem;
  font-weight: 600;
  border-bottom: 1px solid;
  padding-bottom: 0.5rem;
}

.observaciones {
  background: rgba(255, 255, 255, 0.1);
  padding: 1rem;
  border-radius: 0.5rem;
  margin: 0;
  line-height: 1.6;
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

.light-button-secondary {
  background: rgba(107, 114, 128, 0.8);
  color: #f9fafb;
  box-shadow: 0 4px 15px rgba(107, 114, 128, 0.3);
}

.light-button-secondary:hover:not(:disabled) {
  background: rgba(75, 85, 99, 0.9);
  box-shadow: 0 6px 20px rgba(107, 114, 128, 0.4);
}

.light-icon-container {
  background: rgba(251, 146, 60, 0.2);
  color: #ea580c;
}

.light-item-card {
  background: rgba(254, 247, 237, 0.4);
  border-color: rgba(253, 186, 116, 0.3);
}

.light-edit-button {
  background: rgba(59, 130, 246, 0.8);
  color: white;
}

.light-edit-button:hover {
  background: rgba(37, 99, 235, 0.9);
}

.light-view-button {
  background: rgba(16, 185, 129, 0.8);
  color: white;
}

.light-view-button:hover {
  background: rgba(5, 150, 105, 0.9);
}

.light-card .detail-row {
  background: rgba(254, 247, 237, 0.3);
  border-color: rgba(253, 186, 116, 0.3);
}

.light-card .detail-item {
  background: rgba(254, 247, 237, 0.3);
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

.dark-button-secondary {
  background: rgba(107, 114, 128, 0.8);
  color: #f9fafb;
  box-shadow: 0 4px 15px rgba(107, 114, 128, 0.3);
}

.dark-button-secondary:hover:not(:disabled) {
  background: rgba(75, 85, 99, 0.9);
  box-shadow: 0 6px 20px rgba(107, 114, 128, 0.4);
}

.dark-icon-container {
  background: rgba(59, 130, 246, 0.2);
  color: #93c5fd;
}

.dark-item-card {
  background: rgba(51, 65, 85, 0.4);
  border-color: rgba(71, 85, 105, 0.3);
}

.dark-edit-button {
  background: rgba(59, 130, 246, 0.8);
  color: white;
}

.dark-edit-button:hover {
  background: rgba(37, 99, 235, 0.9);
}

.dark-view-button {
  background: rgba(16, 185, 129, 0.8);
  color: white;
}

.dark-view-button:hover {
  background: rgba(5, 150, 105, 0.9);
}

.dark-card .detail-row {
  background: rgba(51, 65, 85, 0.3);
  border-color: rgba(71, 85, 105, 0.3);
}

.dark-card .detail-item {
  background: rgba(51, 65, 85, 0.3);
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
  .mis-polizas-container {
    padding: 0.5rem;
  }
  
  .form-row,
  .filter-row {
    grid-template-columns: 1fr;
    flex-direction: column;
  }
  
  .client-details,
  .detail-grid {
    grid-template-columns: 1fr;
  }
  
  .poliza-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .poliza-actions {
    width: 100%;
    justify-content: flex-end;
  }
  
  .modal-content {
    width: 95%;
    padding: 1rem;
  }
}
</style> 
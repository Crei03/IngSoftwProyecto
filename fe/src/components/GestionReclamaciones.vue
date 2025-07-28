<template>
  <div :class="['gestion-reclamaciones', themeClass]">
    <!-- Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <FileX class="page-icon" />
          <div class="header-text">
            <h1 class="page-title">Gestión de Reclamaciones</h1>
            <p class="page-subtitle">Administrar y procesar reclamaciones de seguros</p>
          </div>
        </div>
        <div class="header-actions">
          <button @click="cargarReclamaciones" class="refresh-button" :disabled="loading">
            <RefreshCw :class="['refresh-icon', { spinning: loading }]" />
            Actualizar
          </button>
        </div>
      </div>
    </div>

    <!-- Filtros -->
    <div class="filters-section">
      <div class="filters-grid">
        <div class="filter-group">
          <label class="filter-label">Estado</label>
          <select v-model="filtroEstado" @change="aplicarFiltros" class="filter-select">
            <option value="">Todos los estados</option>
            <option value="REGISTRADA">Registrada</option>
            <option value="EN_EVALUACION">En Evaluación</option>
            <option value="APROBADA">Aprobada</option>
            <option value="RECHAZADA">Rechazada</option>
            <option value="PAGADA">Pagada</option>
          </select>
        </div>
        <div class="filter-group">
          <label class="filter-label">Buscar por póliza</label>
          <input 
            v-model="filtroPoliza" 
            @input="aplicarFiltros"
            type="text" 
            placeholder="Número de póliza..."
            class="filter-input"
          />
        </div>
      </div>
    </div>

    <!-- Estadísticas -->
    <div class="stats-grid">
      <div class="stat-card registrada">
        <div class="stat-icon">
          <Clock class="icon" />
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ estadisticas.registrada }}</div>
          <div class="stat-label">Registradas</div>
        </div>
      </div>
      <div class="stat-card evaluacion">
        <div class="stat-icon">
          <Search class="icon" />
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ estadisticas.enEvaluacion }}</div>
          <div class="stat-label">En Evaluación</div>
        </div>
      </div>
      <div class="stat-card aprobada">
        <div class="stat-icon">
          <CheckCircle class="icon" />
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ estadisticas.aprobada }}</div>
          <div class="stat-label">Aprobadas</div>
        </div>
      </div>
      <div class="stat-card rechazada">
        <div class="stat-icon">
          <XCircle class="icon" />
        </div>
        <div class="stat-content">
          <div class="stat-number">{{ estadisticas.rechazada }}</div>
          <div class="stat-label">Rechazadas</div>
        </div>
      </div>
    </div>

    <!-- Loading y mensajes -->
    <div v-if="loading" class="loading-container">
      <div class="spinner"></div>
      <p>Cargando reclamaciones...</p>
    </div>

    <div v-if="mensaje" :class="['mensaje', tipoMensaje]">
      {{ mensaje }}
    </div>

    <!-- Tabla de Reclamaciones -->
    <div v-if="!loading" class="table-container">
      <div class="table-header">
        <h3>Reclamaciones ({{ reclamacionesFiltradas.length }})</h3>
      </div>
      
      <div v-if="reclamacionesFiltradas.length === 0" class="empty-state">
        <FileX class="empty-icon" />
        <h3>No hay reclamaciones</h3>
        <p>No se encontraron reclamaciones con los filtros aplicados</p>
      </div>

      <div v-else class="table-wrapper">
        <table class="reclamaciones-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Póliza</th>
              <th>Cliente</th>
              <th>Fecha</th>
              <th>Monto</th>
              <th>Estado</th>
              <th>Descripción</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="reclamacion in reclamacionesFiltradas" :key="reclamacion.idReclamacion" class="table-row">
              <td>{{ reclamacion.idReclamacion }}</td>
              <td>
                <span class="poliza-badge">{{ reclamacion.numeroPoliza || 'N/A' }}</span>
              </td>
              <td>{{ reclamacion.nombreCliente || 'N/A' }}</td>
              <td>{{ formatearFecha(reclamacion.fechaReclamacion) }}</td>
              <td>
                <span class="monto">{{ formatearMonto(reclamacion.montoReclamado) }}</span>
              </td>
              <td>
                <span :class="['estado-badge', reclamacion.estado?.toLowerCase()]">
                  {{ formatearEstado(reclamacion.estado) }}
                </span>
              </td>
              <td class="descripcion-cell">
                <div class="descripcion-preview">
                  {{ truncarTexto(reclamacion.descripcion, 50) }}
                </div>
              </td>
              <td>
                <div class="actions-container">
                  <button 
                    @click="verDetalles(reclamacion)" 
                    class="action-button view"
                    title="Ver detalles"
                  >
                    <Eye class="action-icon" />
                  </button>
                  <button 
                    v-if="reclamacion.estado === 'REGISTRADA'"
                    @click="cambiarEstado(reclamacion, 'EN_EVALUACION')" 
                    class="action-button evaluate"
                    title="Evaluar"
                  >
                    <Search class="action-icon" />
                  </button>
                  <button 
                    v-if="reclamacion.estado === 'EN_EVALUACION'"
                    @click="cambiarEstado(reclamacion, 'APROBADA')" 
                    class="action-button approve"
                    title="Aprobar"
                  >
                    <CheckCircle class="action-icon" />
                  </button>
                  <button 
                    v-if="reclamacion.estado === 'EN_EVALUACION'"
                    @click="cambiarEstado(reclamacion, 'RECHAZADA')" 
                    class="action-button reject"
                    title="Rechazar"
                  >
                    <XCircle class="action-icon" />
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Modal de Detalles -->
    <div v-if="showDetallesModal" class="modal-overlay" @click="cerrarDetalles">
      <div :class="['modal-content', 'modal-large', themeClass]" @click.stop>
        <div class="modal-header">
          <h3>Detalles de Reclamación #{{ reclamacionSeleccionada?.idReclamacion }}</h3>
          <button @click="cerrarDetalles" class="close-button">
            <X class="close-icon" />
          </button>
        </div>
        
        <div class="modal-body" v-if="reclamacionSeleccionada">
          <div class="detalles-grid">
            <div class="detalle-section">
              <h4>Información General</h4>
              <div class="detalle-row">
                <span class="detalle-label">ID:</span>
                <span class="detalle-value">{{ reclamacionSeleccionada.idReclamacion }}</span>
              </div>
              <div class="detalle-row">
                <span class="detalle-label">Póliza:</span>
                <span class="detalle-value">{{ reclamacionSeleccionada.numeroPoliza || 'N/A' }}</span>
              </div>
              <div class="detalle-row">
                <span class="detalle-label">Cliente:</span>
                <span class="detalle-value">{{ reclamacionSeleccionada.nombreCliente || 'N/A' }}</span>
              </div>
              <div class="detalle-row">
                <span class="detalle-label">Fecha:</span>
                <span class="detalle-value">{{ formatearFecha(reclamacionSeleccionada.fechaReclamacion) }}</span>
              </div>
              <div class="detalle-row">
                <span class="detalle-label">Estado:</span>
                <span :class="['estado-badge', reclamacionSeleccionada.estado?.toLowerCase()]">
                  {{ formatearEstado(reclamacionSeleccionada.estado) }}
                </span>
              </div>
            </div>
            
            <div class="detalle-section">
              <h4>Información Financiera</h4>
              <div class="detalle-row">
                <span class="detalle-label">Monto Reclamado:</span>
                <span class="detalle-value monto-grande">{{ formatearMonto(reclamacionSeleccionada.montoReclamado) }}</span>
              </div>
            </div>
          </div>
          
          <div class="detalle-section full-width">
            <h4>Descripción del Siniestro</h4>
            <div class="descripcion-completa">
              {{ reclamacionSeleccionada.descripcion || 'Sin descripción disponible' }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { 
  FileX, RefreshCw, Clock, Search, CheckCircle, XCircle, 
  Eye, X 
} from 'lucide-vue-next'
import apiService from '../services/apiService.js'

const props = defineProps({
  isDark: {
    type: Boolean,
    default: false
  }
})

const themeClass = computed(() => {
  return props.isDark ? 'theme-dark' : 'theme-light'
})

// Estado reactivo
const loading = ref(false)
const mensaje = ref('')
const tipoMensaje = ref('info')
const reclamaciones = ref([])
const reclamacionSeleccionada = ref(null)
const showDetallesModal = ref(false)

// Filtros
const filtroEstado = ref('')
const filtroPoliza = ref('')

// Estadísticas
const estadisticas = reactive({
  registrada: 0,
  enEvaluacion: 0,
  aprobada: 0,
  rechazada: 0,
  pagada: 0
})

// Computed
const reclamacionesFiltradas = computed(() => {
  let filtradas = reclamaciones.value

  if (filtroEstado.value) {
    filtradas = filtradas.filter(r => r.estado === filtroEstado.value)
  }

  if (filtroPoliza.value) {
    filtradas = filtradas.filter(r => 
      r.numeroPoliza?.toLowerCase().includes(filtroPoliza.value.toLowerCase())
    )
  }

  return filtradas
})

// Métodos
const cargarReclamaciones = async () => {
  loading.value = true
  mensaje.value = ''
  
  try {
    const data = await apiService.getReclamaciones()
    reclamaciones.value = data
    calcularEstadisticas()
    
    if (data.length === 0) {
      mensaje.value = 'No hay reclamaciones registradas en el sistema'
      tipoMensaje.value = 'info'
    }
  } catch (error) {
    console.error('Error al cargar reclamaciones:', error)
    mensaje.value = 'Error al cargar las reclamaciones: ' + error.message
    tipoMensaje.value = 'error'
  } finally {
    loading.value = false
  }
}

const calcularEstadisticas = () => {
  estadisticas.registrada = reclamaciones.value.filter(r => r.estado === 'REGISTRADA').length
  estadisticas.enEvaluacion = reclamaciones.value.filter(r => r.estado === 'EN_EVALUACION').length
  estadisticas.aprobada = reclamaciones.value.filter(r => r.estado === 'APROBADA').length
  estadisticas.rechazada = reclamaciones.value.filter(r => r.estado === 'RECHAZADA').length
  estadisticas.pagada = reclamaciones.value.filter(r => r.estado === 'PAGADA').length
}

const aplicarFiltros = () => {
  // Los filtros se aplican automáticamente a través del computed
}

const verDetalles = (reclamacion) => {
  reclamacionSeleccionada.value = reclamacion
  showDetallesModal.value = true
}

const cerrarDetalles = () => {
  showDetallesModal.value = false
  reclamacionSeleccionada.value = null
}

const cambiarEstado = async (reclamacion, nuevoEstado) => {
  try {
    mensaje.value = ''
    let response = null
    
    switch (nuevoEstado) {
      case 'EN_EVALUACION':
        // Evaluar reclamación - cambiar estado a EN_EVALUACION
        response = await apiService.evaluarReclamacion(reclamacion.idReclamacion, {
          montoAprobado: null,
          observaciones: 'Reclamación puesta en evaluación',
          evaluador: '10'
        })
        break
        
      case 'APROBADA':
        // Aprobar reclamación
        response = await apiService.aprobarReclamacion(reclamacion.idReclamacion, '10')
        break
        
      case 'RECHAZADA':
        // Rechazar reclamación
        const motivo = prompt('Ingrese el motivo del rechazo:')
        if (!motivo || !motivo.trim()) {
          mensaje.value = 'Se requiere un motivo para rechazar la reclamación'
          tipoMensaje.value = 'error'
          return
        }
        response = await apiService.rechazarReclamacion(reclamacion.idReclamacion, motivo, '10')
        break
        
      default:
        throw new Error('Estado no válido')
    }
    
    if (response) {
      // Actualizar la reclamación localmente
      const index = reclamaciones.value.findIndex(r => r.idReclamacion === reclamacion.idReclamacion)
      if (index !== -1) {
        reclamaciones.value[index].estado = nuevoEstado
        calcularEstadisticas()
      }
      
      mensaje.value = `Reclamación ${formatearEstado(nuevoEstado).toLowerCase()} exitosamente`
      tipoMensaje.value = 'success'
      
      // Recargar datos para asegurar consistencia
      setTimeout(() => {
        cargarReclamaciones()
      }, 1000)
    }
  } catch (error) {
    console.error('Error al cambiar estado:', error)
    mensaje.value = 'Error al cambiar el estado: ' + error.message
    tipoMensaje.value = 'error'
  }
}

// Utilidades
const formatearFecha = (fecha) => {
  if (!fecha) return 'N/A'
  return new Date(fecha).toLocaleDateString('es-ES', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

const formatearMonto = (monto) => {
  if (!monto) return 'N/A'
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD'
  }).format(monto)
}

const formatearEstado = (estado) => {
  const estados = {
    'REGISTRADA': 'Registrada',
    'EN_EVALUACION': 'En Evaluación',
    'APROBADA': 'Aprobada',
    'RECHAZADA': 'Rechazada',
    'PAGADA': 'Pagada'
  }
  return estados[estado] || estado
}

const truncarTexto = (texto, limite) => {
  if (!texto) return 'N/A'
  return texto.length > limite ? texto.substring(0, limite) + '...' : texto
}

// Lifecycle
onMounted(() => {
  cargarReclamaciones()
})
</script>

<style scoped>
.gestion-reclamaciones {
  padding: 2rem;
  min-height: 100vh;
}

/* Header */
.page-header {
  margin-bottom: 2rem;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.page-icon {
  width: 2.5rem;
  height: 2.5rem;
  color: #e74c3c;
}

.page-title {
  font-size: 2rem;
  font-weight: 700;
  margin: 0;
  color: var(--text-primary);
}

.page-subtitle {
  font-size: 1rem;
  color: var(--text-secondary);
  margin: 0.25rem 0 0 0;
}

.refresh-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: 0.5rem;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.2s ease;
}

.refresh-button:hover:not(:disabled) {
  background: var(--primary-dark);
  transform: translateY(-1px);
}

.refresh-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.refresh-icon {
  width: 1.25rem;
  height: 1.25rem;
}

.spinning {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* Filtros */
.filters-section {
  background: var(--card-bg);
  padding: 1.5rem;
  border-radius: 1rem;
  margin-bottom: 2rem;
  border: 1px solid var(--border-color);
}

.filters-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.filter-label {
  font-weight: 600;
  color: var(--text-primary);
  font-size: 0.9rem;
}

.filter-select,
.filter-input {
  padding: 0.75rem;
  border: 2px solid var(--border-color);
  border-radius: 0.5rem;
  background: var(--input-bg);
  color: var(--text-primary);
  font-size: 1rem;
  transition: border-color 0.2s ease;
}

.filter-select:focus,
.filter-input:focus {
  outline: none;
  border-color: var(--primary-color);
}

/* Estadísticas */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-card {
  background: var(--card-bg);
  padding: 1.5rem;
  border-radius: 1rem;
  border: 1px solid var(--border-color);
  display: flex;
  align-items: center;
  gap: 1rem;
  transition: transform 0.2s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
}

.stat-icon {
  padding: 0.75rem;
  border-radius: 0.75rem;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-card.registrada .stat-icon {
  background: rgba(52, 152, 219, 0.1);
  color: #3498db;
}

.stat-card.evaluacion .stat-icon {
  background: rgba(241, 196, 15, 0.1);
  color: #f1c40f;
}

.stat-card.aprobada .stat-icon {
  background: rgba(46, 204, 113, 0.1);
  color: #2ecc71;
}

.stat-card.rechazada .stat-icon {
  background: rgba(231, 76, 60, 0.1);
  color: #e74c3c;
}

.stat-icon .icon {
  width: 1.5rem;
  height: 1.5rem;
}

.stat-number {
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-primary);
}

.stat-label {
  font-size: 0.9rem;
  color: var(--text-secondary);
  font-weight: 500;
}

/* Loading */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem;
  gap: 1rem;
}

.spinner {
  width: 3rem;
  height: 3rem;
  border: 3px solid var(--border-color);
  border-top: 3px solid var(--primary-color);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

/* Mensajes */
.mensaje {
  padding: 1rem;
  border-radius: 0.5rem;
  margin-bottom: 1rem;
  font-weight: 500;
}

.mensaje.success {
  background: rgba(46, 204, 113, 0.1);
  color: #2ecc71;
  border: 1px solid rgba(46, 204, 113, 0.3);
}

.mensaje.error {
  background: rgba(231, 76, 60, 0.1);
  color: #e74c3c;
  border: 1px solid rgba(231, 76, 60, 0.3);
}

.mensaje.info {
  background: rgba(52, 152, 219, 0.1);
  color: #3498db;
  border: 1px solid rgba(52, 152, 219, 0.3);
}

/* Tabla */
.table-container {
  background: var(--card-bg);
  border-radius: 1rem;
  border: 1px solid var(--border-color);
  overflow: hidden;
}

.table-header {
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-color);
}

.table-header h3 {
  margin: 0;
  color: var(--text-primary);
  font-weight: 600;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem;
  text-align: center;
  color: var(--text-secondary);
}

.empty-icon {
  width: 4rem;
  height: 4rem;
  margin-bottom: 1rem;
  opacity: 0.5;
}

.table-wrapper {
  overflow-x: auto;
}

.reclamaciones-table {
  width: 100%;
  border-collapse: collapse;
}

.reclamaciones-table th,
.reclamaciones-table td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid var(--border-color);
}

.reclamaciones-table th {
  background: var(--table-header-bg);
  font-weight: 600;
  color: var(--text-primary);
  font-size: 0.9rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.table-row:hover {
  background: var(--row-hover-bg);
}

.poliza-badge {
  background: var(--primary-light);
  color: var(--primary-color);
  padding: 0.25rem 0.5rem;
  border-radius: 0.25rem;
  font-size: 0.8rem;
  font-weight: 600;
}

.monto {
  font-weight: 600;
  color: var(--success-color);
}

.estado-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 1rem;
  font-size: 0.8rem;
  font-weight: 600;
  text-transform: uppercase;
}

.estado-badge.registrada {
  background: rgba(52, 152, 219, 0.1);
  color: #3498db;
}

.estado-badge.en_evaluacion {
  background: rgba(241, 196, 15, 0.1);
  color: #f1c40f;
}

.estado-badge.aprobada {
  background: rgba(46, 204, 113, 0.1);
  color: #2ecc71;
}

.estado-badge.rechazada {
  background: rgba(231, 76, 60, 0.1);
  color: #e74c3c;
}

.estado-badge.pagada {
  background: rgba(155, 89, 182, 0.1);
  color: #9b59b6;
}

.descripcion-cell {
  max-width: 200px;
}

.descripcion-preview {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.actions-container {
  display: flex;
  gap: 0.5rem;
}

.action-button {
  padding: 0.5rem;
  border: none;
  border-radius: 0.25rem;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-button.view {
  background: rgba(52, 152, 219, 0.1);
  color: #3498db;
}

.action-button.evaluate {
  background: rgba(241, 196, 15, 0.1);
  color: #f1c40f;
}

.action-button.approve {
  background: rgba(46, 204, 113, 0.1);
  color: #2ecc71;
}

.action-button.reject {
  background: rgba(231, 76, 60, 0.1);
  color: #e74c3c;
}

.action-button:hover {
  transform: scale(1.1);
}

.action-icon {
  width: 1rem;
  height: 1rem;
}

/* Modal */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 2rem;
}

.modal-content {
  background: var(--card-bg);
  border-radius: 1rem;
  border: 1px solid var(--border-color);
  max-height: 90vh;
  overflow-y: auto;
  width: 100%;
  max-width: 800px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-color);
}

.modal-header h3 {
  margin: 0;
  color: var(--text-primary);
}

.close-button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 0.25rem;
  color: var(--text-secondary);
  transition: all 0.2s ease;
}

.close-button:hover {
  background: var(--border-color);
}

.close-icon {
  width: 1.25rem;
  height: 1.25rem;
}

.modal-body {
  padding: 1.5rem;
}

.detalles-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
  margin-bottom: 2rem;
}

.detalle-section {
  background: var(--section-bg);
  padding: 1.5rem;
  border-radius: 0.5rem;
  border: 1px solid var(--border-color);
}

.detalle-section.full-width {
  grid-column: 1 / -1;
}

.detalle-section h4 {
  margin: 0 0 1rem 0;
  color: var(--text-primary);
  font-weight: 600;
  font-size: 1.1rem;
}

.detalle-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 0;
  border-bottom: 1px solid var(--border-light);
}

.detalle-row:last-child {
  border-bottom: none;
}

.detalle-label {
  font-weight: 500;
  color: var(--text-secondary);
}

.detalle-value {
  color: var(--text-primary);
  font-weight: 600;
}

.monto-grande {
  font-size: 1.25rem;
  color: var(--success-color);
}

.descripcion-completa {
  background: var(--input-bg);
  padding: 1rem;
  border-radius: 0.5rem;
  border: 1px solid var(--border-color);
  color: var(--text-primary);
  line-height: 1.6;
  min-height: 100px;
}

/* Responsive */
@media (max-width: 768px) {
  .gestion-reclamaciones {
    padding: 1rem;
  }
  
  .header-content {
    flex-direction: column;
    gap: 1rem;
    align-items: flex-start;
  }
  
  .filters-grid {
    grid-template-columns: 1fr;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .detalles-grid {
    grid-template-columns: 1fr;
  }
  
  .reclamaciones-table {
    font-size: 0.8rem;
  }
  
  .reclamaciones-table th,
  .reclamaciones-table td {
    padding: 0.5rem;
  }
}

/* Tema oscuro */
.theme-dark {
  --text-primary: #ffffff;
  --text-secondary: #a0a0a0;
  --card-bg: #2d3748;
  --border-color: #4a5568;
  --border-light: #2d3748;
  --input-bg: #1a202c;
  --section-bg: #1a202c;
  --table-header-bg: #1a202c;
  --row-hover-bg: #2d3748;
  --primary-color: #3182ce;
  --primary-dark: #2c5aa0;
  --primary-light: rgba(49, 130, 206, 0.1);
  --success-color: #68d391;
}

/* Tema claro */
.theme-light {
  --text-primary: #2d3748;
  --text-secondary: #718096;
  --card-bg: #ffffff;
  --border-color: #e2e8f0;
  --border-light: #f7fafc;
  --input-bg: #ffffff;
  --section-bg: #f7fafc;
  --table-header-bg: #f7fafc;
  --row-hover-bg: #f7fafc;
  --primary-color: #3182ce;
  --primary-dark: #2c5aa0;
  --primary-light: rgba(49, 130, 206, 0.1);
  --success-color: #38a169;
}
</style>

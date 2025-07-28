<template>
  <div class="polizas-container">
    <!-- Header RF-002 -->
    <div :class="['header-card', cardClass]">
      <div class="header-content">
        <div :class="['icon-container', iconContainerClass]">
          <Shield class="header-icon" />
        </div>
        <div class="header-text">
          <h2 class="header-title">RF-002: Gestión de Pólizas</h2>
          <p class="header-subtitle">Panel de administración y control de pólizas</p>
        </div>
      </div>
    </div>

    <!-- Indicador de carga -->
    <div v-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>Cargando datos...</p>
    </div>

    <!-- Mensaje de error -->
    <div v-if="error" class="error-message">
      <p>{{ error }}</p>
      <button @click="cargarDatos" class="retry-button">Reintentar</button>
    </div>

    <div class="main-grid">
      <!-- Panel de Control -->
      <div :class="['control-card', cardClass]">
        <div class="card-header">
          <Shield :class="['card-icon', iconSecondaryClass]" />
          <h3 class="card-title">Panel de Control</h3>
        </div>

        <!-- Estadísticas -->
        <div class="stats-grid">
          <div :class="['stats-card', statsCardClass]">
            <div class="stats-number">{{ polizas.length }}</div>
            <div class="stats-label">Total Pólizas</div>
          </div>
          <div :class="['stats-card', statsCardClass]">
            <div class="stats-number">{{ polizasPendientes.length }}</div>
            <div class="stats-label">Pendientes</div>
          </div>
          <div :class="['stats-card', statsCardClass]">
            <div class="stats-number">{{ polizasAprobadas.length }}</div>
            <div class="stats-label">Aprobadas</div>
          </div>
          <div :class="['stats-card', statsCardClass]">
            <div class="stats-number">{{ polizasRechazadas.length }}</div>
            <div class="stats-label">Rechazadas</div>
          </div>
        </div>

        <!-- Características Automáticas -->
        <div class="features-section">
          <h4 class="features-title">Características del Sistema:</h4>
          <div :class="['feature-card', featureCardGreen]">
            <div class="feature-content">
              <CheckCircle2 class="feature-icon green" />
              <span>Gestión automática de estados</span>
            </div>
          </div>
          <div :class="['feature-card', featureCardBlue]">
            <div class="feature-content">
              <CheckCircle2 class="feature-icon blue" />
              <span>Validación automática de datos</span>
            </div>
          </div>
          <div :class="['feature-card', featureCardYellow]">
            <div class="feature-content">
              <CheckCircle2 class="feature-icon yellow" />
              <span>Asignación automática de agentes</span>
          </div>
        </div>
      </div>
    </div>

      <!-- Lista de Pólizas con Filtros -->
    <div :class="['list-card', cardClass]">
      <div class="card-header">
        <FileText :class="['card-icon', iconTertiaryClass]" />
        <h3 class="card-title">Pólizas Registradas</h3>
      </div>

        <!-- Filtros de Búsqueda -->
        <div class="filters-section">
          <div class="filters-grid">
            <!-- Filtro por Estado -->
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

            <!-- Filtro por Cliente -->
            <div class="filter-group">
              <label class="filter-label">Cliente</label>
              <input
                v-model="filtros.cliente"
                type="text"
                :class="['filter-input', inputClass]"
                placeholder="Buscar por nombre..."
              />
            </div>

            <!-- Filtro por Agente -->
            <div class="filter-group">
              <label class="filter-label">Agente</label>
              <select v-model="filtros.agente" :class="['filter-input', inputClass]">
                <option value="">Todos los agentes</option>
                <option v-for="agente in agentes" :key="agente.id" :value="agente.id">
                  {{ agente.nombre }}
                </option>
              </select>
            </div>

            <!-- Filtro por Tipo de Seguro -->
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

            <!-- Filtro por Rango de Prima -->
            <div class="filter-group">
              <label class="filter-label">Prima Mínima</label>
              <input
                v-model="filtros.primaMin"
                type="number"
                step="0.01"
                :class="['filter-input', inputClass]"
                placeholder="0.00"
              />
            </div>

            <div class="filter-group">
              <label class="filter-label">Prima Máxima</label>
              <input
                v-model="filtros.primaMax"
                type="number"
                step="0.01"
                :class="['filter-input', inputClass]"
                placeholder="9999.99"
              />
            </div>
          </div>

          <!-- Botones de Filtro -->
          <div class="filter-actions">
            <button @click="limpiarFiltros" :class="['filter-button', buttonSecondaryClass]">
              Limpiar Filtros
            </button>
            <button @click="aplicarFiltros" :class="['filter-button', buttonPrimaryClass]">
              Aplicar Filtros
            </button>
          </div>
        </div>

        <!-- Contador de resultados -->
        <div class="results-info">
          <p>Mostrando {{ polizasFiltradas.length }} de {{ polizas.length }} pólizas</p>
        </div>

      <div class="polizas-list">
        <div
          v-for="poliza in polizasFiltradas"
          :key="poliza.id"
          :class="['poliza-item', itemCardClass]"
        >
          <div class="poliza-content">
            <div class="poliza-info">
              <div class="poliza-header">
                <h4 class="poliza-nombre">{{ poliza.clienteNombre }}</h4>
                <div :class="['status-badge', getStatusBadgeClass(poliza.estado)]">
                  <component :is="getStatusIcon(poliza.estado)" class="status-icon" />
                  {{ poliza.estado }}
                </div>
              </div>
              <div class="poliza-details">
                  <div class="detail-item">ID: {{ poliza.idPoliza }}</div>
                  <div class="detail-item">Número: {{ poliza.numeroPoliza }}</div>
                <div class="detail-item">Tipo: {{ poliza.tipo }}</div>
                <div class="detail-item">Prima: ${{ poliza.primaMensual }}</div>
                <div class="detail-item">Agente: {{ poliza.agenteNombre }}</div>
                  <div class="detail-item">Email: {{ poliza.clienteEmail }}</div>
                  <div class="detail-item">Teléfono: {{ poliza.clienteTelefono }}</div>
                <div class="detail-item">Observaciones: {{ poliza.observaciones }}</div>
              </div>
            </div>
            <div class="poliza-actions" v-if="poliza.estado === 'PENDIENTE'">
              <button
                  @click="aprobarPoliza(poliza.idPoliza)"
                :class="['action-button', 'approve-button', approveButtonClass]"
              >
                Aprobar
              </button>
              <button
                  @click="mostrarModalRechazo(poliza.idPoliza)"
                :class="['action-button', 'reject-button', rejectButtonClass]"
              >
                Rechazar
              </button>
            </div>
          </div>
          </div>
        </div>

        <!-- Mensaje cuando no hay resultados -->
        <div v-if="polizasFiltradas.length === 0 && !loading" class="no-results">
          <p>No se encontraron pólizas con los filtros aplicados</p>
        </div>
      </div>
    </div>

    <!-- Modal de Rechazo -->
    <div v-if="showRechazoModal" class="modal-overlay" @click="cerrarModalRechazo">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>Rechazar Póliza</h3>
          <button @click="cerrarModalRechazo" class="modal-close">&times;</button>
        </div>
        <div class="modal-body">
          <p>¿Está seguro que desea rechazar esta póliza?</p>
          <div class="form-group">
            <label for="motivoRechazo">Motivo del rechazo (opcional):</label>
            <textarea 
              id="motivoRechazo"
              v-model="motivoRechazo" 
              placeholder="Especifique el motivo del rechazo..."
              rows="3"
              class="form-textarea"
            ></textarea>
          </div>
        </div>
        <div class="modal-footer">
          <button @click="cerrarModalRechazo" class="btn-secondary">Cancelar</button>
          <button @click="confirmarRechazo" class="btn-danger">Confirmar Rechazo</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { FileText, Shield, CheckCircle2, Clock, XCircle, AlertCircle } from 'lucide-vue-next'
import apiService from '../services/apiService.js'

const props = defineProps({
  isDark: Boolean
})

const polizas = ref([])
const agentes = ref([])
const loading = ref(false)
const error = ref(null)

// Variables para el modal de rechazo
const showRechazoModal = ref(false)
const motivoRechazo = ref('')
const polizaRechazando = ref(null)

// Filtros
const filtros = ref({
  estado: '',
  cliente: '',
  agente: '',
  tipoSeguro: '',
  primaMin: '',
  primaMax: ''
})

// Computed properties
const polizasFiltradas = computed(() => {
  let resultado = polizas.value

  // Filtro por estado
  if (filtros.value.estado) {
    resultado = resultado.filter(poliza => poliza.estado === filtros.value.estado)
  }

  // Filtro por cliente (nombre)
  if (filtros.value.cliente) {
    const busqueda = filtros.value.cliente.toLowerCase()
    resultado = resultado.filter(poliza => 
      poliza.clienteNombre.toLowerCase().includes(busqueda) ||
      poliza.clienteEmail.toLowerCase().includes(busqueda)
    )
  }

  // Filtro por agente
  if (filtros.value.agente) {
    resultado = resultado.filter(poliza => poliza.agenteId === filtros.value.agente)
  }

  // Filtro por tipo de seguro
  if (filtros.value.tipoSeguro) {
    resultado = resultado.filter(poliza => poliza.tipo === filtros.value.tipoSeguro)
  }

  // Filtro por prima mínima
  if (filtros.value.primaMin) {
    const primaMin = parseFloat(filtros.value.primaMin)
    resultado = resultado.filter(poliza => poliza.primaMensual >= primaMin)
  }

  // Filtro por prima máxima
  if (filtros.value.primaMax) {
    const primaMax = parseFloat(filtros.value.primaMax)
    resultado = resultado.filter(poliza => poliza.primaMensual <= primaMax)
  }

  return resultado
})

const polizasPendientes = computed(() => {
  return polizas.value.filter(poliza => poliza.estado === 'PENDIENTE')
})

const polizasAprobadas = computed(() => {
  return polizas.value.filter(poliza => poliza.estado === 'APROBADA')
})

const polizasRechazadas = computed(() => {
  return polizas.value.filter(poliza => poliza.estado === 'RECHAZADA')
})

// Estilos computados
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

const iconSecondaryClass = computed(() => {
  return props.isDark ? 'dark-icon-secondary' : 'light-icon-secondary'
})

const iconTertiaryClass = computed(() => {
  return props.isDark ? 'dark-icon-tertiary' : 'light-icon-tertiary'
})

const itemCardClass = computed(() => {
  return props.isDark ? 'dark-item-card' : 'light-item-card'
})

const statsCardClass = computed(() => {
  return props.isDark ? 'dark-stats-card' : 'light-stats-card'
})

const featureCardGreen = computed(() => {
  return props.isDark ? 'dark-feature-green' : 'light-feature-green'
})

const featureCardBlue = computed(() => {
  return props.isDark ? 'dark-feature-blue' : 'light-feature-blue'
})

const featureCardYellow = computed(() => {
  return props.isDark ? 'dark-feature-yellow' : 'light-feature-yellow'
})

const approveButtonClass = computed(() => {
  return props.isDark ? 'dark-approve-button' : 'light-approve-button'
})

const rejectButtonClass = computed(() => {
  return props.isDark ? 'dark-reject-button' : 'light-reject-button'
})

// Métodos
const cargarDatos = async () => {
  loading.value = true
  error.value = null
  
  try {
    // Cargar agentes, pólizas y clientes en paralelo
    const [agentesData, polizasData, clientesData] = await Promise.all([
      apiService.getAgentes(),
      apiService.getPolizas(),
      apiService.getClientes()
    ])
    
    agentes.value = agentesData.map(agente => ({
      id: agente.idAgente,
      nombre: agente.nombre,
      email: agente.email,
      telefono: agente.telefono,
      codigo: agente.codigo
    }))
    
    // Crear un mapa de clientes para búsqueda rápida
    const clientesMap = new Map()
    clientesData.forEach(cliente => {
      clientesMap.set(cliente.idCliente, cliente)
    })
    
    polizas.value = polizasData.map(poliza => {
      const cliente = clientesMap.get(poliza.clienteId)
      const polizaMapeada = {
        idPoliza: poliza.idPoliza,
        numeroPoliza: poliza.numeroPoliza,
        clienteNombre: cliente ? cliente.nombre : 'Cliente no encontrado',
        clienteEmail: cliente ? cliente.email : 'cliente@email.com',
        clienteTelefono: cliente ? cliente.telefono : '+1234567890',
        agenteId: poliza.agenteId,
        agenteNombre: obtenerNombreAgente(poliza.agenteId),
        tipo: poliza.tipoSeguro || 'AUTO',
        primaMensual: poliza.prima,
        estado: poliza.estado,
        fechaCreacion: poliza.fechaEmision,
        observaciones: poliza.observaciones || 'Sin observaciones',
        validacionCompleta: true
      }
      console.log('Póliza mapeada:', polizaMapeada)
      return polizaMapeada
    })
    
  } catch (err) {
    error.value = 'Error al cargar datos: ' + err.message
    console.error('Error cargando datos:', err)
  } finally {
    loading.value = false
  }
}

const aprobarPoliza = async (id) => {
  try {
    console.log(`Aprobando póliza ${id}`)
    console.log('Tipo de ID:', typeof id)
    console.log('ID recibido:', id)
    
    // Verificar que el ID sea válido
    if (!id || isNaN(id)) {
      throw new Error(`ID de póliza inválido: ${id}`)
    }
    
    const polizaActualizada = await apiService.actualizarEstadoPoliza(id, 'APROBADA')
    
    // Actualizar estado local
    const polizaIndex = polizas.value.findIndex(p => p.idPoliza === id)
    if (polizaIndex !== -1) {
      polizas.value[polizaIndex].estado = 'APROBADA'
      console.log(`Estado actualizado localmente para póliza ${id}`)
    } else {
      console.warn(`No se encontró la póliza con ID ${id} en la lista local`)
    }
    
    // Mostrar mensaje de éxito
    alert('Póliza aprobada exitosamente')
    
  } catch (err) {
    error.value = 'Error al aprobar póliza: ' + err.message
    console.error('Error aprobando póliza:', err)
    alert('Error al aprobar póliza: ' + err.message)
  }
}

const mostrarModalRechazo = (id) => {
  polizaRechazando.value = id
  motivoRechazo.value = ''
  showRechazoModal.value = true
}

const cerrarModalRechazo = () => {
  showRechazoModal.value = false
  polizaRechazando.value = null
  motivoRechazo.value = ''
}

const confirmarRechazo = async () => {
  if (!polizaRechazando.value) return
  
  try {
    console.log(`Rechazando póliza ${polizaRechazando.value} con motivo: ${motivoRechazo.value}`)
    
    const polizaActualizada = await apiService.rechazarPoliza(polizaRechazando.value, motivoRechazo.value)
    
    // Actualizar estado local
    const polizaIndex = polizas.value.findIndex(p => p.idPoliza === polizaRechazando.value)
    if (polizaIndex !== -1) {
      polizas.value[polizaIndex].estado = 'RECHAZADA'
      if (motivoRechazo.value.trim()) {
        polizas.value[polizaIndex].observaciones = motivoRechazo.value
      }
      console.log(`Estado actualizado localmente para póliza ${polizaRechazando.value}`)
    }
    
    // Cerrar modal y mostrar mensaje de éxito
    cerrarModalRechazo()
    alert('Póliza rechazada exitosamente')
    
  } catch (err) {
    error.value = 'Error al rechazar póliza: ' + err.message
    console.error('Error rechazando póliza:', err)
    alert('Error al rechazar póliza: ' + err.message)
  }
}

const obtenerNombreAgente = (agenteId) => {
  if (!agenteId) return 'No asignado'
  const agente = agentes.value.find(a => a.id === agenteId)
  return agente ? agente.nombre : 'No asignado'
}

const limpiarFiltros = () => {
  filtros.value = {
    estado: '',
    cliente: '',
    agente: '',
    tipoSeguro: '',
    primaMin: '',
    primaMax: ''
  }
}

const aplicarFiltros = () => {
  // Los filtros se aplican automáticamente por computed
  console.log('Filtros aplicados:', filtros.value)
}

const getStatusIcon = (estado) => {
  switch (estado) {
    case 'PENDIENTE': return Clock
    case 'APROBADA': return CheckCircle2
    case 'RECHAZADA': return XCircle
    case 'CANCELADA': return AlertCircle
    default: return Clock
  }
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
  cargarDatos()
})
</script>

<style scoped>
/* Contenedor principal */
.polizas-container {
  display: flex;
  flex-direction: column;
  gap: 2rem;
  padding: 1rem;
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

/* Grid principal */
.main-grid {
  display: grid;
  gap: 2rem;
  grid-template-columns: 1fr;
}

/* Tarjetas */
.form-card,
.control-card,
.list-card {
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

/* Formulario */
.form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
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

.form-input:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
}

.submit-button {
  width: 100%;
  padding: 0.75rem 1rem;
  border-radius: 0.75rem;
  font-weight: 600;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
}

.submit-button:hover {
  transform: scale(1.02);
}

/* Filtros */
.filters-section {
  margin-bottom: 2rem;
  padding: 1.5rem;
  border-radius: 0.75rem;
  border: 1px solid;
}

.filters-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1rem;
  margin-bottom: 1rem;
}

.filter-group {
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
  gap: 1rem;
  justify-content: flex-end;
}

.filter-button {
  padding: 0.75rem 1.5rem;
  border-radius: 0.75rem;
  font-weight: 600;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
}

.filter-button:hover {
  transform: scale(1.02);
}

.results-info {
  margin-bottom: 1rem;
  padding: 0.75rem;
  border-radius: 0.5rem;
  text-align: center;
  font-size: 0.875rem;
  opacity: 0.8;
}

.no-results {
  text-align: center;
  padding: 2rem;
  opacity: 0.6;
}

/* Tema claro */
.theme-light .filters-section {
  background: rgba(254, 247, 237, 0.3);
  border-color: rgba(253, 186, 116, 0.3);
}

.theme-light .filter-input {
  background: rgba(254, 247, 237, 0.5);
  border-color: rgba(253, 186, 116, 0.5);
  color: #9a3412;
}

.theme-light .filter-input:focus {
  border-color: #fb923c;
  box-shadow: 0 0 0 2px rgba(251, 146, 60, 0.1);
  background: rgba(254, 247, 237, 0.8);
}

.theme-light .results-info {
  background: rgba(254, 247, 237, 0.3);
}

/* Tema oscuro */
.theme-dark .filters-section {
  background: rgba(51, 65, 85, 0.3);
  border-color: rgba(71, 85, 105, 0.3);
}

.theme-dark .filter-input {
  background: rgba(30, 41, 59, 0.5);
  border-color: rgba(71, 85, 105, 0.5);
  color: #e2e8f0;
}

.theme-dark .filter-input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.1);
  background: rgba(30, 41, 59, 0.8);
}

.theme-dark .results-info {
  background: rgba(51, 65, 85, 0.3);
}

/* Estadísticas */
.stats-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.stats-card {
  padding: 1rem;
  border-radius: 0.75rem;
  text-align: center;
}

.stats-number {
  font-size: 1.5rem;
  font-weight: bold;
}

.stats-label {
  font-size: 0.875rem;
  opacity: 0.7;
}

/* Características */
.features-section {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.features-title {
  font-weight: 600;
  font-size: 0.875rem;
  opacity: 0.8;
  margin: 0;
}

.feature-card {
  padding: 0.75rem;
  border-radius: 0.5rem;
  border: 1px solid;
}

.feature-content {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
}

.feature-icon {
  width: 1rem;
  height: 1rem;
}

.feature-icon.green {
  color: #10b981;
}

.feature-icon.blue {
  color: #3b82f6;
}

.feature-icon.yellow {
  color: #f59e0b;
}

/* Lista de pólizas */
.polizas-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.poliza-item {
  padding: 1rem;
  border-radius: 0.75rem;
  border: 1px solid;
  transition: all 0.2s ease;
}

.poliza-item:hover {
  transform: scale(1.01);
}

.poliza-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.poliza-info {
  flex: 1;
}

.poliza-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 0.5rem;
}

.poliza-nombre {
  font-weight: 600;
  margin: 0;
}

.status-badge {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  font-size: 0.75rem;
  font-weight: 500;
}

.status-icon {
  width: 1rem;
  height: 1rem;
}

.poliza-details {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  font-size: 0.875rem;
  opacity: 0.8;
}

.poliza-details .detail-item:last-child {
  grid-column: 1 / -1; /* Las observaciones ocupan toda la fila */
}

.detail-item {
  margin: 0;
}

.poliza-actions {
  display: flex;
  gap: 0.5rem;
}

.action-button {
  padding: 0.25rem 0.75rem;
  border-radius: 0.5rem;
  font-size: 0.75rem;
  font-weight: 500;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
}

.action-button:hover {
  transform: scale(1.05);
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

.light-button-primary:hover {
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  box-shadow: 0 6px 20px rgba(251, 146, 60, 0.4);
}

.light-button-secondary {
  background: rgba(107, 114, 128, 0.8);
  color: #f9fafb;
  box-shadow: 0 4px 15px rgba(107, 114, 128, 0.3);
}

.light-button-secondary:hover {
  background: rgba(75, 85, 99, 0.9);
  box-shadow: 0 6px 20px rgba(107, 114, 128, 0.4);
}

.light-icon-container {
  background: rgba(251, 146, 60, 0.2);
  color: #ea580c;
}

.light-icon {
  color: #ea580c;
}

.light-icon-secondary {
  color: #16a34a;
}

.light-icon-tertiary {
  color: #9333ea;
}

.light-item-card {
  background: rgba(254, 247, 237, 0.3);
  border-color: rgba(253, 186, 116, 0.3);
}

.light-stats-card {
  background: rgba(254, 247, 237, 0.3);
}

.light-feature-green {
  background: rgba(34, 197, 94, 0.1);
  border-color: rgba(34, 197, 94, 0.2);
}

.light-feature-blue {
  background: rgba(59, 130, 246, 0.1);
  border-color: rgba(59, 130, 246, 0.2);
}

.light-feature-yellow {
  background: rgba(245, 158, 11, 0.1);
  border-color: rgba(245, 158, 11, 0.2);
}

.light-approve-button {
  background: #16a34a;
  color: #f0fdf4;
}

.light-approve-button:hover {
  background: #15803d;
}

.light-reject-button {
  background: #dc2626;
  color: #fef2f2;
}

.light-reject-button:hover {
  background: #b91c1c;
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

.dark-button-primary:hover {
  background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.4);
}

.dark-button-secondary {
  background: rgba(107, 114, 128, 0.8);
  color: #f9fafb;
  box-shadow: 0 4px 15px rgba(107, 114, 128, 0.3);
}

.dark-button-secondary:hover {
  background: rgba(75, 85, 99, 0.9);
  box-shadow: 0 6px 20px rgba(107, 114, 128, 0.4);
}

.dark-icon-container {
  background: rgba(59, 130, 246, 0.2);
  color: #93c5fd;
}

.dark-icon {
  color: #93c5fd;
}

.dark-icon-secondary {
  color: #4ade80;
}

.dark-icon-tertiary {
  color: #a78bfa;
}

.dark-item-card {
  background: rgba(51, 65, 85, 0.3);
  border-color: rgba(71, 85, 105, 0.3);
}

.dark-stats-card {
  background: rgba(51, 65, 85, 0.3);
}

.dark-feature-green {
  background: rgba(34, 197, 94, 0.1);
  border-color: rgba(34, 197, 94, 0.2);
}

.dark-feature-blue {
  background: rgba(59, 130, 246, 0.1);
  border-color: rgba(59, 130, 246, 0.2);
}

.dark-feature-yellow {
  background: rgba(245, 158, 11, 0.1);
  border-color: rgba(245, 158, 11, 0.2);
}

.dark-approve-button {
  background: #16a34a;
  color: #f0fdf4;
}

.dark-approve-button:hover {
  background: #15803d;
}

.dark-reject-button {
  background: #dc2626;
  color: #fef2f2;
}

.dark-reject-button:hover {
  background: #b91c1c;
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

/* Responsive */
@media (max-width: 768px) {
  .polizas-container {
    padding: 0.5rem;
  }
  
  .main-grid {
    grid-template-columns: 1fr;
  }
  
  .poliza-content {
    flex-direction: column;
    gap: 1rem;
  }
  
  .poliza-details {
    grid-template-columns: 1fr;
  }
  
  .poliza-actions {
    width: 100%;
    justify-content: center;
  }
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
}

.modal-content {
  background: var(--card-bg, #ffffff);
  border-radius: 1rem;
  padding: 0;
  max-width: 500px;
  width: 90%;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-color, #e5e7eb);
}

.modal-header h3 {
  margin: 0;
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--text-primary, #111827);
}

.modal-close {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: var(--text-secondary, #6b7280);
  padding: 0;
  width: 2rem;
  height: 2rem;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 0.5rem;
  transition: all 0.2s ease;
}

.modal-close:hover {
  background: var(--hover-bg, #f3f4f6);
  color: var(--text-primary, #111827);
}

.modal-body {
  padding: 1.5rem;
}

.modal-body p {
  margin: 0 0 1rem 0;
  color: var(--text-primary, #111827);
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: var(--text-primary, #111827);
}

.form-textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid var(--border-color, #d1d5db);
  border-radius: 0.5rem;
  font-family: inherit;
  font-size: 0.875rem;
  resize: vertical;
  background: var(--input-bg, #ffffff);
  color: var(--text-primary, #111827);
}

.form-textarea:focus {
  outline: none;
  border-color: var(--primary-color, #3b82f6);
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding: 1.5rem;
  border-top: 1px solid var(--border-color, #e5e7eb);
}

.btn-secondary {
  padding: 0.75rem 1.5rem;
  border: 1px solid var(--border-color, #d1d5db);
  border-radius: 0.5rem;
  background: var(--button-secondary-bg, #ffffff);
  color: var(--text-primary, #111827);
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-secondary:hover {
  background: var(--button-secondary-hover, #f9fafb);
}

.btn-danger {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 0.5rem;
  background: linear-gradient(135deg, #ef4444, #dc2626);
  color: white;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-danger:hover {
  background: linear-gradient(135deg, #dc2626, #b91c1c);
}
</style>
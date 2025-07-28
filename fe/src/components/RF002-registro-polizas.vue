<template>
  <div class="polizas-container">
    <!-- Header RF-002 -->
    <div :class="['header-card', cardClass]">
      <div class="header-content">
        <div :class="['icon-container', iconContainerClass]">
          <Shield class="header-icon" />
        </div>
        <div class="header-text">
          <h2 class="header-title">RF-002: Automatización del Registro de Pólizas</h2>
          <p class="header-subtitle">Creación automática con validación y asignación de estado</p>
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
      <!-- Formulario de Creación -->
      <div :class="['form-card', cardClass]">
        <div class="card-header">
          <FileText :class="['card-icon', iconClass]" />
          <h3 class="card-title">Crear Nueva Póliza</h3>
        </div>

        <form @submit.prevent="crearPoliza" class="form">
          <div class="form-group">
            <label class="form-label">Nombre del Cliente</label>
            <input
              v-model="nuevaPoliza.clienteNombre"
              type="text"
              :class="['form-input', inputClass]"
              placeholder="Nombre completo"
              required
            />
          </div>

          <div class="form-group">
            <label class="form-label">Email del Cliente</label>
            <input
              v-model="nuevaPoliza.clienteEmail"
              type="email"
              :class="['form-input', inputClass]"
              placeholder="cliente@email.com"
              required
            />
          </div>

          <div class="form-group">
            <label class="form-label">Teléfono</label>
            <input
              v-model="nuevaPoliza.clienteTelefono"
              type="tel"
              :class="['form-input', inputClass]"
              placeholder="+1234567890"
              required
            />
          </div>

          <div class="form-group">
            <label class="form-label">Agente Asignado</label>
            <select v-model="nuevaPoliza.agenteId" :class="['form-input', inputClass]" required>
              <option value="">Seleccionar agente</option>
              <option v-for="agente in agentes" :key="agente.id" :value="agente.id">
                {{ agente.nombre }}
              </option>
            </select>
          </div>



          <div class="form-group">
            <label class="form-label">Prima Mensual ($)</label>
            <input
              v-model="nuevaPoliza.primaMensual"
              type="number"
              step="0.01"
              :class="['form-input', inputClass]"
              placeholder="0.00"
              required
            />
          </div>

          <button
            type="submit"
            :class="['submit-button', buttonPrimaryClass]"
            :disabled="loading"
          >
            {{ loading ? 'Creando Póliza...' : 'Crear Póliza (Auto-validación y Estado PENDIENTE)' }}
          </button>
        </form>
      </div>

      <!-- Panel de Control -->
      <div :class="['control-card', cardClass]">
        <div class="card-header">
          <Shield :class="['card-icon', iconSecondaryClass]" />
          <h3 class="card-title">Panel de Control</h3>
        </div>

        <!-- Filtro por Estado -->
        <div class="filter-section">
          <label class="form-label">Filtrar por Estado</label>
          <select v-model="filtroEstado" :class="['form-input', inputClass]">
            <option value="">Todos los estados</option>
            <option value="PENDIENTE">PENDIENTE</option>
            <option value="APROBADA">APROBADA</option>
            <option value="RECHAZADA">RECHAZADA</option>
            <option value="CANCELADA">CANCELADA</option>
          </select>
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
        </div>

        <!-- Características Automáticas -->
        <div class="features-section">
          <h4 class="features-title">Características Automáticas:</h4>
          <div :class="['feature-card', featureCardGreen]">
            <div class="feature-content">
              <CheckCircle2 class="feature-icon green" />
              <span>Generación automática de ID único</span>
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
              <span>Estado PENDIENTE automático</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Lista de Pólizas -->
    <div :class="['list-card', cardClass]">
      <div class="card-header">
        <FileText :class="['card-icon', iconTertiaryClass]" />
        <h3 class="card-title">Pólizas Registradas</h3>
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
                <div class="detail-item">ID: {{ poliza.id }}</div>
                <div class="detail-item">Tipo: {{ poliza.tipo }}</div>
                <div class="detail-item">Prima: ${{ poliza.primaMensual }}</div>
                <div class="detail-item">Agente: {{ poliza.agenteNombre }}</div>
                <div class="detail-item">Observaciones: {{ poliza.observaciones }}</div>
              </div>
            </div>
            <div class="poliza-actions" v-if="poliza.estado === 'PENDIENTE'">
              <button
                @click="cambiarEstadoPoliza(poliza.id, 'APROBADA')"
                :class="['action-button', 'approve-button', approveButtonClass]"
              >
                Aprobar
              </button>
              <button
                @click="cambiarEstadoPoliza(poliza.id, 'RECHAZADA')"
                :class="['action-button', 'reject-button', rejectButtonClass]"
              >
                Rechazar
              </button>
            </div>
          </div>
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
const nuevaPoliza = ref({
  clienteNombre: '',
  clienteEmail: '',
  clienteTelefono: '',
  agenteId: '',
  primaMensual: ''
})
const agentes = ref([])
const filtroEstado = ref('')
const loading = ref(false)
const error = ref(null)

// Computed properties
const polizasFiltradas = computed(() => {
  if (!filtroEstado.value) return polizas.value
  return polizas.value.filter(poliza => poliza.estado === filtroEstado.value)
})

const polizasPendientes = computed(() => {
  return polizas.value.filter(poliza => poliza.estado === 'PENDIENTE')
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

const iconContainerClass = computed(() => {
  return props.isDark ? 'dark-icon-container' : 'light-icon-container'
})

const iconClass = computed(() => {
  return props.isDark ? 'dark-icon' : 'light-icon'
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
      return {
        id: poliza.idPoliza,
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
    })
    
  } catch (err) {
    error.value = 'Error al cargar datos: ' + err.message
    console.error('Error cargando datos:', err)
  } finally {
    loading.value = false
  }
}

const validarDatos = (datos) => {
  const errores = []

  if (!datos.clienteNombre || datos.clienteNombre.length < 2) {
    errores.push('Nombre del cliente debe tener al menos 2 caracteres')
  }

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(datos.clienteEmail)) {
    errores.push('Email inválido')
  }

  const telefonoRegex = /^\+?[\d\s-()]{10,}$/
  if (!telefonoRegex.test(datos.clienteTelefono)) {
    errores.push('Teléfono inválido')
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
  const errores = validarDatos(nuevaPoliza.value)

  if (errores.length > 0) {
    alert('Errores de validación:\n' + errores.join('\n'))
    return
  }

  loading.value = true
  error.value = null
  
  try {
    // Primero crear el cliente
    const clienteData = {
      nombre: nuevaPoliza.value.clienteNombre,
      email: nuevaPoliza.value.clienteEmail,
      telefono: nuevaPoliza.value.clienteTelefono
    }
    
    const clienteCreado = await apiService.crearCliente(clienteData)
    
    // Luego crear la póliza
    const polizaData = {
      clienteId: clienteCreado.idCliente,
      agenteId: nuevaPoliza.value.agenteId,
      tipoSeguro: 'AUTO', // Siempre será seguro de auto
      prima: parseFloat(nuevaPoliza.value.primaMensual),
      observaciones: 'Póliza de auto creada automáticamente'
    }
    
    const polizaCreada = await apiService.crearPoliza(polizaData)
    
    // Agregar la póliza a la lista local
    const nuevaPolizaCompleta = {
      id: polizaCreada.idPoliza,
      numeroPoliza: polizaCreada.numeroPoliza,
      clienteNombre: nuevaPoliza.value.clienteNombre,
      clienteEmail: nuevaPoliza.value.clienteEmail,
      clienteTelefono: nuevaPoliza.value.clienteTelefono,
      agenteId: nuevaPoliza.value.agenteId,
      agenteNombre: obtenerNombreAgente(nuevaPoliza.value.agenteId),
      tipo: 'AUTO',
      primaMensual: parseFloat(nuevaPoliza.value.primaMensual),
      estado: 'PENDIENTE',
      fechaCreacion: new Date().toISOString(),
      observaciones: 'Póliza de auto creada automáticamente',
      validacionCompleta: true
    }
    
    polizas.value.push(nuevaPolizaCompleta)
    
    // Limpiar formulario
    nuevaPoliza.value = {
      clienteNombre: '',
      clienteEmail: '',
      clienteTelefono: '',
      agenteId: '',
      primaMensual: ''
    }
    
    alert(`Póliza creada exitosamente!\nID: ${polizaCreada.numeroPoliza}\nEstado: PENDIENTE`)
    
  } catch (err) {
    error.value = 'Error al crear póliza: ' + err.message
    console.error('Error creando póliza:', err)
  } finally {
    loading.value = false
  }
}

const cambiarEstadoPoliza = async (id, nuevoEstado) => {
  try {
    await apiService.actualizarEstadoPoliza(id, nuevoEstado)
    
    // Actualizar estado local
    const polizaIndex = polizas.value.findIndex(p => p.id === id)
    if (polizaIndex !== -1) {
      polizas.value[polizaIndex].estado = nuevoEstado
    }
    
  } catch (err) {
    error.value = 'Error al cambiar estado: ' + err.message
    console.error('Error cambiando estado:', err)
  }
}

const obtenerNombreAgente = (agenteId) => {
  if (!agenteId) return 'No asignado'
  const agente = agentes.value.find(a => a.id === agenteId)
  return agente ? agente.nombre : 'No asignado'
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
}

@media (min-width: 1024px) {
  .main-grid {
    grid-template-columns: 1fr 1fr;
  }
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

/* Filtro */
.filter-section {
  margin-bottom: 1.5rem;
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
</style>
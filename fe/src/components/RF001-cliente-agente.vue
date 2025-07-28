<template>
  <div class="clientes-container">
    <!-- Header RF-001 -->
    <div :class="['header-card', cardClass]">
      <div class="header-content">
        <div :class="['icon-container', iconContainerClass]">
          <Users class="header-icon" />
        </div>
        <div class="header-text">
          <h2 class="header-title">RF-001: Gestión de Clientes y Agentes</h2>
          <p class="header-subtitle">Registro automático y vinculación cliente-agente</p>
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
      <!-- Lista de Agentes -->
      <div :class="['agents-card', cardClass]">
        <div class="card-header">
          <Users :class="['card-icon', iconSecondaryClass]" />
          <h3 class="card-title">Agentes Disponibles</h3>
        </div>

        <div class="agents-list">
          <div
            v-for="agente in agentes"
            :key="agente.id"
            :class="['agent-item', itemCardClass]"
          >
            <div class="agent-content">
              <div class="agent-info">
                <h4 class="agent-nombre">{{ agente.nombre }}</h4>
                <p class="agent-email">{{ agente.email }}</p>
                <p class="agent-telefono">{{ agente.telefono }}</p>
              </div>
              <div class="agent-stats">
                <div :class="['agent-badge', badgeClass]">
                  {{ agente.clientesAsignados }} clientes
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Lista de Clientes Registrados -->
    <div :class="['clients-card', cardClass]">
      <div class="card-header">
        <Link :class="['card-icon', iconTertiaryClass]" />
        <h3 class="card-title">Clientes Registrados y Vinculaciones</h3>
      </div>

      <div class="clients-list">
        <div
          v-for="cliente in clientes"
          :key="cliente.id"
          :class="['client-item', itemCardClass]"
        >
          <div class="client-content">
            <div class="client-info">
              <div class="client-header">
                <h4 class="client-nombre">{{ cliente.nombre }}</h4>
                <CheckCircle :class="['success-icon', successIconClass]" />
              </div>
              <p class="client-detail">ID: {{ cliente.id }}</p>
              <p class="client-detail">Email: {{ cliente.email }}</p>
              <p class="client-detail">Teléfono: {{ cliente.telefono }}</p>
              <p class="client-detail">Registrado: {{ cliente.fechaRegistro }}</p>
            </div>
            <div class="client-actions">
              <div :class="['agent-badge', buttonSecondaryClass]">
                Agente: {{ obtenerNombreAgente(cliente.agenteAsignado) }}
              </div>
              <div :class="['success-badge', successBadgeClass]">
                Vinculado automáticamente
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Users, Link, CheckCircle } from 'lucide-vue-next'
import apiService from '../services/apiService.js'

const props = defineProps({
  isDark: Boolean
})

const clientes = ref([])
const agentes = ref([])
const loading = ref(false)
const error = ref(null)

// Estilos computados
const cardClass = computed(() => {
  return props.isDark ? 'dark-card' : 'light-card'
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

const successIconClass = computed(() => {
  return props.isDark ? 'dark-success-icon' : 'light-success-icon'
})

const itemCardClass = computed(() => {
  return props.isDark ? 'dark-item-card' : 'light-item-card'
})

const badgeClass = computed(() => {
  return props.isDark ? 'dark-badge' : 'light-badge'
})

const successBadgeClass = computed(() => {
  return props.isDark ? 'dark-success-badge' : 'light-success-badge'
})

// Métodos
const cargarDatos = async () => {
  loading.value = true
  error.value = null
  
  try {
    // Cargar agentes y clientes en paralelo
    const [agentesData, clientesData] = await Promise.all([
      apiService.getAgentes(),
      apiService.getClientes()
    ])
    
    agentes.value = agentesData.map(agente => ({
      id: agente.idAgente,
      nombre: agente.nombre,
      email: agente.email,
      telefono: agente.telefono,
      codigo: agente.codigo,
      clientesAsignados: agente.cantidadPolizas || 0
    }))
    
    clientes.value = clientesData.map(cliente => ({
      id: cliente.idCliente,
      nombre: cliente.nombre,
      email: cliente.email,
      telefono: cliente.telefono,
      fechaRegistro: new Date().toISOString().split('T')[0], // Valor por defecto
      agenteAsignado: null // Por ahora no hay vinculación directa
    }))
    
  } catch (err) {
    error.value = 'Error al cargar datos: ' + err.message
    console.error('Error cargando datos:', err)
  } finally {
    loading.value = false
  }
}

const asignarAgenteAutomatico = () => {
  if (agentes.value.length === 0) return null
  return agentes.value.reduce((prev, current) => 
    prev.clientesAsignados < current.clientesAsignados ? prev : current
  ).id
}

const obtenerNombreAgente = (agenteId) => {
  if (!agenteId) return 'No asignado'
  const agente = agentes.value.find(a => a.id === agenteId)
  return agente ? agente.nombre : 'No asignado'
}

onMounted(() => {
  cargarDatos()
})
</script>

<style scoped>
/* Contenedor principal */
.clientes-container {
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
.agents-card,
.clients-card {
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



/* Lista de agentes */
.agents-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.agent-item {
  padding: 1rem;
  border-radius: 0.75rem;
  border: 1px solid;
  transition: all 0.2s ease;
}

.agent-item:hover {
  transform: scale(1.01);
}

.agent-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.agent-info {
  flex: 1;
}

.agent-nombre {
  font-weight: 600;
  margin: 0 0 0.25rem 0;
}

.agent-email,
.agent-telefono {
  font-size: 0.875rem;
  opacity: 0.7;
  margin: 0.125rem 0;
}

.agent-stats {
  text-align: right;
}

.agent-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  font-size: 0.75rem;
  font-weight: 500;
}

/* Lista de clientes */
.clients-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.client-item {
  padding: 1rem;
  border-radius: 0.75rem;
  border: 1px solid;
  transition: all 0.2s ease;
}

.client-item:hover {
  transform: scale(1.01);
}

.client-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.client-info {
  flex: 1;
}

.client-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 0.5rem;
}

.client-nombre {
  font-weight: 600;
  margin: 0;
}

.success-icon {
  width: 1rem;
  height: 1rem;
}

.client-detail {
  font-size: 0.875rem;
  opacity: 0.7;
  margin: 0.125rem 0;
}

.client-actions {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  align-items: flex-end;
}

.success-badge {
  padding: 0.125rem 0.5rem;
  border-radius: 9999px;
  font-size: 0.75rem;
}

/* Tema claro */
.light-card {
  background: rgba(254, 247, 237, 0.6);
  border: 1px solid rgba(253, 186, 116, 0.5);
  box-shadow: 0 8px 32px rgba(251, 146, 60, 0.1);
}



.light-button-secondary {
  background: rgba(34, 197, 94, 0.8);
  color: #f0fdf4;
  box-shadow: 0 4px 15px rgba(34, 197, 94, 0.25);
}

.light-button-secondary:hover {
  background: rgba(34, 197, 94, 0.9);
  box-shadow: 0 6px 20px rgba(34, 197, 94, 0.35);
}

.light-icon-container {
  background: rgba(251, 146, 60, 0.2);
  color: #ea580c;
}



.light-icon-secondary {
  color: #16a34a;
}

.light-icon-tertiary {
  color: #9333ea;
}

.light-success-icon {
  color: #16a34a;
}

.light-item-card {
  background: rgba(254, 247, 237, 0.3);
  border-color: rgba(253, 186, 116, 0.3);
}

.light-badge {
  background: rgba(251, 146, 60, 0.2);
  color: #ea580c;
}

.light-success-badge {
  background: rgba(34, 197, 94, 0.2);
  color: #166534;
}

/* Tema oscuro */
.dark-card {
  background: rgba(30, 41, 59, 0.6);
  border: 1px solid rgba(71, 85, 105, 0.5);
  box-shadow: 0 8px 32px rgba(59, 130, 246, 0.1);
}



.dark-button-secondary {
  background: rgba(34, 197, 94, 0.8);
  color: #f0fdf4;
  box-shadow: 0 4px 15px rgba(34, 197, 94, 0.25);
}

.dark-button-secondary:hover {
  background: rgba(34, 197, 94, 0.9);
  box-shadow: 0 6px 20px rgba(34, 197, 94, 0.35);
}

.dark-icon-container {
  background: rgba(59, 130, 246, 0.2);
  color: #93c5fd;
}



.dark-icon-secondary {
  color: #4ade80;
}

.dark-icon-tertiary {
  color: #a78bfa;
}

.dark-success-icon {
  color: #4ade80;
}

.dark-item-card {
  background: rgba(51, 65, 85, 0.3);
  border-color: rgba(71, 85, 105, 0.3);
}

.dark-badge {
  background: rgba(59, 130, 246, 0.2);
  color: #93c5fd;
}

.dark-success-badge {
  background: rgba(34, 197, 94, 0.2);
  color: #4ade80;
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
  .clientes-container {
    padding: 0.5rem;
  }
  
  .main-grid {
    grid-template-columns: 1fr;
  }
  
  .client-content,
  .agent-content {
    flex-direction: column;
    gap: 1rem;
  }
  
  .client-actions {
    align-items: flex-start;
  }
}
</style>
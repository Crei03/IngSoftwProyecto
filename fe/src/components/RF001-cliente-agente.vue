<template>
  <div class="clientes-container">
    <!-- Header RF-001 -->
    <div :class="['header-card', cardClass]">
      <div class="header-content">
        <div :class="['icon-container', iconContainerClass]">
          <Users class="header-icon" />
        </div>
        <div class="header-text">
          <h2 class="header-title">Gestión de Clientes y Agentes</h2>
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
          <button 
            @click="mostrarModalAgente" 
            :class="['add-button', addButtonClass]"
            title="Crear nuevo agente"
          >
            <Plus class="add-icon" />
            Crear Agente
          </button>
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

      <!-- Lista de Evaluadores -->
      <div :class="['evaluators-card', cardClass]">
        <div class="card-header">
          <Shield :class="['card-icon', iconTertiaryClass]" />
          <h3 class="card-title">Evaluadores Disponibles</h3>
          <button 
            @click="mostrarModalEvaluador" 
            :class="['add-button', addButtonClass]"
            title="Crear nuevo evaluador"
          >
            <Plus class="add-icon" />
            Crear Evaluador
          </button>
        </div>

        <div class="evaluators-list">
          <div
            v-for="evaluador in evaluadores"
            :key="evaluador.id"
            :class="['evaluator-item', itemCardClass]"
          >
            <div class="evaluator-content">
              <div class="evaluator-info">
                <h4 class="evaluator-nombre">{{ evaluador.nombre }}</h4>
                <p class="evaluator-email">{{ evaluador.email }}</p>
                <p class="evaluator-telefono">{{ evaluador.telefono }}</p>
                <p class="evaluator-especialidad">{{ evaluador.especialidad || 'Sin especialidad' }}</p>
              </div>
              <div class="evaluator-stats">
                <div :class="['evaluator-badge', evaluador.activo ? 'active-badge' : 'inactive-badge', badgeClass]">
                  {{ evaluador.activo ? 'ACTIVO' : 'INACTIVO' }}
                </div>
                <div :class="['code-badge', badgeClass]">
                  {{ evaluador.codigo || 'Sin código' }}
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

    <!-- Modales -->
    <AgenteRegistroModal 
      :show-modal="showAgenteModal"
      :is-dark="isDark"
      @close="cerrarModalAgente"
      @agente-creado="onAgenteCreado"
    />

    <EvaluadorRegistroModal 
      :show-modal="showEvaluadorModal"
      :is-dark="isDark"
      @close="cerrarModalEvaluador"
      @evaluador-creado="onEvaluadorCreado"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Users, Link, CheckCircle, Shield, Plus } from 'lucide-vue-next'
import apiService from '../services/apiService.js'
import AgenteRegistroModal from './AgenteRegistroModal.vue'
import EvaluadorRegistroModal from './EvaluadorRegistroModal.vue'

const props = defineProps({
  isDark: {
    type: Boolean,
    default: true
  }
})

const clientes = ref([])
const agentes = ref([])
const evaluadores = ref([])
const loading = ref(false)
const error = ref(null)

// Estados para modales
const showAgenteModal = ref(false)
const showEvaluadorModal = ref(false)

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

const buttonPrimaryClass = computed(() => {
  return props.isDark ? 'dark-button-primary' : 'light-button-primary'
})

const addButtonClass = computed(() => {
  return props.isDark ? 'dark-add-button' : 'light-add-button'
})

// Métodos
const cargarDatos = async () => {
  loading.value = true
  error.value = null
  
  try {
    // Cargar agentes, clientes y evaluadores en paralelo
    const [agentesData, clientesData, evaluadoresData] = await Promise.all([
      apiService.getAgentes(),
      apiService.getClientes(),
      apiService.getEvaluadores()
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

    evaluadores.value = evaluadoresData.map(evaluador => ({
      id: evaluador.idEvaluador,
      nombre: evaluador.nombre,
      email: evaluador.email,
      telefono: evaluador.telefono,
      codigo: evaluador.codigo,
      especialidad: evaluador.especialidad,
      activo: evaluador.activo
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

// Métodos para modales
const mostrarModalAgente = () => {
  showAgenteModal.value = true
}

const cerrarModalAgente = () => {
  showAgenteModal.value = false
}

const onAgenteCreado = (nuevoAgente) => {
  console.log('Agente creado:', nuevoAgente)
  // Recargar datos para mostrar el nuevo agente
  cargarDatos()
}

const mostrarModalEvaluador = () => {
  showEvaluadorModal.value = true
}

const cerrarModalEvaluador = () => {
  showEvaluadorModal.value = false
}

const onEvaluadorCreado = (nuevoEvaluador) => {
  console.log('Evaluador creado:', nuevoEvaluador)
  // Recargar datos para mostrar el nuevo evaluador
  cargarDatos()
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
.evaluators-card,
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
  justify-content: space-between;
}

.card-icon {
  width: 1.25rem;
  height: 1.25rem;
}

.card-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0;
  flex: 1;
}

/* Botón para agregar */
.add-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 0.75rem;
  font-size: 0.875rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  white-space: nowrap;
}

.add-button:hover {
  transform: translateY(-1px);
}

.add-icon {
  width: 1rem;
  height: 1rem;
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

/* Lista de evaluadores */
.evaluators-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.evaluator-item {
  padding: 1rem;
  border-radius: 0.75rem;
  border: 1px solid;
  transition: all 0.2s ease;
}

.evaluator-item:hover {
  transform: scale(1.01);
}

.evaluator-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.evaluator-info {
  flex: 1;
}

.evaluator-nombre {
  font-weight: 600;
  margin: 0 0 0.25rem 0;
}

.evaluator-email,
.evaluator-telefono,
.evaluator-especialidad {
  font-size: 0.875rem;
  opacity: 0.7;
  margin: 0.125rem 0;
}

.evaluator-stats {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  text-align: right;
}

.evaluator-badge,
.code-badge {
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

.light-add-button {
  background: linear-gradient(135deg, #16a34a 0%, #15803d 100%);
  color: #f0fdf4;
  box-shadow: 0 4px 15px rgba(22, 163, 74, 0.3);
}

.light-add-button:hover {
  background: linear-gradient(135deg, #15803d 0%, #166534 100%);
  box-shadow: 0 6px 20px rgba(22, 163, 74, 0.4);
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

.dark-add-button {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: #ecfdf5;
  box-shadow: 0 4px 15px rgba(16, 185, 129, 0.3);
}

.dark-add-button:hover {
  background: linear-gradient(135deg, #059669 0%, #047857 100%);
  box-shadow: 0 6px 20px rgba(16, 185, 129, 0.4);
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
  .agent-content,
  .evaluator-content {
    flex-direction: column;
    gap: 1rem;
  }
  
  .client-actions {
    align-items: flex-start;
  }
  
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
  
  .add-button {
    align-self: stretch;
    justify-content: center;
  }
}
</style>

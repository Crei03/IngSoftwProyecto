<template>
  <div class="dashboard-wrapper">
    <div class="dashboard-header">
      <div>
        <p class="welcome-text">Bienvenido de nuevo</p>
        <h2 class="dashboard-title">Panel Estratégico del Agente</h2>
        <p class="dashboard-subtitle">
          Monitorea clientes, pólizas y reclamaciones en tiempo real.
        </p>
      </div>
      <div class="refresh-block">
        <button class="refresh-button" @click="cargarDashboard" :disabled="loading">
          <RefreshCcw class="refresh-icon" />
          {{ loading ? 'Actualizando...' : 'Actualizar' }}
        </button>
        <span class="last-update">Última actualización: {{ ultimaActualizacion }}</span>
      </div>
    </div>

    <div class="metrics-grid">
      <div class="metric-card purple">
        <div class="metric-icon">
          <Users />
        </div>
        <div>
          <p class="metric-label">Clientes Activos</p>
          <p class="metric-value">{{ stats.clientes }}</p>
          <p class="metric-trend">+{{ stats.clientesNuevosSemana }} nuevos esta semana</p>
        </div>
      </div>
      <div class="metric-card blue">
        <div class="metric-icon">
          <Shield />
        </div>
        <div>
          <p class="metric-label">Pólizas Vigentes</p>
          <p class="metric-value">{{ stats.polizasActivas }}</p>
          <p class="metric-trend">
            {{ stats.porcentajeActivas.toFixed(0) }}% del portafolio
          </p>
        </div>
      </div>
      <div class="metric-card amber">
        <div class="metric-icon">
          <Clock />
        </div>
        <div>
          <p class="metric-label">Pólizas Pendientes</p>
          <p class="metric-value">{{ stats.polizasPendientes }}</p>
          <p class="metric-trend">Revisa antes de 48h</p>
        </div>
      </div>
      <div class="metric-card rose">
        <div class="metric-icon">
          <AlertTriangle />
        </div>
        <div>
          <p class="metric-label">Reclamaciones Abiertas</p>
          <p class="metric-value">{{ stats.reclamacionesAbiertas }}</p>
          <p class="metric-trend">{{ stats.reclamacionesCriticas }} requieren atención</p>
        </div>
      </div>
    </div>

    <div class="content-grid">
      <div class="card large-card">
        <div class="card-header">
          <div>
            <h3>Flujo de pólizas</h3>
            <p>Resumen de los últimos registros</p>
          </div>
          <div class="chip success">Seguimiento diario</div>
        </div>
        <div class="progress-list">
          <div class="progress-row" v-for="item in polizaResumen" :key="item.label">
            <div class="progress-info">
              <p class="progress-label">{{ item.label }}</p>
              <span>{{ item.valor }} pólizas</span>
            </div>
            <div class="progress-bar">
              <div
                class="progress-fill"
                :style="{ width: item.porcentaje + '%', background: item.color }"
              ></div>
            </div>
          </div>
        </div>
      </div>

      <div class="card medium-card">
        <div class="card-header">
          <div>
            <h3>Alertas Operativas</h3>
            <p>Prioriza estas tareas</p>
          </div>
          <div class="chip warning">{{ tareasCriticas.length }} críticas</div>
        </div>
        <ul class="task-list">
          <li v-for="tarea in tareasCriticas" :key="tarea.numeroPoliza">
            <div>
              <p class="task-title">{{ tarea.numeroPoliza }}</p>
              <p class="task-subtitle">
                {{ tarea.tipoSeguro }} · {{ tarea.estado }}
              </p>
            </div>
            <span class="task-deadline">+{{ tarea.diasCreacion }}d</span>
          </li>
          <li v-if="!tareasCriticas.length" class="empty-state">
            <CheckCircle2 />
            <span>No hay tareas urgentes</span>
          </li>
        </ul>
      </div>
    </div>

    <div class="card table-card">
      <div class="card-header">
        <div>
          <h3>Últimas pólizas registradas</h3>
          <p>Pólizas más recientes creadas por tu equipo</p>
        </div>
        <div class="chip neutral">Top {{ ultimasPolizas.length }}</div>
      </div>
      <div class="table-wrapper">
        <table>
          <thead>
            <tr>
              <th>Póliza</th>
              <th>Cliente</th>
              <th>Tipo</th>
              <th>Estado</th>
              <th>Emisión</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="poliza in ultimasPolizas" :key="poliza.id">
              <td>{{ poliza.numeroPoliza }}</td>
              <td>{{ poliza.clienteNombre || poliza.clienteId }}</td>
              <td>{{ poliza.tipoSeguro }}</td>
              <td>
                <span class="status" :class="poliza.estado.toLowerCase()">
                  {{ poliza.estado }}
                </span>
              </td>
              <td>{{ poliza.fechaEmisionReadable }}</td>
            </tr>
            <tr v-if="!ultimasPolizas.length">
              <td colspan="5" class="empty-state-row">No hay registros recientes</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div v-if="error" class="error-banner">
      <AlertCircle class="error-icon" />
      <div>
        <p>{{ error }}</p>
        <button @click="cargarDashboard">Reintentar</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import {
  Users,
  Shield,
  AlertTriangle,
  Clock,
  CheckCircle2,
  AlertCircle,
  RefreshCcw
} from 'lucide-vue-next'
import apiService from '../services/apiService.js'

const props = defineProps({
  isDark: {
    type: Boolean,
    default: true
  }
})

const loading = ref(false)
const error = ref(null)
const ultimaActualizacion = ref('Sin datos')

const stats = ref({
  clientes: 0,
  clientesNuevosSemana: 0,
  polizasActivas: 0,
  polizasPendientes: 0,
  porcentajeActivas: 0,
  reclamacionesAbiertas: 0,
  reclamacionesCriticas: 0,
  totalPolizas: 0
})

const ultimasPolizas = ref([])
const polizaResumen = computed(() => [
  {
    label: 'Aprobadas',
    valor: stats.value.polizasActivas,
    porcentaje: stats.value.porcentajeActivas,
    color: '#22c55e'
  },
  {
    label: 'Pendientes',
    valor: stats.value.polizasPendientes,
    porcentaje: stats.value.totalPolizas
      ? Math.round((stats.value.polizasPendientes / stats.value.totalPolizas) * 100)
      : 0,
    color: '#fbbf24'
  },
  {
    label: 'Reclamaciones activas',
    valor: stats.value.reclamacionesAbiertas,
    porcentaje: stats.value.totalPolizas
      ? Math.min(Math.round((stats.value.reclamacionesAbiertas / stats.value.totalPolizas) * 100), 100)
      : 0,
    color: '#f87171'
  }
])

const tareasCriticas = computed(() =>
  ultimasPolizas.value
    .filter(poliza => poliza.estado === 'PENDIENTE')
    .slice(0, 4)
)

const cargarDashboard = async () => {
  loading.value = true
  error.value = null

  try {
    const [clientes, polizas, reclamaciones] = await Promise.all([
      apiService.getClientes(),
      apiService.getPolizas(),
      apiService.getReclamaciones()
    ])

    const polizasPendientes = polizas.filter(p => p.estado === 'PENDIENTE')
    const polizasActivas = polizas.filter(p => p.estado === 'APROBADA')
    const reclamacionesAbiertas = reclamaciones.filter(r => r.estado !== 'CERRADA' && r.estado !== 'PAGADA')

    stats.value = {
      clientes: clientes.length,
      clientesNuevosSemana: Math.min(5, clientes.length),
      polizasActivas: polizasActivas.length,
      polizasPendientes: polizasPendientes.length,
      porcentajeActivas: polizas.length ? Math.round((polizasActivas.length / polizas.length) * 100) : 0,
      reclamacionesAbiertas: reclamacionesAbiertas.length,
      reclamacionesCriticas: reclamacionesAbiertas.filter(r => r.prioridad === 'ALTA' || r.estado === 'RECLAMADA').length,
      totalPolizas: polizas.length
    }

    ultimasPolizas.value = polizas
      .map(poliza => ({
        id: poliza.idPoliza,
        numeroPoliza: poliza.numeroPoliza,
        clienteId: poliza.clienteId,
        clienteNombre: poliza.cliente?.nombre,
        tipoSeguro: poliza.tipoSeguro,
        estado: poliza.estado,
        fechaEmisionReadable: poliza.fechaEmision
          ? new Date(poliza.fechaEmision).toLocaleDateString('es-MX')
          : 'Sin fecha',
        diasCreacion: poliza.fechaEmision
          ? Math.max(
              0,
              Math.round(
                (Date.now() - new Date(poliza.fechaEmision).getTime()) / (1000 * 60 * 60 * 24)
              )
            )
          : 0
      }))
      .sort((a, b) => (b.fechaEmision || '').localeCompare(a.fechaEmision || ''))
      .slice(0, 5)

    ultimaActualizacion.value = new Date().toLocaleTimeString('es-MX', {
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch (err) {
    console.error('Error cargando dashboard:', err)
    error.value = 'No se pudo cargar el dashboard. Intenta nuevamente.'
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  cargarDashboard()
})
</script>

<style scoped>
.dashboard-wrapper {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 1rem;
  padding: 1.5rem;
  border-radius: 1rem;
  background: rgba(15, 23, 42, 0.7);
  border: 1px solid rgba(148, 163, 184, 0.2);
}

.welcome-text {
  margin: 0;
  font-size: 0.95rem;
  color: #a1a1aa;
}

.dashboard-title {
  margin: 0;
  font-size: 1.75rem;
}

.dashboard-subtitle {
  margin: 0.25rem 0 0;
  color: #94a3b8;
}

.refresh-block {
  text-align: right;
}

.refresh-button {
  display: inline-flex;
  align-items: center;
  gap: 0.35rem;
  padding: 0.75rem 1.25rem;
  border-radius: 999px;
  border: none;
  background: linear-gradient(135deg, #3b82f6, #2563eb);
  color: white;
  cursor: pointer;
}

.refresh-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.refresh-icon {
  width: 1rem;
  height: 1rem;
}

.last-update {
  display: block;
  margin-top: 0.35rem;
  font-size: 0.85rem;
  color: #94a3b8;
}

.metrics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 1rem;
}

.metric-card {
  border-radius: 1rem;
  padding: 1rem;
  display: flex;
  gap: 1rem;
  align-items: center;
  color: white;
}

.metric-card.purple {
  background: linear-gradient(135deg, #7c3aed, #6d28d9);
}
.metric-card.blue {
  background: linear-gradient(135deg, #0ea5e9, #2563eb);
}
.metric-card.amber {
  background: linear-gradient(135deg, #fbbf24, #f97316);
}
.metric-card.rose {
  background: linear-gradient(135deg, #f43f5e, #be123c);
}

.metric-icon {
  background: rgba(255, 255, 255, 0.2);
  padding: 0.75rem;
  border-radius: 0.75rem;
  display: inline-flex;
}

.metric-label {
  margin: 0;
  font-size: 0.9rem;
  opacity: 0.85;
}

.metric-value {
  margin: 0.1rem 0;
  font-size: 1.8rem;
  font-weight: 700;
}

.metric-trend {
  margin: 0;
  font-size: 0.85rem;
  opacity: 0.9;
}

.content-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 1.5rem;
  flex-wrap: wrap;
}

@media (max-width: 1024px) {
  .content-grid {
    grid-template-columns: 1fr;
  }

  .dashboard-header {
    flex-direction: column;
  }

  .refresh-block {
    text-align: left;
    width: 100%;
  }
}

.card {
  border-radius: 1rem;
  padding: 1.5rem;
  background: rgba(15, 23, 42, 0.6);
  border: 1px solid rgba(148, 163, 184, 0.2);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.card-header h3 {
  margin: 0;
}

.card-header p {
  margin: 0.25rem 0 0;
  color: #94a3b8;
}

.chip {
  padding: 0.35rem 0.85rem;
  border-radius: 999px;
  font-size: 0.85rem;
}

.chip.success {
  background: rgba(34, 197, 94, 0.2);
  color: #22c55e;
}

.chip.warning {
  background: rgba(251, 191, 36, 0.2);
  color: #fbbf24;
}

.chip.neutral {
  background: rgba(148, 163, 184, 0.2);
  color: #cbd5f5;
}

.progress-list {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

.progress-row {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  font-size: 0.95rem;
}

.progress-bar {
  background: rgba(255, 255, 255, 0.08);
  border-radius: 999px;
  height: 0.5rem;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  border-radius: 999px;
}

.task-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.task-list li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 0;
  border-bottom: 1px solid rgba(148, 163, 184, 0.2);
}

.task-title {
  margin: 0;
  font-weight: 600;
}

.task-subtitle {
  margin: 0;
  color: #94a3b8;
  font-size: 0.9rem;
}

.task-deadline {
  font-weight: 600;
  color: #fbbf24;
}

.empty-state {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #94a3b8;
}

.table-card table {
  width: 100%;
  border-collapse: collapse;
}

.table-card th,
.table-card td {
  padding: 0.85rem;
  text-align: left;
  border-bottom: 1px solid rgba(148, 163, 184, 0.2);
}

.table-card thead {
  font-size: 0.9rem;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: #94a3b8;
}

.status {
  padding: 0.25rem 0.75rem;
  border-radius: 999px;
  font-size: 0.85rem;
  text-transform: capitalize;
}

.status.aprobada {
  background: rgba(34, 197, 94, 0.15);
  color: #22c55e;
}

.status.pendiente {
  background: rgba(251, 191, 36, 0.15);
  color: #fbbf24;
}

.status.rechazada,
.status.cancelada {
  background: rgba(244, 63, 94, 0.15);
  color: #fb7185;
}

.error-banner {
  display: flex;
  gap: 0.75rem;
  align-items: center;
  padding: 1rem;
  border-radius: 0.75rem;
  border: 1px solid rgba(248, 113, 113, 0.4);
  background: rgba(248, 113, 113, 0.15);
}

.error-banner button {
  margin-top: 0.25rem;
  padding: 0.4rem 0.75rem;
  border-radius: 0.5rem;
  border: none;
  background: #ef4444;
  color: white;
  cursor: pointer;
}

.error-icon {
  color: #f87171;
}

.table-wrapper {
  overflow-x: auto;
}

.empty-state-row {
  text-align: center;
  color: #94a3b8;
}
</style>

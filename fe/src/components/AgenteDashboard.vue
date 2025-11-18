<template>
  <div class="agente-dashboard">
    <div class="dashboard-header">
      <h1>Dashboard de Agentes</h1>
      <p class="subtitle">Gestión completa de agentes y su actividad</p>
    </div>

    <div class="dashboard-content">
      <div class="stats-container">
        <div class="stat-card">
          <div class="stat-icon">👥</div>
          <div class="stat-info">
            <h3>Agentes Activos</h3>
            <p class="stat-value">{{ agentesActivos }}</p>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon">📋</div>
          <div class="stat-info">
            <h3>Pólizas Registradas</h3>
            <p class="stat-value">{{ polizasRegistradas }}</p>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon">✅</div>
          <div class="stat-info">
            <h3>Pólizas Activas</h3>
            <p class="stat-value">{{ polizasActivas }}</p>
          </div>
        </div>

        <div class="stat-card">
          <div class="stat-icon">⏳</div>
          <div class="stat-info">
            <h3>Pólizas Pendientes</h3>
            <p class="stat-value">{{ polizasPendientes }}</p>
          </div>
        </div>
      </div>

      <div class="table-container">
        <h2>Listado de Agentes</h2>
        <table class="agents-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nombre</th>
              <th>Email</th>
              <th>Teléfono</th>
              <th>Estado</th>
              <th>Pólizas Activas</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="agente in agentes" :key="agente.idAgente">
              <td>{{ agente.idAgente }}</td>
              <td>{{ agente.nombre }}</td>
              <td>{{ agente.correo }}</td>
              <td>{{ agente.telefono }}</td>
              <td>
                <span :class="['status', agente.activo ? 'active' : 'inactive']">
                  {{ agente.activo ? 'Activo' : 'Inactivo' }}
                </span>
              </td>
              <td>{{ agente.polizasActivas || 0 }}</td>
              <td>
                <button class="btn-small" @click="verDetalles(agente)">Ver</button>
              </td>
            </tr>
          </tbody>
        </table>
        <p v-if="agentes.length === 0" class="no-data">No hay agentes disponibles</p>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import apiService from '../services/apiService'

export default {
  name: 'AgenteDashboard',
  setup() {
    const agentes = ref([])
    const agentesActivos = ref(0)
    const polizasRegistradas = ref(0)
    const polizasActivas = ref(0)
    const polizasPendientes = ref(0)

    const cargarDatos = async () => {
      try {
        // Cargar agentes
        const agentesRes = await apiService.getAgentes()
        agentes.value = agentesRes.data || []
        agentesActivos.value = agentes.value.filter(a => a.activo).length

        // Cargar pólizas
        const polizasRes = await apiService.getPolizas()
        const polizas = polizasRes.data || []
        polizasRegistradas.value = polizas.length
        polizasActivas.value = polizas.filter(p => p.estado === 'ACTIVA').length
        polizasPendientes.value = polizas.filter(p => p.estado === 'PENDIENTE').length
      } catch (error) {
        console.error('Error al cargar datos:', error)
      }
    }

    const verDetalles = (agente) => {
      console.log('Ver detalles del agente:', agente)
      // Implementar navegación a detalles
    }

    onMounted(() => {
      cargarDatos()
    })

    return {
      agentes,
      agentesActivos,
      polizasRegistradas,
      polizasActivas,
      polizasPendientes,
      verDetalles
    }
  }
}
</script>

<style scoped>
.agente-dashboard {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.dashboard-header {
  margin-bottom: 30px;
}

.dashboard-header h1 {
  font-size: 28px;
  color: #333;
  margin-bottom: 5px;
}

.subtitle {
  color: #666;
  font-size: 14px;
}

.stats-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  font-size: 32px;
}

.stat-info h3 {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin: 5px 0 0 0;
}

.table-container {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.table-container h2 {
  margin-top: 0;
  color: #333;
  font-size: 18px;
}

.agents-table {
  width: 100%;
  border-collapse: collapse;
}

.agents-table thead {
  background-color: #f9f9f9;
}

.agents-table th {
  padding: 12px;
  text-align: left;
  font-weight: 600;
  color: #333;
  border-bottom: 2px solid #ddd;
}

.agents-table td {
  padding: 12px;
  border-bottom: 1px solid #eee;
}

.agents-table tbody tr:hover {
  background-color: #f9f9f9;
}

.status {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
}

.status.active {
  background-color: #d4edda;
  color: #155724;
}

.status.inactive {
  background-color: #f8d7da;
  color: #721c24;
}

.btn-small {
  padding: 6px 12px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
}

.btn-small:hover {
  background-color: #0056b3;
}

.no-data {
  text-align: center;
  color: #666;
  padding: 20px;
}
</style>

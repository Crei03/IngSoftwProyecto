<template>
  <div class="app-container theme-dark">
    <!-- Si no está logueado, mostrar página de login -->
    <LoginPage 
      v-if="!isLoggedIn" 
      @loginSuccess="handleLoginSuccess"
    />
    
    <!-- Si está logueado, mostrar la aplicación principal -->
    <div v-else class="main-app">
      <!-- Header -->
      <header class="header">
        <div class="header-content">
          <div class="header-left">
            <button
              @click="toggleSidebar"
              class="sidebar-toggle"
              :title="sidebarCollapsed ? 'Expandir menú' : 'Contraer menú'"
            >
              <Menu class="menu-icon" />
            </button>
            <div class="logo-section">
              <h1 class="logo-title">SeguraTuAuto</h1>
              <p class="logo-subtitle">{{ getUserRoleText() }}</p>
            </div>
          </div>
          <div class="header-right">
            <div class="user-profile">
              <div class="user-info">
                <span class="user-name">{{ currentUser.nombre }}</span>
                <span class="user-role">{{ currentUser.role === 'agente' ? 'Agente' : 'Cliente' }}</span>
              </div>
              <button @click="logout" class="logout-button">
                <LogOut class="logout-icon" />
                Cerrar Sesión
              </button>
            </div>
          </div>
        </div>
      </header>

      <!-- Sidebar Navigation -->
      <aside :class="['sidebar', { collapsed: sidebarCollapsed }]">
        <div class="sidebar-content">
          <div class="nav-buttons">
            <!-- Navegación para Agentes (Administradores) -->
            <template v-if="currentUser.role === 'agente'">
              <button
                @click="currentView = 'dashboard'"
                :class="['nav-button', { active: currentView === 'dashboard' }]"
                :title="sidebarCollapsed ? 'Dashboard Administrativo' : ''"
              >
                <BarChart3 class="nav-icon" />
                <div class="nav-text">
                  <div class="nav-title">Dashboard</div>
                  <div class="nav-subtitle">Panel de Control</div>
                </div>
              </button>
              <button
                @click="currentView = 'RF001'"
                :class="['nav-button', { active: currentView === 'RF001' }]"
                :title="sidebarCollapsed ? 'RF-001 - Gestión de Clientes y Agentes' : ''"
              >
                <Users class="nav-icon" />
                <div class="nav-text">
                  <div class="nav-title">RF-001</div>
                  <div class="nav-subtitle">Gestión de Clientes y Agentes</div>
                </div>
              </button>
              <button
                @click="currentView = 'RF002'"
                :class="['nav-button', { active: currentView === 'RF002' }]"
                :title="sidebarCollapsed ? 'RF-002 - Gestión de Pólizas' : ''"
              >
                <FileText class="nav-icon" />
                <div class="nav-text">
                  <div class="nav-title">RF-002</div>
                  <div class="nav-subtitle">Gestión de Pólizas</div>
                </div>
              </button>
              <button
                @click="currentView = 'reclamaciones'"
                :class="['nav-button', { active: currentView === 'reclamaciones' }]"
                :title="sidebarCollapsed ? 'Gestión de Reclamaciones' : ''"
              >
                <AlertTriangle class="nav-icon" />
                <div class="nav-text">
                  <div class="nav-title">Reclamaciones</div>
                  <div class="nav-subtitle">Gestión Completa</div>
                </div>
              </button>
            </template>

            <!-- Navegación para Clientes -->
            <template v-else>
              <button
                @click="currentView = 'landing'"
                :class="['nav-button', { active: currentView === 'landing' }]"
                :title="sidebarCollapsed ? 'Inicio - Información General' : ''"
              >
                <Home class="nav-icon" />
                <div class="nav-text">
                  <div class="nav-title">Inicio</div>
                  <div class="nav-subtitle">Información General</div>
                </div>
              </button>
              <button
                @click="currentView = 'clientePoliza'"
                :class="['nav-button', { active: currentView === 'clientePoliza' }]"
                :title="sidebarCollapsed ? 'Registro de Póliza' : ''"
              >
                <Car class="nav-icon" />
                <div class="nav-text">
                  <div class="nav-title">Nueva Póliza</div>
                  <div class="nav-subtitle">Registrar Seguro</div>
                </div>
              </button>
              <button
                @click="currentView = 'misPolizas'"
                :class="['nav-button', { active: currentView === 'misPolizas' }]"
                :title="sidebarCollapsed ? 'Mis Pólizas' : ''"
              >
                <Shield class="nav-icon" />
                <div class="nav-text">
                  <div class="nav-title">Mis Pólizas</div>
                  <div class="nav-subtitle">Ver y Gestionar</div>
                </div>
              </button>
              <button
                @click="currentView = 'misReclamaciones'"
                :class="['nav-button', { active: currentView === 'misReclamaciones' }]"
                :title="sidebarCollapsed ? 'Mis Reclamaciones' : ''"
              >
                <AlertCircle class="nav-icon" />
                <div class="nav-text">
                  <div class="nav-title">Reclamaciones</div>
                  <div class="nav-subtitle">Mis Solicitudes</div>
                </div>
              </button>
            </template>
          </div>
        </div>
      </aside>

      <!-- Main Content -->
      <main :class="['main-content', { 'sidebar-collapsed': sidebarCollapsed }]">
        <div class="content-area">
          <div :key="currentView">
              <!-- Vistas para Agentes (Administradores) -->
              <template v-if="currentUser.role === 'agente'">
                <div v-if="currentView === 'dashboard'" class="dashboard">
                  <h2>Dashboard Administrativo</h2>
                  <p>Panel de control para agentes - Próximamente...</p>
                </div>
                <RF001ClienteAgente v-if="currentView === 'RF001'" />
                <RF002RegistroPolizas v-if="currentView === 'RF002'" />
                <GestionReclamaciones v-if="currentView === 'reclamaciones'" />
              </template>

              <!-- Vistas para Clientes -->
              <template v-else>
                <LandingPage 
                  v-if="currentView === 'landing'" 
                />
                <ClienteRegistroPoliza 
                  v-if="currentView === 'clientePoliza'" 
                  :cliente="currentUser"
                />
                <MisPolizas 
                  v-if="currentView === 'misPolizas'" 
                  :cliente="currentUser"
                />
                <FormularioReclamaciones
                  v-if="currentView === 'misReclamaciones'"
                  :cliente="currentUser"
                />
              </template>
            </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { 
  Menu, LogOut, Home, Users, FileText, Car, Shield, 
  AlertTriangle, AlertCircle, BarChart3 
} from 'lucide-vue-next'

// Importar componentes
import LoginPage from './components/LoginPage.vue'
import LandingPage from './components/LandingPage.vue'
import RF001ClienteAgente from './components/RF001-cliente-agente.vue'
import RF002RegistroPolizas from './components/RF002-registro-polizas.vue'
import ClienteRegistroPoliza from './components/ClienteRegistroPoliza.vue'
import MisPolizas from './components/MisPolizas.vue'
import GestionReclamaciones from './components/GestionReclamaciones.vue'
import FormularioReclamaciones from './components/FormularioReclamaciones.vue'

// Estado de la aplicación
const sidebarCollapsed = ref(false)
const isLoggedIn = ref(false)
const currentUser = ref({})
const currentView = ref('landing')

// Funciones
const toggleSidebar = () => {
  sidebarCollapsed.value = !sidebarCollapsed.value
}

const getUserRoleText = () => {
  if (currentUser.value.role === 'agente') {
    return 'Panel Administrativo'
  } else {
    return 'Portal del Cliente'
  }
}

const handleLoginSuccess = (userData) => {
  currentUser.value = userData
  isLoggedIn.value = true
  
  // Establecer vista inicial según rol
  if (userData.role === 'agente') {
    currentView.value = 'dashboard'
  } else {
    currentView.value = 'landing'
  }
}

const logout = () => {
  isLoggedIn.value = false
  currentUser.value = {}
  currentView.value = 'landing'
  localStorage.removeItem('currentUser')
}

// Verificar sesión al cargar
onMounted(() => {
  // Verificar sesión guardada
  const savedUser = localStorage.getItem('currentUser')
  if (savedUser) {
    try {
      const userData = JSON.parse(savedUser)
      handleLoginSuccess(userData)
    } catch (error) {
      console.error('Error al cargar sesión:', error)
      localStorage.removeItem('currentUser')
    }
  }
})
</script>

<style scoped>
/* Contenedor principal */
.app-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
  color: #e2e8f0;
}

/* Layout principal */
.main-app {
  display: grid;
  grid-template-areas: 
    "header header"
    "sidebar content";
  grid-template-rows: auto 1fr;
  grid-template-columns: auto 1fr;
  min-height: 100vh;
}

/* Header */
.header {
  grid-area: header;
  padding: 1rem 2rem;
  border-bottom: 1px solid rgba(71, 85, 105, 0.3);
  background: rgba(15, 23, 42, 0.9);
  backdrop-filter: blur(10px);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1400px;
  margin: 0 auto;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.sidebar-toggle {
  background: none;
  border: 1px solid rgba(71, 85, 105, 0.5);
  padding: 0.5rem;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #e2e8f0;
}

.sidebar-toggle:hover {
  transform: scale(1.05);
  background: rgba(59, 130, 246, 0.1);
  border-color: #3b82f6;
}

.menu-icon {
  width: 1.25rem;
  height: 1.25rem;
}

.logo-section {
  text-align: left;
}

.logo-title {
  font-size: 1.5rem;
  font-weight: bold;
  margin: 0;
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.logo-subtitle {
  font-size: 0.875rem;
  opacity: 0.8;
  margin: 0;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.user-name {
  font-weight: 600;
}

.user-role {
  font-size: 0.75rem;
  opacity: 0.8;
}

.logout-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  border: 1px solid rgba(239, 68, 68, 0.5);
  border-radius: 0.5rem;
  background: none;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #f87171;
}

.logout-button:hover {
  transform: translateY(-1px);
  background: rgba(239, 68, 68, 0.1);
  border-color: #f87171;
}

.logout-icon {
  width: 1rem;
  height: 1rem;
}

/* Sidebar */
.sidebar {
  grid-area: sidebar;
  width: 300px;
  transition: width 0.3s ease;
  border-right: 1px solid rgba(71, 85, 105, 0.3);
  background: rgba(15, 23, 42, 0.95);
  backdrop-filter: blur(10px);
}

.sidebar.collapsed {
  width: 80px;
}

.sidebar-content {
  padding: 1rem;
  height: 100%;
}

.nav-buttons {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.nav-button {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  border: 1px solid rgba(71, 85, 105, 0.3);
  border-radius: 0.75rem;
  background: none;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: left;
  color: #e2e8f0;
}

.nav-button:hover,
.nav-button.active {
  background: rgba(59, 130, 246, 0.1);
  border-color: #3b82f6;
  transform: translateX(4px);
}

.nav-icon {
  width: 1.5rem;
  height: 1.5rem;
  flex-shrink: 0;
}

.nav-text {
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.sidebar.collapsed .nav-text {
  display: none;
}

.nav-title {
  font-weight: 600;
  font-size: 0.875rem;
}

.nav-subtitle {
  font-size: 0.75rem;
  opacity: 0.8;
}

/* Main Content */
.main-content {
  grid-area: content;
  overflow-y: auto;
  height: calc(100vh - 80px);
}

.content-area {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
  background: rgba(30, 41, 59, 0.3);
  border-radius: 1rem;
}

/* Dashboard placeholder */
.dashboard {
  padding: 2rem;
  text-align: center;
  border-radius: 1rem;
  background: rgba(30, 41, 59, 0.6);
  border: 1px solid rgba(71, 85, 105, 0.5);
}

/* Responsive */
@media (max-width: 768px) {
  .main-app {
    grid-template-areas: 
      "header"
      "content";
    grid-template-columns: 1fr;
  }
  
  .sidebar {
    position: fixed;
    top: 80px;
    left: 0;
    height: calc(100vh - 80px);
    z-index: 200;
    transform: translateX(-100%);
  }
  
  .sidebar:not(.collapsed) {
    transform: translateX(0);
  }
  
  .header-content {
    flex-direction: column;
    gap: 1rem;
  }
  
  .user-info {
    text-align: center;
  }
}

@media (max-width: 480px) {
  .content-area {
    padding: 1rem;
  }
  
  .header {
    padding: 1rem;
  }
}
</style>
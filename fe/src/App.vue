<template>
  <div :class="['app-container', themeClass]">
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
            <p class="logo-subtitle">Tu seguridad en la carretera</p>
          </div>
        </div>
        <div class="header-right">
          <button
            @click="showLoginModal = true"
            class="login-button"
            v-if="!isLoggedIn"
          >
            <LogIn class="login-icon" />
            Iniciar Sesión
          </button>
          <div v-else class="user-profile">
            <button @click="showProfileModal = true" class="profile-button">
              <User class="profile-icon" />
              {{ currentUser.nombre }}
            </button>
            <button @click="logout" class="logout-button">
              Cerrar Sesión
            </button>
          </div>
          <button
            @click="toggleTheme"
            class="theme-switch"
            :title="isDark ? 'Cambiar a tema claro' : 'Cambiar a tema oscuro'"
          >
            <component :is="isDark ? 'Sun' : 'Moon'" class="theme-icon" />
          </button>
        </div>
      </div>
    </header>

    <!-- Sidebar Navigation -->
    <aside :class="['sidebar', { collapsed: sidebarCollapsed }]">
      <div class="sidebar-content">
        <div class="nav-buttons">
          <button
            @click="currentRF = 'landing'"
            :class="['nav-button', { active: currentRF === 'landing' }]"
            :title="sidebarCollapsed ? 'Inicio - Landing Page' : ''"
          >
            <Home class="nav-icon" />
            <div class="nav-text">
              <div class="nav-title">Inicio</div>
              <div class="nav-subtitle">Landing Page</div>
            </div>
          </button>
          <button
            @click="currentRF = 'RF001'"
            :class="['nav-button', { active: currentRF === 'RF001' }]"
            :title="sidebarCollapsed ? 'RF-001 - Gestión de Clientes y Agentes' : ''"
          >
            <Users class="nav-icon" />
            <div class="nav-text">
              <div class="nav-title">RF-001</div>
              <div class="nav-subtitle">Gestión de Clientes y Agentes</div>
            </div>
          </button>
          <button
            @click="currentRF = 'RF002'"
            :class="['nav-button', { active: currentRF === 'RF002' }]"
            :title="sidebarCollapsed ? 'RF-002 - Gestión de Pólizas' : ''"
          >
            <FileText class="nav-icon" />
            <div class="nav-text">
              <div class="nav-title">RF-002</div>
              <div class="nav-subtitle">Gestión de Pólizas</div>
            </div>
          </button>
          <button
            @click="navegarARegistroPoliza"
            :class="['nav-button', { active: currentRF === 'clientePoliza' }]"
            :title="sidebarCollapsed ? 'Registro de Póliza - Cliente' : ''"
          >
            <Car class="nav-icon" />
            <div class="nav-text">
              <div class="nav-title">Registro</div>
              <div class="nav-subtitle">Póliza Cliente</div>
            </div>
          </button>
          <button
            @click="navegarAMisPolizas"
            :class="['nav-button', { active: currentRF === 'misPolizas' }]"
            :title="sidebarCollapsed ? 'Mis Pólizas' : ''"
          >
            <List class="nav-icon" />
            <div class="nav-text">
              <div class="nav-title">Mis Pólizas</div>
              <div class="nav-subtitle">Gestionar Pólizas</div>
            </div>
          </button>
          <button
            @click="navegarAGestionReclamaciones"
            :class="['nav-button', { active: currentRF === 'gestionReclamaciones' }]"
            :title="sidebarCollapsed ? 'Gestión de Reclamaciones' : ''"
          >
            <FileX class="nav-icon" />
            <div class="nav-text">
              <div class="nav-title">Gestión</div>
              <div class="nav-subtitle">Reclamaciones</div>
            </div>
          </button>
          <button
            @click="navegarAFormularioReclamaciones"
            :class="['nav-button', { active: currentRF === 'formularioReclamaciones' }]"
            :title="sidebarCollapsed ? 'Nueva Reclamación' : ''"
            v-if="isLoggedIn"
          >
            <AlertTriangle class="nav-icon" />
            <div class="nav-text">
              <div class="nav-title">Nueva</div>
              <div class="nav-subtitle">Reclamación</div>
            </div>
          </button>
        </div>
      </div>
    </aside>

    <!-- Contenido Principal -->
    <main :class="['main-content', { 'sidebar-collapsed': sidebarCollapsed }]">
      <transition name="fade" mode="out-in">
        <div :key="currentRF">
          <LandingPage 
            v-if="currentRF === 'landing'" 
            :isDark="isDark" 
            @clienteRegistrado="handleClienteRegistrado"
          />
          <RF001ClienteAgente v-if="currentRF === 'RF001'" :isDark="isDark" />
          <RF002RegistroPolizas v-if="currentRF === 'RF002'" :isDark="isDark" />
          <ClienteRegistroPoliza 
            v-if="currentRF === 'clientePoliza'" 
            :isDark="isDark" 
            :cliente="clienteRegistrado"
          />
          <MisPolizas 
            v-if="currentRF === 'misPolizas'" 
            :isDark="isDark" 
            :cliente="currentUser"
            @navegarARegistro="navegarARegistroPoliza"
          />
          <GestionReclamaciones
            v-if="currentRF === 'gestionReclamaciones'"
            :isDark="isDark"
          />
          <FormularioReclamaciones
            v-if="currentRF === 'formularioReclamaciones'"
            :isDark="isDark"
            :cliente="currentUser"
            @navegarARegistro="navegarARegistroPoliza"
            @reclamacionCreada="handleReclamacionCreada"
          />
        </div>
      </transition>
    </main>

    <!-- Modal de Login -->
    <div v-if="showLoginModal" class="modal-overlay" @click="cerrarModalLogin">
      <div :class="['modal-content', themeClass]" @click.stop>
        <div class="modal-header">
          <h3>Iniciar Sesión</h3>
          <button @click="cerrarModalLogin" class="close-button">
            <X class="close-icon" />
          </button>
        </div>
        
        <form @submit.prevent="login" class="login-form">
          <div class="form-group">
            <label class="form-label">Email</label>
            <input
              v-model="loginData.email"
              type="email"
              class="form-input"
              placeholder="tu@email.com"
              required
            />
          </div>
          
          <div class="form-group">
            <label class="form-label">Teléfono</label>
            <input
              v-model="loginData.telefono"
              type="tel"
              class="form-input"
              placeholder="+1234567890"
              required
            />
          </div>
          
          <button type="submit" class="submit-button">
            Iniciar Sesión
          </button>
        </form>
      </div>
    </div>

    <!-- Modal de Perfil -->
    <div v-if="showProfileModal" class="modal-overlay" @click="showProfileModal = false">
      <div :class="['modal-content', themeClass]" @click.stop>
        <div class="modal-header">
          <h3>Mi Perfil</h3>
          <button @click="showProfileModal = false" class="close-button">
            <X class="close-icon" />
          </button>
        </div>
        
        <div class="profile-info">
          <div class="profile-item">
            <strong>Nombre:</strong> {{ currentUser.nombre }}
          </div>
          <div class="profile-item">
            <strong>Email:</strong> {{ currentUser.email }}
          </div>
          <div class="profile-item">
            <strong>Teléfono:</strong> {{ currentUser.telefono }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { Sun, Moon, Menu, Home, Users, FileText, Car, List, LogIn, User, X, FileX, AlertTriangle } from 'lucide-vue-next'
import LandingPage from './components/LandingPage.vue'
import RF001ClienteAgente from './components/RF001-cliente-agente.vue'
import RF002RegistroPolizas from './components/RF002-registro-polizas.vue'
import ClienteRegistroPoliza from './components/ClienteRegistroPoliza.vue'
import MisPolizas from './components/MisPolizas.vue'
import GestionReclamaciones from './components/GestionReclamaciones.vue'
import FormularioReclamaciones from './components/FormularioReclamaciones.vue'
import apiService from './services/apiService.js'

const isDark = ref(false)
const currentRF = ref('landing')
const sidebarCollapsed = ref(false)
const isLoggedIn = ref(false)
const currentUser = ref({})
const clienteRegistrado = ref({})
const showLoginModal = ref(false)
const showProfileModal = ref(false)
const intentandoNavegarAPoliza = ref(false)

// Datos de login
const loginData = reactive({
  email: '',
  telefono: ''
})

const themeClass = computed(() => {
  return isDark.value ? 'theme-dark' : 'theme-light'
})

const toggleTheme = () => {
  isDark.value = !isDark.value
  localStorage.setItem('theme', isDark.value ? 'dark' : 'light')
}

const toggleSidebar = () => {
  sidebarCollapsed.value = !sidebarCollapsed.value
  localStorage.setItem('sidebarCollapsed', sidebarCollapsed.value.toString())
}

const login = async () => {
  try {
    // Validar que los campos no estén vacíos
    if (!loginData.email || !loginData.telefono) {
      alert('Por favor completa todos los campos')
      return
    }
    
    // Buscar cliente en la base de datos
    const clientes = await apiService.getClientes()
    console.log('Clientes obtenidos del API:', clientes)
    
    const clienteEncontrado = clientes.find(cliente => 
      cliente.email.toLowerCase() === loginData.email.toLowerCase() &&
      cliente.telefono === loginData.telefono
    )
    
    console.log('Cliente encontrado:', clienteEncontrado)
    
    if (clienteEncontrado) {
      isLoggedIn.value = true
      currentUser.value = clienteEncontrado // Guardar el cliente completo con ID
      console.log('currentUser asignado:', currentUser.value)
      showLoginModal.value = false
      
      // Limpiar formulario
      loginData.email = ''
      loginData.telefono = ''
      
      // Si el usuario estaba intentando navegar al registro de póliza, redirigir automáticamente
      if (intentandoNavegarAPoliza.value) {
        navegarARegistroPoliza()
      } else {
        alert('¡Inicio de sesión exitoso!')
      }
    } else {
      alert('Credenciales incorrectas. Verifica tu email y teléfono.')
    }
  } catch (error) {
    console.error('Error en login:', error)
    alert('Error al iniciar sesión. Intenta nuevamente.')
  }
}

const logout = () => {
  isLoggedIn.value = false
  currentUser.value = {}
  showProfileModal.value = false
}

const handleClienteRegistrado = (cliente) => {
  clienteRegistrado.value = cliente
  currentRF.value = 'clientePoliza'
  isLoggedIn.value = true
  currentUser.value = cliente
}

const navegarARegistroPoliza = () => {
  if (!isLoggedIn.value || !currentUser.value.idCliente) {
    // Si no hay usuario logueado, mostrar modal de login
    intentandoNavegarAPoliza.value = true
    showLoginModal.value = true
    return
  }
  
  // Si hay usuario logueado, navegar directamente
  clienteRegistrado.value = currentUser.value
  currentRF.value = 'clientePoliza'
  intentandoNavegarAPoliza.value = false
}

const navegarAMisPolizas = () => {
  if (!isLoggedIn.value || !currentUser.value.idCliente) {
    // Si no hay usuario logueado, mostrar modal de login
    showLoginModal.value = true
    return
  }
  
  // Si hay usuario logueado, navegar directamente
  currentRF.value = 'misPolizas'
}

const navegarAGestionReclamaciones = () => {
  currentRF.value = 'gestionReclamaciones'
}

const navegarAFormularioReclamaciones = () => {
  if (!isLoggedIn.value || !currentUser.value.idCliente) {
    // Si no hay usuario logueado, mostrar modal de login
    showLoginModal.value = true
    return
  }
  
  // Si hay usuario logueado, navegar directamente
  currentRF.value = 'formularioReclamaciones'
}

const handleReclamacionCreada = (reclamacion) => {
  // Opcionalmente navegar a gestión de reclamaciones o mostrar mensaje
  console.log('Reclamación creada:', reclamacion)
}

const cerrarModalLogin = () => {
  showLoginModal.value = false
  intentandoNavegarAPoliza.value = false
}

onMounted(() => {
  const savedTheme = localStorage.getItem('theme')
  if (savedTheme) {
    isDark.value = savedTheme === 'dark'
  } else {
    // Detectar preferencia del sistema
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
    isDark.value = prefersDark
  }
  
  // Cargar estado del sidebar
  const savedSidebarState = localStorage.getItem('sidebarCollapsed')
  if (savedSidebarState) {
    sidebarCollapsed.value = savedSidebarState === 'true'
  }
})
</script>

<style scoped>
/* Contenedor principal */
.app-container {
  min-height: 100vh;
  transition: background-color 0.3s ease, color 0.3s ease;
}

/* Header */
.header {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding: 1rem 0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.sidebar-toggle {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 0.5rem;
  padding: 0.75rem;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 48px;
  width: 48px;
}

.sidebar-toggle:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: scale(1.05);
}

.menu-icon {
  width: 1.25rem;
  height: 1.25rem;
}

.login-button {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 0.5rem;
  padding: 0.75rem 1.5rem;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: white;
  font-weight: 500;
}

.login-button:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.login-icon {
  width: 1.25rem;
  height: 1.25rem;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.profile-button {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 0.5rem;
  padding: 0.75rem 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: white;
}

.profile-button:hover {
  background: rgba(255, 255, 255, 0.2);
}

.profile-icon {
  width: 1.25rem;
  height: 1.25rem;
}

.logout-button {
  background: rgba(239, 68, 68, 0.8);
  border: none;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 0.875rem;
}

.logout-button:hover {
  background: rgba(239, 68, 68, 1);
  transform: scale(1.05);
}

.logo-section {
  display: flex;
  flex-direction: column;
}

.logo-title {
  font-size: 1.5rem;
  font-weight: bold;
  margin: 0;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.logo-subtitle {
  font-size: 0.875rem;
  opacity: 0.7;
  margin: 0;
}

.theme-switch {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 0.5rem;
  padding: 0.5rem;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.theme-switch:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: scale(1.05);
}

.theme-icon {
  width: 1.25rem;
  height: 1.25rem;
}

/* Sidebar */
.sidebar {
  position: fixed;
  left: 0;
  top: 0;
  height: 100vh;
  width: 280px;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border-right: 1px solid rgba(255, 255, 255, 0.1);
  transition: all 0.3s ease;
  z-index: 50;
  padding-top: 80px; /* Espacio para el header */
}

.sidebar.collapsed {
  width: 80px;
}

.sidebar-content {
  padding: 1rem;
  height: 100%;
  overflow-y: auto;
}

.nav-buttons {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.nav-button {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 0.75rem;
  padding: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: left;
  display: flex;
  align-items: center;
  gap: 1rem;
  width: 100%;
}

.nav-button:hover {
  background: rgba(255, 255, 255, 0.15);
  transform: translateX(4px);
}

.nav-button.active {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-color: transparent;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.nav-icon {
  width: 1.5rem;
  height: 1.5rem;
  flex-shrink: 0;
}

.nav-text {
  flex: 1;
  min-width: 0;
}

.nav-title {
  font-size: 1rem;
  font-weight: 600;
  margin-bottom: 0.25rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.nav-subtitle {
  font-size: 0.75rem;
  opacity: 0.8;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Sidebar colapsado */
.sidebar.collapsed .nav-text {
  display: none;
}

.sidebar.collapsed .nav-button {
  justify-content: center;
  padding: 1rem 0.5rem;
}

.sidebar.collapsed .nav-icon {
  width: 1.25rem;
  height: 1.25rem;
}

/* Contenido principal */
.main-content {
  margin-left: 280px;
  padding: 2rem;
  transition: margin-left 0.3s ease;
  min-height: calc(100vh - 80px);
}

.main-content.sidebar-collapsed {
  margin-left: 80px;
}

/* Transiciones */
.fade-enter-active,
.fade-leave-active {
  transition: all 0.3s ease;
}

.fade-enter-from {
  opacity: 0;
  transform: translateX(20px);
}

.fade-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}

/* Tema claro */
.theme-light {
  background: linear-gradient(135deg, #fef7ed 0%, #fed7aa 100%);
  color: #9a3412;
}

.theme-light .modal-content {
  background: rgba(254, 247, 237, 0.9);
  border-color: rgba(253, 186, 116, 0.5);
  color: #9a3412;
}

.theme-light .close-button:hover {
  background: rgba(251, 146, 60, 0.1);
}

.theme-light .close-icon {
  color: #ea580c;
}

.theme-light .form-input {
  background: rgba(254, 247, 237, 0.5);
  border-color: rgba(253, 186, 116, 0.5);
  color: #9a3412;
}

.theme-light .form-input:focus {
  border-color: #fb923c;
  box-shadow: 0 0 0 2px rgba(251, 146, 60, 0.2);
  background: rgba(254, 247, 237, 0.8);
}

.theme-light .submit-button {
  background: linear-gradient(135deg, #fb923c 0%, #f97316 100%);
  color: #fef7ed;
  box-shadow: 0 4px 15px rgba(251, 146, 60, 0.3);
}

.theme-light .submit-button:hover {
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  box-shadow: 0 6px 20px rgba(251, 146, 60, 0.4);
}

.theme-light .profile-item {
  background: rgba(254, 247, 237, 0.3);
  border-color: rgba(253, 186, 116, 0.3);
  border-left-color: #fb923c;
}

.theme-light .header {
  background: rgba(254, 247, 237, 0.8);
  border-bottom-color: rgba(253, 186, 116, 0.3);
}

.theme-light .sidebar {
  background: rgba(254, 247, 237, 0.6);
  border-right-color: rgba(253, 186, 116, 0.3);
}

.theme-light .sidebar-toggle {
  background: rgba(251, 146, 60, 0.1);
  border-color: rgba(251, 146, 60, 0.3);
  color: #ea580c;
}

.theme-light .sidebar-toggle:hover {
  background: rgba(251, 146, 60, 0.2);
}

.theme-light .login-button {
  background: rgba(251, 146, 60, 0.1);
  border-color: rgba(251, 146, 60, 0.3);
  color: #ea580c;
}

.theme-light .login-button:hover {
  background: rgba(251, 146, 60, 0.2);
}

.theme-light .profile-button {
  background: rgba(251, 146, 60, 0.1);
  border-color: rgba(251, 146, 60, 0.3);
  color: #ea580c;
}

.theme-light .profile-button:hover {
  background: rgba(251, 146, 60, 0.2);
}

.theme-light .logo-title {
  background: linear-gradient(135deg, #fb923c 0%, #f97316 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.theme-light .theme-switch {
  background: rgba(251, 146, 60, 0.1);
  border-color: rgba(251, 146, 60, 0.3);
  color: #ea580c;
}

.theme-light .theme-switch:hover {
  background: rgba(251, 146, 60, 0.2);
}

.theme-light .nav-section {
  background: rgba(254, 247, 237, 0.6);
  border-bottom-color: rgba(253, 186, 116, 0.3);
}

.theme-light .nav-button {
  background: rgba(254, 247, 237, 0.5);
  border-color: rgba(253, 186, 116, 0.3);
  color: #9a3412;
}

.theme-light .nav-button:hover {
  background: rgba(254, 247, 237, 0.7);
}

.theme-light .nav-button.active {
  background: linear-gradient(135deg, #fb923c 0%, #f97316 100%);
  color: #fef7ed;
  box-shadow: 0 4px 15px rgba(251, 146, 60, 0.4);
}

/* Tema oscuro */
.theme-dark {
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
  color: #e2e8f0;
}

.theme-dark .modal-content {
  background: rgba(30, 41, 59, 0.9);
  border-color: rgba(71, 85, 105, 0.5);
  color: #e2e8f0;
}

.theme-dark .close-button:hover {
  background: rgba(59, 130, 246, 0.1);
}

.theme-dark .close-icon {
  color: #93c5fd;
}

.theme-dark .form-input {
  background: rgba(30, 41, 59, 0.5);
  border-color: rgba(71, 85, 105, 0.5);
  color: #e2e8f0;
}

.theme-dark .form-input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
  background: rgba(30, 41, 59, 0.8);
}

.theme-dark .submit-button {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: #e2e8f0;
  box-shadow: 0 4px 15px rgba(59, 130, 246, 0.3);
}

.theme-dark .submit-button:hover {
  background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.4);
}

.theme-dark .profile-item {
  background: rgba(51, 65, 85, 0.3);
  border-color: rgba(71, 85, 105, 0.3);
  border-left-color: #3b82f6;
}

.theme-dark .header {
  background: rgba(30, 41, 59, 0.8);
  border-bottom-color: rgba(71, 85, 105, 0.3);
}

.theme-dark .sidebar {
  background: rgba(30, 41, 59, 0.6);
  border-right-color: rgba(71, 85, 105, 0.3);
}

.theme-dark .sidebar-toggle {
  background: rgba(59, 130, 246, 0.1);
  border-color: rgba(59, 130, 246, 0.3);
  color: #93c5fd;
}

.theme-dark .sidebar-toggle:hover {
  background: rgba(59, 130, 246, 0.2);
}

.theme-dark .login-button {
  background: rgba(59, 130, 246, 0.1);
  border-color: rgba(59, 130, 246, 0.3);
  color: #93c5fd;
}

.theme-dark .login-button:hover {
  background: rgba(59, 130, 246, 0.2);
}

.theme-dark .profile-button {
  background: rgba(59, 130, 246, 0.1);
  border-color: rgba(59, 130, 246, 0.3);
  color: #93c5fd;
}

.theme-dark .profile-button:hover {
  background: rgba(59, 130, 246, 0.2);
}

.theme-dark .logo-title {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.theme-dark .theme-switch {
  background: rgba(59, 130, 246, 0.1);
  border-color: rgba(59, 130, 246, 0.3);
  color: #93c5fd;
}

.theme-dark .theme-switch:hover {
  background: rgba(59, 130, 246, 0.2);
}

.theme-dark .nav-section {
  background: rgba(30, 41, 59, 0.6);
  border-bottom-color: rgba(71, 85, 105, 0.3);
}

.theme-dark .nav-button {
  background: rgba(51, 65, 85, 0.5);
  border-color: rgba(71, 85, 105, 0.3);
  color: #e2e8f0;
}

.theme-dark .nav-button:hover {
  background: rgba(51, 65, 85, 0.7);
}

.theme-dark .nav-button.active {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: #e2e8f0;
  box-shadow: 0 4px 15px rgba(59, 130, 246, 0.4);
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
  max-width: 500px;
  width: 90%;
  max-height: 80vh;
  overflow-y: auto;
  backdrop-filter: blur(10px);
  border: 1px solid;
  transition: all 0.3s ease;
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

.login-form {
  display: flex;
  flex-direction: column;
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
  padding: 0.75rem;
  border: 1px solid;
  border-radius: 0.5rem;
  font-size: 1rem;
  transition: all 0.2s ease;
  background: rgba(255, 255, 255, 0.1);
}

.form-input:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
  background: rgba(255, 255, 255, 0.15);
}

.submit-button {
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 0.5rem;
  font-size: 1rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.submit-button:hover {
  transform: translateY(-2px);
}

.profile-info {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.profile-item {
  padding: 1rem;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 0.5rem;
  border-left: 4px solid;
  border: 1px solid;
}

/* Responsive */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }
  
  .header-left,
  .header-right {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .user-profile {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .sidebar {
    width: 100%;
    transform: translateX(-100%);
  }
  
  .sidebar.collapsed {
    transform: translateX(0);
    width: 100%;
  }
  
  .main-content {
    margin-left: 0;
    padding: 1rem;
  }
  
  .main-content.sidebar-collapsed {
    margin-left: 0;
  }
  
  .nav-buttons {
    flex-direction: column;
  }
  
  .nav-button {
    width: 100%;
  }
  
  .sidebar.collapsed .nav-text {
    display: block;
  }
  
  .sidebar.collapsed .nav-button {
    justify-content: flex-start;
    padding: 1rem;
  }
  
  .sidebar.collapsed .nav-icon {
    width: 1.5rem;
    height: 1.5rem;
  }
}

@media (max-width: 480px) {
  .logo-title {
    font-size: 1.25rem;
  }
  
  .nav-title {
    font-size: 1rem;
  }
  
  .nav-subtitle {
    font-size: 0.75rem;
  }
}
</style>
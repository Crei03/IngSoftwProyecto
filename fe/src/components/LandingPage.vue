<template>
  <div :class="['landing-container', themeClass]">

    <!-- Hero Section -->
    <section class="hero-section">
      <div class="hero-content">
        <div class="hero-text">
          <h2 class="hero-title">Protege tu vehículo con la mejor cobertura</h2>
          <p class="hero-description">
            SeguraTuAuto te ofrece las mejores pólizas de seguro para tu automóvil. 
            Cobertura completa, precios competitivos y atención personalizada.
          </p>
          <div class="hero-features">
            <div class="feature-item">
              <Shield class="feature-icon" />
              <span>Cobertura completa</span>
            </div>
            <div class="feature-item">
              <Clock class="feature-icon" />
              <span>Respuesta rápida</span>
            </div>
            <div class="feature-item">
              <Users class="feature-icon" />
              <span>Atención personalizada</span>
            </div>
          </div>
        </div>
        <div class="hero-image">
          <Car class="car-icon" />
        </div>
      </div>
    </section>

    <!-- Formulario de Registro -->
    <section class="registration-section">
      <div class="registration-content">
        <h3 class="section-title">Regístrate y obtén tu póliza</h3>
        <p class="section-description">
          Completa el formulario y uno de nuestros agentes se pondrá en contacto contigo
        </p>
        
        <form @submit.prevent="registrarCliente" class="registration-form">
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">Nombre Completo *</label>
              <input
                v-model="nuevoCliente.nombre"
                type="text"
                class="form-input"
                placeholder="Tu nombre completo"
                required
                :class="{ 'error': errors.nombre }"
              />
              <span v-if="errors.nombre" class="error-message">{{ errors.nombre }}</span>
            </div>
            
            <div class="form-group">
              <label class="form-label">Email *</label>
              <input
                v-model="nuevoCliente.email"
                type="email"
                class="form-input"
                placeholder="tu@email.com"
                required
                :class="{ 'error': errors.email }"
              />
              <span v-if="errors.email" class="error-message">{{ errors.email }}</span>
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">Teléfono *</label>
              <input
                v-model="nuevoCliente.telefono"
                type="tel"
                class="form-input"
                placeholder="+1234567890"
                required
                :class="{ 'error': errors.telefono }"
              />
              <span v-if="errors.telefono" class="error-message">{{ errors.telefono }}</span>
            </div>
            
            <div class="form-group">
              <label class="form-label">Marca del Vehículo</label>
              <input
                v-model="nuevoCliente.marcaVehiculo"
                type="text"
                class="form-input"
                placeholder="Toyota, Honda, etc."
              />
            </div>
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">Modelo del Vehículo</label>
              <input
                v-model="nuevoCliente.modeloVehiculo"
                type="text"
                class="form-input"
                placeholder="Corolla, Civic, etc."
              />
            </div>
            
            <div class="form-group">
              <label class="form-label">Año del Vehículo</label>
              <input
                v-model="nuevoCliente.anoVehiculo"
                type="number"
                class="form-input"
                placeholder="2020"
                min="1990"
                max="2024"
              />
            </div>
          </div>
          
          <button
            type="submit"
            class="submit-button"
            :disabled="loading"
          >
            {{ loading ? 'Registrando...' : 'Registrarme y Solicitar Póliza' }}
          </button>
        </form>
      </div>
    </section>

    <!-- Modal de Login -->
    <div v-if="showLoginModal" class="modal-overlay" @click="showLoginModal = false">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>Iniciar Sesión</h3>
          <button @click="showLoginModal = false" class="close-button">
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
          
          <button type="submit" class="submit-button" :disabled="loading">
            {{ loading ? 'Verificando...' : 'Iniciar Sesión' }}
          </button>
        </form>
      </div>
    </div>

    <!-- Modal de Perfil -->
    <div v-if="showProfileModal" class="modal-overlay" @click="showProfileModal = false">
      <div class="modal-content" @click.stop>
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
          <div class="profile-item">
            <strong>ID Cliente:</strong> {{ currentUser.id }}
          </div>
        </div>
      </div>
    </div>

    <!-- Footer -->
    <footer class="landing-footer">
      <div class="footer-content">
        <div class="footer-section">
          <h4>SeguraTuAuto</h4>
          <p>Tu seguridad en la carretera es nuestra prioridad</p>
        </div>
        <div class="footer-section">
          <h4>Contacto</h4>
          <p>Email: info@seguratuauto.com</p>
          <p>Teléfono: +1 (555) 123-4567</p>
        </div>
        <div class="footer-section">
          <h4>Servicios</h4>
          <p>Seguros de Auto</p>
          <p>Cobertura Completa</p>
          <p>Asistencia en Carretera</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { Shield, Clock, Users, Car, User, X } from 'lucide-vue-next'
import apiService from '../services/apiService.js'

const props = defineProps({
  isDark: Boolean
})

// Computed properties
const themeClass = computed(() => {
  return props.isDark ? 'theme-dark' : 'theme-light'
})

// Estado de la aplicación
const isLoggedIn = ref(false)
const currentUser = ref({})
const loading = ref(false)
const showLoginModal = ref(false)
const showProfileModal = ref(false)

// Datos del formulario
const nuevoCliente = reactive({
  nombre: '',
  email: '',
  telefono: '',
  marcaVehiculo: '',
  modeloVehiculo: '',
  anoVehiculo: ''
})

// Datos de login
const loginData = reactive({
  email: '',
  telefono: ''
})

// Errores de validación
const errors = reactive({
  nombre: '',
  email: '',
  telefono: ''
})

// Validar si el cliente ya existe
const validarClienteExistente = async () => {
  errors.nombre = ''
  errors.email = ''
  errors.telefono = ''
  
  try {
    const clientes = await apiService.getClientes()
    
    // Validar email único
    const emailExiste = clientes.find(cliente => 
      cliente.email.toLowerCase() === nuevoCliente.email.toLowerCase()
    )
    if (emailExiste) {
      errors.email = 'Este email ya está registrado'
    }
    
    // Validar teléfono único
    const telefonoExiste = clientes.find(cliente => 
      cliente.telefono === nuevoCliente.telefono
    )
    if (telefonoExiste) {
      errors.telefono = 'Este teléfono ya está registrado'
    }
    
    return !errors.email && !errors.telefono
  } catch (error) {
    console.error('Error validando cliente:', error)
    return true // Permitir registro si hay error en validación
  }
}

// Registrar nuevo cliente
const registrarCliente = async () => {
  loading.value = true
  
  try {
    // Validar cliente existente
    const esValido = await validarClienteExistente()
    if (!esValido) {
      loading.value = false
      return
    }
    
    // Crear cliente
    const clienteData = {
      nombre: nuevoCliente.nombre,
      email: nuevoCliente.email,
      telefono: nuevoCliente.telefono
    }
    
    const clienteCreado = await apiService.crearCliente(clienteData)
    
    // Limpiar formulario
    Object.keys(nuevoCliente).forEach(key => {
      nuevoCliente[key] = ''
    })
    
    alert('¡Registro exitoso! Uno de nuestros agentes se pondrá en contacto contigo pronto.')
    
  } catch (error) {
    alert('Error al registrar: ' + error.message)
  } finally {
    loading.value = false
  }
}

// Login de cliente
const login = async () => {
  loading.value = true
  
  try {
    const clientes = await apiService.getClientes()
    
    const cliente = clientes.find(c => 
      c.email.toLowerCase() === loginData.email.toLowerCase() &&
      c.telefono === loginData.telefono
    )
    
    if (cliente) {
      isLoggedIn.value = true
      currentUser.value = cliente
      showLoginModal.value = false
      
      // Limpiar datos de login
      loginData.email = ''
      loginData.telefono = ''
      
      alert('¡Bienvenido de vuelta, ' + cliente.nombre + '!')
    } else {
      alert('Email o teléfono incorrectos')
    }
    
  } catch (error) {
    alert('Error al iniciar sesión: ' + error.message)
  } finally {
    loading.value = false
  }
}

// Logout
const logout = () => {
  isLoggedIn.value = false
  currentUser.value = {}
  showProfileModal.value = false
  alert('Sesión cerrada exitosamente')
}

onMounted(() => {
  // Verificar si hay sesión guardada
  const savedUser = localStorage.getItem('currentUser')
  if (savedUser) {
    currentUser.value = JSON.parse(savedUser)
    isLoggedIn.value = true
  }
})
</script>

<style scoped>
/* Contenedor principal */
.landing-container {
  min-height: 100vh;
  transition: background-color 0.3s ease, color 0.3s ease;
}

/* Tema claro */
.theme-light .landing-container {
  background: linear-gradient(135deg, #fef7ed 0%, #fed7aa 100%);
  color: #9a3412;
}

/* Tema oscuro */
.theme-dark .landing-container {
  background: linear-gradient(135deg, #0f172a 0%, #1e293b 100%);
  color: #e2e8f0;
}



.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo-title {
  font-size: 2rem;
  font-weight: bold;
  margin: 0;
  transition: all 0.3s ease;
}

.logo-subtitle {
  font-size: 1rem;
  opacity: 0.7;
  margin: 0;
  transition: all 0.3s ease;
}

/* Tema claro */
.theme-light .logo-title {
  background: linear-gradient(135deg, #fb923c 0%, #f97316 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* Tema oscuro */
.theme-dark .logo-title {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.nav-button {
  padding: 0.75rem 1.5rem;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  border: 1px solid;
}

.nav-button:hover {
  transform: translateY(-2px);
}

/* Tema claro */
.theme-light .nav-button {
  background: rgba(254, 247, 237, 0.5);
  border-color: rgba(253, 186, 116, 0.3);
  color: #9a3412;
}

.theme-light .nav-button:hover {
  background: rgba(254, 247, 237, 0.7);
}

/* Tema oscuro */
.theme-dark .nav-button {
  background: rgba(51, 65, 85, 0.5);
  border-color: rgba(71, 85, 105, 0.3);
  color: #e2e8f0;
}

.theme-dark .nav-button:hover {
  background: rgba(51, 65, 85, 0.7);
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.profile-button {
  padding: 0.75rem 1rem;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  border: 1px solid;
}

/* Tema claro */
.theme-light .profile-button {
  background: rgba(254, 247, 237, 0.5);
  border-color: rgba(253, 186, 116, 0.3);
  color: #9a3412;
}

/* Tema oscuro */
.theme-dark .profile-button {
  background: rgba(51, 65, 85, 0.5);
  border-color: rgba(71, 85, 105, 0.3);
  color: #e2e8f0;
}

.profile-icon {
  width: 1rem;
  height: 1rem;
}

.logout-button {
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: all 0.3s ease;
  color: white;
}

.logout-button:hover {
  transform: scale(1.05);
}

/* Tema claro */
.theme-light .logout-button {
  background: rgba(239, 68, 68, 0.8);
}

.theme-light .logout-button:hover {
  background: rgba(239, 68, 68, 1);
}

/* Tema oscuro */
.theme-dark .logout-button {
  background: rgba(239, 68, 68, 0.8);
}

.theme-dark .logout-button:hover {
  background: rgba(239, 68, 68, 1);
}

/* Hero Section */
.hero-section {
  padding: 4rem 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.hero-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4rem;
  align-items: center;
}

.hero-title {
  font-size: 3rem;
  font-weight: bold;
  margin: 0 0 1rem 0;
  line-height: 1.2;
  transition: color 0.3s ease;
}

.hero-description {
  font-size: 1.25rem;
  opacity: 0.7;
  margin: 0 0 2rem 0;
  line-height: 1.6;
  transition: color 0.3s ease;
}

.hero-features {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  font-size: 1.1rem;
}

.feature-icon {
  width: 1.5rem;
  height: 1.5rem;
  transition: color 0.3s ease;
}

/* Tema claro */
.theme-light .feature-icon {
  color: #f59e0b;
}

/* Tema oscuro */
.theme-dark .feature-icon {
  color: #fbbf24;
}

.hero-image {
  display: flex;
  justify-content: center;
  align-items: center;
}

.car-icon {
  width: 200px;
  height: 200px;
  transition: color 0.3s ease;
}

/* Tema claro */
.theme-light .car-icon {
  color: rgba(251, 146, 60, 0.8);
}

/* Tema oscuro */
.theme-dark .car-icon {
  color: rgba(255, 255, 255, 0.8);
}

/* Registration Section */
.registration-section {
  backdrop-filter: blur(10px);
  padding: 4rem 2rem;
  margin: 2rem auto;
  max-width: 1200px;
  border-radius: 1rem;
  transition: all 0.3s ease;
}

/* Tema claro */
.theme-light .registration-section {
  background: rgba(254, 247, 237, 0.6);
  border: 1px solid rgba(253, 186, 116, 0.5);
  box-shadow: 0 8px 32px rgba(251, 146, 60, 0.1);
}

/* Tema oscuro */
.theme-dark .registration-section {
  background: rgba(30, 41, 59, 0.6);
  border: 1px solid rgba(71, 85, 105, 0.5);
  box-shadow: 0 8px 32px rgba(59, 130, 246, 0.1);
}

.registration-content {
  max-width: 800px;
  margin: 0 auto;
}

.section-title {
  font-size: 2.5rem;
  font-weight: bold;
  text-align: center;
  margin: 0 0 1rem 0;
}

.section-description {
  font-size: 1.25rem;
  text-align: center;
  opacity: 0.9;
  margin: 0 0 3rem 0;
}

.registration-form {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-label {
  font-size: 1rem;
  font-weight: 500;
  opacity: 0.9;
}

.form-input {
  padding: 1rem;
  border: 1px solid;
  border-radius: 0.5rem;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.form-input:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.2);
}

/* Tema claro */
.theme-light .form-input {
  background: rgba(254, 247, 237, 0.5);
  border-color: rgba(253, 186, 116, 0.5);
  color: #9a3412;
}

.theme-light .form-input::placeholder {
  color: rgba(154, 52, 18, 0.6);
}

.theme-light .form-input:focus {
  border-color: #fb923c;
  box-shadow: 0 0 0 2px rgba(251, 146, 60, 0.1);
  background: rgba(254, 247, 237, 0.8);
}

/* Tema oscuro */
.theme-dark .form-input {
  background: rgba(30, 41, 59, 0.5);
  border-color: rgba(71, 85, 105, 0.5);
  color: #e2e8f0;
}

.theme-dark .form-input::placeholder {
  color: rgba(226, 232, 240, 0.6);
}

.theme-dark .form-input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.1);
  background: rgba(30, 41, 59, 0.8);
}

.form-input.error {
  border-color: #ef4444;
}

.error-message {
  color: #ef4444;
  font-size: 0.875rem;
  margin-top: 0.25rem;
}

.submit-button {
  border: none;
  padding: 1rem 2rem;
  border-radius: 0.5rem;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-top: 1rem;
}

.submit-button:hover:not(:disabled) {
  transform: translateY(-2px);
}

/* Tema claro */
.theme-light .submit-button {
  background: linear-gradient(135deg, #fb923c 0%, #f97316 100%);
  color: #fef7ed;
  box-shadow: 0 4px 15px rgba(251, 146, 60, 0.3);
}

.theme-light .submit-button:hover:not(:disabled) {
  background: linear-gradient(135deg, #f97316 0%, #ea580c 100%);
  box-shadow: 0 6px 20px rgba(251, 146, 60, 0.4);
}

/* Tema oscuro */
.theme-dark .submit-button {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%);
  color: #e2e8f0;
  box-shadow: 0 4px 15px rgba(59, 130, 246, 0.3);
}

.theme-dark .submit-button:hover:not(:disabled) {
  background: linear-gradient(135deg, #2563eb 0%, #1d4ed8 100%);
  box-shadow: 0 6px 20px rgba(59, 130, 246, 0.4);
}

.submit-button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Modal */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  transition: all 0.3s ease;
}

.modal-content {
  border-radius: 1rem;
  padding: 2rem;
  max-width: 500px;
  width: 90%;
  max-height: 80vh;
  overflow-y: auto;
  transition: all 0.3s ease;
}

/* Tema claro */
.theme-light .modal-overlay {
  background: rgba(0, 0, 0, 0.8);
}

.theme-light .modal-content {
  background: white;
  color: #1f2937;
}

/* Tema oscuro */
.theme-dark .modal-overlay {
  background: rgba(0, 0, 0, 0.9);
}

.theme-dark .modal-content {
  background: #1e293b;
  color: #e2e8f0;
  border: 1px solid rgba(71, 85, 105, 0.5);
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
  transition: background-color 0.2s ease;
}

.close-icon {
  width: 1.5rem;
  height: 1.5rem;
  transition: color 0.3s ease;
}

/* Tema claro */
.theme-light .close-button:hover {
  background: #f3f4f6;
}

.theme-light .close-icon {
  color: #6b7280;
}

/* Tema oscuro */
.theme-dark .close-button:hover {
  background: #374151;
}

.theme-dark .close-icon {
  color: #9ca3af;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.profile-info {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.profile-item {
  padding: 1rem;
  border-radius: 0.5rem;
  border-left: 4px solid;
  transition: all 0.3s ease;
}

/* Tema claro */
.theme-light .profile-item {
  background: #f9fafb;
  border-left-color: #3b82f6;
}

/* Tema oscuro */
.theme-dark .profile-item {
  background: #374151;
  border-left-color: #3b82f6;
}

/* Footer */
.landing-footer {
  padding: 3rem 2rem;
  margin-top: 4rem;
  transition: all 0.3s ease;
}

/* Tema claro */
.theme-light .landing-footer {
  background: rgba(251, 146, 60, 0.1);
  border-top: 1px solid rgba(253, 186, 116, 0.3);
}

/* Tema oscuro */
.theme-dark .landing-footer {
  background: rgba(0, 0, 0, 0.3);
  border-top: 1px solid rgba(71, 85, 105, 0.3);
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.footer-section h4 {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0 0 1rem 0;
}

.footer-section p {
  margin: 0.5rem 0;
  opacity: 0.8;
}

/* Responsive */
@media (max-width: 768px) {
  .hero-content {
    grid-template-columns: 1fr;
    text-align: center;
  }
  
  .hero-title {
    font-size: 2rem;
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .header-content {
    flex-direction: column;
    gap: 1rem;
  }
  
  .user-profile {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .car-icon {
    width: 150px;
    height: 150px;
  }
}

@media (max-width: 480px) {
  .hero-section,
  .registration-section {
    padding: 2rem 1rem;
  }
  
  .hero-title {
    font-size: 1.75rem;
  }
  
  .section-title {
    font-size: 2rem;
  }
}
</style> 
<template>
  <div :class="['formulario-reclamaciones', themeClass]">
    <!-- Header -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <AlertTriangle class="page-icon" />
          <div class="header-text">
            <h1 class="page-title">Nueva Reclamación</h1>
            <p class="page-subtitle">Reportar un siniestro y solicitar indemnización</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Información del Cliente -->
    <div v-if="cliente" class="cliente-info">
      <div class="info-header">
        <User class="info-icon" />
        <h3>Información del Cliente</h3>
      </div>
      <div class="info-content">
        <div class="info-item">
          <span class="info-label">Nombre:</span>
          <span class="info-value">{{ cliente.nombre }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">Email:</span>
          <span class="info-value">{{ cliente.email }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">Teléfono:</span>
          <span class="info-value">{{ cliente.telefono || 'No registrado' }}</span>
        </div>
      </div>
    </div>

    <!-- Selección de Póliza -->
    <div class="polizas-section">
      <div class="section-header">
        <FileText class="section-icon" />
        <h3>Seleccionar Póliza</h3>
      </div>
      
      <div v-if="loadingPolizas" class="loading-container">
        <div class="spinner"></div>
        <p>Cargando pólizas aprobadas...</p>
      </div>
      
      <div v-else-if="polizasAprobadas.length === 0" class="empty-state">
        <FileX class="empty-icon" />
        <h4>No tienes pólizas aprobadas</h4>
        <p>Para hacer una reclamación necesitas tener al menos una póliza aprobada.</p>
        <button @click="$emit('navegarARegistro')" class="btn-primary">
          Registrar Nueva Póliza
        </button>
      </div>
      
      <div v-else class="polizas-grid">
        <div 
          v-for="poliza in polizasAprobadas" 
          :key="poliza.id || poliza.idPoliza || poliza.polizaId"
          :class="['poliza-card', { selected: formulario.polizaId === (poliza.id || poliza.idPoliza || poliza.polizaId) }]"
          @click="seleccionarPoliza(poliza)"
        >
          <div class="poliza-header">
            <span class="poliza-numero">{{ poliza.numeroPoliza }}</span>
            <span class="poliza-estado">{{ poliza.estado }}</span>
          </div>
          <div class="poliza-details">
            <div class="poliza-detail">
              <span class="detail-label">Tipo:</span>
              <span class="detail-value">{{ poliza.tipoSeguro }}</span>
            </div>
            <div class="poliza-detail">
              <span class="detail-label">Prima:</span>
              <span class="detail-value">{{ formatearMonto(poliza.prima) }}</span>
            </div>
            <div class="poliza-detail">
              <span class="detail-label">Vigencia:</span>
              <span class="detail-value">
                {{ formatearFecha(poliza.fechaEmision) }} - {{ formatearFecha(poliza.fechaVencimiento) }}
              </span>
            </div>
          </div>
          <div v-if="formulario.polizaId === (poliza.id || poliza.idPoliza || poliza.polizaId)" class="selected-indicator">
            <CheckCircle class="check-icon" />
          </div>
        </div>
      </div>
    </div>

    <!-- Formulario de Reclamación -->
    <div v-if="polizaSeleccionada" class="formulario-section">
      <div class="section-header">
        <AlertTriangle class="section-icon" />
        <h3>Detalles de la Reclamación</h3>
      </div>
      
      <form @submit.prevent="enviarReclamacion" class="reclamacion-form">
        <div class="form-grid">
          <div class="form-group full-width">
            <label class="form-label required">Descripción del Siniestro</label>
            <textarea
              v-model="formulario.descripcion"
              class="form-textarea"
              rows="6"
              placeholder="Describe detalladamente lo que ocurrió: fecha, hora, lugar, circunstancias, daños, etc."
              required
            ></textarea>
            <div class="form-help">
              Proporciona la mayor cantidad de detalles posible para agilizar el proceso de evaluación.
            </div>
          </div>
          
          <div class="form-group">
            <label class="form-label required">Fecha del Siniestro</label>
            <input
              v-model="formulario.fechaSiniestro"
              type="date"
              class="form-input"
              :max="fechaMaxima"
              required
            />
          </div>
          
          <div class="form-group">
            <label class="form-label">Hora del Siniestro</label>
            <input
              v-model="formulario.horaSiniestro"
              type="time"
              class="form-input"
            />
          </div>
          
          <div class="form-group">
            <label class="form-label">Lugar del Siniestro</label>
            <input
              v-model="formulario.lugar"
              type="text"
              class="form-input"
              placeholder="Dirección o ubicación donde ocurrió"
            />
          </div>
          
          <div class="form-group">
            <label class="form-label required">Monto Estimado de Daños</label>
            <input
              v-model="formulario.montoReclamado"
              type="number"
              step="0.01"
              min="0"
              max="999999.99"
              class="form-input"
              placeholder="0.00"
              required
            />
            <div class="form-help">
              Estimación inicial de los daños (puede ser ajustada durante la evaluación)
            </div>
          </div>
          
          <div class="form-group">
            <label class="form-label">Tipo de Siniestro</label>
            <select v-model="formulario.tipoSiniestro" class="form-select">
              <option value="">Seleccionar tipo</option>
              <option value="COLISION">Colisión</option>
              <option value="ROBO">Robo</option>
              <option value="INCENDIO">Incendio</option>
              <option value="VANDALISMO">Vandalismo</option>
              <option value="FENOMENOS_NATURALES">Fenómenos Naturales</option>
              <option value="CRISTALES">Rotura de Cristales</option>
              <option value="OTROS">Otros</option>
            </select>
          </div>
          
          <div class="form-group full-width">
            <label class="form-label">Información Adicional</label>
            <textarea
              v-model="formulario.informacionAdicional"
              class="form-textarea"
              rows="3"
              placeholder="Cualquier información adicional que consideres relevante..."
            ></textarea>
          </div>
        </div>
        
        <!-- Términos y Condiciones -->
        <div class="terms-section">
          <div class="terms-checkbox">
            <input
              id="terminos"
              v-model="formulario.aceptaTerminos"
              type="checkbox"
              class="checkbox-input"
              required
            />
            <label for="terminos" class="checkbox-label">
              Acepto los términos y condiciones y declaro que la información proporcionada es veraz y completa.
            </label>
          </div>
          <div class="terms-info">
            <Info class="info-icon" />
            <p>
              Al enviar esta reclamación, declaras bajo tu responsabilidad que toda la información 
              proporcionada es verdadera y exacta. La información falsa puede resultar en la 
              denegación de la reclamación.
            </p>
          </div>
        </div>
        
        <!-- Botones -->
        <div class="form-actions">
          <div class="action-buttons">
            <button
              type="button"
              @click="limpiarFormulario"
              class="btn-secondary"
              :disabled="enviando"
            >
              Limpiar Formulario
            </button>
            <button
              type="submit"
              class="btn-primary"
              :disabled="enviando"
            >
              <AlertTriangle v-if="enviando" class="btn-icon spinning" />
              <Send v-else class="btn-icon" />
              {{ enviando ? 'Enviando...' : 'Enviar Reclamación' }}
            </button>
          </div>
        </div>
      </form>
    </div>

    <!-- Mensajes -->
    <div v-if="mensaje" :class="['mensaje', tipoMensaje]">
      <component :is="tipoMensaje === 'success' ? 'CheckCircle' : 'AlertTriangle'" class="mensaje-icon" />
      {{ mensaje }}
    </div>

    <!-- Modal de Confirmación -->
    <div v-if="showConfirmModal" class="modal-overlay" @click="cancelarEnvio">
      <div :class="['modal-content', themeClass]" @click.stop>
        <div class="modal-header">
          <h3>Confirmar Envío de Reclamación</h3>
          <button @click="cancelarEnvio" class="close-button">
            <X class="close-icon" />
          </button>
        </div>
        
        <div class="modal-body">
          <div class="confirm-info">
            <AlertTriangle class="confirm-icon" />
            <p>¿Estás seguro de que deseas enviar esta reclamación?</p>
            <p class="confirm-subtitle">
              Una vez enviada, no podrás modificar la información. Asegúrate de que todos los datos son correctos.
            </p>
          </div>
          
          <div class="confirm-details">
            <div class="detail-row">
              <span class="detail-label">Póliza:</span>
              <span class="detail-value">{{ polizaSeleccionada?.numeroPoliza }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">Monto:</span>
              <span class="detail-value">{{ formatearMonto(formulario.montoReclamado) }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">Fecha del Siniestro:</span>
              <span class="detail-value">{{ formatearFecha(formulario.fechaSiniestro) }}</span>
            </div>
          </div>
        </div>
        
        <div class="modal-actions">
          <button @click="cancelarEnvio" class="btn-secondary">
            Cancelar
          </button>
          <button @click="confirmarEnvio" class="btn-primary" :disabled="enviando">
            <Send class="btn-icon" />
            Confirmar Envío
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { 
  AlertTriangle, User, FileText, FileX, CheckCircle, Send, 
  Info, X
} from 'lucide-vue-next'
import apiService from '../services/apiService.js'

const props = defineProps({
  isDark: {
    type: Boolean,
    default: false
  },
  cliente: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['navegarARegistro', 'reclamacionCreada'])

const themeClass = computed(() => {
  return props.isDark ? 'theme-dark' : 'theme-light'
})

// Estado reactivo
const loadingPolizas = ref(false)
const enviando = ref(false)
const polizasAprobadas = ref([])
const polizaSeleccionada = ref(null)
const mensaje = ref('')
const tipoMensaje = ref('info')
const showConfirmModal = ref(false)

// Formulario
const formulario = reactive({
  polizaId: null,
  descripcion: '',
  fechaSiniestro: '',
  horaSiniestro: '',
  lugar: '',
  montoReclamado: '',
  tipoSiniestro: '',
  informacionAdicional: '',
  aceptaTerminos: false
})

// Computed
const fechaMaxima = computed(() => {
  return new Date().toISOString().split('T')[0]
})

const formularioValido = computed(() => {
  return (
    formulario.polizaId && 
    formulario.descripcion.trim() && 
    formulario.fechaSiniestro && 
    formulario.montoReclamado && 
    parseFloat(formulario.montoReclamado) > 0 &&
    formulario.aceptaTerminos
  )
})

// Métodos
const cargarPolizasAprobadas = async () => {
  console.log('Cliente recibido en FormularioReclamaciones:', props.cliente)
  console.log('Cliente.idCliente:', props.cliente?.idCliente)
  console.log('Cliente.id:', props.cliente?.id)
  
  if (!props.cliente?.idCliente && !props.cliente?.id) {
    mensaje.value = 'Error: No se pudo identificar el cliente'
    tipoMensaje.value = 'error'
    return
  }
  
  loadingPolizas.value = true
  
  try {
    // Intentar con idCliente primero, luego con id
    const clienteId = props.cliente.idCliente || props.cliente.id
    console.log('Usando clienteId:', clienteId)
    
    const polizas = await apiService.getPolizasPorCliente(clienteId)
    console.log('Todas las pólizas recibidas:', polizas)
    polizasAprobadas.value = polizas.filter(poliza => poliza.estado === 'APROBADA')
    console.log('Pólizas aprobadas:', polizasAprobadas.value)
    
    // Verificar las propiedades de las pólizas
    if (polizasAprobadas.value.length > 0) {
      console.log('Primera póliza aprobada:', polizasAprobadas.value[0])
      console.log('Propiedades de la primera póliza:', Object.keys(polizasAprobadas.value[0]))
    }
    
    if (polizasAprobadas.value.length === 0) {
      mensaje.value = 'No tienes pólizas aprobadas. Necesitas una póliza aprobada para hacer una reclamación.'
      tipoMensaje.value = 'info'
    }
  } catch (error) {
    console.error('Error al cargar pólizas:', error)
    mensaje.value = 'Error al cargar tus pólizas: ' + error.message
    tipoMensaje.value = 'error'
  } finally {
    loadingPolizas.value = false
  }
}

const seleccionarPoliza = (poliza) => {
  console.log('Seleccionando póliza:', poliza)
  console.log('Propiedades de la póliza:', Object.keys(poliza))
  
  // Manejar diferentes campos posibles para el ID
  const polizaId = poliza.id || poliza.idPoliza || poliza.polizaId
  console.log('ID de la póliza encontrado:', polizaId)
  
  if (!polizaId) {
    console.error('No se pudo encontrar un ID válido para la póliza')
    mensaje.value = 'Error: No se pudo identificar la póliza seleccionada'
    tipoMensaje.value = 'error'
    return
  }
  
  formulario.polizaId = polizaId
  polizaSeleccionada.value = poliza
  console.log('formulario.polizaId después de asignar:', formulario.polizaId)
  mensaje.value = `Póliza ${poliza.numeroPoliza} seleccionada`
  tipoMensaje.value = 'success'
}

const enviarReclamacion = () => {
  console.log('Intentando enviar reclamación...')
  console.log('formulario.polizaId:', formulario.polizaId)
  console.log('Tipo de formulario.polizaId:', typeof formulario.polizaId)
  console.log('polizaSeleccionada.value:', polizaSeleccionada.value)
  
  // Validaciones adicionales
  if (!formulario.polizaId) {
    console.log('Error: No hay póliza seleccionada')
    mensaje.value = 'Debes seleccionar una póliza'
    tipoMensaje.value = 'error'
    return
  }
  
  if (!formulario.descripcion.trim()) {
    mensaje.value = 'La descripción del siniestro es obligatoria'
    tipoMensaje.value = 'error'
    return
  }
  
  if (!formulario.fechaSiniestro) {
    mensaje.value = 'La fecha del siniestro es obligatoria'
    tipoMensaje.value = 'error'
    return
  }
  
  if (!formulario.montoReclamado || formulario.montoReclamado <= 0) {
    mensaje.value = 'El monto reclamado debe ser mayor a 0'
    tipoMensaje.value = 'error'
    return
  }
  
  // Mostrar modal de confirmación
  showConfirmModal.value = true
}

const confirmarEnvio = async () => {
  enviando.value = true
  
  try {
    const reclamacionData = {
      polizaId: formulario.polizaId,
      descripcion: formulario.descripcion.trim(),
      montoReclamado: parseFloat(formulario.montoReclamado)
    }
    
    console.log('Enviando datos de reclamación:', reclamacionData)
    
    const resultado = await apiService.crearReclamacion(reclamacionData)
    
    mensaje.value = 'Reclamación enviada exitosamente. Se te asignará un número de seguimiento.'
    tipoMensaje.value = 'success'
    
    // Limpiar formulario
    limpiarFormulario()
    
    // Emitir evento
    emit('reclamacionCreada', resultado)
    
    showConfirmModal.value = false
  } catch (error) {
    console.error('Error al crear reclamación:', error)
    mensaje.value = 'Error al enviar la reclamación: ' + error.message
    tipoMensaje.value = 'error'
  } finally {
    enviando.value = false
  }
}

const cancelarEnvio = () => {
  showConfirmModal.value = false
}

const limpiarFormulario = () => {
  formulario.polizaId = null
  formulario.descripcion = ''
  formulario.fechaSiniestro = ''
  formulario.horaSiniestro = ''
  formulario.lugar = ''
  formulario.montoReclamado = ''
  formulario.tipoSiniestro = ''
  formulario.informacionAdicional = ''
  formulario.aceptaTerminos = false
  polizaSeleccionada.value = null
  mensaje.value = ''
}

// Utilidades
const formatearMonto = (monto) => {
  if (!monto) return 'N/A'
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD'
  }).format(monto)
}

const formatearFecha = (fecha) => {
  if (!fecha) return 'N/A'
  return new Date(fecha).toLocaleDateString('es-ES', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

// Lifecycle
onMounted(() => {
  cargarPolizasAprobadas()
})
</script>

<style scoped>
.formulario-reclamaciones {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

/* Header */
.page-header {
  margin-bottom: 2rem;
}

.header-content {
  display: flex;
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
  color: #f39c12;
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

/* Información del Cliente */
.cliente-info {
  background: var(--card-bg);
  border-radius: 1rem;
  border: 1px solid var(--border-color);
  margin-bottom: 2rem;
  overflow: hidden;
}

.info-header {
  background: var(--primary-light);
  padding: 1rem 1.5rem;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  border-bottom: 1px solid var(--border-color);
}

.info-icon {
  width: 1.25rem;
  height: 1.25rem;
  color: var(--primary-color);
}

.info-header h3 {
  margin: 0;
  color: var(--text-primary);
  font-weight: 600;
}

.info-content {
  padding: 1.5rem;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1rem;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-label {
  font-weight: 500;
  color: var(--text-secondary);
}

.info-value {
  font-weight: 600;
  color: var(--text-primary);
}

/* Secciones */
.polizas-section,
.formulario-section {
  background: var(--card-bg);
  border-radius: 1rem;
  border: 1px solid var(--border-color);
  margin-bottom: 2rem;
  overflow: hidden;
}

.section-header {
  background: var(--section-header-bg);
  padding: 1rem 1.5rem;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  border-bottom: 1px solid var(--border-color);
}

.section-icon {
  width: 1.25rem;
  height: 1.25rem;
  color: var(--primary-color);
}

.section-header h3 {
  margin: 0;
  color: var(--text-primary);
  font-weight: 600;
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
  width: 2rem;
  height: 2rem;
  border: 2px solid var(--border-color);
  border-top: 2px solid var(--primary-color);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

/* Empty State */
.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem;
  text-align: center;
}

.empty-icon {
  width: 4rem;
  height: 4rem;
  color: var(--text-secondary);
  opacity: 0.5;
  margin-bottom: 1rem;
}

.empty-state h4 {
  margin: 0 0 0.5rem 0;
  color: var(--text-primary);
}

.empty-state p {
  margin: 0 0 1.5rem 0;
  color: var(--text-secondary);
}

/* Pólizas Grid */
.polizas-grid {
  padding: 1.5rem;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 1.5rem;
}

.poliza-card {
  background: var(--card-bg);
  border: 2px solid var(--border-color);
  border-radius: 0.75rem;
  padding: 1.5rem;
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
}

.poliza-card:hover {
  border-color: var(--primary-color);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.poliza-card.selected {
  border-color: var(--primary-color);
  background: var(--primary-light);
}

.poliza-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.poliza-numero {
  font-weight: 700;
  font-size: 1.1rem;
  color: var(--primary-color);
}

.poliza-estado {
  background: rgba(46, 204, 113, 0.1);
  color: #2ecc71;
  padding: 0.25rem 0.75rem;
  border-radius: 1rem;
  font-size: 0.8rem;
  font-weight: 600;
  text-transform: uppercase;
}

.poliza-details {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.poliza-detail {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.detail-label {
  font-weight: 500;
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.detail-value {
  font-weight: 600;
  color: var(--text-primary);
  font-size: 0.9rem;
}

.selected-indicator {
  position: absolute;
  top: 1rem;
  right: 1rem;
  background: var(--primary-color);
  border-radius: 50%;
  padding: 0.25rem;
}

.check-icon {
  width: 1rem;
  height: 1rem;
  color: white;
}

/* Formulario */
.reclamacion-form {
  padding: 1.5rem;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group.full-width {
  grid-column: 1 / -1;
}

.form-label {
  font-weight: 600;
  color: var(--text-primary);
  font-size: 0.9rem;
}

.form-label.required::after {
  content: " *";
  color: #e74c3c;
}

.form-input,
.form-select,
.form-textarea {
  padding: 0.75rem;
  border: 2px solid var(--border-color);
  border-radius: 0.5rem;
  background: var(--input-bg);
  color: var(--text-primary);
  font-size: 1rem;
  transition: border-color 0.2s ease;
}

.form-input:focus,
.form-select:focus,
.form-textarea:focus {
  outline: none;
  border-color: var(--primary-color);
}

.form-textarea {
  resize: vertical;
  min-height: 100px;
}

.form-help {
  font-size: 0.8rem;
  color: var(--text-secondary);
  font-style: italic;
}

/* Términos */
.terms-section {
  background: var(--warning-bg);
  border: 1px solid var(--warning-border);
  border-radius: 0.5rem;
  padding: 1.5rem;
  margin-bottom: 2rem;
}

.terms-checkbox {
  display: flex;
  align-items: flex-start;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.checkbox-input {
  margin-top: 0.25rem;
  width: 1rem;
  height: 1rem;
}

.checkbox-label {
  font-weight: 500;
  color: var(--text-primary);
  line-height: 1.4;
}

.terms-info {
  display: flex;
  align-items: flex-start;
  gap: 0.75rem;
  padding: 1rem;
  background: var(--info-bg);
  border-radius: 0.5rem;
  border: 1px solid var(--info-border);
}

.terms-info .info-icon {
  width: 1rem;
  height: 1rem;
  color: var(--info-color);
  margin-top: 0.125rem;
  flex-shrink: 0;
}

.terms-info p {
  margin: 0;
  font-size: 0.9rem;
  color: var(--text-secondary);
  line-height: 1.4;
}

/* Botones */
.form-actions {
  display: flex;
  justify-content: flex-end;
}

.action-buttons {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
}

.btn-primary,
.btn-secondary {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 0.5rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 1rem;
}

.btn-primary {
  background: var(--primary-color);
  color: white;
}

.btn-primary:hover:not(:disabled) {
  background: var(--primary-dark);
  transform: translateY(-1px);
}

.btn-primary:disabled {
  background: var(--disabled-bg);
  color: var(--disabled-text);
  cursor: not-allowed;
}

.btn-secondary {
  background: var(--secondary-bg);
  color: var(--text-primary);
  border: 1px solid var(--border-color);
}

.btn-secondary:hover:not(:disabled) {
  background: var(--secondary-hover);
}

.btn-icon {
  width: 1rem;
  height: 1rem;
}

.spinning {
  animation: spin 1s linear infinite;
}

/* Mensajes */
.mensaje {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem;
  border-radius: 0.5rem;
  margin-bottom: 1rem;
  font-weight: 500;
}

.mensaje.success {
  background: var(--success-bg);
  color: var(--success-color);
  border: 1px solid var(--success-border);
}

.mensaje.error {
  background: var(--error-bg);
  color: var(--error-color);
  border: 1px solid var(--error-border);
}

.mensaje.info {
  background: var(--info-bg);
  color: var(--info-color);
  border: 1px solid var(--info-border);
}

.mensaje-icon {
  width: 1.25rem;
  height: 1.25rem;
  flex-shrink: 0;
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
  max-width: 500px;
  width: 100%;
  max-height: 90vh;
  overflow-y: auto;
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

.confirm-info {
  text-align: center;
  margin-bottom: 1.5rem;
}

.confirm-icon {
  width: 3rem;
  height: 3rem;
  color: #f39c12;
  margin-bottom: 1rem;
}

.confirm-info p {
  margin: 0.5rem 0;
  color: var(--text-primary);
}

.confirm-subtitle {
  font-size: 0.9rem;
  color: var(--text-secondary);
}

.confirm-details {
  background: var(--section-bg);
  padding: 1rem;
  border-radius: 0.5rem;
  border: 1px solid var(--border-color);
}

.detail-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 0;
  border-bottom: 1px solid var(--border-light);
}

.detail-row:last-child {
  border-bottom: none;
}

.modal-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  padding: 1.5rem;
  border-top: 1px solid var(--border-color);
}

/* Responsive */
@media (max-width: 768px) {
  .formulario-reclamaciones {
    padding: 1rem;
  }
  
  .form-grid {
    grid-template-columns: 1fr;
  }
  
  .polizas-grid {
    grid-template-columns: 1fr;
  }
  
  .info-content {
    grid-template-columns: 1fr;
  }
  
  .form-actions {
    gap: 1rem;
  }
  
  .action-buttons {
    flex-direction: column;
  }
  
  .modal-actions {
    flex-direction: column;
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
  --section-header-bg: #1a202c;
  --primary-color: #3182ce;
  --primary-dark: #2c5aa0;
  --primary-light: rgba(49, 130, 206, 0.1);
  --secondary-bg: #4a5568;
  --secondary-hover: #2d3748;
  --disabled-bg: #4a5568;
  --disabled-text: #a0a0a0;
  --success-bg: rgba(72, 187, 120, 0.1);
  --success-color: #68d391;
  --success-border: rgba(72, 187, 120, 0.3);
  --error-bg: rgba(245, 101, 101, 0.1);
  --error-color: #f56565;
  --error-border: rgba(245, 101, 101, 0.3);
  --info-bg: rgba(66, 153, 225, 0.1);
  --info-color: #4299e1;
  --info-border: rgba(66, 153, 225, 0.3);
  --warning-bg: rgba(237, 137, 54, 0.1);
  --warning-border: rgba(237, 137, 54, 0.3);
  --warning-color: #ed8936;
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
  --section-header-bg: #f7fafc;
  --primary-color: #3182ce;
  --primary-dark: #2c5aa0;
  --primary-light: rgba(49, 130, 206, 0.1);
  --secondary-bg: #f7fafc;
  --secondary-hover: #edf2f7;
  --disabled-bg: #e2e8f0;
  --disabled-text: #a0aec0;
  --success-bg: rgba(72, 187, 120, 0.1);
  --success-color: #38a169;
  --success-border: rgba(72, 187, 120, 0.3);
  --error-bg: rgba(245, 101, 101, 0.1);
  --error-color: #e53e3e;
  --error-border: rgba(245, 101, 101, 0.3);
  --info-bg: rgba(66, 153, 225, 0.1);
  --info-color: #3182ce;
  --info-border: rgba(66, 153, 225, 0.3);
  --warning-bg: rgba(237, 137, 54, 0.1);
  --warning-border: rgba(237, 137, 54, 0.3);
  --warning-color: #d69e2e;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style>

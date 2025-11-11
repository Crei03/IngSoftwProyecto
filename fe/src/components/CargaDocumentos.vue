<template>
  <div class="carga-documentos">
    <!-- Header del componente -->
    <div class="upload-header">
      <div class="header-info">
        <FileText class="header-icon" />
        <div class="header-text">
          <h3 class="header-title">Documentos Adjuntos</h3>
          <p class="header-subtitle">
            Sube archivos que respalden tu reclamación (PDF, DOC, DOCX, JPG, PNG)
          </p>
        </div>
      </div>
      <div class="upload-limits">
        <div class="limit-item">
          <span class="limit-label">Máximo:</span>
          <span class="limit-value">{{ maxFiles }} archivos</span>
        </div>
        <div class="limit-item">
          <span class="limit-label">Tamaño:</span>
          <span class="limit-value">{{ maxFileSize }}</span>
        </div>
      </div>
    </div>

    <!-- Zona de arrastre -->
    <div 
      ref="dropZone"
      :class="['drop-zone', { 'drop-zone--dragover': isDragging, 'drop-zone--disabled': isDisabled }]"
      @drop.prevent="handleDrop"
      @dragover.prevent="handleDragOver"
      @dragenter.prevent="handleDragEnter"
      @dragleave.prevent="handleDragLeave"
      @click="openFileDialog"
    >
      <input 
        ref="fileInput"
        type="file"
        multiple
        :accept="acceptedTypes"
        @change="handleFileSelect"
        style="display: none"
        :disabled="isDisabled"
      />
      
      <div class="drop-zone-content">
        <Upload class="drop-zone-icon" />
        <h4 class="drop-zone-title">
          {{ isDragging ? 'Suelta los archivos aquí' : 'Arrastra archivos aquí o haz clic para seleccionar' }}
        </h4>
        <p class="drop-zone-subtitle">
          Formatos soportados: PDF, DOC, DOCX, JPG, PNG (máximo {{ maxFileSize }} cada uno)
        </p>
      </div>
    </div>

    <!-- Lista de archivos seleccionados -->
    <div v-if="files.length > 0" class="files-list">
      <div class="files-header">
        <h4 class="files-title">
          <FileText class="files-icon" />
          Archivos Seleccionados ({{ files.length }})
        </h4>
        <button 
          @click="clearFiles" 
          class="btn-clear"
          :disabled="uploading"
        >
          <X class="btn-icon" />
          Limpiar
        </button>
      </div>
      
      <div class="files-grid">
        <div 
          v-for="(file, index) in files" 
          :key="index"
          :class="['file-item', { 'file-item--error': file.error, 'file-item--uploading': file.uploading }]"
        >
          <!-- Icono del archivo -->
          <div class="file-icon">
            <FileText v-if="file.isPDF" class="file-type-icon pdf" />
            <Image v-else-if="file.isImage" class="file-type-icon image" />
            <FileText v-else class="file-type-icon document" />
          </div>
          
          <!-- Información del archivo -->
          <div class="file-info">
            <div class="file-name" :title="file.name">{{ file.name }}</div>
            <div class="file-details">
              <span class="file-size">{{ file.sizeFormatted }}</span>
              <span class="file-type">{{ file.type }}</span>
            </div>
            <div v-if="file.error" class="file-error">
              <AlertTriangle class="error-icon" />
              {{ file.error }}
            </div>
          </div>
          
          <!-- Progreso de subida -->
          <div v-if="file.uploading" class="file-progress">
            <div class="progress-bar">
              <div 
                class="progress-fill" 
                :style="{ width: file.progress + '%' }"
              ></div>
            </div>
            <span class="progress-text">{{ file.progress }}%</span>
          </div>
          
          <!-- Estado del archivo -->
          <div class="file-actions">
            <button 
              v-if="!file.uploading && !file.uploaded"
              @click="removeFile(index)"
              class="btn-remove"
            >
              <X class="action-icon" />
            </button>
            <CheckCircle v-if="file.uploaded" class="status-icon success" />
            <Loader v-if="file.uploading" class="status-icon loading" />
          </div>
        </div>
      </div>
    </div>

    <!-- Descripción opcional -->
    <div v-if="files.length > 0" class="description-section">
      <label class="description-label">
        <MessageSquare class="label-icon" />
        Descripción (opcional)
      </label>
      <textarea
        v-model="descripcion"
        class="description-input"
        rows="3"
        placeholder="Describe brevemente los documentos adjuntos..."
        :disabled="uploading"
      ></textarea>
    </div>

    <!-- Acciones -->
    <div v-if="files.length > 0" class="actions-section">
      <button 
        @click="uploadFiles"
        :disabled="!canUpload"
        :class="['btn-upload', { 'btn-upload--disabled': !canUpload }]"
      >
        <Upload v-if="!uploading" class="btn-icon" />
        <Loader v-else class="btn-icon spinning" />
        {{ uploading ? `Subiendo... (${uploadProgress}%)` : 'Subir Documentos' }}
      </button>
      
      <div class="upload-info">
        <span class="info-text">
          Se subirán {{ validFiles.length }} de {{ files.length }} archivos
        </span>
      </div>
    </div>

    <!-- Documentos ya subidos -->
    <div v-if="documentosExistentes.length > 0" class="existing-docs">
      <h4 class="existing-title">
        <Archive class="existing-icon" />
        Documentos Adjuntos ({{ documentosExistentes.length }})
      </h4>
      
      <div class="existing-grid">
        <div 
          v-for="doc in documentosExistentes" 
          :key="doc.idDocumento"
          class="existing-item"
        >
          <div class="existing-icon">
            <FileText v-if="doc.isPDF" class="doc-type-icon pdf" />
            <Image v-else-if="doc.isImage" class="doc-type-icon image" />
            <FileText v-else class="doc-type-icon document" />
          </div>
          
          <div class="existing-info">
            <div class="existing-name" :title="doc.nombreOriginal">
              {{ doc.nombreOriginal }}
            </div>
            <div class="existing-details">
              <span class="existing-size">{{ doc.tamañoFormatted }}</span>
              <span class="existing-date">{{ formatDate(doc.fechaSubida) }}</span>
            </div>
            <div v-if="doc.descripcion" class="existing-description">
              {{ doc.descripcion }}
            </div>
          </div>
          
          <div class="existing-actions">
            <button 
              @click="downloadDocument(doc)"
              class="btn-download"
              :title="'Descargar ' + doc.nombreOriginal"
            >
              <Download class="action-icon" />
            </button>
            <button 
              @click="deleteDocument(doc)"
              class="btn-delete"
              :title="'Eliminar ' + doc.nombreOriginal"
              :disabled="uploading"
            >
              <Trash2 class="action-icon" />
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Mensajes -->
    <div v-if="mensaje" :class="['mensaje', tipoMensaje]">
      <component :is="tipoMensaje === 'success' ? 'CheckCircle' : 'AlertTriangle'" class="mensaje-icon" />
      {{ mensaje }}
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { 
  FileText, Upload, X, AlertTriangle, CheckCircle, Loader, 
  MessageSquare, Archive, Image, Download, Trash2
} from 'lucide-vue-next'
import apiService from '../services/apiService.js'

// Props
const props = defineProps({
  reclamacionId: {
    type: Number,
    required: true
  },
  disabled: {
    type: Boolean,
    default: false
  }
})

// Emits
const emit = defineEmits(['documentoSubido', 'documentoEliminado', 'error'])

// Estado reactivo
const files = ref([])
const documentosExistentes = ref([])
const isDragging = ref(false)
const uploading = ref(false)
const descripcion = ref('')
const mensaje = ref('')
const tipoMensaje = ref('info')

// Referencias
const dropZone = ref(null)
const fileInput = ref(null)

// Configuración
const maxFiles = 5
const maxFileSize = '10 MB'
const maxFileSizeBytes = 10 * 1024 * 1024
const acceptedTypes = '.pdf,.doc,.docx,.jpg,.jpeg,.png'
const acceptedMimeTypes = [
    'application/pdf',
    'application/msword',
    'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
    'image/jpeg',
    'image/png'
]

// Computed
const isDisabled = computed(() => props.disabled || uploading.value)

const validFiles = computed(() => 
    files.value.filter(file => !file.error)
)

const canUpload = computed(() => 
    validFiles.value.length > 0 && !uploading.value
)

const uploadProgress = computed(() => {
    if (files.value.length === 0) return 0
    const total = files.value.reduce((sum, file) => sum + (file.progress || 0), 0)
    return Math.round(total / files.value.length)
})

// Watchers
watch(() => props.reclamacionId, (newId) => {
    if (newId) {
    cargarDocumentosExistentes()
    }
}, { immediate: true })

// Métodos principales
const openFileDialog = () => {
    if (!isDisabled.value) {
    fileInput.value?.click()
    }
}

const handleFileSelect = (event) => {
    const selectedFiles = Array.from(event.target.files)
    processFiles(selectedFiles)
    //Limpiar input para permitir seleccionar el mismo archivo de nuevo
    event.target.value = ''
}

const handleDrop = (event) => {
  isDragging.value = false
  if (isDisabled.value) return
  
  const droppedFiles = Array.from(event.dataTransfer.files)
  processFiles(droppedFiles)
}

const handleDragOver = () => {
  if (!isDisabled.value) {
    isDragging.value = true
  }
}

const handleDragEnter = () => {
  if (!isDisabled.value) {
    isDragging.value = true
  }
}

const handleDragLeave = (event) => {
  // Solo remover el estado si realmente salimos del drop zone
  if (!dropZone.value?.contains(event.relatedTarget)) {
    isDragging.value = false
  }
}

const processFiles = (selectedFiles) => {
  const totalFiles = files.value.length + selectedFiles.length + documentosExistentes.value.length
  
  if (totalFiles > maxFiles) {
    mostrarMensaje(`No puedes subir más de ${maxFiles} archivos en total`, 'error')
    return
  }
  
  selectedFiles.forEach(file => {
    const fileInfo = createFileInfo(file)
    validateFile(fileInfo)
    files.value.push(fileInfo)
  })
  
  limpiarMensaje()
}

const createFileInfo = (file) => {
  return {
    file: file,
    name: file.name,
    size: file.size,
    sizeFormatted: formatFileSize(file.size),
    type: file.type,
    isPDF: file.type === 'application/pdf',
    isImage: file.type.startsWith('image/'),
    isDocument: file.type.includes('word'),
    error: null,
    uploading: false,
    uploaded: false,
    progress: 0
  }
}

const validateFile = (fileInfo) => {
  // Validar tamaño
  if (fileInfo.size > maxFileSizeBytes) {
    fileInfo.error = `El archivo es demasiado grande (máximo ${maxFileSize})`
    return
  }
  
  // Validar tipo MIME
  if (!acceptedMimeTypes.includes(fileInfo.type)) {
    fileInfo.error = 'Tipo de archivo no permitido'
    return
  }
  
  // Validar nombre duplicado
  const nombreExistente = files.value.some(f => f !== fileInfo && f.name === fileInfo.name) ||
                          documentosExistentes.value.some(d => d.nombreOriginal === fileInfo.name)
  
  if (nombreExistente) {
    fileInfo.error = 'Ya existe un archivo con este nombre'
    return
  }
}

const removeFile = (index) => {
  files.value.splice(index, 1)
  limpiarMensaje()
}

const clearFiles = () => {
  files.value = []
  descripcion.value = ''
  limpiarMensaje()
}

const uploadFiles = async () => {
  if (!canUpload.value) return
  
  uploading.value = true
  limpiarMensaje()
  
  try {
    const filesToUpload = validFiles.value
    let uploadedCount = 0
    
    for (const fileInfo of filesToUpload) {
      fileInfo.uploading = true
      fileInfo.progress = 0
      
      try {
        // Simular progreso de subida
        const progressInterval = setInterval(() => {
          if (fileInfo.progress < 90) {
            fileInfo.progress += 10
          }
        }, 100)
        
        const formData = new FormData()
        formData.append('archivo', fileInfo.file)
        formData.append('reclamacionId', props.reclamacionId)
        if (descripcion.value.trim()) {
          formData.append('descripcion', descripcion.value.trim())
        }
        
        const response = await apiService.subirDocumento(formData)
        
        clearInterval(progressInterval)
        fileInfo.progress = 100
        fileInfo.uploading = false
        fileInfo.uploaded = true
        uploadedCount++
        
        emit('documentoSubido', response)
        
      } catch (error) {
        fileInfo.uploading = false
        fileInfo.error = error.message || 'Error al subir archivo'
        console.error('Error subiendo archivo:', error)
      }
    }
    
    if (uploadedCount > 0) {
      mostrarMensaje(`Se subieron ${uploadedCount} documentos exitosamente`, 'success')
      await cargarDocumentosExistentes()
      
      // Limpiar archivos subidos exitosamente
      files.value = files.value.filter(f => !f.uploaded)
      if (files.value.length === 0) {
        descripcion.value = ''
      }
    }
    
  } catch (error) {
    console.error('Error general en subida:', error)
    mostrarMensaje('Error al subir documentos', 'error')
    emit('error', error)
  } finally {
    uploading.value = false
  }
}

const cargarDocumentosExistentes = async () => {
  if (!props.reclamacionId) return
  
  try {
    const response = await apiService.getDocumentosPorReclamacion(props.reclamacionId)
    documentosExistentes.value = response.data || []
  } catch (error) {
    console.error('Error cargando documentos existentes:', error)
  }
}

const downloadDocument = async (documento) => {
  try {
    const url = `/api/documentos/${documento.idDocumento}/descargar`
    window.open(url, '_blank')
  } catch (error) {
    console.error('Error descargando documento:', error)
    mostrarMensaje('Error al descargar documento', 'error')
  }
}

const deleteDocument = async (documento) => {
  if (!confirm(`¿Estás seguro de eliminar "${documento.nombreOriginal}"?`)) return
  
  try {
    await apiService.eliminarDocumento(documento.idDocumento)
    mostrarMensaje('Documento eliminado exitosamente', 'success')
    
    emit('documentoEliminado', documento)
    await cargarDocumentosExistentes()
    
  } catch (error) {
    console.error('Error eliminando documento:', error)
    mostrarMensaje('Error al eliminar documento', 'error')
    emit('error', error)
  }
}

// Métodos de utilidad
const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const formatDate = (dateString) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('es-ES', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const mostrarMensaje = (texto, tipo = 'info') => {
  mensaje.value = texto
  tipoMensaje.value = tipo
  
  if (tipo === 'success' || tipo === 'info') {
    setTimeout(limpiarMensaje, 5000)
  }
}

const limpiarMensaje = () => {
  mensaje.value = ''
  tipoMensaje.value = 'info'
}

// Lifecycle
onMounted(() => {
  cargarDocumentosExistentes()
})
</script>

<style scoped>
.carga-documentos {
  background: var(--card-bg);
  border-radius: 1rem;
  border: 1px solid var(--border-color);
  padding: 1.5rem;
  margin-bottom: 1.5rem;
}

/* Header */
.upload-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1.5rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid var(--border-color);
}

.header-info {
  display: flex;
  align-items: flex-start;
  gap: 0.75rem;
}

.header-icon {
  width: 1.5rem;
  height: 1.5rem;
  color: var(--primary-color);
  margin-top: 0.25rem;
}

.header-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0;
  color: var(--text-primary);
}

.header-subtitle {
  font-size: 0.9rem;
  color: var(--text-secondary);
  margin: 0.25rem 0 0 0;
}

.upload-limits {
  display: flex;
  gap: 1rem;
}

.limit-item {
  font-size: 0.85rem;
}

.limit-label {
  color: var(--text-secondary);
}

.limit-value {
  color: var(--text-primary);
  font-weight: 500;
  margin-left: 0.25rem;
}

/* Drop Zone */
.drop-zone {
  border: 2px dashed var(--border-color);
  border-radius: 0.75rem;
  padding: 2rem;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: var(--section-bg);
}

.drop-zone:hover:not(.drop-zone--disabled) {
  border-color: var(--primary-color);
  background: var(--primary-light);
}

.drop-zone--dragover {
  border-color: var(--primary-color);
  background: var(--primary-light);
  transform: scale(1.02);
}

.drop-zone--disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.drop-zone-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.75rem;
}

.drop-zone-icon {
  width: 2.5rem;
  height: 2.5rem;
  color: var(--primary-color);
}

.drop-zone-title {
  font-size: 1.1rem;
  font-weight: 500;
  margin: 0;
  color: var(--text-primary);
}

.drop-zone-subtitle {
  font-size: 0.9rem;
  color: var(--text-secondary);
  margin: 0;
}

/* Files List */
.files-list {
  margin-top: 1.5rem;
}

.files-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.files-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1rem;
  font-weight: 600;
  margin: 0;
  color: var(--text-primary);
}

.files-icon {
  width: 1.25rem;
  height: 1.25rem;
  color: var(--primary-color);
}

.btn-clear {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  border: 1px solid var(--border-color);
  border-radius: 0.5rem;
  background: var(--card-bg);
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-clear:hover:not(:disabled) {
  background: var(--border-color);
}

.btn-clear:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-icon {
  width: 1rem;
  height: 1rem;
}

.files-grid {
  display: grid;
  gap: 0.75rem;
}

.file-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  border: 1px solid var(--border-color);
  border-radius: 0.5rem;
  background: var(--section-bg);
  transition: all 0.2s ease;
}

.file-item--error {
  border-color: var(--error-color);
  background: var(--error-bg);
}

.file-item--uploading {
  border-color: var(--primary-color);
  background: var(--primary-light);
}

.file-icon {
  flex-shrink: 0;
}

.file-type-icon {
  width: 2rem;
  height: 2rem;
}

.file-type-icon.pdf {
  color: #e74c3c;
}

.file-type-icon.image {
  color: #27ae60;
}

.file-type-icon.document {
  color: #3498db;
}

.file-info {
  flex: 1;
  min-width: 0;
}

.file-name {
  font-weight: 500;
  color: var(--text-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.file-details {
  font-size: 0.85rem;
  color: var(--text-secondary);
  margin-top: 0.25rem;
  display: flex;
  gap: 1rem;
}

.file-error {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.85rem;
  color: var(--error-color);
  margin-top: 0.5rem;
}

.error-icon {
  width: 1rem;
  height: 1rem;
}

.file-progress {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  min-width: 120px;
}

.progress-bar {
  height: 4px;
  background: var(--border-color);
  border-radius: 2px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: var(--primary-color);
  transition: width 0.3s ease;
}

.progress-text {
  font-size: 0.8rem;
  color: var(--text-secondary);
  text-align: center;
}

.file-actions {
  flex-shrink: 0;
}

.btn-remove {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 2rem;
  height: 2rem;
  border: none;
  border-radius: 0.25rem;
  background: var(--error-bg);
  color: var(--error-color);
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-remove:hover {
  background: var(--error-color);
  color: white;
}

.action-icon {
  width: 1rem;
  height: 1rem;
}

.status-icon {
  width: 1.5rem;
  height: 1.5rem;
}

.status-icon.success {
  color: var(--success-color);
}

.status-icon.loading {
  color: var(--primary-color);
  animation: spin 1s linear infinite;
}

.spinning {
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

/* Description */
.description-section {
  margin-top: 1.5rem;
}

.description-label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: 0.5rem;
}

.label-icon {
  width: 1rem;
  height: 1rem;
  color: var(--primary-color);
}

.description-input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: 0.5rem;
  background: var(--input-bg);
  color: var(--text-primary);
  font-family: inherit;
  font-size: 0.9rem;
  resize: vertical;
  transition: border-color 0.2s ease;
}

.description-input:focus {
  outline: none;
  border-color: var(--primary-color);
}

.description-input:disabled {
  opacity: 0.6;
  background: var(--border-color);
}

/* Actions */
.actions-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 1.5rem;
  padding-top: 1rem;
  border-top: 1px solid var(--border-color);
}

.btn-upload {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 0.5rem;
  background: var(--primary-color);
  color: white;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-upload:hover:not(.btn-upload--disabled) {
  background: var(--primary-dark);
  transform: translateY(-1px);
}

.btn-upload--disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.upload-info {
  font-size: 0.9rem;
  color: var(--text-secondary);
}

/* Existing Documents */
.existing-docs {
  margin-top: 2rem;
  padding-top: 1.5rem;
  border-top: 1px solid var(--border-color);
}

.existing-title {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1rem;
  font-weight: 600;
  margin: 0 0 1rem 0;
  color: var(--text-primary);
}

.existing-icon {
  width: 1.25rem;
  height: 1.25rem;
  color: var(--primary-color);
}

.existing-grid {
  display: grid;
  gap: 0.75rem;
}

.existing-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  border: 1px solid var(--border-color);
  border-radius: 0.5rem;
  background: var(--section-bg);
}

.doc-type-icon {
  width: 2rem;
  height: 2rem;
}

.doc-type-icon.pdf {
  color: #e74c3c;
}

.doc-type-icon.image {
  color: #27ae60;
}

.doc-type-icon.document {
  color: #3498db;
}

.existing-info {
  flex: 1;
  min-width: 0;
}

.existing-name {
  font-weight: 500;
  color: var(--text-primary);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.existing-details {
  font-size: 0.85rem;
  color: var(--text-secondary);
  margin-top: 0.25rem;
  display: flex;
  gap: 1rem;
}

.existing-description {
  font-size: 0.85rem;
  color: var(--text-secondary);
  margin-top: 0.5rem;
  font-style: italic;
}

.existing-actions {
  display: flex;
  gap: 0.5rem;
}

.btn-download,
.btn-delete {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 2rem;
  height: 2rem;
  border: none;
  border-radius: 0.25rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-download {
  background: var(--info-bg);
  color: var(--info-color);
}

.btn-download:hover {
  background: var(--info-color);
  color: white;
}

.btn-delete {
  background: var(--error-bg);
  color: var(--error-color);
}

.btn-delete:hover:not(:disabled) {
  background: var(--error-color);
  color: white;
}

.btn-delete:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Messages */
.mensaje {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem;
  border-radius: 0.5rem;
  margin-top: 1rem;
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

/* Responsive */
@media (max-width: 768px) {
  .upload-header {
    flex-direction: column;
    gap: 1rem;
  }
  
  .upload-limits {
    align-self: stretch;
    justify-content: space-between;
  }
  
  .files-header {
    flex-direction: column;
    gap: 0.75rem;
  }
  
  .file-item,
  .existing-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.75rem;
  }
  
  .file-actions,
  .existing-actions {
    align-self: stretch;
    justify-content: center;
  }
  
  .actions-section {
    flex-direction: column;
    gap: 1rem;
  }
}
</style>
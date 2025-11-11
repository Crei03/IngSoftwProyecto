const API_BASE_URL = 'http://localhost:8080/api'

class ApiService {
  async request(endpoint, options = {}) {
    const url = `${API_BASE_URL}${endpoint}`
    const config = {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers
      },
      ...options
    }

    console.log('🔄 API Request:', {
      url: url,
      method: config.method || 'GET',
      headers: config.headers
    })

    try {
      const response = await fetch(url, config)
      console.log('📡 Response status:', response.status, response.statusText)

      const data = await response.json()

      if (!response.ok) {
        throw new Error(data.message || `Error ${response.status}: ${response.statusText}`)
      }

      return data
    } catch (error) {
      console.error('❌ Error en API request:', error)
      console.error('🔍 URL que falló:', url)
      console.error('⚙️ Config:', config)
      throw error
    }
  }

  // Métodos para Clientes
  async getClientes() {
    const response = await this.request('/clientes')
    return response.data || []
  }

  async crearCliente(clienteData) {
    const response = await this.request('/clientes', {
      method: 'POST',
      body: JSON.stringify(clienteData)
    })
    return response.data
  }
  
  async loginCliente({ email, password }) {
    const response = await this.request('/auth/login', {
      method: 'POST',
      body: JSON.stringify({
        email,
        password,
        tipoUsuario: 'cliente'
      })
    })
    return response.data
  }

  // Métodos para Agentes
  async getAgentes() {
    const response = await this.request('/agentes')
    return response.data || []
  }

  async getAgentesActivos() {
    const response = await this.request('/agentes/activos')
    return response.data || []
  }

  async crearAgente(agenteData) {
    const response = await this.request('/agentes', {
      method: 'POST',
      body: JSON.stringify(agenteData)
    })
    return response.data
  }
  
  async loginAgente({ email, password }) {
    const response = await this.request('/auth/login', {
      method: 'POST',
      body: JSON.stringify({
        email,
        password,
        tipoUsuario: 'agente'
      })
    })
    return response.data
  }
  
  async solicitarRecuperacionPassword(email) {
    return this.request('/password/recuperar', {
      method: 'POST',
      body: JSON.stringify({ email })
    })
  }
  
  async restablecerPassword({ token, password }) {
    return this.request('/password/restablecer', {
      method: 'POST',
      body: JSON.stringify({ token, password })
    })
  }

  // Métodos para Evaluadores
  async getEvaluadores() {
    const response = await this.request('/evaluadores')
    return response.data || []
  }

  async getEvaluadoresActivos() {
    const response = await this.request('/evaluadores/activos')
    return response.data || []
  }

  async crearEvaluador(evaluadorData) {
    const response = await this.request('/evaluadores', {
      method: 'POST',
      body: JSON.stringify(evaluadorData)
    })
    return response.data
  }

  // Métodos para Pólizas
  async getPolizas() {
    const response = await this.request('/polizas')
    return response.data || []
  }

  async crearPoliza(polizaData) {
    const response = await this.request('/polizas', {
      method: 'POST',
      body: JSON.stringify(polizaData)
    })
    return response.data
  }

  async getPolizasPorCliente(clienteId) {
    const response = await this.request(`/polizas/cliente/${clienteId}`)
    return response.data || []
  }

  async actualizarPoliza(polizaId, polizaData) {
    const response = await this.request(`/polizas/${polizaId}`, {
      method: 'PUT',
      body: JSON.stringify(polizaData)
    })
    return response.data
  }

  async actualizarEstadoPoliza(id, estado) {
    console.log('apiService.actualizarEstadoPoliza - ID recibido:', id, 'Tipo:', typeof id)
    console.log('apiService.actualizarEstadoPoliza - Estado recibido:', estado)

    let endpoint
    let method = 'PUT'

    if (estado === 'APROBADA') {
      endpoint = `/polizas/${id}/aprobar`
    } else if (estado === 'RECHAZADA') {
      endpoint = `/polizas/${id}/rechazar`
    } else {
      throw new Error('Estado no válido para actualización')
    }

    console.log('apiService.actualizarEstadoPoliza - Endpoint:', endpoint)

    const response = await this.request(endpoint, {
      method: method
    })
    return response.data
  }

  async rechazarPoliza(id, motivo) {
    let endpoint = `/polizas/${id}/rechazar`
    let method = 'PUT'

    const params = new URLSearchParams()
    if (motivo && motivo.trim()) {
      params.append('motivo', motivo.trim())
    }

    if (params.toString()) {
      endpoint += '?' + params.toString()
    }

    const response = await this.request(endpoint, {
      method: method
    })
    return response.data
  }

  async getPolizasPorEstado(estado) {
    const response = await this.request(`/polizas/estado/${estado}`)
    return response.data || []
  }

  // Métodos para Reclamaciones
  async getReclamaciones() {
    console.log('API: Obteniendo todas las reclamaciones...');
    const response = await this.request('/reclamaciones/todas')
    console.log('API: Respuesta recibida:', response);
    const reclamaciones = response.reclamaciones || response.data || [];
    console.log(`API: ${reclamaciones.length} reclamaciones encontradas`);
    return reclamaciones;
  }

  async crearReclamacion(reclamacionData) {
    const response = await this.request('/reclamaciones', {
      method: 'POST',
      body: JSON.stringify(reclamacionData)
    })
    return response.data
  }

  async getReclamacionesPorEstado(estado) {
    const response = await this.request(`/reclamaciones/estado/${estado}`)
    return response.reclamaciones || []
  }

  async getReclamacionesPorPoliza(polizaId) {
    const response = await this.request(`/reclamaciones/poliza/${polizaId}`)
    return response.reclamaciones || []
  }

  async getReclamacionPorId(id) {
    const response = await this.request(`/reclamaciones/${id}`)
    return response.data
  }

  // Helper method to get the first active evaluator
  async obtenerPrimerEvaluadorActivo() {
    console.log('API: Obteniendo primer evaluador activo...');
    try {
      const evaluadores = await this.getEvaluadoresActivos();
      if (evaluadores.length === 0) {
        // Intentar obtener todos los evaluadores (activos e inactivos)
        const todosEvaluadores = await this.getEvaluadores();
        if (todosEvaluadores.length === 0) {
          throw new Error('No hay evaluadores en el sistema. Por favor, cree al menos un evaluador primero.');
        } else {
          throw new Error('No hay evaluadores activos. Por favor, active al menos un evaluador.');
        }
      }
      const primerEvaluador = evaluadores[0];
      console.log(`API: Usando evaluador ${primerEvaluador.idEvaluador || primerEvaluador.id} - ${primerEvaluador.nombre}`);
      return primerEvaluador.idEvaluador || primerEvaluador.id;
    } catch (error) {
      console.error('Error obteniendo evaluador activo:', error);
      throw new Error('No se pudo obtener un evaluador activo: ' + error.message);
    }
  }

  // Aprobar reclamación (con evaluador automático)
  async aprobarReclamacion(id, evaluadorId = null) {
    console.log(`API: Aprobando reclamación ${id}${evaluadorId ? ` con evaluador ${evaluadorId}` : ' (evaluador automático)'}`);

    // Validar que el ID sea válido
    if (!id || id === 'undefined' || isNaN(id)) {
      throw new Error(`ID de reclamación inválido: ${id}`);
    }

    // Si no se proporciona evaluadorId, obtener uno automáticamente
    if (!evaluadorId) {
      evaluadorId = await this.obtenerPrimerEvaluadorActivo();
    }

    // Para aprobar, el evaluadorId va como query parameter, no en el body
    const response = await this.request(`/reclamaciones/${id}/aprobar?evaluadorId=${evaluadorId}`, {
      method: 'POST'
    })
    return response
  }

  // Métodos para Documentos
  async subirDocumento(formData) {
    const url = `${API_BASE_URL}/documentos/subir`
    
    console.log('🔄 Subiendo documento:', {
      url: url,
      files: formData.get('archivo')?.name || 'No file'
    })

    try {
      const response = await fetch(url, {
        method: 'POST',
        body: formData
        // No incluir Content-Type header para FormData
      })

      console.log('📡 Upload response status:', response.status, response.statusText)
      
      const data = await response.json()
      console.log('📥 Upload response data:', data)
      
      if (!response.ok) {
        throw new Error(data.message || `Error ${response.status}: ${response.statusText}`)
      }
      
      return data
    } catch (error) {
      console.error('❌ Error subiendo documento:', error)
      throw error
    }
  }

  async getDocumentosPorReclamacion(reclamacionId) {
    try {
      const response = await this.request(`/documentos/reclamacion/${reclamacionId}`)
      
      // Procesar datos para agregar propiedades computadas
      const documentos = response.data || []
      return {
        ...response,
        data: documentos.map(doc => this.procesarDocumento(doc))
      }
    } catch (error) {
      console.error('Error obteniendo documentos:', error)
      // Si no hay documentos o error 404, devolver array vacío
      if (error.message.includes('404')) {
        return { data: [] }
      }
      throw error
    }
  }

  async descargarDocumento(idDocumento) {
    const url = `${API_BASE_URL}/documentos/${idDocumento}/descargar`
    
    try {
      const response = await fetch(url, {
        method: 'GET',
        headers: {
          // No incluir Content-Type para descarga
        }
      })

      if (!response.ok) {
        const errorData = await response.json()
        throw new Error(errorData.message || `Error ${response.status}: ${response.statusText}`)
      }

      return response // Devolver la respuesta para manejar el blob
    } catch (error) {
      console.error('❌ Error descargando documento:', error)
      throw error
    }
  }

  async eliminarDocumento(idDocumento) {
    const response = await this.request(`/documentos/${idDocumento}`, {
      method: 'DELETE'
    })
    return response
  }

  // Rechazar reclamación (con evaluador automático)
  async rechazarReclamacion(id, motivo = 'Rechazada mediante acción rápida', evaluadorId = null) {
    console.log(`API: Rechazando reclamación ${id} con motivo: ${motivo}`);

    // Validar que el ID sea válido
    if (!id || id === 'undefined' || isNaN(id)) {
      throw new Error(`ID de reclamación inválido: ${id}`);
    }

    // Si no se proporciona evaluadorId, obtener uno automáticamente
    if (!evaluadorId) {
      evaluadorId = await this.obtenerPrimerEvaluadorActivo();
    }

    const response = await this.request(`/reclamaciones/${id}/rechazar`, {
      method: 'POST',
      body: JSON.stringify({ motivo, evaluadorId })
    })
    return response
  }

  // Evaluar reclamación (con evaluador automático)
  async evaluarReclamacion(id, datosEvaluacion = {}) {
    console.log(`API: Iniciando evaluación de reclamación ${id}`);

    // Validar que el ID sea válido
    if (!id || id === 'undefined' || isNaN(id)) {
      throw new Error(`ID de reclamación inválido: ${id}`);
    }

    // Si no hay evaluadorId en datosEvaluacion, obtener uno automáticamente
    if (!datosEvaluacion.evaluadorId) {
      datosEvaluacion.evaluadorId = await this.obtenerPrimerEvaluadorActivo();
    }

    const response = await this.request(`/reclamaciones/${id}/evaluar`, {
      method: 'POST',
      body: JSON.stringify(datosEvaluacion)
    })
    return response
  }

  async getDocumentoPorId(idDocumento) {
    const response = await this.request(`/documentos/${idDocumento}`)
    return response.data ? this.procesarDocumento(response.data) : null
  }

  // Método auxiliar para procesar datos de documento
  procesarDocumento(doc) {
    return {
      ...doc,
      // Propiedades computadas para el componente
      isPDF: doc.tipoContenido === 'application/pdf',
      isImage: doc.tipoContenido?.startsWith('image/'),
      isDocument: doc.tipoContenido?.includes('word') || doc.tipoContenido?.includes('document'),
      tamañoFormatted: this.formatFileSize(doc.tamaño || 0),
      fechaSubidaFormatted: this.formatDate(doc.fechaSubida)
    }
  }

  // Utilidades para formato
  formatFileSize(bytes) {
    if (bytes === 0) return '0 B'
    const k = 1024
    const sizes = ['B', 'KB', 'MB', 'GB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
  }

  formatDate(dateString) {
    if (!dateString) return ''
    const date = new Date(dateString)
    return date.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    })
  }
}

export default new ApiService() 

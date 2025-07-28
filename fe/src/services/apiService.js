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

    try {
      const response = await fetch(url, config)
      const data = await response.json()
      
      if (!response.ok) {
        throw new Error(data.message || `Error ${response.status}: ${response.statusText}`)
      }
      
      return data
    } catch (error) {
      console.error('Error en API request:', error)
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

  // Métodos para Agentes
  async getAgentes() {
    const response = await this.request('/agentes')
    return response.data || []
  }

  async getAgentesActivos() {
    const response = await this.request('/agentes/activos')
    return response.data || []
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
    const response = await this.request('/reclamaciones/todas')
    return response.reclamaciones || []
  }

  async crearReclamacion(reclamacionData) {
    const response = await this.request('/reclamaciones/registrar', {
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

  async evaluarReclamacion(reclamacionId, evaluacionData) {
    const response = await this.request(`/reclamaciones/${reclamacionId}/evaluar`, {
      method: 'POST',
      body: JSON.stringify(evaluacionData)
    })
    return response.data
  }

  async aprobarReclamacion(reclamacionId, evaluador = '10') {
    const response = await this.request(`/reclamaciones/${reclamacionId}/aprobar?evaluador=${encodeURIComponent(evaluador)}`, {
      method: 'POST'
    })
    return response.data
  }

  async rechazarReclamacion(reclamacionId, motivo, evaluador = '10') {
    const response = await this.request(`/reclamaciones/${reclamacionId}/rechazar`, {
      method: 'POST',
      body: JSON.stringify({ motivo, evaluador })
    })
    return response.data
  }
}

export default new ApiService() 
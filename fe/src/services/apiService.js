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

  async actualizarEstadoPoliza(id, estado) {
    // Por ahora, simulamos la actualización del estado
    // En una implementación real, esto sería una llamada al backend
    console.log(`Actualizando póliza ${id} a estado ${estado}`)
    return { success: true, estado }
  }

  async getPolizasPorEstado(estado) {
    const response = await this.request(`/polizas/estado/${estado}`)
    return response.data || []
  }
}

export default new ApiService() 
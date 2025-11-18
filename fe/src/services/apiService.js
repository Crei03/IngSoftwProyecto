// Servicio centralizado para llamadas API

const API_BASE_URL = 'http://localhost:8080/api';

const apiService = {
  // Autenticación
  async login(email, password) {
    return fetch(`${API_BASE_URL}/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, password })
    }).then(res => res.json());
  },

  async logout() {
    return fetch(`${API_BASE_URL}/auth/logout`, {
      method: 'POST'
    }).then(res => res.json());
  },

  // Recuperación de contraseña
  async solicitarRecuperacion(email) {
    return fetch(`${API_BASE_URL}/password/recovery`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email })
    }).then(res => res.json());
  },

  // Clientes
  async getClientes() {
    try {
      const response = await fetch(`${API_BASE_URL}/clientes`);
      return await response.json();
    } catch (error) {
      console.error('Error fetching clientes:', error);
      return { data: [] };
    }
  },

  async crearCliente(cliente) {
    return fetch(`${API_BASE_URL}/clientes`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(cliente)
    }).then(res => res.json());
  },

  async obtenerClientePorId(id) {
    return fetch(`${API_BASE_URL}/clientes/${id}`).then(res => res.json());
  },

  // Agentes
  async getAgentes() {
    try {
      const response = await fetch(`${API_BASE_URL}/agentes`);
      return await response.json();
    } catch (error) {
      console.error('Error fetching agentes:', error);
      return { data: [] };
    }
  },

  async getAgentesActivos() {
    try {
      const response = await fetch(`${API_BASE_URL}/agentes/activos`);
      return await response.json();
    } catch (error) {
      console.error('Error fetching agentes activos:', error);
      return { data: [] };
    }
  },

  async crearAgente(agente) {
    return fetch(`${API_BASE_URL}/agentes`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(agente)
    }).then(res => res.json());
  },

  // Pólizas
  async getPolizas() {
    try {
      const response = await fetch(`${API_BASE_URL}/polizas`);
      return await response.json();
    } catch (error) {
      console.error('Error fetching polizas:', error);
      return { data: [] };
    }
  },

  async crearPoliza(poliza) {
    return fetch(`${API_BASE_URL}/polizas`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(poliza)
    }).then(res => res.json());
  },

  async actualizarEstadoPoliza(id, estado) {
    return fetch(`${API_BASE_URL}/polizas/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ estado })
    }).then(res => res.json());
  },

  async obtenerPolizasPorEstado(estado) {
    try {
      const response = await fetch(`${API_BASE_URL}/polizas/estado/${estado}`);
      return await response.json();
    } catch (error) {
      console.error('Error fetching polizas por estado:', error);
      return { data: [] };
    }
  },

  // Reclamaciones
  async crearReclamacion(reclamacion) {
    return fetch(`${API_BASE_URL}/reclamaciones/registrar`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(reclamacion)
    }).then(res => res.json());
  },

  async obtenerReclamacionesPorPoliza(polizaId) {
    try {
      const response = await fetch(`${API_BASE_URL}/reclamaciones/poliza/${polizaId}`);
      return await response.json();
    } catch (error) {
      console.error('Error fetching reclamaciones:', error);
      return { data: [] };
    }
  }
};

export default apiService;

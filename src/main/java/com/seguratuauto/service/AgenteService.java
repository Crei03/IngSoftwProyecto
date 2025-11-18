package com.seguratuauto.service;

import com.seguratuauto.api.dto.LoginResponse;
import java.util.List;
import com.seguratuauto.model.Agente;

public interface AgenteService {
    Agente crearAgente(Agente agente);
    Agente obtenerAgentePorId(Long id);
    List<Agente> obtenerTodosLosAgentes();
    List<Agente> obtenerAgentesActivos();
    Agente actualizarAgente(Long id, Agente agente);
    void eliminarAgente(Long id);
    Agente obtenerAgentePorCorreo(String correo);
}

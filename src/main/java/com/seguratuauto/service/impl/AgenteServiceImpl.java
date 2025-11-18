package com.seguratuauto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seguratuauto.service.AgenteService;
import com.seguratuauto.model.Agente;
import com.seguratuauto.dao.ClienteRepository;
import java.util.List;
import java.util.Optional;

@Service
public class AgenteServiceImpl implements AgenteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Agente crearAgente(Agente agente) {
        if (agente == null) {
            throw new IllegalArgumentException("El agente no puede ser nulo");
        }
        return agente;
    }

    @Override
    public Agente obtenerAgentePorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser válido");
        }
        return null;
    }

    @Override
    public List<Agente> obtenerTodosLosAgentes() {
        return List.of();
    }

    @Override
    public List<Agente> obtenerAgentesActivos() {
        return List.of();
    }

    @Override
    public Agente actualizarAgente(Long id, Agente agente) {
        if (id == null || agente == null) {
            throw new IllegalArgumentException("Los parámetros no pueden ser nulos");
        }
        return agente;
    }

    @Override
    public void eliminarAgente(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser válido");
        }
    }

    @Override
    public Agente obtenerAgentePorCorreo(String correo) {
        if (correo == null || correo.isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío");
        }
        return null;
    }
}

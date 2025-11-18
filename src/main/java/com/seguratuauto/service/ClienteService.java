package com.seguratuauto.service;

import com.seguratuauto.api.dto.LoginResponse;
import java.util.List;
import com.seguratuauto.model.Cliente;

public interface ClienteService {
    Cliente crearCliente(Cliente cliente);
    Cliente obtenerClientePorId(Long id);
    List<Cliente> obtenerTodosLosClientes();
    Cliente actualizarCliente(Long id, Cliente cliente);
    void eliminarCliente(Long id);
    Cliente obtenerClientePorCorreo(String correo);
    LoginResponse autenticar(String email, String password);
}

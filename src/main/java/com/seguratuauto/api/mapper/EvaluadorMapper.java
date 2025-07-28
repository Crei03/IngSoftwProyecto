package com.seguratuauto.api.mapper;

import com.seguratuauto.api.dto.EvaluadorRequest;
import com.seguratuauto.api.dto.EvaluadorResponse;
import com.seguratuauto.model.Evaluador;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

/**
 * Mapper para convertir entre entidades Evaluador y DTOs
 */
@Component
public class EvaluadorMapper {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Convierte una entidad Evaluador a EvaluadorResponse
     * @param evaluador La entidad evaluador
     * @return EvaluadorResponse con los datos del evaluador
     */
    public EvaluadorResponse toResponse(Evaluador evaluador) {
        if (evaluador == null) {
            return null;
        }
        
        String fechaIngresoStr = evaluador.getFechaIngreso() != null 
                ? evaluador.getFechaIngreso().format(DATE_FORMATTER) 
                : null;
        
        return new EvaluadorResponse(
            evaluador.getIdEvaluador() != null ? evaluador.getIdEvaluador().toString() : null,
            evaluador.getNombre(),
            evaluador.getCodigo(),
            evaluador.getEmail(),
            evaluador.getTelefono(),
            evaluador.getEspecialidad(),
            evaluador.getActivo() != null ? evaluador.getActivo().toString() : "true",
            fechaIngresoStr
        );
    }
    
    /**
     * Convierte un EvaluadorRequest a entidad Evaluador
     * @param request El DTO de request
     * @return Evaluador con los datos del request
     */
    public Evaluador toEntity(EvaluadorRequest request) {
        if (request == null) {
            return null;
        }
        
        Evaluador evaluador = new Evaluador();
        evaluador.setNombre(request.getNombre());
        evaluador.setCodigo(request.getCodigo());
        evaluador.setEmail(request.getEmail());
        evaluador.setTelefono(request.getTelefono());
        evaluador.setEspecialidad(request.getEspecialidad());
        evaluador.setActivo(request.getActivo() != null ? request.getActivo() : true);
        
        return evaluador;
    }
    
    /**
     * Actualiza una entidad Evaluador existente con datos de EvaluadorRequest
     * @param evaluadorExistente El evaluador existente a actualizar
     * @param request El DTO con los nuevos datos
     * @return El evaluador actualizado
     */
    public Evaluador updateEntity(Evaluador evaluadorExistente, EvaluadorRequest request) {
        if (evaluadorExistente == null || request == null) {
            return evaluadorExistente;
        }
        
        // Actualizar solo los campos que vienen en el request
        if (request.getNombre() != null) {
            evaluadorExistente.setNombre(request.getNombre());
        }
        
        if (request.getCodigo() != null) {
            evaluadorExistente.setCodigo(request.getCodigo());
        }
        
        if (request.getEmail() != null) {
            evaluadorExistente.setEmail(request.getEmail());
        }
        
        if (request.getTelefono() != null) {
            evaluadorExistente.setTelefono(request.getTelefono());
        }
        
        if (request.getEspecialidad() != null) {
            evaluadorExistente.setEspecialidad(request.getEspecialidad());
        }
        
        if (request.getActivo() != null) {
            evaluadorExistente.setActivo(request.getActivo());
        }
        
        return evaluadorExistente;
    }
    
    /**
     * Convierte una entidad Evaluador a EvaluadorRequest (útil para edición)
     * @param evaluador La entidad evaluador
     * @return EvaluadorRequest con los datos del evaluador
     */
    public EvaluadorRequest toRequest(Evaluador evaluador) {
        if (evaluador == null) {
            return null;
        }
        
        return new EvaluadorRequest(
            evaluador.getNombre(),
            evaluador.getCodigo(),
            evaluador.getEmail(),
            evaluador.getTelefono(),
            evaluador.getEspecialidad(),
            evaluador.getActivo()
        );
    }
}

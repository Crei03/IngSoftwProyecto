package com.seguratuauto.api.mapper;

import org.springframework.stereotype.Component;
import com.seguratuauto.model.Poliza;

@Component
public class PolizaMapper {
    
    public Poliza toEntity(Poliza poliza) {
        if (poliza == null) {
            return null;
        }
        return poliza;
    }

    public Poliza updateEntity(Poliza request, Poliza poliza) {
        if (request == null) {
            return poliza;
        }
        poliza.setNumeroPoliza(request.getNumeroPoliza());
        poliza.setEstado(request.getEstado());
        poliza.setFechaInicio(request.getFechaInicio());
        poliza.setFechaVencimiento(request.getFechaVencimiento());
        poliza.setPrecio(request.getPrecio());
        return poliza;
    }
}

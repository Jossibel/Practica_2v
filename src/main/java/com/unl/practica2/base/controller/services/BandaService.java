package com.unl.practica2.base.controller.services;

import java.sql.Date;
import java.util.HashMap;

import com.unl.practica2.base.controller.DataStruct.list.LinkedList;
import com.unl.practica2.base.controller.dao.dao_models.DaoBanda;
import com.unl.practica2.base.models.Banda; // Asumiendo que esta es tu implementación personalizada
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import jakarta.validation.constraints.NotEmpty;

@BrowserCallable
@AnonymousAllowed
public class BandaService {

    private DaoBanda db;

    public BandaService() {
        db = new DaoBanda();
    }

    public void create(@NotEmpty String nombre, @NotEmpty String fecha) throws Exception {
        if (nombre.trim().length() > 0 && fecha.trim().length() > 0) {
            db.getObj().setNombre(nombre);
            db.getObj().setFecha(Date.valueOf(fecha)); // formato: yyyy-MM-dd
            if (!db.save()) {
                throw new Exception("No se pudo guardar los datos de la banda");
            }
        }
    }

    public void update(Integer id, @NotEmpty String nombre, @NotEmpty String fecha) throws Exception {
        if (nombre.trim().length() > 0 && fecha.trim().length() > 0) {
            db.setObj(db.listAll().get(id - 1)); // suponiendo posición = id - 1
            db.getObj().setNombre(nombre);
            db.getObj().setFecha(Date.valueOf(fecha));
            if (!db.update(id - 1)) {
                throw new Exception("No se pudo actualizar los datos de la banda");
            }
        }
    }

    public LinkedList<HashMap<String, String>> listBandas() {
        LinkedList<HashMap<String, String>> lista = new LinkedList<>();
        if (!db.listAll().isEmpty()) {
            Banda[] arreglo = db.listAll().toArray();
            for (int i = 0; i < db.listAll().getLength(); i++) {
                HashMap<String, String> aux = new HashMap<>();
                Banda b = arreglo[i];
                aux.put("id", String.valueOf(b.getId()));
                aux.put("nombre", b.getNombre());
                aux.put("fecha", b.getFecha().toString());
                lista.add(aux);
            }
        }
        return lista;
    }
}

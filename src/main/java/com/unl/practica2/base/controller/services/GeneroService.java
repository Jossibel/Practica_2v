package com.unl.practica2.base.controller.services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.unl.practica2.base.controller.dao.dao_models.DaoGenero;
import com.unl.practica2.base.models.Genero;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.NotEmpty;

public class GeneroService{
    private DaoGenero db;

    public GeneroService(){
        db = new DaoGenero();
    }

    public void createBanda(@NotEmpty String nombre, @NonNull Date fecha) throws Exception {
        if(nombre.trim().length() > 0 && fecha.toString().length() > 0 ) {
            db.getObj().setNombre(nombre);
            if(!db.save())
                throw new  Exception("No se pudo guardar los datos de la banda");
        }
    }

    public List<Genero> listAllGenero(){
        return Arrays.asList(db.listAll().toArray());
    }

    
    
}

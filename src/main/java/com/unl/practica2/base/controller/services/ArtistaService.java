package com.unl.practica2.base.controller.services;

import com.unl.practica2.base.controller.DataStruct.list.LinkedList;
import com.unl.practica2.base.controller.dao.dao_models.DaoArtista;
import com.unl.practica2.base.models.Artista;
import com.vaadin.hilla.BrowserCallable;

@BrowserCallable
public class ArtistaService {

    private DaoArtista dao = new DaoArtista();

    public LinkedList<Artista> listArtistas() {
        return dao.listAll();
    }

    public Artista createArtista(String nombre, String nacionalidad) {
        dao.getObj().setId(dao.listAll().getLength() + 1);
        dao.getObj().setNombre(nombre);
        dao.getObj().setNacionalidad(nacionalidad);
        if (dao.save()) {
            return dao.getObj();
        }
        throw new RuntimeException("No se pudo guardar el artista");
    }

    public Artista updateArtista(Integer id, String nombre, String nacionalidad) {
        LinkedList<Artista> artistas = dao.listAll();
        for (int i = 0; i < artistas.getLength(); i++) {
            Artista a = artistas.get(i);
            if (a.getId().equals(id)) {
                dao.getObj().setId(id);
                dao.getObj().setNombre(nombre);
                dao.getObj().setNacionalidad(nacionalidad);
                if (dao.update(i)) {
                    return dao.getObj();
                }
                break;
            }
        }
        throw new RuntimeException("No se pudo actualizar el artista con ID: " + id);
    }

    public LinkedList<String> listCountry() {
        LinkedList<Artista> artistas = dao.listAll();
        LinkedList<String> countries = new LinkedList<>();

        for (int i = 0; i < artistas.getLength(); i++) {
            String nacionalidad = artistas.get(i).getNacionalidad();
            boolean existe = false;
            for (int j = 0; j < countries.getLength(); j++) {
                if (countries.get(j).equals(nacionalidad)) {
                    existe = true;
                    break;
                }
            }
            if (!existe) {
                countries.add(nacionalidad);
            }
        }

        return countries;
    }
}

package com.unl.practica2.base.controller.services;

import java.sql.Date;
import java.util.HashMap;

import com.unl.practica2.base.controller.DataStruct.list.LinkedList;
import com.unl.practica2.base.controller.dao.dao_models.DaoAlbum;
import com.unl.practica2.base.controller.dao.dao_models.DaoBanda;
import com.unl.practica2.base.models.Album;
import com.unl.practica2.base.models.Banda;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import jakarta.validation.constraints.NotEmpty;

@BrowserCallable
@AnonymousAllowed
public class AlbumServices {
    private DaoAlbum dao;

    public AlbumServices() {
        dao = new DaoAlbum();
    }

    public void create(@NotEmpty String nombre, Date fecha, int id_banda) throws Exception {
        if (nombre.trim().length() > 0 && fecha != null && id_banda > 0) {
            dao.getObj().setNombre(nombre);
            dao.getObj().setFecha(fecha);
            dao.getObj().setId_banda(id_banda);
            if (!dao.save())
                throw new Exception("No se pudo guardar el álbum");
        }
    }

    public void update(int id, @NotEmpty String nombre, Date fecha, int id_banda) throws Exception {
        if (nombre.trim().length() > 0 && fecha != null && id_banda > 0) {
            Album a = dao.findById(id);
            if (a == null)
                throw new Exception("Álbum no encontrado");

            dao.setObj(a);
            dao.getObj().setNombre(nombre);
            dao.getObj().setFecha(fecha);
            dao.getObj().setId_banda(id_banda);

            if (!dao.update(id - 1))
                throw new Exception("No se pudo actualizar el álbum");
        }
    }

    public HashMap[] listAlbum() {
        int len = dao.listAll().getLength();
        HashMap[] lista = new HashMap[len];
        Album[] arreglo = dao.listAll().toArray();

        for (int i = 0; i < len; i++) {
            HashMap<String, String> aux = new HashMap<>();
            aux.put("id", String.valueOf(arreglo[i].getId()));
            aux.put("nombre", arreglo[i].getNombre());
            aux.put("fecha", arreglo[i].getFecha().toString());

            Banda b = buscarBandaPorId(arreglo[i].getId_banda());
            if (b != null) {
                aux.put("banda", b.getNombre());
                aux.put("id_banda", String.valueOf(b.getId()));
            } else {
                aux.put("banda", "Desconocida");
                aux.put("id_banda", "");
            }

            lista[i] = aux;
        }

        return lista;
    }

    public HashMap[] listaBandaCombo() {
        DaoBanda daoB = new DaoBanda();
        int len = daoB.listAll().getLength();
        HashMap[] lista = new HashMap[len];
        Banda[] arreglo = daoB.listAll().toArray();

        for (int i = 0; i < len; i++) {
            HashMap<String, String> aux = new HashMap<>();
            aux.put("value", String.valueOf(arreglo[i].getId()));
            aux.put("label", arreglo[i].getNombre());
            lista[i] = aux;
        }

        return lista;
    }

    // Método auxiliar que busca una Banda por ID, sin usar ArrayList
    private Banda buscarBandaPorId(int id_banda) {
        DaoBanda daoB = new DaoBanda();
        LinkedList<Banda> bandas = daoB.listAll();

        for (int i = 0; i < bandas.getLength(); i++) {
            Banda b = bandas.get(i);
            if (b.getId() == id_banda) {
                return b;
            }
        }
        return null;
    }
}

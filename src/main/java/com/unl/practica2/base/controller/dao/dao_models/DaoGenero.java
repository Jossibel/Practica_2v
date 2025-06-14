package com.unl.practica2.base.controller.dao.dao_models;

import com.unl.practica2.base.controller.dao.AdapterDao;
import com.unl.practica2.base.models.Genero;

public class DaoGenero extends AdapterDao<Genero> {
    private Genero obj;

    public DaoGenero() {
        super(Genero.class);
        // TODO Auto-generated constructor stub
    }

    public Genero getObj() {
        if (obj == null)
            this.obj = new Genero();
        return this.obj;
    }

    public void setObj(Genero obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            this.persist(obj);
            return true;
        } catch (Exception e) {
            // TODO
            return false;
            // TODO: handle exception
        }
    }

    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            // TODO
            return false;
            // TODO: handle exception
        }
    }

    public static void main(String[] args) {
        DaoGenero dao = new DaoGenero();

        // Ejemplo de creación
        dao.getObj().setId(dao.listAll().getLength() + 1);
        dao.getObj().setNombre("Rock");
        if (dao.save()) {
            System.out.println("Genero ");
        } else {
            System.out.println("Error al guardar genero");
        }

        // Ejemplo de actualización
        dao.setObj(null);
        dao.getObj().setId(1); // ID a actualizar
        dao.getObj().setNombre("Opera");
        if (dao.update(0)) { // Posición en la lista
            System.out.println("Genero actualizado exitosamente");
        } else {
            System.out.println("Error al actualizar genero");
        }
    }

}

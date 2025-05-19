package com.unl.practica2.base.controller.dao.dao_models;

import com.unl.practica2.base.controller.dao.AdapterDao;
import com.unl.practica2.base.models.Album;

public class DaoAlbum extends AdapterDao<Album> {
    private Album obj;

    public DaoAlbum() {
        super(Album.class);
    }

    public Album getObj() {
        if (obj == null)
            this.obj = new Album();
        return this.obj;
    }

    public void setObj(Album obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            if (obj.getId() == null) {
                obj.setId(this.listAll().getLength() + 1);
            }
            this.persist(obj);
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar álbum: " + e.getMessage());
            return false;
        }
    }

    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar álbum: " + e.getMessage());
            return false;
        }
    }

    public Album findById(int id) {
        try {
            for (int i = 0; i < this.listAll().getLength(); i++) {
                Album a = this.listAll().get(i);
                if (a.getId() == id) {
                    return a;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al buscar álbum por ID: " + e.getMessage());
        }
        return null;
    }

    public Album findByNombre(String nombre) {
        try {
            for (int i = 0; i < this.listAll().getLength(); i++) {
                Album a = this.listAll().get(i);
                if (a.getNombre().equalsIgnoreCase(nombre)) {
                    return a;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al buscar álbum por nombre: " + e.getMessage());
        }
        return null;
    }

    public boolean delete(int id) {
        try {
            for (int i = 0; i < this.listAll().getLength(); i++) {
                if (this.listAll().get(i).getId() == id) {
                    this.delete(i);
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar álbum: " + e.getMessage());
        }
        return false;
    }

    // Nuevo método para obtener todos los álbumes como array
    public Album[] getAllAlbums() {
        return this.listAll().toArray();
    }

    public static void main(String[] args) {
        DaoAlbum da = new DaoAlbum();
        da.getObj().setId(da.listAll().getLength() + 1);
        da.getObj().setNombre("limon");
        if (da.save())
            System.out.println("GUARDADO");
        else
            System.out.println("Hubo un error");
        da.setObj(null);
        da.getObj().setId(da.listAll().getLength() + 1);
        da.getObj().setNombre("lulo");
        if (da.save())
            System.out.println("GUARDADO");
        else
            System.out.println("Hubo un error");
    }

}

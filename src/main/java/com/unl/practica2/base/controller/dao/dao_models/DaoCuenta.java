package com.unl.practica2.base.controller.dao.dao_models;

import com.unl.practica2.base.controller.dao.AdapterDao;
import com.unl.practica2.base.models.Cuenta;

public class DaoCuenta extends AdapterDao<Cuenta> {
    private Cuenta obj;

    public DaoCuenta() {
        super(Cuenta.class);
        // TODO Auto-generated constructor stub
    }

    public Cuenta getObj() {
        if (obj == null)
            this.obj = new Cuenta();
        return this.obj;
    }

    public void setObj(Cuenta obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            obj.setId(this.listAll().getLength() + 1);
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
        DaoCuenta da = new DaoCuenta();
        da.getObj().setId(da.listAll().getLength() + 1);

        if (da.save())
            System.out.println("GUARDADO");
        else
            System.out.println("Hubo un error");
        da.setObj(null);
        da.getObj().setId(da.listAll().getLength() + 1);
        da.getObj().setCorreo("correo");
        da.getObj().setClave("clave");
        da.getObj().setEstado(true);
        da.getObj().setId_persona(1);
        if (da.save())
            System.out.println("GUARDADO");
        else
            System.out.println("Hubo un error");
        da.setObj(null);
        da.getObj().setId(da.listAll().getLength() + 1);
        da.getObj().setCorreo("correo");
        da.getObj().setClave("clave");
        da.getObj().setEstado(true);
        da.getObj().setId_persona(2);
        if (da.save())
            System.out.println("GUARDADO");
        else
            System.out.println("Hubo un error");

    }

}

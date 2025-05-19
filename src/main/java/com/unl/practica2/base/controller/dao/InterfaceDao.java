package com.unl.practica2.base.controller.dao;

import com.unl.practica2.base.controller.DataStruct.list.LinkedList;

public interface InterfaceDao<T> {
    // Se utilizara para todo el sistema
    public LinkedList<T> listAll();

    public void persist(T obj) throws Exception;// guarda el objeto

    public void update(T obj, Integer pos) throws Exception;// actualiza el objeto

    public void update_by_id(T obj, Integer id) throws Exception;// actualiza el objeto por id

    public T get(Integer id) throws Exception;// obtiene el objeto por id

}
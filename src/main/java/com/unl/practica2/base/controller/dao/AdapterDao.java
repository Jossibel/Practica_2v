package com.unl.practica2.base.controller.dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

import com.google.gson.Gson;
import com.unl.practica2.base.controller.DataStruct.list.LinkedList;

public class AdapterDao<T> implements InterfaceDao<T> {
    protected Class<T> clazz;
    protected Gson g;
    protected static String base_path = "data" + File.separatorChar; // Cambiado a tu ruta

    public AdapterDao(Class<T> clazz) {
        this.clazz = clazz;
        this.g = new Gson();
        // Asegurar que el directorio existe
        new File(base_path).mkdirs();
    }

    protected String readFile() throws Exception {
        File file = new File(base_path + clazz.getSimpleName() + ".json");
        if (!file.exists()) {
            saveFile("[]");
        }
        StringBuilder sb = new StringBuilder();
        try (Scanner in = new Scanner(new FileReader(file))) {
            while (in.hasNextLine()) {
                sb.append(in.nextLine()).append("\n");
            }
        }
        return sb.toString();
    }

    protected void saveFile(String data) throws Exception {
        File file = new File(base_path + clazz.getSimpleName() + ".json");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(data);
            fw.flush();
        }
    }

    protected void saveAll(LinkedList<T> lista) throws Exception {
        saveFile(g.toJson(lista.toArray()));
    }

    @Override
    public LinkedList<T> listAll() {
        LinkedList<T> lista = new LinkedList<>();
        try {
            String data = readFile();
            T[] array = (T[]) g.fromJson(data, java.lang.reflect.Array.newInstance(clazz, 0).getClass());
            for (T item : array) {
                lista.add(item);
            }
        } catch (Exception e) {
            System.err.println("Error al listar: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void persist(T obj) throws Exception {
        LinkedList<T> lista = listAll();
        lista.add(obj);
        saveFile(g.toJson(lista.toArray()));
    }

    @Override
    public void update(T obj, Integer pos) throws Exception {
        LinkedList<T> lista = listAll();
        if (pos >= 0 && pos < lista.getLength()) {
            lista.update(obj, pos);
            saveFile(g.toJson(lista.toArray()));
        } else {
            throw new IndexOutOfBoundsException("Posición inválida");
        }
    }

    @Override
    public void update_by_id(T obj, Integer id) throws Exception {
        // Implementación específica para cada DAO
        throw new UnsupportedOperationException("Método no implementado");
    }

    @Override
    public T get(Integer id) throws Exception {
        // Implementación específica para cada DAO
        throw new UnsupportedOperationException("Método no implementado");
    }

}
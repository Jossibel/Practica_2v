package com.unl.practica2.base.controller.dao.dao_models;

import com.unl.practica2.base.models.Album;
import com.unl.practica2.base.models.Cancion;
import java.util.HashMap;
import com.unl.practica2.base.controller.Utiles;
import com.unl.practica2.base.controller.dao.AdapterDao;
import com.unl.practica2.base.controller.DataStruct.list.LinkedList;

import java.util.HashMap;

public class DaoCancion extends AdapterDao<Cancion> {
    private Cancion obj; // instancia de cancion

    public DaoCancion() {
        super(Cancion.class);
        // constructor de la clase
    }

    public Cancion getObj() {// devuelve la instancia de cancion
        if (obj == null)
            this.obj = new Cancion();
        return this.obj;
    }

    public void setObj(Cancion obj) {// setea la instancia de cancion
        this.obj = obj;
    }

    public Boolean save() {
        try {
            if (obj.getId() == null) {
                // Encontrar el máximo ID existente
                int maxId = 0;
                if (!listAll().isEmpty()) {
                    Cancion[] canciones = listAll().toArray();
                    for (Cancion c : canciones) {
                        if (c.getId() > maxId) {
                            maxId = c.getId();
                        }
                    }
                }
                obj.setId(maxId + 1); // Asignar el siguiente ID disponible
            }
            this.persist(obj);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            //TODO
            return false;
        }
    }

    public LinkedList<HashMap<String, String>> all() throws Exception {
        LinkedList<HashMap<String, String>> lista = new LinkedList<>();
        if (!this.listAll().isEmpty()) {
            Cancion[] arreglo = this.listAll().toArray();
            for (int i = 0; i < arreglo.length; i++) {
                lista.add(toDict(arreglo[i]));
            }
        }
        return lista;
    }

    private HashMap<String, String> toDict(Cancion cancion) throws Exception {
        HashMap<String, String> aux = new HashMap<>();
        aux.put("id", cancion.getId().toString());
        aux.put("nombre", cancion.getNombre());
        aux.put("genero", new DaoGenero().listAll().get(cancion.getId_genero()-1).getNombre());
        aux.put("duracion", cancion.getDuracion().toString());
        aux.put("tipo", cancion.getTipo().toString());
        aux.put("url", cancion.getUrl());
        aux.put("album", new DaoAlbum().listAll().get(cancion.getId_album()-1).getNombre());
        aux.put("id_genero", cancion.getId_genero().toString());
        aux.put("id_album", cancion.getId_album().toString());
        return aux;
    }

    public LinkedList<HashMap<String, String>> orderByCancion(Integer type, String attribute) throws Exception {
        LinkedList<HashMap<String, String>> lista = all();
        if (!lista.isEmpty()) {
            HashMap[] arr = lista.toArray();
            int n = arr.length;
            if (type == Utiles.ASCEDENTE) {
                for (int i = 0; i < n - 1; i++) {
                    int min_idx = i;
                    for (int j = i + 1; j < n; j++)
                        if (arr[j].get(attribute).toString().toLowerCase()
                                .compareTo(arr[min_idx].get(attribute).toString().toLowerCase()) < 0) {
                            min_idx = j;
                        }

                    HashMap temp = arr[min_idx];
                    arr[min_idx] = arr[i];
                    arr[i] = temp;
                }
            } else {
                for (int i = 0; i < n - 1; i++) {
                    int min_idx = i;
                    for (int j = i + 1; j < n; j++)
                        if (arr[j].get(attribute).toString().toLowerCase()
                                .compareTo(arr[min_idx].get(attribute).toString().toLowerCase()) > 0) {
                            min_idx = j;
                        }

                    HashMap temp = arr[min_idx];
                    arr[min_idx] = arr[i];
                    arr[i] = temp;
                }
            }
            lista.toList(arr);
        }
        return lista;
    }

    private int partition(HashMap<String, String> arr[], int begin, int end, Integer type, String attribute) {
        HashMap<String, String> pivot = arr[end];
        int i = (begin - 1);

        if (type == Utiles.ASCEDENTE) {
            for (int j = begin; j < end; j++) {
                if (arr[j].get(attribute).toString().toLowerCase()
                        .compareTo(pivot.get(attribute).toString().toLowerCase()) < 0) {
                    i++;
                    HashMap<String, String> swapTemp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = swapTemp;
                }
            }
        } else {
            for (int j = begin; j < end; j++) {
                if (arr[j].get(attribute).toString().toLowerCase()
                        .compareTo(pivot.get(attribute).toString().toLowerCase()) > 0) {
                    i++;
                    HashMap<String, String> swapTemp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = swapTemp;
                }
            }
        }
        HashMap<String, String> swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

    private void quicksort(HashMap<String, String> arr[], int begin, int end, Integer type, String attribute) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end, type, attribute);
            quicksort(arr, begin, partitionIndex - 1, type, attribute);
            quicksort(arr, partitionIndex + 1, end, type, attribute);
        }
    }

    public LinkedList<HashMap<String, String>> orderQuickCancion(Integer type, String attribute) throws Exception {
        LinkedList<HashMap<String, String>> lista = all();
        if (!lista.isEmpty()) {
            HashMap<String, String> arr[] = lista.toArray();
            quicksort(arr, 0, arr.length - 1, type, attribute);
            lista.toList(arr);
        }
        return lista;
    }

    public LinkedList<HashMap<String, String>> search(String attribute, String text, Integer type) throws Exception {
        LinkedList<HashMap<String, String>> lista = all();
        LinkedList<HashMap<String, String>> resp = new LinkedList<>();

        if (!lista.isEmpty()) {
            lista = orderQuickCancion(Utiles.ASCEDENTE, attribute);
            HashMap<String, String>[] arr = lista.toArray();
            Integer n = bynaryLineal(arr, attribute, text);

            switch (type) {
                case 1: // startsWith
                    if (n > 0) {
                        for (int i = n; i < arr.length; i++) {
                            if (arr[i].get(attribute).toString().trim().toLowerCase().startsWith(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    } else {
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].get(attribute).toString().toLowerCase().startsWith(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    }
                    break;

                case 2: // endsWith
                    if (n > 0) {
                        for (int i = n; i < arr.length; i++) {
                            if (arr[i].get(attribute).toString().toLowerCase().endsWith(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    } else {
                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].get(attribute).toString().toLowerCase().endsWith(text.toLowerCase())) {
                                resp.add(arr[i]);
                            }
                        }
                    }
                    break;
                    
                default: // contains
                    for (int i = 0; i < arr.length; i++) {
                        if (arr[i].get(attribute).toString().toLowerCase().contains(text.toLowerCase())) {
                            resp.add(arr[i]);
                        }
                    }
                    break;
            }
        }
        return resp;
    }

    private Integer bynaryLineal(HashMap<String, String>[] array, String attribute, String text) {
        Integer half = 0;
        if (!(array.length == 0) && !text.isEmpty()) {
            half = array.length / 2;
            int aux = 0;
            if (text.trim().toLowerCase().charAt(0) > array[half].get(attribute).toString().toLowerCase().charAt(0))
                aux = 1;
            else if (text.trim().toLowerCase().charAt(0) < array[half].get(attribute).toString().trim().toLowerCase().charAt(0))
                aux = -1;
            half = half * aux;
        }
        return half;
    }
}
package com.unl.practica2.base.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;

import com.unl.practica2.base.controller.DataStruct.list.LinkedList;

public class Practica {
    private Integer[] matriz;
    private LinkedList<Integer> lista;

    public void cargar() {
        // TODO
        lista = new LinkedList<>();
        try {
            BufferedReader fb = new BufferedReader(new FileReader("data/data.txt"));
            String line = fb.readLine();
            while (line != null) {
                lista.add(Integer.parseInt(line));
                line = fb.readLine();
            }
            fb.close();
            // System.out.println(fb.readLine());
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    private Boolean verificar_numero_arreglo(Integer a) {
        int cont = 0;
        Boolean band = false;
        // StringBuilder resp = new StringBuilder();
        for (int i = 0; i < matriz.length; i++) {
            if (a.intValue() == matriz[i].intValue())
                cont++;
            if (cont >= 2) {
                band = true;
                break;
            }
        }
        return band;// resp.toString().split("-");
    }

    public String[] verificar_arreglo() {
        StringBuilder resp = new StringBuilder();
        for (int i = 0; i < matriz.length; i++) {
            if (verificar_numero_arreglo(matriz[i]))
                resp.append(matriz[i].toString()).append("-");
        }
        return resp.toString().split("-");
    }

    private Boolean verificar_numero_lista(Integer a) {
        int cont = 0;
        Boolean band = false;
        // StringBuilder resp = new StringBuilder();
        for (int i = 0; i < lista.getLength(); i++) {
            if (a.intValue() == lista.get(i).intValue())
                cont++;
            if (cont >= 2) {
                band = true;
                break;
            }
        }
        return band;// resp.toString().split("-");
    }

    public LinkedList<Integer> verificar_lista() {
        LinkedList<Integer> resp = new LinkedList<>();
        for (int i = 0; i < lista.getLength(); i++) {
            if (verificar_numero_lista(lista.get(i).intValue()))
                resp.add(lista.get(i));
        }
        return resp;
    }

    public LinkedList<Integer> burbuja() {
        cargar();
        // LinkedList<Integer> listaO = new LinkedList<>();
        Integer cont = 0;
        long startTime = System.currentTimeMillis();

        if (!lista.isEmpty()) {
            Integer[] m = lista.toArray();
            for (int i = 0; i < m.length - 1; i++) {
                // En cada iteración llegamos hasta n-1-i ya que hemos ordenado i enteros
                // en las i iteraciones pasadas.

                for (int j = 0; j < (m.length - 1 - i); j++) {
                    // Comparamos e intercambiamos si se cumple la condición
                    if (m[j] > m[j + 1]) {
                        Integer aux = m[j];
                        // lista.update(lista.get(j+1), j);
                        m[j] = m[j + 1];
                        // lista.update(aux, j + 1);
                        m[j + 1] = aux;
                        cont++;
                    }
                }
            }
            long endTime = System.currentTimeMillis() - startTime;

            System.out.println("se ha demorado " + endTime + " he hizo " + cont);
            lista.toList(m);
        }

        return lista;
    }

    public LinkedList<Integer> burbujaMejorado() {
        cargar();
        // LinkedList<Integer> listaO = new LinkedList<>();
        Integer cont = 0;
        long startTime = System.currentTimeMillis();

        if (!lista.isEmpty()) {
            Integer arreglo[] = lista.toArray();
            int intercambios = 0, comparaciones = 0;
            int i, izq, der, k, aux;
            izq = 1;
            der = arreglo.length - 1;
            k = arreglo.length - 1;
            while (izq <= der) {
                for (i = der; i >= izq; i--) {
                    comparaciones++;
                    if (arreglo[i - 1] > arreglo[i]) {
                        intercambios++;
                        aux = arreglo[i - 1];
                        arreglo[i - 1] = arreglo[i];
                        arreglo[i] = aux;
                        k = i;
                    }
                }
                izq = k + 1;
                for (i = izq; i <= der; i++) {
                    comparaciones++;
                    if (arreglo[i - 1] > arreglo[i]) {
                        intercambios++;
                        aux = arreglo[i - 1];
                        arreglo[i - 1] = arreglo[i];
                        arreglo[i] = aux;
                        k = i;
                    }
                }
                der = k - 1;

            }
            long endTime = System.currentTimeMillis() - startTime;
            System.out.println("se ha demorado " + endTime + " he hizo " + intercambios);
            lista.toList(arreglo);

        }
        return lista;
    }

    public LinkedList<Integer> insercion() {
        cargar();
        // LinkedList<Integer> listaO = new LinkedList<>();
        Integer cont = 0;
        long startTime = System.currentTimeMillis();
        Integer array[] = lista.toArray();
        if (!lista.isEmpty()) {
            for (int i = 1; i < array.length; i++) {

                int key = array[i];

                int j = i - 1;

                // Move elements of array[0..i-1], that are greater than key, to one position
                // ahead

                while (j >= 0 && array[j] > key) {

                    array[j + 1] = array[j];

                    j = j - 1;
                    cont++;
                }

                array[j + 1] = key;

            }
            long endTime = System.currentTimeMillis() - startTime;
            System.out.println("se ha demorado " + endTime + " he hizo " + cont);
            lista.toList(array);
        }
        return lista;
    }

    

    public LinkedList<Integer> seleccion(Integer type) {
        cargar();
        // LinkedList<Integer> listaO = new LinkedList<>();
        // se ha demorado 538 he hizo 116453 NORMALITO
        // se ha demorado 546 he hizo 116453
        // 599
        if (!lista.isEmpty()) {
            Integer cont = 0;
            long startTime = System.currentTimeMillis();
            Integer arr[] = lista.toArray();
            int n = arr.length;
            if (type == 1) {
                for (int i = 0; i < n - 1; i++) {
                    int min_idx = i;
                    for (int j = i + 1; j < n; j++) {
                        if (arr[j] < arr[min_idx]) {
                            min_idx = j;
                            cont++;
                        }
                    }
                    int temp = arr[min_idx];
                    arr[min_idx] = arr[i];
                    arr[i] = temp;
                }
            } else {
                for (int i = 0; i < n - 1; i++) {
                    int min_idx = i;
                    for (int j = i + 1; j < n; j++) {
                        if (arr[j] > arr[min_idx]) {
                            min_idx = j;
                            cont++;
                        }
                    }
                    int temp = arr[min_idx];
                    arr[min_idx] = arr[i];
                    arr[i] = temp;
                }
            }

            long endTime = System.currentTimeMillis() - startTime;
            System.out.println("se ha demorado " + endTime + " he hizo " + cont);
            lista.toList(arr);
        }
        return lista;
    }
// PRACTICA ORDER
    private void quickSort(Integer arr[], int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, end);
        }
    }

    private int partition(Integer arr[], int begin, int end) {
        int pivot = arr[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (arr[j] <= pivot) {
                i++;

                int swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }
        int swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }

    public void q_order() {
        cargar();
        if (!lista.isEmpty()) {
            Integer arr[] = lista.toArray();
            Integer cont = 0;
            long startTime = System.currentTimeMillis();
            quickSort(arr, 0, arr.length - 1);
           // quickSort(arr, 0, (arr.length - 1));
            long endTime = System.currentTimeMillis() - startTime;
            System.out.println("se ha demorado quicksort " + endTime );
            lista.toList(arr);
        }        
    }

    public void s_order() {
        cargar();
        if (!lista.isEmpty()) {
            Integer arr[] = lista.toArray();
            Integer cont = 0;
            long startTime = System.currentTimeMillis();
            shell_sort(arr);
           // quickSort(arr, 0, (arr.length - 1));
            long endTime = System.currentTimeMillis() - startTime;
            System.out.println("se ha demorado shell " + endTime );
            lista.toList(arr);
        }        
    }

    public void shell_sort(Integer arrayToSort[]) {
        int n = arrayToSort.length;

        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int key = arrayToSort[i];
                int j = i;
                while (j >= gap && arrayToSort[j - gap] > key) {
                    arrayToSort[j] = arrayToSort[j - gap];
                    j -= gap;
                }
                arrayToSort[j] = key;
            }
        }
    }

    public static void main(String[] args) {
        Practica p = new Practica();
        /*
         * System.out.println("BURBUJA");
         * p.burbuja();
         * System.out.println("BURBUJA MEOJRADISIMO");
         * p.burbujaMejorado();
         * System.out.println("Insersion");
         * p.insercion();
         */
        System.out.println("Quicksort");
        p.q_order();
        System.out.println("Shellsort");
        p.s_order();
    }
}

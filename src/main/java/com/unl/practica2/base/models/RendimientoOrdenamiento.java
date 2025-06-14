package com.unl.practica2.base.models;

import com.unl.practica2.base.controller.DataStruct.list.LinkedList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RendimientoOrdenamiento {

    public static LinkedList<Integer> cargarDatos(String path) {
        LinkedList<Integer> lista = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                try {
                    Integer numero = Integer.parseInt(linea.trim());
                    lista.add(numero);
                } catch (NumberFormatException e) {
                    System.out.println("Dato inválido: " + linea);
                }
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        return lista;
    }

    // Implementación de QuickSort para LinkedList
    public static void quickSort(LinkedList<Integer> list) {
        if (list.getLength() <= 1) {
            return;
        }
        
        LinkedList<Integer> menores = new LinkedList<>();
        LinkedList<Integer> iguales = new LinkedList<>();
        LinkedList<Integer> mayores = new LinkedList<>();
        
        Integer pivot = list.get(0);
        
        for (int i = 0; i < list.getLength(); i++) {
            Integer current = list.get(i);
            if (current < pivot) {
                menores.add(current);
            } else if (current.equals(pivot)) {
                iguales.add(current);
            } else {
                mayores.add(current);
            }
        }
        
        quickSort(menores);
        quickSort(mayores);
        
        list.clear();
        list.addAll(menores);
        list.addAll(iguales);
        list.addAll(mayores);
    }

    // Implementación de ShellSort para LinkedList
    public static void shellSort(LinkedList<Integer> list) {
        int n = list.getLength();
        
        // Secuencia de gaps optimizada
        for (int gap = n/2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                Integer temp = list.get(i);
                int j;
                for (j = i; j >= gap && list.get(j - gap) > temp; j -= gap) {
                    list.set(j, list.get(j - gap));
                }
                list.set(j, temp);
            }
        }
    }

    public static void main(String[] args) {
        // Ruta específica del archivo de datos
        String path = "/home/anahi/Descargas/data.txt";
        LinkedList<Integer> datosOriginales = cargarDatos(path);
        int cantidadOriginal = datosOriginales.getLength();
        
        // Agregar 10,000 datos adicionales
        int adicionales = 10000;
        LinkedList<Integer> datosAmpliados = new LinkedList<>();
        datosAmpliados.addAll(datosOriginales);
        for (int i = 0; i < adicionales; i++) {
            datosAmpliados.add((int)(Math.random() * 10000));
        }
        
        // Mostrar información de los datos
        System.out.println("===============================================");
        System.out.printf("%-35s %10s%n", "Descripción", "Cantidad");
        System.out.println("===============================================");
        System.out.printf("%-35s %10d%n", "Números en data original:", cantidadOriginal);
        System.out.printf("%-35s %10d%n", "Números adicionales agregados:", adicionales);
        System.out.printf("%-35s %10d%n", "Total números para ordenar:", datosAmpliados.getLength());
        System.out.println("===============================================");

        // Ejecutar y medir QuickSort
        LinkedList<Integer> copiaQS = copiarLista(datosAmpliados);
        long inicioQS = System.nanoTime();
        quickSort(copiaQS);
        long finQS = System.nanoTime();
        double tiempoQS = (finQS - inicioQS) / 1_000_000.0;

        // Ejecutar y medir ShellSort
        LinkedList<Integer> copiaSS = copiarLista(datosAmpliados);
        long inicioSS = System.nanoTime();
        shellSort(copiaSS);
        long finSS = System.nanoTime();
        double tiempoSS = (finSS - inicioSS) / 1_000_000.0;

        // Mostrar resultados
        System.out.println("\nRESULTADOS DE TIEMPOS DE ORDENAMIENTO");
        System.out.println("+----------------+-------------------+");
        System.out.println("| Algoritmo      | Tiempo (ms)       |");
        System.out.println("+----------------+-------------------+");
        System.out.printf("| %-14s | %17.3f |%n", "QuickSort", tiempoQS);
        System.out.printf("| %-14s | %17.3f |%n", "ShellSort", tiempoSS);
        System.out.println("+----------------+-------------------+");
    }

    private static LinkedList<Integer> copiarLista(LinkedList<Integer> original) {
        LinkedList<Integer> copia = new LinkedList<>();
        for (int i = 0; i < original.getLength(); i++) {
            copia.add(original.get(i));
        }
        return copia;
    }

}
package com.unl.practica2.base.models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.unl.practica2.base.controller.DataStruct.list.LinkedList;

public class Rendimiento {

    public static void main(String[] args) {
        String path = "/home/anahi/Descargas/data.txt";

        // ARREGLO
        long inicioA = System.nanoTime();
        int[] arreglo = new int[1000];
        int count = 0;
        HashMap<Integer, Integer> mapaArreglo = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                int num = Integer.parseInt(linea.trim());
                if (count == arreglo.length) {
                    int[] nuevo = new int[arreglo.length * 2];
                    System.arraycopy(arreglo, 0, nuevo, 0, arreglo.length);
                    arreglo = nuevo;
                }
                arreglo[count++] = num;
                mapaArreglo.put(num, mapaArreglo.getOrDefault(num, 0) + 1);
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
        }
        long finA = System.nanoTime();

        // LISTA ENLAZADA
        long inicioL = System.nanoTime();
        LinkedList<Integer> lista = new LinkedList<>();
        HashMap<Integer, Integer> mapaLista = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                int num = Integer.parseInt(linea.trim());
                lista.add(num);
                mapaLista.put(num, mapaLista.getOrDefault(num, 0) + 1);
            }
        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
        }
        long finL = System.nanoTime();

        // Mostrar repetidos del arreglo
        System.out.println("Números repetidos en ARREGLO:");
        int repetidosArreglo = 0;
        for (int num : mapaArreglo.keySet()) {
            if (mapaArreglo.get(num) > 1) {
                System.out.println(num);
                repetidosArreglo++;
            }
        }
        System.out.println("Total distintos repetidos en ARREGLO: " + repetidosArreglo);

        // Mostrar repetidos de la lista
        System.out.println("\nNúmeros repetidos en LISTA:");
        int repetidosLista = 0;
        for (int num : mapaLista.keySet()) {
            if (mapaLista.get(num) > 1) {
                System.out.println(num);
                repetidosLista++;
            }
        }
        System.out.println("Total distintos repetidos en LISTA: " + repetidosLista);

        // Comparativa de tiempos
        System.out.println("\n==========TIEMPOS DE EJECUCIÓN (milisegundos==============):");
        System.out.printf("Arreglo       : %d ms\n", (finA - inicioA) / 1_000_000);
        System.out.printf("Lista Enlazada: %d ms\n", (finL - inicioL) / 1_000_000);
    }
}

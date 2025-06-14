package com.unl.practica2.base.controller.DataStruct.list;

import com.unl.practica2.base.controller.DataStruct.stack.Stack;
import com.unl.practica2.base.controller.DataStruct.stack.StackImplementation;

public class LinkedList<E> {
    private Node<E> head;
    private Node<E> last;
    private Integer length;

    // Clase Node interna
    private static class Node<T> {
        private T data;
        private Node<T> next;

        public Node(T data) {
            this(data, null);
        }

        public Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }
    }

    public LinkedList() {
        head = null;
        last = null;
        length = 0;
    }

    public Integer getLength() {
        return this.length;
    }

    public Boolean isEmpty() {
        return head == null || length == 0;
    }

    private Node<E> getNode(Integer pos) {
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("List empty");
        } else if (pos < 0 || pos >= length) {
            throw new ArrayIndexOutOfBoundsException("Index out range");
        } else if (pos == 0) {
            return head;
        } else if ((length - 1) == pos) {
            return last;
        } else {
            Node<E> search = head;
            int cont = 0;
            while (cont < pos) {
                cont++;
                search = search.getNext();
            }
            return search;
        }
    }

    public E get(Integer pos) {
        return getNode(pos).getData();
    }

    public void add(E data) {
        addLast(data);
    }

    public void add(E data, Integer pos) {
        if (pos == 0) {
            addFirst(data);
        } else if (length == pos) {
            addLast(data);
        } else {
            Node<E> search_preview = getNode(pos - 1);
            Node<E> search = getNode(pos);
            Node<E> aux = new Node<>(data, search);
            search_preview.setNext(aux);
            length++;
        }
    }

    private void addFirst(E data) {
        if (isEmpty()) {
            Node<E> aux = new Node<>(data);
            head = aux;
            last = aux;
        } else {
            Node<E> head_old = head;
            Node<E> aux = new Node<>(data, head_old);
            head = aux;
        }
        length++;
    }

    private void addLast(E data) {
        if (isEmpty()) {
            addFirst(data);
        } else {
            Node<E> aux = new Node<>(data);
            last.setNext(aux);
            last = aux;
            length++;
        }
    }

    public void addAll(LinkedList<E> otraLista) {
        for (int i = 0; i < otraLista.getLength(); i++) {
            this.add(otraLista.get(i));
        }
    }

    public void set(int index, E element) {
        getNode(index).setData(element);
    }

    public void update(E data, Integer pos) {
        set(pos, data);
    }

    public void clear() {
        head = null;
        last = null;
        length = 0;
    }

    public E[] toArray() {
        if (length == 0) {
            return null;
        }
        
        @SuppressWarnings("unchecked")
        E[] matriz = (E[]) java.lang.reflect.Array.newInstance(
            head.getData().getClass(), length);
        
        Node<E> aux = head;
        for (int i = 0; i < length; i++) {
            matriz[i] = aux.getData();
            aux = aux.getNext();
        }
        return matriz;
    }

    public LinkedList<E> toList(E[] matriz) {
        clear();
        for (E element : matriz) {
            this.add(element);
        }
        return this;
    }

    public E deleteFirst() {
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("List empty");
        }
        
        E element = head.getData();
        head = head.getNext();
        if (length == 1) {
            last = null;
        }
        length--;
        return element;
    }

    public E deleteLast() {
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("List empty");
        }
        
        if (length == 1) {
            return deleteFirst();
        }
        
        Node<E> prev = getNode(length - 2);
        E element = last.getData();
        prev.setNext(null);
        last = prev;
        length--;
        return element;
    }

    public E delete(Integer pos) {
        if (isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("List empty");
        } else if (pos < 0 || pos >= length) {
            throw new ArrayIndexOutOfBoundsException("Index out range");
        } else if (pos == 0) {
            return deleteFirst();
        } else if (pos == length - 1) {
            return deleteLast();
        } else {
            Node<E> prev = getNode(pos - 1);
            Node<E> current = prev.getNext();
            prev.setNext(current.getNext());
            length--;
            return current.getData();
        }
    }

    // Métodos para ordenamiento
    public void quickSort() {
        if (length > 1) {
            quickSort(0, length - 1);
        }
    }

    private void quickSort(int low, int high) {
        if (low < high) {
            int pi = partition(low, high);
            quickSort(low, pi - 1);
            quickSort(pi + 1, high);
        }
    }

    @SuppressWarnings("unchecked")
    private int partition(int low, int high) {
        E pivot = get(high);
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (((Comparable<E>) get(j)).compareTo(pivot) <= 0) {
                i++;
                swap(i, j);
            }
        }
        swap(i + 1, high);
        return i + 1;
    }

    public void shellSort() {
        int n = length;
        // Secuencia de gaps optimizada
        for (int gap = n/2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                E temp = get(i);
                int j;
                for (j = i; j >= gap && ((Comparable<E>) get(j - gap)).compareTo(temp) > 0; j -= gap) {
                    set(j, get(j - gap));
                }
                set(j, temp);
            }
        }
    }

    private void swap(int i, int j) {
        E temp = get(i);
        set(i, get(j));
        set(j, temp);
    }

    // Métodos adicionales para búsqueda
    @SuppressWarnings("unchecked")
    public LinkedList<E> busquedaLineal(String criterio, String valor) {
        LinkedList<E> resultados = new LinkedList<>();
        for (int i = 0; i < length; i++) {
            E elemento = get(i);
            try {
                java.lang.reflect.Field campo = elemento.getClass().getDeclaredField(criterio);
                campo.setAccessible(true);
                Object valorCampo = campo.get(elemento);
                
                if (valorCampo.toString().equalsIgnoreCase(valor)) {
                    resultados.add(elemento);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultados;
    }

    @SuppressWarnings("unchecked")
    public LinkedList<E> busquedaBinaria(String criterio, String valor) {
        LinkedList<E> resultados = new LinkedList<>();
        int left = 0, right = length - 1;
        
        try {
            while (left <= right) {
                int mid = left + (right - left) / 2;
                E elemento = get(mid);
                
                java.lang.reflect.Field campo = elemento.getClass().getDeclaredField(criterio);
                campo.setAccessible(true);
                Object valorCampo = campo.get(elemento);
                
                int cmp = valorCampo.toString().compareToIgnoreCase(valor);
                
                if (cmp == 0) {
                    resultados.add(elemento);
                    // Buscar duplicados
                    int temp = mid - 1;
                    while (temp >= 0) {
                        E elemTemp = get(temp);
                        campo = elemTemp.getClass().getDeclaredField(criterio);
                        campo.setAccessible(true);
                        if (campo.get(elemTemp).toString().equalsIgnoreCase(valor)) {
                            resultados.add(elemTemp);
                            temp--;
                        } else {
                            break;
                        }
                    }
                    temp = mid + 1;
                    while (temp < length) {
                        E elemTemp = get(temp);
                        campo = elemTemp.getClass().getDeclaredField(criterio);
                        campo.setAccessible(true);
                        if (campo.get(elemTemp).toString().equalsIgnoreCase(valor)) {
                            resultados.add(elemTemp);
                            temp++;
                        } else {
                            break;
                        }
                    }
                    return resultados;
                } else if (cmp < 0) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultados;
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.getData());
            if (current.getNext() != null) {
                sb.append(", ");
            }
            current = current.getNext();
        }
        sb.append("]");
        return sb.toString();
    }
}
package com.unl.practica2.base.controller.DataStruct.list;

import java.io.Serializable;

//T .......dato generico
//E .......colecciones 
//K,V .....key value
public class Node<E> implements Serializable {
    private E data;
    private Node<E> next;

    public Node(E data) {
        this.data = data;
        this.next = null;
    }

    public Node(E data, Node<E> next) {
        this.data = data;
        this.next = next;
    }

    public E getData() {
        return this.data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public Node<E> getNext() {
        return this.next;
    }

    public void setNext(Node<E> next) {
        this.next = next;
    }

}
package com.unl.practica2.base.models;

public class Complejo {
    private Float num;
    private Float imag;

    public Complejo(float num, float imag) {
        this.num = num;
        this.imag = imag;
    }

    public Complejo() {
    }

    public Float getNum() {
        return num;
    }

    public void setNum(Float num) {
        this.num = num;
    }

    public Float getImag() {
        return imag;
    }

    public void setImag(Float imag) {
        this.imag = imag;
    }

}

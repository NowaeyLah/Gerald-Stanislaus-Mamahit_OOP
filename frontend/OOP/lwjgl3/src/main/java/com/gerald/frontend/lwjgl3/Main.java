package com.gerald.frontend.lwjgl3;

class ObjectB {
    public String nama;

    public ObjectB(String nama) {
        this.nama = nama;
    }

    public String getNama() {
        return nama;
    }
}

class ObjectA {
    public void menyapa(ObjectB objekB) {
        System.out.println("Hello, " + objekB.getNama() + "!");
    }
}

public class Main {
    public static void main(String[] args) {
        ObjectB b = new ObjectB("Object B");
        ObjectA a = new ObjectA();

        a.menyapa(b);
    }
}

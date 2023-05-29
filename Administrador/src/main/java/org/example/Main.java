package org.example;

import org.zeromq.ZMQ;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ZMQ.Context ctx = ZMQ.context(1);

        ZMQ.Socket subPrestamo = ctx.socket(ZMQ.SUB);
        //ZMQ.Socket subRenovacion = ctx.socket(ZMQ.SUB);
        //ZMQ.Socket subDevolucion = ctx.socket(ZMQ.SUB);

        subPrestamo.connect("tcp://*:12345");
        //subRenovacion.connect("tcp://localhost:12346");
        //subDevolucion.connect("tcp://localhost:12347");


        // Eliminate slow subscriber problem
        Thread.sleep(100);

        while(!Thread.currentThread().isInterrupted()){
            System.out.println("SUB: " + subPrestamo.recvStr());
            System.out.println("Buenas bobbies");
        }

        subPrestamo.close();
        subPrestamo.close();
        ctx.close();
    }
}
package org.example;

import org.zeromq.ZMQ;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ZMQ.Context ctx = ZMQ.context(1);

        ZMQ.Socket pub = ctx.socket(ZMQ.PUB);
        ZMQ.Socket sub = ctx.socket(ZMQ.SUB);

        pub.bind("tcp://*:12347");
        sub.connect("tcp://localhost:12347");

        String topico = "ActorDevolucion:";
        sub.subscribe(topico);

        // Eliminate slow subscriber problem
        Thread.sleep(100);

        pub.send("ActorDevolucion: Hello, world!");
        while(!Thread.currentThread().isInterrupted()){
            System.out.println("SUB: " + sub.recvStr());
        }

        sub.close();
        pub.close();
        ctx.close();
    }
}
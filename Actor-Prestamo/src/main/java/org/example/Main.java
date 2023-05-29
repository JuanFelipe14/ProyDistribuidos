package org.example;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.net.Socket;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ZMQ.Context ctx = ZMQ.context(1);

        ZMQ.Socket pub = ctx.socket(ZMQ.PUB);
        ZMQ.Socket sub = ctx.socket(ZMQ.SUB);

        pub.bind("tcp://*:12345");
        //sub.connect("tcp://localhost:12345");

        String topico = "ActorPrestamo:";
        //sub.subscribe(topico);

        // Eliminate slow subscriber problem
        Thread.sleep(100);

        pub.send("ActorPrestamo: Hello, world!");

        /*while(!Thread.currentThread().isInterrupted()){
            System.out.println("SUB: " + sub.recvStr());
        }

        sub.close();
        */

        pub.close();

        ctx.close();
    }
}
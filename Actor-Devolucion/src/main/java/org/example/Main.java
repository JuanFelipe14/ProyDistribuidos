package org.example;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        try (ZContext context = new ZContext()) {
            ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
            subscriber.connect("tcp://localhost:5564");
            subscriber.subscribe("".getBytes(ZMQ.CHARSET));
            ZMQ.Socket publisher = context.createSocket(SocketType.PUB);
            publisher.bind("tcp://*:5563");

            while (!Thread.currentThread().isInterrupted()) {
                // Read message contents
                String contents = subscriber.recvStr();
                System.out.println(contents);

                publisher.send(contents);
            }
        }


    }
}
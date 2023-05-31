package org.example;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.net.Socket;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Prepare our context and publisher
        try (ZContext context = new ZContext()) {
            ZMQ.Socket publisher = context.createSocket(SocketType.PUB);
            publisher.bind("tcp://*:5564");

            //while (!Thread.currentThread().isInterrupted()) {
                // Write two messages, each with an envelope and content
                Thread.sleep(1000);
                publisher.send("DEVOLVER:0043");
                System.out.println("SENT");
            //}
        }
    }
}
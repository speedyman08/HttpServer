package com.speedyman77.framework;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class Server {
    ServerSocket serverSocket;

    public CountDownLatch latch;

    public Server() throws IOException {
        serverSocket = new ServerSocket(80);
        latch = new CountDownLatch(1);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Server s = new Server();
        s.init();

        s.awaitUntilReady();
        System.out.println("Listening on 80");
    }

    public void init() {
        listenForConnections();
        latch.countDown();
    }

    public void listenForConnections() {
        Thread t = new Thread (() -> {
            while(serverSocket.isBound()) {
                System.out.println("Waiting for connection..");
                try {
                    handleConnection(serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("Something went wrong with getting a client socket.");
                    System.exit(1);
                }
            }
        });
        t.start();
    }

    public void awaitUntilReady() throws InterruptedException {
        latch.await();
    }

    public void close() throws IOException {
        serverSocket.close();
    }

    public void handleConnection(Socket client) throws IOException {
        OutputStream out = client.getOutputStream();
        Response res = Response.getBuilder(HttpStatus.OK, "text/plain")
                .addContent("Hello World")
                .build();

        out.write(res.asBytes());

        System.out.println("Sent response..");

        out.close();
        client.close();
    }
}
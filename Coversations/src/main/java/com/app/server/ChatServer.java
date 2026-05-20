package com.app.server;
import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 5000;
    private static final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws IOException {
        System.out.println("Server started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, clients);
                clients.add(handler);
                new Thread(handler).start();
            }
        }
    }

    public static void broadcast(Object message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }
}
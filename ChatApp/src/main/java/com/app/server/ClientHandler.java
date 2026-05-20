package com.app.server;

// server/ClientHandler.java


import com.app.common.Message;
import java.io.*;
import java.net.*;
import java.util.List;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final List<ClientHandler> clients;
    private ObjectOutputStream out;
    private String username;

    public ClientHandler(Socket socket, List<ClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();


            Message joinMsg = (Message) in.readObject();
            this.username = joinMsg.getSender();
            System.out.println(username + " connected.");
            ChatServer.broadcast(joinMsg, this);


            Message msg;
            while ((msg = (Message) in.readObject()) != null) {
                System.out.println("[" + msg.getType() + "] from " + msg.getSender());
                ChatServer.broadcast(msg, this);
            }
        } catch (EOFException | SocketException e) {
            System.out.println((username != null ? username : "A client") + " disconnected.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            clients.remove(this);
            if (username != null) {
                ChatServer.broadcast(new Message(Message.Type.LEAVE, username, username + " left the chat."), this);
            }
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    public void sendMessage(Object msg) {
        try {
            out.writeObject(msg);
            out.flush();
            out.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

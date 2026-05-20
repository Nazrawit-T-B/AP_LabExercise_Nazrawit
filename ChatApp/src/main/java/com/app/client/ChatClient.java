package com.app.client;

// client/ChatClient.java

import com.app.common.Message;
import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {
    private Socket socket;
    private ObjectOutputStream out;
    private final Consumer<Message> onMessageReceived;

    public ChatClient(Consumer<Message> onMessageReceived) {
        this.onMessageReceived = onMessageReceived;
    }

    public void connect(String host, int port, String username) throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();


        send(new Message(Message.Type.JOIN, username, username + " joined the chat."));


        new Thread(() -> {
            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                Message msg;
                while ((msg = (Message) in.readObject()) != null) {
                    onMessageReceived.accept(msg);
                }
            } catch (Exception e) {
                System.out.println("Disconnected from server.");
            }
        }).start();
    }

    public void send(Message msg) throws IOException {
        out.writeObject(msg);
        out.flush();
        out.reset();
    }

    public void disconnect() throws IOException {
        if (socket != null) socket.close();
    }
}

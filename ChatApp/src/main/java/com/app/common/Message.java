package com.app.common;

// common/Message.java
import java.io.Serializable;

public class Message implements Serializable {
    public enum Type { TEXT,IMAGE, JOIN, LEAVE }

    private final Type type;
    private final String sender;
    private final String text;
    private final byte[] imageData;


    public Message(Type type, String sender, String text) {
        this.type = type;
        this.sender = sender;
        this.text = text;
        this.imageData = null;
    }


    public Message(String sender, byte[] imageData) {
        this.type = Type.IMAGE;
        this.sender = sender;
        this.text = null;
        this.imageData = imageData;
    }

    public Type getType()        { return type; }
    public String getSender()    { return sender; }
    public String getText()      { return text; }
    public byte[] getImageData() { return imageData; }
}

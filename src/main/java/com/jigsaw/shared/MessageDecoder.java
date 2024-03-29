package com.jigsaw.shared;

import jakarta.websocket.DecodeException;
import jakarta.websocket.Decoder;
import jakarta.websocket.EndpointConfig;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

public class MessageDecoder implements Decoder.Binary<Message> {

    @Override
    public Message decode(ByteBuffer b) throws DecodeException {
        Message message = null;
        var in = new ByteArrayInputStream(b.array());
        try {
            var stream = new ObjectInputStream(in);
            message = (Message) stream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public boolean willDecode(ByteBuffer b) {
        return true;
    }
}

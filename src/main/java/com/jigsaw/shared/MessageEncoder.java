package com.jigsaw.shared;

import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class MessageEncoder implements Encoder.Binary<Message> {

    @Override
    public void init(EndpointConfig config) {
        // Auto-generated method stub
    }

    @Override
    public void destroy() {
        // Auto-generated method stub
    }

    @Override
    public ByteBuffer encode(Message message) throws EncodeException {
        var out = new ByteArrayOutputStream();
        try {
            var stream  = new ObjectOutputStream(out);
            stream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ByteBuffer.wrap(out.toByteArray());
    }
}

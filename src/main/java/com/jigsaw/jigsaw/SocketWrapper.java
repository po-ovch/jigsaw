package com.jigsaw.jigsaw;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class SocketWrapper {
    private final BufferedWriter writer;
    private final BufferedReader reader;
    private final Socket socket;

    private String playerName;
    private final int playerIndex;

    public BufferedWriter getWriter() {
        return writer;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public Thread generatorThread;

    public AtomicBoolean isWorking;
    public Integer figuresGeneratedNum;

    public SocketWrapper(Socket socket, int index) throws IOException {
        this.socket = socket;
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        isWorking = new AtomicBoolean(true);
        playerIndex = index;
        figuresGeneratedNum = 0;
    }

    public SocketWrapper(Socket socket) throws IOException {
        this.socket = socket;
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        isWorking = new AtomicBoolean(true);
        playerIndex = -1;
    }

    public void close() {
        try {
            writer.close();
            reader.close();
            socket.close();
            if (generatorThread != null) {
                generatorThread.interrupt();
            }
        } catch (Exception ignored) {
        }
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}

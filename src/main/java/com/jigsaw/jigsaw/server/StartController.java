package com.jigsaw.jigsaw.server;

import com.jigsaw.jigsaw.Result;
import com.jigsaw.jigsaw.SocketWrapper;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class StartController {
    public VBox startPane;
    public Spinner<Integer> playersNumSpinner;
    public Slider gameDurationSlider;

    public static ServerSocket server;
    public static Thread gameSetupThread;

    public static final List<SocketWrapper> clientSockets = Collections.synchronizedList(new ArrayList<>());
    private final List<Result> gameResults = Collections.synchronizedList(new ArrayList<>());
    private final List<String> figures = Collections.synchronizedList(new ArrayList<>());
    public VBox mistakePane;
    public VBox runningPane;

    private int playersNum;
    private int gameDuration;

    public void onRunButtonClick() {
        playersNum = playersNumSpinner.getValue();
        gameDuration = (int) gameDurationSlider.getValue();
        try {
            server = new ServerSocket(5000);
            startPane.setVisible(false);
            runningPane.setVisible(true);

            gameSetupThread = new Thread(this::connectPlayers);
            gameSetupThread.start();
        } catch (IOException e) {
            startPane.setVisible(false);
            mistakePane.setVisible(true);
        }
    }

    private void connectPlayers() {
        while (true) {
            acceptClients();
        }
    }

    private void acceptClients() {
        for (int i = clientSockets.size(); i < playersNum; i++) {
            SocketWrapper clientSocket = null;
            try {
                clientSocket = new SocketWrapper(server.accept(), i);
                clientSockets.add(clientSocket);

                var playerName = clientSocket.getReader().readLine();
                clientSocket.setPlayerName(playerName);
                if (i < playersNum - 1) {
                    clientSocket.getWriter().write("await\n");
                    clientSocket.getWriter().flush();
                } else if (i == playersNum - 1) {
                    for (var clientSocket_: clientSockets) {
                        try {
                            runGameForClient(clientSocket_);
                        } catch (Exception e) {
                            disconnectPlayer(clientSocket_);
                            notifyPartnerAboutDisconnect(clientSocket_);
                        }
                    }
                }

            } catch (IOException e) {
                disconnectPlayer(clientSocket);
                i--;
            }
        }
    }

    private void runGameForClient(SocketWrapper clientSocket) throws IOException {
        clientSocket.getWriter().write("game\n");
        clientSocket.getWriter().write(clientSocket.getPlayerName() + "\n");
        clientSocket.getWriter().write((playersNum == 2) + "\n");
        if (playersNum > 1) {
            clientSocket.getWriter().write(getOtherClient(clientSocket.getPlayerIndex()).getPlayerName() + "\n");
        }
        clientSocket.getWriter().write(gameDuration + "\n");
        clientSocket.getWriter().flush();

        // Настройка генерации для клиента.
        clientSocket.generatorThread = new Thread(() -> {
            var random = new Random();
            onCommunication(clientSocket, random);
        });
        clientSocket.generatorThread.start();
    }

    private void onCommunication(SocketWrapper clientSocket, Random random) {
        try {
            label:
            while (true) {
                var text = clientSocket.getReader().readLine();
                switch (text) {
                    case "figure":
                        if (clientSocket.figuresGeneratedNum >= figures.size()) {
                            int figureIndex = random.nextInt(0, 10);
                            int rotateIndex = random.nextInt(0, 4);
                            var newFigure = String.format("%d %d\n", figureIndex, rotateIndex);
                            figures.add(newFigure);
                        }
                        clientSocket.getWriter().write(figures.get(clientSocket.figuresGeneratedNum));
                        clientSocket.getWriter().flush();
                        clientSocket.figuresGeneratedNum++;
                        break;
                    case "result":
                        concludeResults(clientSocket);
                        gameResults.clear();
                        break;
                    case "topPlayers":
                        var top = DbUtils.getTop();
                        if (top == null) {
                            clientSocket.getWriter().write("problem\n");
                        } else {
                            for (var result : top) {
                                result.writeResult(clientSocket.getWriter());
                            }
                            clientSocket.getWriter().write("end\n");
                        }
                        clientSocket.getWriter().flush();
                        break;
                    case "exit":
                        disconnectPlayer(clientSocket);
                        notifyPartnerAboutDisconnect(clientSocket);
                        break label;
                }
            }
        } catch (Exception e) {
            disconnectPlayer(clientSocket);
            notifyPartnerAboutDisconnect(clientSocket);
        }
    }

    private void concludeResults(SocketWrapper clientSocket) throws IOException {
        var result = Result.readResult(clientSocket.getReader());
        DbUtils.saveResult(result);
        gameResults.add(result);
        while (playersNum == 2 && gameResults.size() != 2) {
            // wait until other player is ready.
        }
        Collections.sort(gameResults);
        clientSocket.getWriter().write(gameResults.get(playersNum - 1).getPlayerName() + "\n");
        clientSocket.getWriter().write(clientSocket.getPlayerName() + "\n");
        clientSocket.getWriter().flush();
    }

    private void notifyPartnerAboutDisconnect(SocketWrapper clientSocket) {
        if (playersNum == 2) {
            var otherPlayer = getOtherClient(clientSocket.getPlayerIndex());
            try {
                otherPlayer.getWriter().write("win\n");
                otherPlayer.getWriter().flush();
            } catch (IOException e) {
                disconnectPlayer(otherPlayer);
            }
        }
    }

    private SocketWrapper getOtherClient(int firstIndex) {
        return clientSockets.get((firstIndex + 1) % 2);
    }

    private void disconnectPlayer(SocketWrapper clientSocket) {
        if (clientSocket != null) {
            clientSocket.isWorking.set(false);
            clientSockets.remove(clientSocket);
            clientSocket.close();
        }
    }
}

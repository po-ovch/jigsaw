package com.jigsaw.client.webControllers;

import com.jigsaw.shared.Message;
import com.jigsaw.shared.MessageDecoder;
import com.jigsaw.shared.MessageType;
import com.jigsaw.shared.entities.GameStatistics;
import com.jigsaw.shared.messages.TopPlayersMessage;
import jakarta.websocket.DecodeException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.util.List;

public class HttpClient {
    private static final String serverUri = "http://localhost:7002/";

    public List<GameStatistics> getTopStatistics(int limit) {
        var relativeUri = "players/top?limit=" + limit;

        var client = java.net.http.HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUri + relativeUri))
                .build();

        Message message;
        try {
            var response =
                    client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() != 200) {
                return null;
            }

            message = new MessageDecoder().decode(ByteBuffer.wrap(response.body()));
        } catch (IOException | InterruptedException | DecodeException e) {
            return null;
        }

        if (message.messageType != MessageType.TopPlayers) {
            return null;
        }
        return ((TopPlayersMessage)message).statistics;
    }
}

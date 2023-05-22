package com.jigsaw.server.webControllers;

import com.jigsaw.server.dao.ResultDAO;
import com.jigsaw.server.db.Result;
import com.jigsaw.shared.MessageEncoder;
import com.jigsaw.shared.entities.GameStatistics;
import com.jigsaw.shared.messages.TopPlayersMessage;
import com.sun.net.httpserver.HttpExchange;
import jakarta.websocket.EncodeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpHandler implements com.sun.net.httpserver.HttpHandler {
    private final ResultDAO results = new ResultDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            try {
                var param = getRequestParams(exchange);
                handleGetRequest(exchange, Integer.parseInt(param));
            } catch (Exception e) {
                exchange.sendResponseHeaders(500, 0);
            }
        } else {
            System.out.println("Operation not supported");
        }
    }

    private void handleGetRequest(HttpExchange httpExchange, int topLimit) throws IOException, EncodeException {
        var outputStream = httpExchange.getResponseBody();

        List<GameStatistics> stats = new ArrayList<>();
        for (Result result : results.findAll()) {
            stats.add(new GameStatistics(result.getPlayerName(), result.getFiguresMappedNum(), result.getPlayedTime(), result.getEndTime()));
        }

        var message = new TopPlayersMessage(stats);
        var byteArr = (new MessageEncoder()).encode(message).array();

        httpExchange.sendResponseHeaders(200, byteArr.length);
        outputStream.write(byteArr);
        outputStream.flush();
        outputStream.close();
    }

    private String getRequestParams(HttpExchange httpExchange) {
        return httpExchange.
                getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("=")[1];
    }
}

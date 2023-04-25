package com.jigsaw.jigsaw.client;

import com.jigsaw.jigsaw.client.web.ServerConnection;
import com.jigsaw.jigsaw.endpoint.shared.FigureInfo;
import com.jigsaw.jigsaw.endpoint.shared.GameSettings;

public class SharedComponents {
    public static ServerConnection server = new ServerConnection();
    public static GameSettings settings;
    public static FigureInfo firstFigure;
}

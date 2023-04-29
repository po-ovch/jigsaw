package com.jigsaw.jigsaw.client;

import com.jigsaw.jigsaw.client.webControllers.ServerConnection;
import com.jigsaw.jigsaw.shared.entities.FigureInfo;
import com.jigsaw.jigsaw.shared.entities.GameSettings;

public class SharedComponents {
    public static ServerConnection server = new ServerConnection();
    public static GameSettings settings;
    public static FigureInfo firstFigure;
}

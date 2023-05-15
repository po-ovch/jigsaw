package com.jigsaw.client;

import com.jigsaw.client.webControllers.ServerConnection;
import com.jigsaw.shared.entities.FigureInfo;
import com.jigsaw.shared.entities.GameSettings;

public class SharedComponents {
    public static ServerConnection server = new ServerConnection();
    public static GameSettings settings;
    public static FigureInfo firstFigure;
}

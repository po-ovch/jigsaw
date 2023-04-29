package com.jigsaw.jigsaw.client;

import com.jigsaw.jigsaw.client.web.ServerConnection;
import com.jigsaw.jigsaw.shared.entites.FigureInfo;
import com.jigsaw.jigsaw.shared.entites.GameSettings;

public class SharedComponents {
    public static ServerConnection server = new ServerConnection();
    public static GameSettings settings;
    public static FigureInfo firstFigure;
}

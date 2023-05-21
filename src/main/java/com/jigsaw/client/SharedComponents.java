package com.jigsaw.client;

import com.jigsaw.client.viewControllers.ViewManager;
import com.jigsaw.client.webControllers.ServerConnection;
import com.jigsaw.shared.entities.FigureInfo;
import com.jigsaw.shared.entities.GameSettings;
import com.jigsaw.shared.entities.Result;

public class SharedComponents {
    public static ServerConnection server;
    public static ViewManager viewManager;

    public static GameSettings settings;
    public static FigureInfo firstFigure;
    public static Result result;
}

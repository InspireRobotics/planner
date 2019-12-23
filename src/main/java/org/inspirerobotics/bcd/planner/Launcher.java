package org.inspirerobotics.bcd.planner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The first class called by the JVM -
 *
 * Responsible for setting up loggers, the gui, etc.
 */
public class Launcher extends Application {

    public static final String VERSION = "0.1.0";
    private Gui gui;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        try {
            Images.load();
        } catch(IOException e) {
            System.out.println("Failed to load field image!");
            e.printStackTrace();

            Platform.exit();
        }
    }

    @Override
    public void start(Stage stage) {
        gui = new Gui(stage);
    }

    @Override
    public void stop() {
        gui.stop();
    }
}

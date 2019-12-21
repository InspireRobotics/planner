package org.inspirerobotics.bcd.planner;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The first class called by the JVM -
 *
 * Responsible for setting up loggers, the gui, etc.
 */
public class Launcher extends Application {

    private Gui gui;

    public static void main(String[] args) {
        launch(args);
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

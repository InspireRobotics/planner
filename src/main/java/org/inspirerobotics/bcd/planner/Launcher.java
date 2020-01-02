package org.inspirerobotics.bcd.planner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.inspirerobotics.bcd.planner.ui.Gui;

import java.io.IOException;

/**
 * The first class called by the JVM -
 *
 * Responsible for setting up loggers, the gui, etc.
 */
public class Launcher extends Application {

    public static final String VERSION = "0.1.0";
    private Gui gui;
    private boolean error = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() {
        try {
            Images.load();
        } catch(IOException e) {
            this.error = true;
            String error = "Failed to load field image!";
            System.out.println(error);

            Gui.showError(error, e, Platform::exit);
        }
    }

    @Override
    public void start(Stage stage) {
        if(error)
            return;

        Thread.setDefaultUncaughtExceptionHandler((t, exception) ->
                Gui.showError("Unexpected Error", new RuntimeException(exception)));

        try{
            gui = new Gui(stage);
        }catch(Exception e){
            Gui.showError("Failed to create gui!", e, Platform::exit);
        }
    }

    @Override
    public void stop() {
        gui.stop();
    }
}

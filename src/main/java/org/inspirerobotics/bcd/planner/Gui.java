package org.inspirerobotics.bcd.planner;

import javafx.stage.Stage;

/**
 * Main class for responsible for creating/handling the GUI
 *
 * This is created from the Launcher class after JavaFX is initialized
 */
public class Gui {

    private final Stage stage;
    private final MainScene scene;

    public Gui(Stage stage) {
        this.stage = stage;
        this.scene = new MainScene();

        initStageSettings(stage);

        this.stage.setScene(scene.getScene());
        this.stage.show();
    }

    private void initStageSettings(Stage stage) {
        stage.setTitle("BCD Planner");
        stage.setResizable(true);

        stage.setWidth(600);
        stage.setHeight(400);
    }

    public void stop() {

    }
}

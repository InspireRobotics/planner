package org.inspirerobotics.bcd.planner;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

/**
 * Contains all of the gui components for the planning scene
 */
public class MainScene {

    private final BorderPane borderPane;
    private final CurvePane curvePane;
    private final Scene scene;

    public MainScene(Gui gui) {
        this.borderPane = new BorderPane();
        this.curvePane = new CurvePane(gui);

        this.borderPane.setCenter(new FieldPane(gui).wrap());
        this.borderPane.setRight(curvePane);

        this.scene = new Scene(borderPane);
    }

    public CurvePane getCurvePane() {
        return curvePane;
    }

    public Scene getScene() {
        return scene;
    }
}

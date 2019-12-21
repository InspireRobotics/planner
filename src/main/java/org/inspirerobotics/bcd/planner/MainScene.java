package org.inspirerobotics.bcd.planner;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 * Contains all of the gui components for the planning scene
 */
public class MainScene {

    private final BorderPane borderPane;
    private final Scene scene;

    public MainScene() {
        this.borderPane = new BorderPane();
        this.borderPane.setStyle("-fx-background-color:black");
        this.borderPane.setPadding(new Insets(25));
        this.borderPane.setCenter(AnchorUtils.wrap(new Button("Test")));

        this.scene = new Scene(borderPane);
    }

    public Scene getScene() {
        return scene;
    }
}
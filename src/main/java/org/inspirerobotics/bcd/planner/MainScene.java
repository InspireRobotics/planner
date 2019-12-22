package org.inspirerobotics.bcd.planner;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Contains all of the gui components for the planning scene
 */
public class MainScene {

    private final Scene scene;
    private final Gui gui;

    public MainScene(Gui gui) {
        this.gui = gui;

        SplitPane splitPane = new SplitPane();

        ButtonBar buttonBar = createButtonBar();
        CurvePane curvePane = new CurvePane(gui);

        VBox.setVgrow(curvePane, Priority.ALWAYS);
        VBox vbox = new VBox(curvePane, buttonBar);
        vbox.setMinWidth(250);
        vbox.setMaxWidth(450);
        vbox.setStyle("-fx-background-color:gray");

        AnchorPane anchorPane = new FieldPane(gui).wrap();

        splitPane.getItems().add(anchorPane);
        splitPane.getItems().add(vbox);

        this.scene = new Scene(splitPane);
    }

    private ButtonBar createButtonBar() {
        ButtonBar bar = new ButtonBar();
        bar.setPadding(new Insets(5));

        Button button = new Button("Add Curve");
        button.setOnAction(this::addCurve);
        bar.getButtons().addAll(button);

        bar.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        ButtonBar.setButtonData(button, ButtonBar.ButtonData.LEFT);

        return bar;
    }

    private void addCurve(ActionEvent actionEvent) {
        gui.getCurves().add(new QBezierCurve());
        gui.getCurves().stream().map(QBezierCurve::getStart).forEach(System.out::println);
    }

    public Scene getScene() {
        return scene;
    }
}

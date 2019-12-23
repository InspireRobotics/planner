package org.inspirerobotics.bcd.planner;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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

        BorderPane borderPane = new BorderPane();
        MenuBar menuBar = createMenuBar();
        SplitPane splitPane = createCenterPane(gui);

        borderPane.setTop(menuBar);
        borderPane.setCenter(splitPane);

        this.scene = new Scene(borderPane);
    }

    private SplitPane createCenterPane(Gui gui) {
        SplitPane splitPane = new SplitPane();

        ToolBar buttonBar = createButtonBar();
        CurvePane curvePane = new CurvePane(gui);

        VBox.setVgrow(curvePane, Priority.ALWAYS);
        VBox vbox = new VBox(curvePane, buttonBar);
        vbox.setMinWidth(250);
        vbox.setMaxWidth(450);
        vbox.setStyle("-fx-background-color:gray");

        AnchorPane anchorPane = new FieldPane(gui).wrap();

        splitPane.getItems().add(anchorPane);
        splitPane.getItems().add(vbox);
        return splitPane;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu optionsMenu = new Menu("Options");

        MenuItem about = new MenuItem("About");
        about.setOnAction(event-> new AboutWindow());
        MenuItem quit = new MenuItem("Quit");
        quit.setOnAction(event -> Platform.exit());

        optionsMenu.getItems().addAll(about, quit);
        menuBar.getMenus().add(optionsMenu);
        return menuBar;
    }

    private ToolBar createButtonBar() {
        ToolBar bar = new ToolBar();
        bar.setPadding(new Insets(5));

        Button addCurve = new Button("Add Curve");
        addCurve.setOnAction(this::addCurve);

        Button startSimulation = new Button("Start Simulation");
        startSimulation.setOnAction(this::startSimulation);

        bar.getItems().addAll(addCurve, startSimulation);

        return bar;
    }

    private void startSimulation(ActionEvent actionEvent) {
        gui.getSimulation().start();
    }

    private void addCurve(ActionEvent actionEvent) {
        gui.getCurves().add(new QBezierCurve());
        gui.getCurves().stream().map(QBezierCurve::getStart).forEach(System.out::println);
    }

    public Scene getScene() {
        return scene;
    }
}

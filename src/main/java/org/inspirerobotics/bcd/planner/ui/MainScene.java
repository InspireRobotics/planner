package org.inspirerobotics.bcd.planner.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.inspirerobotics.bcd.planner.curve.QBezierCurve;

/**
 * Contains all of the gui components for the planning scene
 */
public class MainScene {

    private final Scene scene;
    private final Gui gui;

    private CurvePane curvePane;

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
        curvePane = new CurvePane(gui);

        VBox.setVgrow(curvePane, Priority.ALWAYS);
        VBox vbox = new VBox(curvePane, buttonBar);
        vbox.setMinWidth(300);
        vbox.setMaxWidth(500);
        vbox.setStyle("-fx-background-color:gray");

        AnchorPane anchorPane = new FieldPane(gui).wrap();

        splitPane.getItems().add(anchorPane);
        splitPane.getItems().add(vbox);
        return splitPane;
    }

    private MenuBar createMenuBar() {
        var menuBar = new MenuBar();
        var fileMenu = createFileMenu();
        var aboutMenu = createAboutMenu();

        menuBar.getMenus().addAll(fileMenu, aboutMenu);
        return menuBar;
    }

    private Menu createAboutMenu() {
        Menu menu = new Menu("About");

        MenuItem about = new MenuItem("About");
        about.setOnAction(event-> new AboutWindow());

        MenuItem quit = new MenuItem("Quit");
        quit.setOnAction(event -> Platform.exit());

        menu.getItems().addAll(about, quit);
        return menu;
    }

    private Menu createFileMenu() {
        Menu menu = new Menu("File");

        MenuItem new_ = new MenuItem("New");
        new_.setOnAction(event -> gui.startNewProject());

        MenuItem open = new MenuItem("Open");
        open.setOnAction(this::openFile);

        MenuItem save = new MenuItem("Save");
        save.setOnAction(this::save);

        MenuItem saveAs = new MenuItem("Save As");
        saveAs.setOnAction(this::saveAs);

        menu.getItems().addAll(new_, open, save, saveAs);

        return menu;
    }

    private void save(ActionEvent actionEvent) {
        if(gui.getCurrentFile() == null){
            saveAs(actionEvent);
            return;
        }

        gui.save(gui.getCurrentFile());
    }

    private void openFile(ActionEvent actionEvent) {
        var extensionFilter = new FileChooser.ExtensionFilter("BCD Save File", "*.json");
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("BCD: Open");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setInitialFileName("save.json");

        gui.open(fileChooser.showOpenDialog(gui.getStage()));
    }

    private void saveAs(ActionEvent actionEvent) {
        var extensionFilter = new FileChooser.ExtensionFilter("BCD Save File", "*.json");
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle("BCD: Save As");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setInitialFileName("save.json");

        gui.save(fileChooser.showSaveDialog(gui.getStage()));
    }

    private ToolBar createButtonBar() {
        ToolBar bar = new ToolBar();
        bar.setPadding(new Insets(5));

        Button addCurve = new Button("Add Curve");
        addCurve.setOnAction(this::addCurve);

        Button removeCurve = new Button("Remove Curve");
        removeCurve.setOnAction(this::removeCurve);

        Button startSimulation = new Button("Start Simulation");
        startSimulation.setOnAction(this::startSimulation);

        Button findArc = new Button("Find Arc");
        findArc.setOnAction(this::findArc);

        bar.getItems().addAll(addCurve, removeCurve, startSimulation, findArc);

        return bar;
    }

    private void removeCurve(ActionEvent actionEvent) {
        String context = "Would you like to delete " + curvePane.getCurrentCurve().getName() + "?";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, context, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Delete?");
        alert.showAndWait();

        if(alert.getResult() == ButtonType.YES){
            gui.getCurves().remove(curvePane.getCurrentCurve());
        }
    }

    private void findArc(ActionEvent actionEvent) {
        if(curvePane.getCurrentCurve() != null){
            new ArcLengthWindow(curvePane.getCurrentCurve());
        }
    }

    private void startSimulation(ActionEvent actionEvent) {
        gui.getSimulation().start();
    }

    private void addCurve(ActionEvent actionEvent) {
        gui.getCurves().add(gui.createDefaultCurve());
        gui.getCurves().stream().map(QBezierCurve::getStart).forEach(System.out::println);
    }

    public CurvePane getCurvePane() {
        return curvePane;
    }

    public Scene getScene() {
        return scene;
    }
}

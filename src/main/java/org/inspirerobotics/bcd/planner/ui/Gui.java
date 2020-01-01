package org.inspirerobotics.bcd.planner.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.stage.Stage;
import org.inspirerobotics.bcd.planner.Images;
import org.inspirerobotics.bcd.planner.Launcher;
import org.inspirerobotics.bcd.planner.curve.CurvesIO;
import org.inspirerobotics.bcd.planner.curve.QBezierCurve;
import org.inspirerobotics.bcd.planner.curve.Simulation;

import java.io.File;
import java.util.stream.Collectors;

/**
 * Main class for responsible for creating/handling the GUI
 *
 * This is created from the Launcher class after JavaFX is initialized
 */
public class Gui {

    private final Stage stage;
    private final MainScene scene;
    private final Simulation simulation;

    private ObservableList<QBezierCurve> curves;

    public Gui(Stage stage) {
        this.stage = stage;
        this.curves = FXCollections.observableArrayList();
        this.simulation = new Simulation(this);
        this.scene = new MainScene(this);

        initStageSettings(stage);
        addTestCurves();

        this.stage.setScene(scene.getScene());
        this.stage.show();

        GuiUtils.createTimer(simulation::tick).start();
    }

    private void initStageSettings(Stage stage) {
        stage.setTitle("BCD Planner: " + Launcher.VERSION);
        stage.setResizable(true);
        stage.getIcons().add(Images.getIcon());
        stage.setOnCloseRequest(event -> Platform.exit());

        stage.setWidth(900);
        stage.setHeight(700);
    }

    public void stop() {

    }

    private void addTestCurves() {
        QBezierCurve testCurve = new QBezierCurve();
        testCurve.setStart(new Point2D(1.2, 5));
        testCurve.setControlPoint(new Point2D(12, 5));
        testCurve.setEnd(new Point2D(12, 12));

        QBezierCurve testCurve2 = new QBezierCurve();
        testCurve2.setStart(new Point2D(12, 12));
        testCurve2.setControlPoint(new Point2D(12, 23));
        testCurve2.setEnd(new Point2D(18, 26.5));

        curves.add(testCurve);
        curves.add(testCurve2);
    }

    public void save(File file) {
        if(file == null)
            return;

        var clonedCurves = curves.stream().map(QBezierCurve::copy).collect(Collectors.toList());

        new Thread(() -> CurvesIO.save(file, clonedCurves)).start();

    }

    public void open(File file) {
        if(file == null)
            return;

        new Thread(() -> CurvesIO.open(file, this)).start();
    }

    public MainScene getScene() {
        return scene;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public Stage getStage() {
        return stage;
    }

    public ObservableList<QBezierCurve> getCurves() {
        return curves;
    }
}

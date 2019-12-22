package org.inspirerobotics.bcd.planner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.stage.Stage;

/**
 * Main class for responsible for creating/handling the GUI
 *
 * This is created from the Launcher class after JavaFX is initialized
 */
public class Gui {

    private final Stage stage;
    private final MainScene scene;

    private ObservableList<QBezierCurve> curves = FXCollections.observableArrayList();

    public Gui(Stage stage) {
        this.stage = stage;
        this.scene = new MainScene(this);

        initStageSettings(stage);
        addTestCurves();

        this.stage.setScene(scene.getScene());
        this.stage.show();
    }

    private void initStageSettings(Stage stage) {
        stage.setTitle("BCD Planner");
        stage.setResizable(true);

        stage.setWidth(800);
        stage.setHeight(500);
    }

    public void stop() {

    }

    private void addTestCurves() {
        QBezierCurve testCurve = new QBezierCurve();
        testCurve.setStart(new Point2D(25, 100));
        testCurve.setControlPoint(new Point2D(300, 75));
        testCurve.setEnd(new Point2D(300, 400));

        QBezierCurve testCurve2 = new QBezierCurve();
        testCurve2.setStart(new Point2D(300, 400));
        testCurve2.setControlPoint(new Point2D(300, 540));
        testCurve2.setEnd(new Point2D(370, 570));

        curves.add(testCurve);
        curves.add(testCurve2);
    }

    public ObservableList<QBezierCurve> getCurves() {
        return curves;
    }
}

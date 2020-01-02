package org.inspirerobotics.bcd.planner.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.inspirerobotics.bcd.planner.Images;
import org.inspirerobotics.bcd.planner.Launcher;
import org.inspirerobotics.bcd.planner.curve.CurvesIO;
import org.inspirerobotics.bcd.planner.curve.QBezierCurve;
import org.inspirerobotics.bcd.planner.curve.Simulation;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalTime;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Main class for responsible for creating/handling the GUI
 *
 * This is created from the Launcher class after JavaFX is initialized
 */
public class Gui {

    private static final String TITLE = "BCD Planner: " + Launcher.VERSION;

    private final Stage stage;
    private final MainScene scene;
    private final Simulation simulation;

    private ObservableList<QBezierCurve> curves;
    private File currentFile;

    public Gui(Stage stage) {
        this.stage = stage;
        this.curves = FXCollections.observableArrayList();
        this.simulation = new Simulation(this);
        this.scene = new MainScene(this);

        initStageSettings(stage);
        startNewProject();

        this.stage.setScene(scene.getScene());
        this.stage.show();

        GuiUtils.createTimer(simulation::tick).start();
    }

    private void initStageSettings(Stage stage) {
        stage.setTitle(TITLE);
        stage.setResizable(true);
        stage.getIcons().add(Images.getIcon());
        stage.setOnCloseRequest(event -> Platform.exit());

        stage.setWidth(900);
        stage.setHeight(700);
    }

    public void stop() {

    }

    public void save(File file) {
        if(file == null)
            return;

        currentFile = file;
        var clonedCurves = curves.stream().map(QBezierCurve::copy).collect(Collectors.toList());

        new Thread(() -> CurvesIO.save(file, clonedCurves)).start();

        updateTitle();
    }

    private void updateTitle() {
        String title = TITLE + ": Saved ";
        LocalTime time = LocalTime.now().withNano(0);

        stage.setTitle(title + time);
    }

    public void startNewProject(){
        QBezierCurve.resetCount();
        QBezierCurve curve = createDefaultCurve();

        currentFile = null;
        curves.setAll(curve);
    }

    QBezierCurve createDefaultCurve() {
        Random rand = new Random();
        QBezierCurve curve = new QBezierCurve();
        var color = Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
        var offsetX = rand.nextDouble() * 15;
        var offsetY = rand.nextDouble() * 15;

        curve.setStart(new Point2D(offsetX, offsetY));
        curve.setControlPoint(new Point2D(5 + offsetX, 5 + offsetY));
        curve.setEnd(new Point2D(10 + offsetX, 10 + offsetY));
        curve.setColor(color);

        return curve;
    }

    public void open(File file) {
        if(file == null)
            return;

        currentFile = file;
        new Thread(() -> CurvesIO.open(file, this)).start();
    }

    public static void showError(String title, Exception e){
        showError(title, e, () -> {});
    }

    public static void showError(String title, Exception e, Runnable onClose){
        if(!Platform.isFxApplicationThread()){
            Platform.runLater(() -> showError(title, e, onClose));
            return;
        }

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error!");
        alert.setHeaderText(title);

        addExceptionToDialog(e, alert);

        alert.show();
        alert.setOnCloseRequest(event -> onClose.run());
    }

    private static void addExceptionToDialog(Exception e, Alert alert) {
        TextArea textArea = new TextArea(stacktraceToString(e));
        textArea.setMinWidth(650);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        alert.getDialogPane().setExpandableContent(textArea);
        alert.getDialogPane().setExpanded(true);
    }

    private static String stacktraceToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString().replaceAll("org.inspirerobotics.bcd.planner/", "");
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

    public File getCurrentFile() {
        return currentFile;
    }
}

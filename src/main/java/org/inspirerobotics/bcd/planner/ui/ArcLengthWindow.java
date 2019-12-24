package org.inspirerobotics.bcd.planner.ui;

import javafx.concurrent.Service;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.inspirerobotics.bcd.planner.Images;
import org.inspirerobotics.bcd.planner.curve.*;

/**
 * The window for testing arc lengths.
 * This class also contains the code to create the GUI for the window
 */
public class ArcLengthWindow {

    private final QBezierCurve curve;

    public ArcLengthWindow(QBezierCurve curve) {
        this.curve = curve;

        Stage stage = new Stage();
        Scene scene = createScene();

        initStageSettings(stage);
        stage.setScene(scene);

        stage.show();
    }

    private Scene createScene() {
        VBox vbox = new VBox();
        Node titleNode = createTitleLabel();

        Node bruteForceNodeQuick = createSolverNode(new BruteForceArcSolver(.01));
        Node bruteForceNodeSlow = createSolverNode(new BruteForceArcSolver(.0001));
        Node controlPolygon = createSolverNode(new ControlPolygonSolver());
        Node integrationSolver = createSolverNode(new IntegrationSolver());

        vbox.setPadding(new Insets(15));
        vbox.setSpacing(15);
        vbox.getChildren().addAll(titleNode, bruteForceNodeQuick, bruteForceNodeSlow,
                controlPolygon, integrationSolver);
        return new Scene(vbox);
    }

    private Node createSolverNode(ArcLengthSolver bruteForceSolver) {
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color:#EBEAEA");
        vBox.setPadding(new Insets(5));

        Label title = new Label(bruteForceSolver.getName());
        title.setFont(Font.font(16));

        ProgressBar bar = new ProgressBar();
        bar.setProgress(0); 
        Service<ArcLengthSolver.Result> service = ArcLengthTask.createService(curve, bruteForceSolver, bar);

        Button button = createTaskButton(service);
        Label result = createResultPane(service);

        vBox.getChildren().addAll(title, bar, button, result);
        return vBox;
    }

    private Label createResultPane(Service<ArcLengthSolver.Result> service) {
        Label label = new Label("Result: ");
        service.setOnSucceeded(event -> {
            ArcLengthSolver.Result result = service.getValue();
            label.setText(String.format("Result (time=%2.3f ms, avg=%3.5f)", result.time, result.avgResult));
            service.reset();
        });

        return label;
    }

    private Button createTaskButton(Service<ArcLengthSolver.Result> service) {
        Button startTest = new Button("Start");
        startTest.setOnAction(event -> {
            if(!service.isRunning()){
                startTest.setText("Restart");
                service.start();
            }else{
                service.cancel();
                service.restart();
            }
        });

        return startTest;
    }

    private Node createTitleLabel() {
        Label title = new Label("Arc Length: " + curve.getName());
        title.setFont(Font.font(25));

        Label subtitle = new Label("Curves per test:  " + ArcLengthTask.COUNT);
        title.setFont(Font.font(16));

        return new VBox(title, subtitle);
    }

    private void initStageSettings(Stage stage) {
        stage.setTitle("Arc Curve Length Tester");
        stage.getIcons().add(Images.getIcon());
        stage.setResizable(false);

        stage.setWidth(575);
        stage.setHeight(600);
    }
}

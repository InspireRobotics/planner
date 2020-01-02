package org.inspirerobotics.bcd.planner.ui;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.NonInvertibleTransformException;
import org.inspirerobotics.bcd.planner.Images;
import org.inspirerobotics.bcd.planner.curve.QBezierCurve;
import org.inspirerobotics.bcd.planner.curve.Simulation;

import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * The Main component of the GUI - it draws the curves and the picture of the field
 *
 * Note: This canvas is in pixels, so when drawing,
 * all points must be converted from feet to pixels.
 *
 * Unit Constant: {@link Images#PIXELS_PER_FOOT}
 */
public class FieldPane extends Canvas{

    private static final int TRANSLATE_SPEED = 5;
    private final HashSet<KeyCode> keysDown = new LinkedHashSet<>();
    private final Gui gui;

    public FieldPane(Gui gui) {
        this.gui = gui;

        this.getGraphicsContext2D().scale(.75, .75);
        this.setFocusTraversable(true);

        this.widthProperty().addListener(e -> draw());
        this.heightProperty().addListener(e -> draw());

        this.setOnScroll(this::onScroll);
        this.setOnKeyPressed(this::onKeyPressed);
        this.setOnKeyReleased(this::onKeyReleased);
        this.setOnMouseClicked(this::onClick);

        this.setOnMouseDragged(this::onDrag);

        GuiUtils.createTimer(this::animate).start();
    }

    private void onDrag(MouseEvent mouseEvent) {
        double clickRadius = 1.5;//Note: This is feet because the click gets converted into feet later

        Point2D click = new Point2D(mouseEvent.getX(), mouseEvent.getY());
        try {
            click = getGraphicsContext2D().getTransform().inverseTransform(click);
        } catch(NonInvertibleTransformException e) {
            e.printStackTrace();
        }

        click = click.multiply(1.0 / Images.PIXELS_PER_FOOT);

        for(QBezierCurve curve : gui.getCurves()){
            if(curve.getStart().distance(click) < clickRadius){
                curve.setStart(click);
                gui.getScene().getCurvePane().syncCurveBoxes();
                return;
            }

            if(curve.getControlPoint().distance(click) < clickRadius){
                curve.setControlPoint(click);
                gui.getScene().getCurvePane().syncCurveBoxes();
                return;
            }

            if(curve.getEnd().distance(click) < clickRadius){
                curve.setEnd(click);
                gui.getScene().getCurvePane().syncCurveBoxes();
                return;
            }
        }
    }

    private void onClick(MouseEvent mouseEvent) {
        this.requestFocus();
    }

    private void animate(long time) {
        updateTranslation();

        draw();
    }

    private void updateTranslation() {
        if(keysDown.contains(KeyCode.W)){
            getGraphicsContext2D().translate(0, TRANSLATE_SPEED);
        }else if(keysDown.contains(KeyCode.S)){
            getGraphicsContext2D().translate(0, -TRANSLATE_SPEED);
        }

        if(keysDown.contains(KeyCode.A)){
            getGraphicsContext2D().translate(TRANSLATE_SPEED, 0);
        } else if(keysDown.contains(KeyCode.D)){
            getGraphicsContext2D().translate(-TRANSLATE_SPEED, 0);
        }
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        keysDown.remove(keyEvent.getCode());
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.isShiftDown() || keyEvent.isControlDown())
            return;
        keysDown.add(keyEvent.getCode());
    }

    private void onScroll(ScrollEvent event) {
        if(event.getDeltaY() > 0){
            getGraphicsContext2D().scale(1.1, 1.1);
        }else if(event.getDeltaY() < 0){
            getGraphicsContext2D().scale(.9, .9);
        }

        draw();
    }

    private void draw(){
        GraphicsContext g = getGraphicsContext2D();
        g.clearRect(-10000, -10000, 20000, 200000);

        g.drawImage(Images.getFieldImage(), 0, 0);
        drawCurves(g);

        drawSimulation(g, gui.getSimulation());
        drawControls(g);
    }

    private void drawControls(GraphicsContext g) {
        String helpText = "WASD To Move, Scroll to zoom";
        g.setFill(Color.BLACK);
        g.setFont(Font.font(18));
        g.fillText(helpText, 10, Images.getFieldImage().getHeight() + 25);
    }

    private void drawSimulation(GraphicsContext g, Simulation simulation) {
        if(simulation == null || simulation.getRobotPos() == null)
            return;

        if(simulation.isRunning()){
            drawRobot(g, pointToPixel(simulation.getRobotPos()), simulation.getAngle());
            drawPoint(g, pointToPixel(simulation.getRobotPos()), Color.SPRINGGREEN);
            drawPoint(g, pointToPixel(simulation.getCurrentPoint()), Color.BLUE);
        }

        drawSimulationInfo(g, simulation);
    }

    private void drawRobot(GraphicsContext g, Point2D point, double angle) {
        g.save();

        //Translate so that the rotation is on the center of the rectangle
        g.translate(point.getX(), point.getY());
        g.rotate(angle * 180 / Math.PI);
        g.translate(-point.getX(), -point.getY());


        double width = 50;
        double height = 25;

        g.setFill(Color.YELLOW);
        g.fillRect(point.getX() - (width / 2), point.getY() - (height / 2), width, height);

        g.restore();
    }


    private void drawSimulationInfo(GraphicsContext g, Simulation simulation) {
        g.save();

        //Ignore current scale and transform because we want
        //to keep this always in lower left hand corner
        g.setTransform(1, 0, 0, 1, 0, 0);


        g.setFill(Color.BLACK);
        g.fillRect(0, getHeight() - 82, 152, 84);
        g.setFill(Color.LIGHTGRAY);
        g.fillRect(0, getHeight() - 80, 150, 80);

        g.setFill(Color.BLACK);
        g.setFont(Font.font(18));

        String angle = simulation.isRunning() ?
                String.format("Angle: %2.2f", -simulation.getAngle() * 180 / Math.PI) : "";
        String totalTime = String.format("Time: %2.2fs", simulation.getTimeRan() / 1000.0);

        g.fillText(angle, 5, getHeight() - 10);
        g.fillText(totalTime, 5, getHeight() - 35);
        g.fillText("Curve: " + simulation.getCurrentCurve(), 5, getHeight() - 60);

        g.restore();
    }

    private void drawCurves(GraphicsContext g) {
        gui.getCurves().forEach(curve -> {
            drawCurve(g, curve);
            drawPoint(g, pointToPixel(curve.getStart()), curve.getColor());
            drawPoint(g, pointToPixel(curve.getControlPoint()), curve.getColor());
            drawPoint(g, pointToPixel(curve.getEnd()), curve.getColor());
        });
    }

    private Point2D pointToPixel(Point2D point){
        return point.multiply(Images.PIXELS_PER_FOOT);
    }

    private void drawPoint(GraphicsContext g, Point2D point, Color color) {
        int radius = 8;
        point = point.subtract(radius, radius);

        g.setFill(color);
        g.fillOval(point.getX(), point.getY(), radius * 2, radius * 2);
    }

    private void drawCurve(GraphicsContext g, QBezierCurve curve) {
        Point2D start = pointToPixel(curve.getStart());
        Point2D control = pointToPixel(curve.getControlPoint());
        Point2D end = pointToPixel(curve.getEnd());

        g.setLineWidth(4);
        g.setStroke(curve.getColor());

        g.beginPath();
        g.moveTo(start.getX(), start.getY());
        g.quadraticCurveTo(control.getX(), control.getY(), end.getX(), end.getY());
        g.stroke();
    }

    public AnchorPane wrap(){
        final Canvas canvas = this;
        AnchorPane pane = GuiUtils.anchor(this);

        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        pane.setMinSize(0, 0);

        return pane;
    }
}

package org.inspirerobotics.bcd.planner;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * The Main component of the GUI - it draws the curves and the picture of the field
 */
public class FieldPane extends Canvas{

    private static final int TRANSLATE_SPEED = 5;
    private final HashSet<KeyCode> keysDown = new LinkedHashSet<>();
    private List<QBezierCurve> curves = new ArrayList<>();

    public FieldPane() {
        draw();

        addTestCurves();

        //Allows key events to trigger while not typing
        this.setFocusTraversable(true);

        this.widthProperty().addListener(e -> draw());
        this.heightProperty().addListener(e -> draw());

        this.setOnScroll(this::onScroll);
        this.setOnKeyPressed(this::onKeyPressed);
        this.setOnKeyReleased(this::onKeyReleased);

        GuiUtils.createTimer(this::animate).start();
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

        g.drawImage(FieldImage.getImage(), 0, 0);
        drawCurves(g);
    }

    private void drawCurves(GraphicsContext g) {
        curves.forEach(curve -> {
            drawCurve(g, curve);
            drawPoint(g, curve.getStart(), curve.getColor());
            drawPoint(g, curve.getControlPoint(), curve.getColor());
            drawPoint(g, curve.getEnd(), curve.getColor());
        });
    }

    private void drawPoint(GraphicsContext g, Point2D point, Color color) {
        int radius = 8;
        point = point.subtract(radius, radius);

        g.setFill(color);
        g.fillOval(point.getX(), point.getY(), radius * 2, radius * 2);
    }

    private void drawCurve(GraphicsContext g, QBezierCurve curve) {
        Point2D start = curve.getStart();
        Point2D control = curve.getControlPoint();
        Point2D end = curve.getEnd();

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

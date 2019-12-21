package org.inspirerobotics.bcd.planner;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 * The Main component of the GUI - it draws the curves and the picture of the field
 */
public class FieldPane extends Canvas{

    public FieldPane() {
        draw();

        this.widthProperty().addListener(e -> draw());
        this.heightProperty().addListener(e -> draw());
    }

    private void draw(){
        GraphicsContext g = getGraphicsContext2D();
        g.clearRect(0, 0, getWidth(), getHeight());

        g.setFill(Color.GREEN);
        g.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 10, 10);
        g.save();
    }

    public AnchorPane wrap(){
        final Canvas canvas = this;
        AnchorPane pane = AnchorUtils.wrap(this);

        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        pane.setMinSize(0, 0);

        return pane;
    }
}

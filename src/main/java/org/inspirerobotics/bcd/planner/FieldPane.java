package org.inspirerobotics.bcd.planner;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

/**
 * The Main component of the GUI - it draws the curves and the picture of the field
 */
public class FieldPane extends Canvas{


    public FieldPane() {
        draw();

        //Allows key events to trigger while not typing
        this.setFocusTraversable(true);

        this.widthProperty().addListener(e -> draw());
        this.heightProperty().addListener(e -> draw());

        this.setOnScroll(this::onScroll);
        this.setOnKeyPressed(this::onKeyPressed);
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode() == KeyCode.A){
            getGraphicsContext2D().translate(20, 0);
        }else if(keyEvent.getCode() == KeyCode.D){
            getGraphicsContext2D().translate(-20, 0);
        }

        if(keyEvent.getCode() == KeyCode.W){
            getGraphicsContext2D().translate(0, 20);
        }else if(keyEvent.getCode() == KeyCode.S){
            getGraphicsContext2D().translate(0, -20);
        }

        draw();
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

        g.setFill(Color.GREEN);
        g.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 10, 10);
        g.drawImage(FieldImage.getImage(), 0, 0);
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

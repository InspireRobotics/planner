package org.inspirerobotics.bcd.planner.ui;

import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.util.function.Consumer;

/**
 * Contains different utility functions
 */
public class GuiUtils {

    /**
     * Creates an anchor pane with the node as its only child, and sets the Node to
     * have a 0.0 anchor in all directions
     *
     * @return an anchor pane with the node as a child
     */
    public static AnchorPane anchor(Node node){
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);

        return new AnchorPane(node);
    }

    /**
     * Creates a timer that calls the passed handler once per frame.
     *
     * Note: This function does <b>NOT</b> start the timer.
     */
    public static AnimationTimer createTimer(Consumer<Long> handler){
        return new AnimationTimer() {
            @Override
            public void handle(long now) {
                handler.accept(now);
            }
        };
    }
}

package org.inspirerobotics.bcd.planner;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class AnchorUtils {

    /**
     * Creates an anchor pane with the node as its only child, and sets the Node to
     * have a 0.0 anchor in all directions
     *
     * @return an anchor pane with the node as a child
     */
    public static AnchorPane wrap(Node node){
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);

        return new AnchorPane(node);
    }
}

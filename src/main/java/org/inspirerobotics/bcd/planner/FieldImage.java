package org.inspirerobotics.bcd.planner;

import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Responsible for loading the Field image.
 */
public class FieldImage {

    private static Image image;

    public static void load() throws IOException {
        var inputStream = FieldImage.class.getResourceAsStream("/field.png");

        if(inputStream == null) {
            throw new FileNotFoundException("Could find field image");
        }

        image = new Image(inputStream);
    }


    public static Image getImage() {
        return image;
    }
}

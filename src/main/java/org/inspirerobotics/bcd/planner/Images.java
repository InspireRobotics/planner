package org.inspirerobotics.bcd.planner;

import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Responsible for loading the images used by the application
 */
public class Images {

    private static Image fieldImage;
    private static Image icon;

    public static void load() throws IOException {
        icon = load("/icon.png");
        fieldImage = load("/field.png");
    }

    private static Image load(String path) throws IOException{
        var inputStream = Images.class.getResourceAsStream(path);

        if(inputStream == null) {
            throw new FileNotFoundException("Could find image at " + path);
        }

        return new Image(inputStream);
    }

    public static Image getFieldImage() {
        return fieldImage;
    }

    public static Image getIcon() {
        return icon;
    }
}
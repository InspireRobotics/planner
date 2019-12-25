package org.inspirerobotics.bcd.planner;

import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Responsible for loading the images used by the application
 */
public class Images {

    public static final double PIXELS_PER_FOOT = 21.49;

    private static Image fieldImage;
    private static Image icon;
    private static Image aboutLogo;

    public static void load() throws IOException {
        icon = load("/icon.png");
        fieldImage = load("/field.png");
        aboutLogo = load("/about_logo.png");
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

    public static Image getAboutLogo() {
        return aboutLogo;
    }
}

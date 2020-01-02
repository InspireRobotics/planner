package org.inspirerobotics.bcd.planner.curve;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.inspirerobotics.bcd.planner.ui.Gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Responsible for saving and loading the curves. This class is designed to be run
 * on a separate thread (apart from the UI thread).
 */
public class CurvesIO {

    public static void open(File file, Gui gui) {
        try {
            String data = Files.readString(file.toPath());

            List<QBezierCurve> curves = parseCurves(data);
            Platform.runLater(() -> gui.getCurves().setAll(curves));
        } catch(IOException | RuntimeException e) {
            Gui.showError("Failed to open the file!", e);
            e.printStackTrace();
        }
    }

    private static List<QBezierCurve> parseCurves(String data) {
        var gson = new Gson();
        var curves = new ArrayList<QBezierCurve>();
        var curvesJson = gson.fromJson(data, JsonObject[].class);

        Stream.of(curvesJson).map(CurvesIO::parseCurve).forEach(curves::add);

        return curves;
    }

    private static QBezierCurve parseCurve(JsonObject jsonObject) {
        String name = jsonObject.get("name").getAsString();
        double p0x = jsonObject.get("p0x").getAsDouble();
        double p0y = jsonObject.get("p0y").getAsDouble();
        double p1x = jsonObject.get("p1x").getAsDouble();
        double p1y = jsonObject.get("p1y").getAsDouble();
        double p2x = jsonObject.get("p2x").getAsDouble();
        double p2y = jsonObject.get("p2y").getAsDouble();
        int r = (int) (jsonObject.get("r").getAsDouble() * 255);
        int g = (int) (jsonObject.get("g").getAsDouble() * 255);
        int b = (int) (jsonObject.get("b").getAsDouble() * 255);

        QBezierCurve curve = new QBezierCurve(name);
        curve.setStart(new Point2D(p0x, p0y));
        curve.setControlPoint(new Point2D(p1x, p1y));
        curve.setEnd(new Point2D(p2x, p2y));
        curve.setColor(Color.rgb(r, g, b, 1.0));

        System.out.println(curve.getStart());

        return curve;
    }

    public static void save(File file, List<QBezierCurve> curves){
        try {
            Files.write(file.toPath(), generateJSON(curves).getBytes());
        } catch(IOException | RuntimeException e) {
            e.printStackTrace();
            Gui.showError("Failed to save the file!", e);
        }
    }

    private static String generateJSON(List<QBezierCurve> curves) {
        Gson gson = new Gson();
        JsonArray jsonCurves = new JsonArray();

        curves.forEach(c -> jsonCurves.add(toJSON(c)));

        return gson.toJson(jsonCurves);
    }

    private static JsonObject toJSON(QBezierCurve c) {
        JsonObject element = new JsonObject();

        System.out.println(c.getStart());
        element.addProperty("name", c.getName());
        element.addProperty("p0x", c.getStart().getX());
        element.addProperty("p0y", c.getStart().getY());
        element.addProperty("p1x", c.getControlPoint().getX());
        element.addProperty("p1y", c.getControlPoint().getY());
        element.addProperty("p2x", c.getEnd().getX());
        element.addProperty("p2y", c.getEnd().getY());
        element.addProperty("r", c.getColor().getRed());
        element.addProperty("g", c.getColor().getGreen());
        element.addProperty("b", c.getColor().getBlue());

        return element;
    }
}

package org.inspirerobotics.bcd.planner.ui;

import javafx.beans.Observable;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import org.inspirerobotics.bcd.planner.curve.QBezierCurve;

/**
 * The pane on the right side of the GUI that allows the
 * user to edit the curves being displayed
 */
public class CurvePane extends VBox {

    private final ChoiceBox<QBezierCurve> choices = new ChoiceBox<>();
    private QBezierCurve currentCurve;

    private TextField p0x;
    private TextField p0y;
    private TextField p1x;
    private TextField p1y;
    private TextField p2x;
    private TextField p2y;
    private ColorPicker colorPicker;

    /**
     * When this is true, the curve is not updated with the
     * current values because we are in the middle of updating the
     * text fields
     */
    private boolean updating = false;

    public CurvePane(Gui gui) {
        gui.getCurves().addListener(this::onCurveListChanged);

        choices.setConverter(new CurveStringConverter(gui));
        choices.setOnAction(this::onSelected);
        choices.setMaxWidth(Double.MAX_VALUE);

        this.setPadding(new Insets(10));
        this.setSpacing(20);
        this.getChildren().add(choices);

        addCurveBoxes();
        createColorPicker();
        syncCurveBoxes();
    }

    private void createColorPicker() {
        colorPicker = new ColorPicker();
        colorPicker.valueProperty().addListener(this::updateCurvesWithOptions);
        colorPicker.setEditable(false);

        this.getChildren().add(colorPicker);
    }

    private void onSelected(ActionEvent observable) {
        this.currentCurve = choices.getSelectionModel().getSelectedItem();

        syncCurveBoxes();
    }

    void syncCurveBoxes() {
        if(currentCurve != null){
            updating = true;
            p0x.setText(doubleToShortString(currentCurve.getStart().getX()));
            p0y.setText(doubleToShortString(currentCurve.getStart().getY()));
            p1x.setText(doubleToShortString(currentCurve.getControlPoint().getX()));
            p1y.setText(doubleToShortString(currentCurve.getControlPoint().getY()));
            p2x.setText(doubleToShortString(currentCurve.getEnd().getX()));
            p2y.setText(doubleToShortString(currentCurve.getEnd().getY()));
            colorPicker.setValue(currentCurve.getColor());
            colorPicker.setEditable(true);

            updating = false;
            updateCurvesWithOptions(null);
        }else{
            System.out.println("no curve");
            p0x.setText("");
            p0y.setText("");
            p1x.setText("");
            p1y.setText("");
            p2x.setText("");
            p2y.setText("");
            colorPicker.setValue(Color.WHITE);
            colorPicker.setEditable(false);
        }
    }

    private String doubleToShortString(double number){
        return String.format("%4.2f", number);
    }

    private void addCurveBoxes() {
        p0x = addCurveBox("P0-X");
        p0y = addCurveBox("P0-Y");
        p1x = addCurveBox("P1-X");
        p1y =  addCurveBox("P1-Y");
        p2x = addCurveBox("P2-X");
        p2y = addCurveBox("P2-Y");
    }

    private TextField addCurveBox(String name){
        HBox hBox = new HBox();
        Label label = new Label(name);
        label.setTextFill(Color.WHITE);
        TextField field = new TextField();
        field.textProperty().addListener(this::updateCurvesWithOptions);

        field.setTextFormatter(new TextFormatter<String>(this::formatNumberTextField));
        hBox.getChildren().addAll(label, field);

        hBox.setSpacing(5);
        HBox.setHgrow(field, Priority.ALWAYS);

        this.getChildren().add(hBox);

        return field;
    }

    private void updateCurvesWithOptions(Observable observable) {
        if(currentCurve == null || updating)
            return;

        if(!p0x.getText().isEmpty() && !p0y.getText().isEmpty())
            currentCurve.setStart(new Point2D(parseTextField(p0x), parseTextField(p0y)));

        if(!p1x.getText().isEmpty() && !p1y.getText().isEmpty())
            currentCurve.setControlPoint(new Point2D(parseTextField(p1x), parseTextField(p1y)));

        if(!p2x.getText().isEmpty() && !p2y.getText().isEmpty())
            currentCurve.setEnd(new Point2D(parseTextField(p2x), parseTextField(p2y)));

        currentCurve.setColor(colorPicker.getValue());
    }

    private double parseTextField(TextField field) {
        try{
            return Double.parseDouble(field.getText());
        }catch(NumberFormatException e){
            System.out.println("parse error: " + field.getText());
            return 0.0;
        }
    }

    private TextFormatter.Change formatNumberTextField(TextFormatter.Change change) {
        String newText = change.getControlNewText();

        //Allows for user to type a . without the regex going insane
        if(newText.endsWith("."))
            newText = newText.concat("0");

        if(newText.isEmpty()){
            return change;
        }

        if (newText.matches("^(-?[1-9]+\\d*([.]\\d+)?)$|^(-?0[.]\\d*[1-9]+)$|^0$|^0.0$|$[.]")
                && currentCurve != null) {
            return change;
        }

        return null;
    }

    private void onCurveListChanged(ListChangeListener.Change<? extends QBezierCurve> c) {
        while(c.next()){
            choices.getItems().removeAll(c.getRemoved());
            choices.getItems().addAll(c.getAddedSubList());
        }

        if(choices.getSelectionModel().isEmpty() && choices.getItems().size() > 0){
            choices.getSelectionModel().select(0);
        }
    }

    public QBezierCurve getCurrentCurve() {
        return currentCurve;
    }
}
class CurveStringConverter extends StringConverter<QBezierCurve>{

    private final Gui gui;

    public CurveStringConverter(Gui gui) {
        this.gui = gui;
    }

    @Override
    public String toString(QBezierCurve curve) {
        return curve.getName();
    }

    @Override
    public QBezierCurve fromString(String string) {
        return gui.getCurves().stream().filter(curve -> curve.getName().equals(string))
                .findFirst().orElse(null);
    }

}
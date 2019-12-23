package org.inspirerobotics.bcd.planner;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutWindow {

    private static final String DOC_URL = "https://docs.google.com/document/d/1k6oOVGwozWX_TjwgITE2ENiYw41exKZpt7xYZzDYwAg/edit?usp=sharing";
    private static final String DESC = "BCD Planner is a program to generate Bezier curves for different FRC Games. "
            + "BCD (Bezier Curve Drive) is a new autonomous system of driving being developed by " +
            "FRC Team 4283 (INSPIRE Robotics). It works by following Quadratic Bezier curves to make a smooth path.";

    private static Stage current;

    public AboutWindow() {
        if(current != null) {
            current.requestFocus();
            return;
        }

        current = new Stage();
        current.setScene(createScene());

        initStageSettings(current);
    }

    private Scene createScene() {
        HBox titlePane = createTitlePane();
        VBox infoPane = createInfoPane();

        return new Scene(new VBox(titlePane, infoPane));
    }

    private VBox createInfoPane() {
        Label descLabel = new Label(DESC);
        descLabel.setFont(Font.font(16));
        descLabel.setWrapText(true);

        Node repoLabel = createLinkLabel("Repository", "https://github.com/InspireRobotics/planner");
        Node docLabel = createLinkLabel("BCD Documentation", DOC_URL);

        VBox vBox = new VBox(descLabel, new Label(), docLabel, repoLabel);
        vBox.setPadding(new Insets(10));
        return vBox;
    }

    private Node createLinkLabel(String name, String link){
        Hyperlink hyperlink = new Hyperlink(name);
        hyperlink.setOnAction(event -> clickLink(link));
        hyperlink.setVisited(false);
        hyperlink.setBorder(Border.EMPTY);

        return hyperlink;
    }

    private void clickLink(String link) {
        try {
            Desktop.getDesktop().browse(new URI(link));
        } catch(URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    private HBox createTitlePane() {
        ImageView image = new ImageView(Images.getAboutLogo());
        image.setFitHeight(180);
        image.setFitWidth(180);
        VBox title = createTitleInfo();

        HBox.setHgrow(title, Priority.ALWAYS);
        HBox hBox = new HBox(image, title);
        hBox.setSpacing(20);
        hBox.setPadding(new Insets(10, 15, 10, 15));
        return hBox;
    }

    private VBox createTitleInfo() {
        Label title = new Label("BCD Planner:  " + Launcher.VERSION);
        title.setFont(Font.font(36));

        String subText = "Created by Noah Charlton\n INSPIRE Robotics #4283";
        Label subtitle = new Label(subText);
        subtitle.setFont(Font.font(18));

        VBox.setVgrow(title, Priority.ALWAYS);
        return new VBox(title, subtitle);
    }

    private void initStageSettings(Stage stage) {
        stage.setTitle("About: BCD Planner");
        stage.getIcons().add(Images.getIcon());
        stage.setResizable(false);
        stage.setIconified(false);
        stage.setOnCloseRequest(this::onClose);

        stage.setWidth(575);
        stage.setHeight(425);

        stage.show();
    }

    private void onClose(WindowEvent windowEvent) {
        current = null;
    }
}

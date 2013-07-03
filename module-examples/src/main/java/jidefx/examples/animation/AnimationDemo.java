/*
 * @(#)AnimationDemo.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.examples.animation;

import javafx.animation.PauseTransition;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import jidefx.animation.AnimationType;
import jidefx.animation.AnimationUtils;
import jidefx.examples.demo.AbstractFxDemo;

@SuppressWarnings("Convert2Lambda")
public class AnimationDemo extends AbstractFxDemo {

    private BorderPane _demoPane;
    private ListView<AnimationType> _animationTypeListView;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public String getName() {
        return "Animation Demo";
    }

    @Override
    public String getDescription() {
        return "Animation demo shows some pre-defined animations that you can use inside your application. Most of the animations are from the fxexperience project created by Jasper Potts.\n" +
                "\n" +
                "Demoed classes:\n" +
                "jidefx.animation.AnimationType\n" +
                "jidefx.animation.AnimationUtils\n";
    }

    @Override
    public Region getDemoPanel() {
        Button button = new Button("JideFX Rocks!");
        button.setStyle(
                "-fx-background-color: linear-gradient(#ff5400, #be1d00);\n" +
                        "-fx-background-radius: 10;\n" +
                        "-fx-background-insets: 0;\n" +
                        "-fx-padding: 5;\n" +
                        "-fx-text-fill: white;-fx-font-size: 24px;");

        _demoPane = new BorderPane(button);
        _demoPane.setMinSize(400, 400);
        return _demoPane;
    }

    private void bringBackAfter(Node node) {
        PauseTransition transition = new PauseTransition(Duration.seconds(1));
        transition.setOnFinished((ActionEvent t) -> {
            node.setOpacity(1);
            node.setScaleX(1);
            node.setScaleY(1);
            node.setScaleZ(1);
        });
        transition.play();
    }

    @Override
    public Region getOptionsPanel() {
        VBox optionPane = new VBox();

        _animationTypeListView = new ListView<>();
        _animationTypeListView.getItems().addAll(AnimationType.values());
        _animationTypeListView.getItems().remove(0); // remove NONE
        _animationTypeListView.setPrefHeight(200);
        _animationTypeListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<AnimationType>() {
            @Override
            public void changed(ObservableValue<? extends AnimationType> observable, AnimationType oldValue, AnimationType newValue) {
                play();
            }
        });

        optionPane.getChildren().addAll(_animationTypeListView);

        return optionPane;
    }

    private void play() {
        Transition transition = AnimationUtils.createTransition(_demoPane.getCenter(), _animationTypeListView.getSelectionModel().getSelectedItem());
        if (transition != null) {
            if (transition.getClass().getName().contains("Out") || transition.getClass().getName().contains("Hinge")) {
                transition.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        bringBackAfter(_demoPane.getCenter());
                    }
                });
            }
            transition.play();
        }
    }

}

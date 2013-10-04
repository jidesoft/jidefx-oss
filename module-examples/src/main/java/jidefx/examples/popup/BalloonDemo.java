/*
 * @(#)DecorationDemo.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.examples.popup;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import jidefx.examples.demo.AbstractFxDemo;
import jidefx.scene.control.decoration.DecorationPane;
import jidefx.scene.control.field.IntegerField;
import jidefx.scene.control.field.NumberField;
import jidefx.scene.control.popup.BalloonPopupOutline;
import jidefx.scene.control.popup.ShapedPopup;
import net.miginfocom.layout.AC;
import net.miginfocom.layout.CC;
import net.miginfocom.layout.LC;
import org.tbee.javafx.scene.layout.MigPane;

public class BalloonDemo extends AbstractFxDemo {

    private ComboBox<Side> _arrowSide;
    private ComboBox<Pos> _anchorPos;

    private IntegerField _roundedRadius;
    private NumberField _arrowPosition;
    private NumberField _arrowBasePosition;
    private IntegerField _arrowHeight;
    private IntegerField _arrowWidth;
    private IntegerField _xOffset;
    private IntegerField _yOffset;

    private Button _button;
    private ShapedPopup _shapedPopup;

    @Override
    public Product getProduct() {
        return Product.COMMON;
    }

    @Override
    public String getName() {
        return "Balloon Demo";
    }

    @Override
    public String getDescription() {
        return "This is a demo of the Balloon popup. " +
                "\n" +
                "Demoed classes:\n" +
                "jidefx.scene.control.popup.Balloon\n" +
                "jidefx.scene.control.popup.BalloonShapePath\n";
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public Region getDemoPanel() {
        ///////////////////////////////////////
        // Button Pane
        VBox buttonPane = new VBox();

        buttonPane.setPrefSize(350, 350);
        buttonPane.setAlignment(Pos.CENTER);
        buttonPane.setPadding(new Insets(30));
        _button = new Button("Show Balloon");
        _button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showBalloon();
            }
        });
        buttonPane.getChildren().addAll(_button);

        ///////////////////////////////////////
        // Option Pane
        MigPane optionPane = new MigPane(new LC().width("350px").height("350px").insets("10"), new AC().index(0).align("right").gap("8px").index(1).fill(), new AC().gap("10px"));

        Label posLabel = new Label("Anchor Point: ");
        optionPane.add(posLabel);

        ObservableList<Pos> poses = FXCollections.observableArrayList();
        poses.addAll(Pos.values());
        _anchorPos = new ComboBox<>(poses);
        _anchorPos.getSelectionModel().select(Pos.BOTTOM_CENTER);
        optionPane.add(_anchorPos, new CC().wrap());

        Label sideLabel = new Label("Arrow Position: ");
        optionPane.add(sideLabel);

        ObservableList<Side> sides = FXCollections.observableArrayList();
        sides.addAll(Side.values());
        _arrowSide = new ComboBox<>(sides);
        _arrowSide.getSelectionModel().select(Side.TOP);
        optionPane.add(_arrowSide, new CC().wrap());

        _roundedRadius = new IntegerField(0, 30);
        _roundedRadius.setValue(6);
        _roundedRadius.setSpinnersVisible(true);
        Label radiusLabel = new Label("Rounded Radius: ");
        optionPane.add(radiusLabel);
        optionPane.add(_roundedRadius, new CC().wrap());
        _roundedRadius.installAdjustmentMouseHandler(radiusLabel);

        _arrowPosition = new NumberField(NumberField.NumberType.Percent);
        _arrowPosition.setValue(0.45);
        _arrowPosition.setSpinnersVisible(true);
        Label arrowPositionLabel = new Label("Arrow Position: ");
        optionPane.add(arrowPositionLabel);
        optionPane.add(_arrowPosition, new CC().wrap());
        _arrowPosition.installAdjustmentMouseHandler(arrowPositionLabel);

        _arrowBasePosition = new NumberField(NumberField.NumberType.Percent);
        _arrowBasePosition.setValue(0.55);
        Label arrowBasePositionLabel = new Label("Arrow Base Position: ");
        optionPane.add(arrowBasePositionLabel);
        optionPane.add(_arrowBasePosition, new CC().wrap());
        _arrowBasePosition.installAdjustmentMouseHandler(arrowBasePositionLabel);

        _arrowHeight = new IntegerField(0, 100);
        _arrowHeight.setValue(30);
        _arrowHeight.setSpinnersVisible(true);
        Label arrowLengthLabel = new Label("Arrow Height: ");
        optionPane.add(arrowLengthLabel);
        optionPane.add(_arrowHeight, new CC().wrap());
        _arrowHeight.installAdjustmentMouseHandler(arrowLengthLabel);

        _arrowWidth = new IntegerField(0, 100);
        _arrowWidth.setValue(20);
        _arrowWidth.setSpinnersVisible(true);
        Label arrowWidthLabel = new Label("Arrow Width: ");
        optionPane.add(arrowWidthLabel);
        optionPane.add(_arrowWidth, new CC().wrap());
        _arrowWidth.installAdjustmentMouseHandler(arrowWidthLabel);

        _xOffset = new IntegerField(-100, 100);
        _xOffset.setValue(0);
        _xOffset.setSpinnersVisible(true);
        Label widthLabel = new Label("Offset X: ");
        optionPane.add(widthLabel);
        optionPane.add(_xOffset, new CC().wrap());
        _xOffset.installAdjustmentMouseHandler(widthLabel);

        _yOffset = new IntegerField(-100, 100);
        _yOffset.setValue(0);
        _yOffset.setSpinnersVisible(true);
        Label heightLabel = new Label("Offset Y: ");
        optionPane.add(heightLabel);
        optionPane.add(_yOffset, new CC().wrap());
        _yOffset.installAdjustmentMouseHandler(heightLabel);

        InvalidationListener adjustListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (_shapedPopup != null) {
                    adjustBalloon();
                }
            }
        };
        _arrowPosition.valueProperty().addListener(adjustListener);
        _arrowBasePosition.valueProperty().addListener(adjustListener);
        _arrowWidth.valueProperty().addListener(adjustListener);
        _roundedRadius.valueProperty().addListener(adjustListener);
        _xOffset.valueProperty().addListener(adjustListener);
        _yOffset.valueProperty().addListener(adjustListener);
        _anchorPos.valueProperty().addListener(adjustListener);

        // this one will change the content padding so we have to show the popup again.
        InvalidationListener showListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (_shapedPopup != null) {
                    showBalloon();
                }
            }
        };
        _arrowSide.valueProperty().addListener(showListener);
        _arrowHeight.valueProperty().addListener(showListener);

        // Assemble
        TitledPane optionTitledPane = new TitledPane("Options", optionPane);
        optionTitledPane.setCollapsible(false);
        TitledPane previewTitledPane = new TitledPane("Balloon", buttonPane);
        previewTitledPane.setCollapsible(false);
        HBox hBox = new HBox(20, optionTitledPane, previewTitledPane);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(10));

        return new DecorationPane(hBox);
    }

    private void showBalloon() {
        if (_shapedPopup != null) {
            _shapedPopup.hide();
            _shapedPopup = null;
        }
        _shapedPopup = new ShapedPopup();
        _shapedPopup.setAutoHide(false);
        _shapedPopup.setCloseButtonVisible(false);
        _shapedPopup.setInsets(new Insets(20));
        _shapedPopup.setPopupContent(new Group(new Label("Welcome to JideFX!")));
        _shapedPopup.getScene().getRoot().getStylesheets().add(BalloonDemo.class.getResource("BalloonPopup.css").toExternalForm());

        BalloonPopupOutline outline = new BalloonPopupOutline();
        outline.arrowSideProperty().bind(_arrowSide.valueProperty());
        outline.arrowPositionProperty().bind(_arrowPosition.valueProperty());
        outline.arrowBasePositionProperty().bind(_arrowBasePosition.valueProperty());
        outline.arrowHeightProperty().bind(_arrowHeight.valueProperty());
        outline.arrowWidthProperty().bind(_arrowWidth.valueProperty());
        outline.roundedRadiusProperty().bind(_roundedRadius.valueProperty());

        _shapedPopup.setPopupOutline(outline);
        if (_shapedPopup.isShowing()) {
            _shapedPopup.adjustPopup(_button, _anchorPos.getValue(), _xOffset.getValue(), _yOffset.getValue());
        }
        else {
            _shapedPopup.showPopup(_button, _anchorPos.getValue(), _xOffset.getValue(), _yOffset.getValue());
        }
    }

    private void adjustBalloon() {
        if (_shapedPopup != null) {
            _shapedPopup.adjustPopup(_button, _anchorPos.getValue(), _xOffset.getValue(), _yOffset.getValue());
        }
    }
}


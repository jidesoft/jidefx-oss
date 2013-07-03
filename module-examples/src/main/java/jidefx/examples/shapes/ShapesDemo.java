/*
 * @(#)AnimationDemo.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.examples.shapes;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import jidefx.examples.demo.AbstractFxDemo;
import jidefx.scene.control.field.IntegerField;
import jidefx.utils.PredefinedShapes;

@SuppressWarnings("Convert2Lambda")
public class ShapesDemo extends AbstractFxDemo {

    private BorderPane _demoPane;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public String getName() {
        return "Shapes Demo";
    }

    @Override
    public String getDescription() {
        return "PredefinedShapes has several pre-defined shapes that you can use inside your application.\n" +
                "\n" +
                "Demoed classes:\n" +
                "jidefx.utils.PredefinedShapes\n";
    }

    @Override
    public Region getDemoPanel() {
        _demoPane = new BorderPane();
        _demoPane.setMinSize(400, 400);
        return _demoPane;
    }

    @Override
    public Region getOptionsPanel() {
        Label fieldLabel = new Label("Size of the Shape: ");
        IntegerField field = new IntegerField(5, 200, 40);
        field.installAdjustmentMouseHandler(fieldLabel);

        ListView<String> shapesList = new ListView<>();
        shapesList.getItems().addAll("Calendar", "Clock", "Key", "Filter", "Clear", "Close", "Plus", "Minus", "Magnifier", "Arrow");
        shapesList.setPrefHeight(200);
        shapesList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                changeShape(newValue, field.getValue());
            }
        });
        field.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                changeShape(shapesList.getSelectionModel().getSelectedItem(), field.getValue());
            }
        });
        shapesList.getSelectionModel().select(0);

        return new VBox(6, fieldLabel, field.asSpinner(), shapesList);
    }

    private void changeShape(String newValue, double size) {
        if ("Calendar".equals(newValue)) {
            _demoPane.setCenter(PredefinedShapes.getInstance().createCalendarIcon(size));
        }
        else if ("Clock".equals(newValue)) {
            _demoPane.setCenter(PredefinedShapes.getInstance().createClockIcon(size));
        }
        else if ("Key".equals(newValue)) {
            _demoPane.setCenter(PredefinedShapes.getInstance().createKeyIcon(size));
        }
        else if ("Filter".equals(newValue)) {
            _demoPane.setCenter(PredefinedShapes.getInstance().createFilterIcon(size));
        }
        else if ("Clear".equals(newValue)) {
            _demoPane.setCenter(PredefinedShapes.getInstance().createClearIcon(size));
        }
        else if ("Close".equals(newValue)) {
            _demoPane.setCenter(PredefinedShapes.getInstance().createCloseIcon(size));
        }
        else if ("Plus".equals(newValue)) {
            _demoPane.setCenter(PredefinedShapes.getInstance().createPlusIcon(size));
        }
        else if ("Minus".equals(newValue)) {
            _demoPane.setCenter(PredefinedShapes.getInstance().createMinusIcon(size));
        }
        else if ("Arrow".equals(newValue)) {
            _demoPane.setCenter(PredefinedShapes.getInstance().createArrowIcon(size));
        }
        else if ("Magnifier".equals(newValue)) {
            _demoPane.setCenter(PredefinedShapes.getInstance().createMagnifierIcon(size, false));
        }
    }
}

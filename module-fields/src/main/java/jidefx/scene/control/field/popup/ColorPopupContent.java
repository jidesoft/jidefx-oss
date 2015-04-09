/*
 * Copyright (c) 2002-2015, JIDE Software Inc. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package jidefx.scene.control.field.popup;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import jidefx.scene.control.decoration.DecorationPane;
import jidefx.scene.control.field.FormattedTextField;
import jidefx.scene.control.field.IntegerField;
import jidefx.utils.converter.javafx.HexColorConverter;

import java.util.Locale;

public class ColorPopupContent extends DecorationPane implements PopupContent<Color> {
    private static final String STYLE_CLASS_DEFAULT = "popup-content"; //NON-NLS

    private static final int PICKER_PADDING = 2;
    private static final int RECT_SIZE = 170;
    private static final int ARROW_SIZE = 10;
    private static final int PICKER_WIDTH = 220;
    private static final int PICKER_HEIGHT = 280;

    private boolean changeIsLocal = false;
    private DoubleProperty hue = new SimpleDoubleProperty() {
        @Override
        protected void invalidated() {
            if (!changeIsLocal) {
                changeIsLocal = true;
                _colorProperty.set(Color.hsb(hue.get(), clamp(sat.get() / 100), clamp(bright.get() / 100)));
                changeIsLocal = false;
            }
        }
    };
    private DoubleProperty sat = new SimpleDoubleProperty() {
        @Override
        protected void invalidated() {
            if (!changeIsLocal) {
                changeIsLocal = true;
                _colorProperty.set(Color.hsb(hue.get(), clamp(sat.get() / 100), clamp(bright.get() / 100)));
                changeIsLocal = false;
            }
        }
    };
    private DoubleProperty bright = new SimpleDoubleProperty() {
        @Override
        protected void invalidated() {
            if (!changeIsLocal) {
                changeIsLocal = true;
                _colorProperty.set(Color.hsb(hue.get(), clamp(sat.get() / 100), clamp(bright.get() / 100)));
                changeIsLocal = false;
            }
        }
    };
    private ObjectProperty<Color> _colorProperty = new SimpleObjectProperty<Color>(Color.RED) {
        @Override
        protected void invalidated() {
            if (!changeIsLocal) {
                changeIsLocal = true;
                Color c = get();
                if (c == null) c = Color.WHITE;
                hue.set(c.getHue());
                sat.set(c.getSaturation() * 100);
                bright.set(c.getBrightness() * 100);
                changeIsLocal = false;
            }
        }
    };

    @Override
    public final ObjectProperty<Color> valueProperty() {
        return _colorProperty;
    }

    @Override
    public final Color getValue() {
        return _colorProperty.get();
    }

    @Override
    public final void setValue(Color value) {
        _colorProperty.set(value);
    }


    public ColorPopupContent() {
        super(new Group());
        getStylesheets().add(PopupContent.class.getResource("PopupContent.css").toExternalForm()); //NON-NLS
        getStyleClass().add(STYLE_CLASS_DEFAULT);

        // create rectangle to capture mouse events to hide
        Rectangle windowClickRect = new Rectangle(PICKER_PADDING + PICKER_WIDTH + PICKER_PADDING, PICKER_PADDING + PICKER_HEIGHT + PICKER_PADDING);
        windowClickRect.setFill(Color.TRANSPARENT);

        Circle colorRectIndicator = new Circle(60, 60, 5);
        colorRectIndicator.setStroke(Color.WHITE);
        colorRectIndicator.setFill(null);
        colorRectIndicator.setEffect(new DropShadow(2, 0, 1, Color.BLACK));

        colorRectIndicator.centerXProperty().bind(new DoubleBinding() {
            {
                bind(sat);
            }

            @Override
            protected double computeValue() {
                return (PICKER_PADDING + 10) + (RECT_SIZE * (sat.get() / 100));
            }
        });

        colorRectIndicator.centerYProperty().bind(new DoubleBinding() {
            {
                bind(bright);
            }

            @Override
            protected double computeValue() {
                return (PICKER_PADDING + ARROW_SIZE + 10) + (RECT_SIZE * (1 - (bright.get() / 100)));
            }
        });

        final Rectangle colorRect = new Rectangle(PICKER_PADDING + 10, PICKER_PADDING + ARROW_SIZE + 10, RECT_SIZE, RECT_SIZE);
        colorRect.setStroke(Color.GRAY);

        colorRect.fillProperty().bind(new ObjectBinding<Paint>() {
            {
                bind(_colorProperty);
            }

            @Override
            protected Paint computeValue() {
                return Color.hsb(hue.getValue(), 1, 1);
            }
        });

        Rectangle colorRectOverlayOne = new Rectangle(PICKER_PADDING + 10, PICKER_PADDING + ARROW_SIZE + 10, RECT_SIZE, RECT_SIZE);
        colorRectOverlayOne.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(255, 255, 255, 1)), new Stop(1, Color.rgb(255, 255, 255, 0))));

        EventHandler<MouseEvent> rectMouseHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX() - colorRect.getX();
                double y = event.getY() - colorRect.getY();
                sat.set(clamp(x / RECT_SIZE) * 100);
                bright.set(100 - (clamp(y / RECT_SIZE) * 100));
            }
        };

        Rectangle colorRectOverlayTwo = new Rectangle(PICKER_PADDING + 10, PICKER_PADDING + ARROW_SIZE + 10, RECT_SIZE, RECT_SIZE);
        colorRectOverlayTwo.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.rgb(0, 0, 0, 0)), new Stop(1, Color.rgb(0, 0, 0, 1))));
        colorRectOverlayTwo.setOnMouseDragged(rectMouseHandler);
        colorRectOverlayTwo.setOnMouseClicked(rectMouseHandler);

        final Rectangle colorBar = new Rectangle(PICKER_PADDING + PICKER_WIDTH - 30, PICKER_PADDING + ARROW_SIZE + 10, 20, RECT_SIZE);
        colorBar.setStroke(Color.GRAY);
        colorBar.setFill(createHueGradient());

        Rectangle colorBarIndicator = new Rectangle(PICKER_PADDING + PICKER_WIDTH - 32, PICKER_PADDING + ARROW_SIZE + 15, 24, 10);
        colorBarIndicator.setArcWidth(4);
        colorBarIndicator.setArcHeight(4);
        colorBarIndicator.setStroke(Color.WHITE);
        colorBarIndicator.setFill(null);
        colorBarIndicator.setEffect(new DropShadow(2, 0, 1, Color.BLACK));

        colorBarIndicator.yProperty().bind(new DoubleBinding() {
            {
                bind(hue);
            }

            @Override
            protected double computeValue() {
                return (PICKER_PADDING + ARROW_SIZE + 5) + (RECT_SIZE * (hue.get() / 360));
            }
        });
        EventHandler<MouseEvent> barMouseHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double y = event.getY() - colorBar.getY();
                hue.set(clamp(y / RECT_SIZE) * 360);
            }
        };
        colorBar.setOnMouseDragged(barMouseHandler);
        colorBar.setOnMouseClicked(barMouseHandler);

        Label brightnessLabel = new Label(getResourceString("brightness"));
        brightnessLabel.setMinWidth(Control.USE_PREF_SIZE);
        GridPane.setConstraints(brightnessLabel, 0, 0);

        Slider brightnessSlider = new Slider();
        brightnessSlider.setMin(0);
        brightnessSlider.setMax(100);
        brightnessSlider.setId("BrightnessSlider"); //NON-NLS
        brightnessSlider.valueProperty().bindBidirectional(bright);
        GridPane.setConstraints(brightnessSlider, 1, 0);

        IntegerField brightnessField = new IntegerField();
        brightnessField.setSpinnersVisible(true);
        brightnessField.setPrefColumnCount(7);
        brightnessField.installAdjustmentMouseHandler(brightnessLabel);
        bindBidirectional(brightnessField.valueProperty(), bright);
        GridPane.setConstraints(brightnessField, 2, 0);

        Label saturationLabel = new Label(getResourceString("saturation"));
        saturationLabel.setMinWidth(Control.USE_PREF_SIZE);
        GridPane.setConstraints(saturationLabel, 0, 1);

        Slider saturationSlider = new Slider();
        saturationSlider.setMin(0);
        saturationSlider.setMax(100);
        saturationSlider.setId("SaturationSlider"); //NON-NLS
        saturationSlider.valueProperty().bindBidirectional(sat);
        GridPane.setConstraints(saturationSlider, 1, 1);

        saturationSlider.styleProperty().bind(new StringBinding() {
            {
                bind(_colorProperty);
            }

            @Override
            protected String computeValue() {
                return "picker-color: hsb(" + hue.get() + ",100%,100%);"; //NON-NLS
            }
        });

        IntegerField saturationField = new IntegerField();
        saturationField.setSpinnersVisible(true);
        saturationField.setPrefColumnCount(7);
        saturationField.setValue(0);
        saturationField.installAdjustmentMouseHandler(saturationLabel);
        bindBidirectional(saturationField.valueProperty(), sat);
        GridPane.setConstraints(saturationField, 2, 1);

        Label webLabel = new Label(getResourceString("web"));
        webLabel.setMinWidth(Control.USE_PREF_SIZE);
        GridPane.setConstraints(webLabel, 0, 2);

        FormattedTextField<Color> webField = new FormattedTextField<>();
        webField.setStringConverter(new HexColorConverter().toStringConverter());
        webField.valueProperty().bindBidirectional(_colorProperty);
        webField.setComboBoxLike(false);
        webField.setEditable(false);
        GridPane.setConstraints(webField, 1, 2, 2, 1);

        GridPane controls = new GridPane();
        controls.setVgap(5);
        controls.setHgap(5);
        controls.getChildren().addAll(brightnessLabel, brightnessSlider, brightnessField, saturationLabel, saturationSlider, saturationField, webLabel, webField);
        controls.setManaged(false);
        controls.resizeRelocate(
                PICKER_PADDING + 10,
                PICKER_PADDING + ARROW_SIZE + 10 + 170 + 10,
                PICKER_WIDTH - 20,
                80);

        ((Group) getContent()).getChildren().addAll(windowClickRect, colorRect, colorRectOverlayOne, colorRectOverlayTwo, colorBar, colorRectIndicator, colorBarIndicator, controls);
    }

    private void bindBidirectional(ObjectProperty<Integer> objectProperty, DoubleProperty doubleProperty) {
        doubleProperty.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                objectProperty.set(newValue.intValue());
            }
        });
        objectProperty.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                doubleProperty.setValue(newValue.doubleValue());
            }
        });
    }

    private static double clamp(double value) {
        return value < 0 ? 0 : value > 1 ? 1 : value;
    }

    private static LinearGradient createHueGradient() {
        double offset;
        Stop[] stops = new Stop[255];
        for (int y = 0; y < 255; y++) {
            offset = 1 - (1.0 / 255) * y;
            int h = (int) ((y / 255.0) * 360);
            stops[y] = new Stop(offset, Color.hsb(h, 1.0, 1.0));
        }
        return new LinearGradient(0f, 1f, 1f, 0f, true, CycleMethod.NO_CYCLE, stops);
    }

    /**
     * Gets the localized string from resource bundle. Subclass can override it to provide its own string. Available
     * keys are defined in grid.properties that begins with "Filter." and lucene.properties that begins with "Lucene".
     *
     * @param key the key to the resource.
     * @return the localized string.
     */
    public String getResourceString(String key) {
        if (key == null) {
            return "";
        }
        return PopupsResource.getResourceBundle(Locale.getDefault()).getString(key);
    }
}

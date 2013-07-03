/*
 * @(#)IntegerField.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import jidefx.scene.control.field.popup.PopupContent;
import jidefx.scene.control.field.verifier.IntegerDigitsPatternVerifier;

import java.text.NumberFormat;

/**
 * A {@code PopupField} for Integers. It allows you to specify the min and max value. The slider is used as the
 * popup to choose an integer value.
 */
public class IntegerField extends PopupField<Integer> {
    private IntegerProperty _maxProperty;
    private IntegerProperty _minProperty;

    public IntegerField() {
        this(0, 100, 0);
    }

    public IntegerField(int min, int max) {
        this(min, max, min);
    }

    public IntegerField(int min, int max, int value) {
        setMin(min);
        setMax(max);
        setValue(value);
    }

    private static final String STYLE_CLASS_DEFAULT = "integer-field"; //NON-NLS

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().addAll(STYLE_CLASS_DEFAULT);
    }

    private void initializePatternVerifiers(final int min, final int max) {
        getPatternVerifiers().put("n", new IntegerDigitsPatternVerifier<Integer>(min, max) { //NON-NLS
            @Override
            public Number toTargetValue(Integer fieldValue) {
                return fieldValue;
            }

            @Override
            public Integer fromTargetValue(Integer previousFieldValue, Number targetValue) {
                return targetValue.intValue();
            }
        });
    }

    public IntegerProperty maxProperty() {
        if (_maxProperty == null) {
            _maxProperty = new SimpleIntegerProperty(this, "max") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    initializePatternVerifiers(getMin(), get());
                }
            };
        }
        return _maxProperty;
    }

    public int getMax() {
        return maxProperty().get();
    }

    public void setMax(int maxValue) {
        maxProperty().set(maxValue);
    }

    public IntegerProperty minProperty() {
        if (_minProperty == null) {
            _minProperty = new SimpleIntegerProperty(this, "min") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    initializePatternVerifiers(get(), getMax());
                }
            };
        }
        return _minProperty;
    }

    public int getMin() {
        return minProperty().get();
    }

    public void setMin(int min) {
        minProperty().set(min);
    }

    @Override
    protected void initializePattern() {
        super.initializePattern();
        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(false);
        setStringConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                return object != null ? format.format(object) : null;
            }

            @Override
            public Integer fromString(String string) {
                try {
                    return Integer.parseInt(string);
                }
                catch (NumberFormatException e) {
                    return null;
                }
            }
        });

        initializePatternVerifiers(getMin(), getMax());
        setPattern("n"); //NON-NLS
    }

    @Override
    protected void initializeTextField() {
        super.initializeTextField();
        setPopupContentFactory(new Callback<Integer, PopupContent<Integer>>() {
            @Override
            public PopupContent<Integer> call(Integer param) {
                return new IntegerSliderPopupContent(getValue());
            }
        });
    }

    public class IntegerSliderPopupContent extends VBox implements PopupContent<Integer> {
        private static final String STYLE_CLASS_DEFAULT = "popup-content"; //NON-NLS

        private final Slider _slider;
        private ObjectProperty<Integer> _value;

        public IntegerSliderPopupContent(int value) {
            getStylesheets().add(PopupContent.class.getResource("PopupContent.css").toExternalForm()); //NON-NLS
            getStyleClass().add(STYLE_CLASS_DEFAULT);
            _slider = new Slider(0, 100, value);
            _slider.maxProperty().bind(maxProperty());
            _slider.minProperty().bind(minProperty());
            getChildren().add(_slider);
            _value = new SimpleObjectProperty<>();
            _slider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    _value.set(newValue.intValue());
                }
            });
        }

        @Override
        public ObservableValue<Integer> valueProperty() {
            return _value;
        }

        @Override
        public Integer getValue() {
            return (int) _slider.getValue();
        }

        @Override
        public void setValue(Integer value) {
            _slider.setValue(value);
        }
    }
}

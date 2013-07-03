/*
 * @(#)LocalDateTimeComboBox.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.combobox;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;
import jidefx.scene.control.field.FormattedTextField;
import jidefx.scene.control.field.LocalDateTimeField;
import jidefx.scene.control.field.popup.PopupContent;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

/**
 * A {@code FormattedComboBox} for {@link LocalDateTime}.
 */
public class LocalDateTimeComboBox extends FormattedComboBox<LocalDateTime> {
    private static final String STYLE_CLASS_DEFAULT = "local-date-time-combo-box"; //NON-NLS
    private StringProperty _patternProperty;

    /**
     * Creates a {@code LocalDateTimeComboBox} with a default pattern and the specified value.
     */
    public LocalDateTimeComboBox() {
        this(((SimpleDateFormat) SimpleDateFormat.getDateTimeInstance()).toPattern(), LocalDateTime.now());
    }

    /**
     * Creates a {@code LocalDateTimeComboBox} with the specified pattern and the today's date.
     *
     * @param pattern  the pattern of the format
     */
    public LocalDateTimeComboBox(String pattern) {
        this(pattern, LocalDateTime.now());
    }

    /**
     * Creates a {@code LocalDateTimeComboBox} with a default pattern and the specified value.
     *
     * @param value the initial value
     */
    public LocalDateTimeComboBox(LocalDateTime value) {
        this(((SimpleDateFormat) SimpleDateFormat.getDateTimeInstance()).toPattern(), value);
    }

    /**
     * Creates a {@code LocalDateTimeComboBox} with the specified pattern and the specified value.
     *
     * @param pattern  the pattern of the format
     * @param value the initial value
     */
    public LocalDateTimeComboBox(String pattern, LocalDateTime value) {
        super(value);
        setPattern(pattern);
    }

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().add(STYLE_CLASS_DEFAULT);
    }

    public StringProperty patternProperty() {
        if (_patternProperty == null) {
            _patternProperty = new SimpleStringProperty();
        }
        return _patternProperty;
    }

    /**
     * Gets the pattern.
     *
     * @return the pattern.
     */
    public String getPattern() {
        return patternProperty().get();
    }

    /**
     * Sets the pattern.
     *
     * @param pattern a new pattern
     */
    public void setPattern(String pattern) {
        patternProperty().set(pattern);
    }

    @Override
    protected FormattedTextField<LocalDateTime> createFormattedTextField() {
        LocalDateTimeField field = new LocalDateTimeField(getPattern());
        field.patternProperty().bind(patternProperty());
        return field;
    }

    @Override
    protected void initializeComboBox() {
        super.initializeComboBox();
        setPopupContentFactory(new Callback<LocalDateTime, PopupContent<LocalDateTime>>() {
            @Override
            public PopupContent<LocalDateTime> call(LocalDateTime param) {
                PopupContent<LocalDateTime> popupContent = ((LocalDateTimeField) getEditor()).getPopupContentFactory().call(param);
                popupContent.valueProperty().addListener(new ChangeListener<LocalDateTime>() {
                    @Override
                    public void changed(ObservableValue<? extends LocalDateTime> observable, LocalDateTime oldValue, LocalDateTime newValue) {
                        hide();
                    }
                });
                return popupContent;
            }
        });
    }
}

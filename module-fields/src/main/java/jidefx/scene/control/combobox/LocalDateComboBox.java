/*
 * @(#)LocalDateComboBox.java 5/19/2013
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
import jidefx.scene.control.field.LocalDateField;
import jidefx.scene.control.field.popup.PopupContent;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

/**
 * A {@code FormattedComboBox} for {@link LocalDate}.
 */
public class LocalDateComboBox extends FormattedComboBox<LocalDate> {
    private static final String STYLE_CLASS_DEFAULT = "local-date-combo-box"; //NON-NLS
    private StringProperty _patternProperty;

    /**
     * Creates a {@code LocalDateComboBox} with a default pattern and the specified value.
     */
    public LocalDateComboBox() {
        this(((SimpleDateFormat) SimpleDateFormat.getDateInstance()).toPattern(), LocalDate.now());
    }

    /**
     * Creates a {@code LocalDateComboBox} with the specified pattern and the today's date.
     *
     * @param pattern  the pattern of the format
     */
    public LocalDateComboBox(String pattern) {
        this(pattern, LocalDate.now());
    }

    /**
     * Creates a {@code LocalDateComboBox} with a default pattern and the specified value.
     *
     * @param value the initial value
     */
    public LocalDateComboBox(LocalDate value) {
        this(((SimpleDateFormat) SimpleDateFormat.getDateInstance()).toPattern(), value);
    }

    /**
     * Creates a {@code LocalDateComboBox} with the specified pattern and the specified value.
     *
     * @param pattern  the pattern of the format
     * @param value the initial value
     */
    public LocalDateComboBox(String pattern, LocalDate value) {
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
    protected FormattedTextField<LocalDate> createFormattedTextField() {
        LocalDateField field = new LocalDateField(getPattern());
        field.patternProperty().bind(patternProperty());
        return field;
    }

    @Override
    protected void initializeComboBox() {
        super.initializeComboBox();
        setPopupContentFactory(new Callback<LocalDate, PopupContent<LocalDate>>() {
            @Override
            public PopupContent<LocalDate> call(LocalDate param) {
                PopupContent<LocalDate> popupContent = ((LocalDateField) getEditor()).getPopupContentFactory().call(param);
                popupContent.valueProperty().addListener(new ChangeListener<LocalDate>() {
                    @Override
                    public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                        hide();
                    }
                });
                return popupContent;
            }
        });
    }
}

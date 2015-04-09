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
package jidefx.scene.control.field;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jidefx.scene.control.field.verifier.PatternVerifierUtils;
import jidefx.utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * A {@code PopupField} for {@link java.time.LocalDate}.
 */
public class LocalTimeField extends PopupField<LocalTime> {
    public LocalTimeField() {
        this(((SimpleDateFormat) SimpleDateFormat.getTimeInstance()).toPattern()); // TODO: find out the pattern for the DateTimeFormatter's FormatStyle.MEDIUM
    }

    public LocalTimeField(String pattern) {
        this(pattern, LocalTime.now());
    }

    public LocalTimeField(String pattern, LocalTime value) {
        setPattern(pattern);
        setValue(value);
    }

    private static final String STYLE_CLASS_DEFAULT = "local-time-field"; //NON-NLS

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().addAll(STYLE_CLASS_DEFAULT);
    }

    @Override
    protected void initializeTextField() {
        super.initializeTextField();
        setSpinnersVisible(true);
        setPopupButtonVisible(false);
    }

    @Override
    protected void registerListeners() {
        super.registerListeners();
        patternProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                setDateTimeFormatter(DateTimeFormatter.ofPattern(getPattern()));
            }
        });
    }

    @Override
    protected boolean supportFromString() {
        return getDateTimeFormatter() != null || super.supportFromString();
    }

    @Override
    protected LocalTime fromString(String text) {
        DateTimeFormatter dateTimeFormatter = getDateTimeFormatter();
        if (dateTimeFormatter != null) {
            TemporalAccessor parse = dateTimeFormatter.parse(text);
            return LocalTime.from(parse);
        }
        return super.fromString(text);
    }

    @Override
    protected String toString(LocalTime value) {
        DateTimeFormatter formatter = getDateTimeFormatter();
        if (formatter != null) {
            try {
                return formatter.format(value);
            }
            catch (Exception e) {
                CommonUtils.ignoreException(e);
            }
        }
        return super.toString(value);
    }

    private ObjectProperty<DateTimeFormatter> _dateTimeFormatProperty;

    public ObjectProperty<DateTimeFormatter> dateTimeFormatterProperty() {
        if (_dateTimeFormatProperty == null) {
            _dateTimeFormatProperty = new SimpleObjectProperty<DateTimeFormatter>(this, "dateTimeFormatter") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    PatternVerifierUtils.initializePatternVerifiersForDateTimeFormatter(getPatternVerifiers());
                    setStringConverter(null);
                }
            };
        }
        return _dateTimeFormatProperty;
    }

    /**
     * Gets the DateTimeFormatter.
     *
     * @return the DateTimeFormatter.
     */
    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatterProperty().get();
    }

    /**
     * Sets the DateTimeFormatter that will format a LocalTime.
     *
     * @param format the new DateTimeFormatter.
     */
    public void setDateTimeFormatter(DateTimeFormatter format) {
        dateTimeFormatterProperty().set(format);
    }


    /**
     * A static method which will create a LocalTimeField for LocalTime.
     *
     * @param timeFormat   the time format string as defined in {@link DateTimeFormatter}. Only the time related pattern
     *                     letters should be used.
     * @param initialValue the initial value.
     * @return a LocalDateField for the LocalDate.
     */
    public static LocalTimeField createLocalTimeField(String timeFormat, LocalTime initialValue) {
        LocalTimeField field = new LocalTimeField(timeFormat);
        field.setValue(initialValue);
        return field;
    }

    /**
     * A static method which will create a LocalTimeField for LocalTime.
     *
     * @param initialValue the initial value.
     * @return a LocalDateField for the LocalDate.
     */
    public static LocalTimeField createLocalTimeField(LocalTime initialValue) {
        LocalTimeField field = new LocalTimeField();
        field.setValue(initialValue);
        return field;
    }

    /**
     * A static method which will create a LocalTimeField for LocalTime.
     *
     * @return a LocalTimeField for the LocalTime.
     */
    public static LocalTimeField createLocalTimeField() {
        return new LocalTimeField();
    }

}

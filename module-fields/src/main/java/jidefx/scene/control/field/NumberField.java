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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.StringConverter;
import jidefx.scene.control.field.verifier.FractionDigitsPatternVerifier;
import jidefx.scene.control.field.verifier.IntegerDigitsPatternVerifier;
import jidefx.utils.CommonUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * A {@code FormattedTextField} for {@link Number}.
 */
public class NumberField extends FormattedTextField<Number> {
    public static enum NumberType {Normal, Integer, Percent, Currency}

    private BooleanProperty _positiveOnlyProperty;

    public NumberField() {
        setNumberType(NumberType.Normal);
    }

    public NumberField(NumberType type) {
        setNumberType(type);
    }

    private static final String STYLE_CLASS_DEFAULT = "number-field"; //NON-NLS

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().addAll(STYLE_CLASS_DEFAULT);
    }

    @Override
    protected void initializeTextField() {
        super.initializeTextField();
        setSpinnersVisible(true);
    }

    private boolean isNegative(Number value) {
        // null is considered as positive as that matches our default pattern
        return value != null && value.doubleValue() < 0;
    }

    @Override
    protected void registerListeners() {
        super.registerListeners();
        valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!isNegative(oldValue) && isNegative(newValue)) {
                    initializePatternVerifiersForDecimalFormat(getDecimalFormat());
                }
                if (isNegative(oldValue) && !isNegative(newValue)) {
                    initializePatternVerifiersForDecimalFormat(getDecimalFormat());
                }
            }
        });
    }

    private ObjectProperty<NumberType> _numberTypeProperty;

    public ObjectProperty<NumberType> numberTypeProperty() {
        if (_numberTypeProperty == null) {
            _numberTypeProperty = new SimpleObjectProperty<NumberType>(this, "numberType") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    DecimalFormat format = (DecimalFormat) DecimalFormat.getNumberInstance();
                    NumberType newValue = get();
                    switch (newValue) {
                        case Percent:
                            format = (DecimalFormat) DecimalFormat.getPercentInstance();
                            break;
                        case Currency:
                            format = (DecimalFormat) DecimalFormat.getCurrencyInstance();
                            break;
                        case Normal:
                            format = (DecimalFormat) DecimalFormat.getNumberInstance();
                            break;
                        case Integer:
                            format = (DecimalFormat) DecimalFormat.getIntegerInstance();
                            break;
                    }
                    format.setGroupingUsed(false);
                    setDecimalFormat(format);
                }
            };
        }
        return _numberTypeProperty;
    }

    public NumberType getNumberType() {
        return numberTypeProperty().get();
    }

    public void setNumberType(NumberType numberType) {
        numberTypeProperty().set(numberType);
    }

    private ObjectProperty<DecimalFormat> _decimalFormatProperty;

    public ObjectProperty<DecimalFormat> decimalFormatProperty() {
        if (_decimalFormatProperty == null) {
            _decimalFormatProperty = new SimpleObjectProperty<DecimalFormat>(this, "decimalFormat") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    initializePatternVerifiersForDecimalFormat(get());
                    setStringConverter(null);
                }
            };
        }
        return _decimalFormatProperty;
    }

    /**
     * Gets the DecimalFormat.
     *
     * @return the DecimalFormat.
     */
    public DecimalFormat getDecimalFormat() {
        return decimalFormatProperty().get();
    }

    /**
     * Sets the DecimalFormat that will format the value. We currently only support DateFormat and DecimalFormat.
     * Because the way the FormattedTextField works, we will automatically call the following line for any
     * DecimalFormats.
     * <pre>{@code
     * DecimalFormat format = ...
     * ..
     * format.setMinimumFractionDigits(format.getMaximumFractionDigits());
     * }</pre>
     *
     * @param format the new DecimalFormat.
     */
    public void setDecimalFormat(DecimalFormat format) {
        decimalFormatProperty().set(format);
    }

    public BooleanProperty positiveOnlyProperty() {
        if (_positiveOnlyProperty == null) {
            _positiveOnlyProperty = new SimpleBooleanProperty(this, "positiveOnly") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    initializePatternVerifiersForDecimalFormat(getDecimalFormat());
                }
            };
        }
        return _positiveOnlyProperty;
    }

    /**
     * Gets the flag to allow only positive values in the field.
     *
     * @return true or false.
     */
    public boolean isPositiveOnly() {
        return positiveOnlyProperty().get();
    }

    /**
     * Sets the flag to allow only positive values in the field.
     *
     * @param positiveOnly true to allow only possible values. False to allow both negative and positive values.
     */
    public void setPositiveOnly(boolean positiveOnly) {
        positiveOnlyProperty().set(positiveOnly);
    }

    @Override
    protected String toString(Number value) {
        DecimalFormat format = getDecimalFormat();
        if (format != null) {
            try {
                return format.format(value);
            }
            catch (Exception e) {
                CommonUtils.ignoreException(e);
            }
        }
        return super.toString(value);
    }

    @Override
    protected Number fromString(String text) {
        DecimalFormat format = getDecimalFormat();
        if (format != null) {
            try {
                return format.parse(text);
            }
            catch (ParseException e) {
                CommonUtils.ignoreException(e);
            }
        }
        return super.fromString(text);
    }

    @Override
    protected boolean supportFromString() {
        return getDecimalFormat() != null || super.supportFromString();
    }

    protected void initializePatternVerifiersForDecimalFormat(DecimalFormat decimalFormat) {
        int multiplier = decimalFormat.getMultiplier();
        String text = decimalFormat.format((getValue() != null && getValue().doubleValue() < 0 ? -1.2 : 1.2) / (double) multiplier);
        text = text.replace("0", "").replace('2', 'f').replace('1', 'n');
        final int integerDigits = decimalFormat.getMaximumIntegerDigits();
        if (integerDigits >= 1) {
            IntegerDigitsPatternVerifier<Number> patternVerifier = new IntegerDigitsPatternVerifier<Number>(
                    isPositiveOnly() ? 0 : -(int) Math.pow(10, integerDigits) + 1, (int) Math.pow(10, integerDigits) - 1, 1, multiplier) {
                @Override
                public Number toTargetValue(Number fieldValue) {
                    return fieldValue;
                }

                @Override
                public Number fromTargetValue(Number previousFieldValue, Number targetValue) {
                    return targetValue;
                }
            };
            patternVerifier.setStringConverter(new StringConverter<Number>() {
                private NumberFormat getIntegerFormat(NumberFormat format) {
                    NumberFormat instance = NumberFormat.getInstance();
                    instance.setMaximumIntegerDigits(format.getMaximumIntegerDigits());
                    instance.setMinimumIntegerDigits(format.getMinimumIntegerDigits());
                    instance.setMaximumFractionDigits(0);
                    instance.setGroupingUsed(format.isGroupingUsed());
                    instance.setParseIntegerOnly(true);
                    return instance;
                }

                @Override
                public String toString(Number object) {
                    return getIntegerFormat(decimalFormat).format(object);
                }

                @Override
                public Number fromString(String string) {
                    try {
                        return getIntegerFormat(decimalFormat).parse(string);
                    }
                    catch (ParseException e) {
                        CommonUtils.ignoreException(e);
                    }
                    return Integer.parseInt(string);
                }
            });
            getPatternVerifiers().put("n", patternVerifier); //NON-NLS
        }
        if (decimalFormat.getMaximumFractionDigits() >= 1) {
            getPatternVerifiers().put("f", new FractionDigitsPatternVerifier<Number>(decimalFormat.getMaximumFractionDigits(), multiplier) {
                @Override
                public Number toTargetValue(Number fieldValue) {
                    return fieldValue;
                }

                @Override
                public Number fromTargetValue(Number previousFieldValue, Number targetValue) {
                    return targetValue;
                }
            });
            // we have to enforce the MinimumFractionDigits because the way group works
            decimalFormat.setMinimumFractionDigits(decimalFormat.getMaximumFractionDigits());
        }
        setPattern(text);
    }
}

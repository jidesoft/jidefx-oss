/*
 * @(#)FontField.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.text.Font;
import javafx.util.Callback;
import jidefx.scene.control.field.popup.FontPopupContent;
import jidefx.scene.control.field.popup.PopupContent;
import jidefx.scene.control.field.verifier.FractionDigitsPatternVerifier;
import jidefx.scene.control.field.verifier.IntegerDigitsPatternVerifier;
import jidefx.scene.control.field.verifier.StringValuesPatternVerifier;
import jidefx.utils.FontUtils;
import jidefx.utils.converter.javafx.FontConverter;

import java.util.List;

/**
 * A {@code PopupField} for {@link Font}.
 */
public class FontField extends PopupField<Font> {
    public FontField() {
    }

    private static final String STYLE_CLASS_DEFAULT = "font-field"; //NON-NLS

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().addAll(STYLE_CLASS_DEFAULT);
    }

    @Override
    protected void initializePattern() {
        super.initializePattern();
        setStringConverter(new FontConverter().toStringConverter());

        getPatternVerifiers().put("family", new StringValuesPatternVerifier<Font>() { //NON-NLS
            @Override
            protected List<String> createValues() {
                return Font.getFamilies();
            }

            @Override
            public String toTargetValue(Font fieldValue) {
                return fieldValue.getFamily();
            }

            @Override
            public Font fromTargetValue(Font previousFieldValue, String value) {
                return previousFieldValue != null ? FontUtils.createFont(value, previousFieldValue.getStyle(), previousFieldValue.getSize())
                        : FontUtils.createFont(value, null, 10);
            }
        });
        StringValuesPatternVerifier<Font> verifier = new StringValuesPatternVerifier<Font>() {
            @Override
            protected List<String> createValues() {
                String family = getGroupText(0);
                return FontUtils.getSupportedFontStyles(family, 10.0);
            }

            @Override
            public String toTargetValue(Font fieldValue) {
                return fieldValue.getStyle();
            }

            @Override
            public Font fromTargetValue(Font previousFieldValue, String value) {
                return previousFieldValue != null ? FontUtils.createFont(previousFieldValue.getFamily(), value, previousFieldValue.getSize())
                        : FontUtils.createFont(null, value, 10);
            }
        };
        getPatternVerifiers().put("style", verifier);

        valueProperty().addListener(new ChangeListener<Font>() {
            @Override
            public void changed(ObservableValue<? extends Font> observable, Font oldValue, Font newValue) {
                verifier.invalidate();
            }
        });

        getPatternVerifiers().put("integer", new IntegerDigitsPatternVerifier<Font>(0, 999) { //NON-NLS
            @Override
            public Number toTargetValue(Font fieldValue) {
                return fieldValue.getSize();
            }

            @Override
            public Font fromTargetValue(Font previousFieldValue, Number value) {
                double size = value.doubleValue();
                return previousFieldValue != null ? FontUtils.createFont(previousFieldValue.getFamily(), previousFieldValue.getStyle(), size)
                        : FontUtils.createFont(null, null, size);
            }
        });
        getPatternVerifiers().put("fraction", new FractionDigitsPatternVerifier<Font>(1) { //NON-NLS
            @Override
            public Number toTargetValue(Font fieldValue) {
                return fieldValue.getSize();
            }

            @Override
            public Font fromTargetValue(Font previousFieldValue, Number value) {
                double size = value.doubleValue();
                return previousFieldValue != null ? FontUtils.createFont(previousFieldValue.getFamily(), previousFieldValue.getStyle(), size)
                        : FontUtils.createFont(null, null, size);
            }
        });
        setPattern("family, style, integer.fraction"); //NON-NLS
    }

    @Override
    protected void initializeTextField() {
        super.initializeTextField();
        setPopupContentFactory(new Callback<Font, PopupContent<Font>>() {
            @Override
            public PopupContent<Font> call(Font param) {
                return new FontPopupContent(getValue());
            }
        });
    }
}
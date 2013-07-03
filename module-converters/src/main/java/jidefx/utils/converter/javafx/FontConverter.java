/*
 * @(#)FontConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */
package jidefx.utils.converter.javafx;

import javafx.scene.text.Font;
import jidefx.utils.converter.ConverterContext;
import jidefx.utils.converter.DefaultObjectConverter;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Converter which converts {@link Font} to String and converts it back.
 */
public class FontConverter extends DefaultObjectConverter<Font> {

    private String _separator = ", ";
    private NumberFormat _numberFormat = null;

    public FontConverter() {
        this(", ");
    }

    public FontConverter(String separator) {
        setSeparator(separator);
    }

    /**
     * Gets the separator string used to split the string representation of the Font. The default value is ", ".
     *
     * @return the separator string used to split the string representation of the Font.
     */
    public String getSeparator() {
        return _separator;
    }

    /**
     * Sets the separator string used to split the string representation of the Font. The default value is ", ".pli
     *
     * @param separator the separator string used to split the string representation of the Font.
     */
    public void setSeparator(String separator) {
        _separator = separator;
    }

    /**
     * Sets the NumberFormat which is used to format the font size.
     *
     * @param numberFormat a new NumberFormat which is used to format the font size.
     */
    public void setNumberFormat(NumberFormat numberFormat) {
        _numberFormat = numberFormat;
    }

    /**
     * Gets the NumberFormat which is used to format the font size. By default, it will return a format which has maximum 3 integer digits and 1 fraction digits.
     *
     * @return the NumberFormat which is used to format the font size.
     */
    public NumberFormat getNumberFormat() {
        if (_numberFormat == null) {
            _numberFormat = NumberFormat.getNumberInstance();
            _numberFormat.setMaximumFractionDigits(1);
            _numberFormat.setMinimumFractionDigits(1);
            _numberFormat.setMaximumIntegerDigits(3);
        }
        return _numberFormat;
    }

    @Override
    public String toString(Font font, ConverterContext context) {
        if (font == null) {
            return null;
        }
        return font.getFamily() + _separator + font.getStyle() + _separator + getNumberFormat().format(font.getSize());
    }

    @Override
    public Font fromString(String string, ConverterContext context) {
        if (string != null && !string.trim().isEmpty()) {
            String fontFamily = null;

            String[] strings = string.split(_separator);
            if (strings.length > 0) {
                fontFamily = strings[0].trim();
            }
            String fontStyle = null;
            if (strings.length > 1) {
                fontStyle = strings[1].trim();
            }
            double fontSize = Font.getDefault().getSize();
            if (strings.length > 2) {
                try {
                    fontSize = getNumberFormat().parse(strings[2].trim()).doubleValue();
                }
                catch (ParseException ignored) {
                    // ignore, use the default font size
                }
            }

            return FontUtils.createFont(fontFamily, fontStyle, fontSize);
        }

        return null;
    }
}

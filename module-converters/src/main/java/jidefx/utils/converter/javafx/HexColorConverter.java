/*
 * @(#)HexColorConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter.javafx;

import javafx.scene.paint.Color;
import jidefx.utils.converter.ConverterContext;
import jidefx.utils.converter.DefaultObjectConverter;

/**
 * If alpha value is not included, converts Color to/from #RRGGBB format. For example #000000 is Color(0, 0, 0) and
 * #FF00FF is Color(255, 0, 255). If alpha value is included, converts Color to/from #RRGGBBAAformat. For example
 * #000000FF is Color(0, 0, 0, 255) and #FF00FF64 is Color(255, 0, 255, 100).
 */
public class HexColorConverter extends DefaultObjectConverter<Color> implements ColorConverter {

    private boolean _opacityIncluded = false;

    /**
     * Creates a HexColorConverter. This is the default constructor and will not include alpha value.
     */
    public HexColorConverter() {
    }

    /**
     * Creates a HexColorConverter. With this constructor, you can create a converter with alpha value included.
     *
     * @param opacityIncluded the flag if alpha value will be included in this converter
     */
    public HexColorConverter(boolean opacityIncluded) {
        _opacityIncluded = opacityIncluded;
    }

    /**
     * Get the flag if this converter should consider alpha value.
     * <p>
     * If you use default constructor, the default value of this flag is false.
     * <p>
     *
     * @return true if this converter should consider alpha value.
     * @see HexColorConverter
     */
    public boolean isOpacityIncluded() {
        return _opacityIncluded;
    }

    /**
     * Set the flag if this converter should consider alpha value.
     * <p>
     *
     * @param opacityIncluded the flag if this converter should consider alpha value.
     * @see #isOpacityIncluded()
     */
    public void setOpacityIncluded(boolean opacityIncluded) {
        _opacityIncluded = opacityIncluded;
    }

    private static String getHexString(int color) {
        String value = Integer.toHexString(color).toUpperCase();
        if (value.length() == 1) {
            value = '0' + value;
        }
        return value;
    }

    @Override
    public String toString(Color color, ConverterContext context) {
        if (color == null) {
            return null;
        }
        StringBuffer colorText = new StringBuffer("#");
        colorText.append(getHexString((int) Math.round(color.getRed() * 255)));
        colorText.append(getHexString((int) Math.round(color.getGreen() * 255)));
        colorText.append(getHexString((int) Math.round(color.getBlue() * 255)));

        if (isOpacityIncluded()) {
            colorText.append(getHexString((int) Math.round(color.getOpacity() * 255)));
        }
        return new String(colorText);
    }

    @Override
    public Color fromString(String string, ConverterContext context) {
        if (string == null || string.trim().isEmpty()) {
            return null;
        }
        if (string.startsWith("#")) {
            string = string.substring(1);
        }
        if (isOpacityIncluded()) {
            if (string.length() > 8) {
                string = string.substring(string.length() - 8);
            }
        }
        else {
            if (string.length() > 6) {
                string = string.substring(string.length() - 6);
            }
        }
        return Color.web(string);
    }
}

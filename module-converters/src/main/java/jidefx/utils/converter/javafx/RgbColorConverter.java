/*
 * @(#)RgbColorConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter.javafx;

import javafx.scene.paint.Color;
import jidefx.utils.converter.ConverterContext;
import jidefx.utils.converter.DefaultObjectConverter;

import java.util.StringTokenizer;

/**
 * If alpha value is not included, converts Color to/from "XXX, XXX, XXX" format. For example "0, 0, 0" is Color(0, 0,
 * 0) and "255, 0, 255" is Color(255, 0, 255).
 * <p>
 * If alpha value is included, converts Color to/from "XXX, XXX, XXX, XXX" format. For example "0, 0, 0, 255" is
 * Color(0, 0, 0, 255) and "255, 0, 255, 100" is Color(255, 0, 255, 100).
 */
public class RgbColorConverter extends DefaultObjectConverter<Color> implements ColorConverter {

    private boolean _opacityIncluded = false;

    /**
     * Creates a RgbColorConverter. This is the default constructor and will not include alpha value.
     */
    public RgbColorConverter() {
    }

    /**
     * Creates a RgbColorConverter. With this constructor, you can create a converter with alpha value included.
     *
     * @param opacityIncluded the flag if alpha value will be included in this converter
     */
    public RgbColorConverter(boolean opacityIncluded) {
        _opacityIncluded = opacityIncluded;
    }

    /**
     * Get the flag if this converter should consider alpha value.
     * <p>
     * If you use default constructor, the default value of this flag is false.
     * <p>
     *
     * @return true if this converter should consider alpha value.
     * @see RgbColorConverter
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

    @Override
    public String toString(Color color, ConverterContext context) {
        if (color == null) {
            return null;
        }
        StringBuffer colorText = new StringBuffer();
        colorText.append((int) Math.round(color.getRed() * 255)).append(", ");
        colorText.append((int) Math.round(color.getGreen() * 255)).append(", ");
        colorText.append((int) Math.round(color.getBlue() * 255));
        if (isOpacityIncluded()) {
            colorText.append(", ").append((int) Math.round(color.getOpacity() * 255));
        }
        return new String(colorText);
    }

    @Override
    public Color fromString(String string, ConverterContext context) {
        if (string == null || string.trim().isEmpty()) {
            return null;
        }
        StringTokenizer token = new StringTokenizer(string, ",; ");
        int r = 0;
        if (token.hasMoreTokens()) {
            String s = token.nextToken();
            try {
                r = Integer.parseInt(s, 10) % 256;
                if (r < 0) {
                    r += 256;
                }
            }
            catch (NumberFormatException ignored) {
                return null;
            }
        }
        int g = 0;
        if (token.hasMoreTokens()) {
            String s = token.nextToken();
            try {
                g = Integer.parseInt(s, 10) % 256;
                if (g < 0) {
                    g += 256;
                }
            }
            catch (NumberFormatException ignored) {
                return null;
            }
        }
        int b = 0;
        if (token.hasMoreTokens()) {
            String s = token.nextToken();
            try {
                b = Integer.parseInt(s, 10) % 256;
                if (b < 0) {
                    b += 256;
                }
            }
            catch (NumberFormatException ignored) {
                return null;
            }
        }
        int a = 255;
        if (isOpacityIncluded() && token.hasMoreTokens()) {
            String s = token.nextToken();
            try {
                a = Integer.parseInt(s, 10) % 256;
                if (a < 0) {
                    a += 256;
                }
            }
            catch (NumberFormatException ignored) {
                return null;
            }
        }

        return Color.rgb(r, g, b, a / 255.0);
    }
}

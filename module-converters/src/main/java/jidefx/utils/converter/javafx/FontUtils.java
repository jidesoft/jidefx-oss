/*
 * @(#)FontUtils.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter.javafx;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

/**
 * An exact copy of the same name class from the JideFX Common Layer. Do it in order to remove the dependency on the
 * Common Layer. If you would like to use it directly, please use the one in the Common Layer.
 */
class FontUtils {
    public static List<String> getSupportedFontStyles(String fontFamily, double size) {
        List<String> styles = new ArrayList<>();
        FontWeight[] values = FontWeight.values();
        for (FontWeight value : values) {
            Font font = Font.font(fontFamily, value, size);
            if (!styles.contains(font.getStyle())) {
                styles.add(font.getStyle());
            }
        }
        Font font = Font.font(fontFamily, FontPosture.ITALIC, size);
        if (!styles.contains(font.getStyle())) {
            styles.add(font.getStyle());
        }
        return styles;
    }

    public static Font createFont(String fontFamily, String fontStyle, double fontSize) {
        Font font = null;
        // if no font family, we just return a default font.
        if (fontFamily == null && fontStyle == null) {
            font = Font.getDefault();
        }


        // if no font style, we just return a normal regular font
        if (font == null && fontStyle == null) {
            font = Font.font(fontFamily, FontWeight.NORMAL, FontPosture.REGULAR, fontSize);
        }

        // if no font family, we set the family to the default one
        if (font == null && fontFamily == null) {
            fontFamily = Font.getDefault().getFamily();
        }

        if (font == null) {
            // create the font until we found the font
            FontWeight[] values = FontWeight.values();
            outside:
            for (FontWeight weight : values) {
                for (FontPosture posture : FontPosture.values()) {
                    Font f = Font.font(fontFamily, weight, posture, fontSize);
                    if (f.getStyle().equals(fontStyle) && f.getFamily().equals(fontFamily)) {
                        font = f;
                        break outside;
                    }
                }
            }
            if (font == null) {
                font = Font.font(fontFamily, FontWeight.NORMAL, FontPosture.REGULAR, fontSize);
            }
        }
        return font;
    }
}

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
package jidefx.utils;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for the Font in the javafx.scene.text package.
 */
public class FontUtils {
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

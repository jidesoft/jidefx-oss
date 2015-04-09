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
package jidefx.utils.converter.javafx;

import javafx.scene.paint.Color;
import jidefx.utils.converter.ConverterContext;
import jidefx.utils.converter.DefaultObjectConverter;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WebColorConverter extends DefaultObjectConverter<Color> implements ColorConverter {
    private static final Map<Color, String> colorNameMap = new HashMap<>(147);

    static {
        // The following code is used to generate the hard-coded colorNameMap
        // below, but it is then hand tweaked to proper represent the colour
        // names.

//        // Initializes the namedColors map
//        Color.web("white", 1.0);
//        for (Field f : Color.class.getDeclaredFields()) {
//            int modifier = f.getModifiers();
//            if (Modifier.isStatic(modifier) && Modifier.isPublic(modifier) && f.getType().equals(Color.class)) {
//                colorNames.add(f.getName());
//
//                String name = f.toString();
//                name = name.substring(name.lastIndexOf("Color."));
//
//                String displayName = f.getName();
//                displayName = displayName.substring(0, 1) + displayName.substring(1).toLowerCase();
//
//                System.out.println("colorNameMap.put(" + name + ", \"" + displayName + "\");");
//            }
//        }

        colorNameMap.put(Color.TRANSPARENT, "Transparent");
        colorNameMap.put(Color.ALICEBLUE, "Alice Blue");
        colorNameMap.put(Color.ANTIQUEWHITE, "Antique White");
        colorNameMap.put(Color.AQUA, "Aqua");
        colorNameMap.put(Color.AQUAMARINE, "Aquamarine");
        colorNameMap.put(Color.AZURE, "Azure");
        colorNameMap.put(Color.BEIGE, "Beige");
        colorNameMap.put(Color.BISQUE, "Bisque");
        colorNameMap.put(Color.BLACK, "Black");
        colorNameMap.put(Color.BLANCHEDALMOND, "Blanched Almond");
        colorNameMap.put(Color.BLUE, "Blue");
        colorNameMap.put(Color.BLUEVIOLET, "Blue Violet");
        colorNameMap.put(Color.BROWN, "Brown");
        colorNameMap.put(Color.BURLYWOOD, "Burlywood");
        colorNameMap.put(Color.CADETBLUE, "Cadet Blue");
        colorNameMap.put(Color.CHARTREUSE, "Chartreuse");
        colorNameMap.put(Color.CHOCOLATE, "Chocolate");
        colorNameMap.put(Color.CORAL, "Coral");
        colorNameMap.put(Color.CORNFLOWERBLUE, "Cornflower Blue");
        colorNameMap.put(Color.CORNSILK, "Cornsilk");
        colorNameMap.put(Color.CRIMSON, "Crimson");
        colorNameMap.put(Color.CYAN, "Cyan");
        colorNameMap.put(Color.DARKBLUE, "Dark Blue");
        colorNameMap.put(Color.DARKCYAN, "Dark Cyan");
        colorNameMap.put(Color.DARKGOLDENROD, "Dark Goldenrod");
        colorNameMap.put(Color.DARKGRAY, "Dark Gray");
        colorNameMap.put(Color.DARKGREEN, "Dark Green");
        colorNameMap.put(Color.DARKGREY, "Dark Grey");
        colorNameMap.put(Color.DARKKHAKI, "Dark Khaki");
        colorNameMap.put(Color.DARKMAGENTA, "Dark Magenta");
        colorNameMap.put(Color.DARKOLIVEGREEN, "Dark Olive Green");
        colorNameMap.put(Color.DARKORANGE, "Dark Orange");
        colorNameMap.put(Color.DARKORCHID, "Dark Orchid");
        colorNameMap.put(Color.DARKRED, "Dark Red");
        colorNameMap.put(Color.DARKSALMON, "Dark Salmon");
        colorNameMap.put(Color.DARKSEAGREEN, "Dark Sea Green");
        colorNameMap.put(Color.DARKSLATEBLUE, "Dark Slate Blue");
        colorNameMap.put(Color.DARKSLATEGRAY, "Dark Slate Gray");
        colorNameMap.put(Color.DARKSLATEGREY, "Dark Slate Grey");
        colorNameMap.put(Color.DARKTURQUOISE, "Dark Turquoise");
        colorNameMap.put(Color.DARKVIOLET, "Dark Violet");
        colorNameMap.put(Color.DEEPPINK, "Deep Pink");
        colorNameMap.put(Color.DEEPSKYBLUE, "Deep Sky Blue");
        colorNameMap.put(Color.DIMGRAY, "Dim Gray");
        colorNameMap.put(Color.DIMGREY, "Dim Grey");
        colorNameMap.put(Color.DODGERBLUE, "Dodger Blue");
        colorNameMap.put(Color.FIREBRICK, "Firebrick");
        colorNameMap.put(Color.FLORALWHITE, "Floral White");
        colorNameMap.put(Color.FORESTGREEN, "Forest Green");
        colorNameMap.put(Color.FUCHSIA, "Fuchsia");
        colorNameMap.put(Color.GAINSBORO, "Gainsboro");
        colorNameMap.put(Color.GHOSTWHITE, "Ghost White");
        colorNameMap.put(Color.GOLD, "Gold");
        colorNameMap.put(Color.GOLDENROD, "Goldenrod");
        colorNameMap.put(Color.GRAY, "Gray");
        colorNameMap.put(Color.GREEN, "Green");
        colorNameMap.put(Color.GREENYELLOW, "Green Yellow");
        colorNameMap.put(Color.GREY, "Grey");
        colorNameMap.put(Color.HONEYDEW, "Honeydew");
        colorNameMap.put(Color.HOTPINK, "Hot Pink");
        colorNameMap.put(Color.INDIANRED, "Indian Red");
        colorNameMap.put(Color.INDIGO, "Indigo");
        colorNameMap.put(Color.IVORY, "Ivory");
        colorNameMap.put(Color.KHAKI, "Khaki");
        colorNameMap.put(Color.LAVENDER, "Lavender");
        colorNameMap.put(Color.LAVENDERBLUSH, "Lavender Blush");
        colorNameMap.put(Color.LAWNGREEN, "Lawn Green");
        colorNameMap.put(Color.LEMONCHIFFON, "Lemon Chiffon");
        colorNameMap.put(Color.LIGHTBLUE, "Light Blue");
        colorNameMap.put(Color.LIGHTCORAL, "Light Coral");
        colorNameMap.put(Color.LIGHTCYAN, "Light Cyan");
        colorNameMap.put(Color.LIGHTGOLDENRODYELLOW, "Light Goldenrod Yellow");
        colorNameMap.put(Color.LIGHTGRAY, "Light Gray");
        colorNameMap.put(Color.LIGHTGREEN, "Light Green");
        colorNameMap.put(Color.LIGHTGREY, "Light Grey");
        colorNameMap.put(Color.LIGHTPINK, "Light Pink");
        colorNameMap.put(Color.LIGHTSALMON, "Light Salmon");
        colorNameMap.put(Color.LIGHTSEAGREEN, "Light Sea Green");
        colorNameMap.put(Color.LIGHTSKYBLUE, "Light Sky Blue");
        colorNameMap.put(Color.LIGHTSLATEGRAY, "Light Slate Gray");
        colorNameMap.put(Color.LIGHTSLATEGREY, "Light Slate Grey");
        colorNameMap.put(Color.LIGHTSTEELBLUE, "Light Steel Blue");
        colorNameMap.put(Color.LIGHTYELLOW, "Light Yellow");
        colorNameMap.put(Color.LIME, "Lime");
        colorNameMap.put(Color.LIMEGREEN, "Lime Green");
        colorNameMap.put(Color.LINEN, "Linen");
        colorNameMap.put(Color.MAGENTA, "Magenta");
        colorNameMap.put(Color.MAROON, "Maroon");
        colorNameMap.put(Color.MEDIUMAQUAMARINE, "Medium Aquamarine");
        colorNameMap.put(Color.MEDIUMBLUE, "Medium Blue");
        colorNameMap.put(Color.MEDIUMORCHID, "Medium Orchid");
        colorNameMap.put(Color.MEDIUMPURPLE, "Medium Purple");
        colorNameMap.put(Color.MEDIUMSEAGREEN, "Medium Sea Green");
        colorNameMap.put(Color.MEDIUMSLATEBLUE, "Medium Slate Blue");
        colorNameMap.put(Color.MEDIUMSPRINGGREEN, "Medium Spring Green");
        colorNameMap.put(Color.MEDIUMTURQUOISE, "Medium Turquoise");
        colorNameMap.put(Color.MEDIUMVIOLETRED, "Medium Violet Red");
        colorNameMap.put(Color.MIDNIGHTBLUE, "Midnight Blue");
        colorNameMap.put(Color.MINTCREAM, "Mint Cream");
        colorNameMap.put(Color.MISTYROSE, "Misty Rose");
        colorNameMap.put(Color.MOCCASIN, "Moccasin");
        colorNameMap.put(Color.NAVAJOWHITE, "Navajo White");
        colorNameMap.put(Color.NAVY, "Navy");
        colorNameMap.put(Color.OLDLACE, "Old Lace");
        colorNameMap.put(Color.OLIVE, "Olive");
        colorNameMap.put(Color.OLIVEDRAB, "Olive Drab");
        colorNameMap.put(Color.ORANGE, "Orange");
        colorNameMap.put(Color.ORANGERED, "Orange Red");
        colorNameMap.put(Color.ORCHID, "Orchid");
        colorNameMap.put(Color.PALEGOLDENROD, "Pale Goldenrod");
        colorNameMap.put(Color.PALEGREEN, "Pale Green");
        colorNameMap.put(Color.PALETURQUOISE, "Pale Turquoise");
        colorNameMap.put(Color.PALEVIOLETRED, "Pale Violet Red");
        colorNameMap.put(Color.PAPAYAWHIP, "Papaya Whip");
        colorNameMap.put(Color.PEACHPUFF, "Peach Puff");
        colorNameMap.put(Color.PERU, "Peru");
        colorNameMap.put(Color.PINK, "Pink");
        colorNameMap.put(Color.PLUM, "Plum");
        colorNameMap.put(Color.POWDERBLUE, "Powder Blue");
        colorNameMap.put(Color.PURPLE, "Purple");
        colorNameMap.put(Color.RED, "Red");
        colorNameMap.put(Color.ROSYBROWN, "Rosy Brown");
        colorNameMap.put(Color.ROYALBLUE, "Royal Blue");
        colorNameMap.put(Color.SADDLEBROWN, "Saddle Brown");
        colorNameMap.put(Color.SALMON, "Salmon");
        colorNameMap.put(Color.SANDYBROWN, "Sandy Brown");
        colorNameMap.put(Color.SEAGREEN, "Sea Green");
        colorNameMap.put(Color.SEASHELL, "Seashell");
        colorNameMap.put(Color.SIENNA, "Sienna");
        colorNameMap.put(Color.SILVER, "Silver");
        colorNameMap.put(Color.SKYBLUE, "Sky Blue");
        colorNameMap.put(Color.SLATEBLUE, "Slate Blue");
        colorNameMap.put(Color.SLATEGRAY, "Slate Gray");
        colorNameMap.put(Color.SLATEGREY, "Slate Grey");
        colorNameMap.put(Color.SNOW, "Snow");
        colorNameMap.put(Color.SPRINGGREEN, "Spring Green");
        colorNameMap.put(Color.STEELBLUE, "Steel Blue");
        colorNameMap.put(Color.TAN, "Tan");
        colorNameMap.put(Color.TEAL, "Teal");
        colorNameMap.put(Color.THISTLE, "Thistle");
        colorNameMap.put(Color.TOMATO, "Tomato");
        colorNameMap.put(Color.TURQUOISE, "Turquoise");
        colorNameMap.put(Color.VIOLET, "Violet");
        colorNameMap.put(Color.WHEAT, "Wheat");
        colorNameMap.put(Color.WHITE, "White");
        colorNameMap.put(Color.WHITESMOKE, "White Smoke");
        colorNameMap.put(Color.YELLOW, "Yellow");
        colorNameMap.put(Color.YELLOWGREEN, "Yellow Green");
    }

    public WebColorConverter() {
    }

    public static Map<Color, String> getColorNameMap() {
        return colorNameMap;
    }

    static String colorValueToWeb(Color c) {
        if (c == null) {
            return null;
        }
        String web = colorNameMap.get(c);
        if (web == null) {
            web = String.format((Locale) null, "%02x%02x%02x", Math.round(c.getRed() * 255), Math.round(c.getGreen() * 255), Math.round(c.getBlue() * 255));
        }
        return web;
    }

    @Override
    public String toString(Color color, ConverterContext context) {
        return colorValueToWeb(color);
    }

    @Override
    public Color fromString(String s, ConverterContext context) {
        Color color = null;
        if (s != null) {
            for (Color key : colorNameMap.keySet()) {
                if (colorNameMap.get(key).equalsIgnoreCase(s)) {
                    color = key;
                    break;
                }
            }
        }
        if (color == null) {
            try {
                color = Color.web(s, 1.0);
            }
            catch (IllegalArgumentException e) {
                // ignore
            }
        }

        return color;
    }
}

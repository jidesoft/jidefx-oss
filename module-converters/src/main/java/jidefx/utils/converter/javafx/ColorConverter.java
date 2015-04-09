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
import jidefx.utils.converter.ObjectConverter;

/**
 * {@link ObjectConverter} for {@link Color}. This is just a base abstract class that doesn't do
 * anything but define a few converter contexts. Subclasses of it will do the actual conversion.
 */
public interface ColorConverter extends ObjectConverter<Color> {

    /**
     * ConverterContext for color to convert to RGB string.
     */
    ConverterContext CONTEXT_RGB = ConverterContext.CONTEXT_DEFAULT;

    /**
     * ConverterContext for color to convert to HEX string.
     */
    ConverterContext CONTEXT_HEX = new ConverterContext("Hex"); //NON-NLS

    /**
     * ConverterContext for color to convert to RGB and alpha string.
     */
    ConverterContext CONTEXT_RGBA = new ConverterContext("RGBA"); //NON-NLS

    /**
     * ConverterContext for color to convert to HEX string.
     */
    ConverterContext CONTEXT_HEX_WITH_ALPHA = new ConverterContext("HexWithAlpha"); //NON-NLS

    /**
     * ConverterContext for color to convert to WEB string.
     */
    ConverterContext CONTEXT_WEB = new ConverterContext("Web"); //NON-NLS
}

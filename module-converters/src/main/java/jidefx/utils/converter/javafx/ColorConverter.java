/*
 * @(#)ColorConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
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

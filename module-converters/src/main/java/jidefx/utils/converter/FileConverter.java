/*
 * @(#)FileConverter.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils.converter;

import java.io.File;

/**
 * {@link ObjectConverter} implementation for {@link File} using the full file path.
 */
public class FileConverter extends DefaultObjectConverter<File> {

    @Override
    public String toString(File file, ConverterContext context) {
        if (file != null) {
            return file.getAbsolutePath();
        }
        else {
            return "";
        }
    }

    @Override
    public File fromString(String string, ConverterContext context) {
        if (string == null) {
            return null;
        }

        string = string.trim();
        if (string.length() == 0) {
            return null;
        }

        return new File(string);
    }

}

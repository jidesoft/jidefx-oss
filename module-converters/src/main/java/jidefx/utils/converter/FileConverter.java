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

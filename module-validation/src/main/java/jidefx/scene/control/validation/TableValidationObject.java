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
package jidefx.scene.control.validation;

/**
 * {@code TableValidationObject} extends {@code ValidationObject} to provide row and column information of the
 * value to be validated.
 */
@SuppressWarnings("UnusedDeclaration")
public class TableValidationObject extends ValidationObject {
    private int _row = -1;
    private int _column = -1;

    public TableValidationObject(Object source, Object oldValue, Object newValue) {
        super(source, oldValue, newValue);
    }

    public TableValidationObject(Object source, Object oldValue, Object newValue, int row, int column) {
        super(source, oldValue, newValue);
        _row = row;
        _column = column;
    }

    /**
     * Gets the row.
     *
     * @return the row.
     */
    public int getRow() {
        return _row;
    }

    /**
     * Gets the column.
     *
     * @return the column.
     */
    public int getColumn() {
        return _column;
    }

    @Override
    public String toString() {
        String properties =
                " source=" + getSource() + //NON-NLS
                        " oldValue=" + getOldValue() + //NON-NLS
                        " newValue=" + getNewValue() + //NON-NLS
                        " row=" + getRow() + //NON-NLS
                        " column=" + getColumn() + //NON-NLS
                        " ";
        return getClass().getName() + "[" + properties + "]";
    }
}

/*
 * @(#)TableValidationObject.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
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

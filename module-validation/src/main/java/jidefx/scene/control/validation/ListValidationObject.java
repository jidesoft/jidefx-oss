/*
 * @(#)ListValidationObject.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */
package jidefx.scene.control.validation;

/**
 * {@code ListValidationObject} extends {@code ValidationObject} to provide the row information of the value
 * to be validated.
 */
@SuppressWarnings("UnusedDeclaration")
public class ListValidationObject extends ValidationObject {
    private int _row = -1;

    public ListValidationObject(Object source, Object oldValue, Object newValue) {
        super(source, oldValue, newValue);
    }

    public ListValidationObject(Object source, Object oldValue, Object newValue, int row) {
        super(source, oldValue, newValue);
        _row = row;
    }

    /**
     * Gets the row.
     *
     * @return the row.
     */
    public int getRow() {
        return _row;
    }

    @Override
    public String toString() {
        String properties =
                " source=" + getSource() + //NON-NLS
                        " oldValue=" + getOldValue() + //NON-NLS
                        " newValue=" + getNewValue() + //NON-NLS
                        " row=" + getRow() + //NON-NLS
                        " ";
        return getClass().getName() + "[" + properties + "]";
    }
}

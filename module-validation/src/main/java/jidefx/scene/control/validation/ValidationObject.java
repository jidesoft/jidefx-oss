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
 * {@code ValidationObject} is an object containing the information needed by a {@code Validator}. It has
 * three things - the source, the new value and old value.
 * <p>
 * The source is the object who has the Validator. It is usually the Node to be validated. For example, in the case of
 * validating a text field, the source will be the text field.
 * <p>
 * Normally {@code ValidationObject} is accompanied by the old and new value. If the value is a primitive type
 * (such as int or boolean) it must be wrapped as the corresponding java.lang.* Object type (such as Integer or
 * Boolean).
 * <p>
 * Null values may be provided for the old and the new values if their true values are not known. Please note, the new
 * value could be a different data type which were failed to be converted to the expected. For example, for an integer
 * field, the new value could be String if the value cannot be converted to an integer. A correctly?written
 * {@code Validator} should check for the data type of the new value and generate a proper ValidationEvent when the
 * new value has the wrong data type.
 * <p>
 * Users can extend this class to create their own {@code ValidationObject} to provide additional information that
 * are needed by {@code Validator}. For example, {@code TableValidationObject} extends
 * {@code ValidationObject} to add row and column information in a table.
 */
public class ValidationObject {
    /**
     * The source. It is usually the node to be validated.
     */
    private Object _source;

    /**
     * New value.
     */
    private Object _newValue;

    /**
     * Previous value. May be null if not known.
     */
    private Object _oldValue;

    /**
     * Constructs a new {@code ValidationObject}.
     *
     * @param source   The source that sends this ValidationObject.
     * @param oldValue The old value. Please note, the old value is optional, so it might be null in some cases.
     * @param newValue The new value.
     */
    public ValidationObject(Object source, Object oldValue, Object newValue) {
        _source = source;
        _newValue = newValue;
        _oldValue = oldValue;
    }

    public Object getSource() {
        return _source;
    }

    /**
     * Sets the new value, expressed as an Object.
     *
     * @return The new value, expressed as an Object.
     */
    public Object getNewValue() {
        return _newValue;
    }

    /**
     * Gets the old value, expressed as an Object.
     *
     * @return The old value, expressed as an Object.
     */
    public Object getOldValue() {
        return _oldValue;
    }

    @Override
    public String toString() {
        String properties =
                " source=" + getSource() + //NON-NLS
                        " oldValue=" + getOldValue() + //NON-NLS
                        " newValue=" + getNewValue() + //NON-NLS
                        " ";
        return getClass().getName() + "[" + properties + "]";
    }
}

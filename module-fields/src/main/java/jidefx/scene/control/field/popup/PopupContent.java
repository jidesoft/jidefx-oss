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
package jidefx.scene.control.field.popup;

import javafx.beans.value.ObservableValue;

/**
 * An interface for all the popup content of the PopupFields or the ComboBoxes. It has three methods but all are for the
 * same reason - the value that is represented by the popup. The ComboBox or the PopupField will set the value using the
 * {@link #setValue(Object)} method and also listen to the change of the value using the {@link #valueProperty()}.
 *
 * @param <T> the data type that the PopupContent represents.
 */
public interface PopupContent<T> {

    /**
     * The observable value that the owner of the popup content can listen to.
     *
     * @return the value property.
     */
    ObservableValue<T> valueProperty();

    /**
     * Gets the value.
     *
     * @return the value
     */
    T getValue();

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    void setValue(T value);
}

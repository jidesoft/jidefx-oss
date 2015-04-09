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
package jidefx.scene.control.editor;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;

/**
 * An interface for an editor of a certain data type. This editor can be used to set a value and to get a value. User
 * can adjust the value using the editor itself. There are many different editors that are all unified under this common
 * interface. It works with {@link EditorManager} to provide a consistent way to retrieve an editor to edit any data types.
 *
 * @param <T> the value data type
 * @see EditorManager
 */
public interface Editor<T> {

    /**
     * An observable value of the editor so that one can listen to the value change.
     *
     * @return An observable value.
     */
    ObservableValue<T> observableValue();

    /**
     * Gets the value from the editor. For example, if the editor is a ComboBox, this method should return {@code
     * comboBox.getValue()}.
     *
     * @return the value.
     */
    T getValue();

    /**
     * Sets the value to the editor. For example, if the editor is a ComboBox, this method should call {@code
     * comboBox.setValue(value)}.
     *
     * @param value the new value.
     */
    void setValue(final T value);

    /**
     * Creates the editor node. The default implementation of this method return the Editor itself as in most cases, we
     * implement the Editor interface directly on the control itself.
     *
     * @return the editor node.
     */
    default Node createEditor() {
        return this instanceof Node ? (Node) this : null;
    }
}

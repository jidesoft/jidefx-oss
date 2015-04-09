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
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 * An editor based on {@code ComboBox} for any data that can be enumerated.
 */
public class ComboBoxEditor<T> extends ComboBox<T> implements Editor<T>, LazyInitializeEditor<T> {
    @Override
    public ObservableValue<T> observableValue() {
        return valueProperty();
    }

    @Override
    public void initialize(Class<T> clazz, EditorContext context) {
        Object list = context != null ? context.getProperties().get(EditorContext.PROPERTY_OBSERVABLE_LIST) : null;
        if (list instanceof ObservableList) {
            setItems(((ObservableList<T>) list));
        }
        Object editable = context != null ? context.getProperties().get(EditorContext.PROPERTY_EDITABLE) : null;
        if (editable instanceof Boolean) {
            setEditable(((Boolean) editable));
        }
    }
}

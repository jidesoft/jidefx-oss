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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EnumChoiceBoxEditor<T extends Enum> extends ChoiceBoxEditor<T> implements LazyInitializeEditor<T> {
    private boolean initialized = false;

    @Override
    public void initialize(Class<T> clazz, EditorContext context) {
        super.initialize(clazz, context);
        if (Enum.class.isAssignableFrom(clazz)) {
            initializeEnums(clazz);
        }
    }

    private void initializeEnums(Class<T> clazz) {
        if (!initialized && clazz != null && clazz.isEnum()) {
            ObservableList<T> observableList = FXCollections.observableArrayList(clazz.getEnumConstants());
            setItems(observableList);
            initialized = true;
        }
    }
}

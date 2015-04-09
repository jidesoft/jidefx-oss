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
import javafx.scene.control.TextField;

/**
 * An editor based on {@code TextField} for {@code String}.
 */
public class TextFieldEditor extends TextField implements Editor<String>, LazyInitializeEditor<String> {
    @Override
    public ObservableValue<String> observableValue() {
        return textProperty();
    }

    @Override
    public void setValue(String newValue) {
        setText(newValue);
    }

    @Override
    public String getValue() {
        return observableValue().getValue();
    }

    @Override
    public void initialize(Class<String> clazz, EditorContext context) {
        Object editable = context != null ? context.getProperties().get(EditorContext.PROPERTY_EDITABLE) : null;
        if (editable instanceof Boolean) {
            setEditable(((Boolean) editable));
        }
    }
}

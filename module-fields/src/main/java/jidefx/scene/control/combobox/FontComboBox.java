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
package jidefx.scene.control.combobox;

import javafx.scene.text.Font;
import jidefx.scene.control.field.FontField;
import jidefx.scene.control.field.FormattedTextField;

/**
 * A {@code FormattedComboBox} for {@link Font}.
 */
public class FontComboBox extends FormattedComboBox<Font> {
    private static final String STYLE_CLASS_DEFAULT = "font-combo-box"; //NON-NLS

    /**
     * Creates a {@code FontComboBox} with the value set to Font.getDefault().
     */
    public FontComboBox() {
        this(Font.getDefault());
    }

    /**
     * Creates a {@code FontComboBox} with the specified value.
     *
     * @param font the initial value
     */
    public FontComboBox(Font font) {
        super(font);
    }

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().add(STYLE_CLASS_DEFAULT);
    }

    @Override
    protected FormattedTextField<Font> createFormattedTextField() {
        return new FontField();
    }

    @Override
    protected void initializeComboBox() {
        super.initializeComboBox();
        setPopupContentFactory(((FontField) getEditor()).getPopupContentFactory());
    }
}

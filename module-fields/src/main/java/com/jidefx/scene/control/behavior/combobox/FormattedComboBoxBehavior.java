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
package com.jidefx.scene.control.behavior.combobox;

import com.sun.javafx.scene.control.behavior.ComboBoxBaseBehavior;
import com.sun.javafx.scene.control.behavior.KeyBinding;
import jidefx.scene.control.combobox.FormattedComboBox;

import java.util.ArrayList;
import java.util.List;

import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.UP;
import static javafx.scene.input.KeyEvent.KEY_PRESSED;

public class FormattedComboBoxBehavior<T> extends ComboBoxBaseBehavior<T> {

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     *
     */
    public FormattedComboBoxBehavior(FormattedComboBox<T> comboBox) {
        super(comboBox, COMBO_BOX_BINDINGS);
    }

    /**
     * ************************************************************************ * Key event handling * *
     * ************************************************************************
     */

    protected static final List<KeyBinding> COMBO_BOX_BINDINGS = new ArrayList<>();

    static {
        COMBO_BOX_BINDINGS.add(new KeyBinding(UP, KEY_PRESSED, "selectPrevious")); //NON-NLS
        COMBO_BOX_BINDINGS.add(new KeyBinding(DOWN, "selectNext")); //NON-NLS
        COMBO_BOX_BINDINGS.addAll(COMBO_BOX_BASE_BINDINGS);
    }

    @Override
    protected void callAction(String name) {
        if ("selectPrevious".equals(name)) { //NON-NLS
            selectPrevious();
        }
        else if ("selectNext".equals(name)) { //NON-NLS
            selectNext();
        }
        else {
            super.callAction(name);
        }
    }

    private FormattedComboBox<T> getComboBox() {
        return (FormattedComboBox<T>) getControl();
    }

    private void selectPrevious() {
        getComboBox().getEditor().decreaseValue();
    }

    private void selectNext() {
        getComboBox().getEditor().increaseValue();
    }
}

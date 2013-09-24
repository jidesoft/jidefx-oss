/*
 * @(#)FormattedComboBoxBehavior.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
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

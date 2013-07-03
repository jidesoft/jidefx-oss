/*
 * @(#)InsetsComboBox.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.combobox;

import javafx.geometry.Insets;
import jidefx.scene.control.field.FormattedTextField;
import jidefx.scene.control.field.InsetsField;

/**
 * A {@code FormattedComboBox} for {@link Insets}.
 */
public class InsetsComboBox extends ValuesComboBox<Insets> {
    public InsetsComboBox() {
        this(new Insets(0));
    }

    public InsetsComboBox(Insets insets) {
        super(insets);
    }

    @Override
    protected FormattedTextField<Insets> createFormattedTextField() {
        return new InsetsField();
    }

    @Override
    protected void initializeComboBox() {
        super.initializeComboBox();
        setPopupContentFactory(((InsetsField) getEditor()).getPopupContentFactory());
    }
}

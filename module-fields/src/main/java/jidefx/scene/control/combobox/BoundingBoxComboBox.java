/*
 * @(#)BoundingBoxComboBox.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.combobox;

import javafx.geometry.BoundingBox;
import jidefx.scene.control.field.BoundingBoxField;
import jidefx.scene.control.field.FormattedTextField;

/**
 * A {@code FormattedComboBox} for {@link BoundingBox}.
 */
public class BoundingBoxComboBox extends ValuesComboBox<BoundingBox> {
    public BoundingBoxComboBox() {
        this(new BoundingBox(0, 0, 0, 0, 0, 0));
    }

    public BoundingBoxComboBox(BoundingBox value) {
        super(value);
    }


    @Override
    protected FormattedTextField<BoundingBox> createFormattedTextField() {
        return new BoundingBoxField();
    }

    @Override
    protected void initializeComboBox() {
        super.initializeComboBox();
        setPopupContentFactory(((BoundingBoxField) getEditor()).getPopupContentFactory());
    }
}

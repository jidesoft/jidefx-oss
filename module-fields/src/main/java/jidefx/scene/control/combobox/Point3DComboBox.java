/*
 * @(#)Point3DComboBox.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.combobox;

import javafx.geometry.Point3D;
import jidefx.scene.control.field.FormattedTextField;
import jidefx.scene.control.field.Point3DField;

/**
 * A {@code FormattedComboBox} for {@link Point3D}.
 */
public class Point3DComboBox extends ValuesComboBox<Point3D> {
    public Point3DComboBox() {
        this(new Point3D(0, 0, 0));
    }

    public Point3DComboBox(Point3D value) {
        super(value);
    }


    @Override
    protected FormattedTextField<Point3D> createFormattedTextField() {
        return new Point3DField();
    }

    @Override
    protected void initializeComboBox() {
        super.initializeComboBox();
        setPopupContentFactory(((Point3DField) getEditor()).getPopupContentFactory());
    }
}

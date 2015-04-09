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

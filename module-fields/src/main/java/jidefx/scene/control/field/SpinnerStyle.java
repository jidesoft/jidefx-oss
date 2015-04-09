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
package jidefx.scene.control.field;

/**
 * An enum for SpinnerStyles. It can be set using {@link FormattedTextField#setSpinnerStyle(SpinnerStyle)}.
 *
 * @see FormattedTextField
 */
public enum SpinnerStyle {
    /**
     * The spinner buttons are inside the field, on the right hand side and vertically positioned. This is the default
     * style.
     */
    INSIDE_RIGHT_VERTICAL,
    /**
     * The spinner buttons are inside the field, on the left hand side and vertically positioned.
     */
    INSIDE_LEFT_VERTICAL,

    /**
     * The spinner buttons are inside the field, on the right hand side and horizontally positioned.
     */
    INSIDE_RIGHT_HORIZONTAL,

    /**
     * The spinner buttons are inside the field, on the left hand side and horizontally positioned.
     */
    INSIDE_LEFT_HORIZONTAL,

    /**
     * The spinner buttons are inside the field, on both sides and horizontally positioned.
     */
    INSIDE_CENTER_HORIZONTAL,

    /**
     * The spinner buttons are outside the field, on the right hand side and vertically positioned. Better use it by
     * adding {@link FormattedTextField#asSpinner()} to a parent.
     */
    OUTSIDE_RIGHT_VERTICAL,

    /**
     * The spinner buttons are outside the field, on the left hand side and vertically positioned. Better use it by
     * adding {@link FormattedTextField#asSpinner()} to a parent.
     */
    OUTSIDE_LEFT_VERTICAL,
    /**
     * The spinner buttons are outside the field, on the horizontal center and vertically positioned. Better use it by
     * adding {@link FormattedTextField#asSpinner()} to a parent.
     */
    OUTSIDE_CENTER_VERTICAL,
    /**
     * The spinner buttons are outside the field, on the right hand side and horizontally positioned. Better use it by
     * adding {@link FormattedTextField#asSpinner()} to a parent.
     */
    OUTSIDE_RIGHT_HORIZONTAL,
    /**
     * The spinner buttons are outside the field, on the left hand side and horizontally positioned. Better use it by
     * adding {@link FormattedTextField#asSpinner()} to a parent.
     */
    OUTSIDE_LEFT_HORIZONTAL,
    /**
     * The spinner buttons are outside the field, on both sides and horizontally positioned. Better use it by
     * adding {@link FormattedTextField#asSpinner()} to a parent.
     */
    OUTSIDE_CENTER_HORIZONTAL,
}

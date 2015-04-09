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
package jidefx.scene.control.decoration;

import javafx.animation.Transition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import jidefx.animation.AnimationType;
import jidefx.animation.AnimationUtils;

/**
 * {@code MutableDecorator} is a decorator whose position information can be changed.
 *
 * @param <T> the type of the decoration node.
 */
@SuppressWarnings("UnusedDeclaration")
public class MutableDecorator<T extends Node> extends Decorator<T> {


    public MutableDecorator() {
        super();
    }

    /**
     * Creates a MutableDecorator at the TOP_RIGHT position.
     *
     * @param node the decoration node.
     */
    public MutableDecorator(T node) {
        this(node, Pos.TOP_RIGHT);
    }

    /**
     * Creates a MutableDecorator at the specified position.
     *
     * @param node the decoration node.
     * @param pos  the position of the decoration node relative to the target node. For example, TOP_RIGHT means the
     *             center of the decoration node will be at the exact top right corner of the target node.
     */
    public MutableDecorator(T node, Pos pos) {
        this(node, pos, new Point2D(0, 0));
    }

    /**
     * Creates a MutableDecorator at the specified position. The decoration node has an offset from the specified Pos.
     *
     * @param node   the decoration node.
     * @param pos    the position of the decoration node relative to the target node. For example, TOP_RIGHT means the
     *               center of the decoration node will be at the exact top right corner of the target node.
     * @param offset the offset of the decoration node. The value in the offset is the percentage of size of the
     *               decoration node.
     */
    public MutableDecorator(T node, Pos pos, Point2D offset) {
        this(node, pos, offset, null);
    }

    /**
     * Creates a MutableDecorator at the specified position with a specified padding added to the target node. The
     * decoration node has an offset from the specified Pos.
     *
     * @param node    the decoration node.
     * @param pos     the position of the decoration node relative to the target node. For example, TOP_RIGHT means the
     *                center of the decoration node will be at the exact top right corner of the target node.
     * @param offset  the offset of the decoration node. The value in the offset is the percentage of size of the
     *                decoration node.
     * @param padding the padding added to the target node. The value in the padding is the percentage of size of the
     *                decoration node.
     */
    public MutableDecorator(T node, Pos pos, Point2D offset, Insets padding) {
        this(node, pos, offset, padding, true);
    }

    /**
     * Creates a MutableDecorator at the specified position with a specified padding added to the target node. The
     * decoration node has an offset from the specified Pos.
     *
     * @param node                   the decoration node.
     * @param pos                    the position of the decoration node relative to the target node. For example,
     *                               TOP_RIGHT means the center of the decoration node will be at the exact top right
     *                               corner of the target node.
     * @param offset                 the offset of the decoration node. The value in the offset is the percentage of
     *                               size of the decoration node if valueInPercentOrPixels is true. Otherwise, it would
     *                               be in pixels.
     * @param padding                the padding added to the target node. The value in the padding is the percentage of
     *                               size of the decoration node if valueInPercentOrPixels is true. Otherwise, it would
     *                               be in pixels.
     * @param valueInPercentOrPixels true if the value in the offset and padding are percentage. Otherwise in pixels.
     */
    public MutableDecorator(T node, Pos pos, Point2D offset, Insets padding, boolean valueInPercentOrPixels) {
        this(node, pos, offset, padding, valueInPercentOrPixels, AnimationType.NONE);
    }

    /**
     * Creates a MutableDecorator at the specified position with a specified padding added to the target node. The
     * decoration node has an offset from the specified Pos.
     *
     * @param node                   the decoration node.
     * @param pos                    the position of the decoration node relative to the target node. For example,
     *                               TOP_RIGHT means the center of the decoration node will be at the exact top right
     *                               corner of the target node.
     * @param offset                 the offset of the decoration node. The value in the offset is the percentage of
     *                               size of the decoration node if valueInPercentOrPixels is true. Otherwise, it would
     *                               be in pixels.
     * @param padding                the padding added to the target node. The value in the padding is the percentage of
     *                               size of the decoration node if valueInPercentOrPixels is true. Otherwise, it would
     *                               be in pixels.
     * @param valueInPercentOrPixels true if the value in the offset and padding are percentage. Otherwise in pixels.
     * @param type                   the animation type as defined in {@link AnimationType}.
     */
    public MutableDecorator(T node, Pos pos, Point2D offset, Insets padding, boolean valueInPercentOrPixels, AnimationType type) {
        this(node, pos, offset, padding, valueInPercentOrPixels, AnimationUtils.createTransition(node, type));
    }

    /**
     * Creates a MutableDecorator at the specified position with a specified padding added to the target node. The
     * decoration node has an offset from the specified Pos.
     *
     * @param node                   the decoration node.
     * @param pos                    the position of the decoration node relative to the target node. For example,
     *                               TOP_RIGHT means the center of the decoration node will be at the exact top right
     *                               corner of the target node.
     * @param offset                 the offset of the decoration node. The value in the offset is the percentage of
     *                               size of the decoration node if valueInPercentOrPixels is true. Otherwise, it would
     *                               be in pixels.
     * @param padding                the padding added to the target node. The value in the padding is the percentage of
     *                               size of the decoration node if valueInPercentOrPixels is true. Otherwise, it would
     *                               be in pixels.
     * @param valueInPercentOrPixels true if the value in the offset and padding are percentage. Otherwise in pixels.
     * @param transition             the transition
     */
    public MutableDecorator(T node, Pos pos, Point2D offset, Insets padding, boolean valueInPercentOrPixels, Transition transition) {
        super(node, pos, offset, padding, valueInPercentOrPixels, transition);
    }

    @Override
    public ObjectProperty<T> nodeProperty() {
        return nodePropertyImpl();
    }

    @Override
    public ObjectProperty<Pos> posProperty() {
        return posPropertyImpl();
    }

    @Override
    public ObjectProperty<Insets> paddingProperty() {
        return paddingPropertyImpl();
    }

    @Override
    public ObjectProperty<Point2D> offsetProperty() {
        return offsetPropertyImpl();
    }

    @Override
    public ObjectProperty<Transition> transitionProperty() {
        return transitionPropertyImpl();
    }

    @Override
    public BooleanProperty valueInPercentProperty() {
        return valueInPercentPropertyImpl();
    }

    /**
     * Sets the position of the decoration node relative to the target node.
     *
     * @param pos the Pos.
     */
    public void setPos(Pos pos) {
        posPropertyImpl().set(pos);
    }

    /**
     * Sets the offset of the decoration node. The value in the offset is the percentage of the size of the decoration
     * node if the {@link Decorator#isValueInPercent()} is true. If not, the values should be in pixels.
     *
     * @param offset the offset.
     */
    public void setPosOffset(Point2D offset) {
        offsetPropertyImpl().set(offset);
    }

    /**
     * Sets the padding to the target node. The value in the padding is the percentage of the size of the decoration
     * node if the {@link Decorator#isValueInPercent()} is true. If not, the values should be in pixels.
     *
     * @param padding the padding.
     */
    public void setPadding(Insets padding) {
        paddingPropertyImpl().set(padding);
    }

    /**
     * Sets the transition.
     *
     * @param transition a new transition.
     */
    public void setTransition(Transition transition) {
        transitionPropertyImpl().set(transition);
    }

    /**
     * Sets the flag if the value is in percentage or pixels.
     *
     * @param valueInPercent true or false.
     */
    public void setValueInPercent(boolean valueInPercent) {
        valueInPercentPropertyImpl().set(valueInPercent);
    }
}

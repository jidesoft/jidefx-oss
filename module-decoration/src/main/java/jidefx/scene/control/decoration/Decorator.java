/*
 * @(#)Decorator.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.decoration;

import javafx.animation.Transition;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import jidefx.animation.AnimationType;
import jidefx.animation.AnimationUtils;

/**
 * {@code Decorator} provides the information for a decorator. 
 * <p/>
 * <pre>{@code
 * DecorationUtils.install(textField, new Decorator(clearButton, Pos.CENTER_RIGHT, new Insets(0, 100, 0, 0), new
 * Point2D(-50, 0), true));
 * DecorationUtils.install(textField, new Decorator(labelButton, Pos.CENTER_LEFT, new Insets(0, 0, 0, 100), new
 * Point2D(50, 0), true));
 * }</pre> The code above will put two buttons inside a TextField on both sides. Both are vertically center aligned.
 * Since the two buttons only have icon, we used CENTER_RIGHT and CENTER_LEFT. If the buttons had text, we would have
 * chosen BASELINE_RIGHT and BASELINE_LEFT so that the text of the buttons will align with the text of the TextField.
 * The two offsets are 50 and -50. This value will move the buttons to inside the TextField. At last, in order to avoid
 * the clipping of the text in the TextField, we add 100% (of the button size) padding on both sides.
 *
 * @param <T> the type of the decoration node.
 */
@SuppressWarnings("UnusedDeclaration")
public class Decorator<T extends Node> {
    protected ReadOnlyObjectWrapper<T> _nodeProperty;
    protected ReadOnlyObjectWrapper<Pos> _posProperty;
    protected ReadOnlyObjectWrapper<Insets> _paddingProperty;
    protected ReadOnlyObjectWrapper<Point2D> _offsetProperty;
    protected ReadOnlyBooleanWrapper _valueInPercentProperty;
    protected ReadOnlyObjectWrapper<Transition> _transitionProperty;

    public Decorator() {
        super();
    }

    /**
     * Creates a Decorator at the TOP_RIGHT position.
     *
     * @param node the decoration node.
     */
    public Decorator(T node) {
        this(node, Pos.TOP_RIGHT);
    }

    /**
     * Creates a Decorator at the specified position.
     *
     * @param node the decoration node.
     * @param pos  the position of the decoration node relative to the target node. For example, TOP_RIGHT means the
     *             center of the decoration node will be at the exact top right corner of the target node.
     */
    public Decorator(T node, Pos pos) {
        this(node, pos, null);
    }

    /**
     * Creates a Decorator at the specified position. The decoration node has an offset from the specified Pos.
     *
     * @param node   the decoration node.
     * @param pos    the position of the decoration node relative to the target node. For example, TOP_RIGHT means the
     *               center of the decoration node will be at the exact top right corner of the target node.
     * @param offset the offset of the decoration node. The value in the offset is the percentage of size of the
     *               decoration node.
     */
    public Decorator(T node, Pos pos, Point2D offset) {
        this(node, pos, offset, null);
    }

    /**
     * Creates a Decorator at the specified position. The decoration node has an offset from the specified Pos.
     *
     * @param node   the decoration node.
     * @param pos    the position of the decoration node relative to the target node. For example, TOP_RIGHT means the
     *               center of the decoration node will be at the exact top right corner of the target node.
     * @param offset the offset of the decoration node. The value in the offset is the percentage of size of the
     *               decoration node.
     * @param padding                the padding added to the target node. The value in the padding is the percentage of
     *                               size of the decoration node if valueInPercentOrPixels is true. Otherwise, it would
     *                               be in pixels.
     */
    public Decorator(T node, Pos pos, Point2D offset, Insets padding) {
        this(node, pos, offset, padding, AnimationType.NONE);
    }

    /**
     * Creates a Decorator at the specified position with a specified padding added to the target node. The
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
     * @param type                   the animation type as defined in {@link AnimationType}.
     */
    public Decorator(T node, Pos pos, Point2D offset, Insets padding, AnimationType type) {
        this(node, pos, offset, padding, true, AnimationUtils.createTransition(node, type));
    }

    /**
     * Creates a Decorator at the specified position with a specified padding added to the target node. The
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
    public Decorator(T node, Pos pos, Point2D offset, Insets padding, boolean valueInPercentOrPixels, Transition transition) {
        nodePropertyImpl().set(node);
        posPropertyImpl().set(pos == null ? Pos.TOP_RIGHT : pos);
        offsetPropertyImpl().set(offset == null ? new Point2D(0, 0) : offset);
        valueInPercentPropertyImpl().set(valueInPercentOrPixels);
        paddingPropertyImpl().set(padding);
        transitionPropertyImpl().set(transition);
    }

    /**
     * A read-only property for the decoration node.
     *
     * @return a read-only property for the decoration node.
     */
    public ReadOnlyObjectProperty<T> nodeProperty() {
        return nodePropertyImpl().getReadOnlyProperty();
    }

    protected ReadOnlyObjectWrapper<T> nodePropertyImpl() {
        if (_nodeProperty == null) {
            _nodeProperty = new ReadOnlyObjectWrapper<>();
        }
        return _nodeProperty;
    }

    /**
     * Gets the decoration node.
     *
     * @return the decoration node.
     */
    public T getNode() {
        return nodePropertyImpl().get();
    }

    public ReadOnlyObjectProperty<Pos> posProperty() {
        return posPropertyImpl().getReadOnlyProperty();
    }

    protected ReadOnlyObjectWrapper<Pos> posPropertyImpl() {
        if (_posProperty == null) {
            _posProperty = new ReadOnlyObjectWrapper<>(Pos.TOP_RIGHT);
        }
        return _posProperty;
    }

    /**
     * Gets the position of the decoration. The javafx.geometry.Pos relative to
     * the target node. The position uses the center of the decoration as the
     * anchor point. For example, if the Pos is TOP_RIGHT, it means the center
     * of the decoration is exactly at the top right corner of the target node.
     *
     * @return the position of the decoration.
     */
    public Pos getPos() {
        return posProperty().get();
    }


    public ReadOnlyObjectProperty<Point2D> offsetProperty() {
        return offsetPropertyImpl().getReadOnlyProperty();
    }

    protected ReadOnlyObjectWrapper<Point2D> offsetPropertyImpl() {
        if (_offsetProperty == null) {
            _offsetProperty = new ReadOnlyObjectWrapper<>(new Point2D(0, 0));
        }
        return _offsetProperty;
    }

    /**
     * Gets the position offset from the specified Pos. It allows you to move
     * the decoration node by an offset on both x and y directions from the
     * position defined by the getPos. The offset value could be pixels or a
     * percentage of the size of the decoration node. If it is percentage, the x
     * value is the percentage of the width of the target node, the y is that of
     * the height.
     *
     * @return the position offset from the specified Pos.
     */
    public Point2D getPosOffset() {
        return offsetProperty().get();
    }

    public ReadOnlyObjectProperty<Insets> paddingProperty() {
        return paddingPropertyImpl().getReadOnlyProperty();
    }

    protected ReadOnlyObjectWrapper<Insets> paddingPropertyImpl() {
        if (_paddingProperty == null) {
            _paddingProperty = new ReadOnlyObjectWrapper<>();
        }
        return _paddingProperty;
    }

    /**
     * Gets the padding that will be applied to the node that is decorated.  The
     * value could be pixel based or percentage of the size of the decoration
     * node depending on the value of {@link #isValueInPercent()}. You could
     * return null if you don't mind the decoration node overlaps with the
     * target node, which might cover some of the content of the target node.
     *
     * @return the padding for the decorated node.
     */
    public Insets getPadding() {
        return paddingProperty().get();
    }

    public ReadOnlyObjectProperty<Transition> transitionProperty() {
        return transitionPropertyImpl().getReadOnlyProperty();
    }

    protected ReadOnlyObjectWrapper<Transition> transitionPropertyImpl() {
        if (_transitionProperty == null) {
            _transitionProperty = new ReadOnlyObjectWrapper<Transition>() {
                @Override
                protected void invalidated() {
                    super.invalidated();
                    DecorationUtils.setAnimationPlayed(getNode(), false);
                }
            };
        }
        return _transitionProperty;
    }

    public ReadOnlyBooleanProperty valueInPercentProperty() {
        return valueInPercentPropertyImpl().getReadOnlyProperty();
    }

    protected ReadOnlyBooleanWrapper valueInPercentPropertyImpl() {
        if (_valueInPercentProperty == null) {
            _valueInPercentProperty = new ReadOnlyBooleanWrapper(true);
        }
        return _valueInPercentProperty;
    }

    /**
     * Checks if the PosOffset and Padding is pixel based or percent based.
     *
     * @return true if the values are percentage of the decoration node size.
     *         False if the values are pixels.
     */
    public boolean isValueInPercent() {
        return valueInPercentProperty().get();
    }

    /**
     * Gets the transition. The transition will be used to animate the decoration node. For example, a bounce effect to
     * bring user attention, a fade in effect to bring a sense of changing smoothly.
     *
     * @return the Transition.
     */
    public Transition getTransition() {
        return transitionProperty().get();
    }

//    @Override
//    public void registerSensitiveListeners() {
//        if (isSensitive() && getDecorationPane() != null) {
//            if (_changeListener == null) {
//                _changeListener = new ChangeListener() {
//                    @Override
//                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                        _decorationPane.requestLayout();
//                    }
//                };
//            }
//            nodeProperty().removeListener(_changeListener);
//            posProperty().removeListener(_changeListener);
//            paddingProperty().removeListener(_changeListener);
//            offsetProperty().removeListener(_changeListener);
//            valueInPercentProperty().removeListener(_changeListener);
//
//            nodeProperty().addListener(_changeListener);
//            posProperty().addListener(_changeListener);
//            paddingProperty().addListener(_changeListener);
//            offsetProperty().addListener(_changeListener);
//            valueInPercentProperty().addListener(_changeListener);
//        }
//    }
//
//    @Override
//    public void clearSensitiveListeners() {
//        if (_changeListener != null) {
//            nodeProperty().removeListener(_changeListener);
//            posProperty().removeListener(_changeListener);
//            paddingProperty().removeListener(_changeListener);
//            offsetProperty().removeListener(_changeListener);
//            valueInPercentProperty().removeListener(_changeListener);
//
//            _changeListener = null;
//        }
//    }
}

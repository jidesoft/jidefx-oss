/*
 * @(#)BalloonPopupOutline.java 6/4/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.popup;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.StrokeType;
import javafx.scene.shape.VLineTo;

/**
 * {@code BalloonPopupOutline} is a balloon shaped {@link javafx.scene.shape.Path} which can be used to display a balloon-like popup. The
 * size, shape, the arrow size and position can be customized using various properties on this class.
 */
public class BalloonPopupOutline extends PopupOutline {
    private ObjectProperty<Side> _arrowSide;

    private DoubleProperty _widthProperty;

    private DoubleProperty _heightProperty;

    private DoubleProperty _roundedRadiusProperty;

    private DoubleProperty _arrowPositionProperty;

    private DoubleProperty _arrowBasePositionProperty;

    private DoubleProperty _arrowWidthProperty;

    private DoubleProperty _arrowHeightProperty;

    public BalloonPopupOutline() {
        createPath();
        initializeStyle();
    }

    protected void initializeStyle() {
        getStyleClass().add("balloon-popup-outline");
    }

    public DoubleProperty widthProperty() {
        if (_widthProperty == null) {
            _widthProperty = new SimpleDoubleProperty();
        }
        return _widthProperty;
    }

    /**
     * Gets the width of the balloon content.
     *
     * @return the width of the balloon content.
     */
    public double getWidth() {
        return widthProperty().get();
    }

    /**
     * Sets the width of the balloon content.
     *
     * @param width the width of the balloon content.
     */
    public void setWidth(double width) {
        widthProperty().set(width);
    }

    public DoubleProperty heightProperty() {
        if (_heightProperty == null) {
            _heightProperty = new SimpleDoubleProperty();
        }
        return _heightProperty;
    }

    /**
     * Gets the height of the balloon content.
     *
     * @return the height of the balloon content.
     */
    public double getHeight() {
        return heightProperty().get();
    }


    /**
     * Sets the height of the balloon content.
     *
     * @param height the height of the balloon content.
     */
    public void setHeight(double height) {
        heightProperty().set(height);
    }

    public DoubleProperty roundedRadiusProperty() {
        if (_roundedRadiusProperty == null) {
            _roundedRadiusProperty = new SimpleDoubleProperty(8.0);
        }
        return _roundedRadiusProperty;
    }

    /**
     * Gets the radius of the rounded rectangle corner of the balloon content.
     *
     * @return the radius of the rounded rectangle corner.
     */
    public double getRoundedRadius() {
        return roundedRadiusProperty().get();
    }

    /**
     * Sets the radius of the rounded rectangle corner of the balloon content.
     *
     * @param roundedRadius the radius of the rounded rectangle corner.
     */
    public void setRoundedRadius(double roundedRadius) {
        roundedRadiusProperty().set(roundedRadius);
    }

    public DoubleProperty arrowPositionProperty() {
        if (_arrowPositionProperty == null) {
            _arrowPositionProperty = new SimpleDoubleProperty(0.1);
        }
        return _arrowPositionProperty;
    }

    /**
     * Gets the arrow position relative to the balloon content.
     *
     * @return the arrow position relative to the balloon content.
     */
    public double getArrowPosition() {
        return arrowPositionProperty().get();
    }

    /**
     * Sets the arrow position relative to the balloon content. It is a value from 0 to 1. 0 means the arrow is at the
     * starting position of the content (which is the top corner for the EAST/WEST side arrow and the left corner of a
     * NORTH/SOUTH side arrow) and 1 means at the ending position of the content. Typically, you want to use a value
     * between 0.1 and 0.9. Default value is 0.1.
     *
     * @param arrowPosition arrow position relative to the balloon content.
     */
    public void setArrowPosition(double arrowPosition) {
        arrowPositionProperty().set(arrowPosition);
    }

    public DoubleProperty arrowBasePositionProperty() {
        if (_arrowBasePositionProperty == null) {
            _arrowBasePositionProperty = new SimpleDoubleProperty(0.1);
        }
        return _arrowBasePositionProperty;
    }

    /**
     * Gets the arrow base position relative to the balloon content.
     *
     * @return the arrow position relative to the balloon content.
     */
    public double getArrowBasePosition() {
        return arrowBasePositionProperty().get();
    }

    /**
     * Sets the arrow base position relative to the balloon content. It is a value from 0 to 1. 0 means the arrow is at
     * the starting position of the content (which is the top corner for the EAST/WEST side arrow and the left corner of
     * a NORTH/SOUTH side arrow) and 1 means at the ending position of the content. Typically, you want to use a value
     * between 0.1 and 0.9. Default value is 0.1.
     *
     * @param arrowBasePosition arrow base position relative to the balloon content.
     */
    public void setArrowBasePosition(double arrowBasePosition) {
        arrowBasePositionProperty().set(arrowBasePosition);
    }

    public DoubleProperty arrowHeightProperty() {
        if (_arrowHeightProperty == null) {
            _arrowHeightProperty = new SimpleDoubleProperty(10);
        }
        return _arrowHeightProperty;
    }

    /**
     * Gets the height of the arrow, in pixels.
     *
     * @return the height of the arrow.
     */
    public double getArrowHeight() {
        return arrowHeightProperty().get();
    }

    /**
     * Sets the height of the arrow, in pixels.
     *
     * @param arrowHeight the height of the arrow.
     */
    public void setArrowHeight(double arrowHeight) {
        arrowHeightProperty().set(arrowHeight);
    }

    public DoubleProperty arrowWidthProperty() {
        if (_arrowWidthProperty == null) {
            _arrowWidthProperty = new SimpleDoubleProperty(12);
        }
        return _arrowWidthProperty;
    }

    /**
     * Gets the width of the arrow base, in pixels.
     *
     * @return the width of the arrow base.
     */
    public double getArrowWidth() {
        return arrowWidthProperty().get();
    }

    /**
     * Sets the width of the arrow base, in pixels.
     *
     * @param arrowWidth the width of the arrow base.
     */
    public void setArrowWidth(double arrowWidth) {
        arrowWidthProperty().set(arrowWidth);
    }

    public ObjectProperty<Side> arrowSideProperty() {
        if (_arrowSide == null) {
            _arrowSide = new SimpleObjectProperty<Side>(Side.RIGHT) {
                @Override
                protected void invalidated() {
                    createPath();
                }
            };
        }
        return _arrowSide;
    }

    /**
     * Gets the arrow side.
     *
     * @return the side of the arrow.
     */
    public Side getArrowSide() {
        return arrowSideProperty().get();
    }

    /**
     * Sets the arrow side.
     *
     * @param side the side of the arrow.
     */
    public void setArrowSide(Side side) {
        arrowSideProperty().set(side);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public Insets getContentPadding() {
        double arrowHeight = getArrowHeight();
        switch (getArrowSide()) {
            case RIGHT:
                return new Insets(0, arrowHeight, 0, 0);
            case LEFT:
                return new Insets(0, 0, 0, arrowHeight);
            case TOP:
                return new Insets(arrowHeight, 0, 0, 0);
            case BOTTOM:
                return new Insets(0, 0, arrowHeight, 0);
        }
        return new Insets(0);
    }

    /**
     * Gets the location of the arrow pointer in the local coordinates.
     *
     * @return the location of the arrow pointer.
     */
    public Point2D getOriginPoint() {
        double width = getWidth();
        double height = getHeight();
        double arrowPosition = getArrowPosition();
        switch (getArrowSide()) {
            case RIGHT:
                return new Point2D(width, height * arrowPosition);
            case LEFT:
                return new Point2D(0, height * arrowPosition);
            case TOP:
                return new Point2D(width * arrowPosition, 0);
            case BOTTOM:
                return new Point2D(width * arrowPosition, height);
        }
        return null;
    }

    public void createPath() {
        getElements().clear();
        setStrokeType(StrokeType.INSIDE);

        switch (getArrowSide()) {
            case RIGHT: {
                MoveTo startPoint = new MoveTo();
                startPoint.xProperty().bind(roundedRadiusProperty());
                startPoint.setY(0.0f);

                HLineTo topLine = new HLineTo();
                topLine.xProperty().bind(widthProperty().subtract(roundedRadiusProperty()).subtract(arrowHeightProperty()));

                ArcTo trArc = new ArcTo();
                trArc.setSweepFlag(true);
                trArc.xProperty().bind(widthProperty().subtract(arrowHeightProperty()));
                trArc.yProperty().bind(roundedRadiusProperty());
                trArc.radiusXProperty().bind(roundedRadiusProperty());
                trArc.radiusYProperty().bind(roundedRadiusProperty());

                VLineTo rightLineTop = new VLineTo();
                rightLineTop.yProperty().bind(heightProperty().multiply(arrowBasePositionProperty()).subtract(arrowWidthProperty().divide(2)));

                LineTo arrowTop = new LineTo();
                arrowTop.xProperty().bind((widthProperty()));
                arrowTop.yProperty().bind(heightProperty().multiply(arrowPositionProperty()));

                LineTo arrowBottom = new LineTo();
                arrowBottom.xProperty().bind(widthProperty().subtract(arrowHeightProperty()));
                arrowBottom.yProperty().bind(heightProperty().multiply(arrowBasePositionProperty()).add(arrowWidthProperty().divide(2)));

                VLineTo rightLineBottom = new VLineTo();
                rightLineBottom.yProperty().bind(heightProperty().subtract(roundedRadiusProperty()));

                ArcTo brArc = new ArcTo();
                brArc.setSweepFlag(true);
                brArc.xProperty().bind(widthProperty().subtract(arrowHeightProperty()).subtract(roundedRadiusProperty()));
                brArc.yProperty().bind(heightProperty());
                brArc.radiusXProperty().bind(roundedRadiusProperty());
                brArc.radiusYProperty().bind(roundedRadiusProperty());

                HLineTo bottomLine = new HLineTo();
                bottomLine.xProperty().bind(roundedRadiusProperty());

                ArcTo blArc = new ArcTo();
                blArc.setSweepFlag(true);
                blArc.setX(0);
                blArc.yProperty().bind(heightProperty().subtract(roundedRadiusProperty()));
                blArc.radiusXProperty().bind(roundedRadiusProperty());
                blArc.radiusYProperty().bind(roundedRadiusProperty());

                VLineTo leftLine = new VLineTo();
                leftLine.yProperty().bind(roundedRadiusProperty());

                ArcTo tlArc = new ArcTo();
                tlArc.setSweepFlag(true);
                tlArc.xProperty().bind(startPoint.xProperty());  // close the path
                tlArc.yProperty().bind(startPoint.yProperty());
                tlArc.radiusXProperty().bind(roundedRadiusProperty());
                tlArc.radiusYProperty().bind(roundedRadiusProperty());

                getElements().addAll(startPoint, topLine, trArc, rightLineTop, arrowTop, arrowBottom, rightLineBottom, brArc, bottomLine, blArc, leftLine, tlArc);
                break;
            }
            case BOTTOM: {
                MoveTo startPoint = new MoveTo();
                startPoint.xProperty().bind(roundedRadiusProperty());
                startPoint.setY(0.0f);

                HLineTo topLine = new HLineTo();
                topLine.xProperty().bind(widthProperty().subtract(roundedRadiusProperty()));

                ArcTo trArc = new ArcTo();
                trArc.setSweepFlag(true);
                trArc.xProperty().bind(widthProperty());
                trArc.yProperty().bind(roundedRadiusProperty());
                trArc.radiusXProperty().bind(roundedRadiusProperty());
                trArc.radiusYProperty().bind(roundedRadiusProperty());

                VLineTo rightLine = new VLineTo();
                rightLine.yProperty().bind(heightProperty().subtract(arrowHeightProperty()).subtract(roundedRadiusProperty()));

                ArcTo brArc = new ArcTo();
                brArc.setSweepFlag(true);
                brArc.xProperty().bind(widthProperty().subtract(roundedRadiusProperty()));
                brArc.yProperty().bind(heightProperty().subtract(arrowHeightProperty()));
                brArc.radiusXProperty().bind(roundedRadiusProperty());
                brArc.radiusYProperty().bind(roundedRadiusProperty());

                HLineTo bottomLineRight = new HLineTo();
                bottomLineRight.xProperty().bind(widthProperty().multiply(arrowBasePositionProperty()).add(arrowWidthProperty().divide(2)));

                LineTo arrowRight = new LineTo();
                arrowRight.xProperty().bind(widthProperty().multiply(arrowPositionProperty()));
                arrowRight.yProperty().bind(heightProperty());

                LineTo arrowLeft = new LineTo();
                arrowLeft.xProperty().bind(widthProperty().multiply(arrowBasePositionProperty()).subtract(arrowWidthProperty().divide(2)));
                arrowLeft.yProperty().bind(heightProperty().subtract(arrowHeightProperty()));

                HLineTo bottomLineLeft = new HLineTo();
                bottomLineLeft.xProperty().bind(roundedRadiusProperty());

                ArcTo blArc = new ArcTo();
                blArc.setSweepFlag(true);
                blArc.setX(0);
                blArc.yProperty().bind(heightProperty().subtract(arrowHeightProperty()).subtract(roundedRadiusProperty()));
                blArc.radiusXProperty().bind(roundedRadiusProperty());
                blArc.radiusYProperty().bind(roundedRadiusProperty());

                VLineTo leftLine = new VLineTo();
                leftLine.yProperty().bind(roundedRadiusProperty());

                ArcTo tlArc = new ArcTo();
                tlArc.setSweepFlag(true);
                tlArc.xProperty().bind(startPoint.xProperty());  // close the path
                tlArc.yProperty().bind(startPoint.yProperty());
                tlArc.radiusXProperty().bind(roundedRadiusProperty());
                tlArc.radiusYProperty().bind(roundedRadiusProperty());

                getElements().addAll(startPoint, topLine, trArc, rightLine, brArc, bottomLineRight, arrowRight, arrowLeft, bottomLineLeft, blArc, leftLine, tlArc);
                break;
            }
            case LEFT: {
                MoveTo startPoint = new MoveTo();
                startPoint.xProperty().bind(roundedRadiusProperty().add(arrowHeightProperty()));
                startPoint.setY(0.0f);

                HLineTo topLine = new HLineTo();
                topLine.xProperty().bind(widthProperty().subtract(roundedRadiusProperty()));

                ArcTo trArc = new ArcTo();
                trArc.setSweepFlag(true);
                trArc.xProperty().bind(widthProperty());
                trArc.yProperty().bind(roundedRadiusProperty());
                trArc.radiusXProperty().bind(roundedRadiusProperty());
                trArc.radiusYProperty().bind(roundedRadiusProperty());

                VLineTo rightLine = new VLineTo();
                rightLine.yProperty().bind(heightProperty().subtract(roundedRadiusProperty()));

                ArcTo brArc = new ArcTo();
                brArc.setSweepFlag(true);
                brArc.xProperty().bind(widthProperty().subtract(roundedRadiusProperty()));
                brArc.yProperty().bind(heightProperty());
                brArc.radiusXProperty().bind(roundedRadiusProperty());
                brArc.radiusYProperty().bind(roundedRadiusProperty());

                HLineTo bottomLine = new HLineTo();
                bottomLine.xProperty().bind(roundedRadiusProperty().add(arrowHeightProperty()));

                ArcTo blArc = new ArcTo();
                blArc.setSweepFlag(true);
                blArc.xProperty().bind(arrowHeightProperty());
                blArc.yProperty().bind(heightProperty().subtract(roundedRadiusProperty()));
                blArc.radiusXProperty().bind(roundedRadiusProperty());
                blArc.radiusYProperty().bind(roundedRadiusProperty());

                VLineTo leftLineBottom = new VLineTo();
                leftLineBottom.yProperty().bind(heightProperty().multiply(arrowBasePositionProperty()).add(arrowWidthProperty().divide(2)));

                LineTo arrowBottom = new LineTo();
                arrowBottom.setX(0.0);
                arrowBottom.yProperty().bind(heightProperty().multiply(arrowPositionProperty()));

                LineTo arrowTop = new LineTo();
                arrowTop.xProperty().bind(arrowHeightProperty());
                arrowTop.yProperty().bind(heightProperty().multiply(arrowBasePositionProperty()).subtract(arrowWidthProperty().divide(2)));

                VLineTo leftLineTop = new VLineTo();
                leftLineTop.yProperty().bind(roundedRadiusProperty());

                ArcTo tlArc = new ArcTo();
                tlArc.setSweepFlag(true);
                tlArc.xProperty().bind(startPoint.xProperty());  // close the path
                tlArc.yProperty().bind(startPoint.yProperty());
                tlArc.radiusXProperty().bind(roundedRadiusProperty());
                tlArc.radiusYProperty().bind(roundedRadiusProperty());

                getElements().addAll(startPoint, topLine, trArc, rightLine, brArc, bottomLine, blArc, leftLineBottom, arrowBottom, arrowTop, leftLineTop, tlArc);
                break;
            }
            case TOP: {
                MoveTo startPoint = new MoveTo();
                startPoint.xProperty().bind(roundedRadiusProperty());
                startPoint.yProperty().bind(arrowHeightProperty());

                HLineTo topLineLeft = new HLineTo();
                topLineLeft.xProperty().bind(widthProperty().multiply(arrowBasePositionProperty()).subtract(arrowWidthProperty().divide(2)));

                LineTo arrowLeft = new LineTo();
                arrowLeft.xProperty().bind(widthProperty().multiply(arrowPositionProperty()));
                arrowLeft.setY(0.0);

                LineTo arrowRight = new LineTo();
                arrowRight.xProperty().bind(widthProperty().multiply(arrowBasePositionProperty()).add(arrowWidthProperty().divide(2)));
                arrowRight.yProperty().bind(arrowHeightProperty());

                HLineTo topLineRight = new HLineTo();
                topLineRight.xProperty().bind(widthProperty().subtract(roundedRadiusProperty()));

                ArcTo trArc = new ArcTo();
                trArc.setSweepFlag(true);
                trArc.xProperty().bind(widthProperty());
                trArc.yProperty().bind(roundedRadiusProperty().add(arrowHeightProperty()));
                trArc.radiusXProperty().bind(roundedRadiusProperty());
                trArc.radiusYProperty().bind(roundedRadiusProperty());

                VLineTo rightLine = new VLineTo();
                rightLine.yProperty().bind(heightProperty().subtract(roundedRadiusProperty()));

                ArcTo brArc = new ArcTo();
                brArc.setSweepFlag(true);
                brArc.xProperty().bind(widthProperty().subtract(roundedRadiusProperty()));
                brArc.yProperty().bind(heightProperty());
                brArc.radiusXProperty().bind(roundedRadiusProperty());
                brArc.radiusYProperty().bind(roundedRadiusProperty());

                HLineTo bottomLine = new HLineTo();
                bottomLine.xProperty().bind(roundedRadiusProperty());

                ArcTo blArc = new ArcTo();
                blArc.setSweepFlag(true);
                blArc.setX(0.0);
                blArc.yProperty().bind(heightProperty().subtract(roundedRadiusProperty()));
                blArc.radiusXProperty().bind(roundedRadiusProperty());
                blArc.radiusYProperty().bind(roundedRadiusProperty());

                VLineTo leftLine = new VLineTo();
                leftLine.yProperty().bind(arrowHeightProperty().add(roundedRadiusProperty()));

                ArcTo tlArc = new ArcTo();
                tlArc.setSweepFlag(true);
                tlArc.xProperty().bind(startPoint.xProperty());  // close the path
                tlArc.yProperty().bind(startPoint.yProperty());
                tlArc.radiusXProperty().bind(roundedRadiusProperty());
                tlArc.radiusYProperty().bind(roundedRadiusProperty());

                getElements().addAll(startPoint, topLineLeft, arrowLeft, arrowRight, topLineRight, trArc, rightLine, brArc, bottomLine, blArc, leftLine, tlArc);
                break;
            }
        }
    }
}

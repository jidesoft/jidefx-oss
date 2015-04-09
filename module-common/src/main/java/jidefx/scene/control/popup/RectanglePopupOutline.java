/*
 * @(#)RectanglePopupOutline.java 6/4/2013
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
import javafx.geometry.Pos;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.StrokeType;
import javafx.scene.shape.VLineTo;

/**
 * {@code RectanglePopupOutline} is a rectangle shaped {@link javafx.scene.shape.Path} which can be used to display a
 * rectangle or rounded rectangle popup.
 */
    public class RectanglePopupOutline extends PopupOutline {
    private ObjectProperty<Pos> _originPositionProperty;

    private DoubleProperty _widthProperty;

    private DoubleProperty _heightProperty;

    private DoubleProperty _roundedRadiusProperty;

    public RectanglePopupOutline() {
        createPath();
        initializeStyle();
    }

    protected void initializeStyle() {
        getStyleClass().add("rectangle-popup-outline");
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

    public ObjectProperty<Pos> originPositionProperty() {
        if (_originPositionProperty == null) {
            _originPositionProperty = new SimpleObjectProperty<>(Pos.CENTER);
        }
        return _originPositionProperty;
    }

    /**
     * Gets the origin point position.
     *
     * @return the origin point position.
     */
    public Pos getOriginPosition() {
        return originPositionProperty().get();
    }

    /**
     * Sets origin point position.
     *
     * @param pos the origin point position.
     */
    public void setOriginPosition(Pos pos) {
        originPositionProperty().set(pos);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public Insets getContentPadding() {
        return new Insets(0);
    }

    /**
     * Gets the location of the origin point in the local coordinates.
     *
     * @return the location of the origin point.
     */
    public Point2D getOriginPoint() {
        double width = getWidth();
        double height = getHeight();
        Pos originPosition = getOriginPosition();
        switch (originPosition) {
            case TOP_LEFT:
                return new Point2D(0, 0);
            case TOP_CENTER:
                return new Point2D(width / 2, 0);
            case TOP_RIGHT:
                return new Point2D(width, 0);
            case BASELINE_LEFT:
            case CENTER_LEFT:
                return new Point2D(0, height / 2);
            case CENTER:
            case BASELINE_CENTER:
                return new Point2D(width / 2, height / 2);
            case CENTER_RIGHT:
            case BASELINE_RIGHT:
                return new Point2D(width, height / 2);
            case BOTTOM_LEFT:
                return new Point2D(0, height);
            case BOTTOM_CENTER:
                return new Point2D(width / 2, height);
            case BOTTOM_RIGHT:
                return new Point2D(width, height);
        }
        return null;
    }

    protected void createPath() {
        getElements().clear();
        setStrokeType(StrokeType.INSIDE);

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

        getElements().addAll(startPoint, topLine, trArc, rightLine, brArc, bottomLine, blArc, leftLine, tlArc);
    }
}

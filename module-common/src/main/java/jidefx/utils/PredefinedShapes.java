/*
 * @(#)ShapeUtils.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;

/**
 * A collection of pre-defined shapes. All the methods take a size as parameter and returns a Node which is a Shape or a
 * Group. You can set the fill color, stroke color on the shape if you would like to adjust the colors.
 * <p/>
 * We created this class mainly because we need to use some icons inside our products. Ideally it probably should
 * support svg files. Since we are not using a whole lot of icons, we decided to create them using Java code to avoid
 * the dependency on a svg reader.
 */
public class PredefinedShapes {
    private static PredefinedShapes _instance = new PredefinedShapes();

    /**
     * Gets the default instance of PredefinedShapes.
     *
     * @return the default instance of PredefinedShapes.
     * @see #setInstance(PredefinedShapes)
     */
    public static PredefinedShapes getInstance() {
        return _instance;
    }

    /**
     * Sets your own instance of PredefinedShapes if you want to customize the predefined shapes.
     *
     * @param instance a new instance.
     */
    public static void setInstance(PredefinedShapes instance) {
        _instance = instance;
    }

    private final static String STYLE_CLASS_DEFAULT = "predefined-shape"; //NON-NLS

    /**
     * Creates an arrow icon.
     *
     * @param size the size of the icon
     * @return an arrow icon.
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public Shape createArrowIcon(double size) {
        Color strokeColor = new Color(0.3, 0.3, 0.3, 1);
        Color fillColor = new Color(0.2, 0.2, 0.2, 1);

        double width = size;
        double height = size / 2;
        double strokeWidth = 0.0;

        Polygon triangle = new Polygon(0.0, 0.0,
                width, 0.0,
                height, width / 2,
                0.0, 0.0
        );
        triangle.setStroke(strokeColor);
        triangle.setStrokeLineCap(StrokeLineCap.ROUND);
        triangle.setStrokeWidth(strokeWidth);
        triangle.setFill(fillColor);

        triangle.setPickOnBounds(true);
        triangle.getStyleClass().addAll(STYLE_CLASS_DEFAULT, "predefined-filter-icon"); //NON-NLS

        return triangle;
    }

    /**
     * Create a clear node with a circle plus a cross inside within an area
     *
     * @param size the size of icon
     * @return a clear icon.
     */
    public Shape createClearIcon(double size) {
        Color circleFillColor = new Color(0.5, 0.5, 0.5, 1);

        double radius = size / 2;
        double crossStrokeWidth = size * 0.2;
        double crossHalfLength = radius * 0.4;

        Circle circle = new Circle(radius, radius, radius);
        circle.setStrokeWidth(0.0);

        Line crossLineLeft = new Line(radius - crossHalfLength,
                radius - crossHalfLength,
                radius + crossHalfLength,
                radius + crossHalfLength);
        crossLineLeft.setStrokeWidth(crossStrokeWidth);
        crossLineLeft.setStrokeLineCap(StrokeLineCap.ROUND);

        Line crossLineRight = new Line(radius - crossHalfLength,
                radius + crossHalfLength,
                radius + crossHalfLength,
                radius - crossHalfLength);
        crossLineRight.setStrokeWidth(crossStrokeWidth);
        crossLineRight.setStrokeLineCap(StrokeLineCap.ROUND);

        Shape shape = Shape.subtract(Shape.subtract(circle, crossLineLeft), crossLineRight);
        shape.setFill(circleFillColor);
        shape.setPickOnBounds(true);
        shape.getStyleClass().addAll(STYLE_CLASS_DEFAULT, "predefined-clear-icon"); //NON-NLS
        return shape;
    }

    /**
     * Create a close node with a circle plus a cross inside within an area. It is a group with three nodes in it.
     *
     * @param size the size of icon
     * @return a close icon.
     */
    public Group createCloseIcon(double size) {
        Color fillColor = new Color(0, 0, 0, 1);
        Color strokeColor = new Color(1, 1, 1, 1);

        double radius = size / 2;
        double strokeWidth = size * 0.1;
        double crossHalfLength = radius * 0.4;

        Circle circle = new Circle(radius, radius, radius);
        circle.setStrokeWidth(strokeWidth);
        circle.setFill(fillColor);
        circle.setStroke(strokeColor);

        Line crossLineLeft = new Line(radius - crossHalfLength,
                radius - crossHalfLength,
                radius + crossHalfLength,
                radius + crossHalfLength);
        crossLineLeft.setStrokeWidth(strokeWidth);
        crossLineLeft.setStrokeLineCap(StrokeLineCap.ROUND);
        crossLineLeft.setStrokeType(StrokeType.CENTERED);
        crossLineLeft.setStroke(strokeColor);
        crossLineLeft.setFill(strokeColor);

        Line crossLineRight = new Line(
                radius - crossHalfLength,
                radius + crossHalfLength,
                radius + crossHalfLength,
                radius - crossHalfLength);
        crossLineRight.setStrokeWidth(strokeWidth);
        crossLineRight.setStrokeLineCap(StrokeLineCap.ROUND);
        crossLineRight.setStrokeType(StrokeType.CENTERED);
        crossLineRight.setStroke(strokeColor);
        crossLineRight.setFill(strokeColor);

        Group node = new Group(circle, crossLineRight, crossLineLeft);
        node.setPickOnBounds(true);
        node.getStyleClass().addAll(STYLE_CLASS_DEFAULT, "predefined-close-icon"); //NON-NLS
        return node;
    }

    /**
     * Creates a magnifier glass icon.
     *
     * @param size the size of the icon
     * @return a magnifier icon.
     */
    public Shape createMagnifierIcon(double size) {
        return createMagnifierIcon(size, false);
    }

    /**
     * Creates a magnifier glass icon with or without a down arrow.
     *
     * @param size      the size of the icon
     * @param showArrow true to include a down arrow. False to not include one.
     * @return a magnifier icon.
     */
    public Shape createMagnifierIcon(double size, boolean showArrow) {
        Color strokeColor = new Color(0.3, 0.3, 0.3, 1);
        Color fillColor = new Color(1, 1, 1, 0);

        double ratio = 0.6;  // ratio of the circle size (diameter) to the whole icon size

        double strokeWidth = size / 10;
        double circleRadius = size * ratio / 2;

        Circle mirror = new Circle(circleRadius, circleRadius, circleRadius);

        Line handle = new Line(circleRadius * 2,
                circleRadius,
                size,
                circleRadius);

        Shape magnifier = Shape.union(mirror, handle);
        magnifier.setRotate(135);

        Shape shape;
        if (showArrow) {
            Shape arrow = createArrowIcon(3);
            arrow.setLayoutX(size);
            arrow.setLayoutY(size - 7);
            shape = Shape.union(magnifier, arrow);
        }
        else {
            shape = magnifier;
        }

        shape.setStrokeWidth(strokeWidth);
        shape.setStroke(strokeColor);
        shape.setFill(fillColor);
        shape.setPickOnBounds(true);
        shape.getStyleClass().addAll(STYLE_CLASS_DEFAULT, "predefined-magnifier-icon"); //NON-NLS

        return shape;
    }

    /**
     * Creates a filter icon.
     *
     * @param size the size of the icon
     * @return a filter icon.
     */
    public Shape createFilterIcon(double size) {
        Color strokeColor = new Color(0.3, 0.3, 0.3, 1);
        Color fillColor = new Color(1, 1, 1, 0);

        double strokeWidth = size / 20 + 1;

        double top = 0.35;
        double footTilt = 0.2;

        Polygon filter = new Polygon(0, 0,
                size * top, size * top,
                size * top, size,
                size * (1 - top), size * (1 - footTilt),
                size * (1 - top), size * top,
                size, 0
        );
        filter.setStroke(strokeColor);
        filter.setStrokeWidth(strokeWidth);
        filter.setFill(fillColor);

        filter.setPickOnBounds(true);
        filter.getStyleClass().addAll(STYLE_CLASS_DEFAULT, "predefined-filter-icon"); //NON-NLS

        return filter;
    }

    /**
     * Creates a key icon.
     *
     * @param size the size of the icon
     * @return a key icon.
     */
    public Shape createKeyIcon(double size) {
        Color strokeColor = new Color(0.2, 0.2, 0.2, 1);
        Color fillColor = new Color(1, 1, 1, 0);

        double stroke = 1 + size / 20;

        double circleRadius = size / 3;
        double lineLength = circleRadius * 0.3;
        double lineStartX = size / 6;
        double lineStartY = size / 20;

        Circle circle = new Circle(size - circleRadius, size - circleRadius, circleRadius);

        Line trunk = new Line(0, 0, size - (circleRadius + circleRadius / Math.sqrt(2)) - 1, size - (circleRadius + circleRadius / Math.sqrt(2)) - 1);
        Line teeth = new Line(lineStartX, lineStartY, lineStartX + lineLength, lineStartY + lineLength);

        Shape shape = Shape.union(circle, Shape.union(trunk, teeth));
        shape.setStrokeWidth(stroke);
        shape.setStroke(strokeColor);
        shape.setFill(fillColor);
        shape.setPickOnBounds(true);
        shape.getStyleClass().addAll(STYLE_CLASS_DEFAULT, "predefined-key-icon"); //NON-NLS

        return shape;
    }

    /**
     * Creates a calendar icon.
     *
     * @param size the size of the icon
     * @return a calendar icon.
     */
    @SuppressWarnings({"SuspiciousNameCombination", "UnnecessaryLocalVariable"})
    public Shape createCalendarIcon(double size) {
        Color fillColor = new Color(0.3, 0.3, 0.3, 1);

        double width = size;
        double height = size;
        double lineWidth = width / 8;
        double padding = width / 16;
        double topLineHeight = 1.5 * lineWidth;

        Rectangle board = new Rectangle(0, lineWidth - 1, width, height - lineWidth + 1);
        board.setArcHeight(lineWidth * 2);
        board.setArcWidth(lineWidth * 2);
        board.setStrokeWidth(0.0);

        Rectangle content = new Rectangle(padding, 3 * lineWidth, width - 2 * padding, height - 4 * lineWidth);
        content.setArcWidth(lineWidth);
        content.setArcHeight(lineWidth);
        content.setStrokeWidth(0.0);

        Line line1 = new Line(2 * lineWidth, 0, 2 * lineWidth, topLineHeight);
        line1.setStrokeWidth(lineWidth);
        line1.setStrokeLineCap(StrokeLineCap.ROUND);

        Line line11 = new Line(2 * lineWidth, 0, 2 * lineWidth, topLineHeight);
        line11.setStrokeWidth(lineWidth * 2);
        line11.setStrokeLineCap(StrokeLineCap.ROUND);

        Line line2 = new Line(width - 2 * lineWidth, 0, width - 2 * lineWidth, topLineHeight);
        line2.setStrokeWidth(lineWidth);
        line2.setStrokeLineCap(StrokeLineCap.ROUND);

        Line line21 = new Line(width - 2 * lineWidth, 0, width - 2 * lineWidth, topLineHeight);
        line21.setStrokeWidth(lineWidth * 2);
        line21.setStrokeLineCap(StrokeLineCap.ROUND);

        Rectangle dot = new Rectangle(0, 0, lineWidth, lineWidth);
        dot.setStrokeLineCap(StrokeLineCap.BUTT);

        Shape shape = Shape.union(Shape.union(Shape.subtract(Shape.subtract(Shape.subtract(board, content), line11), line21), line1), line2);
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 2; y++) {
                dot.setX(lineWidth * 1.2 + 1.5 * lineWidth * x);
                dot.setY(3.8 * lineWidth + 1.5 * lineWidth * y);
                shape = Shape.union(shape, dot);
            }
        }
        shape.setFill(fillColor);
        shape.setPickOnBounds(true);
        shape.getStyleClass().addAll(STYLE_CLASS_DEFAULT, "predefined-calendar-icon"); //NON-NLS
        return shape;
    }

    /**
     * Creates a clock icon.
     *
     * @param size the size of the icon
     * @return a clock icon.
     */
    @SuppressWarnings({"SuspiciousNameCombination", "UnnecessaryLocalVariable"})
    public Shape createClockIcon(double size) {
        Color fillColor = new Color(0.3, 0.3, 0.3, 1);

        double radius = size / 2;
        double lineWidth = size / 16;
        double dotRadius = size / 16;
        double padding = size / 8;


        Circle board = new Circle(radius, radius, radius);
        board.setStrokeWidth(0.0);

        Circle dot1 = new Circle(padding, radius, dotRadius);
        dot1.setStrokeWidth(0.0);
        Circle dot2 = new Circle(radius * 2 - padding, radius, dotRadius);
        dot2.setStrokeWidth(0.0);
        Circle dot3 = new Circle(radius, padding, dotRadius);
        dot3.setStrokeWidth(0.0);
        Circle dot4 = new Circle(radius, radius * 2 - padding, dotRadius);
        dot4.setStrokeWidth(0.0);

        Line hour = new Line(radius, radius, radius * 2 - padding * 1.5, padding * 1.5);
        hour.setStrokeWidth(lineWidth);
        hour.setStrokeLineCap(StrokeLineCap.ROUND);

        Line minute = new Line(radius, radius, padding * 2, padding * 2);
        minute.setStrokeWidth(lineWidth);
        minute.setStrokeLineCap(StrokeLineCap.ROUND);

        Shape shape = Shape.subtract(Shape.subtract(Shape.subtract(Shape.subtract(Shape.subtract(Shape.subtract(board, dot1), dot2), dot3), dot4), minute), hour);
        shape.setFill(fillColor);
        shape.setPickOnBounds(true);
        shape.getStyleClass().addAll(STYLE_CLASS_DEFAULT, "predefined-clock-icon"); //NON-NLS
        return shape;
    }

    /**
     * Creates a shape by adding a down arrow to it. By default, it will be added to the bottom right of the original
     * shape. The width of the arrow is 0.4 of the original shape's width.
     *
     * @param shape the original shape.
     * @return the shape with a down arrow
     */
    public Shape createArrowedIcon(Shape shape) {
        return createArrowedIcon(shape, Pos.BOTTOM_RIGHT, 0.4);
    }

    /**
     * Creates a shape by adding a down arrow to it.
     *
     * @param shape the original shape.
     * @param pos   the position of the down arrow. For now only BOTTOM_LEFT, BOTTOM_RIGHT, TOP_RIGHT, TOP_LEFT are
     *              supported.
     * @param ratio the arrow width to the original shape's width ratio.
     * @return the shape with a down arrow
     */
    public Shape createArrowedIcon(Shape shape, Pos pos, double ratio) {
        double width = shape.prefWidth(-1);
        double height = shape.prefHeight(-1);
        double arrowWidth = width * ratio;
        double arrowHeight = arrowWidth / 2 + 2;
        Shape arrowIcon = PredefinedShapes.getInstance().createArrowIcon(arrowWidth);
        switch (pos) {
            case BOTTOM_RIGHT:
                arrowIcon.setLayoutX(width + 1);
                arrowIcon.setLayoutY(height - arrowHeight);
                break;
            case BOTTOM_LEFT:
                arrowIcon.setLayoutX(-arrowWidth - 1);
                arrowIcon.setLayoutY(height - arrowHeight);
                break;
            case TOP_RIGHT:
                arrowIcon.setLayoutX(width + 1);
                arrowIcon.setLayoutY(arrowHeight / 2);
                break;
            case TOP_LEFT:
                arrowIcon.setLayoutX(-arrowWidth - 1);
                arrowIcon.setLayoutY(arrowHeight / 2);
                break;
        }
        Shape union = Shape.union(shape, arrowIcon);
        union.setStrokeWidth(shape.getStrokeWidth());
        union.setStroke(shape.getStroke());
        union.setFill(shape.getFill());
        union.getStyleClass().addAll(shape.getStyleClass());
        return union;
    }

    /**
     * Creates a plus icon.
     *
     * @param size the size of the icon
     * @return a plus icon.
     */
    public Shape createPlusIcon(double size) {
        Color fillColor = new Color(0.3, 0.3, 0.3, 1);
        double width = 0.2;
        double v = size * (1 - width) / 2;
        Polygon shape = new Polygon(
                0.0, v,
                v, v,
                v, 0.0,
                size - v, 0.0,
                size - v, v,
                size, v,
                size, size - v,
                size - v, size - v,
                size - v, size,
                v, size,
                v, size - v,
                0.0, size - v
        );
        shape.setStrokeLineCap(StrokeLineCap.ROUND);
        shape.setStroke(fillColor);

        return shape;
    }


    /**
     * Creates a minus icon.
     *
     * @param size the size of the icon
     * @return a minus icon.
     */
    public Shape createMinusIcon(double size) {
        Color fillColor = new Color(0.3, 0.3, 0.3, 1);
        double width = 0.2;
        Rectangle shape = new Rectangle(0.0, 0.0, size, size * width);
        shape.setStroke(fillColor);
        return shape;
    }
}

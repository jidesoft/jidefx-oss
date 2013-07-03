/*
 * @(#)ShapedPopup.java 6/4/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.popup;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PopupControl;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.stage.Window;
import jidefx.utils.PredefinedShapes;

/**
 * A {@link PopupControl} that can set an outline of any shape.
 */
public class ShapedPopup extends PopupControl {
    private ObjectProperty<Node> _popupContentProperty;
    private ObjectProperty<Insets> _insetsProperty;
    private ObjectProperty<PopupOutline> _popupOutlineProperty;
    private BooleanProperty _closeButtonVisibleProperty;

    public ShapedPopup() {
        initializePopup();
        initializeStyle();
    }

    protected void initializePopup() {
        setAutoFix(false);
        setAutoHide(true);
        setHideOnEscape(true);
    }

    protected void initializeStyle() {
    }

    private void initializePath() {
        Node content = getPopupContent();
        content.getStyleClass().add("shaped-popup-content");

        PopupOutline path = getPopupOutline();
        path.getStyleClass().add("shaped-popup-outline");

        customizePath(path);

        Button closeButton = new Button();
        closeButton.setVisible(isCloseButtonVisible());
        closeButton.getStyleClass().add("shaped-popup-close-button");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                hide();
            }
        });
        closeButton.setStyle("-fx-background-color: #ffffff00;");
        Node clearIcon = PredefinedShapes.getInstance().createCloseIcon(20);
        clearIcon.setEffect(new DropShadow(2, Color.DARKGRAY));
        closeButton.setGraphic(clearIcon);

        AnchorPane anchorPane = new AnchorPane(path, content, closeButton);
        anchorPane.getStyleClass().add("shaped-popup");
        anchorPane.setEffect(new DropShadow(8, Color.DARKGRAY));

        path.widthProperty().bind(anchorPane.widthProperty());
        path.heightProperty().bind(anchorPane.heightProperty());

        Insets insets = getInsets();
        Insets padding = path.getContentPadding();
        AnchorPane.setTopAnchor(content, insets.getTop() + padding.getTop());
        AnchorPane.setBottomAnchor(content, insets.getBottom() + padding.getBottom());
        AnchorPane.setLeftAnchor(content, insets.getLeft() + padding.getLeft());
        AnchorPane.setRightAnchor(content, insets.getRight() + padding.getRight());

        adjustCloseButton(closeButton, padding);

        closeButton.setVisible(isCloseButtonVisible());

        ((Group) getScene().getRoot()).getChildren().setAll(anchorPane);

        InvalidationListener invalidationListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                anchorPane.requestLayout();
            }
        };
        getScene().widthProperty().addListener(invalidationListener);
        getScene().heightProperty().addListener(invalidationListener);
    }

    protected void adjustCloseButton(Button closeButton, Insets padding) {
        AnchorPane.setTopAnchor(closeButton, padding.getTop() - 14.0);
        AnchorPane.setRightAnchor(closeButton, padding.getRight() - 14.0);
    }

    /**
     * Customizes the path.
     *
     * @param path the path to be customized
     */
    protected void customizePath(Path path) {
        path.setStroke(Color.LIGHTGRAY);
        path.setFill(Color.WHITE);
        path.setStrokeWidth(1);
        path.setSmooth(true);
    }

    @Override
    public void show(Window owner) {
        throw new IllegalStateException("Please call one of the showPopup methods");
    }

    @Override
    public void show(Node ownerNode, double screenX, double screenY) {
        throw new IllegalStateException("Please call one of the showPopup methods");
    }

    @Override
    public void show(Window ownerWindow, double screenX, double screenY) {
        throw new IllegalStateException("Please call one of the showPopup methods");
    }

    public void showPopup(Node node, Pos pos) {
        showPopup(node, pos, 0, 0);
    }

    public void showPopup(final Node node, final Pos pos, final double xOffset, final double yOffset) {
        initializePath();

        // bind with window
        Window w = node.getScene().getWindow();
        ChangeListener<Number> adjustListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (isShowing()) {
                    Runnable runnable = new Runnable() {
                        public void run() {
                            adjustPopup(node, pos, xOffset, yOffset, false);
                        }
                    };
                    Platform.runLater(runnable);
                }
            }
        };

        w.xProperty().addListener(adjustListener);
        w.yProperty().addListener(adjustListener);
        w.widthProperty().addListener(adjustListener);
        w.heightProperty().addListener(adjustListener);
        getScene().widthProperty().addListener(adjustListener);
        getScene().heightProperty().addListener(adjustListener);

        adjustPopup(node, pos, xOffset, yOffset, true);
    }

    public void adjustPopup(Node node, Pos pos, double xOffset, double yOffset) {
        adjustPopup(node, pos, xOffset, yOffset, false);
    }

    private void adjustPopup(Node node, Pos pos, double xOffset, double yOffset, boolean show) {
        Bounds bounds = node.getBoundsInLocal();

        // use the center of the node as the starting point
        double w = bounds.getWidth();
        double h = bounds.getHeight();
        Point2D p = node.localToScreen(bounds.getMinX() + w / 2 + xOffset, bounds.getMinY() + h / 2 + yOffset);
        double x = p.getX();
        double y = p.getY();
        switch (pos) {
            case CENTER:
            case BASELINE_CENTER:
                break;
            case CENTER_RIGHT:
            case BASELINE_RIGHT:
                x += w / 2;
                break;
            case CENTER_LEFT:
            case BASELINE_LEFT:
                x -= w / 2;
                break;
            case BOTTOM_RIGHT:
                x += w / 2;
                y += h / 2;
                break;
            case BOTTOM_LEFT:
                x -= w / 2;
                y += h / 2;
                break;
            case BOTTOM_CENTER:
                y += h / 2;
                break;
            case TOP_RIGHT:
                x += w / 2;
                y -= h / 2;
                break;
            case TOP_LEFT:
                x -= w / 2;
                y -= h / 2;
                break;
            case TOP_CENTER:
                y -= h / 2;
                break;
        }

        if (show) {
            super.show(node, x, y);
        }

        // adjust the anchor point.
        PopupOutline path = getPopupOutline();
        Point2D anchorPoint = path.getOriginPoint();
        setX(x - anchorPoint.getX());
        System.out.println("set x " + (x - anchorPoint.getX()));
        setY(y - anchorPoint.getY());
    }


    public ObjectProperty<Insets> insetsProperty() {
        if (_insetsProperty == null) {
            _insetsProperty = new SimpleObjectProperty<>(this, "insets", new Insets(10)); //NON-NLS
        }
        return _insetsProperty;
    }

    /**
     * Gets the insets for the popup.
     *
     * @return the insets for the popup.
     */
    public Insets getInsets() {
        return insetsProperty().get();
    }

    /**
     * Sets the insets for the popup.
     *
     * @param insets the insets for the popup.
     */
    public void setInsets(Insets insets) {
        insetsProperty().set(insets);
    }

    public ObjectProperty<Node> popupContentProperty() {
        if (_popupContentProperty == null) {
            _popupContentProperty = new SimpleObjectProperty<>(this, "popupContent"); //NON-NLS
        }
        return _popupContentProperty;
    }

    /**
     * Gets the popup content.
     *
     * @return the popup content.
     */
    public Node getPopupContent() {
        return popupContentProperty().get();
    }

    /**
     * Sets the content for the popup.
     *
     * @param content the content for the popup.
     */
    public void setPopupContent(Node content) {
        popupContentProperty().set(content);
    }

    public ObjectProperty<PopupOutline> popupOutlineProperty() {
        if (_popupOutlineProperty == null) {
            _popupOutlineProperty = new SimpleObjectProperty<PopupOutline>(this, "popupOutline", new RectanglePopupOutline()); //NON-NLS
        }
        return _popupOutlineProperty;
    }

    /**
     * Gets the popup outline.
     *
     * @return the popup outline.
     */
    public PopupOutline getPopupOutline() {
        return popupOutlineProperty().get();
    }

    /**
     * Sets the outline for the popup.
     *
     * @param popupOutline the outline for the popup.
     */
    public void setPopupOutline(PopupOutline popupOutline) {
        popupOutlineProperty().set(popupOutline);
    }

    public BooleanProperty closeButtonVisibleProperty() {
        if (_closeButtonVisibleProperty == null) {
            _closeButtonVisibleProperty = new SimpleBooleanProperty(this, "closeButtonVisible", false);
        }
        return _closeButtonVisibleProperty;
    }

    /**
     * Checks if the close button is visible.
     *
     * @return true if visible. Otherwise false.
     */

    public boolean isCloseButtonVisible() {
        return closeButtonVisibleProperty().get();
    }

    /**
     * Sets the close button visible or invisible.
     *
     * @param closeButtonVisible true or false.
     */
    public void setCloseButtonVisible(boolean closeButtonVisible) {
        closeButtonVisibleProperty().set(closeButtonVisible);
    }
}

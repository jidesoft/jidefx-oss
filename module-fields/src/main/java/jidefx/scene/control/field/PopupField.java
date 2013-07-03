/*
 * @(#)PopupField.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.field;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.util.Callback;
import jidefx.scene.control.decoration.DecorationUtils;
import jidefx.scene.control.decoration.Decorator;
import jidefx.scene.control.decoration.PredefinedDecorators;
import jidefx.scene.control.field.popup.PopupContent;
import jidefx.scene.control.popup.BalloonPopupOutline;
import jidefx.scene.control.popup.ShapedPopup;

import java.util.Locale;

/**
 * {@code PopupField} is {@code FormattedTextField} with a popup button. Clicking on the popup button will show a
 * balloon popup to edit the value.
 *
 * @param <T> the data type of the value in the {@code FormattedTextField}
 */
public class PopupField<T> extends FormattedTextField<T> {
    private static final String STYLE_CLASS_DEFAULT = "popup-field"; //NON-NLS
    private ShapedPopup _shapedPopup;
    private Decorator<Button> _popupButtonDecorator;
    private BooleanProperty _popupButtonVisibleProperty;

    public PopupField() {
    }

    @Override
    protected void initializeStyle() {
        super.initializeStyle();
        getStyleClass().addAll(STYLE_CLASS_DEFAULT);
    }

    public BooleanProperty popupButtonVisibleProperty() {
        if (_popupButtonVisibleProperty == null) {
            _popupButtonVisibleProperty = new SimpleBooleanProperty(this, "popupButtonVisible") { //NON-NLS
                @Override
                protected void invalidated() {
                    super.invalidated();
                    boolean visible = get();
                    if (visible && getPopupContentFactory() != null) {
                        showPopupButton();
                    }
                    else {
                        hidePopupButton();
                    }
                }
            };
        }
        return _popupButtonVisibleProperty;
    }

    /**
     * Checks if the popup button is visible. The popup button will show a popup to choose a value for the field.
     *
     * @return true or false.
     */
    public boolean isPopupButtonVisible() {
        return popupButtonVisibleProperty().get();
    }

    /**
     * Shows or hides the popup button.
     *
     * @param popupButtonVisible true or false.
     */
    public void setPopupButtonVisible(boolean popupButtonVisible) {
        popupButtonVisibleProperty().set(popupButtonVisible);
    }

    private void showPopupButton() {
        if (_popupButtonDecorator == null) {
            _popupButtonDecorator = createPopupButtonDecorator();
            _popupButtonDecorator.getNode().disableProperty().bind(disabledProperty());
            _popupButtonDecorator.getNode().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    togglePopup();
                }
            });
        }
        DecorationUtils.install(this, _popupButtonDecorator);
    }

    protected Decorator<Button> createPopupButtonDecorator() {
        return PredefinedDecorators.getInstance().getPopupButtonDecoratorSupplier().get();
    }

    private void hidePopupButton() {
        if (_popupButtonDecorator != null) {
            DecorationUtils.uninstall(this, _popupButtonDecorator);
        }
    }

    /**
     * Shows or hides the popup.
     */
    protected void togglePopup() {
        if (_shapedPopup == null || !_shapedPopup.isShowing()) {
            show();
        }
        else {
            hide();
        }
    }

    public void hide() {
        if (_shapedPopup != null) {
            _shapedPopup.hide();
            _shapedPopup = null;
        }
    }

    public void show() {
        if (_shapedPopup != null) {
            hide();
        }

        if (!commitEdit()) {
            cancelEdit();
        }
        PopupContent<T> popupContent = createPopupContent(getValue());
        if (popupContent != null) {
            _shapedPopup = new ShapedPopup();
            _shapedPopup.setPopupContent((Parent) popupContent);
            showPopup(_popupButtonDecorator.getNode(), _shapedPopup);
        }
    }

    protected void customizePopupContent(PopupContent<T> popupContent) {
        popupContent.valueProperty().addListener(new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
                setValue(newValue);
            }
        });
    }

    /**
     * Shows the popup. Subclass can override it to show the popup in another direction or at a different position.
     *
     * @param owner   the owner of the popup. It should always be the popup button.
     * @param shapedPopup the balloon which contains the popup.
     */
    protected void showPopup(Node owner, ShapedPopup shapedPopup) {
        BalloonPopupOutline outline = new BalloonPopupOutline();
        outline.setArrowSide(Side.TOP);
        outline.setArrowPosition(0.9);
        outline.setArrowBasePosition(0.9);
        shapedPopup.setPopupOutline(outline);
        shapedPopup.showPopup(owner, Pos.BOTTOM_CENTER, 0, 0);
    }

    /**
     * Creates the popup content. The content will be added to a Balloon popup window.
     *
     * @param value the value
     * @return the popup content.
     */
    protected PopupContent<T> createPopupContent(T value) {
        Callback<T, PopupContent<T>> factory = getPopupContentFactory();
        if (factory != null) {
            PopupContent<T> popupContent = factory.call(value);
            customizePopupContent(popupContent);
            return popupContent;
        }
        else return null;
    }

    private ObjectProperty<Callback<T, PopupContent<T>>> _popupContentFactory;

    /**
     * Sets a custom PopupContent factory allows for complete customization of the popup pane in the ComboBox.
     *
     * @param value a custom PopupContent factory
     */
    public final void setPopupContentFactory(Callback<T, PopupContent<T>> value) {
        popupContentFactoryProperty().set(value);
    }

    /**
     * Gets the PopupContent factory
     *
     * @return the PopupContent factory.
     */
    public final Callback<T, PopupContent<T>> getPopupContentFactory() {
        return popupContentFactoryProperty().get();
    }

    public ObjectProperty<Callback<T, PopupContent<T>>> popupContentFactoryProperty() {
        if (_popupContentFactory == null) {
            _popupContentFactory = new SimpleObjectProperty<>(this, "popupContentFactory"); //NON-NLS
        }
        return _popupContentFactory;
    }

    /**
     * Gets the localized string from resource bundle. Subclass can override it to provide its own string. Available
     * keys are defined in grid.properties that begins with "Filter." and lucene.properties that begins with "Lucene".
     *
     * @param key the key to the resource.
     * @return the localized string.
     */
    public String getResourceString(String key) {
        if (key == null) {
            return "";
        }
        return FieldsResource.getResourceBundle(Locale.getDefault()).getString(key);
    }
}

/*
 * @(#)FormattedComboBox.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.combobox;

import com.jidefx.scene.control.skin.combobox.FormattedComboBoxSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import jidefx.scene.control.editor.Editor;
import jidefx.scene.control.field.FormattedTextField;
import jidefx.scene.control.field.popup.PopupContent;

/**
 * {@code FormattedComboBox} is a {@code ComboBox} that uses {@code FormattedTextField} as the editor.
 *
 * @param <T> The type of the value that has been selected or otherwise entered in to this ComboBox.
 */
public class FormattedComboBox<T> extends ComboBoxBase<T> implements Editor<T> {
    /**
     * ************************************************************************ * Stylesheet Handling * *
     * ************************************************************************
     */

    private static final String STYLE_CLASS_COMBO_BOX = "combo-box"; //NON-NLS
    private static final String STYLE_CLASS_FORMATTED_COMBO_BOX = "formatted-combo-box"; //NON-NLS

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    /**
     * Creates a default {@code FormattedComboBox} instance with a null value.
     */
    public FormattedComboBox() {
        this(null);
    }

    /**
     * Creates a default {@code FormattedComboBox} instance with the provided value.
     *
     * @param value the data type of the value in the combobox
     */
    public FormattedComboBox(T value) {
        initializeComboBox();
        initializeStyle();
        registerListeners();
        setValue(value);
    }

    /**
     * Adds or removes style from the getStyleClass. Subclass should call super if you want to keep the existing
     * styles.
     */
    protected void initializeStyle() {
        getStyleClass().addAll(STYLE_CLASS_COMBO_BOX, STYLE_CLASS_FORMATTED_COMBO_BOX);
    }

    /**
     * Subclass can override it to do additional customization, such as calling {@link
     * #setPopupContentFactory(Callback)}.
     */
    protected void initializeComboBox() {
    }

    protected void registerListeners() {
    }

    /**
     * ************************************************************************ * Properties * *
     * ************************************************************************
     */

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
     * The editor for the ComboBox. It is used for both editable combobox and non-editable combobox.
     */
    private ReadOnlyObjectWrapper<FormattedTextField<T>> editor;

    public final FormattedTextField<T> getEditor() {
        return editorProperty().get();
    }

    public final ReadOnlyObjectProperty<FormattedTextField<T>> editorProperty() {
        if (editor == null) {
            editor = new ReadOnlyObjectWrapper<>(this, "editor"); //NON-NLS
            FormattedTextField<T> field = createFormattedTextField();
            field.valueProperty().bindBidirectional(valueProperty());
            field.setComboBoxLike(false);
            editor.set(field);
        }
        return editor.getReadOnlyProperty();
    }

    /**
     * Creates a FormattedTextField. Subclass can override it to create a FormattedTextField subclass.
     *
     * @return a FormattedTextField
     */
    protected FormattedTextField<T> createFormattedTextField() {
        return new FormattedTextField<>();
    }

    /***************************************************************************
     *                                                                         *
     * Methods                                                                 *
     *                                                                         *
     **************************************************************************/

    @Override
    protected Skin<?> createDefaultSkin() {
        return new FormattedComboBoxSkin<>(this);
    }

    @Override
    public ObservableValue<T> observableValue() {
        return valueProperty();
    }

}
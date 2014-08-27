/*
 * @(#)ValidationUtils.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.validation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import jidefx.animation.AnimationType;
import jidefx.scene.control.decoration.DecorationUtils;
import jidefx.scene.control.decoration.Decorator;
import jidefx.utils.CommonUtils;
import jidefx.utils.FXUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"UnusedDeclaration", "Convert2Lambda"})
public class ValidationUtils {

    public static class TooltipFix extends Tooltip {

        private Node parentNode;

        public TooltipFix(Node parentNode) {
            this.parentNode = parentNode;
        }

        @Override
        public Styleable getStyleableParent() {
            Styleable styleableParent = super.getStyleableParent();
            if (styleableParent != null) {
                return styleableParent;
            } else {
                return parentNode;
            }
        }
    }

    private static final String PROPERTY_ON_DEMAND_VALIDATOR = "Validation.On.Demand.Validator"; //NON-NLS
    private static final String PROPERTY_ON_DEMAND_OBSERVABLE_VALUE = "Validation.On.Demand.ObservableValue"; //NON-NLS
    private static final String PROPERTY_ON_DEMAND_EVENT_FILTER = "Validation.On.Demand.EventFilter"; //NON-NLS
    private static final String PROPERTY_ON_DEMAND_OBJECT = "Validation.On.Demand.Object"; //NON-NLS

    private static final String PROPERTY_ON_FLY_VALIDATOR = "Validation.On.Fly.Validator"; //NON-NLS
    private static final String PROPERTY_ON_FLY_OBSERVABLE_VALUE = "Validation.On.Fly.ObservableValue"; //NON-NLS
    private static final String PROPERTY_ON_FLY_LISTENER = "Validation.On.Fly.Listener"; //NON-NLS
    private static final String PROPERTY_ON_FLY_EVENT_FILTER = "Validation.On.Fly.EventFilter"; //NON-NLS

    private static final String PROPERTY_ON_FOCUS_LOST_VALIDATOR = "Validation.On.FocusLost.Validator"; //NON-NLS
    private static final String PROPERTY_ON_FOCUS_LOST_LISTENER = "Validation.On.FocusLost.Listener"; //NON-NLS
    private static final String PROPERTY_ON_FOCUS_LOST_EVENT_FILTER = "Validation.On.FocusLost.EventFilter"; //NON-NLS
    private static final String PROPERTY_ON_FOCUS_LOST_OBJECT = "Validation.On.FocusLost.Object"; //NON-NLS

    private static final String PROPERTY_VALIDATION_RESULT = "Validation.Result";
    private static final String PROPERTY_VALIDATION_RESULT_MESSAGE = "Validation.Result.Message";

    private static final String PROPERTY_LABEL_VALIDATION = "label-validation"; //NON-NLS

    private static final String PSEUDO_CLASS_VALIDATION_ERROR = "validation-error"; //NON-NLS
    private static final String PSEUDO_CLASS_VALIDATION_WARNING = "validation-warning"; //NON-NLS
    private static final String PSEUDO_CLASS_VALIDATION_INFO = "validation-info"; //NON-NLS
    private static final String PSEUDO_CLASS_VALIDATION_OK = "validation-ok"; //NON-NLS

    /**
     * Installs a validator to a target node using the ON_FLY validation mode. If there is an old validator, it will be
     * uninstalled first. A default observable value will be used for the give node. For example, textProperty of a
     * TextInputControl or its subclasses, selectedItemProperty of the SelectionModel for ListView, TableView, TreeView,
     * ChoiceBox, and ComboBox, selectedProperty for CheckBox and RadioButton. A default event handler will be installed
     * on the node to display the validation result.
     *
     * @param targetNode the target node where the validator will be installed
     * @param validator  the validator
     * @param <T>        the date type of the observable value.
     * @return true or false. True if the validator is installed correctly. It actually always return true as long as
     * the observable value and the validator are not null.
     */
    public static <T> boolean install(Node targetNode, Validator validator) {
        return install(targetNode, getDefaultObservableValue(targetNode), validator, getDefaultMode(), createDefaultValidationEventHandler(targetNode));
    }

    /**
     * Installs a validator to a target node. If there is an old validator, it will be uninstalled first. A default
     * observable value will be used for the give node. For example, textProperty of a TextInputControl or its
     * subclasses, selectedItemProperty of the SelectionModel for ListView, TableView, TreeView, ChoiceBox, and
     * ComboBox, selectedProperty for CheckBox and RadioButton. A default event handler will be installed on the node to
     * display the validation result.
     *
     * @param targetNode the target node where the validator will be installed
     * @param validator  the validator
     * @param mode       the validation mode
     * @param <T>        the date type of the observable value.
     * @return true or false. True if the validator is installed correctly. It actually always return true as long as
     * the observable value and the validator are not null.
     */
    public static <T> boolean install(Node targetNode, Validator validator, ValidationMode mode) {
        return install(targetNode, getDefaultObservableValue(targetNode), validator, mode, createDefaultValidationEventHandler(targetNode));
    }

    /**
     * Installs a validator to a target node using the ON_FLY validation mode. If there is an old validator, it will be
     * uninstalled first. A default event handler will be installed on the node to display the validation result.
     *
     * @param targetNode      the target node where the validator will be installed
     * @param observableValue the observable value to listen to if the validation mode is ON_FLY. In the other two
     *                        modes, there is where the value to be validated is retrieved
     * @param validator       the validator
     * @param <T>             the date type of the observable value.
     * @return true or false. True if the validator is installed correctly. It actually always return true as long as
     * the observable value and the validator are not null.
     */
    public static <T> boolean install(Node targetNode, ObservableValue<T> observableValue, Validator validator) {
        return install(targetNode, observableValue, validator, ValidationMode.ON_FLY, createDefaultValidationEventHandler(targetNode));
    }

    /**
     * Installs a validator to a target node. If there is an old validator, it will be uninstalled first. A default
     * event handler will be installed on the node to display the validation result.
     *
     * @param targetNode      the target node where the validator will be installed
     * @param observableValue the observable value to listen to if the validation mode is ON_FLY. In the other two
     *                        modes, there is where the value to be validated is retrieved
     * @param validator       the validator
     * @param mode            the validation mode
     * @param <T>             the date type of the observable value.
     * @return true or false. True if the validator is installed correctly. It actually always return true as long as
     * the observable value and the validator are not null.
     */
    public static <T> boolean install(Node targetNode, ObservableValue<T> observableValue, Validator validator, ValidationMode mode) {
        return install(targetNode, observableValue, validator, mode, createDefaultValidationEventHandler(targetNode));
    }

    /**
     * Installs a validator to a target node using the ON_FLY validation mode. If there is an old validator, it will be
     * uninstalled first.
     *
     * @param targetNode      the target node where the validator will be installed
     * @param observableValue the observable value to listen to if the validation mode is ON_FLY. In the other two
     *                        modes, there is where the value to be validated is retrieved
     * @param validator       the validator
     * @param eventFilter     the event handler. It will be added to the target node. When there is a validation event,
     *                        this handler will response to it and display the validation result.
     * @param <T>             the date type of the observable value.
     * @return true or false. True if the validator is installed correctly. It actually always return true as long as
     * the observable value and the validator are not null.
     */
    public static <T> boolean install(Node targetNode, ObservableValue<T> observableValue, Validator validator, EventHandler<ValidationEvent> eventFilter) {
        return install(targetNode, observableValue, validator, getDefaultMode(), eventFilter);
    }

    /**
     * Installs a validator to a target node. If there is an old validator, it will be uninstalled first.
     *
     * @param targetNode      the target node where the validator will be installed
     * @param observableValue the observable value to listen to if the validation mode is ON_FLY. In the other two
     *                        modes, there is where the value to be validated is retrieved
     * @param validator       the validator
     * @param mode            the validation mode
     * @param eventFilter     the event handler. It will be added to the target node. When there is a validation event,
     *                        this handler will response to it and display the validation result.
     * @param <T>             the date type of the observable value.
     * @return true or false. True if the validator is installed correctly. It actually always return true as long as
     * the observable value and the validator are not null.
     */
    public static <T> boolean install(Node targetNode, ObservableValue<T> observableValue, Validator validator, ValidationMode mode, EventHandler<ValidationEvent> eventFilter) {
        if (observableValue == null || validator == null) {
            return false;
        }

        switch (mode) {
            case ON_FOCUS_LOST:
                return setOnFocusLostValidation(targetNode, validator, observableValue, eventFilter);
            case ON_DEMAND:
                return setOnDemandValidation(targetNode, validator, observableValue, eventFilter);
            case ON_FLY:
                return setOnFlyValidation(targetNode, validator, observableValue, eventFilter);
        }
        return false;
    }

    // TODO: what if the targetNode is a TableView, ListView or a TreeView. Can the validator work as well when the validation is for an editing cell??
    private static ObservableValue getDefaultObservableValue(Node targetNode) {
        ObservableValue observableValue = null;
        Class clazz = targetNode.getClass();
        if (TextInputControl.class.isAssignableFrom(clazz)) {
            return ((TextInputControl) targetNode).textProperty();
        } else if (ListView.class.isAssignableFrom(clazz)) {
            return ((ListView) targetNode).getSelectionModel().selectedItemProperty();
        } else if (TreeView.class.isAssignableFrom(clazz)) {
            return ((TreeView) targetNode).getSelectionModel().selectedItemProperty();
        } else if (TableView.class.isAssignableFrom(clazz)) {
            return ((TableView) targetNode).getSelectionModel().selectedItemProperty();
        } else if (ComboBox.class.isAssignableFrom(clazz)) {
            return ((ComboBox) targetNode).getSelectionModel().selectedItemProperty();
        } else if (ChoiceBox.class.isAssignableFrom(clazz)) {
            return ((ChoiceBox) targetNode).getSelectionModel().selectedItemProperty();
        } else if (CheckBox.class.isAssignableFrom(clazz)) {
            return ((CheckBox) targetNode).selectedProperty();
        } else if (RadioButton.class.isAssignableFrom(clazz)) {
            return ((RadioButton) targetNode).selectedProperty();
        } else if (DatePicker.class.isAssignableFrom(clazz)) {
            return ((DatePicker) targetNode).valueProperty();
        }
        return observableValue;
    }

    private static ValidationMode getDefaultMode() {
        return ValidationMode.ON_FLY;
    }

    private static <T> boolean setOnFlyValidation(Node targetNode, Validator validator, ObservableValue<T> targetProperty, EventHandler<ValidationEvent> eventFilter) {
        uninstall(targetNode, ValidationMode.ON_FLY);

        ChangeListener<T> listener = new ChangeListener<T>() {
            @Override
            public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue) {
                ValidationObject validationObject = new ValidationObject(targetNode, oldValue, newValue);
                ValidationEvent event = validator.call(validationObject);
                targetNode.fireEvent(event);
            }
        };
        targetProperty.addListener(listener);

        targetNode.addEventFilter(ValidationEvent.ANY, eventFilter);

        targetNode.getProperties().put(PROPERTY_ON_FLY_VALIDATOR, validator);
        targetNode.getProperties().put(PROPERTY_ON_FLY_OBSERVABLE_VALUE, targetProperty);
        targetNode.getProperties().put(PROPERTY_ON_FLY_EVENT_FILTER, eventFilter);
        targetNode.getProperties().put(PROPERTY_ON_FLY_LISTENER, listener);

        return true;
    }

    private static <T> boolean setOnFocusLostValidation(Node targetNode, Validator validator, ObservableValue<T> targetProperty, EventHandler<ValidationEvent> eventFilter) {
        uninstall(targetNode, ValidationMode.ON_FOCUS_LOST);

        ChangeListener<Boolean> listener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) { // focus gained
                    targetNode.getProperties().put(PROPERTY_ON_FOCUS_LOST_OBJECT, targetProperty.getValue()); // save old value
                } else { // focus lost
                    Object oldValidationValue = targetNode.getProperties().get(PROPERTY_ON_FOCUS_LOST_OBJECT);
                    T newValidationValue = targetProperty.getValue();
                    if (!CommonUtils.equals(oldValidationValue, newValidationValue)) {
                        ValidationObject validationObject = new ValidationObject(targetNode, oldValidationValue, newValidationValue);
                        ValidationEvent event = validator.call(validationObject);
                        targetNode.fireEvent(event);
                    }
                }
            }
        };
        targetNode.focusedProperty().addListener(listener);

        targetNode.addEventFilter(ValidationEvent.ANY, eventFilter);

        targetNode.getProperties().put(PROPERTY_ON_FOCUS_LOST_VALIDATOR, validator);
        targetNode.getProperties().put(PROPERTY_ON_FOCUS_LOST_LISTENER, listener);
        targetNode.getProperties().put(PROPERTY_ON_FOCUS_LOST_EVENT_FILTER, eventFilter);
        targetNode.getProperties().put(PROPERTY_ON_FOCUS_LOST_LISTENER, listener);

        return true;
    }

    private static <T> boolean setOnDemandValidation(Node targetNode, Validator validator, ObservableValue<T> targetProperty, EventHandler<ValidationEvent> eventFilter) {
        uninstall(targetNode, ValidationMode.ON_DEMAND);

        targetNode.addEventFilter(ValidationEvent.ANY, eventFilter);

        targetNode.getProperties().put(PROPERTY_ON_DEMAND_VALIDATOR, validator);
        targetNode.getProperties().put(PROPERTY_ON_DEMAND_OBSERVABLE_VALUE, targetProperty);
        targetNode.getProperties().put(PROPERTY_ON_DEMAND_EVENT_FILTER, eventFilter);
        targetNode.getProperties().put(PROPERTY_ON_DEMAND_OBJECT, targetProperty.getValue()); // save old value
        return true;
    }

    /**
     * Uninstalls the validators from the target node for all validation modes.
     *
     * @param targetNode the node to be validated.
     * @return true if uninstalled successfully. Otherwise false.
     */
    public static boolean uninstall(Node targetNode) {
        return uninstall(targetNode, ValidationMode.ON_FLY) && uninstall(targetNode, ValidationMode.ON_FOCUS_LOST) && uninstall(targetNode, ValidationMode.ON_DEMAND);
    }

    /**
     * Uninstalls the validator from the target node for the given validation mode.
     *
     * @param targetNode the node to be validated.
     * @param mode       the validation mode.
     * @return true if uninstalled successfully. If the target node is null or there is no validators installed, it will
     * return false. Please be noted that all validators will be cleared no matter what is returned.
     */
    @SuppressWarnings("unchecked")
    public static boolean uninstall(Node targetNode, ValidationMode mode) {
        if (targetNode == null) {
            return false;
        }
        ValidationMode removeMode = mode;
        if (removeMode == null) {
            removeMode = getDefaultMode();
        }
        switch (removeMode) {
            case ON_DEMAND: {
                Object eventFilter = targetNode.getProperties().get(PROPERTY_ON_DEMAND_EVENT_FILTER);
                if (eventFilter instanceof EventHandler) {
                    targetNode.removeEventFilter(ValidationEvent.ANY, (EventHandler<ValidationEvent>) eventFilter);
                }

                Object remove = targetNode.getProperties().remove(PROPERTY_ON_DEMAND_VALIDATOR);
                if (remove != null) {
                    targetNode.getProperties().remove(PROPERTY_ON_DEMAND_OBSERVABLE_VALUE);
                    targetNode.getProperties().remove(PROPERTY_ON_DEMAND_EVENT_FILTER);
                } else return false;
            }
            break;
            case ON_FOCUS_LOST: {
                Object eventFilter = targetNode.getProperties().get(PROPERTY_ON_FOCUS_LOST_EVENT_FILTER);
                if (eventFilter instanceof EventHandler) {
                    targetNode.removeEventFilter(ValidationEvent.ANY, (EventHandler<ValidationEvent>) eventFilter);
                }

                Object remove = targetNode.getProperties().remove(PROPERTY_ON_FOCUS_LOST_VALIDATOR);
                if (remove != null) {
                    targetNode.getProperties().remove(PROPERTY_ON_FOCUS_LOST_LISTENER);
                    targetNode.getProperties().remove(PROPERTY_ON_FOCUS_LOST_EVENT_FILTER);
                    targetNode.getProperties().remove(PROPERTY_ON_FOCUS_LOST_OBJECT);

                    Object listener = targetNode.getProperties().get(PROPERTY_ON_FOCUS_LOST_LISTENER);
                    if (listener instanceof ChangeListener) {
                        targetNode.focusedProperty().removeListener((ChangeListener) listener);
                    } else return false;
                } else return false;
            }
            break;
            case ON_FLY:
            default: {
                Object eventFilter = targetNode.getProperties().get(PROPERTY_ON_FLY_EVENT_FILTER);
                if (eventFilter instanceof EventHandler) {
                    targetNode.removeEventFilter(ValidationEvent.ANY, (EventHandler<ValidationEvent>) eventFilter);
                }

                Object remove = targetNode.getProperties().remove(PROPERTY_ON_FLY_VALIDATOR);
                if (remove != null) {
                    targetNode.getProperties().remove(PROPERTY_ON_FLY_OBSERVABLE_VALUE);
                    targetNode.getProperties().remove(PROPERTY_ON_FLY_LISTENER);
                    targetNode.getProperties().remove(PROPERTY_ON_FLY_EVENT_FILTER);

                    Object onFlyValue = targetNode.getProperties().get(PROPERTY_ON_FLY_OBSERVABLE_VALUE);
                    Object onFlyListener = targetNode.getProperties().get(PROPERTY_ON_FLY_LISTENER);
                    if (onFlyValue instanceof ObservableValue && onFlyListener instanceof ChangeListener) {
                        ((ObservableValue) onFlyValue).removeListener((ChangeListener) onFlyListener);
                    } else return false;
                } else return false;

            }
            break;
        }
        return true;
    }

    /**
     * Force the validator to execute validation immediately when there is no correspond event. Should be used ON_FLY
     * and ON_FOCUS_LOST validation modes.
     *
     * @param validateNode the validate node
     * @param mode         the validation mode, ON_FLY or ON_FOCUS_LOST.
     */
    public static void forceValidate(Node validateNode, ValidationMode mode) {
        Object validator;
        ObservableValue observableValue;

        switch (mode) {
            case ON_FLY:
                validator = validateNode.getProperties().get(PROPERTY_ON_FLY_VALIDATOR);
                observableValue = (ObservableValue) validateNode.getProperties().get(PROPERTY_ON_FLY_OBSERVABLE_VALUE);
                if (validator instanceof Validator) {
                    ValidationObject object = new ValidationObject(validateNode, null, observableValue.getValue());
                    ValidationEvent event = ((Validator) validator).call(object);
                    validateNode.fireEvent(event);
                }
                break;
            case ON_FOCUS_LOST:
                validator = validateNode.getProperties().get(PROPERTY_ON_FLY_VALIDATOR);
                observableValue = (ObservableValue) validateNode.getProperties().get(PROPERTY_ON_FOCUS_LOST_OBJECT);
                if (validator instanceof Validator) {
                    ValidationObject object = new ValidationObject(validateNode, null, observableValue);
                    ValidationEvent event = ((Validator) validator).call(object);
                    validateNode.fireEvent(event);
                }
                break;
        }
    }

    /**
     * Validates the Region. It will call the validators that were installed on any children using ON_DEMAND validation
     * mode.
     *
     * @param targetRegionOrNode the node or the region to be validated.
     * @return the true or false. True if there is no validation errors. False is there is an error.
     */
    public static boolean validateOnDemand(Node targetRegionOrNode) {
        if (targetRegionOrNode == null) {
            return false;
        }

        final List<Node> nodes = new ArrayList<>();
        FXUtils.setRecursively(targetRegionOrNode, new FXUtils.Handler<Node>() {
            @Override
            public boolean condition(Node c) {
                return c.getProperties().get(PROPERTY_ON_DEMAND_VALIDATOR) instanceof Validator;
            }

            @Override
            public void action(Node c) {
                nodes.add(c);
            }
        });

        boolean valid = true;

        for (Node validateNode : nodes) {
            ObservableMap<Object, Object> properties = validateNode.getProperties();

            if (properties.get(PROPERTY_ON_DEMAND_VALIDATOR) instanceof Validator && properties.get(PROPERTY_ON_DEMAND_OBSERVABLE_VALUE) instanceof ObservableValue) {
                Validator validator = (Validator) properties.get(PROPERTY_ON_DEMAND_VALIDATOR);
                ObservableValue observableValue = (ObservableValue) properties.get(PROPERTY_ON_DEMAND_OBSERVABLE_VALUE);
                Object oldValue = properties.get(PROPERTY_ON_DEMAND_OBJECT);
                ValidationObject object = new ValidationObject(validateNode, oldValue, observableValue.getValue());
                ValidationEvent event = validator.call(object);
                validateNode.fireEvent(event);

                if (ValidationEvent.VALIDATION_ERROR.equals(event.getEventType())) {
                    valid = false;
                } else { // if valid, we will store the current value as the old value so that it can be used next time
                    properties.put(PROPERTY_ON_DEMAND_OBJECT, object);
                }
            }
        }

        if (targetRegionOrNode instanceof Region) {
            ((Region) targetRegionOrNode).requestLayout();
        }

        return valid;
    }

    public static boolean isValidationDecorator(Decorator decorator) {
        if (Optional.of(decorator.getNode()).isPresent()) {
            Node _node = Optional.of((decorator).getNode()).get();
            if (_node instanceof Label) {
                Label validationLabel = (Label) _node;
                // Search for validation label
                long count = validationLabel.getStyleClass()
                        .stream()
                        .filter(styleClass -> styleClass.equals(PROPERTY_LABEL_VALIDATION)).count();
                if (count > 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Optional<List<Decorator>> getValidationDecorators(Node node) {
        if (Optional.ofNullable(DecorationUtils.getDecorators(node)).isPresent()) {
            Object o = Optional.of(DecorationUtils.getDecorators(node)).get();

            if (o instanceof Decorator) {
                if (isValidationDecorator((Decorator) o)) {
                    return Optional.of(new ArrayList<Decorator>() {{
                        add((Decorator) o);
                    }});
                }
            } else if (o instanceof Decorator[]) {
                Decorator[] decorators = (Decorator[]) o;
                List<Decorator> validationLabels = new ArrayList<>();

                for (int i = 0; i < decorators.length; i++) {
                    Decorator d = decorators[i];

                    if (isValidationDecorator(d)) {
                        validationLabels.add(d);
                    }
                }
                return Optional.of(validationLabels);
            }
        }

        return Optional.empty();
    }

    private static TooltipFix createTooltip(ValidationEvent event, Label label) {
        TooltipFix tooltip = null;

        if (event.getMessage() != null && event.getMessage().trim().length() > 0) {
            tooltip = new TooltipFix(label);
            tooltip.setText(event.getMessage());
            tooltip.setAutoHide(true);
        }

        return tooltip;
    }

    public static void showTooltip(Label label) {
        if (label != null && label.getTooltip() != null) {
            Point2D point = label.localToScene(15.0, 15.0);

            label.getTooltip().setAutoHide(true);
            label.getTooltip().show(label.getScene().getWindow(), point.getX()
                    + label.getScene().getX() + label.getScene().getWindow().getX(), point.getY()
                    + label.getScene().getY() + label.getScene().getWindow().getY());
        }
    }

    /**
     * Creates a validation event handler. This handler will install an icon decorator to the node to be validated, and
     * also set the pseudo-class as "validation-ok", "validation-info", "validation-warning", "warning-error" so that
     * you can use css file to display the validation result.
     *
     * @param targetNode the node to be validated
     * @return the event handler
     */
    public static EventHandler<ValidationEvent> createDefaultValidationEventHandler(Node targetNode) {
        return new EventHandler<ValidationEvent>() {
            Decorator resultDecorator = null;

            @Override
            public void handle(ValidationEvent event) {
                if (event != null) {
                    adjustValidationPseudoClasses(event, targetNode);

                    if (event.getEventType().equals(ValidationEvent.VALIDATION_UNKNOWN)) {
                        DecorationUtils.uninstall(targetNode);
                        resultDecorator = null;
                        targetNode.getParent().requestLayout();
                        return;
                    }

                    if (targetNode instanceof Cell && event.getEventType().equals(ValidationEvent.VALIDATION_OK)) {
                        return;
                    }

                    // check to make sure it is the same event type and even message
                    Object validationResultProperty = targetNode.getProperties().get(PROPERTY_VALIDATION_RESULT);
                    Object validationResultMessageProperty = targetNode.getProperties().get(PROPERTY_VALIDATION_RESULT_MESSAGE);
                    if (validationResultProperty != null && validationResultProperty.equals(event.getEventType().getName())
                            && validationResultMessageProperty != null && validationResultMessageProperty.equals(event.getMessage())) {
                        return;
                    }

                    Label label;

                    ImageView graphic = new ImageView(ValidationIcons.getInstance().getValidationResultIcon(event.getEventType()));
                    if (resultDecorator != null && exists(targetNode, resultDecorator)) {
                        label = (Label) resultDecorator.getNode();
                        label.setGraphic(graphic);
                        DecorationUtils.setAnimationPlayed(label, false);
                        label.setTooltip(createTooltip(event, label));
                    } else {
                        label = new Label("", graphic);
                        label.getStyleClass().add(PROPERTY_LABEL_VALIDATION);
                        label.setTooltip(createTooltip(event, label));
                        label.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                            if (label.getTooltip() != null) {
                                label.getTooltip().show(label, 100, 100);
                            }
                        });
                        resultDecorator = createDefaultDecorator(targetNode, label);
                        DecorationUtils.install(targetNode, resultDecorator);
                        targetNode.getParent().requestLayout();
                    }

                    adjustValidationPseudoClasses(event, label);

                    targetNode.getProperties().put(PROPERTY_VALIDATION_RESULT, event.getEventType().getName());
                    targetNode.getProperties().put(PROPERTY_VALIDATION_RESULT_MESSAGE, event.getMessage());
                }
            }

            private boolean exists(Node targetNode, Decorator resultDecorator) {
                Object o = DecorationUtils.getDecorators(targetNode);
                if (o != null) {
                    if (o.equals(resultDecorator) || (o instanceof Decorator[] && Arrays.asList((Decorator[]) o).contains(resultDecorator))) {
                        return true;
                    }
                }
                return false;
            }

            private void adjustValidationPseudoClasses(ValidationEvent event, Node targetNode) {

                if (event.getEventType() == ValidationEvent.VALIDATION_OK) {
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_OK), true);
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_INFO), false);
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_WARNING), false);
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_ERROR), false);
                } else if (event.getEventType() == ValidationEvent.VALIDATION_ERROR) {
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_OK), false);
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_INFO), false);
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_WARNING), false);
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_ERROR), true);
                } else if (event.getEventType() == ValidationEvent.VALIDATION_WARNING) {
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_OK), false);
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_INFO), false);
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_WARNING), true);
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_ERROR), false);
                } else if (event.getEventType() == ValidationEvent.VALIDATION_INFO) {
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_OK), false);
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_INFO), true);
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_WARNING), false);
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_ERROR), false);
                } else { // UNKNOWN
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_OK), false);
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_INFO), false);
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_WARNING), false);
                    targetNode.pseudoClassStateChanged(PseudoClass.getPseudoClass(PSEUDO_CLASS_VALIDATION_ERROR), false);
                }

            }

            private Decorator<Label> createDefaultDecorator(Node node, Label label) {
                if (node instanceof Cell) {
                    return new Decorator<>(label, Pos.CENTER_RIGHT, new Point2D(-60, 0), new Insets(0, 100, 0, 0), AnimationType.TADA);
                } else {
                    //return new Decorator<>(label, Pos.BOTTOM_LEFT);
                    return new Decorator<>(label, Pos.BOTTOM_LEFT, new Point2D(0, 0), new Insets(0, 0, 0, 0));
                }
            }
        };
    }
}


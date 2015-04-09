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
package jidefx.scene.control.validation;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * {@code ValidationEvent} is the object that returns from the {@code Validator} callback. The
 * {@code ValidationEvent} extends {@code javafx.event.Event} so it can be fired as normal just like any other
 * JavaFX events.
 * <p>
 * To get notified of a ValidationEvent, you can addEventFilter or addEventHandler at the validating node or its
 * ancestors. The latter case is very useful because you can listen to all ValidationEvents in a form by add event
 * handler to the form only.
 * <p>
 * By default, we will add our own EventHandler using addEventFilter. We will handle it to display the validation
 * results.
 * <p>
 * If you want to add more decorations, you can do it by adding your own EventHandler for the ValidationEvent. If you
 * are using {@link ValidationUtils#install(javafx.scene.Node, javafx.beans.value.ObservableValue, Validator,
 * ValidationMode, javafx.event.EventHandler)} method, you can also install your own EventHandler to replace our default
 * one. In this case, no display for the validation results will be shown except the ones you will add.
 */
public class ValidationEvent extends Event {
    /**
     * Common super type for all validation event types.
     */
    public static final EventType<ValidationEvent> ANY =
            new EventType<>(Event.ANY, "VALIDATION"); //NON-NLS

    /**
     * This event occurs on the validation is ok
     */
    public static final EventType<ValidationEvent> VALIDATION_OK =
            new EventType<>(ValidationEvent.ANY, "VALIDATION_OK"); //NON-NLS

    /**
     * This event occurs on the validation has information
     */
    public static final EventType<ValidationEvent> VALIDATION_INFO =
            new EventType<>(ValidationEvent.ANY, "VALIDATION_INFO"); //NON-NLS

    /**
     * This event occurs on the validation has warning
     */
    public static final EventType<ValidationEvent> VALIDATION_WARNING =
            new EventType<>(ValidationEvent.ANY, "VALIDATION_WARNING"); //NON-NLS

    /**
     * This event occurs on the validation has error
     */
    public static final EventType<ValidationEvent> VALIDATION_ERROR =
            new EventType<>(ValidationEvent.ANY, "VALIDATION_ERROR"); //NON-NLS

    /**
     * This event occurs on the validation status is unknown. This event can be used to clear the previous validation
     * status.
     */
    public static final EventType<ValidationEvent> VALIDATION_UNKNOWN =
            new EventType<>(ValidationEvent.ANY, "VALIDATION_UNKNOWN"); //NON-NLS

    public enum FailBehavior {

        /**
         * When validation fails, reverts back to the previous valid value. In case of the table cell editing, revert
         * and stop cell editing as normal.
         */
        REVERT,
        /**
         * When validation fails, it will self-correct to the getProposedValue() and stop cell editing as normal in case
         * of table cell.
         */
        SELF_CORRECT,
        /**
         * When validation fails, do not clear the invalid value (and in the case of table, do not stop cell editing)
         * and wait for user to enter a valid value or press ESCAPE to cancel the editing.
         */
        PERSIST,
        /**
         * This is used if you want to display the message but still want to commit the value as normal.
         */
        IGNORE,
    }

    private FailBehavior _failBehavior = FailBehavior.REVERT;

    private int _id;

    private Object _proposedValue;

    private String _message;

    /**
     * The shared ValidationEvent when the validation result is okay.
     */
    public static final ValidationEvent OK = new ValidationEvent(VALIDATION_OK);

    /**
     * The shared ValidationEvent when the validation result is unknown. You can use this event to clear the validation
     * result from last time. It is better than using OK because OK means there is no validation error. Using UNKNOWN
     * means there might be a validation warning error but we just don't know at the moment, so please clear the
     * validation status for now until further notice.
     */
    public static final ValidationEvent UNKNOWN = new ValidationEvent(VALIDATION_UNKNOWN);

    /**
     * Creates an empty ValidationEvent. The id is set to 0. The event type is set to UNKNOWN.
     */
    public ValidationEvent() {
        this(VALIDATION_UNKNOWN);
    }

    /**
     * Creates a ValidationEvent with the giving event type. The id is set to 0. The failBehavior is set to IGNORE for
     * OK, INFO and UNKNOWN event types, PERSIST for error and warning event types.
     *
     * @param type the event type
     */
    public ValidationEvent(EventType<ValidationEvent> type) {
        this(type, 0);
    }

    /**
     * Creates a ValidationEvent with a given event type, an id and no message. The failBehavior is set to IGNORE for
     * OK, INFO and UNKNOWN event types, PERSIST for error and warning event types.
     *
     * @param type the event type
     * @param id the result id. You can create the id by your definition
     */
    public ValidationEvent(EventType<ValidationEvent> type, int id) {
        this(type, id, "");
    }

    /**
     * Creates an ValidationEvent with a given event type, an id and a message. The failBehavior is set to IGNORE for
     * OK, INFO and UNKNOWN event types, PERSIST for error and warning event types.
     *
     * @param id      the result id. You can create the id by your definition
     * @param type    a boolean value to indicate if the value input is valid
     * @param message the message you want to display to your user
     */
    public ValidationEvent(EventType<ValidationEvent> type, int id, String message) {
        super(type);
        _id = id;
        _message = message;
        _failBehavior = isIgnorableEvent() ? FailBehavior.IGNORE : FailBehavior.PERSIST;
    }

    /**
     * Creates an ValidationEvent with a given event type, an id and a FailBehavior.
     *
     * @param id           the result id. You can create the id by your definition
     * @param type         a boolean value to indicate if the value input is valid
     * @param failBehavior the behavior
     */
    public ValidationEvent(EventType<ValidationEvent> type, int id, FailBehavior failBehavior) {
        super(type);
        _id = id;
        _failBehavior = failBehavior;
    }

    /**
     * Creates an ValidationEvent with a given event type, an id, a message and a FailBehavior.
     *
     * @param id           the result id. You can create the id by your definition
     * @param type         a boolean value to indicate if the value input is valid
     * @param failBehavior the behavior
     * @param message      the message you want to display to your user
     */
    public ValidationEvent(EventType<ValidationEvent> type, int id, FailBehavior failBehavior, String message) {
        super(type);
        _id = id;
        _failBehavior = failBehavior;
        _message = message;
    }

    /**
     * Creates a SELF_CORRECT ValidationEvent with a given event type, an id, a message and a proposed value. The
     * failBehavior is set to SELF_CORRECT.
     *
     * @param id            the result id. You can create the id by your definition
     * @param type          a boolean value to indicate if the value input is valid
     * @param proposedValue a proposed value
     * @param message       the message you want to display to your user
     */
    public ValidationEvent(EventType<ValidationEvent> type, int id, Object proposedValue, String message) {
        super(type);
        _id = id;
        _proposedValue = proposedValue;
        _message = message;
        _failBehavior = FailBehavior.SELF_CORRECT;
    }

    private boolean isIgnorableEvent() {
        return getEventType() == VALIDATION_OK || getEventType() == VALIDATION_INFO || getEventType() == VALIDATION_UNKNOWN;
    }

    /**
     * Gets the id of the ValidationEvent.
     *
     * @return the id.
     */
    public int getId() {
        return _id;
    }

    /**
     * Gets the proposed value of the ValidationEvent. It is only used if the failBehavior is SELF_CORRECT. For all
     * other failBehaviors, it will return null.
     *
     * @return the new value.
     */
    public Object getProposedValue() {
        return _proposedValue;
    }

    /**
     * Gets the message associated with the ValidationEvent.
     *
     * @return the message.
     */
    public String getMessage() {
        return _message;
    }

    /**
     * Gets the behavior if validation fails.
     *
     * @return the behavior if validation fails.
     */
    public FailBehavior getFailBehavior() {
        return _failBehavior;
    }

    @Override
    public EventType<ValidationEvent> getEventType() {
        return (EventType<ValidationEvent>) super.getEventType();
    }

    @Override
    public String toString() {
        String properties = " eventType=" + getEventType() + " id=" + getId() + " message=" + getMessage() + " failBehavior=" + getFailBehavior(); //NON-NLS
        return getClass().getName() + "[" + properties + "]";
    }
}

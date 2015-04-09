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
package jidefx.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * {@code LazyLoadUtils} provides an easy way to implement the lazy loading feature in a {@code Combobox} or a
 * {@code ChoiceBox}. Sometimes it takes a long time to create the ObservableList for the control. By using this
 * LazyLoadUtils, we will create the ObservableList only when the popup is about to show or after a delay, so that it
 * doesn't block the UI thread. The UI will come up without the ObservableList and will be set later.
 * <p>
 * To use it, simply call one of the install methods. The callback will take care of the creation of the
 * ObservableList.
 * <p>
 * There are two ways to trigger the callback. The first trigger is when the ComboBox or the ChoiceBox is clicked before
 * the popup content is about to show. The beforeShowing flag will determine if this trigger will be triggered. Default
 * is true. If this trigger is triggered, we will call the callback on the UI thread which means users will have to wait
 * for the callback to finish. We will have to do that because the popup doesn't make sense to show without the
 * ObservableList. The second trigger is triggered after a delay. The delay time is controlled by the delay parameter.
 * When it reaches the specified delay duration, a worker thread will run to call the callback so that it doesn't block
 * the UI. Either trigger can come first but it will be triggered only once. Ideally, when the UI is shown, if user
 * never clicks the ComboBox or the ChoiceBox, the second trigger kicks in and populates the data behind the scene. Not
 * ideally, user clicks on it right away and then he/she has to wait a while. However it is still much better than
 * waiting for the same period of time before the UI showing.
 * <p>
 * A typical use case is to create a ComboBox that list all the fonts in the system. However the Font.getFamilies call
 * is expensive, especially the system has a lot of fonts. The following code will take care of it.
 * <pre>{@code
 * ComboBox&lt;String&gt; fontComboBox = new ComboBox&lt;&gt;();
 * fontComboBox.setValue("Arial"); // set a default value without setting the Items
 * LazyLoadUtils.install(fontComboBox, new Callback&lt;ComboBox&lt;String&gt;, ObservableList&lt;String&gt;&gt;() {
 *     public ObservableList&lt;String&gt; call(ComboBox&lt;String&gt; comboBox) {
 *         return FXCollections.observableArrayList(Font.getFamilies());
 *     }
 * });
 * }</pre>
 */
public class LazyLoadUtils {
    private final static double DELAY = 200;
    private final static String PROPERTY_TASK = "LazyLoadUtils.Task"; //NON-NLS

    /**
     * Customizes the ComboBox to call the callback and setItems until user clicks on the ComboBox or after a 200 ms
     * delay.
     *
     * @param comboBox the ComboBox.
     * @param callback the callback to create the ObservableList.
     * @param <T>      The type of the value that has been selected or otherwise entered in to this ComboBox.
     */
    public static <T> void install(ComboBox<T> comboBox, Callback<ComboBox<T>, ObservableList<T>> callback) {
        install(comboBox, callback, Duration.millis(DELAY), true, null);
    }

    /**
     * Customizes the ComboBox to call the callback and setItems until user clicks on the ComboBox or after a delay.
     *
     * @param comboBox the ComboBox.
     * @param callback the callback to create the ObservableList.
     * @param delay    the delay before the callback is called.
     * @param <T>      The type of the value that has been selected or otherwise entered in to this ComboBox.
     */
    public static <T> void install(ComboBox<T> comboBox, Callback<ComboBox<T>, ObservableList<T>> callback, Duration delay) {
        install(comboBox, callback, delay, true, null);
    }

    /**
     * Customizes the ComboBox to call the callback and setItems until user clicks on the ComboBox (if beforeShowing is
     * true) or after a 200ms delay.
     *
     * @param comboBox      the ComboBox.
     * @param callback      the callback to create the ObservableList.
     * @param beforeShowing whether to trigger the callback before the ComboBox popup is about to show, if the callback
     * @param <T>           The type of the value that has been selected or otherwise entered in to this ComboBox.
     */
    public static <T> void install(ComboBox<T> comboBox, Callback<ComboBox<T>, ObservableList<T>> callback, boolean beforeShowing) {
        install(comboBox, callback, Duration.millis(DELAY), beforeShowing, null);
    }

    /**
     * Customizes the ComboBox to call the callback until user clicks on the ComboBox (if beforeShowing is true) or
     * after a delay.
     *
     * @param comboBox      the ComboBox.
     * @param callback      the callback to create the ObservableList.
     * @param delay         the delay before the callback is called.
     * @param beforeShowing whether to trigger the callback before the ComboBox popup is about to show, if the callback
     *                      hasn't called yet.
     * @param <T>           The type of the value that has been selected or otherwise entered in to this ComboBox.
     */
    public static <T> void install(ComboBox<T> comboBox, Callback<ComboBox<T>, ObservableList<T>> callback, Duration delay, boolean beforeShowing) {
        customizeComboBox(comboBox, callback, delay, beforeShowing, null);
    }

    /**
     * Customizes the ComboBox to call the callback until user clicks on the ComboBox (if beforeShowing is true) or
     * after a delay. It will also set the initialValue to the ComboBox.
     *
     * @param comboBox      the ComboBox.
     * @param callback      the callback to create the ObservableList.
     * @param delay         the delay before the callback is called.
     * @param beforeShowing whether to trigger the callback before the ComboBox popup is about to show, if the callback
     *                      hasn't called yet.
     * @param initialValue  the initial value. Null if the value has been set on the ComboBox.
     * @param <T>           The type of the value that has been selected or otherwise entered in to this ComboBox.
     */
    public static <T> void install(ComboBox<T> comboBox, Callback<ComboBox<T>, ObservableList<T>> callback, Duration delay, boolean beforeShowing, T initialValue) {
        customizeComboBox(comboBox, callback, delay, beforeShowing, initialValue);
    }

    /**
     * Customizes the ComboBox to call the callback until user clicks on the ComboBox or after a delay.
     *
     * @param comboBox      the ComboBox.
     * @param callback      the callback to create the ObservableList.
     * @param delay         the delay before the callback is called.
     * @param beforeShowing whether to trigger the callback before the ComboBox popup is about to show, if the callback
     *                      hasn't called yet.
     * @param initialValue  the initial value. Null if the value has been set on the ComboBox.
     * @param <T>           The type of the value that has been selected or otherwise entered in to this ComboBox.
     */
    protected static <T> void customizeComboBox(ComboBox<T> comboBox, Callback<ComboBox<T>, ObservableList<T>> callback, Duration delay, boolean beforeShowing, T initialValue) {
        Task oldTask = getTask(comboBox);
        if (oldTask != null) {
            oldTask.cancel(true);
        }

        if (initialValue != null) comboBox.setValue(initialValue);

        Task<ObservableList<T>> task = new Task<ObservableList<T>>() {
            @Override
            protected ObservableList<T> call() throws Exception {
                return callback.call(comboBox);
            }
        };
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                T item = comboBox.getValue();
                Object value = event.getSource().getValue();
                if (value instanceof ObservableList) {
                    //noinspection unchecked
                    comboBox.setItems((ObservableList<T>) value);

                    //noinspection unchecked
                    if (item == null || !((ObservableList<T>) value).contains(item)) {
                        comboBox.setValue(null);
                        comboBox.getSelectionModel().select(0);
                    }
                    else {
                        // trick in order to show the item. should be a bug in JavaFX combobox
                        comboBox.setValue(null);
                        comboBox.setValue(item);
                    }
                }

            }
        });

// when user clicks on the combobox, we will retrieve the fonts, not using thread because we don't want the empty
// list to show up. User has to wait for a while if the task takes a long time
        if (beforeShowing) {
            comboBox.setOnShowing(new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    if (!task.isRunning() && !task.isDone()) {
                        task.run();
                    }
                    comboBox.setOnShowing(null);
                }
            });
        }

// or use Timeline to get the font in a thread after 1 second, if user didn't click on the font combobox before that
        startTimer(delay, task);

        putTask(comboBox, task);
    }

    /**
     * Customizes the ChoiceBox to call the callback and setItems until user clicks on the ChoiceBox or after a 200 ms
     * delay.
     *
     * @param choiceBox the ChoiceBox.
     * @param callback  the callback to create the ObservableList.
     * @param <T>       The type of the value that has been selected or otherwise entered in to this ChoiceBox.
     */
    public static <T> void install(ChoiceBox<T> choiceBox, Callback<ChoiceBox<T>, ObservableList<T>> callback) {
        install(choiceBox, callback, Duration.millis(DELAY), true, null);
    }

    /**
     * Customizes the ChoiceBox to call the callback and setItems until user clicks on the ChoiceBox or after a delay.
     *
     * @param choiceBox the ChoiceBox.
     * @param callback  the callback to create the ObservableList.
     * @param delay     the delay before the callback is called.
     * @param <T>       The type of the value that has been selected or otherwise entered in to this ChoiceBox.
     */
    public static <T> void install(ChoiceBox<T> choiceBox, Callback<ChoiceBox<T>, ObservableList<T>> callback, Duration delay) {
        install(choiceBox, callback, delay, true, null);
    }

    /**
     * Customizes the ChoiceBox to call the callback and setItems until user clicks on the ChoiceBox (if beforeShowing
     * is true) or after a 200ms delay.
     *
     * @param choiceBox     the ChoiceBox.
     * @param callback      the callback to create the ObservableList.
     * @param beforeShowing whether to trigger the callback before the ChoiceBox popup is about to show, if the
     *                      callback
     * @param <T>           The type of the value that has been selected or otherwise entered in to this ChoiceBox.
     */
    public static <T> void install(ChoiceBox<T> choiceBox, Callback<ChoiceBox<T>, ObservableList<T>> callback, boolean beforeShowing) {
        install(choiceBox, callback, Duration.millis(DELAY), beforeShowing, null);
    }

    /**
     * Customizes the ChoiceBox to call the callback until user clicks on the ChoiceBox (if beforeShowing is true) or
     * after a delay.
     *
     * @param choiceBox     the ChoiceBox.
     * @param callback      the callback to create the ObservableList.
     * @param delay         the delay before the callback is called.
     * @param beforeShowing whether to trigger the callback before the ChoiceBox popup is about to show, if the callback
     *                      hasn't called yet.
     * @param <T>           The type of the value that has been selected or otherwise entered in to this ChoiceBox.
     */
    public static <T> void install(ChoiceBox<T> choiceBox, Callback<ChoiceBox<T>, ObservableList<T>> callback, Duration delay, boolean beforeShowing) {
        customizeChoiceBox(choiceBox, callback, delay, beforeShowing, null);
    }

    /**
     * Customizes the ChoiceBox to call the callback until user clicks on the ChoiceBox (if beforeShowing is true) or
     * after a delay. It will also set the initialValue to the ChoiceBox.
     *
     * @param choiceBox     the ChoiceBox.
     * @param callback      the callback to create the ObservableList.
     * @param delay         the delay before the callback is called.
     * @param beforeShowing whether to trigger the callback before the ChoiceBox popup is about to show, if the callback
     *                      hasn't called yet.
     * @param initialValue  the initial value. Null if the value has been set on the ChoiceBox.
     * @param <T>           The type of the value that has been selected or otherwise entered in to this ChoiceBox.
     */
    public static <T> void install(ChoiceBox<T> choiceBox, Callback<ChoiceBox<T>, ObservableList<T>> callback, Duration delay, boolean beforeShowing, T initialValue) {
        customizeChoiceBox(choiceBox, callback, delay, beforeShowing, initialValue);
    }

    /**
     * Customizes the ChoiceBox to call the callback until user clicks on the ChoiceBox or after a delay.
     *
     * @param choiceBox     the ChoiceBox.
     * @param callback      the callback to create the ObservableList.
     * @param delay         the delay before the callback is called.
     * @param beforeShowing whether to trigger the callback before the ChoiceBox popup is about to show, if the callback
     *                      hasn't called yet.
     * @param initialValue  the initial value. Null if the value has been set on the ChoiceBox.
     * @param <T>           The type of the value that has been selected or otherwise entered in to this ChoiceBox.
     */
    protected static <T> void customizeChoiceBox(ChoiceBox<T> choiceBox, Callback<ChoiceBox<T>, ObservableList<T>> callback, Duration delay, boolean beforeShowing, T initialValue) {
        Task oldTask = getTask(choiceBox);
        if (oldTask != null) {
            oldTask.cancel(true);
        }

        if (initialValue != null) choiceBox.setValue(initialValue);

        Task<ObservableList<T>> task = new Task<ObservableList<T>>() {
            @Override
            protected ObservableList<T> call() throws Exception {
                return callback.call(choiceBox);
            }
        };

        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                T item = choiceBox.getValue();
                Object value = event.getSource().getValue();
                if (value instanceof ObservableList) {
                    //noinspection unchecked
                    choiceBox.setItems((ObservableList<T>) value);

                    //noinspection unchecked
                    if (item == null || !((ObservableList<T>) value).contains(item)) {
                        choiceBox.setValue(null);
                        choiceBox.getSelectionModel().select(0);
                    }
                    else {
                        // trick in order to show the item. should be a bug in JavaFX combobox
                        choiceBox.setValue(null);
                        choiceBox.setValue(item);
                    }
                }
            }
        });

// when user clicks on the choicebox, we will retrieve the fonts, not using thread because we don't want the empty
// list to show up. User has to wait for a while if the task takes a long time
        if (beforeShowing) {
            choiceBox.setOnContextMenuRequested(new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    if (!task.isRunning() && !task.isDone()) {
                        task.run();
                    }
                    choiceBox.setOnContextMenuRequested(null);
                }
            });
        }

// or use Timeline to get the font in a thread after 1 second, if user didn't click on the font choicebox before that
        startTimer(delay, task);

        putTask(choiceBox, task);
    }

    // common
    private static <T> void startTimer(Duration delay, Task<ObservableList<T>> task) {
        if (delay != null && !delay.isIndefinite()) {
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(delay, new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    if (!task.isRunning() && !task.isDone()) {
                        new Thread(task).start();
                    }
                }
            }));
            timeline.play();
        }
    }

    private static Task getTask(Node node) {
        Object task = node.getProperties().get(PROPERTY_TASK);
        if (task instanceof Task) {
            return (Task) task;
        }
        else {
            return null;
        }
    }

    private static void putTask(Node node, Task task) {
        node.getProperties().put(PROPERTY_TASK, task);
    }
}

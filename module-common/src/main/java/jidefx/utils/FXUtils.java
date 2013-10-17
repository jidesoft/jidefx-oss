/*
 * @(#)FXUtils.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.utils;

import com.sun.javafx.Utils;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.SkinBase;
import javafx.stage.Screen;

import java.util.List;

/**
 * Utils that contains methods that depend on JavaFX classes.
 */
@SuppressWarnings("unchecked")
public class FXUtils {

    public static Rectangle2D getScreenBounds(Point2D point) {
        final Screen currentScreen = Utils.getScreenForPoint(point.getX(), point.getY());
        return Utils.hasFullScreenStage(currentScreen) ? currentScreen.getBounds() : currentScreen.getVisualBounds();
    }

    public static Insets add(Insets insets1, Insets insets2) {
        if (insets1 == null) {
            return insets2;
        }
        else if (insets2 == null) {
            return insets1;
        }
        return new Insets(insets1.getTop() + insets2.getTop(), insets1.getRight() + insets2.getRight(),
                insets1.getBottom() + insets2.getBottom(), insets1.getLeft() + insets2.getLeft());
    }

    /**
     * determine if the {@code Node} is the ancestorNode of another {@code Node} or the same.
     *
     * @param node         the node
     * @param ancestorNode the possible ancestorNode node
     * @return true of the ancestorNode is an ancestor of the node, or the same.
     */
    public static boolean isAncestor(Node node, Node ancestorNode) {
        if (node != null && ancestorNode != null) {
            Node parent = ancestorNode;
            while (parent != null) {
                if (parent == node) {
                    return true;
                }
                parent = parent.getParent();
            }
        }
        return false;
    }

    /**
     * Finds the ancestor of the node which is the type of the classToFind and before it reaches a node that is of type
     * classToStop.
     *
     * @param node        the node that we will look for the ancestor.
     * @param classToFind the ancestor type we will try to look for
     * @param classToStop the ancestor type which will stop looking for further if seeing a node of this type.
     * @return the ancestor node if any.
     */
    @SuppressWarnings("unchecked")
    public static Node findAncestor(Node node, Class classToFind, Class classToStop) {
        node = node.getParent();
        while (node != null) {
            if (classToStop != null && classToStop.isAssignableFrom(node.getClass())) {
                return null;
            }
            if (classToFind.isAssignableFrom(node.getClass())) {
                return node;
            }
            node = node.getParent();
        }
        return null;
    }

    /**
     * Finds the ancestor of the node which its type class name ends with String classNameEndWith and before it reaches
     * a node that is of type classToStop.
     *
     * @param node             the node that we will look for the ancestor.
     * @param classNameEndWith the ancestor class name that ends with this String
     * @param classToStop      the ancestor type which will stop looking for further if seeing a node of this type.
     * @return the ancestor node if any.
     */
    @SuppressWarnings("unchecked")
    public static Node findAncestor(Node node, String classNameEndWith, Class classToStop) {
        while (node != null) {
            if (classToStop != null && classToStop.isAssignableFrom(node.getClass())) {
                return null;
            }
            if (node.getClass().getName().endsWith(classNameEndWith)) {
                return node;
            }
            node = node.getParent();
        }
        return null;
    }

    /**
     * A simple handler used by setRecursively.
     * <pre>
     *  if ( condition() ) {
     *      action();
     *  }
     *  postAction();
     * </pre>.
     */
    @FunctionalInterface
    public interface Handler<T> {
        /**
         * If true, it will call {@link #action(Object)} on this Node.
         *
         * @param c the Node
         * @return true or false.
         */
        default boolean condition(T c) {
            return true;
        }

        /**
         * The action you want to perform on this Node. This method will only be called if {@link #condition(Object)}
         * returns true.
         *
         * @param c the Node
         */
        void action(T c);

        /**
         * The action you want to perform to any Nodes. If action(c) is called, this action is after it.
         *
         * @param c the Node.
         */
        default void postAction(T c) {
        }

    }

    /**
     * A simple handler used by setRecursively.
     * <pre>
     *  if ( condition() ) {
     *      action();
     *  }
     *  postAction();
     * </pre>.
     */
    @FunctionalInterface
    public interface ConditionHandler<T> extends Handler<T> {
        /**
         * If this method returns true, the recursive call will stop at the Node and the action will not be called on itself and its children.
         *
         * @param c the Node
         * @return true or false.
         */
        default boolean stopCondition(T c) {
            return false;
        }
    }

    /**
     * A simple handler used by getRecursively.
     * <pre>{@code
     *  if ( condition() ) {
     *      return action();
     *  }
     * }</pre>.
     * Here is an example to get the first child of the specified type.
     * <pre>{@code
     * public static Node getFirstChildOf(final Class clazz, Node c) {
     *     return getRecursively(c, new GetHandler() {
     *         public boolean condition(Node c) {
     *             return clazz.isAssignableFrom(c.getClass());
     *         }
     *         public Node action(Node c) {
     *             return c;
     *         }
     *     });
     * }
     * }</pre>
     */
    @FunctionalInterface
    public interface GetHandler<T> {
        /**
         * If true, it will call {@link #action(Object)} on this Node.
         *
         * @param c the Node
         * @return true or false.
         */
        boolean condition(T c);

        /**
         * The action you want to perform on this Node. This method will only be called if {@link #condition(Object)}
         * returns true.
         *
         * @param c the Node
         * @return the Node that will be returned from {@link FXUtils#getRecursively(Object, FXUtils.GetHandler)}.
         */
        default Object action(T c) {
            return c;
        }
    }

    /**
     * Calls the handler recursively on a Node.
     *
     * @param c       Node
     * @param handler handler to be called
     * @param <T>     the type of the Node.
     */
    public static <T> void setRecursively(final T c, final Handler<T> handler) {
        setRecursively0(c, handler);
        handler.postAction(c);
    }

    @SuppressWarnings("unchecked")
    private static <T> void setRecursively0(final T c, final Handler<T> handler) {
        if (handler instanceof ConditionHandler && ((ConditionHandler<T>) handler).stopCondition(c)) {
            return;
        }

        if (handler.condition(c)) {
            handler.action(c);
        }

        List<Node> children = null;

        if (c instanceof Parent) {
            children = ((Parent) c).getChildrenUnmodifiable();
        }
        if (children != null) {
            for (Node child : children) {
                if (child instanceof Parent) {
                    setRecursively0((T) child, handler);
                }
            }
        }
    }

    /**
     * Gets to a child of a Node recursively based on certain condition.
     *
     * @param c       Node
     * @param handler handler to be called
     * @param <T>     the type of the Node.
     * @return the Node that matches the condition specified in GetHandler.
     */
    public static <T> Object getRecursively(final T c, final GetHandler<T> handler) {
        return getRecursively0(c, handler);
    }

    private static <T> Object getRecursively0(final T c, final GetHandler<T> handler) {
        if (handler.condition(c)) {
            return handler.action(c);
        }

        List<Node> children = null;

        if (c instanceof Parent) {
            children = ((Parent) c).getChildrenUnmodifiable();
        }
        else if (c instanceof SkinBase) {
            children = ((SkinBase) c).getChildren();
        }

        if (children != null) {
            for (Node child : children) {
                if (child instanceof Parent) {
                    Object result = getRecursively0((T) child, handler);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

// TODO: remove it for now to fix the compilation error in JDK8 b111.
//    /**
//     * calculate the layout point for a popup window based on the two points, windowPoint from popup window and
//     * parentPoint from parent node
//     *
//     * @param window      the popup window
//     * @param windowPoint align point inside popup window
//     * @param parent      parent node with which the popup window try to align
//     * @param parentPoint align point inside parent node
//     * @return the Point2D
//     */
//    public static Point2D pointRelativeTo(PopupWindow window, Point2D windowPoint, Node parent, Point2D parentPoint) {
//        if (parent == null || window == null) {
//            return null;
//        }
//
//        double anchorWidth = window.getWidth();
//        double anchorHeight = window.getHeight();
//
//        Point2D point2D = Utils.pointRelativeTo(parent, anchorWidth, anchorHeight, HPos.CENTER, VPos.BOTTOM, false);
////        Rectangle2D screenBounds = JideFXUtilities.getScreenBounds(new Point2D(0, parentPoint.getY()));
////        if (screenBounds.getMaxX() >= point2D.getX() + anchorWidth + parentPoint.getX() - windowPoint.getX()) {
////            point2D = point2D.add(parentPoint.getX() - windowPoint.getX(), 0);
////        }
////        if (screenBounds.getMaxY() >= point2D.getY() + anchorHeight + parentPoint.getY() - parent.getLayoutBounds().getMaxY() - windowPoint.getY()) {
////            point2D = point2D.add(0, parentPoint.getY() - parent.getLayoutBounds().getMaxY() + windowPoint.getY());
////        }
//
//        point2D = new Point2D(point2D.getX() + parentPoint.getX() - windowPoint.getX(), point2D.getY() + parentPoint.getY() - parent.getLayoutBounds().getMaxY() + windowPoint.getY());
//
//        return point2D;
//    }

    /**
     * Runs the runnable using Platform.runLater if the current thread is not on JavaFX Application Thread.
     *
     * @param runnable runnable to run
     */
    public static void runThreadSafe(Runnable runnable) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(runnable);
        }
        else {
            runnable.run();
        }
    }

}

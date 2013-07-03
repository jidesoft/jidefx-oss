/*
 * @(#)DecorationUtils.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.decoration;

import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * {@code DecorationUtils} provides a few convenient ways to install (and uninstall) decorators to a target node.
 * <p/>
 * You can install multiple decorators to the same node, which can be achieved by calling install several times or
 * installAll with all the decorators. However if you only call install or installAll methods, the decorations won't
 * show up yet. The other part of the story is to use DecorationPane as the targetNode?s ancestor. It doesn't have to be
 * the immediate ancestor. It will work as long as the DecorationPane is one of its ancestors. You can create a form
 * with a bunch of nodes, fields, comboboxes, tables, lists, whatever you want, call DecorationUtils.install to add
 * decoration to some of them, then wrap the whole form in the DecorationPane at the end. See below for a sample code.
 * <p/>
 * <pre>{@code
 * // create nodes and add it to a pane
 * Pane pane = new Xxxx ();
 * pane.getChildren().addAll(?);
 * return new DecorationPane(pane);  // instead of return pane, you wrap it
 * into
 * a DecorationPane
 * }</pre>
 * <p/>
 * It is the DecorationPane which will search for all the decorators installed on its children (more precisely,
 * descendants) and placed them at the position as specified in the Decorator interface.
 * <p/>
 * Please note, JavaFX only allows a Node to be added to the same Scene just once. The decoration is a Node as well so
 * it cannot be reused to decorate several target Nodes. If you want to use the same decoration several times, please
 * use a factory pattern like this.
 * <p/>
 * <pre>{@code
 * Supplier&lt;Decorator&gt; asteriskFactory = new Supplier&lt;Decorator&gt;() {
 *     public Decorator get() {
 *         Label label = new Label("*", asteriskImage);
 *         return new Decorator(label, Pos.TOP_RIGHT);
 *     }
 * };
 * // you can use it's create method to create multiple same decorators and use
 * them on different Nodes.
 * DecorationUtils.install(nameField, asteriskFactory.get());
 * DecorationUtils.install(emailField, asteriskFactory.get());
 * }</pre>
 */
@SuppressWarnings("UnusedDeclaration")
public class DecorationUtils {

    /**
     * The property name where the decorators are installed on a Node's getProperties().
     */
    private static final String PROPERTY_DECORATOR = "Decoration.Decorator"; //NON-NLS
    /**
     * The property name where the old insets is saved on a Node's getProperties().
     */
    private static final String PROPERTY_TARGET_NODE_PADDING = "Decoration.Target.Node.Padding"; //NON-NLS
    /**
     * The property name on a target node which indicates the status of all decoration nodes
     */
    private static final String PROPERTY_TARGET_DECORATOR_STATUS = "Decoration.Target.Decorator.Status"; //NON-NLS
    /**
     * Indicates whether decorate node has been removed from decoration pane from outside. For example, decoration node
     * will bee cleaned from table column header by sorting.
     */
    private static final String PROPERTY_TARGET_DECORATOR_STATUS_CLEANED = "cleaned"; //NON-NLS
    /**
     * The property name on a decorate node which indicates the status of the animation
     */
    private static final String PROPERTY_DECORATOR_ANIMATION_STATUS = "Decoration.Decorator.Animation.Status"; //NON-NLS
    /**
     * Indicates whether the animation on decorate node has been played.
     */
    private static final String PROPERTY_DECORATOR_ANIMATION_STATUS_PLAYED = "played"; //NON-NLS

    /**
     * Installs a {@code Decorator} to a target Node.
     *
     * @param targetNode the target Node where the {@code Decorator} will be installed.
     * @param decorator  a {@code Decorator}
     */
    public static void install(Node targetNode, Decorator decorator) {
        if (targetNode != null && decorator != null) {
            decorator.getNode().setVisible(false);

            Object object = getDecorators(targetNode);
            if (object instanceof Decorator) {
                if (!object.equals(decorator)) {
                    installAll(targetNode, (Decorator) object, decorator);
                }
            }
            else if (object instanceof Decorator[]) {
                Decorator[] oldDecorators = (Decorator[]) object;
                if (!Arrays.asList(oldDecorators).contains(decorator)) {
                    Decorator[] newDecorators = new Decorator[oldDecorators.length + 1];
                    System.arraycopy(oldDecorators, 0, newDecorators, 0, oldDecorators.length);
                    newDecorators[oldDecorators.length] = decorator;
                    targetNode.getProperties().put(PROPERTY_DECORATOR, newDecorators);
                }
            }
            else {
                targetNode.getProperties().put(PROPERTY_DECORATOR, decorator);
            }
        }
    }

    /**
     * Installs some {@code Decorator}s to a target Node.
     *
     * @param targetNode the target Node where the {@code Decorator}s will be installed.
     * @param decorators {@code Decorator}s that will be installed.
     */
    public static void installAll(Node targetNode, Decorator... decorators) {
        if (targetNode != null && decorators != null) {
            for (Decorator decorator : decorators) {
                if (decorator != null) {
                    decorator.getNode().setVisible(false);
                }
            }
            targetNode.getProperties().put(PROPERTY_DECORATOR, decorators);
        }
    }

    /**
     * Installs a same {@code Decorator} to many target Nodes. Because each {@code Decorator} instance can only be
     * installed to one Node, that's why we use a Factory here so that for each target Node, a new {@code Decorator}
     * will be created.
     *
     * @param targetNodes      the target Nodes where the {@code Decorator} will be installed.
     * @param decoratorFactory a Factory that will create a new {@code Decorator} for each target Node.
     */
    public static void installAll(List<Node> targetNodes, Supplier<Decorator> decoratorFactory) {
        for (Node node : targetNodes) {
            install(node, decoratorFactory.get());
        }
    }

    /**
     * Checks if the node has decorator(s) installed.
     *
     * @param targetNode the target Node.
     * @return true if there are decorators, otherwise false.
     */
    public static boolean hasDecorators(Node targetNode) {
        Object object = targetNode.getProperties().get(DecorationUtils.PROPERTY_DECORATOR);
        return object instanceof Decorator || object instanceof Decorator[];
    }

    /**
     * Gets the decorators.
     *
     * @param targetNode the target Node.
     * @return an object that could be a Decorator or a Decorator array.
     */
    public static Object getDecorators(Node targetNode) {
        return targetNode.getProperties().get(DecorationUtils.PROPERTY_DECORATOR);
    }

    /**
     * Uninstalls all decorators from the target Node.
     *
     * @param targetNode the target Node.
     */
    public static void uninstall(Node targetNode) {
        if (targetNode != null) {
            targetNode.getProperties().remove(PROPERTY_DECORATOR);
            targetNode.getProperties().remove(DecorationUtils.PROPERTY_TARGET_DECORATOR_STATUS);
        }
    }

    /**
     * Uninstalls a decorator from the target Node.
     *
     * @param targetNode the target Node.
     * @param decorator  the decorator to be uninstalled.
     */
    public static void uninstall(Node targetNode, Decorator decorator) {
        if (targetNode != null && decorator != null) {
            Object object = targetNode.getProperties().get(PROPERTY_DECORATOR);
            if (object instanceof Decorator && object.equals(decorator)) {
                targetNode.getProperties().remove(PROPERTY_DECORATOR);
            }
            else if (object instanceof Decorator[]) {
                Decorator[] oldDecorators = (Decorator[]) object;
                if (oldDecorators.length == 1) {
                    if (oldDecorators[0].equals(decorator)) {
                        targetNode.getProperties().remove(PROPERTY_DECORATOR);
                    }
                }
                else {
                    Decorator[] tempDecorators = new Decorator[oldDecorators.length];
                    int j = 0;
                    for (Decorator oldDecorator : oldDecorators) {
                        if (!oldDecorator.equals(decorator)) {
                            tempDecorators[j++] = oldDecorator;
                        }
                    }

                    if (j == 0) {
                        targetNode.getProperties().remove(PROPERTY_DECORATOR);
                    }
                    else if (j < oldDecorators.length) {
                        Decorator[] newDecorators = new Decorator[j];
                        System.arraycopy(tempDecorators, 0, newDecorators, 0, j);
                        targetNode.getProperties().put(PROPERTY_DECORATOR, newDecorators);
                    }
                }
            }
        }
    }

    private static boolean isVisible(Node c) {
        Node parent = c;
        while (parent != null) {
            if (!parent.isVisible()) {
                return false;
            }
            parent = parent.getParent();
        }
        return true;
    }

    /**
     * A util method that computes the bounds of the decorations.
     *
     * @param targetNode   the target node.
     * @param decorationNode the decoration node.
     * @param decorator    the decorator.
     * @return the bounds of the decoration.
     */
    private static Bounds computeDecorationBounds(Node targetNode, Node decorationNode, Decorator decorator) {
        double width = decorationNode.prefWidth(-1);
        double height = decorationNode.prefHeight(-1);

        // we use local coordinate to do the calculation
        // default TOP_LEFT
        Bounds targetBounds = targetNode.getBoundsInLocal();  // ensure position will be right after the transition is applied

        // adjust to center of the decoration node
        double x = targetBounds.getMinX() - width / 2;
        double y = targetBounds.getMinY() - height / 2;

        double targetWidth = targetBounds.getWidth();
        double targetHeight = targetBounds.getHeight();

        if (targetWidth <= 0) {
            targetWidth = targetNode.prefWidth(-1);
        }
        if (targetHeight <= 0) {
            targetHeight = targetNode.prefHeight(-1);
        }

        double baselineOffset = targetNode.getBaselineOffset();
        Pos pos = decorator.getPos();
        // position
        switch (pos) {
            case TOP_CENTER:
                x += targetWidth / 2;
                break;
            case TOP_RIGHT:
                x += targetWidth;
                break;
            case CENTER_LEFT:
                y += targetHeight / 2;
                break;
            case CENTER:
                x += targetWidth / 2;
                y += targetHeight / 2;
                break;
            case CENTER_RIGHT:
                x += targetWidth;
                y += targetHeight / 2;
                break;
            case BOTTOM_LEFT:
                y += targetHeight;
                break;
            case BOTTOM_CENTER:
                x += targetWidth / 2;
                y += targetHeight;
                break;
            case BOTTOM_RIGHT:
                x += targetWidth;
                y += targetHeight;
                break;
            case BASELINE_LEFT:
                y += baselineOffset - decorationNode.getBaselineOffset() + height / 2;
                break;
            case BASELINE_CENTER:
                x += targetWidth / 2;
                y += baselineOffset - decorationNode.getBaselineOffset() + height / 2;
                break;
            case BASELINE_RIGHT:
                x += targetWidth;
                y += baselineOffset - decorationNode.getBaselineOffset() + height / 2;
                break;
        }

        // offset
        Point2D decoratorPosOffset = decorator.getPosOffset();
        if (decorator.isValueInPercent()) {
            x += decoratorPosOffset.getX() * width / 100;
            y += decoratorPosOffset.getY() * height / 100;
        }
        else {
            x += decoratorPosOffset.getX();
            y += decoratorPosOffset.getY();
        }

        return adjustBounds(decorationNode.getLayoutBounds(), new BoundingBox(x, y, width, height));
    }


    /**
     * A util method that computes the bounds of the decorations.
     *
     * @param parentNode   the parent node.
     * @param targetNode   the target node.
     * @param decorateNode the decoration node.
     * @param decorator    the decorator.
     * @return the bounds of the decoration.
     */
    static Bounds computeDecorationBounds(Node parentNode, Node targetNode, Node decorateNode, Decorator decorator) {
        return parentNode.screenToLocal(targetNode.localToScreen(computeDecorationBounds(targetNode, decorateNode, decorator)));
    }

    /**
     * To determine if the decorators on a node were cleaned.
     *
     * @param targetNode the node which has decorators.
     * @return true, means the decorators on this node had been cleaned. false, means the decorators on this node were
     *         not cleaned.
     * @see #setTargetDecoratorCleaned(javafx.scene.Node, boolean)
     */

    static <T extends Node> boolean isTargetDecoratorCleaned(T targetNode) {
        return targetNode != null && PROPERTY_TARGET_DECORATOR_STATUS_CLEANED.equals(targetNode.getProperties().get(PROPERTY_TARGET_DECORATOR_STATUS));
    }

    /**
     * Set the status of decorators on a node. There are only two status cleaned, means the decorators on this node had
     * been cleaned somehow. not cleaned.
     *
     * @param targetNode the node which has decorators.
     * @param clean      true, set the status to be status of cleaned. false, clear the status.
     */
    public static void setTargetDecoratorCleaned(Node targetNode, boolean clean) {
        if (targetNode == null) {
            return;
        }
        if (clean) {
            targetNode.getProperties().put(PROPERTY_TARGET_DECORATOR_STATUS, PROPERTY_TARGET_DECORATOR_STATUS_CLEANED);
        }
        else {
            targetNode.getProperties().remove(PROPERTY_TARGET_DECORATOR_STATUS);
        }
    }

    static Insets getTargetPadding(Region targetNode) {
        if (targetNode == null) return null;
        else {
            Object insets = targetNode.getProperties().get(PROPERTY_TARGET_NODE_PADDING);
            if (insets instanceof Insets) return (Insets) insets;
            else {
                Insets padding = targetNode.getPadding();
                setTargetPadding(targetNode, padding);
                return padding;
            }
        }
    }

    static void setTargetPadding(Region targetNode, Insets rawInsets) {
        if (targetNode == null) {
            return;
        }
        targetNode.getProperties().put(PROPERTY_TARGET_NODE_PADDING, rawInsets);
    }

    /**
     * To determine if the animation on a decorator has been played.
     *
     * @param decorateNode the decorate node related with an animation.
     * @return true, means the animation has been played. false, means the animation has not been played.
     * @see #setAnimationPlayed(javafx.scene.Node, boolean)
     */
    public static boolean isAnimationPlayed(Node decorateNode) {
        if (decorateNode == null) {
            return false;
        }
        Object status = decorateNode.getProperties().get(PROPERTY_DECORATOR_ANIMATION_STATUS);
        return status != null && status.equals(PROPERTY_DECORATOR_ANIMATION_STATUS_PLAYED);
    }

    /**
     * Set the status of playing of the animation on a decorator.
     *
     * @param decorateNode the decorate node related with an animation.
     * @param played       the status to be set.
     * @see #isAnimationPlayed(javafx.scene.Node)
     */
    public static void setAnimationPlayed(Node decorateNode, boolean played) {
        if (decorateNode == null) {
            return;
        }
        if (played) {
            decorateNode.getProperties().put(PROPERTY_DECORATOR_ANIMATION_STATUS, PROPERTY_DECORATOR_ANIMATION_STATUS_PLAYED);
        }
        else {
            decorateNode.getProperties().remove(PROPERTY_DECORATOR_ANIMATION_STATUS);
        }
    }

    static <T extends Node> Insets computePadding(Region targetNode, Decorator<T> decorator) {
        Node node = decorator.getNode();
        Pos pos = decorator.getPos();
        Point2D offset = decorator.getPosOffset();
        boolean valueInPercentOrPixels = decorator.isValueInPercent();

        if (offset == null) {
            offset = new Point2D(0, 0);
        }
        Insets targetPadding = getTargetPadding(targetNode);
        double w = node.prefWidth(-1);
        double h = node.prefHeight(-1);
        double right = 0, left = 0;
        double x = offset.getX();
        double y = offset.getY();
        if (valueInPercentOrPixels) {
            x *= w / 100;
            y *= h / 100;
        }

        // TODO: compare the bounds. Will profile it later to see if there is any performance issue.
        Bounds boundsInLocal = targetNode.getBoundsInLocal();
        Bounds actualBounds = new BoundingBox(boundsInLocal.getMinX() + targetPadding.getLeft(), boundsInLocal.getMinY() + targetPadding.getTop(),
                boundsInLocal.getWidth() - targetPadding.getLeft() - targetPadding.getRight(), boundsInLocal.getHeight() - targetPadding.getTop() - targetPadding.getBottom());
        Bounds decorationBounds = computeDecorationBounds(targetNode, node, decorator);
        boolean inside = actualBounds.intersects(decorationBounds);
        if (inside) {
            // TODO: need to consider the y as well. If y makes the decoration node completely outside the target node, no need to add left or right padding.
            switch (pos) {
                case TOP_LEFT:
                case CENTER_LEFT:
                case BOTTOM_LEFT:
                case BASELINE_LEFT:
                    left = Math.max(0, x + w / 2 - targetPadding.getLeft() + 1); // TODO: 1 is the extra gap, should be calculated automatically
                    break;
                case TOP_RIGHT:
                case CENTER_RIGHT:
                case BASELINE_RIGHT:
                case BOTTOM_RIGHT:
                    right = Math.max(0, -x + w / 2 - targetPadding.getLeft() + 1); // TODO: 1 is the extra gap, should be calculated automatically
            }
        }

        return new Insets(0, right, 0, left);
    }

    /**
     * Adjust on local bounds, be sure to make this adjustment with padding computation.
     *
     * @param decorateLocalBounds the local bounds of decorate node.
     * @param targetLocalBounds   the local bounds of target node.
     * @return The new bounds
     * @see #adjustPadding
     */
    private static Bounds adjustBounds(Bounds decorateLocalBounds, Bounds targetLocalBounds) {
        return new BoundingBox(
                targetLocalBounds.getMinX() - decorateLocalBounds.getMinX(),
                targetLocalBounds.getMinY() - decorateLocalBounds.getMinY(),
                targetLocalBounds.getWidth(), targetLocalBounds.getHeight());
    }

    /**
     * Adjust padding on local bounds, be sure to make this adjustment with bounds computation.
     *
     * @param decorateLocalBounds the local bounds of decorate node.
     * @param padding             the padding to be adjusted.
     * @return The new padding
     * @see #adjustBounds
     */
    private static Insets adjustPadding(Bounds decorateLocalBounds, Insets padding) {
        return new Insets(
                padding.getTop() - decorateLocalBounds.getMinY(),
                padding.getRight() - decorateLocalBounds.getMinX(),
                padding.getBottom() - decorateLocalBounds.getMinY(),
                padding.getLeft() - decorateLocalBounds.getMinX());
    }
}

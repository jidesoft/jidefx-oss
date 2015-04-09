/*
 * @(#)DecorationDelegate.java 5/19/2013
 *
 * Copyright 2002 - 2013 JIDE Software Inc. All rights reserved.
 */

package jidefx.scene.control.decoration;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.Region;
import jidefx.utils.FXUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * {@code DecorationDelegate} works with the {@code DecorationSupport} interface to allow any Region supporting
 * decorations.
 * <p/>
 * To do it, implement {@link DecorationSupport} on a Region subclass. In the Region subclass, you add the following
 * code.
 * <pre>
 * {@code
 * private DecorationDelegate _decorationDelegate;
 *
 * // initialize it somewhere in the constructor
 *     _decorationDelegate = new DecorationDelegate(this);
 *
 * protected void layoutChildren() {
 *     _decorationDelegate.prepareDecorations();
 *     super.layoutChildren();
 *     Platform.runLater(new Runnable() {
 *         public void run() {
 *             _decorationDelegate.layoutDecorations();
 *         }
 *     });
 * }
 *
 * public ObservableList&lt;Node&gt; getChildren() {
 *    return super.getChildren();
 * }
 *
 * public void positionInArea(Node child, double areaX, double areaY, double areaWidth, double areaHeight, double
 * areaBaselineOffset, HPos halignment, VPos valignment) {
 *     super.positionInArea(child, areaX, areaY, areaWidth, areaHeight, areaBaselineOffset, halignment, valignment);
 * }
 *
 * }
 * </pre>
 */
@SuppressWarnings("Convert2Lambda")
public class DecorationDelegate {
    private Region _parent;

    private ObservableMap<Node, DecorationPair> _appliedDecorationsMap; // (decorate node, (target node, decorator))
    private Map<Node, Insets> _appliedDecorationsPadding;               // (decorate node, insets)

    private boolean _isPaddingApplied;
    private InvalidationListener _invalidationListener;
    private HashMap<Decorator,Node> _currentDecorators;
    private List<Node> _currentTargetNodes;

    public DecorationDelegate(Region parent) {
        parent.getStylesheets().add(DecorationDelegate.class.getResource("Decoration.css").toExternalForm()); //NON-NLS
        _parent = parent;
        _appliedDecorationsMap = FXCollections.observableHashMap();
        _appliedDecorationsPadding = new HashMap<>();

        _isPaddingApplied = false;

        MapChangeListener<Node, DecorationPair> mapChangeListener = new MapChangeListener<Node, DecorationPair>() {
            @Override
            public void onChanged(Change change) {
                Node decorateNode = (Node) change.getKey();
                if (change.wasAdded()) {
                    try {
                        decorateNode.setManaged(false);
                        ((DecorationSupport) _parent).getChildren().add(decorateNode);
//                        Node targetNode = _appliedDecorationsMap.get(decorateNode)._targetNode;
//                        decorateNode.scaleXProperty().bind(targetNode.scaleXProperty());
//                        decorateNode.scaleYProperty().bind(targetNode.scaleYProperty());
//                        decorateNode.scaleZProperty().bind(targetNode.scaleZProperty());
//                        decorateNode.visibleProperty().bind(targetNode.visibleProperty());
//                        decorateNode.opacityProperty().bind(targetNode.opacityProperty());
                        _isPaddingApplied = false;
                    }
                    catch (Exception e) {
                        // duplicate decoration node
                    }
                }
                else if (change.wasRemoved()) {
                    ((DecorationSupport) _parent).getChildren().remove(decorateNode);
//                    decorateNode.scaleXProperty().unbind();
//                    decorateNode.scaleYProperty().unbind();
//                    decorateNode.scaleZProperty().unbind();
//                    decorateNode.visibleProperty().unbind();
//                    decorateNode.opacityProperty().unbind();
                    _isPaddingApplied = false;
                }
            }
        };
        _appliedDecorationsMap.addListener(mapChangeListener);

        _invalidationListener = new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                _parent.requestLayout();
            }
        };
    }

    class DecorationPair {
        Node _targetNode;
        Decorator _decorator;

        public DecorationPair(Node targetNode, Decorator decorator) {
            _targetNode = targetNode;
            _decorator = decorator;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DecorationPair that = (DecorationPair) o;

            if (_decorator != null ? !_decorator.equals(that._decorator) : that._decorator != null) {
                return false;
            }

            if (_targetNode != null ? !_targetNode.equals(that._targetNode) : that._targetNode != null) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            int result = _targetNode != null ? _targetNode.hashCode() : 0;
            result = 31 * result + (_decorator != null ? _decorator.hashCode() : 0);
            return result;
        }
    }

    public void prepareDecorations() {
        // restore decorators in case that they were removed by someone outside of decoration system
        if (DecorationUtils.isTargetDecoratorCleaned(_parent)) {
            for (Node node : _appliedDecorationsMap.keySet()) {
                if (!((DecorationSupport) _parent).getChildren().contains(node)) {
                    ((DecorationSupport) _parent).getChildren().add(node);
                }
            }
        }

        _currentTargetNodes = new ArrayList<>();

        _currentDecorators = new HashMap<>();
        FXUtils.setRecursively(_parent, new FXUtils.ConditionHandler<Node>() {
            @Override
            public boolean condition(Node c) {
                return DecorationUtils.hasDecorators(c);
            }

            @Override
            public void action(Node c) {
                Object object = DecorationUtils.getDecorators(c);
                if (object instanceof Decorator) {
                    _currentDecorators.put((Decorator) object, c);
                }
                else if (object instanceof Decorator[]) {
                    for (Decorator decorator : (Decorator[]) object) {
                        _currentDecorators.put(decorator, c);
                    }
                }
                if (!_currentTargetNodes.contains(c)) {
                    _currentTargetNodes.add(c);
                }
            }

            @Override
            public boolean stopCondition(Node c) {
                return c instanceof DecorationSupport && !c.equals(_parent);
            }
        });

        // and add decorator in new list to children
        for (Decorator decorator : _currentDecorators.keySet()) {
            Node targetNode = _currentDecorators.get(decorator);
            Node decorateNode = findDecoration(targetNode, decorator);
            if (decorateNode == null) {
                decorateNode = decorator.getNode();
                if (decorateNode != null) {
                    addDecorator(targetNode, decorator, decorateNode);
                }
            }
        }

        // remove decorator not in new list from children
        for (int i = ((DecorationSupport) _parent).getChildren().size() - 1; i >= 0; i--) {
            Node decorateNode = ((DecorationSupport) _parent).getChildren().get(i);
            if (decorateNode.equals(_parent)) {
                continue;
            }
            if (_appliedDecorationsMap.get(decorateNode) != null && !_currentDecorators.containsKey(_appliedDecorationsMap.get(decorateNode)._decorator)) {
                ((DecorationSupport) _parent).getChildren().remove(i);
                Node targetNode = _appliedDecorationsMap.get(decorateNode)._targetNode;
                if (!_currentTargetNodes.contains(targetNode)) {
                    _currentTargetNodes.add(targetNode); // for adjusting padding later
                }

                // Be sure to clear listeners on the mutable decorators before remove it from decoration pane
                Decorator decorator = _appliedDecorationsMap.get(decorateNode)._decorator;
                if (decorator instanceof MutableDecorator) {
                    clearPropertyChangeListeners((MutableDecorator) decorator);
                }

                _appliedDecorationsMap.remove(decorateNode);
                _appliedDecorationsPadding.remove(decorateNode);
            }
        }


        // register sensitive listeners after installed into decoration pane
        for (Node decorateNode : _appliedDecorationsMap.keySet()) {
            if (_appliedDecorationsMap.get(decorateNode) == null) {
                continue;
            }
            Decorator decorator = _appliedDecorationsMap.get(decorateNode)._decorator;
            // add property change listeners to mutable decorator
            if (decorator instanceof MutableDecorator) {
                clearPropertyChangeListeners((MutableDecorator) decorator);
                registerPropertyChangeListeners((MutableDecorator) decorator);
            }
        }

    }

    private void registerPropertyChangeListeners(MutableDecorator<?> decorator) {
        decorator.posProperty().addListener(_invalidationListener);
        decorator.offsetProperty().addListener(_invalidationListener);
        decorator.paddingProperty().addListener(_invalidationListener);
        decorator.transitionProperty().addListener(_invalidationListener);
        decorator.valueInPercentProperty().addListener(_invalidationListener);
    }

    private void clearPropertyChangeListeners(MutableDecorator<?> decorator) {
        decorator.posProperty().removeListener(_invalidationListener);
        decorator.offsetProperty().removeListener(_invalidationListener);
        decorator.paddingProperty().removeListener(_invalidationListener);
        decorator.transitionProperty().removeListener(_invalidationListener);
        decorator.valueInPercentProperty().removeListener(_invalidationListener);
    }

    public void layoutDecorations() {
        applyDecoration();
    }

    protected boolean addDecorator(Node targetNode, Decorator decorator, Node decoration) {
        return !(targetNode == null || decorator == null) && _appliedDecorationsMap.put(decoration, new DecorationPair(targetNode, decorator)) != null;
    }

    private void applyDecoration() {
        for (Decorator decorator : _currentDecorators.keySet()) {
            updateDecorationPadding(_currentDecorators.get(decorator), decorator);
        }
        if (!_isPaddingApplied) {
            Map<Node, Insets> _appliedSumPadding = new HashMap<>();
            for (Node targetNode : _currentTargetNodes) {
                Insets sum = new Insets(0); // Sum padding of all decorators on this target node
                if (_appliedSumPadding.get(targetNode) != null) {
                    sum = _appliedSumPadding.get(targetNode);
                }
                else if (targetNode instanceof Region) {
                    Insets raw = DecorationUtils.getTargetPadding((Region) targetNode);
                    sum = new Insets(sum.getTop() + raw.getTop(), sum.getRight() + raw.getRight(), sum.getBottom() + raw.getBottom(), sum.getLeft() + raw.getLeft());
                }

                Object object = DecorationUtils.getDecorators(targetNode);
                if (object instanceof Decorator) {
                    Decorator decorator = (Decorator) object;
                    Insets padding = _appliedDecorationsPadding.get(decorator.getNode());
                    if (padding != null) {
                        sum = new Insets(sum.getTop() + padding.getTop(), sum.getRight() + padding.getRight(), sum.getBottom() + padding.getBottom(), sum.getLeft() + padding.getLeft());
                    }
                }
                else if (object instanceof Decorator[]) {
                    for (Decorator decorator : (Decorator[]) object) {
                        Insets padding = _appliedDecorationsPadding.get(decorator.getNode());
                        if (padding != null) {
                            sum = new Insets(sum.getTop() + padding.getTop(), sum.getRight() + padding.getRight(), sum.getBottom() + padding.getBottom(), sum.getLeft() + padding.getLeft());
                        }
                    }
                }

                _appliedSumPadding.put(targetNode, sum);

                if (targetNode instanceof Region) {
                    String paddingStyle = "-fx-padding: " + sum.getTop() + " " + sum.getRight() + " " + sum.getBottom() + " " + sum.getLeft() + ";"; //NON-NLS
                    String newStyle = targetNode.getStyle().replaceAll("-fx-padding:.*;", "") + paddingStyle;
                    if(!newStyle.equals(targetNode.getStyle())){
                        targetNode.setStyle(newStyle);
                    }
//                    ((Region) targetNode).setPadding(sum);
                }
            }
            _isPaddingApplied = true;
        }

        for (Node decorateNode : _appliedDecorationsMap.keySet()) {
            Node targetNode = _appliedDecorationsMap.get(decorateNode)._targetNode;
            if (targetNode.getScene() == null) { // in case the node has been removed from the scene
                continue;
            }
            Decorator decorator = _appliedDecorationsMap.get(decorateNode)._decorator;

            Bounds bounds = DecorationUtils.computeDecorationBounds(
                    _parent,
                    targetNode,
                    decorateNode,
                    decorator);

            decorateNode.resize(bounds.getWidth(), bounds.getHeight());
            ((DecorationSupport) _parent).positionInArea(decorateNode, bounds.getMinX(), bounds.getMinY(), bounds.getWidth(), bounds.getHeight(), 0, HPos.CENTER, VPos.CENTER);
// TODO: fix the decoration node moves after install
//            System.out.println(bounds);

            decorateNode.setVisible(true);

            // animation
            // TODO: sync all animations
//            new AnimationManager(_parent).register(decorateNode, decorator.getTransition());
            if (decorator.getTransition() != null && !DecorationUtils.isAnimationPlayed(decorateNode)) {
                decorator.getTransition().play();
                DecorationUtils.setAnimationPlayed(decorateNode, true);
            }
        }
    }

    private Node findDecoration(Node targetNode, Decorator decorator) {
        Set<Node> decorations = _appliedDecorationsMap.keySet();
        for (Node decoration : decorations) {
            DecorationPair pair = _appliedDecorationsMap.get(decoration);
            if (pair._decorator == decorator && pair._targetNode == targetNode) {
                return decoration;
            }
        }
        return null;
    }

    private void updateDecorationPadding(Node targetNode, Decorator decorator) {
        Insets newPadding = decorator.getPadding();                              // actual or percent
        Insets oldPadding = _appliedDecorationsPadding.get(decorator.getNode()); // percent

        if (newPadding == null) {
            newPadding = DecorationUtils.computePadding((Region)targetNode, decorator);
            _appliedDecorationsPadding.put(decorator.getNode(), newPadding);
            _isPaddingApplied = false;
        }
        else {
            if (decorator.isValueInPercent()) {
                double width = decorator.getNode().prefWidth(-1);
                double height = decorator.getNode().prefHeight(-1);
                newPadding = new Insets(
                        newPadding.getTop() * height / 100,
                        newPadding.getRight() * width / 100,
                        newPadding.getBottom() * height / 100,
                        newPadding.getLeft() * width / 100);
            }
            if (oldPadding == null || !newPadding.equals(oldPadding)) {
                _appliedDecorationsPadding.put(decorator.getNode(), newPadding);
                _isPaddingApplied = false;
            }
        }
    }
}

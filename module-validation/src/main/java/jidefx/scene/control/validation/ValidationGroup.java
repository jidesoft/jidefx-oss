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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.Node;

import java.util.Map;
import java.util.stream.Stream;

public class ValidationGroup {

    private ObservableMap<Node, ValidationStatus> validationNodes = FXCollections.observableHashMap();
    private BooleanProperty invalidProperty = new SimpleBooleanProperty();

    public ValidationGroup(Node... targetNodes) {
        for (Node node : targetNodes) {
            if (node != null) {
                validationNodes.put(node, ValidationUtils.getValidationStatus(node));
                node.addEventHandler(ValidationEvent.ANY, event -> {
                    validationNodes.put((Node) event.getTarget(), ValidationUtils.getValidationStatus((Node) event.getTarget()));
                });
            }
        }

        validationNodes.addListener((MapChangeListener<Node, ValidationStatus>) change -> {
            Stream<Map.Entry<Node, ValidationStatus>> entryStream = validationNodes.entrySet().stream().filter(entry -> entry.getValue() == ValidationStatus.VALIDATION_ERROR);

            invalidProperty.set(entryStream.count() > 0);
        });
    }

    public Boolean isInvalid() {
        return invalidProperty.get();
    }

    public ReadOnlyBooleanProperty invalidProperty() {
        return invalidProperty;
    }

    public void addValidationNode(Node targetNode) {
        validationNodes.put(targetNode, ValidationUtils.getValidationStatus(targetNode));
    }

    public void removeValidationNode(Node targetNode) {
        validationNodes.remove(targetNode);
    }
}

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
package jidefx.scene.control.decoration;

import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DecorationPaneTest {
    static {
        // force initialization of UI toolkit
        new JFXPanel();
    }

    @Test
    public void ensureDecorationPaneHasASingleChild() {
        // given:
        TextField content = new TextField();
        TextField field = new TextField();
        DecorationPane decorationPane = new DecorationPane();

        // expect:
        assertNull(decorationPane.getContent());

        // when:
        decorationPane.setContent(content);
        // then:
        assertNotNull(decorationPane.getContent());
        assertEquals(content, decorationPane.getContent());

        // when:
        decorationPane.setContent(null);
        // then:
        assertNull(decorationPane.getContent());

        // when:
        decorationPane.getChildren().add(content);
        // then:
        assertNotNull(decorationPane.getContent());
        assertEquals(content, decorationPane.getContent());

        // when:
        decorationPane.getChildren().add(field);
        // then:
        assertEquals(1, decorationPane.getChildren().size());
        assertNotNull(decorationPane.getContent());
        assertEquals(field, decorationPane.getContent());
    }

    @Test
    public void loadFXMLWithExplicitContent() throws Exception {
        // given:
        URL resource = DecorationPaneTest.class.getClassLoader().getResource("jidefx/scene/control/decoration/DecorationPaneTestExplicitContent.fxml");

        // when:
        FXMLLoader loader = new FXMLLoader(resource);
        loader.load();

        // then:
        DecorationPaneTestController controller = loader.getController();
        assertNotNull(controller.decorationPane);
        assertNotNull(controller.content);
        assertEquals(controller.content, controller.decorationPane.getContent());
    }

    @Test
    public void loadFXMLWithImplicitContent() throws Exception {
        // given:
        URL resource = DecorationPaneTest.class.getClassLoader().getResource("jidefx/scene/control/decoration/DecorationPaneTestImplicitContent.fxml");

        // when:
        FXMLLoader loader = new FXMLLoader(resource);
        loader.load();

        // then:
        DecorationPaneTestController controller = loader.getController();
        assertNotNull(controller.decorationPane);
        assertNotNull(controller.content);
        assertEquals(controller.content, controller.decorationPane.getContent());
    }
}

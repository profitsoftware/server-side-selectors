/*
 * Copyright 2013 Profit Software Oy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.profitsoftware.vaadin.query;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

import com.profitsoftware.vaadin.query.ComponentVisitor;
import com.profitsoftware.vaadin.query.VisitableComponents;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class VisitableComponentsTest extends JUnit4Mockery {

  @SuppressWarnings("serial")
  @Test
  public void shouldVisitOnUI() {
    final UI ui = new UI() {
      @Override
      protected void init(VaadinRequest request) {
      }
    };
    VisitableComponents visitableComponents = new VisitableComponents(ui);
    final ComponentVisitor componentVisitor = mock(ComponentVisitor.class);
    checking(new Expectations() {{
      oneOf(componentVisitor).startComponent(ui); will(returnValue(null));
      oneOf(componentVisitor).endComponent(ui);
    }});
    visitableComponents.acceptVisitor(componentVisitor);
    assertIsSatisfied();
  }

  @SuppressWarnings("serial")
  @Test
  public void shouldVisitOnUIEvenExpectChilds() {
    final UI ui = new UI() {
      @Override
      protected void init(VaadinRequest request) {
      }
    };
    VisitableComponents visitableComponents = new VisitableComponents(ui);
    final ComponentVisitor componentVisitor = mock(ComponentVisitor.class);
    checking(new Expectations() {{
      oneOf(componentVisitor).startComponent(ui); will(returnValue(componentVisitor));
      oneOf(componentVisitor).endComponent(ui);
    }});
    visitableComponents.acceptVisitor(componentVisitor);
    assertIsSatisfied();
  }

  @SuppressWarnings("serial")
  @Test
  public void shouldVisitUIAndButton() {
    final UI ui = new UI() {
      @Override
      protected void init(VaadinRequest request) {
      }
    };

    final Button button = new Button();
    ui.setContent(button);

    VisitableComponents visitableComponents = new VisitableComponents(ui);

    final ComponentVisitor componentVisitor = mock(ComponentVisitor.class);
    checking(new Expectations() {{
      oneOf(componentVisitor).startComponent(ui); will(returnValue(componentVisitor));
      oneOf(componentVisitor).startComponent(button); will(returnValue(componentVisitor));
      oneOf(componentVisitor).endComponent(button);
      oneOf(componentVisitor).endComponent(ui);
    }});
    visitableComponents.acceptVisitor(componentVisitor);
    assertIsSatisfied();
  }

  @SuppressWarnings("serial")
  @Test
  public void shouldVisitNotDeeperIfNotVisitorReturnd() {
    final UI ui = new UI() {
      @Override
      protected void init(VaadinRequest request) {
      }
    };

    final VerticalLayout layout = new VerticalLayout();
    final Button button = new Button();
    layout.addComponent(button);
    ui.setContent(layout);

    VisitableComponents visitableComponents = new VisitableComponents(ui);

    final ComponentVisitor componentVisitor = mock(ComponentVisitor.class);
    checking(new Expectations() {{
      oneOf(componentVisitor).startComponent(ui); will(returnValue(componentVisitor));
      oneOf(componentVisitor).startComponent(layout); will(returnValue(componentVisitor));
      oneOf(componentVisitor).startComponent(button); will(returnValue(componentVisitor));
      oneOf(componentVisitor).endComponent(button);
      oneOf(componentVisitor).endComponent(layout);
      oneOf(componentVisitor).endComponent(ui);

      oneOf(componentVisitor).startComponent(ui); will(returnValue(componentVisitor));
      oneOf(componentVisitor).startComponent(layout); will(returnValue(null));
      oneOf(componentVisitor).endComponent(layout);
      oneOf(componentVisitor).endComponent(ui);
    }});
    visitableComponents.acceptVisitor(componentVisitor);
    visitableComponents.acceptVisitor(componentVisitor);
    assertIsSatisfied();
  }


}

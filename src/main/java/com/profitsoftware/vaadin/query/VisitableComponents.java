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

import java.io.Serializable;

import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

public class VisitableComponents implements Serializable {

  private static final long serialVersionUID = 1L;

  private final Component rootComponent;

  public VisitableComponents(final Component rootComponent) {
    this.rootComponent = rootComponent;
  }

  public void acceptVisitor(ComponentVisitor visitor) {
    visitComponents(rootComponent, visitor);
  }

  protected void visitComponents(final Component component, final ComponentVisitor componentVisitor) {
    ComponentVisitor visitor = componentVisitor.startComponent(component);
    if (visitor != null && component instanceof HasComponents)
     for (Component childComponent : (HasComponents)component) {
       visitComponents(childComponent,visitor);
     }
     componentVisitor.endComponent(component);
  }
}

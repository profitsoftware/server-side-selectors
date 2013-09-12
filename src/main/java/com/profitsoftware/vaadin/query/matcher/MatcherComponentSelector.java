/*
 * Copyright 2013 Profit Software Oy
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.profitsoftware.vaadin.query.matcher;

import com.profitsoftware.vaadin.query.ComponentQueryResultCollection;
import com.profitsoftware.vaadin.query.QueryAction;
import com.profitsoftware.vaadin.query.ComponentQueryResult;
import com.profitsoftware.vaadin.query.ComponentQuery;
import com.profitsoftware.vaadin.query.ComponentVisitor;
import com.profitsoftware.vaadin.query.Matcher;
import com.profitsoftware.vaadin.query.VisitableComponents;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

public class MatcherComponentSelector implements ComponentQuery {

  private static final long serialVersionUID = 1749980267489694162L;

  private final Matcher matcher;

  public MatcherComponentSelector(final Matcher matcher) {
    this.matcher = matcher;
  }

  public void doAction(final HasComponents rootComponent, final QueryAction action) {
    new VisitableComponents(rootComponent).acceptVisitor(new ComponentVisitor() {
      public ComponentVisitor startComponent(Component component) {
        if (matcher.match(component)) {
          action.action(component);
        }
        return this;
      }
      public void endComponent(Component component) {}
    });
  }

  public ComponentQueryResult from(HasComponents rootComponent) {
    final ComponentQueryResultCollection componentSelection = new ComponentQueryResultCollection();
    doAction(rootComponent, componentSelection);
    return componentSelection;
  }
}

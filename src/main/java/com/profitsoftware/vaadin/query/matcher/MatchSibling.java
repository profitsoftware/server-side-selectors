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
package com.profitsoftware.vaadin.query.matcher;

import java.util.Iterator;

import com.profitsoftware.vaadin.query.Matcher;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;

public class MatchSibling implements Matcher {

  private static final long serialVersionUID = 1L;

  public Matcher matcher;

  public MatchSibling(Matcher matcher) {
    this.matcher = matcher;
  }

  public boolean match(Component component) {
    Component parent = component.getParent();
    if (parent == null) {
      return false;
    }
    if (parent instanceof HasComponents) {
      HasComponents components = (HasComponents) parent;
      Iterator<Component> i = components.iterator();
      Component previous = null;
      while (i.hasNext()) {
        Component next = i.next();
        if (next == component) {
          return matcher.match(previous);
        }
        previous = next;
      }
    }
    return false;
  }


  @Override
  public String toString() {
   return "sibling(" + matcher + ")";
  }
}

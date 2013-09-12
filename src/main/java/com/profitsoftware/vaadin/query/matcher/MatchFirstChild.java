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

public class MatchFirstChild implements Matcher {

  private static final long serialVersionUID = 6522282521281521068L;

  private final Matcher matcher;

  public MatchFirstChild(Matcher matcher) {
    this.matcher = matcher;
  }

  public boolean match(Component component) {
    if (!matcher.match(component)) {
      return false;
    }
    Component parent = component.getParent();
    if (parent == null) {
      return true;
    }
    if (parent instanceof HasComponents) {
      HasComponents parentComponents = (HasComponents) parent;
      Iterator<Component> i = parentComponents.iterator();
      if (i.hasNext() && i.next() == component) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
   return "first-child("+matcher+")";
  }
}

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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.vaadin.ui.Component;

public class ComponentQueryResultCollection implements ComponentQueryResult, QueryAction {

  private static final long serialVersionUID = 3915331810560090867L;

  private Collection<Component> components;

  public enum DefaultAction implements QueryAction {
    HIDE {
      public void action(Component component) {
        component.setVisible(false);
      }
    },
    SHOW {
      public void action(Component component) {
        component.setVisible(true);
      }
    },
    ENABLE {
      public void action(Component component) {
        component.setEnabled(true);
      }
    },
    DISABLE {
      public void action(Component component) {
        component.setEnabled(false);
      }
    },
    READ_ONLY {
      public void action(Component component) {
        component.setReadOnly(true);
      }
    },
    WRITEABLE {
      public void action(Component component) {
        component.setReadOnly(false);
      }
    },
    SIZE_FULL {
      public void action(Component component) {
        component.setSizeFull();
      }
    },
    SIZE_UNDEFINED {
      public void action(Component component) {
        component.setSizeUndefined();
      }
    };
  }

  public ComponentQueryResultCollection() {
    this.components = new ArrayList<Component>();
  }

  public Iterator<Component> iterator() {
    return components.iterator();
  }

  public void forEach(QueryAction action) {
    for(Component c : components) {
      action.action(c);
    }
  }

  public void hide() {
    forEach(DefaultAction.HIDE);
  }

  public void show() {
    forEach(DefaultAction.SHOW);
  }

  public void disable() {
    forEach(DefaultAction.DISABLE);
  }

  public void enable() {
    forEach(DefaultAction.ENABLE);
  }

  public void setReadOnly() {
    forEach(DefaultAction.READ_ONLY);
  }

  public void setWritable() {
    forEach(DefaultAction.WRITEABLE);
  }

  public void setSizeFull() {
    forEach(DefaultAction.SIZE_FULL);
  }

  public void sizeUndefined() {
    forEach(DefaultAction.SIZE_UNDEFINED);
  }

  public void action(Component component) {
    components.add(component);
  }
}

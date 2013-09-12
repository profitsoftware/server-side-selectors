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

import java.util.StringTokenizer;

import com.profitsoftware.vaadin.query.Matcher;
import com.vaadin.ui.Component;

public class MatchStyle implements Matcher {

  private static final long serialVersionUID = 1L;

  private String style;

  public MatchStyle(String style) {
    this.style = style;
  }

  public boolean match(Component component) {
    if (component == null) {
      return false;
    }
    String styleName = component.getStyleName();
    if (styleName == null) {
      return false;
    }
    if (styleName.contains(" ")) {
      StringTokenizer stringTokenizer = new StringTokenizer(styleName, " ");
      while (stringTokenizer.hasMoreElements()) {
        if (style.equals(stringTokenizer.nextElement())) {
          return true;
        }
      }
      return false;
    }
    return styleName.equals(style);
  }
  @Override
  public String toString() {
   return "style('"+style+"')";
  }


}

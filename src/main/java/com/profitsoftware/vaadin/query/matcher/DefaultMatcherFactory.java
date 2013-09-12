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

import java.util.Arrays;

import com.profitsoftware.vaadin.query.Matcher;
import com.profitsoftware.vaadin.query.MatcherFactory;

public class DefaultMatcherFactory implements MatcherFactory {

  public Matcher any() {
    return MatchAny.ANY;
  }

  public Matcher type(String type) {
    if ("*".equals(type)) {
      return any();
    }
    return new MatchType(type);
  }

  public Matcher style(String style) {
    return new MatchStyle(style);
  }

  public Matcher and(Matcher ...matchers) {
    for (int i = 0; i < matchers.length; i++) {
      if (MatchAny.isMatchAny(matchers[i])) {
        matchers[i] = null;
      }
    }
    matchers = shrink(matchers);
    if (matchers.length == 0) {
      return any();
    }
    if (matchers.length == 1) {
      return matchers[0];
    }
    return new MatchAnd(matchers);
  }

  private Matcher[] shrink(Matcher[] matchers) {
    int p = 0;
    for (int i = 0; i < matchers.length; i++) {
      if (matchers[i] != null) {
        matchers[p++] = matchers[i];
      }
    }
    return Arrays.copyOf(matchers, p);
  }

  public Matcher or(Matcher ...matchers) {
    for (int i = 0; i < matchers.length; i++) {
      if (MatchAny.isMatchAny(matchers[i])) {
        return any();
      }
    }
    matchers = shrink(matchers);
    if (matchers.length == 0) {
      return any();
    }
    if (matchers.length == 1) {
      return matchers[0];
    }
    return new MatchOr(matchers);
  }

  public Matcher id(String id) {
    return new MatchId(id);
  }

  public Matcher parent(Matcher matcher) {
    if (MatchAny.isMatchAny(matcher)) {
      return any();
    }
    return new MatchParent(matcher);
  }

  public Matcher ancestor(Matcher matcher) {
    if (MatchAny.isMatchAny(matcher)) {
      return any();
    }
    return new MatchAncestor(matcher);
  }

  public Matcher sibling(Matcher matcher) {
    if (MatchAny.isMatchAny(matcher)) {
      return any();
    }
    return new MatchSibling(matcher);
  }

  public Matcher firstChild(Matcher matcher) {
    return new MatchFirstChild(matcher);
  }

  public Matcher not(Matcher matcher) {
    if (matcher instanceof MatchNot) {
      MatchNot notMatcher = (MatchNot) matcher;
      return notMatcher.getMatcher();
    }
    return new MatchNot(matcher);
  }
}

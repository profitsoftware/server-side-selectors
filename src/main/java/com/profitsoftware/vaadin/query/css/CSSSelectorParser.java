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
package com.profitsoftware.vaadin.query.css;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.profitsoftware.vaadin.query.ComponentQuery;
import com.profitsoftware.vaadin.query.Matcher;
import com.profitsoftware.vaadin.query.MatcherFactory;
import com.profitsoftware.vaadin.query.matcher.DefaultMatcherFactory;
import com.profitsoftware.vaadin.query.matcher.MatcherComponentSelector;


public class CSSSelectorParser {

  private MatcherFactory factory = new DefaultMatcherFactory();

  public void setMatcherFactory(MatcherFactory factory) {
    this.factory = factory;
  }

  public Matcher matcher(String s) {
    s = s.trim();
    s = s.replaceAll("\\s+", " ");
    s = s.replaceAll("\\s*\\+\\s*", "+");
    s = s.replaceAll("\\s*\\>\\s*", ">");
    String[] selectors = s.split(",");
    List<Matcher> matchers = new ArrayList<Matcher>();
    for (String selector : selectors) {
      matchers.add(parseSingleSelector(selector));
    }
    return factory.or(matchers.toArray(new Matcher[matchers.size()]));
  }

  public ComponentQuery selector(String s) {
    return createSelector(matcher(s));
  }

  protected ComponentQuery createSelector(Matcher matcher) {
    return new MatcherComponentSelector(matcher);
  }

  enum SelectorParserState {
    EXPECT_RELATION_OPER, EXPECT_ELEMENT_MATCHER;
  }

  protected Matcher parseSingleSelector(String s) {
    SelectorParserState state = SelectorParserState.EXPECT_ELEMENT_MATCHER;
    StringTokenizer stringTokenizer = new StringTokenizer(s," +>",true);
    Matcher currentMatcher = factory.any();
    while (stringTokenizer.hasMoreElements()) {
      String token = stringTokenizer.nextToken();
      switch (state) {
        case EXPECT_RELATION_OPER:
          currentMatcher = parseOper(token, currentMatcher);
          state = SelectorParserState.EXPECT_ELEMENT_MATCHER;
          break;
        case EXPECT_ELEMENT_MATCHER:
          currentMatcher = factory.and(parseElementMatcher(token), currentMatcher);
          state = SelectorParserState.EXPECT_RELATION_OPER;
          break;
      }
    }
    return currentMatcher;
  }

  protected Matcher parseElementMatcher(String elementMatcher) {
    if ("*".equals(elementMatcher)) {
      return factory.any();
    }
    if (elementMatcher.startsWith("*")) {
      elementMatcher = elementMatcher.substring(1);
    }
    List<Matcher> matchers = new ArrayList<Matcher>();
    StringTokenizer stringTokenizer = new StringTokenizer(elementMatcher,".#:",true);
    if (!stringTokenizer.hasMoreElements()) {
      throw new IllegalStateException("Couldn't parse : " + elementMatcher);
    }
    String pseudoClass = null;
    while (stringTokenizer.hasMoreElements()) {
      String element = stringTokenizer.nextToken();
      if (matchers.isEmpty() && !isElementOper(element)) {
        matchers.add(factory.type(element));
        continue;
      }
      if (".".equals(element)) {
        matchers.add(factory.style(stringTokenizer.nextToken()));
      } else if ("#".equals(element)) {
        matchers.add(factory.id(stringTokenizer.nextToken()));
      } else if (":".equals(element)) {
        pseudoClass = stringTokenizer.nextToken();
        if (!"first-child".equals(pseudoClass)) {
          throw new IllegalArgumentException("Unknown pseudo class " + pseudoClass);
        }
      }
    }
    Matcher[] matchersArray = matchers.toArray(new Matcher[matchers.size()]);
    Matcher mtcher = factory.and(matchersArray);
    if (pseudoClass != null) {
      mtcher = factory.firstChild(mtcher);
    }
    return mtcher;
  }

  private boolean isElementOper(String element) {
    return ".".equals(element) || "#".equals(element) || ":".equals(element);
  }

  private Matcher parseOper(String token, Matcher currentMatcher) {
    if (" ".equals(token)) {
      return factory.ancestor(currentMatcher);
    }
    if (">".equals(token)) {
      return factory.parent(currentMatcher);
    }
    if ("+".equals(token)) {
      return factory.sibling(currentMatcher);
    }
    throw new IllegalStateException("Unknown hierarchy operator : " + token);
  }
}

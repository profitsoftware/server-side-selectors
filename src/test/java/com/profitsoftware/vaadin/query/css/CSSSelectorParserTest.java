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

import org.junit.Assert;
import org.junit.Test;

import com.profitsoftware.vaadin.query.Matcher;
import com.profitsoftware.vaadin.query.css.CSSSelectorParser;


public class CSSSelectorParserTest {

  @Test
  public void shouldParseNormalCSS() {
    CSSSelectorParser parser = new CSSSelectorParser();
    Matcher m = parser.matcher("div ol > li p");
    Assert.assertEquals("and(type('p'),ancestor(and(type('li'),parent(and(type('ol'),ancestor(type('div')))))))", m.toString());
  }


  @Test
  public void shouldParseMyComponentSelectors() {
    CSSSelectorParser parser = new CSSSelectorParser();
    Assert.assertEquals("and(type('button'),style('cool'))", parser.matcher("button.cool").toString());
    Assert.assertEquals("and(type('cool'),ancestor(type('button')))", parser.matcher("button cool").toString());
    Assert.assertEquals("first-child(all())", parser.matcher(":first-child").toString());
    Assert.assertEquals("first-child(all())", parser.matcher("*:first-child").toString());
    Assert.assertEquals("type('p')", parser.matcher("* p").toString());
    Assert.assertEquals("type('p')", parser.matcher("*>p").toString());
    Assert.assertEquals("type('p')", parser.matcher("*+p").toString());
    Assert.assertEquals("ancestor(type('p'))", parser.matcher("p *").toString());
    Assert.assertEquals("parent(type('p'))", parser.matcher("p>*").toString());
    Assert.assertEquals("sibling(type('p'))", parser.matcher("p+*").toString());
    Assert.assertEquals("and(style('cool'),sibling(type('p')))", parser.matcher("p+*.cool").toString());
    Assert.assertEquals("or(and(style('cool'),sibling(type('p'))),and(type('b'),ancestor(type('a'))))", parser.matcher("p+*.cool,a b").toString());


  }

}

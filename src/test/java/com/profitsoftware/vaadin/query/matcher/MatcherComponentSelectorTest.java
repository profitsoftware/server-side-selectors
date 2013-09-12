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

import org.junit.Assert;
import org.junit.Test;

import com.profitsoftware.vaadin.query.ComponentQuery;
import com.profitsoftware.vaadin.query.css.CSSSelectorParser;
import com.profitsoftware.vaadin.query.matcher.MatchAncestor;
import com.profitsoftware.vaadin.query.matcher.MatchAnd;
import com.profitsoftware.vaadin.query.matcher.MatchAny;
import com.profitsoftware.vaadin.query.matcher.MatchFirstChild;
import com.profitsoftware.vaadin.query.matcher.MatchSibling;
import com.profitsoftware.vaadin.query.matcher.MatchStyle;
import com.profitsoftware.vaadin.query.matcher.MatchType;
import com.profitsoftware.vaadin.query.matcher.MatcherComponentSelector;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class MatcherComponentSelectorTest {

  @SuppressWarnings("serial")
  private UI createUI() {
    UI ui = new UI() {
      @Override
      protected void init(VaadinRequest request) {
      }
    };
    return ui;
  }

  @Test
  public void selectUI() {
    UI ui = createUI();
    ComponentQuery componentSelector = new MatcherComponentSelector(new MatchAny());
    Iterator<Component>  i = componentSelector.from(ui).iterator();
    Assert.assertSame(ui, i.next());
    Assert.assertFalse(i.hasNext());
  }

  @Test
  public void selectAll() {
    UI ui = createUI();
    VerticalLayout layout = new VerticalLayout();
    ui.setContent(layout);
    Button button1 = new Button();
    Button button2 = new Button();

    layout.addComponents(button1, button2);
    ComponentQuery componentSelector = new MatcherComponentSelector(new MatchAny());
    Iterator<Component>  i = componentSelector.from(ui).iterator();
    Assert.assertSame(ui, i.next());
    Assert.assertSame(layout, i.next());
    Assert.assertSame(button1, i.next());
    Assert.assertSame(button2, i.next());
    Assert.assertFalse(i.hasNext());
  }

  @Test
  public void selectButtons() {
    UI ui = createUI();
    VerticalLayout layout = new VerticalLayout();
    ui.setContent(layout);
    Button button1 = new Button();
    Button button2 = new Button();

    layout.addComponents(button1, button2);
    // button
    ComponentQuery componentSelector = new MatcherComponentSelector(new MatchType("button"));
    Iterator<Component>  i = componentSelector.from(ui).iterator();
    Assert.assertSame(button1, i.next());
    Assert.assertSame(button2, i.next());
    Assert.assertFalse(i.hasNext());
  }



  @Test
  public void selectFirstButton() {
    UI ui = createUI();
    VerticalLayout layout = new VerticalLayout();
    ui.setContent(layout);
    Button button1 = new Button();
    Button button2 = new Button();

    layout.addComponents(button1, button2);
    // button:first-child
    ComponentQuery componentSelector = new MatcherComponentSelector(new MatchFirstChild(new MatchType("button")));
    Iterator<Component>  i = componentSelector.from(ui).iterator();
    Assert.assertSame(button1, i.next());
    Assert.assertFalse(i.hasNext());
  }

  @Test
  public void selectFirstCoolButton() {
    UI ui = createUI();
    VerticalLayout layout = new VerticalLayout();
    ui.setContent(layout);
    Button button1 = new Button();
    button1.addStyleName("cool");
    Button button2 = new Button();
    button2.addStyleName("cool");

    layout.addComponents(button1, button2);
    // button.cool:first-child
    ComponentQuery componentSelector = new MatcherComponentSelector(new MatchFirstChild(new MatchAnd(new MatchType("button"),new MatchStyle("cool"))));
    Iterator<Component>  i = componentSelector.from(ui).iterator();
    Assert.assertSame(button1, i.next());
    Assert.assertFalse(i.hasNext());
  }

  @Test
  public void selectAllChildsOfVerticalLayout() {
    UI ui = createUI();
    VerticalLayout layout1 = new VerticalLayout();
    HorizontalLayout layout2 = new HorizontalLayout();
    ui.setContent(layout1);
    layout1.addComponent(layout2);
    Button button1 = new Button();
    Button button2 = new Button();

    layout2.addComponents(button1, button2);
    // "VerticalLayout button"
    ComponentQuery componentSelector = new MatcherComponentSelector(new MatchAnd(new MatchType("button"),new MatchAncestor(new MatchType("VerticalLayout"))));
    Iterator<Component>  i = componentSelector.from(ui).iterator();
    Assert.assertSame(button1, i.next());
    Assert.assertSame(button2, i.next());
    Assert.assertFalse(i.hasNext());
  }

  @Test
  public void selectAdjecentSiblingHAvingStyleCool() {
    UI ui = createUI();
    VerticalLayout layout1 = new VerticalLayout();
    HorizontalLayout layout2 = new HorizontalLayout();
    ui.setContent(layout1);
    layout1.addComponent(layout2);
    Button button1 = new Button();
    Button button2 = new Button();
    Button button3 = new Button();
    Button button4 = new Button();
    Button button5 = new Button();
    Button button6 = new Button();
    Button button7 = new Button();
    layout2.addComponents(button1, button2,button3,button4,button5,button6,button7);

    button3.addStyleName("cool");


    button6.addStyleName("cool");
// .cool
    ComponentQuery componentSelector = new MatcherComponentSelector(new MatchSibling(new MatchStyle("cool")));
    Iterator<Component>  i = componentSelector.from(ui).iterator();
    Assert.assertSame(button4, i.next());
    Assert.assertSame(button7, i.next());

    Assert.assertFalse(i.hasNext());
  }


  @Test
  public void selectUsingCSSNotation() {
    UI ui = createUI();
    VerticalLayout layout1 = new VerticalLayout();
    HorizontalLayout layout2 = new HorizontalLayout();
    ui.setContent(layout1);
    layout1.addComponent(layout2);
    Button button1 = new Button();
    Button button2 = new Button();
    Button button3 = new Button();
    Button button4 = new Button();
    Button button5 = new Button();
    Button button6 = new Button();
    Button button7 = new Button();

    layout2.addComponents(button1,button2,button3,button4,button5,button6,button7);
    button3.addStyleName("cool");
    button6.addStyleName("cool");
    CSSSelectorParser parser = new CSSSelectorParser();

    ComponentQuery componentSelector;
    componentSelector = new MatcherComponentSelector(parser.matcher(".cool+*"));
    Iterator<Component>  i = componentSelector.from(ui).iterator();
    Assert.assertSame(button4, i.next());
    Assert.assertSame(button7, i.next());
    Assert.assertFalse(i.hasNext());


    i = parser.query("button").from(ui).iterator();
    Assert.assertSame(button1, i.next());
    Assert.assertSame(button2, i.next());
    Assert.assertSame(button3, i.next());
    Assert.assertSame(button4, i.next());
    Assert.assertSame(button5, i.next());
    Assert.assertSame(button6, i.next());
    Assert.assertSame(button7, i.next());
    Assert.assertFalse(i.hasNext());

  }


}

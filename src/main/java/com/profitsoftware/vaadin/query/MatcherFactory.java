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

public interface MatcherFactory {

  Matcher any();

  Matcher type(String type);

  Matcher style(String style);

  Matcher id(String id);

  Matcher or(Matcher ...matchers);

  Matcher and(Matcher ...matchers);

  Matcher sibling(Matcher matcher);

  Matcher ancestor(Matcher matcher);

  Matcher parent(Matcher matcher);

  Matcher firstChild(Matcher matcher);

}

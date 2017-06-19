/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.devnull.trugger.element.impl;

import tools.devnull.trugger.element.ElementCopier;
import tools.devnull.trugger.element.ElementFactory;
import tools.devnull.trugger.element.ElementFinder;
import tools.devnull.trugger.element.ElementSelector;
import tools.devnull.trugger.element.ElementsSelector;
import tools.devnull.trugger.util.registry.MapRegistry;
import tools.devnull.trugger.util.registry.Registry;

import java.lang.annotation.Annotation;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import static tools.devnull.trugger.reflection.ClassPredicates.arrayType;
import static tools.devnull.trugger.reflection.ClassPredicates.assignableTo;

/**
 * A default implementation for ElementFactory.
 *
 * @author Marcelo Guimarães
 */
public final class TruggerElementFactory implements ElementFactory {

  private ElementFinder finder;
  private final MapRegistry<Predicate<Class>, ElementFinder> registry;

  public TruggerElementFactory() {
    registry = new MapRegistry<Predicate<Class>, ElementFinder>(new LinkedHashMap());
    registry.register(new AnnotationElementFinder())
        .to(assignableTo(Annotation.class));
    registry.register(new PropertiesElementFinder())
        .to(assignableTo(Properties.class));
    registry.register(new ResourceBundleElementFinder())
        .to(assignableTo(ResourceBundle.class));
    registry.register(new ResultSetElementFinder())
        .to(assignableTo(ResultSet.class));
    registry.register(new MapElementFinder())
        .to(assignableTo(Map.class).and(assignableTo(Properties.class).negate()));
    registry.register(new ArrayElementFinder())
        .to(arrayType());
    registry.register(new ListElementFinder())
        .to(assignableTo(List.class));
    finder = new TruggerElementFinder(new ObjectElementFinder(), registry);
  }

  public Registry<Predicate<Class>, ElementFinder> registry() {
    return registry;
  }

  /**
   * Returns a new {@link TruggerElementSelector}.
   */
  public ElementSelector createElementSelector(String name) {
    return new TruggerElementSelector(name, finder);
  }

  /**
   * Returns a new {@link TruggerElementsSelector}.
   */
  public ElementsSelector createElementsSelector() {
    return new TruggerElementsSelector(finder);
  }

  @Override
  public ElementCopier createElementCopier() {
    return new TruggerElementCopier();
  }

  @Override
  public ElementCopier createElementCopier(ElementsSelector selector) {
    return new TruggerElementCopier(selector);
  }

}

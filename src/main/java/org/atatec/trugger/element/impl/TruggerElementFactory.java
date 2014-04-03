/*
 * Copyright 2009-2014 Marcelo Guimarães
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.atatec.trugger.element.impl;

import org.atatec.trugger.Finder;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.ElementCopier;
import org.atatec.trugger.element.ElementFactory;
import org.atatec.trugger.registry.MapRegistry;
import org.atatec.trugger.registry.Registry;
import org.atatec.trugger.selector.ElementSelector;
import org.atatec.trugger.selector.ElementsSelector;

import java.lang.annotation.Annotation;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import static org.atatec.trugger.reflection.ClassPredicates.arrayType;
import static org.atatec.trugger.reflection.ClassPredicates.assignableTo;

/**
 * A default implementation for ElementFactory.
 *
 * @author Marcelo Guimarães
 */
public final class TruggerElementFactory implements ElementFactory {

	private Finder<Element> finder;
	private final MapRegistry<Predicate<Class>, Finder<Element>> registry;

	public TruggerElementFactory() {
	  registry = new MapRegistry<Predicate<Class>, Finder<Element>>(new LinkedHashMap());
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

    finder = new TruggerElementFinder(new ObjectElementFinder(), registry);
	}

	public Registry<Predicate<Class>, Finder<Element>> registry() {
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

/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.element.impl;

import java.lang.annotation.Annotation;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import net.sf.trugger.Finder;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.ElementCopier;
import net.sf.trugger.element.ElementFactory;
import net.sf.trugger.registry.MapRegistry;
import net.sf.trugger.registry.Registry;
import net.sf.trugger.selector.ElementSelector;
import net.sf.trugger.selector.ElementsSelector;

/**
 * A default implementation for ElementFactory.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public final class TruggerElementFactory implements ElementFactory {

	private Finder<Element> finder;
	private final MapRegistry<Class<?>, Finder<Element>> registry;

	public TruggerElementFactory() {
	  registry = new MapRegistry<Class<?>, Finder<Element>>(new LinkedHashMap());
    registry.register(new AnnotationElementFinder()).to(Annotation.class);
    registry.register(new PropertiesElementFinder()).to(Properties.class);
    registry.register(new ResourceBundleElementFinder()).to(ResourceBundle.class);
    registry.register(new ResultSetElementFinder()).to(ResultSet.class);
    registry.register(new MapElementFinder()).to(Map.class);

    finder = new TruggerElementFinder(new DefaultElementFinder(), registry);
	}

	public Registry<Class<?>, Finder<Element>> registry() {
	  return registry;
	}

  /**
   * Returns a new {@link TruggerElementSelector}.
   */
  public ElementSelector createElementSelector(String name) {
    return new TruggerElementSelector(name, finder);
  }

  public ElementSelector createElementSelector() {
    return new TruggerNoNamedElementSelector(finder);
  }

  /**
   * Returns a new {@link TruggerElementsSelector}.
   */
  public ElementsSelector createElementsSelector() {
    return new TruggerElementsSelector(finder);
  }

  /**
   * Returns a new {@link TruggerElementCopier}.
   */
  public ElementCopier createElementCopier(Object src) {
    return new TruggerElementCopier(src);
  }

}

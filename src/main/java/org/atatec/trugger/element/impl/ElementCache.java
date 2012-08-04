/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
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

import org.atatec.trugger.element.Element;
import org.atatec.trugger.util.Utils;

import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Marcelo Varella Barca Guimarães
 * @since 4.0
 */
public abstract class ElementCache {

  private Map<Class, SoftReference<Map<String, Element>>> map;

  public ElementCache() {
    this.map = new ConcurrentHashMap<Class, SoftReference<Map<String, Element>>>(50);
  }

  private Map<String, Element> getMap(Object target) {
    Class type = Utils.resolveType(target);
    SoftReference<Map<String, Element>> reference = map.get(type);
    if (reference == null || reference.get() == null) {
      Map<String, Element> elementMap = new HashMap<String, Element>(20);
      reference = new SoftReference<Map<String, Element>>(elementMap);
      map.put(type, reference);
      loadElements(type, elementMap);
    }
    return reference.get();
  }

  public Collection get(Object target) {
    return getMap(target).values();
  }

  public Element get(Object target, String elementName) {
    return getMap(target).get(elementName);
  }

  protected abstract void loadElements(Class type, Map<String, Element> map);

}

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

import org.atatec.trugger.element.Element;
import org.atatec.trugger.util.Utils;

import java.lang.ref.SoftReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Marcelo Guimarães
 * @since 4.0
 */
public abstract class ClassElementsCache {

  private SoftReference<Map<Class, Map<String, Element>>> ref;

  public ClassElementsCache() {
    this.ref = new SoftReference<>(new ConcurrentHashMap<>(50));
  }

  private Map<String, Element> getMap(Object target) {
    Class type = Utils.resolveType(target);
    Map<Class, Map<String, Element>> map = ref.get();
    if (map == null) {
      map = new ConcurrentHashMap<>(50);
      this.ref = new SoftReference<>(map);
    }
    Map<String, Element> elementMap = map.get(type);
    if (elementMap == null) {
      elementMap = new HashMap<>(20);
      loadElements(type, elementMap);
      map.put(type, elementMap);
    }
    return elementMap;
  }

  public Collection get(Object target) {
    return getMap(target).values();
  }

  public Element get(Object target, String elementName) {
    return getMap(target).get(elementName);
  }

  protected abstract void loadElements(Class type, Map<String, Element> map);

}

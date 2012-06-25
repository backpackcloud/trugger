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
package org.atatec.trugger.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * A class that groups various {@link ResourceBundle} objects into a single one.
 * <p>
 * The bundles are used in a stack order, the last bundle merged will be the
 * first one to be used.
 * <p>
 * For {@link ResourceBundle#getLocale() locale}, the locale of the last added
 * bundle is always returned.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class MultiResourceBundle extends ResourceBundle {

  private List<ResourceBundle> bundles;

  private Set<String> keys = new HashSet<String>();

  /**
   * Creates a new MultiResourceBundle with the given bundle.
   */
  private MultiResourceBundle(ResourceBundle bundle) {
    this.bundles = new LinkedList<ResourceBundle>();
    merge(bundle);
  }

  /**
   * Merges the given bundles with this one.
   *
   * @param bundles
   *          the bundles to merge.
   * @return a reference to this object.
   */
  public MultiResourceBundle merge(ResourceBundle... bundles) {
    for (ResourceBundle resourceBundle : bundles) {
      this.keys.addAll(resourceBundle.keySet());
      this.bundles.add(0, resourceBundle);
    }
    return this;
  }

  @Override
  public Enumeration<String> getKeys() {
    return Collections.enumeration(keys);
  }

  @Override
  public Set<String> keySet() {
    return new HashSet<String>(keys);
  }

  @Override
  public boolean containsKey(String key) {
    for (ResourceBundle resourceBundle : bundles) {
      if (resourceBundle.containsKey(key)) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected Object handleGetObject(String key) {
    for (ResourceBundle resourceBundle : bundles) {
      if (resourceBundle.containsKey(key)) {
        return resourceBundle.getObject(key);
      }
    }
    return null;
  }

  @Override
  public Locale getLocale() {
    return bundles.get(0).getLocale();
  }

  /**
   * Wraps the given bundles into a MultiResourceBundle.
   * <p>
   * The {@link #merge(ResourceBundle...)} order will be the same given order.
   */
  public static MultiResourceBundle wrap(ResourceBundle rootBundle) {
    return new MultiResourceBundle(rootBundle);
  }
}

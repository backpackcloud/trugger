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
package org.atatec.trugger.loader.impl;

import java.util.Set;

import org.atatec.trugger.CreateException;
import org.atatec.trugger.registry.MapRegistry;
import org.atatec.trugger.registry.Registry;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public final class TruggerRegistry implements Registry<Class<?>, Object> {

  private final Registry<Class<?>, Object> decorated;

  public TruggerRegistry() {
    this(new MapRegistry<Class<?>, Object>());
  }

  public TruggerRegistry(Registry<Class<?>, Object> decorated) {
    this.decorated = decorated;
  }

  public RegistryMapper<Class<?>, Object> register(Object value) {
    return decorated.register(value);
  }

  public boolean hasRegistryFor(Class<?> key) {
    return decorated.hasRegistryFor(key);
  }

  public Object removeRegistryFor(Class<?> key) {
    return decorated.removeRegistryFor(key);
  }

  public synchronized Object registryFor(Class<?> key) {
    if(!decorated.hasRegistryFor(key)) {
      try {
        Class<?> className = Class.forName(getDefaultImplementationName(key));
        decorated.register(className.newInstance()).to(key);
      } catch (Exception e) {
        throw new CreateException(e);
      }
    }
    return decorated.registryFor(key);
  }

  public Set<Entry<Class<?>, Object>> entries() {
    return decorated.entries();
  }

  private static String getDefaultImplementationName(Class<?> type) {
    return type.getPackage().getName() + ".impl.Trugger" + type.getSimpleName();
  }
}

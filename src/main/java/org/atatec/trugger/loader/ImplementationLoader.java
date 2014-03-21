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
package org.atatec.trugger.loader;

import org.atatec.trugger.TruggerException;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * A class used for loading implementations for the DSLs exposed by this framework.
 *
 * Since this class uses a {@link ServiceLoader} for custom loading, you can define
 * your own implementations by using a file in the META-INF/services directory. The
 * implementations used are:
 *
 * <ul>
 * <li>org.atatec.trugger.element.ElementFactory</li>
 * <li>org.atatec.trugger.property.PropertyFactory</li>
 * <li>org.atatec.trugger.reflection.ReflectionFactory</li>
 * <li>org.atatec.trugger.scan.ClassScannerFactory</li>
 * </ul>
 *
 * @author Marcelo Guimarães
 */
public class ImplementationLoader {

  private ImplementationLoader() {
  }

  /**
   * Returns the implementation for a given class.
   *
   * @return the implementation for the given class.
   */
  public static <E> E get(Class<E> type) {
    ServiceLoader<E> loader = ServiceLoader.load(type);
    Iterator<E> iterator = loader.iterator();
    if (iterator.hasNext()) {
      return iterator.next(); // only one is expected
    }
    try {
      //loads the default implementation
      return (E) Class.forName(getDefaultImplementationName(type)).newInstance();
    } catch (Exception e) {
      throw new TruggerException(e);
    }
  }

  private static String getDefaultImplementationName(Class<?> type) {
    return type.getPackage().getName() + ".impl.Trugger" + type.getSimpleName();
  }

}

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
package net.sf.trugger.bind;

import net.sf.trugger.Resolver;
import net.sf.trugger.element.Element;

/**
 * Interface that defines a class capable of binding values on target objects.
 * <p>
 * The intent of this interface is <strong>not</strong> to be an IoC container,
 * is to be a component for seamless bind objects into another one (a
 * lower-level in comparison with an IoC container).
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 1.2
 */
public interface Binder {

  /**
   * Specifies a constant value for bind.
   *
   * @param value
   *          the value to bind.
   * @return the component used for specifying the bind type.
   */
  BindSelector bind(Object value);

  /**
   * Specifies a resolver that will decide the proper value for bind.
   *
   * @param resolver
   *          the resolver for the bind.
   * @return the component used for specifying the bind type.
   */
  BindSelector bind(Resolver<Object, Element> resolver);

  /**
   * Applies the bindings in the given object.
   * <p>
   * After that, every method annotated with {@link PostBind} and with no
   * parameters will be called.
   *
   * @return a reference to the given object.
   */
  <E> E applyBinds(E object);

}

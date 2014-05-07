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

package org.atatec.trugger.util.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

import static org.atatec.trugger.reflection.Reflection.invoke;
import static org.atatec.trugger.reflection.Reflection.reflect;
import static org.atatec.trugger.reflection.ReflectionPredicates.declaring;

/**
 * A class that can create objects based on a {@link Context}.
 *
 * @since 5.0
 */
public class ContextFactory {

  private final Context context;
  private BiFunction<Constructor, Object[], Object> createFunction;

  /**
   * Creates a new instance using a default context implementation
   */
  public ContextFactory() {
    this(new DefaultContext());
  }

  /**
   * Creates a new instance using the given context implementation
   *
   * @param context the context to use
   */
  public ContextFactory(Context context) {
    this.context = context;
    toCreate(defaults());
  }

  /**
   * Returns the context used by this factory.
   *
   * @return the context used by this factory.
   */
  public Context context() {
    return context;
  }

  /**
   * Changes the way the objects are created by using the given function.
   *
   * @param function the function to use for creating the objects.
   * @return a reference to this object
   */
  public ContextFactory toCreate(
      BiFunction<Constructor, Object[], Object> function) {
    this.createFunction = function;
    return this;
  }

  /**
   * Creates a new instance of the given type by looping through its public
   * constructors to find one which all parameters are resolved by the context.
   *
   * @param type the type of the object to create.
   * @return the created object
   * @throws CreateException if the object cannot be created
   */
  public <E> E create(Class<E> type) {
    List<Constructor<?>> constructors = reflect().constructors()
        .filter(declaring(Modifier.PUBLIC))
        .in(type);
    Collections.sort(constructors,
        (c1, c2) -> c2.getParameterCount() - c1.getParameterCount());
    E result;
    for (Constructor<?> constructor : constructors) {
      result = tryCreate(constructor);
      if (result != null) {
        return result;
      }
    }
    throw new CreateException("Unable to create a " + type);
  }

  // tries to create the object using the given constructor
  private <E> E tryCreate(Constructor<?> constructor) {
    Object[] args = new Object[constructor.getParameterCount()];
    Object arg;
    int i = 0;
    for (Parameter parameter : constructor.getParameters()) {
      try {
        arg = context.resolve(parameter);
        args[i++] = arg;
      } catch (UnresolvableValueException e) {
        return null;
      }
    }
    return (E) createFunction.apply(constructor, args);
  }

  /**
   * The default function to create objects. This function basically invokes
   * the constructor with the given arguments.
   *
   * @return the default function used to create objects.
   */
  public static BiFunction<Constructor, Object[], Object> defaults() {
    return (constructor, args) -> invoke(constructor).withArgs(args);
  }

}

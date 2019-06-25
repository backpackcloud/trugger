/*
 * The Apache License
 *
 * Copyright 2009 Marcelo Guimaraes <ataxexe@backpackcloud.com>
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

package io.backpackcloud.trugger.util.factory;

import io.backpackcloud.trugger.reflection.ReflectedConstructor;
import io.backpackcloud.trugger.reflection.Reflection;
import io.backpackcloud.trugger.reflection.ReflectionPredicates;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * A class that can create objects based on a {@link Context}.
 *
 * @since 5.0
 */
public class ContextFactory {

  private final Context context;
  private final BiFunction<Constructor, Object[], Object> createFunction;

  /**
   * Creates a new instance using a default context implementation
   */
  public ContextFactory() {
    this(new DefaultContext());
  }

  /**
   * Creates a new instance using the given context implementation. The function to
   * instantiate the objects will be the default one, which is just call the constructor.
   *
   * @param context the context to use
   */
  public ContextFactory(Context context) {
    this.context = context;
    this.createFunction = (constructor, args) -> Reflection.invoke(constructor).withArgs(args);
  }

  /**
   * Creates a new instance using the given context implementation plus the function to
   * instantiate the objects.
   *
   * @param context        the context to use
   * @param createFunction the function to create the objects
   */
  public ContextFactory(Context context, BiFunction<Constructor, Object[], Object> createFunction) {
    this.context = context;
    this.createFunction = createFunction;
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
   * @return a new ContextFactory that uses the given function and this context
   */
  public ContextFactory toCreate(BiFunction<Constructor, Object[], Object> function) {
    return new ContextFactory(this.context, function);
  }

  /**
   * Creates a new instance of the given type by looping through its public
   * constructors to find one which all parameters are resolved by the context.
   *
   * @param type the type of the object to create.
   * @return the created object
   */
  public <E> Optional<E> create(Class<E> type) {
    List<ReflectedConstructor> constructors = Reflection.reflect().constructors()
        .filter(ReflectionPredicates.declared(Modifier.PUBLIC))
        .from(type);
    Optional<E> created;
    for (ReflectedConstructor constructor : constructors) {
      created = tryCreate(constructor);
      if (created.isPresent()) {
        return created;
      }
    }
    return Optional.empty();
  }

  // tries to create the object using the given constructor
  private Optional tryCreate(ReflectedConstructor constructor) {
    Object[] args = new Object[constructor.getParameterCount()];
    Object arg;
    Optional<Object> resolved;
    int i = 0;
    for (Parameter parameter : constructor.getParameters()) {
      resolved = context.resolve(parameter);
      if (!resolved.isPresent()) {
        return Optional.empty();
      }
      arg = resolved.get();
      args[i++] = arg;
    }
    return Optional.of(createFunction.apply(constructor.actualConstructor(), args));
  }

}

/*
 * The Apache License
 *
 * Copyright 2009 Marcelo "Ataxexe" Guimarães <ataxexe@devnull.tools>
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

package tools.devnull.trugger.util.factory;

import tools.devnull.trugger.Optional;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.function.BiFunction;

import static tools.devnull.trugger.reflection.Reflection.invoke;
import static tools.devnull.trugger.reflection.Reflection.reflect;
import static tools.devnull.trugger.reflection.ReflectionPredicates.declaring;

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
    this.createFunction = (constructor, args) -> invoke(constructor).withArgs(args);
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
    List<Constructor<?>> constructors = reflect().constructors()
        .filter(declaring(Modifier.PUBLIC))
        .from(type);
    for (Constructor<?> constructor : constructors) {
      try {
        return tryCreate(constructor);
      } catch (UnresolvableValueException e) {
        continue;
      }
    }
    return Optional.empty();
  }

  // tries to create the object using the given constructor
  private Optional tryCreate(Constructor<?> constructor) {
    Object[] args = new Object[constructor.getParameterCount()];
    Object arg;
    int i = 0;
    for (Parameter parameter : constructor.getParameters()) {
      arg = context.resolve(parameter)
          .orElseThrow(UnresolvableValueException::new);
      args[i++] = arg;
    }
    return Optional.of(createFunction.apply(constructor, args));
  }

}

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
package tools.devnull.trugger.reflection;

import tools.devnull.trugger.Result;
import tools.devnull.trugger.selector.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Interface that defines a class that encapsulates the reflection using an automata to
 * create a more readable language. That makes more simple to get fields, methods and
 * constructors from that object.
 * <p>
 * There are a set of methods that can find specific reflection objects ( {@link Field
 * fields}, {@link Constructor constructors} or {@link Method methods}). These methods
 * returns components that allows filtering by using common methods and, if no object is
 * found, a <code>null</code> value is <strong>always</strong> returned in a case of a
 * <i>single object selection</i> (for a <i>set</i> of them, an <strong>empty
 * collection</strong> is returned).
 *
 * @author Marcelo Guimarães
 */
public interface Reflector {

  /**
   * Indicates to reflect only visible elements (declared as "public" in all hierarchy).
   * This is useful if you want to use reflection when access to non public fields is
   * forbidden.
   *
   * @return a new reflector
   * @since 4.1
   */
  Reflector visible();

  /**
   * Indicates to reflect a declared element (besides its access modifiers).
   *
   * @return a new reflector
   * @since 4.1
   */
  Reflector declared();

  /**
   * Reflects all fields in a target.
   * <p>
   * Use this method for selecting a set of fields.
   *
   * @return the component used for selection.
   */
  FieldsSelector fields();

  /**
   * Reflects a field with the specified name in a target.
   * <p>
   * Use this method for selecting a single field.
   *
   * @param name the field name.
   * @return the component used for selection.
   */
  FieldSelector field(String name);

  /**
   * Reflects all methods in a target.
   * <p>
   * Use this method for selecting a set of methods.
   *
   * @return the component used for selection.
   */
  MethodsSelector methods();

  /**
   * Reflects a method with the specified name and parameters in a target.
   * <p>
   * Use this method for selecting a single method.
   * <p>
   * <i>The method parameters in question must be informed in the returned object.</i>
   *
   * @param name the method name.
   * @return the component used for selection.
   */
  MethodSelector method(String name);

  /**
   * Reflects a constructor with the specified parameters in a target.
   * <p>
   * Use this method for selecting a single constructor.
   * <p>
   * <i>The constructor parameters in question must be informed in the returned
   * object.</i>
   *
   * @return the component used for selection.
   */
  ConstructorSelector constructor();

  /**
   * Reflects all the constructors.
   * <p>
   * Use this method for selecting a set of constructors.
   *
   * @return the component used for selection.
   */
  ConstructorsSelector constructors();

  /**
   * Reflects all interfaces that a target implements.
   * <p>
   * This method returns the interfaces found in every class of the target hierarchy.
   * <i>For a set of the interfaces implemented only by the target in question, use the
   * {@link Class#getInterfaces()} method.</i>
   *
   * @return the component used for selection.
   */
  Result<List<Class>, Object> interfaces();

  /**
   * Reflects the generic type parameter declared in a target.
   * <p>
   * Example:
   * <p>
   * Based on the following classes.
   * <p>
   * <pre>
   * public class MyClass&lt;E&gt; {
   *   //... fields and methods
   * }
   *
   * public class MyExtendedClass&lt;MyType&gt; {
   *   //... fields and methods
   * }
   * </pre>
   * The code bellow will print <code>MyType</code>:
   * <p>
   * <pre>
   * Class&lt;?&gt; genericType = {@link Reflection#reflect()}.genericType(&quot;E&quot;).in(MyExtendedClass.class);
   * System.out.print(genericType.getSimpleName());
   * </pre>
   *
   * @param parameterName the generic parameter name.
   * @return the component used for selecting the target.
   */
  Result<Class, Object> genericType(String parameterName);

  /**
   * Reflects the generic type parameter declared in a target.
   * <p>
   * This method should be used only if the target has only one generic parameter.
   *
   * @return the component used for selecting the target.
   * @see Reflector#genericType(String)
   */
  Result<Class, Object> genericType();

  /**
   * Reflects the bridged method of a given {@link Method#isBridge() bridge} method.
   * <p>
   * If the given method is not a bridge, then it should be returned.
   *
   * @param bridgeMethod the bridge method.
   * @return the original method.
   */
  Method bridgedMethodFor(Method bridgeMethod);

}

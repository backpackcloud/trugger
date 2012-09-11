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
package org.atatec.trugger.reflection;

import org.atatec.trugger.Result;
import org.atatec.trugger.predicate.CompositePredicate;
import org.atatec.trugger.predicate.Predicates;
import org.atatec.trugger.selector.AnnotatedElementSelector;
import org.atatec.trugger.selector.ConstructorSelector;
import org.atatec.trugger.selector.ConstructorsSelector;
import org.atatec.trugger.selector.FieldGetterMethodSelector;
import org.atatec.trugger.selector.FieldSelector;
import org.atatec.trugger.selector.FieldSetterMethodSelector;
import org.atatec.trugger.selector.FieldsSelector;
import org.atatec.trugger.selector.GetterMethodSelector;
import org.atatec.trugger.selector.MemberSelector;
import org.atatec.trugger.selector.MethodSelector;
import org.atatec.trugger.selector.MethodsSelector;
import org.atatec.trugger.selector.SetterMethodSelector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Interface that defines a class that encapsulates the reflection using an automata to
 * create a more readable language. That makes more simple to get fields, methods and
 * constructors from anyThat object.
 * <p/>
 * There are a set of methods that can find specific reflection objects ( {@link Field
 * fields}, {@link Constructor constructors} or {@link Method methods}). These methods
 * returns components that allows filtering by using common methods and, if no object is
 * found, a <code>null</code> value is <strong>always</strong> returned in a case of a
 * <i>single object selection</i> (for a <i>set</i> of them, an <strong>empty
 * collection</strong> is returned).
 * <p/>
 * Examples: <ul> <li>For selecting elements annotated with some annotations, you can use
 * the {@link AnnotatedElementSelector#annotatedWith(Class) annotatedWith} method.
 * <p/>
 * <pre>
 * Field field = {@link Reflection#reflect()}.field(&quot;myField&quot;).annotatedWith(MyAnnotation.class).in(MyClass.class);
 * </pre>
 * <li>For selecting elements with a particular access, you can use the {@link
 * MemberSelector#withAccess(Access) withAccess} method.
 * <p/>
 * <pre>
 * Set&lt;Constructor&lt;?&gt;&gt; constructors = {@link Reflection#reflect()}.allConstructors().withAccess(Access.PUBLIC).in(MyClass.class);
 * </pre>
 * <li>You can also combine the selectors, making an easily readable code:
 * <p/>
 * <pre>
 * Set&lt;Method&gt; methods = {@link Reflection#reflect()}.methods().annotatedWith(MyAnnotation.class).nonStatic().in(someInstance);
 * </pre>
 * <li>If the selectors provided does not fit in your necessity, you can use a {@link
 * CompositePredicate} and pass it to the {@link MemberSelector#that(org.atatec.trugger.predicate.Predicate)
 * that} method.
 * <p/>
 * <pre>
 * import static {@link org.atatec.trugger.reflection.ReflectionPredicates}.*;
 *
 * Set&lt;Field&gt; fields = {@link Reflection#reflect()}.fields().ofType(String.class).thatMathces(PUBLIC.or(PROTECTED)).in(MyClass.class);
 * </pre>
 * <li>You can also bypass the filters by invoking the {@link Result#in(Object) in} method
 * directly after calling the method in question.
 * <p/>
 * <pre>
 * Method method = {@link Reflection#reflect()}.isGetterOf(&quot;myProperty&quot;).in(MyClass.class);
 * Set&lt;Field&gt; fields = {@link Reflection#reflect()}.allFields().in(MyClass.class);
 * </pre>
 * </ul>
 *
 * @author Marcelo Varella Barca Guimarães
 * @see Predicates#is(org.atatec.trugger.predicate.Predicate)
 */
public interface Reflector {

  /**
   * Indicates to reflect only visible elements (declared as "public"). This is useful if
   * you want to use reflection when access to non public fields are forbidden.
   *
   * @return a reference to this object
   *
   * @since 4.1
   */
  Reflector visible();

  /**
   * Indicates to reflect a declared element (besides its access modifiers).
   *
   * @return a reference to this object
   *
   * @since 4.1
   */
  Reflector declared();

  /**
   * Reflects all fields in a target.
   * <p/>
   * Use this method for selecting a set of fields.
   *
   * @return the component used for selection.
   */
  FieldsSelector fields();

  /**
   * Reflects a field with the specified name in a target.
   * <p/>
   * Use this method for selecting a single field.
   *
   * @param name the field name.
   *
   * @return the component used for selection.
   */
  FieldSelector field(String name);

  /**
   * Reflects a field based on the further selection.
   * <p/>
   * This method should be used only if the selection matches a single field.
   *
   * @return the component used for selection.
   *
   * @since 2.3
   */
  FieldSelector field();

  /**
   * Reflects all methods in a target.
   * <p/>
   * Use this method for selecting a set of methods.
   *
   * @return the component used for selection.
   */
  MethodsSelector methods();

  /**
   * Reflects a method with the specified name and parameters in a target.
   * <p/>
   * Use this method for selecting a single method.
   * <p/>
   * <i>The method parameters in question must be informed in the returned object.</i>
   *
   * @param name the method name.
   *
   * @return the component used for selection.
   */
  MethodSelector method(String name);

  /**
   * Reflects a method based on the further selection.
   * <p/>
   * This method should be used only if the selection matches a single method.
   *
   * @return the component used for selection.
   *
   * @since 2.3
   */
  MethodSelector method();

  /**
   * Reflects a getter method for a specified property.
   *
   * @param name the property name
   *
   * @return the component used for selection.
   *
   * @since 1.2
   */
  GetterMethodSelector getterFor(String name);

  /**
   * Reflects a getter for the given field based on its name and type.
   *
   * @param field the reference field.
   *
   * @return the supposed getter for the field.
   *
   * @since 1.2
   */
  FieldGetterMethodSelector getterFor(Field field);

  /**
   * Reflects a setter method for a specified property.
   *
   * @param name the property name
   *
   * @return the component used for selection.
   *
   * @since 1.2
   */
  SetterMethodSelector setterFor(String name);

  /**
   * Reflects a setter for the given field based on its name and type.
   *
   * @param field the reference field.
   *
   * @return the supposed setter for the field.
   *
   * @since 1.2
   */
  FieldSetterMethodSelector setterFor(Field field);

  /**
   * Reflects a constructor with the specified parameters in a target.
   * <p/>
   * Use this method for selecting a single constructor.
   * <p/>
   * <i>The constructor parameters in question must be informed in the returned
   * object.</i>
   *
   * @return the component used for selection.
   */
  ConstructorSelector constructor();

  /**
   * Reflects all the constructors.
   * <p/>
   * Use this method for selecting a set of constructors.
   *
   * @return the component used for selection.
   */
  ConstructorsSelector constructors();

  /**
   * Reflects all interfaces that a target implements.
   * <p/>
   * This method returns the interfaces found in every class of the target hierarchy.
   * <i>For a set of the interfaces implemented only by the target in question, use the
   * {@link Class#getInterfaces()} method.</i>
   *
   * @return the component used for selection.
   */
  Result<Set<Class<?>>, Object> interfaces();

  /**
   * Reflects the generic type parameter declared in a target.
   * <p/>
   * Example:
   * <p/>
   * Based on the following classes.
   * <p/>
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
   * <p/>
   * <pre>
   * Class&lt;?&gt; genericType = {@link Reflection#reflect()}.genericType(&quot;E&quot;).in(MyExtendedClass.class);
   * System.out.print(genericType.getSimpleName());
   * </pre>
   *
   * @param parameterName the generic parameter name.
   *
   * @return the component used for selecting the target.
   */
  Result<Class, Object> genericType(String parameterName);

  /**
   * Reflects the generic type parameter declared in a target.
   * <p/>
   * This method should be used only if the target has only one generic parameter.
   *
   * @return the component used for selecting the target.
   *
   * @see Reflector#genericType(String)
   */
  Result<Class, Object> genericType();

  /**
   * Reflects the bridged method of a given {@link Method#isBridge() bridge} method.
   * <p/>
   * If the given method is not a bridge, then it should be returned.
   *
   * @param bridgeMethod the bridge method.
   *
   * @return the original method.
   */
  Method bridgedMethodFor(Method bridgeMethod);

}

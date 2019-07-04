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

package io.backpackcloud.trugger.reflection;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * Interface that defines a method reflected with the use of this framework.
 *
 * @since 7.0
 */
public interface ReflectedMethod extends ReflectedObject<Method> {

  /**
   * Invokes the reflected method passing the given args.
   *
   * @param args the args to pass
   * @return the result of the method call
   */
  <E> E invoke(Object... args);

  /**
   * @see Method#getTypeParameters()
   */
  TypeVariable<Method>[] getTypeParameters();

  /**
   * @see Method#getReturnType()
   */
  Class<?> getReturnType();

  /**
   * @see Method#getGenericReturnType()
   */
  Type getGenericReturnType();

  /**
   * @see Method#getParameterTypes()
   */
  Class<?>[] getParameterTypes();

  /**
   * @see Method#getParameterCount()
   */
  int getParameterCount();

  /**
   * @see Method#getGenericParameterTypes()
   */
  Type[] getGenericParameterTypes();

  /**
   * @see Method#getExceptionTypes()
   */
  Class<?>[] getExceptionTypes();

  /**
   * @see Method#getGenericExceptionTypes()
   */
  Type[] getGenericExceptionTypes();

  /**
   * @see Method#isBridge()
   */
  boolean isBridge();

  /**
   * @see Method#isVarArgs()
   */
  boolean isVarArgs();

  /**
   * @see Method#isDefault()
   */
  boolean isDefault();

  /**
   * @see Method#getDefaultValue()
   */
  Object getDefaultValue();

  /**
   * @see Method#getParameters()
   */
  Parameter[] getParameters();

  /**
   * @see Method#getAnnotatedReceiverType()
   */
  AnnotatedType getAnnotatedReceiverType();

  /**
   * @see Method#getAnnotatedParameterTypes()
   */
  AnnotatedType[] getAnnotatedParameterTypes();

}

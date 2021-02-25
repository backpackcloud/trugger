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

package com.backpackcloud.trugger.reflection;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * Interface that defines a constructor reflected with the use of this framework.
 *
 * @since 7.0
 */
public interface ReflectedConstructor extends ReflectedObject<Constructor<?>> {

  /**
   * Invokes the constructor passing the given arguments.
   *
   * @param args the arguments to pass to the constructor
   * @return the created instance
   * @see Reflection#invoke(Constructor)
   */
  <E> E invoke(Object... args);

  /**
   * @see Constructor#getTypeParameters()
   */
  TypeVariable<? extends Constructor<?>>[] getTypeParameters();

  /**
   * @see Constructor#getParameterTypes()
   */
  Class<?>[] getParameterTypes();

  /**
   * @see Constructor#getParameterCount()
   */
  int getParameterCount();

  /**
   * @see Constructor#getGenericParameterTypes()
   */
  Type[] getGenericParameterTypes();

  /**
   * @see Constructor#getExceptionTypes()
   */
  Class<?>[] getExceptionTypes();

  /**
   * @see Constructor#getGenericExceptionTypes()
   */
  Type[] getGenericExceptionTypes();

  /**
   * @see Constructor#newInstance(Object...)
   */
  Object newInstance(Object... initargs) throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException;

  /**
   * @see Constructor#isVarArgs()
   */
  boolean isVarArgs();

  /**
   * @see Constructor#getParameters()
   */
  Parameter[] getParameters();

  /**
   * @see Constructor#getAnnotatedParameterTypes()
   */
  AnnotatedType[] getAnnotatedParameterTypes();

  /**
   * @see Constructor#getAnnotatedExceptionTypes()
   */
  AnnotatedType[] getAnnotatedExceptionTypes();
}

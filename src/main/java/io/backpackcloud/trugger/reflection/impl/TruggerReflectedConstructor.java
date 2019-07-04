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

package io.backpackcloud.trugger.reflection.impl;

import io.backpackcloud.trugger.reflection.ReflectedConstructor;
import io.backpackcloud.trugger.reflection.Reflection;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * A class that holds a reflected constructor
 *
 * @since 7.0
 */
public class TruggerReflectedConstructor extends TruggerReflectedObject<Constructor<?>> implements ReflectedConstructor {

  private final Constructor<?> constructor;

  /**
   * Creates a new instance of this class
   *
   * @param constructor the reflected constructor
   */
  public TruggerReflectedConstructor(Constructor<?> constructor) {
    super(constructor);
    this.constructor = constructor;
  }

  @Override
  public Object target() {
    return constructor.getDeclaringClass();
  }

  @Override
  public Constructor<?> unwrap() {
    return this.constructor;
  }

  @Override
  public <E> E invoke(Object... args) {
    return Reflection.invoke(constructor).withArgs(args);
  }

  // Delegated methods

  @Override
  public TypeVariable<? extends Constructor<?>>[] getTypeParameters() {
    return constructor.getTypeParameters();
  }

  @Override
  public Class<?>[] getParameterTypes() {
    return constructor.getParameterTypes();
  }

  @Override
  public int getParameterCount() {
    return constructor.getParameterCount();
  }

  @Override
  public Type[] getGenericParameterTypes() {
    return constructor.getGenericParameterTypes();
  }

  @Override
  public Class<?>[] getExceptionTypes() {
    return constructor.getExceptionTypes();
  }

  @Override
  public Type[] getGenericExceptionTypes() {
    return constructor.getGenericExceptionTypes();
  }

  @Override
  public Object newInstance(Object... initargs) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    return constructor.newInstance(initargs);
  }

  @Override
  public boolean isVarArgs() {
    return constructor.isVarArgs();
  }

  @Override
  public Parameter[] getParameters() {
    return constructor.getParameters();
  }

  @Override
  public AnnotatedType[] getAnnotatedParameterTypes() {
    return constructor.getAnnotatedParameterTypes();
  }

  @Override
  public AnnotatedType[] getAnnotatedExceptionTypes() {
    return constructor.getAnnotatedExceptionTypes();
  }

}

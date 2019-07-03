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
public class ReflectedConstructor extends ReflectedObject {

  private final Constructor<?> constructor;

  /**
   * Creates a new instance of this class
   *
   * @param constructor the reflected constructor
   */
  public ReflectedConstructor(Constructor<?> constructor) {
    super(constructor);
    this.constructor = constructor;
  }

  /**
   * Returns the enclosed constructor
   *
   * @return the enclosed constructor
   */
  public Constructor<?> actualConstructor() {
    return this.constructor;
  }

  /**
   * Invokes the constructor passing the given arguments.
   *
   * @param args the arguments to pass to the constructor
   * @return the created instance
   * @see Reflection#invoke(Constructor)
   */
  public <E> E invoke(Object... args) {
    return Reflection.invoke(constructor).withArgs(args);
  }

  // Delegated method

  /**
   * @see Constructor#getTypeParameters()
   */
  public TypeVariable<? extends Constructor<?>>[] getTypeParameters() {
    return constructor.getTypeParameters();
  }

  /**
   * @see Constructor#getParameterTypes()
   */
  public Class<?>[] getParameterTypes() {
    return constructor.getParameterTypes();
  }

  /**
   * @see Constructor#getParameterCount()
   */
  public int getParameterCount() {
    return constructor.getParameterCount();
  }

  /**
   * @see Constructor#getGenericParameterTypes()
   */
  public Type[] getGenericParameterTypes() {
    return constructor.getGenericParameterTypes();
  }

  /**
   * @see Constructor#getExceptionTypes()
   */
  public Class<?>[] getExceptionTypes() {
    return constructor.getExceptionTypes();
  }

  /**
   * @see Constructor#getGenericExceptionTypes()
   */
  public Type[] getGenericExceptionTypes() {
    return constructor.getGenericExceptionTypes();
  }

  /**
   * @see Constructor#newInstance(Object...)
   */
  public Object newInstance(Object... initargs) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    return constructor.newInstance(initargs);
  }

  /**
   * @see Constructor#isVarArgs()
   */
  public boolean isVarArgs() {
    return constructor.isVarArgs();
  }

  /**
   * @see Constructor#getParameters()
   */
  public Parameter[] getParameters() {
    return constructor.getParameters();
  }

  /**
   * @see Constructor#getAnnotatedParameterTypes()
   */
  public AnnotatedType[] getAnnotatedParameterTypes() {
    return constructor.getAnnotatedParameterTypes();
  }

  /**
   * @see Constructor#getAnnotatedExceptionTypes()
   */
  public AnnotatedType[] getAnnotatedExceptionTypes() {
    return constructor.getAnnotatedExceptionTypes();
  }

}

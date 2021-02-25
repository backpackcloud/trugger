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

package com.backpackcloud.trugger.reflection.impl;

import com.backpackcloud.trugger.reflection.ReflectedMethod;
import com.backpackcloud.trugger.reflection.Reflection;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class TruggerReflectedMethod extends TruggerReflectedObject<Method> implements ReflectedMethod {

  private final Method method;
  private final Object target;

  public TruggerReflectedMethod(Method method, Object target) {
    super(method);
    this.method = method;
    this.target = target;
  }

  @Override
  public Object target() {
    return this.target;
  }

  @Override
  public Method unwrap() {
    return this.method;
  }

  @Override
  public <E> E invoke(Object... args) {
    return Reflection.invoke(method).on(target).withArgs(args);
  }

  @Override
  public TypeVariable<Method>[] getTypeParameters() {
    return method.getTypeParameters();
  }

  @Override
  public Class<?> getReturnType() {
    return method.getReturnType();
  }

  @Override
  public Type getGenericReturnType() {
    return method.getGenericReturnType();
  }

  @Override
  public Class<?>[] getParameterTypes() {
    return method.getParameterTypes();
  }

  @Override
  public int getParameterCount() {
    return method.getParameterCount();
  }

  @Override
  public Type[] getGenericParameterTypes() {
    return method.getGenericParameterTypes();
  }

  @Override
  public Class<?>[] getExceptionTypes() {
    return method.getExceptionTypes();
  }

  @Override
  public Type[] getGenericExceptionTypes() {
    return method.getGenericExceptionTypes();
  }

  @Override
  public boolean isBridge() {
    return method.isBridge();
  }

  @Override
  public boolean isVarArgs() {
    return method.isVarArgs();
  }

  @Override
  public boolean isDefault() {
    return method.isDefault();
  }

  @Override
  public Object getDefaultValue() {
    return method.getDefaultValue();
  }

  @Override
  public Parameter[] getParameters() {
    return method.getParameters();
  }

  @Override
  public AnnotatedType getAnnotatedReceiverType() {
    return method.getAnnotatedReceiverType();
  }

  @Override
  public AnnotatedType[] getAnnotatedParameterTypes() {
    return method.getAnnotatedParameterTypes();
  }

  public AnnotatedType[] getAnnotatedExceptionTypes() {
    return method.getAnnotatedExceptionTypes();
  }
}

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

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class ReflectedMethod extends ReflectedObject {

  private final Method method;
  private final Object target;

  public ReflectedMethod(Method method, Object target) {
    super(method);
    this.method = method;
    this.target = target;
  }

  public Method actualMethod() {
    return this.method;
  }

  public <E> E invoke(Object... args) {
    return Reflection.invoke(method).on(target).withArgs(args);
  }

  public TypeVariable<Method>[] getTypeParameters() {
    return method.getTypeParameters();
  }

  public Class<?> getReturnType() {
    return method.getReturnType();
  }

  public Type getGenericReturnType() {
    return method.getGenericReturnType();
  }

  public Class<?>[] getParameterTypes() {
    return method.getParameterTypes();
  }

  public int getParameterCount() {
    return method.getParameterCount();
  }

  public Type[] getGenericParameterTypes() {
    return method.getGenericParameterTypes();
  }

  public Class<?>[] getExceptionTypes() {
    return method.getExceptionTypes();
  }

  public Type[] getGenericExceptionTypes() {
    return method.getGenericExceptionTypes();
  }

  public boolean isBridge() {
    return method.isBridge();
  }

  public boolean isVarArgs() {
    return method.isVarArgs();
  }

  public boolean isDefault() {
    return method.isDefault();
  }

  public Object getDefaultValue() {
    return method.getDefaultValue();
  }

  public Parameter[] getParameters() {
    return method.getParameters();
  }

  @Override
  public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
    return method.getAnnotationsByType(annotationClass);
  }

  public AnnotatedType getAnnotatedReceiverType() {
    return method.getAnnotatedReceiverType();
  }

  public AnnotatedType[] getAnnotatedParameterTypes() {
    return method.getAnnotatedParameterTypes();
  }

  public AnnotatedType[] getAnnotatedExceptionTypes() {
    return method.getAnnotatedExceptionTypes();
  }
}

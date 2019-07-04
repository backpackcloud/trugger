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

import io.backpackcloud.trugger.reflection.ReflectedObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;

/**
 * Base class for all objects reflected through this framework.
 *
 * @since 7.0
 */
public abstract class TruggerReflectedObject<E extends Member> implements ReflectedObject<E> {

  private final AnnotatedElement object;
  private final Member member;

  public TruggerReflectedObject(AnnotatedElement object) {
    this.object = object;
    this.member = (Member) object;
  }

  @Override
  public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
    return this.object.getAnnotation(annotationClass);
  }

  @Override
  public Annotation[] getAnnotations() {
    return this.object.getAnnotations();
  }

  @Override
  public Annotation[] getDeclaredAnnotations() {
    return this.object.getDeclaredAnnotations();
  }

  @Override
  public Class<?> getDeclaringClass() {
    return member.getDeclaringClass();
  }

  @Override
  public String getName() {
    return member.getName();
  }

  @Override
  public int getModifiers() {
    return member.getModifiers();
  }

  @Override
  public boolean isSynthetic() {
    return member.isSynthetic();
  }

}

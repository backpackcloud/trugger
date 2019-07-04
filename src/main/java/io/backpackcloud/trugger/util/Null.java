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
package io.backpackcloud.trugger.util;

import io.backpackcloud.trugger.HandlingException;
import io.backpackcloud.trugger.Invoker;
import io.backpackcloud.trugger.ValueHandler;
import io.backpackcloud.trugger.reflection.MethodInvoker;
import io.backpackcloud.trugger.reflection.ConstructorInvoker;
import io.backpackcloud.trugger.reflection.FieldHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * This class has a set of objects that should be used to eliminate the checks against
 * <code>null</code> objects.
 *
 * @author Marcelo Guimaraes
 */
public interface Null {

  /**
   * An {@code AnnotatedElement} that doesn't have annotations.
   */
  AnnotatedElement NULL_ANNOTATED_ELEMENT = new AnnotatedElement() {

    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
      return null;
    }

    public Annotation[] getAnnotations() {
      return new Annotation[0];
    }

    public Annotation[] getDeclaredAnnotations() {
      return new Annotation[0];
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
      return false;
    }

  };

  /**
   * An {@code Invoker} that doesn't do anything.
   */
  Invoker NULL_INVOKER = new Invoker() {
    @Override
    public <E> E withArgs(Object... args) {
      return null;
    }

    @Override
    public <E> E withoutArgs() {
      return null;
    }

  };

  /**
   * A {@code MethodInvoker} that doesn't do anything.
   */
  MethodInvoker NULL_METHOD_INVOKER = new MethodInvoker() {

    @Override
    public Invoker on(Object instance) {
      return NULL_INVOKER;
    }

    @Override
    public <E> E withArgs(Object... args) {
      return null;
    }

    @Override
    public <E> E withoutArgs() {
      return null;
    }

  };

  /**
   * A {@code ValueHandler} that doesn't do anything.
   */
  ValueHandler NULL_VALUE_HANDLER = new ValueHandler() {
    @Override
    public <E> E getValue() throws HandlingException {
      return null;
    }

    @Override
    public void setValue(Object value) throws HandlingException {

    }
  };

  /**
   * A {@code FieldHandler} that doesn't do anything.
   */
  FieldHandler NULL_FIELD_HANDLER = new FieldHandler() {

    @Override
    public <E> E getValue() throws HandlingException {
      return null;
    }

    @Override
    public void setValue(Object value) throws HandlingException {
    }

    @Override
    public ValueHandler on(Object source) {
      return NULL_VALUE_HANDLER;
    }

  };

  /**
   * A {@code ConstructorInvoker} that doesn't do anything.
   */
  ConstructorInvoker NULL_CONSTRUCTOR_INVOKER = new ConstructorInvoker() {
    @Override
    public <E> E withArgs(Object... args) {
      return null;
    }

    @Override
    public <E> E withoutArgs() {
      return null;
    }

  };

}

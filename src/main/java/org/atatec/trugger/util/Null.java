/*
 * Copyright 2009-2012 Marcelo Guimarães
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
package org.atatec.trugger.util;

import org.atatec.trugger.exception.ExceptionHandler;
import org.atatec.trugger.HandlingException;
import org.atatec.trugger.Invoker;
import org.atatec.trugger.ValueHandler;
import org.atatec.trugger.reflection.ConstructorInvoker;
import org.atatec.trugger.reflection.FieldHandler;
import org.atatec.trugger.reflection.MethodInvoker;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Enumeration;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

/**
 * This class has a set of objects that should be used to eliminate the checks against
 * <code>null</code> objects.
 *
 * @author Marcelo Guimarães
 */
public final class Null {

  private Null() {
  }

  /** An <code>AnnotatedElement</code> that has no annotation. */
  public static final AnnotatedElement NULL_ANNOTATED_ELEMENT = new AnnotatedElement() {

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

  /** An empty bundle. */
  public static final ResourceBundle NULL_BUNDLE = new ResourceBundle() {

    @Override
    public Enumeration<String> getKeys() {
      return new Enumeration<String>() {

        public boolean hasMoreElements() {
          return false;
        }

        public String nextElement() {
          throw new NoSuchElementException();
        }

      };
    }

    @Override
    protected Object handleGetObject(String key) {
      return null;
    }

    @Override
    public Locale getLocale() {
      return Locale.getDefault();
    }

  };

  public static final Invoker NULL_INVOKER = new Invoker() {
    @Override
    public <E> E withArgs(Object... args) {
      return null;
    }

    @Override
    public <E> E withoutArgs() {
      return null;
    }

    @Override
    public Invoker whenError(ExceptionHandler handler) {
      return this;
    }

  };

  public static final MethodInvoker NULL_METHOD_INVOKER = new MethodInvoker() {

    @Override
    public Invoker in(Object instance) {
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

    @Override
    public Invoker whenError(ExceptionHandler handler) {
      return this;
    }

  };

  public static final ValueHandler NULL_VALUE_HANDLER = new ValueHandler() {
    @Override
    public <E> E value() throws HandlingException {
      return null;
    }

    @Override
    public void value(Object value) throws HandlingException {

    }
  };

  public static final FieldHandler NULL_FIELD_HANDLER = new FieldHandler() {

    @Override
    public <E> E value() throws HandlingException {
      return null;
    }

    @Override
    public void value(Object value) throws HandlingException {
    }

    @Override
    public ValueHandler in(Object source) {
      return NULL_VALUE_HANDLER;
    }

  };

  public static final ConstructorInvoker NULL_CONSTRUCTOR_INVOKER = new ConstructorInvoker() {
    @Override
    public <E> E withArgs(Object... args) {
      return null;
    }

    @Override
    public <E> E withoutArgs() {
      return null;
    }

    @Override
    public Invoker whenError(ExceptionHandler handler) {
      return this;
    }

  };

  public static final ExceptionHandler NULL_EXCEPTION_HANDLER = new ExceptionHandler() {
    @Override
    public void handle(Throwable throwable) {}

  };

}

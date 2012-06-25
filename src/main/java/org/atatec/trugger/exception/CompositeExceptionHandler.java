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

package org.atatec.trugger.exception;

import org.atatec.trugger.dsl.With;
import org.atatec.trugger.reflection.ClassHierarchyFinder;
import org.atatec.trugger.reflection.Reflection;
import org.atatec.trugger.registry.MapRegistry;
import org.atatec.trugger.registry.Registry;
import org.atatec.trugger.util.Null;

/**
 * An ExceptionHandler that exposes a DSL to configure its behaviour.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 4.0
 */
public class CompositeExceptionHandler implements ExceptionHandler {

  private final Registry<Class<? extends Throwable>, ExceptionHandler> handlers;

  public CompositeExceptionHandler() {
    handlers = new MapRegistry<Class<? extends Throwable>, ExceptionHandler>();
  }

  /**
   * Configures this handler to throw every RuntimeException of the given type.
   *
   * @return a reference to this object.
   */
  public CompositeExceptionHandler throwEvery(Class<? extends RuntimeException> clazz) {
    handlers.register(ExceptionHandlers.DEFAULT_EXCEPTION_HANDLER).to(clazz);
    return this;
  }

  /**
   * Configures this handler to throw every cause of the given exception.
   *
   * @return a reference to this object
   */
  public CompositeExceptionHandler throwEveryCauseOf(Class<? extends Throwable> clazz) {
    handlers.register(new CauseThrowExceptionHandler()).to(clazz);
    return this;
  }

  /**
   * Configures this handler to encapsulate every given exception with another one.
   *
   * @return a reference to this object
   */
  public With<Class<? extends RuntimeException>, CompositeExceptionHandler> encapsulateEvery(final Class<? extends Throwable> clazz) {
    return new With<Class<? extends RuntimeException>, CompositeExceptionHandler>() {
      @Override
      public CompositeExceptionHandler with(Class<? extends RuntimeException> input) {
        handlers.register(new EncapsulatorExceptionHandler(input)).to(clazz);
        return CompositeExceptionHandler.this;
      }
    };
  }

  /**
   * Configures this handler to encapsulate every cause of the given exception with
   * another one.
   *
   * @return a reference to this object
   */
  public With<Class<? extends RuntimeException>, CompositeExceptionHandler> encapsulateEveryCauseOf(final Class<? extends Throwable> clazz) {
    return new With<Class<? extends RuntimeException>, CompositeExceptionHandler>() {
      @Override
      public CompositeExceptionHandler with(Class<? extends RuntimeException> input) {
        handlers.register(new CauseEncapsulatorExceptionHandler(input)).to(clazz);
        return CompositeExceptionHandler.this;
      }
    };
  }

  /**
   * Configures this handler to silence every given exception.
   *
   * @return a reference to this object
   */
  public CompositeExceptionHandler silence(Class<? extends Throwable> clazz) {
    handlers.register(Null.NULL_EXCEPTION_HANDLER).to(clazz);
    return this;
  }

  /**
   * Configures an ExceptionHandler based on its generic type.
   *
   * @return a reference to this object.
   */
  public CompositeExceptionHandler use(ExceptionHandler handler) {
    Class type = Reflection.reflect().genericType("E").in(handler);
    return handle(type).with(handler);
  }

  /** Configures an ExceptionHandler to use */
  public With<ExceptionHandler, CompositeExceptionHandler> handle(final Class<? extends Throwable> clazz) {
    return new With<ExceptionHandler, CompositeExceptionHandler>() {
      @Override
      public CompositeExceptionHandler with(ExceptionHandler input) {
        handlers.register(input).to(clazz);
        return CompositeExceptionHandler.this;
      }
    };
  }

  @Override
  public void handle(Throwable throwable) {
    ExceptionHandler handler = new ClassHierarchyFinder<ExceptionHandler>() {
      @Override
      protected ExceptionHandler findObject(Class clazz) {
        return handlers.registryFor(clazz);
      }
    }.find(throwable);
    if (handler != null) {
      handler.handle(throwable);
    }
  }

}

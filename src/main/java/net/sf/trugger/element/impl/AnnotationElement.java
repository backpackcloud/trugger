/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.element.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sf.trugger.HandlingException;
import net.sf.trugger.ValueHandler;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.UnwritableElementException;

/**
 * Class that represents an Annotation property.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public final class AnnotationElement extends AbstractElement implements Element {
  
  private Method method;
  
  /**
   * Creates a new AnnotationProperty based on the given method.
   * 
   * @param method
   *          the method that allows access to the property.
   */
  public AnnotationElement(Method method) {
    super(method.getName());
    this.method = method;
    this.annotatedElement = method;
  }
  
  @Override
  public Class<?> type() {
    return method.getReturnType();
  }
  
  public ValueHandler in(Object target) {
    return new Handler(target);
  }
  
  public Class<?> declaringClass() {
    return method.getDeclaringClass();
  }
  
  public boolean isReadable() {
    return true;
  }
  
  public boolean isWritable() {
    return false;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + method.hashCode();
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final AnnotationElement other = (AnnotationElement) obj;
    if (!method.equals(other.method)) {
      return false;
    }
    return true;
  }
  
  private class Handler implements ValueHandler {
    
    private final Object annotation;
    
    public Handler(Object annotation) {
      super();
      this.annotation = annotation;
    }
    
    public <E> E value() throws HandlingException {
      try {
        return (E) method.invoke(annotation);
      } catch (InvocationTargetException e) {
        throw new HandlingException(e.getCause());
      } catch (IllegalAccessException e) {
        throw new HandlingException(e);
      }
    }
    
    public void value(Object value) throws HandlingException {
      throw new UnwritableElementException(name());
    }
    
  }
  
}

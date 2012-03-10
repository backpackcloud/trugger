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
package net.sf.trugger.reflection.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.sf.trugger.reflection.ConstructorInvoker;
import net.sf.trugger.reflection.FieldHandler;
import net.sf.trugger.reflection.MethodInvoker;
import net.sf.trugger.reflection.ReflectionFactory;
import net.sf.trugger.reflection.Reflector;

/**
 * A default factory for {@link Reflector} objects.
 * 
 * @author Marcelo Varella Barca Guimarães
 */
public class TruggerReflectionFactory implements ReflectionFactory {
  
  public Reflector createReflector() {
    return new TruggerReflector();
  }
  
  public MethodInvoker createInvoker(Method method) {
    return new TruggerMethodInvoker(method);
  }
  
  public ConstructorInvoker createInvoker(Constructor<?> constructor) {
    return new TruggerConstructorInvoker(constructor);
  }
  
  public FieldHandler createHandler(Field field) {
    return new TruggerFieldHandler(field);
  }
  
}

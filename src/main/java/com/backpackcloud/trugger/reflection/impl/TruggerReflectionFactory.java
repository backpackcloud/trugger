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

import com.backpackcloud.trugger.reflection.ConstructorInvoker;
import com.backpackcloud.trugger.reflection.FieldHandler;
import com.backpackcloud.trugger.reflection.MethodInvoker;
import com.backpackcloud.trugger.reflection.ReflectionFactory;
import com.backpackcloud.trugger.reflection.Reflector;
import com.backpackcloud.trugger.util.Null;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A default factory for {@link Reflector} objects.
 *
 * @author Marcelo "Ataxexe" Guimarães
 */
public class TruggerReflectionFactory implements ReflectionFactory {

  public Reflector createReflector() {
    return new TruggerReflector();
  }

  public MethodInvoker createInvoker(Method method) {
    return method != null ? new TruggerMethodInvoker(method) : Null.NULL_METHOD_INVOKER;
  }

  public ConstructorInvoker createInvoker(Constructor<?> constructor) {
    return constructor != null ? new TruggerConstructorInvoker(constructor) :
        Null.NULL_CONSTRUCTOR_INVOKER;
  }

  public FieldHandler createHandler(Field field) {
    return field != null ? new TruggerFieldHandler(field) : Null.NULL_FIELD_HANDLER;
  }

}

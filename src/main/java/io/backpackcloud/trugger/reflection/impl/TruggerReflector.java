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

import io.backpackcloud.trugger.reflection.ConstructorSelector;
import io.backpackcloud.trugger.reflection.ConstructorsSelector;
import io.backpackcloud.trugger.reflection.FieldSelector;
import io.backpackcloud.trugger.reflection.FieldsSelector;
import io.backpackcloud.trugger.reflection.GenericTypeSelector;
import io.backpackcloud.trugger.reflection.MethodSelector;
import io.backpackcloud.trugger.reflection.MethodsSelector;
import io.backpackcloud.trugger.reflection.ReflectionException;
import io.backpackcloud.trugger.reflection.Reflector;
import io.backpackcloud.trugger.util.Utils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An implementation of the reflection operations.
 *
 * @author Marcelo Guimaraes
 */
public class TruggerReflector implements Reflector {

  private final MemberFindersRegistry registry;

  public TruggerReflector(MemberFindersRegistry defaultRegistry) {
    this.registry = defaultRegistry;
  }

  public TruggerReflector() {
    this(new DeclaredMemberFindersRegistry());
  }

  @Override
  public final Reflector visible() {
    return new TruggerReflector(new VisibleMemberFindersRegistry());
  }

  @Override
  public final Reflector declared() {
    return new TruggerReflector(new DeclaredMemberFindersRegistry());
  }

  @Override
  public ConstructorSelector constructor() {
    return new TruggerConstructorSelector(registry);
  }

  @Override
  public ConstructorsSelector constructors() {
    return new TruggerConstructorsSelector(registry.constructorsFinder());
  }

  @Override
  public FieldSelector field(String name) {
    return new TruggerFieldSelector(name, registry);
  }

  @Override
  public FieldsSelector fields() {
    return new TruggerFieldsSelector(registry.fieldsFinder());
  }

  @Override
  public MethodSelector method(String name) {
    return new TruggerMethodSelector(name, registry);
  }

  @Override
  public MethodsSelector methods() {
    return new TruggerMethodsSelector(registry.methodsFinder());
  }

  @Override
  public List<Class> interfacesOf(Object target) {
    Class<?> objectClass = Utils.resolveType(target);
    Set<Class> set = new HashSet<>(30);
    for (Class c = objectClass;
         (c != null) && !Object.class.equals(c);
         c = c.getSuperclass()) {
      for (Class interf : c.getInterfaces()) {
        loop(interf, set);
      }
    }
    return new ArrayList<>(set);
  }

  private void loop(Class interf, Collection<Class> interfaces) {
    interfaces.add(interf);
    for (Class extendedInterfaces : interf.getInterfaces()) {
      loop(extendedInterfaces, interfaces);
    }
  }

  @Override
  public GenericTypeSelector genericType(String parameterName) {
    return target -> TruggerGenericTypeResolver.resolveParameterName(parameterName, Utils.resolveType(target));
  }

  @Override
  public Class genericTypeOf(Object target) {
    Map<Type, Type> typeVariableMap =
        TruggerGenericTypeResolver.getTypeVariableMap(Utils.resolveType(target));
    Set<Type> keySet = typeVariableMap.keySet();
    Set<String> paramNames = new HashSet<>(keySet.size());
    paramNames.addAll(
        keySet.stream().map(Type::toString)
            .collect(Collectors.toList())
    );
    if (paramNames.isEmpty()) {
      throw new ReflectionException("No generic type found.");
    } else if (paramNames.size() > 1) {
      throw new ReflectionException("More than one generic type found.");
    }
    String name = paramNames.iterator().next();
    return genericType(name).of(target);
  }

  @Override
  public Optional<Method> bridgedMethodFor(Method bridgeMethod) {
    return Optional.of(new TruggerBridgeMethodResolver(bridgeMethod).findBridgedMethod());
  }

}

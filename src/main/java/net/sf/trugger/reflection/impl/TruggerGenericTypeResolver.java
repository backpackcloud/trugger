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

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;

/**
 * Helper class for resolving generic types against type variables.
 * <p>
 * This class is originally from the SpringFramework.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Marcelo Varella Barca Guimarães (method
 *         {@link #resolveParameterName(String, Class)} and default visibility
 *         to public methods).
 */
final class TruggerGenericTypeResolver {

  //Changed to generic type
  private static final Map<Class, Map<Type, Type>> typeVariableCache =
    Collections.synchronizedMap(new WeakHashMap<Class, Map<Type, Type>>());

  private TruggerGenericTypeResolver() {}

  /**
   * Resolve the specified generic type against the given TypeVariable map.
   *
   * @param genericType
   *          the generic type to resolve
   * @param typeVariableMap
   *          the TypeVariable Map to resolved against
   * @return the type if it resolves to a Class, or <code>Object.class otherwise
   */
  static Class resolveType(Type genericType, Map<Type, Type> typeVariableMap) {
    Type rawType = getRawType(genericType, typeVariableMap);
    return(rawType instanceof Class ? (Class) rawType : Object.class);
  }

  /**
   * Resolves the generic parameter name of a class.
   *
   * @param parameterName
   *          the parameter name
   * @param target
   *          the target class
   * @return the parameter class.
   */
  static Class<?> resolveParameterName(String parameterName, Class<?> target) {
    Map<Type, Type> typeVariableMap = getTypeVariableMap(target);
    Set<Entry<Type, Type>> set = typeVariableMap.entrySet();
    Type type = Object.class;

    for (Entry<Type, Type> entry : set) {
      if (entry.getKey().toString().equals(parameterName)) {
        type = entry.getKey();
        break;
      }
    }
    return resolveType(type, typeVariableMap);
  }

  /**
   * Determine the raw type for the given generic parameter type.
   *
   * @param genericType
   *          the generic type to resolve
   * @param typeVariableMap
   *          the TypeVariable Map to resolved against
   * @return the resolved raw type
   */
  static Type getRawType(Type genericType, Map<Type, Type> typeVariableMap) {
    Type resolvedType = genericType;
    if (genericType instanceof TypeVariable) {
      TypeVariable tv = (TypeVariable) genericType;
      resolvedType = typeVariableMap.get(tv);
      if (resolvedType == null) {
        resolvedType = extractBoundForTypeVariable(tv);
      }
    }
    if (resolvedType instanceof ParameterizedType) {
      return ((ParameterizedType) resolvedType).getRawType();
    } else {
      return resolvedType;
    }
  }

  /**
   * Build a mapping of {@link TypeVariable#getName TypeVariable names} to
   * concrete {@link Class} for the specified {@link Class}. Searches all super
   * types, enclosing types and interfaces.
   */
  static Map<Type, Type> getTypeVariableMap(Class clazz) {
    Map<Type, Type> typeVariableMap = typeVariableCache.get(clazz);

    if (typeVariableMap == null) {
      typeVariableMap = new HashMap<Type, Type>();

      // interfaces
      extractTypeVariablesFromGenericInterfaces(clazz.getGenericInterfaces(), typeVariableMap);

      // super class
      Type genericType = clazz.getGenericSuperclass();
      Class type = clazz.getSuperclass();
      while ((type != null) && !Object.class.equals(type)) {
        if (genericType instanceof ParameterizedType) {
          ParameterizedType pt = (ParameterizedType) genericType;
          populateTypeMapFromParameterizedType(pt, typeVariableMap);
        }
        extractTypeVariablesFromGenericInterfaces(type.getGenericInterfaces(), typeVariableMap);
        genericType = type.getGenericSuperclass();
        type = type.getSuperclass();
      }

      // enclosing class
      type = clazz;
      while (type.isMemberClass()) {
        genericType = type.getGenericSuperclass();
        if (genericType instanceof ParameterizedType) {
          ParameterizedType pt = (ParameterizedType) genericType;
          populateTypeMapFromParameterizedType(pt, typeVariableMap);
        }
        type = type.getEnclosingClass();
      }

      typeVariableCache.put(clazz, typeVariableMap);
    }

    return typeVariableMap;
  }

  /**
   * Extracts the bound <code>Type for a given {@link TypeVariable}.
   */
  private static Type extractBoundForTypeVariable(TypeVariable typeVariable) {
    Type[] bounds = typeVariable.getBounds();
    if (bounds.length == 0) {
      return Object.class;
    }
    Type bound = bounds[0];
    if (bound instanceof TypeVariable) {
      bound = extractBoundForTypeVariable((TypeVariable) bound);
    }
    return bound;
  }

  private static void extractTypeVariablesFromGenericInterfaces(Type[] genericInterfaces, Map typeVariableMap) {
    for (Type genericInterface : genericInterfaces) {
      if (genericInterface instanceof ParameterizedType) {
        ParameterizedType pt = (ParameterizedType) genericInterface;
        populateTypeMapFromParameterizedType(pt, typeVariableMap);
        if (pt.getRawType() instanceof Class) {
          extractTypeVariablesFromGenericInterfaces(((Class) pt.getRawType()).getGenericInterfaces(), typeVariableMap);
        }
      } else if (genericInterface instanceof Class) {
        extractTypeVariablesFromGenericInterfaces(((Class) genericInterface).getGenericInterfaces(), typeVariableMap);
      }
    }
  }

  /**
   * Read the {@link TypeVariable TypeVariables} from the supplied
   * {@link ParameterizedType} and add mappings corresponding to the
   * {@link TypeVariable#getName TypeVariable name} -> concrete type to the
   * supplied {@link Map}.
   * <p>
   * Consider this case:
   *
   * <pre class="code>
   * public
   * interface Foo&lt;S, T&gt; { .. } public class FooImpl implements
   * Foo&lt;String, Integer&gt; { .. }
   * </pre>
   *
   * For '<code>FooImpl' the
   * following mappings would be added to the {@link Map}:
   * {S=java.lang.String, T=java.lang.Integer}.
   */
  private static void populateTypeMapFromParameterizedType(ParameterizedType type, Map<Type, Type> typeVariableMap) {
    if (type.getRawType() instanceof Class) {
      Type[] actualTypeArguments = type.getActualTypeArguments();
      TypeVariable[] typeVariables = ((Class) type.getRawType()).getTypeParameters();
      for (int i = 0 ; i < actualTypeArguments.length ; i++) {
        Type actualTypeArgument = actualTypeArguments[i];
        TypeVariable variable = typeVariables[i];
        if (actualTypeArgument instanceof Class) {
          typeVariableMap.put(variable, actualTypeArgument);
        } else if (actualTypeArgument instanceof GenericArrayType) {
          typeVariableMap.put(variable, actualTypeArgument);
        } else if (actualTypeArgument instanceof ParameterizedType) {
          typeVariableMap.put(variable, actualTypeArgument);
        } else if (actualTypeArgument instanceof TypeVariable) {
          // We have a type that is parameterized at instantiation time
          // the nearest match on the bridge method will be the bounded type.
          TypeVariable typeVariableArgument = (TypeVariable) actualTypeArgument;
          Type resolvedType = typeVariableMap.get(typeVariableArgument);
          if (resolvedType == null) {
            resolvedType = extractBoundForTypeVariable(typeVariableArgument);
          }
          typeVariableMap.put(variable, resolvedType);
        }
      }
    }
  }

}

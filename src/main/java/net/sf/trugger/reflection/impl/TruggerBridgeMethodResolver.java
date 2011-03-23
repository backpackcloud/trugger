/*
 * Copyright 2009-2011 Marcelo Varella Barca Guimarães
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

import net.sf.trugger.iteration.Iteration;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.reflection.Reflection;
import net.sf.trugger.reflection.ReflectionException;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.Set;

import static net.sf.trugger.reflection.Reflection.method;
import static net.sf.trugger.reflection.Reflection.methods;

/**
 * Helper for resolving synthetic {@link Method#isBridge bridge Methods} to the
 * {@link Method} being bridged.
 * <p>
 * Given a synthetic {@link Method#isBridge bridge Method} returns the
 * {@link Method} being bridged. A bridge method may be created by the compiler
 * when extending a parameterized type whose methods have parameterized
 * arguments. During runtime invocation the bridge {@link Method} may be invoked
 * and/or used via reflection. When attempting to locate annotations on
 * {@link Method Methods}, it is wise to check for bridge {@link Method Methods}
 * as appropriate and find the bridged {@link Method}.
 * <p>
 * See The Java Language Specification for more details on the use of bridge
 * methods.
 * <p>
 * This class is originally from the SpringFramework.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Marcelo Varella Barca Guimarães (some code adaptations).
 */
final class TruggerBridgeMethodResolver {

  private final Method bridgeMethod;
  private final Map<Type, Type> typeParameterMap;

  TruggerBridgeMethodResolver(Method bridgeMethod) {
    this.bridgeMethod = bridgeMethod;
    this.typeParameterMap = TruggerGenericTypeResolver.getTypeVariableMap(bridgeMethod.getDeclaringClass());
  }

  /**
   * Find the original method for the supplied {@link Method bridge Method}.
   * <p>
   * It is safe to call this method passing in a non-bridge {@link Method}
   * instance to the constructor. In such a case, the supplied {@link Method}
   * instance is returned directly to the caller. Callers are
   * <strong>not</strong> required to check for bridging before calling this
   * method.
   *
   * @return the original method for the supplied {@link Method bridge Method}
   * @throws ReflectionException
   *           if no bridged {@link Method} can be found.
   */
  Method findBridgedMethod() {
    if (!bridgeMethod.isBridge()) {
      return bridgeMethod;
    }
    // Gather all methods with matching name and parameter size.
    Set<Method> candidateMethods = methods().recursively()
      .thatMatches(new SimpleBridgeCandidatePredicate())
    .in(bridgeMethod.getDeclaringClass());

    if (candidateMethods.isEmpty()) {
      throw new ReflectionException("Unable to locate bridged method for bridge method '" + bridgeMethod + "'");
    } else if (candidateMethods.size() > 1) {
      Iteration.retainFrom(candidateMethods).elementsMatching(new BridgeCandidatePredicate());
    }
    return candidateMethods.iterator().next();
  }

  private class SimpleBridgeCandidatePredicate implements Predicate<Method> {

    public boolean evaluate(Method candidateMethod) {
      return(!candidateMethod.isBridge() && !candidateMethod.equals(bridgeMethod)
          && candidateMethod.getName().equals(bridgeMethod.getName()) && (candidateMethod.getParameterTypes().length == bridgeMethod
              .getParameterTypes().length));
    }

  }

  private class BridgeCandidatePredicate implements Predicate<Method> {

    /**
     * Returns <code>true if the {@link Type} signature of both the supplied
     * {@link Method#getGenericParameterTypes() generic Method} and concrete
     * {@link Method} are equal after resolving all {@link TypeVariable
     * TypeVariables} using the supplied TypeVariable Map, otherwise returns
     * <code>false.
     */
    private boolean isResolvedTypeMatch(Method genericMethod, Method candidateMethod) {
      Type[] genericParameters = genericMethod.getGenericParameterTypes();
      Class[] candidateParameters = candidateMethod.getParameterTypes();
      if (genericParameters.length != candidateParameters.length) {
        return false;
      }
      for (int i = 0 ; i < genericParameters.length ; i++) {
        Type genericParameter = genericParameters[i];
        Class candidateParameter = candidateParameters[i];
        if (candidateParameter.isArray()) {
          // An array type: compare the component type.
          Type rawType = TruggerGenericTypeResolver.getRawType(genericParameter, typeParameterMap);
          if (rawType instanceof GenericArrayType) {
            if (!candidateParameter.getComponentType().equals(
                TruggerGenericTypeResolver.resolveType(((GenericArrayType) rawType).getGenericComponentType(),
                    typeParameterMap))) {
              return false;
            }
            break;
          }
        }
        // A non-array type: compare the type itself.
        if (!candidateParameter.equals(TruggerGenericTypeResolver.resolveType(genericParameter, typeParameterMap))) {
          return false;
        }
      }
      return true;
    }

    /**
     * Searches for the generic {@link Method} declaration whose erased
     * signature matches that of the supplied bridge method.
     */
    private Method findGenericDeclaration() {
      // Search parent types for method that has same signature as bridge.
      Class superclass = bridgeMethod.getDeclaringClass().getSuperclass();
      while (!Object.class.equals(superclass)) {
        Method method = searchForMatch(superclass);
        if ((method != null) && !method.isBridge()) {
          return method;
        }
        superclass = superclass.getSuperclass();
      }
      // Search interfaces.
      Set<Class<?>> interfaces = Reflection.reflect().interfaces().in(bridgeMethod.getDeclaringClass()); //changed
      for (Class anInterface : interfaces) {
        Method method = searchForMatch(anInterface);
        if ((method != null) && !method.isBridge()) {
          return method;
        }
      }
      return null;
    }

    /**
     * If the supplied {@link Class} has a declared {@link Method} whose
     * signature matches that of the supplied {@link Method}, then this matching
     * {@link Method} is returned, otherwise <code>null</code> is returned.
     */
    private Method searchForMatch(Class type) {
      return method(bridgeMethod.getName())
        .withParameters(bridgeMethod.getParameterTypes())
      .in(type);
    }

    public boolean evaluate(Method candidateMethod) {
      if (isResolvedTypeMatch(candidateMethod, bridgeMethod)) {
        return true;
      }
      Method method = findGenericDeclaration();
      return((method != null) && isResolvedTypeMatch(method, candidateMethod));
    }
  }
}

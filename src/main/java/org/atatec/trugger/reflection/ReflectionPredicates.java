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
package org.atatec.trugger.reflection;

import org.atatec.trugger.predicate.CompositePredicate;
import org.atatec.trugger.predicate.Predicate;
import org.atatec.trugger.predicate.Predicates;
import org.atatec.trugger.util.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.regex.Pattern;

import static org.atatec.trugger.predicate.Predicates.is;

/**
 * An utilitity class for helping the use of {@link Predicate} object that involves
 * Reflection.
 *
 * @author Marcelo Varella Barca Guimarães
 * @see Predicates
 */
public class ReflectionPredicates {

  private static final Pattern TO_PATTERN = Pattern.compile("to[A-Z].*");
  private static final Pattern GET_PATTERN = Pattern.compile("get[A-Z].*");
  private static final Pattern SET_PATTERN = Pattern.compile("set[A-Z].*");
  private static final Pattern IS_PATTERN = Pattern.compile("is[A-Z].*");

  /**
   * A predicate that returns <code>true</code> if the evaluated method is a getter
   * method.
   * <p/>
   * The method <strong>may</strong> have a prefix "get" or "is", take no parameter and
   * return an object. If the method has the prefix "is", then it must return a boolean
   * value.
   */
  public static final CompositePredicate<Method> GETTER = is(new Predicate<Method>() {

    public boolean evaluate(Method method) {
      if (!Modifier.isPublic(method.getModifiers())) {
        return false;
      }
      String name = method.getName();
      Class<?> returnType = method.getReturnType();
      if ((method.getParameterTypes().length != 0) || Reflection.isStatic(method) ||
        (returnType == null || returnType.equals(void.class) || returnType.equals(Void.class))) {
        return false;
      }
      if (TO_PATTERN.matcher(name).matches()) {
        return false;
      }
      if (name.startsWith("get")) {
        return GET_PATTERN.matcher(name).matches();
      } else if (name.startsWith("is")) {
        boolean returnBoolean = (Boolean.class.equals(returnType) || boolean.class.equals(returnType));
        return returnBoolean && IS_PATTERN.matcher(name).matches();
      }
      return true;
    }

    @Override
    public String toString() {
      return "Getter";
    }
  });

  /**
   * A predicate that returns <code>true</code> if the evaluated method is a setter
   * method.
   * <p/>
   * The method must have the "set" prefix, take one parameter and return no value (a void
   * method).
   */
  public static final CompositePredicate<Method> SETTER = is(new Predicate<Method>() {

    public boolean evaluate(Method method) {
      if (!Modifier.isPublic(method.getModifiers())) {
        return false;
      }
      Class returnType = method.getReturnType();
      if ((method.getParameterTypes().length != 1) ||
        !(returnType == null || returnType.equals(void.class) || returnType.equals(Void.class))) {
        return false;
      }
      return SET_PATTERN.matcher(method.getName()).matches();
    }

    @Override
    public String toString() {
      return "Setter";
    }
  });

  /**
   * @return a predicate that returns <code>true</code> if a method is a getter method for
   *         the specified property name.
   */
  public static CompositePredicate<Method> getterFor(final String propertyName) {
    return GETTER.and(new Predicate<Method>() {

      public boolean evaluate(Method method) {
        return Reflection.parsePropertyName(method).equals(propertyName);
      }

      @Override
      public String toString() {
        return "Getter for " + propertyName;
      }
    });
  }

  /**
   * @return a predicate that returns <code>true</code> if a method is a setter method for
   *         the specified property name.
   */
  public static CompositePredicate<Method> setterFor(final String propertyName) {
    return SETTER.and(new Predicate<Method>() {

      public boolean evaluate(Method method) {
        return Reflection.parsePropertyName(method).equals(propertyName);
      }

      @Override
      public String toString() {
        return "Setter for " + propertyName;
      }
    });
  }

  /**
   * Predicate that returns <code>true</code> if a class is an <i>interface</i> and is not
   * an <i>annotation</i>.
   */
  public static final CompositePredicate<Class> INTERFACE =
    ClassPredicates.declare(Modifier.INTERFACE).and(notAssignableTo(Annotation.class));
  /** The negation of the {@link #INTERFACE} predicate. */
  public static final CompositePredicate<Class> NON_INTERFACE = INTERFACE.negate();
  /** Predicate that returns <code>true</code> if a class is an <i>enum</i>. */
  public static final CompositePredicate<Class> ENUM = is(new Predicate<Class>() {

    public boolean evaluate(Class element) {
      return element.isEnum();
    }

    @Override
    public String toString() {
      return "Enum";
    }
  });
  /** The negation of the {@link #ENUM} predicate. */
  public static final CompositePredicate<Class> NOT_ENUM = ENUM.negate();
  /** Predicate that returns <code>true</code> if a class is an <i>annotation</i>. */
  public static final CompositePredicate<Class> ANNOTATION =
    ClassPredicates.declare(Modifier.INTERFACE).and(assignableTo(Annotation.class));
  /** The negation of the {@link #ANNOTATION} predicate. */
  public static final CompositePredicate<Class> NOT_ANNOTATION = ANNOTATION.negate();
  /**
   * Predicate that returns <code>true</code> if a class is not an <i>interface</i> and is
   * not an <i>enum</i>.
   */
  public static final CompositePredicate<Class> CLASS = NON_INTERFACE.and(NOT_ENUM).and(NOT_ANNOTATION);
  /** The negation of the {@link #CLASS} predicate. */
  public static final CompositePredicate<Class> NOT_CLASS = CLASS.negate();
  /** Predicate that returns <code>true</code> if a method is a bridge method. */
  public static final CompositePredicate<Method> BRIDGE = is(new Predicate<Method>() {

    public boolean evaluate(Method element) {
      return element.isBridge();
    }

    @Override
    public String toString() {
      return "Bridge method";
    }
  });
  /** Predicate that returns <code>true</code> if a method is not a bridge method. */
  public static final CompositePredicate<Method> NON_BRIDGE = BRIDGE.negate();

  /**
   * @return a predicate that returns <code>true</code> if the evaluated element is
   *         annotated with the specified Annotation.
   */
  public static <T extends AnnotatedElement> CompositePredicate<T> annotatedWith(
    final Class<? extends Annotation> annotationType) {
    return is(new Predicate<AnnotatedElement>() {

      public boolean evaluate(AnnotatedElement element) {
        return element.isAnnotationPresent(annotationType);
      }

      @Override
      public String toString() {
        return "Annotated with " + annotationType.getName();
      }
    });
  }

  /**
   * @return a predicate that returns <code>false</code> if the evaluated element is
   *         annotated with the specified Annotation.
   */
  public static <T extends AnnotatedElement> CompositePredicate<T> notAnnotatedWith(
    final Class<? extends Annotation> annotationType) {
    return ReflectionPredicates.<T>annotatedWith(annotationType).negate();
  }

  /** A predicate that returns <code>true</code> if the element has any annotations. */
  public static final CompositePredicate<AnnotatedElement> ANNOTATED =
    is(new Predicate<AnnotatedElement>() {

      public boolean evaluate(AnnotatedElement element) {
        return element.getDeclaredAnnotations().length > 0;
      }

      @Override
      public String toString() {
        return "Annotated";
      }
    });

  /** A predicate that returns <code>false</code> if the element has any annotation. */
  public static final CompositePredicate<AnnotatedElement> NOT_ANNOTATED = ANNOTATED.negate();

  /**
   * @return a predicate that returns <code>true</code> if the specified Class is
   *         assignable from the evaluated element.
   */
  public static CompositePredicate<Class> assignableTo(final Class clazz) {
    return is(new Predicate<Class>() {

      public boolean evaluate(Class element) {
        return clazz.isAssignableFrom(element);
      }

      @Override
      public String toString() {
        return "Assignable to " + clazz.getName();
      }
    });
  }

  /**
   * @return a predicate that returns <code>true</code> if the specified Class is not
   *         assignable from the evaluated element.
   */
  public static CompositePredicate<Class> notAssignableTo(final Class clazz) {
    return assignableTo(clazz).negate();
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated element has a
   *         name that with the given one.
   */
  public static <T extends Member> CompositePredicate<T> named(final String name) {
    return is(new Predicate<Member>() {

      public boolean evaluate(Member element) {
        return element.getName().equals(name);
      }

      @Override
      public String toString() {
        return "Named " + name;
      }
    });
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated element has the
   *         specified modifiers.
   */
  public static <T extends Member> CompositePredicate<T> declare(final int... modifiers) {
    return is(new Predicate<Member>() {

      public boolean evaluate(Member element) {
        int elModifiers = element.getModifiers();
        for (int mod : modifiers) {
          if ((elModifiers & mod) != 0) {
            return true;
          }
        }
        return false;
      }

      @Override
      public String toString() {
        return "With " + Arrays.toString(modifiers) + " modifiers";
      }
    });
  }

  /**
   * @return a predicate that returns <code>false</code> if the evaluated element does not
   *         have the specified modifiers.
   */
  public static <T extends Member> CompositePredicate<T> dontDeclare(final int... modifiers) {
    return is(new Predicate<Member>() {

      public boolean evaluate(Member element) {
        int elModifiers = element.getModifiers();
        for (int mod : modifiers) {
          if ((elModifiers & mod) != 0) {
            return false;
          }
        }
        return true;
      }

      @Override
      public String toString() {
        return "Without " + Arrays.toString(modifiers) + " modifiers";
      }
    });
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated method has the
   *         specified type as the return type.
   */
  public static CompositePredicate<Method> returns(final Class returnType) {
    return is(new Predicate<Method>() {

      public boolean evaluate(Method element) {
        return element.getReturnType().equals(returnType);
      }

      @Override
      public String toString() {
        return "Method returning " + returnType.getName();
      }
    });
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated method has the
   *         return type assignable to the specified type.
   */
  public static CompositePredicate<Method> returnsAssignableTo(final Class returnType) {
    return is(new Predicate<Method>() {

      public boolean evaluate(Method element) {
        return returnType.isAssignableFrom(element.getReturnType());
      }

      @Override
      public String toString() {
        return "Method returning assignable to " + returnType.getName();
      }
    });
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated field is of an
   *         assignable type of the given one.
   */
  public static CompositePredicate<Field> ofType(final Class type, final boolean assignable) {
    return is(new Predicate<Field>() {

      public boolean evaluate(Field element) {
        if (assignable) {
          return Utils.areAssignable(type, element.getType());
        }
        return type.equals(element.getType());
      }

      @Override
      public String toString() {
        return "Field of type " + type.getName();
      }
    });
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated method has the
   *         specified number of parameters.
   */
  public static CompositePredicate<Method> withParameterLength(final int length) {
    return is(new Predicate<Method>() {

      public boolean evaluate(Method element) {
        return element.getParameterTypes().length == length;
      }

      @Override
      public String toString() {
        return "Method with " + length + " parameters";
      }
    });
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated method has the
   *         specified parameters.
   */
  public static CompositePredicate<Method> withParameters(final Class... parameterTypes) {
    return is(new Predicate<Method>() {

      public boolean evaluate(Method element) {
        return Arrays.equals(element.getParameterTypes(), parameterTypes);
      }

      @Override
      public String toString() {
        return "With parameters " + Arrays.toString(parameterTypes);
      }
    });
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated constructor has
   *         the specified parameters.
   */
  public static CompositePredicate<Constructor> constructorWithParameters(final Class... parameterTypes) {
    return is(new Predicate<Constructor>() {

      public boolean evaluate(Constructor element) {
        return Arrays.equals(element.getParameterTypes(), parameterTypes);
      }

      @Override
      public String toString() {
        return "With parameters " + Arrays.toString(parameterTypes);
      }
    });
  }

  public static CompositePredicate<Method> HAS_DEFAULT_VALUE = is(new Predicate<Method>() {

    public boolean evaluate(Method element) {
      return element.getDefaultValue() != null;
    }

    @Override
    public String toString() {
      return "With default value";
    }
  });

  /** A predicate that returns <code>true</code> if a class is anonymous. */
  public static final CompositePredicate<Class> ANONYMOUS = is(new Predicate<Class>() {

    public boolean evaluate(Class element) {
      return element.isAnonymousClass();
    }

    @Override
    public String toString() {
      return "Anonymous";
    }
  });

  /** A predicate that returns <code>true</code> if a class is not anonymous. */
  public static final CompositePredicate<Class> NON_ANONYMOUS = ANONYMOUS.negate();

  public static CompositePredicate<Method> methodDeclaredIn(final Class type) {
    return is(new Predicate<Method>() {

      public boolean evaluate(Method element) {
        Class<?> declaringClass = element.getDeclaringClass();
        return type.isAssignableFrom(declaringClass);
      }

      public String toString() {
        return String.format("Declared in %s", type);
      }

    });
  }

}

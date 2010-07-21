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
package net.sf.trugger.reflection;

import static net.sf.trugger.predicate.Predicates.newComposition;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import net.sf.trugger.predicate.CompositePredicate;
import net.sf.trugger.predicate.Predicate;
import net.sf.trugger.predicate.Predicates;
import net.sf.trugger.util.Utils;

/**
 * An utilitity class for helping the use of {@link Predicate} object that
 * involves Reflection.
 *
 * @see Predicates
 * @author Marcelo Varella Barca Guimarães
 */
public class ReflectionPredicates {

  private static String computeElementName(String name, int i) {
    return Character.toLowerCase(name.charAt(i++)) + name.substring(i);
  }

  /**
   * A predicate that returns <code>true</code> if the evaluated method is a
   * getter method.
   * <p>
   * The method must have a prefix "get" or "is" followed by a capitalized name,
   * take no parameter, return an object and not be static. If the method has
   * the prefix "is", then it must return a boolean value.
   */
  public static final CompositePredicate<Method> GETTER = newComposition(new Predicate<Method>() {

    public boolean evaluate(Method method) {
      String name = method.getName();
      Class<?> returnType = method.getReturnType();
      if ((method.getParameterTypes().length != 0) || Reflection.isStatic(method) || (returnType == null)) {
        return false;
      }
      int i;
      if (name.startsWith("get")) {
        i = 3;
      } else if (name.startsWith("is") && (Boolean.class.equals(returnType) || boolean.class.equals(returnType))) {
        i = 2;
      } else {
        return false;
      }
      return (name.length() > i) && Character.isUpperCase(name.charAt(i));
    }

    @Override
    public String toString() {
      return "Getter";
    };
  });

  /**
   * A predicate that returns <code>true</code> if the evaluated method is a
   * setter method.
   * <p>
   * The method must have the "set" prefix, take one parameter, return no value
   * (a void method) and not be static.
   */
  public static final CompositePredicate<Method> SETTER = newComposition(new Predicate<Method>() {

    public boolean evaluate(Method method) {
      if ((method.getParameterTypes().length != 1) || (Reflection.isStatic(method))
          || (method.getReturnType() != Void.TYPE)) {
        return false;
      }

      String name = method.getName();

      return name.startsWith("set") && ((name.length() > 3) && Character.isUpperCase(name.charAt(3)));
    }

    @Override
    public String toString() {
      return "Setter";
    };
  });

  /**
   * @return a predicate that returns <code>true</code> if a method is a getter
   *         method for the specified property name.
   */
  public static final CompositePredicate<Method> getterFor(final String propertyName) {
    return newComposition(new Predicate<Method>() {

      public boolean evaluate(Method element) {
        if (!GETTER.evaluate(element)) {
          return false;
        }
        String name = element.getName();
        int i;
        if (name.startsWith("get")) {
          i = 3;
        } else { // starts with "is"
          i = 2;
        }
        name = computeElementName(name, i);
        return name.equals(propertyName);
      }

      @Override
      public String toString() {
        return "Getter for " + propertyName;
      }
    });
  }

  /**
   * @return a predicate that returns <code>true</code> if a method is a setter
   *         method for the specified property name.
   */
  public static final CompositePredicate<Method> setterFor(final String propertyName) {
    return newComposition(new Predicate<Method>() {

      public boolean evaluate(Method element) {
        if (!SETTER.evaluate(element)) {
          return false;
        }
        String name = computeElementName(element.getName(), 3); //starts with "set"
        return name.equals(propertyName);
      }

      @Override
      public String toString() {
        return "Setter for " + propertyName;
      }
    });
  }

  /**
   * @return a predicate that returns <code>true</code> if a method is a setter
   *         method for the specified property name and type.
   */
  public static final CompositePredicate<Method> setterFor(String propertyName, final Class<?> type) {
    return setterFor(propertyName).and(new Predicate<Method>() {

      public boolean evaluate(Method element) {
        return element.getParameterTypes()[0].isAssignableFrom(type);
      }

      @Override
      public String toString() {
        return "Setter for " + type.getName();
      }
    });
  }

  /**
   * Predicate that returns <code>true</code> if an element has <i>default</i>
   * access.
   */
  public static final CompositePredicate<Member> DEFAULT =
      withoutModifiers(Modifier.PUBLIC, Modifier.PRIVATE, Modifier.PROTECTED);
  /**
   * Predicate that returns <code>true</code> if an element has <i>public</i>
   * access.
   */
  public static final CompositePredicate<Member> PUBLIC = withModifiers(Modifier.PUBLIC);
  /**
   * Predicate that returns <code>true</code> if an element has <i>protected</i>
   * access.
   */
  public static final CompositePredicate<Member> PROTECTED = withModifiers(Modifier.PROTECTED);
  /**
   * Predicate that returns <code>true</code> if an element has <i>private</i>
   * access.
   */
  public static final CompositePredicate<Member> PRIVATE = withModifiers(Modifier.PRIVATE);
  /**
   * Predicate that returns <code>false</code> if an element has <i>default</i>
   * access.
   */
  public static final CompositePredicate<Member> NON_DEFAULT = DEFAULT.negate();
  /**
   * Predicate that returns <code>false</code> if an element has <i>public</i>
   * access.
   */
  public static final CompositePredicate<Member> NON_PUBLIC = PUBLIC.negate();
  /**
   * Predicate that returns <code>false</code> if an element has
   * <i>protected</i> access.
   */
  public static final CompositePredicate<Member> NON_PROTECTED = PROTECTED.negate();
  /**
   * Predicate that returns <code>false</code> if an element has <i>private</i>
   * access.
   */
  public static final CompositePredicate<Member> NON_PRIVATE = PRIVATE.negate();
  /**
   * Predicate that returns <code>true</code> if an element is <i>static</i>.
   */
  public static final CompositePredicate<Member> STATIC = withModifiers(Modifier.STATIC);
  /**
   * Predicate that returns <code>true</code> if an element is not
   * <i>static</i>.
   */
  public static final CompositePredicate<Member> NON_STATIC = STATIC.negate();
  /**
   * Predicate that returns <code>true</code> if an element is <i>synthetic</i>.
   */
  public static final CompositePredicate<Member> SYNTHETIC = newComposition(new Predicate<Member>() {

    public boolean evaluate(Member element) {
      return element.isSynthetic();
    }

    @Override
    public String toString() {
      return "Synthetic member";
    }
  });
  /**
   * Predicate that returns <code>true</code> if an element is not
   * <i>synthetic</i>.
   */
  public static final CompositePredicate<Member> NON_SYNTHETIC = SYNTHETIC.negate();
  /**
   * Predicate that returns <code>true</code> if an element is <i>final</i>.
   */
  public static final CompositePredicate<Member> FINAL = withModifiers(Modifier.FINAL);
  /**
   * Predicate that returns <code>true</code> if an element is not <i>final</i>.
   */
  public static final CompositePredicate<Member> NON_FINAL = FINAL.negate();
  /**
   * Predicate that returns <code>true</code> if an element is <i>abstract</i>.
   */
  public static final CompositePredicate<Member> ABSTRACT = withModifiers(Modifier.ABSTRACT);
  /**
   * Predicate that returns <code>true</code> if a class has <i>default</i>
   * access.
   */
  public static final CompositePredicate<Class<?>> DEFAULT_CLASS =
      classWithoutModifiers(Modifier.PUBLIC, Modifier.PRIVATE, Modifier.PROTECTED);
  /**
   * Predicate that returns <code>true</code> if a class has <i>public</i>
   * access.
   */
  public static final CompositePredicate<Class<?>> PUBLIC_CLASS = classWithModifiers(Modifier.PUBLIC);
  /**
   * Predicate that returns <code>true</code> if a class has <i>protected</i>
   * access.
   */
  public static final CompositePredicate<Class<?>> PROTECTED_CLASS = classWithModifiers(Modifier.PROTECTED);
  /**
   * Predicate that returns <code>true</code> if a class has <i>private</i>
   * access.
   */
  public static final CompositePredicate<Class<?>> PRIVATE_CLASS = classWithModifiers(Modifier.PRIVATE);
  /**
   * Predicate that returns <code>false</code> if a class has <i>default</i>
   * access.
   */
  public static final CompositePredicate<Class<?>> NON_DEFAULT_CLASS = DEFAULT_CLASS.negate();
  /**
   * Predicate that returns <code>false</code> if a class has <i>public</i>
   * access.
   */
  public static final CompositePredicate<Class<?>> NON_PUBLIC_CLASS = PUBLIC_CLASS.negate();
  /**
   * Predicate that returns <code>false</code> if a class has <i>protected</i>
   * access.
   */
  public static final CompositePredicate<Class<?>> NON_PROTECTED_CLASS = PROTECTED_CLASS.negate();
  /**
   * Predicate that returns <code>false</code> if a class has <i>private</i>
   * access.
   */
  public static final CompositePredicate<Class<?>> NON_PRIVATE_CLASS = PRIVATE_CLASS.negate();
  /**
   * Predicate that returns <code>true</code> if a class is <i>static</i>.
   */
  public static final CompositePredicate<Class<?>> STATIC_CLASS = classWithModifiers(Modifier.STATIC);
  /**
   * Predicate that returns <code>true</code> if a class is not <i>static</i>.
   */
  public static final CompositePredicate<Class<?>> NON_STATIC_CLASS = STATIC_CLASS.negate();
  /**
   * Predicate that returns <code>true</code> if a class is <i>final</i>.
   */
  public static final CompositePredicate<Class<?>> FINAL_CLASS = classWithModifiers(Modifier.FINAL);
  /**
   * Predicate that returns <code>true</code> if a class is not <i>static</i>.
   */
  public static final CompositePredicate<Class<?>> NON_FINAL_CLASS = FINAL_CLASS.negate();
  /**
   * Predicate that returns <code>true</code> if a class is <i>synthetic</i>.
   */
  public static final CompositePredicate<Class<?>> SYNTHETIC_CLASS = newComposition(new Predicate<Class<?>>() {

    public boolean evaluate(Class<?> element) {
      return element.isSynthetic();
    }

    @Override
    public String toString() {
      return "Synthetic class";
    };
  });
  /**
   * Predicate that returns <code>true</code> if a class is not
   * <i>synthetic</i>.
   */
  public static final CompositePredicate<Class<?>> NON_SYNTHETIC_CLASS = SYNTHETIC_CLASS.negate();
  /**
   * Predicate that returns <code>true</code> if a class is <i>abstract</i>.
   */
  public static final CompositePredicate<Class<?>> ABSTRACT_CLASS = classWithModifiers(Modifier.ABSTRACT);
  /**
   * Predicate that returns <code>true</code> if a class is not <i>abstract</i>.
   *
   * @since 2.7
   */
  public static final CompositePredicate<Class<?>> NON_ABSTRACT_CLASS = ABSTRACT_CLASS.negate();
  /**
   * Predicate that returns <code>true</code> if a class is an <i>interface</i>
   * and is not an <i>annotation</i>.
   */
  public static final CompositePredicate<Class<?>> INTERFACE =
      classWithModifiers(Modifier.INTERFACE).and(notAssignableTo(Annotation.class));
  /**
   * The negation of the {@link #INTERFACE} predicate.
   */
  public static final CompositePredicate<Class<?>> NON_INTERFACE = INTERFACE.negate();
  /**
   * Predicate that returns <code>true</code> if a class is an <i>enum</i>.
   */
  public static final CompositePredicate<Class<?>> ENUM = newComposition(new Predicate<Class<?>>() {

    public boolean evaluate(Class<?> element) {
      return element.isEnum();
    }

    @Override
    public String toString() {
      return "Enum";
    };
  });
  /**
   * The negation of the {@link #ENUM} predicate.
   */
  public static final CompositePredicate<Class<?>> NON_ENUM = ENUM.negate();
  /**
   * Predicate that returns <code>true</code> if a class is an
   * <i>annotation</i>.
   */
  public static final CompositePredicate<Class<?>> ANNOTATION =
      classWithModifiers(Modifier.INTERFACE).and(assignableTo(Annotation.class));
  /**
   * The negation of the {@link #ANNOTATION} predicate.
   */
  public static final CompositePredicate<Class<?>> NON_ANNOTATION = ANNOTATION.negate();
  /**
   * Predicate that returns <code>true</code> if a class is not an
   * <i>interface</i> and is not an <i>enum</i>.
   */
  public static final CompositePredicate<Class<?>> CLASS = NON_INTERFACE.and(NON_ENUM).and(NON_ANNOTATION);
  /**
   * The negation of the {@link #CLASS} predicate.
   */
  public static final CompositePredicate<Class<?>> NON_CLASS = CLASS.negate();
  /**
   * Predicate that returns <code>true</code> if a method is a bridge method.
   */
  public static final CompositePredicate<Method> BRIDGE = newComposition(new Predicate<Method>() {

    public boolean evaluate(Method element) {
      return element.isBridge();
    }

    @Override
    public String toString() {
      return "Bridge method";
    }
  });
  /**
   * Predicate that returns <code>true</code> if a method is not a bridge
   * method.
   */
  public static final CompositePredicate<Method> NON_BRIDGE = BRIDGE.negate();

  /**
   * @return a predicate that returns <code>true</code> if the evaluated element
   *         is annotated with the specified Annotation.
   */
  public static <T extends AnnotatedElement> CompositePredicate<T> annotatedWith(
      final Class<? extends Annotation> annotationType) {
    return newComposition(new Predicate<AnnotatedElement>() {

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
   * @return a predicate that returns <code>false</code> if the evaluated
   *         element is annotated with the specified Annotation.
   */
  public static <T extends AnnotatedElement> CompositePredicate<T> notAnnotatedWith(
      final Class<? extends Annotation> annotationType) {
    return ReflectionPredicates.<T> annotatedWith(annotationType).negate();
  }

  /**
   * A predicate that returns <code>true</code> if the element has any
   * annotations.
   */
  public static final CompositePredicate<AnnotatedElement> ANNOTATED =
      newComposition(new Predicate<AnnotatedElement>() {

        public boolean evaluate(AnnotatedElement element) {
          return element.getDeclaredAnnotations().length > 0;
        };

        @Override
        public String toString() {
          return "Annotated";
        }
      });

  /**
   * A predicate that returns <code>false</code> if the element has any
   * annotation.
   */
  public static final CompositePredicate<AnnotatedElement> NOT_ANNOTATED = ANNOTATED.negate();

  /**
   * @return a predicate that returns <code>true</code> if the specified Class
   *         is assignable from the evaluated element.
   */
  public static CompositePredicate<Class<?>> assignableTo(final Class<?> clazz) {
    return newComposition(new Predicate<Class<?>>() {

      public boolean evaluate(Class<?> element) {
        return clazz.isAssignableFrom(element);
      }

      @Override
      public String toString() {
        return "Assignable to " + clazz.getName();
      }
    });
  }

  /**
   * @return a predicate that returns <code>true</code> if the specified Class
   *         is not assignable from the evaluated element.
   */
  public static CompositePredicate<Class<?>> notAssignableTo(final Class<?> clazz) {
    return assignableTo(clazz).negate();
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated element has
   *         a name that with the given one.
   */
  public static <T extends Member> CompositePredicate<T> named(final String name) {
    return newComposition(new Predicate<Member>() {

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
   * @return a predicate that returns <code>true</code> if the evaluated method has
   *         a name that matches with the given one.
   * @since 2.7
   */
  public static <T extends Method> CompositePredicate<T> methodNamed(final String name) {
    return newComposition(new Predicate<Method>() {

      public boolean evaluate(Method element) {
        return element.getName().equals(name);
      }

      @Override
      public String toString() {
        return "Named " + name;
      }
    });
  }

  /**
   * @return a predicate that returns <code>true</code> if the evaluated element
   *         has the specified modifiers.
   */
  public static <T extends Member> CompositePredicate<T> withModifiers(final int... modifiers) {
    return newComposition(new Predicate<Member>() {

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
   * @return a predicate that returns <code>false</code> if the evaluated
   *         element does not have the specified modifiers.
   */
  public static <T extends Member> CompositePredicate<T> withoutModifiers(final int... modifiers) {
    return newComposition(new Predicate<Member>() {

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
   * @return a predicate that returns <code>true</code> if the evaluated method
   *         has the specified type as the return type.
   */
  public static CompositePredicate<Method> ofReturnType(final Class<?> returnType) {
    return newComposition(new Predicate<Method>() {

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
   * @return a predicate that returns <code>true</code> if the evaluated method
   *         has the return type assignable to the specified type.
   */
  public static CompositePredicate<Method> ofReturnTypeAssignableTo(final Class<?> returnType) {
    return newComposition(new Predicate<Method>() {

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
   * @return a predicate that returns <code>true</code> if the evaluated field
   *         is of an assignable type of the given one.
   */
  public static CompositePredicate<Field> ofType(final Class<?> type, final boolean assignable) {
    return newComposition(new Predicate<Field>() {

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
   * @return a predicate that returns <code>false</code> if the evaluated class
   *         has the specified modifiers.
   */
  public static CompositePredicate<Class<?>> classWithModifiers(final int... modifiers) {
    return newComposition(new Predicate<Class<?>>() {

      public boolean evaluate(Class<?> element) {
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
        return "Class with " + Arrays.toString(modifiers) + " modifiers";
      }
    });
  }

  /**
   * @return a predicate that returns <code>false</code> if the evaluated class
   *         does not have the specified modifiers.
   */
  public static CompositePredicate<Class<?>> classWithoutModifiers(int... modifiers) {
    return classWithModifiers(modifiers).negate();
  }

  /**
   * @param length
   * @return a predicate that returns <code>true</code> if the evaluated method
   *         has the specified number of parameters.
   */
  public static CompositePredicate<Method> withParameterLength(final int length) {
    return newComposition(new Predicate<Method>() {

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
   * @return a predicate that returns <code>true</code> if the evaluated method
   *         has the specified parameters.
   */
  public static CompositePredicate<Method> withParameters(final Class<?>... parameterTypes) {
    return newComposition(new Predicate<Method>() {

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
   * @return a predicate that returns <code>true</code> if the evaluated
   *         constructor has the specified parameters.
   */
  public static CompositePredicate<Constructor> constructorWithParameters(final Class<?>... parameterTypes) {
    return newComposition(new Predicate<Constructor>() {

      public boolean evaluate(Constructor element) {
        return Arrays.equals(element.getParameterTypes(), parameterTypes);
      }

      @Override
      public String toString() {
        return "With parameters " + Arrays.toString(parameterTypes);
      }
    });
  }

  public static CompositePredicate<Method> withDefaultValue() {
    return newComposition(new Predicate<Method>() {

      public boolean evaluate(Method element) {
        return element.getDefaultValue() != null;
      }

      @Override
      public String toString() {
        return "With default value";
      }
    });
  }

  /**
   * A predicate that returns <code>true</code> if a class is anonymous.
   */
  public static final CompositePredicate<Class<?>> ANONYMOUS = newComposition(new Predicate<Class<?>>() {

    public boolean evaluate(Class<?> element) {
      return element.isAnonymousClass();
    }

    @Override
    public String toString() {
      return "Anonymous";
    }
  });

  /**
   * A predicate that returns <code>true</code> if a class is not anonymous.
   */
  public static final CompositePredicate<Class<?>> NON_ANONYMOUS = ANONYMOUS.negate();

  public static final CompositePredicate<Method> methodDeclaredIn(final Class type) {
    return newComposition(new Predicate<Method>() {

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

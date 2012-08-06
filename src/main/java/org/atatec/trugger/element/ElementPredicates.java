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
package org.atatec.trugger.element;

import org.atatec.trugger.predicate.CompositePredicate;
import org.atatec.trugger.predicate.Predicate;
import org.atatec.trugger.predicate.Predicates;
import org.atatec.trugger.util.Utils;

import java.util.Arrays;

/**
 * An utility class for helping the use of {@link Predicate predicates} for
 * {@link Element elements}.
 * 
 * @author Marcelo Varella Barca Guimarães
 * @since 1.2
 */
public final class ElementPredicates {
  
  private ElementPredicates() {}
  
  /**
   * @return a predicate that returns <code>true</code> if an element is of the
   *         given type.
   * @since 2.0
   */
  public static CompositePredicate<Element> ofType(final Class<?> type) {
    return Predicates.is(new Predicate<Element>() {

      public boolean evaluate(Element element) {
        return type.equals(element.type());
      }

      @Override
      public String toString() {
        return "Of type " + type;
      }
    });
  }
  
  /**
   * @return a predicate that returns <code>true</code> if the element name
   *         equals one of the specified names.
   */
  public static CompositePredicate<Element> named(final String... elementNames) {
    Arrays.sort(elementNames);
    return Predicates.is(new Predicate<Element>() {

      public boolean evaluate(Element element) {
        return Arrays.binarySearch(elementNames, element.name()) >= 0;
      }

      @Override
      public String toString() {
        return "Named " + Arrays.toString(elementNames);
      }
    });
  }
  
  /**
   * A predicate that returns <code>true</code> if the element is writable.
   */
  public static final CompositePredicate<Element> WRITABLE = Predicates.is(new Predicate<Element>() {

    public boolean evaluate(Element element) {
      return element.isWritable();
    }

    @Override
    public String toString() {
      return "Writable";
    }
  });
  
  /**
   * A predicate that returns <code>false</code> if the element is writable.
   */
  public static final CompositePredicate<Element> NON_WRITABLE = WRITABLE.negate();
  
  /**
   * A predicate that returns <code>true</code> if the element is readable.
   */
  public static final CompositePredicate<Element> READABLE = Predicates.is(new Predicate<Element>() {

    public boolean evaluate(Element element) {
      return element.isReadable();
    }

    @Override
    public String toString() {
      return "Readable";
    }
  });
  
  /**
   * A predicate that returns <code>false</code> if the element is readable.
   */
  public static final CompositePredicate<Element> NON_READABLE = READABLE.negate();
  
  /**
   * @return a predicate that return <code>true</code> if the element is
   *         assignable to the given type.
   */
  public static CompositePredicate<Element> assignableTo(final Class<?> type) {
    return Predicates.is(new Predicate<Element>() {

      public boolean evaluate(Element element) {
        return Utils.areAssignable(type, element.type());
      }

      @Override
      public String toString() {
        return "Assignable to " + type.getName();
      }
    });
  }
  
  /**
   * A predicate that returns <code>true</code> if the element is
   * {@link Element#isSpecific() specific}.
   */
  public static final CompositePredicate<Element> SPECIFIC = Predicates.is(new Predicate<Element>() {

    public boolean evaluate(Element element) {
      return element.isSpecific();
    }

    @Override
    public String toString() {
      return "Specific";
    }

  });
  
  /**
   * A predicate that returns <code>true</code> if the element is not
   * {@link Element#isSpecific() specific}.
   */
  public static final CompositePredicate<Element> NON_SPECIFIC = SPECIFIC.negate();
  
}

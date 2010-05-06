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
package net.sf.trugger.predicate.impl;

import net.sf.trugger.predicate.CompositePredicate;
import net.sf.trugger.predicate.Predicate;

/**
 * The default implementation for the {@link CompositePredicate}.
 *
 * @author Marcelo Varella Barca Guimarães
 * @param <T>
 *          the type of the element evaluated by the predicate.
 */
public final class TruggerCompositePredicate<T> implements CompositePredicate<T> {

  private Predicate<? super T> compositePredicate;

  /**
   * Creates a new composite predicate using the given predicate for
   * composition.
   *
   * @param predicate
   *          the predicate for composition.
   */
  public TruggerCompositePredicate(Predicate<? super T> predicate) {
    this.compositePredicate = predicate;
  }

  public boolean evaluate(T element) {
    return compositePredicate.evaluate(element);
  }

  public CompositePredicate<T> and(final Predicate<? super T> predicate) {
    return new TruggerCompositePredicate<T>(new Predicate<T>() {

      public boolean evaluate(T element) {
        return compositePredicate.evaluate(element) && predicate.evaluate(element);
      }

      @Override
      public String toString() {
        return "(" + compositePredicate.toString() + ") AND (" + predicate.toString() + ")";
      }
    });
  }

  public CompositePredicate<T> nand(final Predicate<? super T> predicate) {
    return new TruggerCompositePredicate<T>(new Predicate<T>() {

      public boolean evaluate(T element) {
        return !(compositePredicate.evaluate(element) && predicate.evaluate(element));
      }

      @Override
      public String toString() {
        return "(" + compositePredicate.toString() + ") NAND (" + predicate.toString() + ")";
      }
    });
  }

  public CompositePredicate<T> nor(final Predicate<? super T> predicate) {
    return new TruggerCompositePredicate<T>(new Predicate<T>() {

      public boolean evaluate(T element) {
        return !(compositePredicate.evaluate(element) || predicate.evaluate(element));
      }

      @Override
      public String toString() {
        return "(" + compositePredicate.toString() + ") NOR (" + predicate.toString() + ")";
      }
    });
  }

  public CompositePredicate<T> or(final Predicate<? super T> predicate) {
    return new TruggerCompositePredicate<T>(new Predicate<T>() {

      public boolean evaluate(T element) {
        return compositePredicate.evaluate(element) || predicate.evaluate(element);
      }

      @Override
      public String toString() {
        return "(" + compositePredicate.toString() + ") OR (" + predicate.toString() + ")";
      }
    });
  }

  public CompositePredicate<T> xand(final Predicate<? super T> predicate) {
    return new TruggerCompositePredicate<T>(new Predicate<T>() {

      public boolean evaluate(T element) {
        // the XAND operation is true only if the elements are equals
        return compositePredicate.evaluate(element) == predicate.evaluate(element);
      }

      @Override
      public String toString() {
        return "(" + compositePredicate.toString() + ") XAND (" + predicate.toString() + ")";
      }
    });
  }

  public CompositePredicate<T> xor(final Predicate<? super T> predicate) {
    return new TruggerCompositePredicate<T>(new Predicate<T>() {

      public boolean evaluate(T element) {
        return compositePredicate.evaluate(element) != predicate.evaluate(element);
      }

      @Override
      public String toString() {
        return "(" + compositePredicate.toString() + ") XOR (" + predicate.toString() + ")";
      }
    });
  }

  public CompositePredicate<T> negate() {
    return new TruggerCompositePredicate<T>(new Predicate<T>() {

      public boolean evaluate(T element) {
        return !compositePredicate.evaluate(element);
      }

      @Override
      public String toString() {
        return "NOT(" + compositePredicate.toString() + ")";
      }
    });
  }

  @Override
  public String toString() {
    return compositePredicate.toString();
  }

}

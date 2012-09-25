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

package org.atatec.trugger.predicate;

/**
 * A base class for creating a CompositePredicate.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 4.1
 */
public abstract class BasePredicate<T> implements CompositePredicate<T> {

  public CompositePredicate<T> and(final Predicate predicate) {
    return new BasePredicate<T>() {

      public boolean evaluate(T element) {
        return BasePredicate.this.evaluate(element) && predicate.evaluate(element);
      }

      @Override
      public String toString() {
        return '(' + BasePredicate.this.toString() + ") AND (" + predicate.toString() + ')';
      }
    };
  }

  public CompositePredicate<T> nand(final Predicate predicate) {
    return new BasePredicate<T>() {

      public boolean evaluate(T element) {
        return !(BasePredicate.this.evaluate(element) && predicate.evaluate(element));
      }

      @Override
      public String toString() {
        return '(' + BasePredicate.this.toString() + ") NAND (" + predicate.toString() + ')';
      }
    };
  }

  public CompositePredicate<T> nor(final Predicate predicate) {
    return new BasePredicate<T>() {

      public boolean evaluate(T element) {
        return !(BasePredicate.this.evaluate(element) || predicate.evaluate(element));
      }

      @Override
      public String toString() {
        return '(' + BasePredicate.this.toString() + ") NOR (" + predicate.toString() + ')';
      }
    };
  }

  public CompositePredicate<T> or(final Predicate predicate) {
    return new BasePredicate<T>() {

      public boolean evaluate(T element) {
        return BasePredicate.this.evaluate(element) || predicate.evaluate(element);
      }

      @Override
      public String toString() {
        return '(' + BasePredicate.this.toString() + ") OR (" + predicate.toString() + ')';
      }
    };
  }

  public CompositePredicate<T> xand(final Predicate predicate) {
    return new BasePredicate<T>() {

      public boolean evaluate(T element) {
        // the XAND operation is true only if the elements are equals
        return BasePredicate.this.evaluate(element) == predicate.evaluate(element);
      }

      @Override
      public String toString() {
        return '(' + BasePredicate.this.toString() + ") XAND (" + predicate.toString() + ')';
      }
    };
  }

  public CompositePredicate<T> xor(final Predicate predicate) {
    return new BasePredicate<T>() {

      public boolean evaluate(T element) {
        return BasePredicate.this.evaluate(element) != predicate.evaluate(element);
      }

      @Override
      public String toString() {
        return '(' + BasePredicate.this.toString() + ") XOR (" + predicate.toString() + ')';
      }
    };
  }

  public CompositePredicate<T> negate() {
    return new BasePredicate<T>() {

      public boolean evaluate(T element) {
        return !BasePredicate.this.evaluate(element);
      }

      @Override
      public String toString() {
        return "NOT(" + BasePredicate.this.toString() + ')';
      }
    };
  }

}

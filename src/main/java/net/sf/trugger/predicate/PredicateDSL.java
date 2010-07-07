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
package net.sf.trugger.predicate;

import static net.sf.trugger.predicate.Predicates.newComposition;
import static net.sf.trugger.reflection.Reflection.reflect;

import java.util.LinkedList;
import java.util.List;

import net.sf.trugger.Result;
import net.sf.trugger.interception.InterceptionContext;
import net.sf.trugger.interception.InvocationTrackerInterceptor;
import net.sf.trugger.util.Utils;
import net.sf.trugger.validation.Validation;
import net.sf.trugger.validation.ValidationResult;

/**
 * This class represents a DSL to build a {@link Predicate}.
 * <p>
 * The DSL may also be created insite of an initializer (using an anonymous
 * class).
 * <p>
 * Example:
 *
 * <pre>
 * Predicate&lt;Element&gt; predicate = new PredicateDSL&lt;Element&gt;() {{
 *     expect(obj.name()).matches(&quot;\\w+\\d{1,2}&quot;);
 *     expect(obj.type()).equals(String.class);
 *     expect(obj.value()).differ(null);
 *   }};
 * Iteration.retainFrom(elementsCollection).elementsMatching(predicate);
 * </pre>
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.5
 * @param <E>
 *          The object type.
 */
public class PredicateDSL<E> implements Predicate<E> {

  /**
   * The proxy object for calling the methods while creating the DSL.
   */
  protected final E obj;

  private final Class<E> clazz;
  private InvocationTrackerInterceptor tracker = new InvocationTrackerInterceptor();
  private List<Predicate> evals = new LinkedList<Predicate>();

  /**
   * Creates a new instance of this class using the specified generic type. Make
   * sure you define the generic type when using this constructor.
   */
  protected PredicateDSL() {
    clazz = reflect().genericType("E").in(this);
    obj = (E) tracker.createProxy().over(clazz);
  }

  /**
   * Creates a new instance of this class using the given class as the type.
   *
   * @param clazz
   *          the type.
   */
  public PredicateDSL(Class<E> clazz) {
    this.clazz = clazz;
    obj = (E) tracker.createProxy().over(clazz);
  }

  /**
   * @return the proxy used for calling the methods while creating the DSL.
   */
  public final E obj() {
    return obj;
  }

  /**
   * Defines a boolean expression that must be <code>true</code>.
   */
  public void expect(Boolean value) {
    evals.add(new Evaluation(EvalType.EQUAL, tracker.trackedContexts(), true));
    tracker.track();
  }

  /**
   * Defines a boolean expression that must be <code>true</code>.
   */
  public void expect(boolean value) {
    evals.add(new Evaluation(EvalType.EQUAL, tracker.trackedContexts(), true));
    tracker.track();
  }

  /**
   * Defines a boolean expression that must be <code>false</code>.
   */
  public void dontExpect(boolean value) {
    evals.add(new Evaluation(EvalType.EQUAL, tracker.trackedContexts(), false));
    tracker.track();
  }

  /**
   * Defines a boolean expression that must be <code>false</code>.
   */
  public void dontExpect(Boolean value) {
    evals.add(new Evaluation(EvalType.EQUAL, tracker.trackedContexts(), false));
    tracker.track();
  }

  /**
   * Defines an expression for this predicate.
   *
   * @return the component for making the restriction.
   */
  public <V> Criteria<E, V> expect(V obj) {
    return new Criteria<E, V>() {

      public void equal(V value) {
        evals.add(new Evaluation(EvalType.EQUAL, tracker.trackedContexts(), value));
        tracker.track();
      }

      public void differ(V value) {
        evals.add(new Evaluation(EvalType.DIFFER, tracker.trackedContexts(), value));
        tracker.track();
      }

      public void greaterThan(V value) {
        evals.add(new Evaluation(EvalType.GREATER, tracker.trackedContexts(), value));
        tracker.track();
      }

      public void equalOrGreaterThan(V value) {
        evals.add(new Evaluation(EvalType.GREATER_OR_EQUAL, tracker.trackedContexts(), value));
        tracker.track();
      }

      public void lessThan(V value) {
        evals.add(new Evaluation(EvalType.LESS, tracker.trackedContexts(), value));
        tracker.track();
      }

      public void equalOrLessThan(V value) {
        evals.add(new Evaluation(EvalType.LESS_OR_EQUAL, tracker.trackedContexts(), value));
        tracker.track();
      }

      public void matches(String pattern) {
        evals.add(new Evaluation(EvalType.PATTERN, tracker.trackedContexts(), pattern));
        tracker.track();
      }

      public void invalid() {
        using(new Validation().validate().allElements()).invalid();
      }

      public void valid() {
        using(new Validation().validate().allElements()).valid();
      }

      public ValidationCriteria using(final Result<ValidationResult, Object> result) {
        return new ValidationCriteria() {

          public void invalid() {
            evals.add(new Evaluation(EvalType.INVALID, tracker.trackedContexts(), result));
            tracker.track();
          }

          public void valid() {
            evals.add(new Evaluation(EvalType.VALID, tracker.trackedContexts(), result));
            tracker.track();
          }
        };
      }

      public void diff(V value) {
        differ(value);
      }

      public void eq(V value) {
        equal(value);
      }

      public void ge(V value) {
        greaterOrEqual(value);
      }

      public void greaterOrEqual(V value) {
        equalOrGreaterThan(value);
      }

      public void gt(V value) {
        greaterThan(value);
      }

      public void le(V value) {
        lessOrEqual(value);
      }

      public void lessOrEqual(V value) {
        equalOrLessThan(value);
      }

      public void lt(V value) {
        lessThan(value);
      }

    };
  }

  /**
   * Defines an expression for this predicate.
   *
   * @return the component for making the restriction.
   */
  public <V> Criteria<E, V> dontExpect(V obj) {
    return new Criteria<E, V>() {

      public void equal(V value) {
        Evaluation evaluation = new Evaluation(EvalType.EQUAL, tracker.trackedContexts(), value);
        evals.add(newComposition(evaluation).negate());
        tracker.track();
      }

      public void differ(V value) {
        Evaluation evaluation = new Evaluation(EvalType.DIFFER, tracker.trackedContexts(), value);
        evals.add(newComposition(evaluation).negate());
        tracker.track();
      }

      public void greaterThan(V value) {
        Evaluation evaluation = new Evaluation(EvalType.GREATER, tracker.trackedContexts(), value);
        evals.add(newComposition(evaluation).negate());
        tracker.track();
      }

      public void equalOrGreaterThan(V value) {
        Evaluation evaluation = new Evaluation(EvalType.GREATER_OR_EQUAL, tracker.trackedContexts(), value);
        evals.add(newComposition(evaluation).negate());
        tracker.track();
      }

      public void lessThan(V value) {
        Evaluation evaluation = new Evaluation(EvalType.LESS, tracker.trackedContexts(), value);
        evals.add(newComposition(evaluation).negate());
        tracker.track();
      }

      public void equalOrLessThan(V value) {
        Evaluation evaluation = new Evaluation(EvalType.LESS_OR_EQUAL, tracker.trackedContexts(), value);
        evals.add(newComposition(evaluation).negate());
        tracker.track();
      }

      public void matches(String pattern) {
        Evaluation evaluation = new Evaluation(EvalType.PATTERN, tracker.trackedContexts(), pattern);
        evals.add(newComposition(evaluation).negate());
        tracker.track();
      }

      public void invalid() {
        using(new Validation().validate().allElements()).invalid();
      }

      public void valid() {
        using(new Validation().validate().allElements()).valid();
      }

      public ValidationCriteria using(final Result<ValidationResult, Object> result) {
        return new ValidationCriteria() {

          public void invalid() {
            Evaluation evaluation = new Evaluation(EvalType.INVALID, tracker.trackedContexts(), result);
            evals.add(newComposition(evaluation).negate());
            tracker.track();
          }

          public void valid() {
            Evaluation evaluation = new Evaluation(EvalType.VALID, tracker.trackedContexts(), result);
            evals.add(newComposition(evaluation).negate());
            tracker.track();
          }
        };
      }

      public void diff(V value) {
        differ(value);
      }

      public void eq(V value) {
        equal(value);
      }

      public void ge(V value) {
        greaterOrEqual(value);
      }

      public void greaterOrEqual(V value) {
        equalOrGreaterThan(value);
      }

      public void gt(V value) {
        greaterThan(value);
      }

      public void le(V value) {
        lessOrEqual(value);
      }

      public void lessOrEqual(V value) {
        equalOrLessThan(value);
      }

      public void lt(V value) {
        lessThan(value);
      }

    };
  }

  public boolean evaluate(E element) {
    for (Predicate predicate : evals) {
      if (!predicate.evaluate(element)) {
        return false;
      }
    }
    return true;
  };

  private enum EvalType {
    EQUAL {
      public boolean eval(Object referenceValue, Object objectValue) {
        return Utils.areEquals(objectValue, referenceValue);
      }
    },
    DIFFER {
      public boolean eval(Object referenceValue, Object objectValue) {
        return !Utils.areEquals(objectValue, referenceValue);
      }
    },
    LESS {
      public boolean eval(Object referenceValue, Object objectValue) {
        return ((Comparable) objectValue).compareTo(referenceValue) < 0;
      }
    },
    LESS_OR_EQUAL {
      public boolean eval(Object referenceValue, Object objectValue) {
        return ((Comparable) objectValue).compareTo(referenceValue) <= 0;
      }
    },
    GREATER {
      public boolean eval(Object referenceValue, Object objectValue) {
        return ((Comparable) objectValue).compareTo(referenceValue) > 0;
      }
    },
    GREATER_OR_EQUAL {
      public boolean eval(Object referenceValue, Object objectValue) {
        return ((Comparable) objectValue).compareTo(referenceValue) >= 0;
      }
    },
    PATTERN {
      public boolean eval(Object referenceValue, Object objectValue) {
        return objectValue != null ? objectValue.toString().matches((String) referenceValue) : false;
      }
    },
    VALID {
      public boolean eval(Object referenceValue, Object objectValue) {
        return Predicates.validUsing((Result<ValidationResult, Object>) referenceValue).evaluate(objectValue);
      }
    },
    INVALID {
      public boolean eval(Object referenceValue, Object objectValue) {
        return Predicates.invalidUsing((Result<ValidationResult, Object>) referenceValue).evaluate(objectValue);
      }
    };

    abstract boolean eval(Object referenceValue, Object objectValue);
  }

  private class Evaluation implements Predicate {

    List<InterceptionContext> contexts;
    Object referenceValue;
    EvalType eval;

    public Evaluation(EvalType eval, List<InterceptionContext> contexts, Object value) {
      this.contexts = contexts;
      this.referenceValue = value;
      this.eval = eval;
    }

    public boolean evaluate(Object object) {
      Object objectValue = InvocationTrackerInterceptor.resolve(object, contexts);
      return eval.eval(referenceValue, objectValue);
    }

  }

}

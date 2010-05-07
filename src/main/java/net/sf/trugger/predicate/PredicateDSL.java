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

import static net.sf.trugger.reflection.Reflection.invoke;
import static net.sf.trugger.reflection.Reflection.reflect;

import java.util.LinkedList;
import java.util.List;

import net.sf.trugger.interception.InterceptionContext;
import net.sf.trugger.interception.InvocationTrackerInterceptor;
import net.sf.trugger.util.Utils;

/**
 * @author Marcelo Varella Barca Guimarães
 * @since 2.5
 */
public class PredicateDSL<E> implements Predicate<E> {

  private final Class<E> clazz;
  protected final E obj;

  private InvocationTrackerInterceptor tracker = new InvocationTrackerInterceptor();
  private List<Predicate> evals = new LinkedList<Predicate>();

  public PredicateDSL() {
    clazz = reflect().genericType("E").in(this);
    obj = tracker.createProxy().over(clazz);
  }

  public E obj() {
    return obj;
  }

  /**
   * Defines
   *
   * @param <V>
   * @param value
   * @return
   */
  public <V> Criteria<E, V> expect(V value) {
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

      public void greaterThanOrEqual(V value) {
        evals.add(new Evaluation(EvalType.GREATER_OR_EQUAL, tracker.trackedContexts(), value));
        tracker.track();
      }

      public void lessThan(V value) {
        evals.add(new Evaluation(EvalType.LESS, tracker.trackedContexts(), value));
        tracker.track();
      }

      public void lessThanOrEqual(V value) {
        evals.add(new Evaluation(EvalType.LESS_OR_EQUAL, tracker.trackedContexts(), value));
        tracker.track();
      }

      public void matches(String pattern) {
        evals.add(new Evaluation(EvalType.PATTERN, tracker.trackedContexts(), pattern));
        tracker.track();
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

  public interface Criteria<E, V> {

    void equal(V value);

    void differ(V value);

    void lessThan(V value);

    void lessThanOrEqual(V value);

    void greaterThan(V value);

    void greaterThanOrEqual(V value);

    void matches(String pattern);

  }

  private enum EvalType {
    EQUAL, DIFFER, LESS, LESS_OR_EQUAL, GREATER, GREATER_OR_EQUAL, PATTERN
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
      Object objValue = object;
      for (InterceptionContext context : contexts) {
        objValue = invoke(context.method).in(objValue).withArgs(context.args);
      }
      switch (eval) {
        case EQUAL:
          return Utils.areEquals(objValue, referenceValue);
        case DIFFER:
          return !Utils.areEquals(objValue, referenceValue);
        case LESS:
          return ((Comparable) objValue).compareTo(referenceValue) < 0;
        case LESS_OR_EQUAL:
          return ((Comparable) objValue).compareTo(referenceValue) <= 0;
        case GREATER:
          return ((Comparable) objValue).compareTo(referenceValue) > 0;
        case GREATER_OR_EQUAL:
          return ((Comparable) objValue).compareTo(referenceValue) >= 0;
        case PATTERN:
          return String.valueOf(objValue).matches((String) referenceValue);
      }
      throw new Error();
    }
  }

}

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
import net.sf.trugger.interception.Interceptor;
import net.sf.trugger.util.Utils;

/**
 * @author Marcelo Varella Barca Guimarães
 * @since 2.5
 *
 * @todo chain methods
 */
public class PredicateDSL<E> implements Predicate<E> {

  private final Class<E> clazz;
  protected final E obj;

  private _Interceptor interceptor = new _Interceptor();
  private List<Predicate> evals = new LinkedList<Predicate>();

  public PredicateDSL() {
    clazz = reflect().genericType("E").in(this);
    if(clazz.isInterface()) {
      obj = interceptor.createProxy().implementing(clazz).withoutTarget();
    } else {
      obj = interceptor.createProxy().extending(clazz);
    }
  }

  public E obj() {
    return obj;
  }

  public <V> Criteria<E, V> expect(V value) {
    return new Criteria<E, V>() {

      public PredicateDSL<E> equal(V value) {
        evals.add(new Evaluation(EvalType.EQUAL, interceptor.currentContext, value));
        return PredicateDSL.this;
      }

      public PredicateDSL<E> differ(V value) {
        evals.add(new Evaluation(EvalType.DIFFER, interceptor.currentContext, value));
        return PredicateDSL.this;
      }

      public PredicateDSL<E> greaterThan(V value) {
        evals.add(new Evaluation(EvalType.GREATER, interceptor.currentContext, value));
        return PredicateDSL.this;
      }

      public PredicateDSL<E> greaterThanOrEqual(V value) {
        evals.add(new Evaluation(EvalType.GREATER_OR_EQUAL, interceptor.currentContext, value));
        return PredicateDSL.this;
      }

      public PredicateDSL<E> lessThan(V value) {
        evals.add(new Evaluation(EvalType.LESS, interceptor.currentContext, value));
        return PredicateDSL.this;
      }

      public PredicateDSL<E> lessThanOrEqual(V value) {
        evals.add(new Evaluation(EvalType.LESS_OR_EQUAL, interceptor.currentContext, value));
        return PredicateDSL.this;
      }

    };
  }

  public boolean evaluate(E element) {
    for (Predicate predicate : evals) {
      if(!predicate.evaluate(element)) {
        return false;
      }
    }
    return true;
  };

  private class _Interceptor extends Interceptor {

    private InterceptionContext currentContext;

    @Override
    protected Object intercept() throws Throwable {
      currentContext = context();
      return null;
    }
  }

  public interface Criteria<E, V> {

    PredicateDSL<E> equal(V value);

    PredicateDSL<E> differ(V value);

    PredicateDSL<E> lessThan(V value);

    PredicateDSL<E> lessThanOrEqual(V value);

    PredicateDSL<E> greaterThan(V value);

    PredicateDSL<E> greaterThanOrEqual(V value);

  }

  private enum EvalType {
    EQUAL, DIFFER, LESS, LESS_OR_EQUAL, GREATER, GREATER_OR_EQUAL
  }

  private class Evaluation implements Predicate {
    InterceptionContext context;
    Object referenceValue;
    EvalType eval;

    public Evaluation(EvalType eval, InterceptionContext context, Object value) {
      this.context = context;
      this.referenceValue = value;
      this.eval = eval;
    }

    public boolean evaluate(Object object) {
      Object objValue = invoke(context.method).in(object).withArgs(context.args);
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
      }
      throw new Error();
    }
  }

}

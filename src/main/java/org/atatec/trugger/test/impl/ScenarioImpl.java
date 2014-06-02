package org.atatec.trugger.test.impl;

import org.atatec.trugger.test.Scenario;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Marcelo Guimarães
 */
public class ScenarioImpl<T> implements Scenario<T> {

  private final T target;

  public ScenarioImpl(T target) {
    this.target = target;
  }

  @Override
  public Scenario<T> when(Consumer<? super T> operation) {
    operation.accept(target);
    return this;
  }

  @Override
  public Scenario<T> then(Consumer operation, Consumer test) {
    try {
      operation.accept(target);
      test.accept(null);
    } catch (Throwable t) {
      test.accept(t);
    }
    return this;
  }

  @Override
  public Scenario<T> the(Function function, Consumer test) {
    Object result = function.apply(target);
    test.accept(result);
    return this;
  }

  public Scenario<T> each(Consumer test) {
    ((Iterable) target).forEach(test);
    return this;
  }

}

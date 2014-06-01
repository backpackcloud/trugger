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
  public Scenario<T> then(Consumer operation, Consumer tests) {
    try {
      operation.accept(target);
      tests.accept(null);
    } catch (Throwable t) {
      tests.accept(t);
    }
    return this;
  }

  @Override
  public Scenario<T> thenIt(Consumer<? super T> tests) {
    tests.accept(target);
    return this;
  }

  @Override
  public Scenario<T> the(Function function, Consumer tests) {
    Object result = function.apply(target);
    tests.accept(result);
    return this;
  }

  @Override
  public <E> Scenario<T> each(Class<E> type, Consumer<? super E> tests) {
    ((Iterable) target).forEach(tests);
    return this;
  }

}

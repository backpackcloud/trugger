package org.atatec.trugger.test.impl;

import org.atatec.trugger.test.ObjectScenario;

import java.util.function.Consumer;

/**
 * @author Marcelo Guimarães
 */
public class ObjectScenarioImpl<T> implements ObjectScenario<T> {

  private final T target;

  public ObjectScenarioImpl(T target) {
    this.target = target;
  }

  @Override
  public ObjectScenario<T> when(Consumer condition) {
    condition.accept(target);
    return this;
  }

  @Override
  public ObjectScenario<T> thenIt(Consumer assertion) {
    assertion.accept(target);
    return this;
  }

  @Override
  public ObjectScenario<T> and(Consumer assertion) {
    assertion.accept(target);
    return this;
  }

}

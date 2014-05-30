package org.atatec.trugger.test.impl;

import org.atatec.trugger.test.IterableScenario;
import org.atatec.trugger.test.ObjectScenario;

import java.util.function.Consumer;

/**
 * @author Marcelo Guimarães
 */
public class IterableScenarioImpl<T> implements IterableScenario<T> {

  private final ObjectScenario<Iterable<T>> objectScenario;
  private final Iterable<T> target;

  public IterableScenarioImpl(Iterable<T> target) {
    this.target = target;
    this.objectScenario = new ObjectScenarioImpl<>(target);
  }

  @Override
  public IterableScenario<T> when(Consumer condition) {
    objectScenario.when(condition);
    return this;
  }

  @Override
  public IterableScenario<T> thenIt(Consumer assertion) {
    objectScenario.thenIt(assertion);
    return this;
  }

  @Override
  public IterableScenario<T> and(Consumer assertion) {
    objectScenario.and(assertion);
    return this;
  }

  @Override
  public IterableScenario<T> whenEach(Consumer<T> condition) {
    target.forEach(condition);
    return this;
  }

  @Override
  public IterableScenario<T> thenEach(Consumer<T> assertion) {
    target.forEach(assertion);
    return this;
  }

  @Override
  public IterableScenario<T> andEach(Consumer<T> consumer) {
    target.forEach(consumer);
    return this;
  }
}

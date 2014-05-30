package org.atatec.trugger.test.impl;

import org.atatec.trugger.test.CollectionScenario;
import org.atatec.trugger.test.ObjectScenario;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * @author Marcelo Guimarães
 */
public class CollectionScenarioImpl<T> implements CollectionScenario<T> {

  private final ObjectScenario<Collection<T>> objectScenario;
  private final Collection<T> target;

  public CollectionScenarioImpl(Collection<T> target) {
    this.target = target;
    this.objectScenario = new ObjectScenarioImpl<>(target);
  }

  @Override
  public CollectionScenario<T> when(Consumer condition) {
    objectScenario.when(condition);
    return this;
  }

  @Override
  public CollectionScenario<T> thenIt(Consumer assertion) {
    objectScenario.thenIt(assertion);
    return this;
  }

  @Override
  public CollectionScenario<T> and(Consumer assertion) {
    objectScenario.and(assertion);
    return this;
  }

  @Override
  public CollectionScenario<T> whenEach(Consumer<T> condition) {
    target.forEach(condition);
    return this;
  }

  @Override
  public CollectionScenario<T> thenEach(Consumer<T> assertion) {
    target.forEach(assertion);
    return this;
  }

  @Override
  public CollectionScenario<T> andEach(Consumer<T> consumer) {
    target.forEach(consumer);
    return this;
  }
}

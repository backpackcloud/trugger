package org.atatec.trugger.test;

import java.util.function.Consumer;

/**
 * Interface that defines a common set of methods to use
 * BDD without a specific framework. This is a helper interface
 * that can be extended
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public interface IterableScenario<T> extends ObjectScenario<Iterable<T>> {

  @Override
  IterableScenario<T> when(Consumer<Iterable<T>> condition);

  @Override
  IterableScenario<T> thenIt(Consumer<Iterable<T>> assertion);

  @Override
  IterableScenario<T> and(Consumer<Iterable<T>> consumer);

  IterableScenario<T> whenEach(Consumer<T> condition);

  IterableScenario<T> thenEach(Consumer<T> assertion);

  IterableScenario<T> andEach(Consumer<T> consumer);

}

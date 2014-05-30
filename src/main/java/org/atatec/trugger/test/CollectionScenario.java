package org.atatec.trugger.test;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Interface that defines a common set of methods to use
 * BDD without a specific framework. This is a helper interface
 * that can be extended
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public interface CollectionScenario<T> extends ObjectScenario<Collection<T>> {

  @Override
  CollectionScenario<T> when(Consumer<Collection<T>> condition);

  @Override
  CollectionScenario<T> thenIt(Consumer<Collection<T>> assertion);

  @Override
  CollectionScenario<T> and(Consumer<Collection<T>> consumer);

  CollectionScenario<T> whenEach(Consumer<T> condition);

  CollectionScenario<T> thenEach(Consumer<T> assertion);

  CollectionScenario<T> andEach(Consumer<T> assertion);

}

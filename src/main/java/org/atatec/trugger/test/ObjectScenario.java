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
public interface ObjectScenario<T> {

  ObjectScenario<T> when(Consumer<T> condition);

  ObjectScenario<T> thenIt(Consumer<T> assertion);

  ObjectScenario<T> and(Consumer<T> consumer);

}

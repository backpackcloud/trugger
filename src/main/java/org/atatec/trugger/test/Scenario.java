package org.atatec.trugger.test;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Interface that defines a common set of methods to use
 * BDD without a specific framework. This is a helper interface
 * that can be extended
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public interface Scenario<T> {

  Scenario<T> when(Consumer<T> operation);

  Scenario<T> then(Consumer<T> operation, Consumer<? extends Throwable> tests);

  Scenario<T> thenIt(Consumer<? super T> tests);

  Scenario<T> the(Function<T, ?> function, Consumer<?> tests);

  <E> Scenario<T> each(Class<E> type, Consumer<? super E> tests);

}

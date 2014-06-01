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

  Scenario<T> when(Consumer<? super T> operation);

  Scenario<T> then(Consumer<? super T> operation,
                   Consumer<? extends Throwable> tests);

  Scenario<T> the(Function<T, ?> function, Consumer<?> tests);

  <E> Scenario<T> each(Class<E> type, Consumer<? super E> tests);

  default Scenario<T> thenIt(Consumer<? super T> tests) {
    return when(tests);
  }

  default Scenario<T> and(Consumer<? super T> consumer) {
    return when(consumer);
  }

  default Scenario<T> it(Consumer<? super T> tests) {
    return when(tests);
  }

}

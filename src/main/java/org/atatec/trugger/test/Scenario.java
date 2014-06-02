package org.atatec.trugger.test;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Interface that defines a common set of methods to define test scenarios for
 * a target object.
 *
 * @author Marcelo Guimarães
 * @see org.atatec.trugger.test.TestScenario#given(Object)
 * @since 5.1
 */
public interface Scenario<T> {

  /**
   * Defines something to do with the target.
   *
   * @param operation the operation to do with the target
   * @return a reference to this object.
   */
  Scenario<T> when(Consumer<? super T> operation);

  /**
   * Defines an operation that will throw an exception.
   *
   * @param operation the operation the operation to do with the target
   * @param test      the test to do with the raised exception (if no exception
   *                  is thrown, a <code>null</code> value will be given to this
   *                  consumer
   * @return a reference to this object.
   * @see Should#raise(Class)
   */
  Scenario<T> then(Consumer<? super T> operation,
                   Consumer<? extends Throwable> test);

  /**
   * Defines a test for some target operation that returns a value.
   *
   * @param function the operation to do with the target
   * @param test     the test to do with the value returned by the given
   *                 function
   * @return a reference to this object
   * @see org.atatec.trugger.test.Should
   */
  Scenario<T> the(Function<T, ?> function, Consumer<?> test);

  /**
   * Defines a test for each element of the target. This requires an
   * {@link java.lang.Iterable} target.
   *
   * @param test the test to do with the values in the targegt
   * @return a reference to this object.
   */
  Scenario<T> each(Consumer test);

  /**
   * Defines a test for each element of the target. This requires
   * an {@link java.lang.Iterable} target.
   *
   * @param type the type of the elements
   * @param test the test to do with the values in the target
   * @return a reference to this object.
   */
  default <E> Scenario<T> each(Class<E> type, Consumer<? super E> test) {
    return each(test);
  }

  /**
   * Defines a set of tests to do with the target.
   *
   * @param tests the tests to do with the target
   * @return a reference to this object.
   */
  default Scenario<T> thenIt(Consumer<? super T> tests) {
    return when(tests);
  }

  /**
   * Defines operations to execute as {@link #when(java.util.function.Consumer)}
   * or {@link #thenIt(java.util.function.Consumer)}.
   *
   * @param consumer the operation to execute with the target object.
   * @return a reference to this object
   */
  default Scenario<T> and(Consumer<? super T> consumer) {
    return when(consumer);
  }

  /**
   * Defines a test to do with the target.
   *
   * @param tests
   * @return
   */
  default Scenario<T> it(Consumer<? super T> tests) {
    return when(tests);
  }

  /**
   * Defines a test to do with a value.
   *
   * @param value the value to test
   * @param test  the test to do with the value
   * @return a reference to this object
   */
  default Scenario<T> the(Object value, Consumer test) {
    test.accept(value);
    return this;
  }

}

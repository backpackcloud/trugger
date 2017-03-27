package tools.devnull.trugger.util;

import tools.devnull.trugger.util.impl.PredicateMappingFunctionImpl;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A Function that can be mapped to a Predicate to only execute if the
 * {@link Predicate#test(Object)} is {@code true}.
 *
 * @since 5.3
 */
public interface PredicateMappingFunction<T, R> extends Function<T, R> {

  /**
   * Maps a function to a predicate.
   *
   * @param function the function to map
   * @return a component to define the predicate
   */
  Mapper<T, R> use(Function<? super T, ? extends R> function);

  /**
   * Interface that maps a function to a predicate.
   */
  interface Mapper<T, R> {
    PredicateMappingFunction<T, R> when(Predicate<? super T> condition);
  }

  static <T, R> PredicateMappingFunction<T, R> byDefault(Function<? super T, ? extends R> function) {
    return new PredicateMappingFunctionImpl<>(null, t -> true, function);
  }

  static <T, R> PredicateMappingFunction<T, R> begin() {
    return new PredicateMappingFunctionImpl<>(o -> {
      throw new IllegalArgumentException();
    }, t -> false, null);
  }

}

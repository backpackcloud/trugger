package tools.devnull.trugger.util.impl;

import tools.devnull.trugger.util.PredicateMappingFunction;

import java.util.function.Function;
import java.util.function.Predicate;

public class PredicateMappingFunctionImpl<T, R> implements PredicateMappingFunction<T, R> {

  private final Function<? super T, ? extends R> defaultFunction;
  private final Predicate<? super T> predicate;
  private final Function<? super T, ? extends R> function;

  public PredicateMappingFunctionImpl(Function<? super T, ? extends R> defaultFunction,
                               Predicate<? super T> predicate,
                               Function<? super T, ? extends R> function) {
    this.defaultFunction = defaultFunction;
    this.predicate = predicate;
    this.function = function;
  }

  @Override
  public Mapper<T, R> use(Function<? super T, ? extends R> function) {
    return condition -> new PredicateMappingFunctionImpl<>(this, condition, function);
  }

  @Override
  public R apply(T t) {
    if (predicate.test(t)) {
      return function.apply(t);
    }
    return defaultFunction.apply(t);
  }

}

package org.atatec.trugger.test;

import org.atatec.trugger.loader.ImplementationLoader;

import java.util.Collection;

/**
 * @author Marcelo Guimarães
 * @since 5.1
 */
public class Scenario {

  private static final ScenarioFactory factory;

  static {
    factory = ImplementationLoader.get(ScenarioFactory.class);
  }

  public static <T> ObjectScenario<T> given(T object) {
    return factory.createObjectScenario(object);
  }

  public static <T> IterableScenario<T> given(Iterable<T> object) {
    return factory.createIterableScenario(object);
  }

  public static <T> CollectionScenario<T> given(Collection<T> object) {
    return factory.createCollectionScenario(object);
  }

}

package org.atatec.trugger.test;

import org.atatec.trugger.loader.ImplementationLoader;

/**
 * A class to create scenarios for testing.
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public class TestScenario {

  private static final ScenarioFactory factory;

  static {
    factory = ImplementationLoader.get(ScenarioFactory.class);
  }

  /**
   * Start defining a new {@link Scenario} based on the given target.
   *
   * @param object
   * @param <T>
   * @return
   */
  public static <T> Scenario<T> given(T object) {
    return factory.createObjectScenario(object);
  }

}

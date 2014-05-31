package org.atatec.trugger.test;

import org.atatec.trugger.loader.ImplementationLoader;

/**
 * @author Marcelo Guimarães
 * @since 5.1
 */
public class TestScenario {

  private static final ScenarioFactory factory;

  static {
    factory = ImplementationLoader.get(ScenarioFactory.class);
  }

  public static <T> Scenario<T> given(T object) {
    return factory.createObjectScenario(object);
  }

}

package org.atatec.trugger.test.impl;

import org.atatec.trugger.test.Scenario;
import org.atatec.trugger.test.ScenarioFactory;

/**
 * @author Marcelo Guimarães
 */
public class TruggerScenarioFactory implements ScenarioFactory {

  @Override
  public Scenario createObjectScenario(Object target) {
    return new ScenarioImpl<>(target);
  }

}

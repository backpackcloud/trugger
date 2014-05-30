package org.atatec.trugger.test.impl;

import org.atatec.trugger.test.CollectionScenario;
import org.atatec.trugger.test.IterableScenario;
import org.atatec.trugger.test.ObjectScenario;
import org.atatec.trugger.test.ScenarioFactory;

import java.util.Collection;

/**
 * @author Marcelo Guimarães
 */
public class TruggerScenarioFactory implements ScenarioFactory {

  @Override
  public ObjectScenario createObjectScenario(Object target) {
    return new ObjectScenarioImpl<>(target);
  }

  @Override
  public IterableScenario createIterableScenario(Iterable target) {
    return new IterableScenarioImpl(target);
  }

  @Override
  public CollectionScenario createCollectionScenario(Collection target) {
    return new CollectionScenarioImpl(target);
  }

}

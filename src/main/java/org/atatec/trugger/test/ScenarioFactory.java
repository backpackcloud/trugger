package org.atatec.trugger.test;

import java.util.Collection;

/**
 *
 * @author Marcelo Guimarães
 * @since 5.1
 */
public interface ScenarioFactory {

  ObjectScenario createObjectScenario(Object target);

  IterableScenario createIterableScenario(Iterable target);

  CollectionScenario createCollectionScenario(Collection target);

}

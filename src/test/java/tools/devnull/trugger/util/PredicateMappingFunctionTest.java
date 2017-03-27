package tools.devnull.trugger.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PredicateMappingFunctionTest {

  @Mock
  private Function functionA;
  @Mock
  private Function functionB;
  @Mock
  private Function functionC;
  @Mock
  private Function functionD;
  @Mock
  private Function defaultFunction;

  @Mock
  private Predicate predicateA;
  @Mock
  private Predicate predicateB;
  @Mock
  private Predicate predicateC;
  @Mock
  private Predicate predicateD;

  private Object object = new Object();

  @Test
  public void testWithBegin() {
    when(predicateB.test(object)).thenReturn(true);

    initialize(PredicateMappingFunction.begin()).apply(object);

    verify(predicateA, never()).test(object);
    verify(predicateB, times(1)).test(object);
    verify(predicateC, times(1)).test(object);
    verify(predicateD, times(1)).test(object);

    verify(functionA, never()).apply(object);
    verify(functionB, times(1)).apply(object);
    verify(functionC, never()).apply(object);
    verify(functionD, never()).apply(object);
  }

  @Test
  public void testWithDefault() {
    initialize(PredicateMappingFunction.byDefault(defaultFunction)).apply(object);

    verify(predicateA, times(1)).test(object);
    verify(predicateB, times(1)).test(object);
    verify(predicateC, times(1)).test(object);
    verify(predicateD, times(1)).test(object);

    verify(functionA, never()).apply(object);
    verify(functionB, never()).apply(object);
    verify(functionC, never()).apply(object);
    verify(functionD, never()).apply(object);

    verify(defaultFunction).apply(object);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithNoMatch() {
    initialize(PredicateMappingFunction.begin()).apply(object);
  }

  private PredicateMappingFunction initialize(PredicateMappingFunction instance) {
    return instance.use(functionA).when(predicateA)
        .use(functionB).when(predicateB)
        .use(functionC).when(predicateC)
        .use(functionD).when(predicateD);
  }

}

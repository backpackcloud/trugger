package tools.devnull.trugger.factory;

import org.junit.Test;
import tools.devnull.kodo.TestScenario;
import tools.devnull.trugger.util.factory.Context;
import tools.devnull.trugger.util.factory.DefaultContext;

import java.lang.reflect.Parameter;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DefaultContextTest {

  @Test
  public void testMessages() {
    Function<Parameter, Object> functionA = mock(Function.class);
    Predicate<Parameter> conditionA = mock(Predicate.class);
    Object resultA = new Object();
    when(functionA.apply(any())).thenReturn(resultA);
    when(conditionA.test(any())).thenReturn(false);

    Function<Parameter, Object> functionB = mock(Function.class);
    Predicate<Parameter> conditionB = mock(Predicate.class);
    Object resultB = new Object();
    when(functionB.apply(any())).thenReturn(resultB);
    when(conditionB.test(any())).thenReturn(true);

    TestScenario.given(new DefaultContext())

    context.use(functionA).when(conditionA).
        use(functionB).when(conditionB);

    // don't need an actual parameter since everything here is a mock
    context.resolve(null);
  }

}

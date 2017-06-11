package tools.devnull.trugger;

import org.junit.Test;
import tools.devnull.kodo.Spec;

import static tools.devnull.kodo.Expectation.to;

public class OptionalTest {

  private Object value = new Object();

  @Test
  public void testValue() {
    Spec.given(Optional.of(value))
      .expect(Optional::value, to().be(value));

    Spec.given(Optional.empty())
        .expect(Optional::value, to().beNull(Object.class));
  }

}

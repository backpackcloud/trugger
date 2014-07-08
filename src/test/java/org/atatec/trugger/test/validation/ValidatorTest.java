package org.atatec.trugger.test.validation;

import org.atatec.trugger.validation.Validator;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Marcelo Guimarães
 */
public class ValidatorTest {

  private Validator TRUE = (object) -> true;
  private Validator FALSE = (object) -> false;

  private void assertValid(Validator validator) {
    assertTrue(validator.isValid(new Object()));
  }

  private void assertInvalid(Validator validator) {
    assertFalse(validator.isValid(new Object()));
  }

  @Test
  public void testAndOperation() {
    assertValid(TRUE.and(TRUE));
    assertInvalid(TRUE.and(FALSE));
    assertInvalid(FALSE.and(FALSE));
    assertInvalid(FALSE.and(TRUE));
  }

  @Test
  public void testOrOperation() {
    assertValid(TRUE.or(TRUE));
    assertValid(TRUE.or(FALSE));
    assertInvalid(FALSE.or(FALSE));
    assertValid(FALSE.or(TRUE));
  }

}

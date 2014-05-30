package org.atatec.trugger.test;

import java.util.Objects;

/**
 * @author Marcelo Guimarães
 */
class Assertions {

  private static void fail() {
    throw new AssertionError();
  }

  public static void assertTrue(boolean condition) {
    if (!condition) {
      fail();
    }
  }

  public static void assertFalse(boolean condition) {
    if (condition) {
      fail();
    }
  }

  public static void assertNull(Object object) {
    assertTrue(object == null);
  }

  public static void assertNotNull(Object object) {
    assertTrue(object != null);
  }

  public static void assertEquals(Object o1, Object o2) {
    assertTrue(Objects.equals(o1, o2));
  }

  public static void assertNotEquals(Object o1, Object o2) {
    assertTrue(!Objects.equals(o1, o2));
  }

}

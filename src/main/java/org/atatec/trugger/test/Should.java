package org.atatec.trugger.test;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.junit.Assert.*;

/**
 * @author Marcelo Guimarães
 */
public class Should {

  private Should() {

  }

  public static Consumer<Boolean> BE_TRUE = beTrue();

  public static Consumer<Boolean> BE_FALSE = beFalse();

  public static Consumer BE_NULL = beNull();

  public static Consumer NOT_BE_NULL = notBeNull();

  public static Consumer<Collection> BE_EMPTY = beEmpty();

  public static Consumer<Collection> NOT_BE_EMPTY = notBeEmpty();

  public static Consumer<Boolean> beTrue() {
    return be(true);
  }

  public static Consumer<Boolean> beFalse() {
    return be(false);
  }

  public static Consumer beNull() {
    return (obj) -> assertNull(obj);
  }

  public static Consumer notBeNull() {
    return (obj) -> assertNotNull(obj);
  }

  public static Consumer be(Object value) {
    return (obj) -> assertEquals(value, obj);
  }

  public static Consumer notBe(Object value) {
    return (obj) -> assertNotEquals(value, obj);
  }

  public static Consumer be(Predicate predicate) {
    return (obj) -> assertTrue(predicate.test(obj));
  }

  public static Consumer notBe(Predicate predicate) {
    return (obj) -> assertFalse(predicate.test(obj));
  }

  public static Consumer<Collection> beEmpty() {
    return (collection) -> assertTrue(collection.isEmpty());
  }

  public static Consumer<Collection> notBeEmpty() {
    return (collection) -> assertFalse(collection.isEmpty());
  }

  public static Consumer raise(Class<? extends Throwable> exception) {
    return (error) -> {
      if (exception != null) {
        assertTrue(exception.isAssignableFrom(error.getClass()));
      } else {
        throw new AssertionError("No Exception was thrown.");
      }
    };
  }

}

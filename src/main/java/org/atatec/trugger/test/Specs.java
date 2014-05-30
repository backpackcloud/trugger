package org.atatec.trugger.test;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.atatec.trugger.test.Assertions.*;

/**
 * @author Marcelo Guimarães
 */
public class Specs {

  private Specs() {

  }

  public static <T> Consumer<T> should(Predicate<? super T> predicate) {
    return (obj) -> assertTrue(predicate.test(obj));
  }

  public static <T> Consumer<T> shouldNot(Predicate<? super T> predicate) {
    return (obj) -> assertFalse(predicate.test(obj));
  }

  public static <T> Consumer<T> shouldBe(Predicate<? super T> predicate) {
    return (obj) -> assertTrue(predicate.test(obj));
  }

  public static <T> Consumer<T> shouldNotBe(Predicate<? super T> predicate) {
    return (obj) -> assertFalse(predicate.test(obj));
  }

  public static <T> Consumer<T> shouldBe(Object value) {
    return (obj) -> assertEquals(value, obj);
  }

  public static <T> Consumer<T> shouldNotBe(Object value) {
    return (obj) -> assertNotEquals(value, obj);
  }

  public static Consumer shouldNotBeNull() {
    return (obj) -> assertNotNull(obj);
  }

  public static Consumer shouldBeNull() {
    return (obj) -> assertNull(obj);
  }

  public static <T> Consumer<T> shouldEqual(T value) {
    return (obj) -> assertEquals(value, obj);
  }

  public static <T extends Collection<?>> Consumer<T> shouldBeEmpty() {
    return (collection) -> assertTrue(collection.isEmpty());
  }

  public static <T extends Collection<?>> Consumer<T> shouldNotBeEmpty() {
    return (collection) -> assertFalse(collection.isEmpty());
  }

}

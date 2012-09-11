/*
 * Copyright 2009-2012 Marcelo Varella Barca Guimarães
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *           http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.atatec.trugger.test.iteration;

import org.atatec.trugger.iteration.Iteration;
import org.atatec.trugger.iteration.SearchException;
import org.atatec.trugger.predicate.CompositePredicate;
import org.atatec.trugger.predicate.Predicate;
import org.atatec.trugger.predicate.Predicates;
import org.atatec.trugger.transformer.Transformer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.atatec.trugger.iteration.Iteration.copyTo;
import static org.atatec.trugger.iteration.Iteration.countIn;
import static org.atatec.trugger.iteration.Iteration.moveTo;
import static org.atatec.trugger.iteration.Iteration.removeFrom;
import static org.atatec.trugger.iteration.Iteration.retainFrom;
import static org.atatec.trugger.iteration.Iteration.selectFrom;
import static org.atatec.trugger.test.TruggerTest.assertMatch;
import static org.junit.Assert.assertEquals;

/**
 * A class for testing the {@link Iteration} class.
 *
 * @author Marcelo Varella Barca Guimarães
 */
public class IterationTest {

  private CompositePredicate<Integer> even = Predicates.is(new Predicate<Integer>() {

    public boolean evaluate(Integer element) {
      return element % 2 == 0;
    }
  });
  private CompositePredicate<Integer> odd = even.negate();

  private Collection<Integer> numbers;
  private int initialNumbersSize;
  private int initialNumbersHalfSize;

  private Transformer<String, Integer> transformer = new Transformer<String, Integer>() {

    public String transform(Integer object) {
      return object.toString();
    }
  };

  @Before
  public void initialize() {
    numbers = new ArrayList<Integer>();
    for (int i = 0 ; i < 10000 ; i++) {
      numbers.add(i);
    }
    initialNumbersSize = numbers.size();
    initialNumbersHalfSize = initialNumbersSize / 2;
  }

  @Test
  public void retainTest() {
    int count = countIn(numbers).any(even);
    int result = retainFrom(numbers).any(even);
    assertEquals(initialNumbersHalfSize, count);
    assertEquals(count, result);
    assertEquals(initialNumbersHalfSize, numbers.size());
    assertMatch(numbers, even);
  }

  @Test
  public void removeTest() {
    int count = countIn(numbers).any(even);
    int result = removeFrom(numbers).any(even);
    assertEquals(initialNumbersHalfSize, count);
    assertEquals(count, result);
    assertEquals(initialNumbersHalfSize, numbers.size());
    assertMatch(numbers, odd);
  }

  @Test
  public void predicateCopyTest() {
    Collection<Integer> evenNumbers = new ArrayList<Integer>();
    int result = copyTo(evenNumbers).any(even).from(numbers);
    assertEquals(initialNumbersHalfSize, result);
    assertEquals(initialNumbersHalfSize, evenNumbers.size());
    assertMatch(evenNumbers, even);

    assertEquals(initialNumbersSize, numbers.size());
  }

  @Test
  public void predicateMoveTest() {
    Collection<Integer> evenNumbers = new ArrayList<Integer>();
    int result = moveTo(evenNumbers).any(even).from(numbers);
    assertEquals(initialNumbersHalfSize, result);
    assertEquals(initialNumbersHalfSize, evenNumbers.size());
    assertMatch(evenNumbers, even);

    assertEquals(initialNumbersHalfSize, numbers.size());
    assertMatch(numbers, odd);
  }

  @Test
  public void copyTest() {
    Collection<Integer> integers = new ArrayList<Integer>();
    int result = copyTo(integers).all().from(numbers);
    assertEquals(initialNumbersSize, result);
    assertEquals(initialNumbersSize, integers.size());
    assertEquals(initialNumbersSize, numbers.size());
  }

  @Test
  public void moveTest() {
    Collection<Integer> integers = new ArrayList<Integer>();
    int result = moveTo(integers).all().from(numbers);
    assertEquals(initialNumbersSize, result);
    assertEquals(initialNumbersSize, integers.size());
    assertEquals(0, numbers.size());
  }

  @Test
  public void transformCopyTest() {
    Collection<String> strings = new ArrayList<String>();
    int result = copyTo(strings).applying(transformer).all().from(numbers);
    assertEquals(initialNumbersSize, result);
    assertEquals(initialNumbersSize, strings.size());
    assertEquals(initialNumbersSize, numbers.size());
  }

  @Test
  public void predicateTransformCopyTest() {
    Collection<String> strings = new ArrayList<String>();
    int result = copyTo(strings).applying(transformer).any(even).from(numbers);
    assertEquals(initialNumbersHalfSize, result);
    assertEquals(initialNumbersHalfSize, strings.size());
    assertEquals(initialNumbersSize, numbers.size());
  }

  @Test
  public void transformMoveTest() {
    Collection<String> strings = new ArrayList<String>();
    int result = moveTo(strings).applying(transformer).all().from(numbers);
    assertEquals(initialNumbersSize, result);
    assertEquals(initialNumbersSize, strings.size());
    assertEquals(0, numbers.size());
  }

  @Test
  public void predicateTransformMoveTest() {
    Collection<String> strings = new ArrayList<String>();
    int result = moveTo(strings).applying(transformer).any(even).from(numbers);
    assertEquals(initialNumbersHalfSize, result);
    assertEquals(initialNumbersHalfSize, strings.size());
    assertEquals(initialNumbersHalfSize, numbers.size());
  }

  @Test
  public void singleElementSearchTest() {
    Integer one = selectFrom(numbers).element(new Predicate<Integer>() {
      public boolean evaluate(Integer element) {
        return element.equals(1);
      }
    });
    assertNotNull(one);
  }

  @Test(expected = SearchException.class)
  public void singleElementFailSearchTest() {
    selectFrom(numbers).element(even);
  }

  @Test
  public void firstElementSearchTest() {
    assertEquals(Integer.valueOf(0), selectFrom(numbers).first(even));
  }

  @Test
  public void multipleElementSearchTest() {
    List<Integer> result = selectFrom(numbers).elements(odd);
    assertEquals(initialNumbersHalfSize, result.size());
    assertMatch(result, odd);
  }

}

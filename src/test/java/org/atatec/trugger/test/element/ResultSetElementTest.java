/*
 * Copyright 2009-2014 Marcelo Guimarães
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
package org.atatec.trugger.test.element;

import org.atatec.trugger.HandlingException;
import org.junit.Before;
import org.junit.Test;
import org.kodo.TestScenario;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.atatec.trugger.element.ElementPredicates.*;
import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.element.Elements.elements;
import static org.kodo.Spec.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ResultSetElementTest implements ElementSpecs {

  private ResultSet resultSet;

  @Before
  public void initialize() throws SQLException {
    resultSet = mock(ResultSet.class);

    ResultSetMetaData metadata = mock(ResultSetMetaData.class);
    when(metadata.getColumnCount()).thenReturn(3);
    when(metadata.getColumnName(1)).thenReturn("name");
    when(metadata.getColumnName(2)).thenReturn("nickname");
    when(metadata.getColumnName(3)).thenReturn("age");

    when(resultSet.getMetaData()).thenReturn(metadata);
    when(resultSet.getObject("name"))
        .thenReturn("John")
        .thenReturn("Justin")
        .thenThrow(new SQLException());
    when(resultSet.getObject(1))
        .thenReturn("John")
        .thenReturn("Justin")
        .thenThrow(new SQLException());
    when(resultSet.next()).thenReturn(true);
  }

  @Test
  public void testElements() {
    TestScenario.given(elements().in(ResultSet.class))
        .it(should(be(EMPTY)));

    TestScenario.given(elements().in(resultSet))
        .it(should(have(elementsNamed("name", "nickname", "age"))))
        .each(should(be(specific())));
  }

  @Test
  public void testNamedElement() throws SQLException {
    TestScenario.given(element("name").in(ResultSet.class))
        .it(should(notBe(specific())));

    TestScenario.given(element("name").in(resultSet))
        .the(declaringClass(), should(be(ResultSet.class)))
        .it(should(be(readable())))
        .it(should(notBe(writable())))
        .the(value(), should(be("John")))

        .when(retrievingNextRow())

        .the(value(), should(be("Justin")))
        .then(attempToChangeValue(), should(raise(HandlingException.class)))
        .then(gettingValueIn(new Object()), should(raise(HandlingException.class)))
        .then(gettingValue(), should(raise(HandlingException.class)));
  }

  @Test
  public void testIndexedElement() throws SQLException {
    TestScenario.given(element("1").in(ResultSet.class))
        .it(should(notBe(specific())));

    TestScenario.given(element("1").in(resultSet))
        .the(declaringClass(), should(be(ResultSet.class)))
        .it(should(be(readable())))
        .it(should(notBe(writable())))
        .the(value(), should(be("John")))

        .when(retrievingNextRow())

        .the(value(), should(be("Justin")))
        .then(attempToChangeValue(), should(raise(HandlingException.class)))
        .then(gettingValueIn(new Object()), should(raise(HandlingException.class)))
        .then(gettingValue(), should(raise(HandlingException.class)));
  }

  private Runnable retrievingNextRow() {
    return () -> {
      try {
        resultSet.next();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    };
  }

}

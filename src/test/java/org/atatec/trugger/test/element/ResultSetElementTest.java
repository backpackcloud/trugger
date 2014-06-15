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
import org.atatec.trugger.TruggerException;
import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.ElementPredicates;
import org.atatec.trugger.element.UnwritableElementException;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import static org.atatec.trugger.element.Elements.element;
import static org.atatec.trugger.element.Elements.elements;
import static org.atatec.trugger.test.TruggerTest.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ResultSetElementTest {

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
    when(resultSet.getObject("name")).thenReturn("John");
    when(resultSet.getObject(1)).thenReturn("John");
    when(resultSet.getObject(1)).thenThrow(new SQLException());
    when(resultSet.getObject("name")).thenReturn("Justin");
    when(resultSet.getObject("nickname")).thenReturn("kranck");
    when(resultSet.getObject("nickname")).thenReturn("tropper");
    when(resultSet.getObject("age")).thenReturn(26);
    when(resultSet.getObject("age")).thenReturn(27);
    when(resultSet.next()).thenReturn(true);
  }

  @Test(expected = TruggerException.class)
  public void testError() throws Exception {
    resultSet = mock(ResultSet.class);
    when(resultSet.getMetaData()).thenThrow(new SQLException());

    elements().in(resultSet);
  }

  @Test
  public void testElements() {
    List<Element> elements = elements().in(ResultSet.class);
    assertTrue(elements.isEmpty());

    elements = elements().in(resultSet);
    assertFalse(elements.isEmpty());
    assertMatch(elements, ElementPredicates.specific());
    assertElements(elements, "name", "nickname", "age");
  }

  @Test
  public void testElement() throws SQLException {
    Element name = element("name").in(ResultSet.class);
    Element nickname = element("nickname").in(ResultSet.class);
    Element age = element("age").in(ResultSet.class);
    assertFalse(name.isSpecific());
    assertFalse(nickname.isSpecific());
    assertFalse(age.isSpecific());

    name = element("name").in(resultSet);
    nickname = element("nickname").in(resultSet);
    age = element("age").in(resultSet);
    assertTrue(name.isSpecific());
    assertTrue(nickname.isSpecific());
    assertTrue(age.isSpecific());

    assertEquals("John", name.value());
    assertEquals("kranck", nickname.value());
    assertEquals(26, (int) age.value());
    resultSet.next();
    assertEquals("Justin", name.value());
    assertEquals("tropper", nickname.value());
    assertEquals(27, (int) age.value());

    name = element("1").in(resultSet);
    assertTrue(name.isReadable());
    assertFalse(name.isWritable());
    assertEquals("John", name.value());
    assertEquals(ResultSet.class, name.declaringClass());
    assertThrow(HandlingException.class, name, (el) -> el.value());
    assertThrow(UnwritableElementException.class, name, (el) -> el.set(""));
    assertThrow(HandlingException.class, name, (el) -> el.in("").value());
  }

}

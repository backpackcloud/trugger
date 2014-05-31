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
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class ResultSetElementTest {

  private ResultSet resultSet;

  @Before
  public void initialize() throws SQLException {
    resultSet = createMock(ResultSet.class);

    ResultSetMetaData metadata = createMock(ResultSetMetaData.class);
    expect(metadata.getColumnCount()).andReturn(3).anyTimes();
    expect(metadata.getColumnName(1)).andReturn("name").anyTimes();
    expect(metadata.getColumnName(2)).andReturn("nickname").anyTimes();
    expect(metadata.getColumnName(3)).andReturn("age").anyTimes();

    expect(resultSet.getMetaData()).andReturn(metadata).anyTimes();
    expect(resultSet.getObject("name")).andReturn("John").times(1);
    expect(resultSet.getObject(1)).andReturn("John").times(1);
    expect(resultSet.getObject(1)).andThrow(new SQLException()).times(1);
    expect(resultSet.getObject("name")).andReturn("Justin").times(1);
    expect(resultSet.getObject("nickname")).andReturn("kranck").times(1);
    expect(resultSet.getObject("nickname")).andReturn("tropper").times(1);
    expect(resultSet.getObject("age")).andReturn(26).times(1);
    expect(resultSet.getObject("age")).andReturn(27).times(1);
    expect(resultSet.next()).andReturn(true).times(1);

    replay(metadata, resultSet);
  }

  @Test(expected = TruggerException.class)
  public void testError() throws Exception {
    resultSet = createMock(ResultSet.class);
    expect(resultSet.getMetaData()).andThrow(new SQLException()).anyTimes();
    replay(resultSet);

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

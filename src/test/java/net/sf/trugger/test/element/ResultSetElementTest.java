/*
 * Copyright 2009-2010 Marcelo Varella Barca Guimarães
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
package net.sf.trugger.test.element;

import static net.sf.trugger.element.Elements.element;
import static net.sf.trugger.element.Elements.elements;
import static net.sf.trugger.test.TruggerTest.assertElements;
import static net.sf.trugger.test.TruggerTest.assertMatch;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Set;

import net.sf.trugger.element.Element;
import net.sf.trugger.element.ElementPredicates;

import org.junit.Before;
import org.junit.Test;

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
    expect(resultSet.getObject("name")).andReturn("Justin").times(1);
    expect(resultSet.getObject("nickname")).andReturn("kranck").times(1);
    expect(resultSet.getObject("nickname")).andReturn("tropper").times(1);
    expect(resultSet.getObject("age")).andReturn(26).times(1);
    expect(resultSet.getObject("age")).andReturn(27).times(1);
    expect(resultSet.next()).andReturn(true).times(1);
    
    replay(metadata, resultSet);
  }
  
  @Test
  public void testElements() {
    Set<Element> elements = elements().in(ResultSet.class);
    assertTrue(elements.isEmpty());
    
    elements = elements().in(resultSet);
    assertFalse(elements.isEmpty());
    assertMatch(elements, ElementPredicates.SPECIFIC);
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
    assertEquals(26, age.value());
    resultSet.next();
    assertEquals("Justin", name.value());
    assertEquals("tropper", nickname.value());
    assertEquals(27, age.value());
  }
  
}

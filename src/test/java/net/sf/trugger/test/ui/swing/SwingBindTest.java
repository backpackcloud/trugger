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
package net.sf.trugger.test.ui.swing;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.GregorianCalendar;

import net.sf.trugger.test.ui.swing.Person.Sex;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class SwingBindTest {

  private PersonPanel personPanel = new PersonPanel();

  @Before
  public void reset() {
    personPanel.reset();
  }

  @Test
  public void testBindToGUI() throws Exception {
    Person person = new Person();
    person.name = "John";
    person.sex = Sex.MALE;
    person.birth = new GregorianCalendar(1980, 0, 1).getTime();
    person.age = 25;
    person.salary = 3800.50;
    person.married = false;
    Address address = new Address();
    address.street = "My Street";
    address.city = "My City";
    address.state = "My State";
    person.address = address;
    person.resume = new File("resume.pdf");
    person.papers = Arrays.asList(new File("paper1.pdf"), new File("paper2.pdf"));

    personPanel.setObject(person);

    assertEquals(person.name, personPanel.txtName.getText());
    assertEquals(Sex.MALE, personPanel.sex.getSelectedItem());
    assertEquals("01/01/1980", personPanel.birth.getText());
    assertEquals("25", personPanel.age.getText());
    assertEquals("3.800,50", personPanel.salary.getText());
    assertFalse(personPanel.married.isSelected());
    assertEquals(person.address.street, personPanel.addressPanel.street.getText());
    assertEquals(person.address.city, personPanel.addressPanel.city.getText());
    assertEquals(person.address.state, personPanel.addressPanel.state.getText());
    assertEquals(person.resume, personPanel.resume.getSelectedFile());
    assertArrayEquals(person.papers.toArray(), personPanel.papers.getSelectedFiles());

    person.name = null;
    person.age = null;
    person.birth = null;
    person.salary = null;
    person.sex = null;

    personPanel.setObject(person);

    assertEquals("", personPanel.txtName.getText());
    assertEquals(null, personPanel.sex.getSelectedItem());
    assertEquals("", personPanel.birth.getText());
    assertEquals("", personPanel.age.getText());
    assertEquals("", personPanel.salary.getText());
  }

  @Test
  public void testBindToObject() throws Exception {
    personPanel.txtName.setText("Rosie");
    personPanel.sex.setSelectedItem(Sex.FEMALE);
    personPanel.age.setText("24");
    personPanel.salary.setText("5.480,60");
    personPanel.married.setSelected(true);
    personPanel.birth.setText("05/10/1972");
    personPanel.addressPanel.city.setText("My City");
    personPanel.addressPanel.state.setText("My State");
    personPanel.addressPanel.street.setText("My Street");
    personPanel.resume.setSelectedFile(new File("resume.pdf"));
    personPanel.papers.setSelectedFiles(new File[] { new File("paper1.pdf"), new File("paper2.pdf") });

    Person person = personPanel.getObject();

    assertEquals("Rosie", person.name);
    assertEquals(Sex.FEMALE, person.sex);
    assertEquals(24, person.age.intValue());
    assertEquals(5480.60, person.salary, 1e-4);
    assertTrue(person.married);
    assertEquals(new GregorianCalendar(1972, 9, 5).getTime(), person.birth);
    assertEquals(personPanel.resume.getSelectedFile(), person.resume);
    assertArrayEquals(personPanel.papers.getSelectedFiles(), person.papers.toArray());

    reset();

    person = personPanel.getObject();

    assertNull(person.age);
    assertNull(person.birth);
    assertNull(person.name);
    assertNull(person.salary);
    assertNull(person.sex);
  }

}

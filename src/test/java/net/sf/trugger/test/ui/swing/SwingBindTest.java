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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.GregorianCalendar;

import net.sf.trugger.bind.Binder;
import net.sf.trugger.test.ui.swing.Person.Sex;
import net.sf.trugger.ui.swing.SwingBinder;

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

    personPanel.setObject(person);

    assertEquals("John", personPanel.txtName.getText());
    assertEquals(Sex.MALE, personPanel.sex.getSelectedItem());
    assertEquals("01/01/1980", personPanel.birth.getText());
    assertEquals("25", personPanel.age.getText());
    assertEquals("3.800,50", personPanel.salary.getText());
    assertFalse(personPanel.married.isSelected());
    assertEquals("My Street", personPanel.addressPanel.street);
    assertEquals("My City", personPanel.addressPanel.city);
    assertEquals("My State", personPanel.addressPanel.state);

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

    Person person = new Person();

    Binder binder = SwingBinder.newBinderForTarget(personPanel, person);
    binder.applyBinds(person);

    assertEquals("Rosie", person.name);
    assertEquals(Sex.FEMALE, person.sex);
    assertEquals(24, person.age.intValue());
    assertEquals(5480.60, person.salary, 1e-4);
    assertTrue(person.married);
    assertEquals(new GregorianCalendar(1972, 9, 5).getTime(), person.birth);

    reset();

    binder.applyBinds(person);

    assertNull(person.age);
    assertNull(person.birth);
    assertNull(person.name);
    assertNull(person.salary);
    assertNull(person.sex);
  }

}

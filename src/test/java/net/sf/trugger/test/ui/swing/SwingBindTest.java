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

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import net.sf.trugger.annotation.Bind;
import net.sf.trugger.bind.Binder;
import net.sf.trugger.format.formatters.Date;
import net.sf.trugger.format.formatters.Number;
import net.sf.trugger.format.formatters.NumberType;
import net.sf.trugger.ui.swing.SwingBind;
import net.sf.trugger.ui.swing.SwingBinder;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Marcelo Varella Barca Guimarães
 */
@SwingBind
public class SwingBindTest {

  enum Sex {
    MALE, FEMALE
  }

  @Bind(to = "name")
  private JTextField txtName = new JTextField();

  @Bind
  private JComboBox sex = new JComboBox(new Sex[] { Sex.MALE, Sex.FEMALE });

  @Bind
  @Number
  private JTextField age = new JTextField();

  @Bind
  @Number(type = NumberType.DOUBLE, pattern = "#,##0.00;(#,##0.00)", locale = "pt_BR")
  private JTextField salary = new JTextField();

  @Bind
  private JCheckBox married = new JCheckBox();

  @Bind
  @Date("dd/MM/yyyy")
  private JTextField birth = new JTextField();

  static class Person {

    String name;
    Sex sex;
    Integer age;
    Double salary;
    java.util.Date birth;
    Boolean married;

  }

  @Before
  public void reset() {
    txtName.setText("");
    age.setText("");
    salary.setText("");
    birth.setText("");
    married.setSelected(false);
    sex.setSelectedItem(null);
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

    Binder binder = SwingBinder.newBinderForUI(this, person);
    binder.applyBinds(this);

    assertEquals("John", txtName.getText());
    assertEquals(Sex.MALE, sex.getSelectedItem());
    assertEquals("01/01/1980", birth.getText());
    assertEquals("25", age.getText());
    assertEquals("3.800,50", salary.getText());
    assertFalse(married.isSelected());

    person.name = null;
    person.age = null;
    person.birth = null;
    person.salary = null;
    person.sex = null;

    binder.applyBinds(this);

    assertEquals("", txtName.getText());
    assertEquals(null, sex.getSelectedItem());
    assertEquals("", birth.getText());
    assertEquals("", age.getText());
    assertEquals("", salary.getText());
  }

  @Test
  public void testBindToObject() throws Exception {
    txtName.setText("Rosie");
    sex.setSelectedItem(Sex.FEMALE);
    age.setText("24");
    salary.setText("5.480,60");
    married.setSelected(true);
    birth.setText("05/10/1972");

    Person person = new Person();

    Binder binder = SwingBinder.newBinderForTarget(this, person);
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

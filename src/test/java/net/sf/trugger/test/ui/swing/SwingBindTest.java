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

import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import net.sf.trugger.annotation.Bind;
import net.sf.trugger.bind.Binder;
import net.sf.trugger.formatter.formatters.DateFormat;
import net.sf.trugger.ui.swing.SwingBind;
import net.sf.trugger.ui.swing.SwingBinder;

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

  private JTextField age = new JTextField();

  @Bind
  @DateFormat("dd/MM/yyyy")
  private JTextField birth = new JTextField();

  static class Person {

    String name;
    Sex sex;
    int age;
    Date birth;

  }

  @Test
  public void testBindToGUI() throws Exception {
    Person person = new Person();
    person.name = "John";
    person.sex = Sex.MALE;
    person.birth = new GregorianCalendar(2010, 0, 1).getTime();
    Binder binder = SwingBinder.newBinderForUI(this, person);
    binder.applyBinds(this);

    assertEquals("John", txtName.getText());
    assertEquals(Sex.MALE, sex.getSelectedItem());
    assertEquals("01/01/2010", birth.getText());

    person.name = null;
    binder.applyBinds(this);
    assertEquals("", txtName.getText());
  }

  @Test
  public void testBindToObject() throws Exception {

  }

}

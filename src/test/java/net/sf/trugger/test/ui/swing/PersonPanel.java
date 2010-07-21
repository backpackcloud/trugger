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

import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import net.sf.trugger.annotation.Bind;
import net.sf.trugger.format.formatters.Date;
import net.sf.trugger.format.formatters.Number;
import net.sf.trugger.format.formatters.NumberType;
import net.sf.trugger.test.ui.swing.Person.Sex;
import net.sf.trugger.transformer.transformers.Collection;
import net.sf.trugger.ui.swing.ObjectPanel;
import net.sf.trugger.ui.swing.SwingBind;

/**
 * @author Marcelo Varella Barca Guimarães
 */
@SwingBind
public class PersonPanel extends ObjectPanel<Person> {

  @Bind(to = "name")
  JTextField txtName = new JTextField();

  @Bind
  JComboBox sex = new JComboBox(new Sex[] { Sex.MALE, Sex.FEMALE });

  @Bind
  @Number
  JTextField age = new JTextField();

  @Bind
  @Number(type = NumberType.DOUBLE, pattern = "#,##0.00;(#,##0.00)", locale = "pt_BR")
  JTextField salary = new JTextField();

  @Bind
  JCheckBox married = new JCheckBox();

  @Bind
  @Date("dd/MM/yyyy")
  JTextField birth = new JTextField();

  @Bind(to = "address")
  AddressPanel addressPanel = new AddressPanel();

  @Bind
  JFileChooser resume = new JFileChooser();

  @Bind
  @Collection(arrayType = File.class)
  JFileChooser papers = new JFileChooser();


  public PersonPanel() {
    resume.setMultiSelectionEnabled(false);
    papers.setMultiSelectionEnabled(true);
  }

  public void reset() {
    txtName.setText("");
    age.setText("");
    salary.setText("");
    birth.setText("");
    married.setSelected(false);
    sex.setSelectedItem(null);
    resume.setSelectedFile(null);
    papers.setSelectedFiles(null);
    addressPanel.reset();
  }

}

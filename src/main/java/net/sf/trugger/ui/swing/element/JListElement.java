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
package net.sf.trugger.ui.swing.element;

import java.lang.reflect.Array;
import java.util.Collection;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import net.sf.trugger.element.Element;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public class JListElement extends SwingComponentElement<JList> {

  public JListElement(Element decorated) {
    super(decorated);
  }

  @Override
  protected Object getComponentValue(JList component) {
    if (component.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION) {
      return component.getSelectedValue();
    }
    return component.getSelectedValues();
  }

  @Override
  protected void setComponentValue(JList component, Object value) {
    if (component.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION) {
      component.setSelectedValue(value, true);
    }
    if (value instanceof Collection) {
      for (Object obj : (Collection) value) {
        component.setSelectedValue(obj, true);
      }
    } else {
      int length = Array.getLength(value);
      for (int i = 0 ; i < length ; i++) {
        component.setSelectedValue(Array.get(value, i), true);
      }
    }
    //TODO multiselection set
  }

}

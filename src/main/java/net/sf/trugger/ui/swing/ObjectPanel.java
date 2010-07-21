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
package net.sf.trugger.ui.swing;

import javax.swing.JPanel;

import net.sf.trugger.annotation.processors.BindProcessor;
import net.sf.trugger.reflection.Reflection;

/**
 * @author Marcelo Varella Barca Guimarães
 * @since 2.7
 */
public class ObjectPanel<T> extends JPanel {

  private static final long serialVersionUID = -3307046282000514145L;

  protected final Class<T> objectType;

  protected ObjectPanel() {
    objectType = Reflection.reflect().genericType("T").in(this);
  }

  public T getObject() {
    T object = Reflection.newInstanceOf(objectType);
    new BindProcessor(SwingBind.class).bindContext(this).toObject(object);
    return object;
  }

  public void setObject(T object) {
    new BindProcessor(SwingBind.class).bindObject(object).toContext(this);
  }

}

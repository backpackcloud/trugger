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
package net.sf.trugger.element.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.sf.trugger.HandlingException;
import net.sf.trugger.element.Element;
import net.sf.trugger.element.ElementValueHandler;
import net.sf.trugger.element.UnwritableElementException;
import net.sf.trugger.util.Null;

/**
 * @author Marcelo Varella Barca Guimarães
 */
public final class ResultSetElement extends AbstractElement implements Element {

  public ResultSetElement(String name) {
    super(name);
  }

  @Override
  public Class<?> declaringClass() {
    return ResultSet.class;
  }

  @Override
  public ElementValueHandler in(final Object target) {
    if (target instanceof ResultSet) {
      final ResultSet resultSet = (ResultSet) target;
      return new AbstractElementValueHandler(Null.NULL_ANNOTATED_ELEMENT, resultSet) {

        public void value(Object value) throws HandlingException {
          throw new UnwritableElementException("Cannot write a value in a ResultSet");
        }

        public <E> E value() throws HandlingException {
          try {
            if (name.matches("\\d+")) { //if the name is the column index
              return (E) resultSet.getObject(Integer.parseInt(name));
            }
            return (E) resultSet.getObject(name);
          } catch (SQLException e) {
            throw new HandlingException(e);
          }
        }
      };
    }
    throw new HandlingException("Target is not a " + ResultSet.class);
  }

  @Override
  public boolean isReadable() {
    return true;
  }

  @Override
  public boolean isWritable() {
    return false;
  }

}

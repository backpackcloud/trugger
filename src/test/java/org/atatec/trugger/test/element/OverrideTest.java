package org.atatec.trugger.test.element;

import org.atatec.trugger.element.Element;
import org.atatec.trugger.element.Elements;
import org.junit.Test;

import java.io.Serializable;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/** @author Marcelo Varella Barca Guimarães */
public class OverrideTest {

  public interface Entity {

    Serializable getId();

  }

  public class MyEntity implements Entity {

    private Integer id;

    public Integer getId() {
      return id;
    }

    public void setId(Integer id) {
      this.id = id;
    }

  }

  @Test
  public void testOverride() {
    Element id = Elements.element("id").in(MyEntity.class);
    assertNotNull(id);
    assertEquals(MyEntity.class, id.declaringClass());
    assertEquals(Integer.class, id.type());
  }

}

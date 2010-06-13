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
package net.sf.trugger.test.transformer;

import static net.sf.trugger.transformer.Transformers.PROPERTIES_PARSER;
import static net.sf.trugger.transformer.Transformers.TO_BOOLEAN;
import static net.sf.trugger.transformer.Transformers.TO_DOUBLE;
import static net.sf.trugger.transformer.Transformers.TO_FLOAT;
import static net.sf.trugger.transformer.Transformers.TO_INTEGER;
import static net.sf.trugger.transformer.Transformers.TO_LONG;
import static net.sf.trugger.transformer.Transformers.TO_STRING;
import static net.sf.trugger.util.mock.Mock.element;
import static net.sf.trugger.util.mock.Mock.mock;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.Properties;

import net.sf.trugger.element.Element;
import net.sf.trugger.element.Elements;
import net.sf.trugger.transformer.Transformer;
import net.sf.trugger.transformer.TransformerDSL;

import org.junit.Test;


/**
 * @author Marcelo Varella Barca Guimarães
 */
public class TransformerTest {

  static class MailConfig {
    boolean sendMail;
    Boolean checkMail;

    int retries;
    float delay;

    String user;
    String password;

    long timestamp;
    double value;
  }

  @Test
  public void testPropertiesTransformer() throws Exception {
    Properties props = new Properties();
    InputStream is = getClass().getResourceAsStream("test.properties");
    props.load(is);
    is.close();

    MailConfig config = new MailConfig();

    Elements.copyTo(config).transformingWith(PROPERTIES_PARSER).from(props);

    assertTrue(config.sendMail);
    assertFalse(config.checkMail);

    assertEquals(2, config.retries);
    assertEquals(1.5, config.delay, 1e-5);

    assertEquals("mailUser", config.user);
    assertEquals("mailPassword", config.password);
  }

  @Test
  public void testTransformers() throws Exception {
    assertTrue(TO_BOOLEAN.transform("true"));
    assertFalse(TO_BOOLEAN.transform("false"));

    assertEquals(Integer.valueOf(1), TO_INTEGER.transform("1"));
    assertEquals(Long.valueOf(1), TO_LONG.transform("1"));
    assertEquals(Double.valueOf(2.5), TO_DOUBLE.transform("2.5"));
    assertEquals(Float.valueOf(2.5f), TO_FLOAT.transform("2.5"));
    assertEquals("1", TO_STRING.transform(1));
  }

  private static class MockTransformer implements Transformer {

    private Object returnValue;

    public MockTransformer(Object returnValue) {
      this.returnValue = returnValue;
    }

    public Object transform(Object object) {
      return returnValue;
    }

  }

  @Test
  public void testDSLTransformer() throws Exception {
    TransformerDSL<Element> dsl = new TransformerDSL<Element>(){{
      use(new MockTransformer("name")).on(obj).when(obj.name()).equal("element");
      use(new MockTransformer("readable")).on(obj).when(obj.isReadable());
    }};
    assertEquals("name", dsl.transform(mock(element().named("element"))));
    assertEquals("readable", dsl.transform(mock(element().readable())));
  }

  @Test
  public void testTargetSelection() throws Exception {
    final Element element = mock(element().named("element"));
    final Transformer<Object, Object> transformer = new Transformer<Object, Object>() {

      @Override
      public Object transform(Object object) {
        assertSame(element, object);
        return ((Element) object).name();
      }
    };
    TransformerDSL<Element> dsl = new TransformerDSL<Element>(){{
      use(TO_STRING).on(obj.name()).whenNot(obj.name()).equal("element");
      use(transformer).on(obj);
    }};
    assertEquals("element", dsl.transform(element));
  }

}

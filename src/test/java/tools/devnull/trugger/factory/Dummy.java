package tools.devnull.trugger.factory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@ConverterClass(DummyConverter.class)
public @interface Dummy {

  String value();

}

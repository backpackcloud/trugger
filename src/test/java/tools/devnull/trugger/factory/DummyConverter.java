package tools.devnull.trugger.factory;

public class DummyConverter implements Converter {

  private final String returnValue;

  public DummyConverter(String returnValue) {
    this.returnValue = returnValue;
  }

  @Override
  public Object convert(Object object) {
    return returnValue;
  }

}

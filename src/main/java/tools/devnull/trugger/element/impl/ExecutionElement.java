package tools.devnull.trugger.element.impl;

import tools.devnull.trugger.HandlingException;
import tools.devnull.trugger.ValueHandler;
import tools.devnull.trugger.element.Element;
import tools.devnull.trugger.element.UnwritableElementException;
import tools.devnull.trugger.reflection.Execution;

import java.lang.reflect.Parameter;

public class ExecutionElement extends AbstractElement implements Element {

  private final Object value;

  public ExecutionElement(Parameter parameter, Object value) {
    super(parameter.getName());
    annotatedElement = parameter;
    this.value = value;
  }

  @Override
  public Class declaringClass() {
    return Execution.class;
  }

  @Override
  public boolean isReadable() {
    return true;
  }

  @Override
  public boolean isWritable() {
    return false;
  }

  @Override
  public ValueHandler in(Object target) {
    return new ValueHandler() {
      @Override
      public <E> E value() throws HandlingException {
        return (E) value;
      }

      @Override
      public void set(Object value) throws HandlingException {
        throw new UnwritableElementException();
      }
    };
  }

}

package tools.devnull.trugger.element.impl;

import tools.devnull.trugger.Finder;
import tools.devnull.trugger.Result;
import tools.devnull.trugger.element.Element;
import tools.devnull.trugger.reflection.Execution;
import tools.devnull.trugger.reflection.ParameterPredicates;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class ExecutionElementFinder implements Finder<Element> {

  @Override
  public Result<Element, Object> find(String name) {
    return target -> {
      if (target instanceof Class<?>) {
        throw new NoSuchElementException();
      }
      Execution execution = (Execution) target;
      Predicate<Parameter> predicate = ParameterPredicates.named(name);
      Parameter[] parameters = execution.executable().getParameters();
      for (int i = 0; i < parameters.length; i++) {
        Parameter parameter = parameters[i];
        if (predicate.test(parameter)) {
          return new ExecutionElement(parameter, execution.args()[i]);
        }
      }
      throw new NoSuchElementException();
    };
  }

  @Override
  public Result<List<Element>, Object> findAll() {
    return target -> {
      if (target instanceof Class<?>) {
        return Collections.emptyList();
      }
      Execution execution = (Execution) target;
      Parameter[] parameters = execution.executable().getParameters();
      List<Element> result = new ArrayList<>(parameters.length);
      for (int i = 0; i < parameters.length; i++) {
        result.add(new ExecutionElement(parameters[i], execution.args()[i]));
      }
      return result;
    };
  }

}

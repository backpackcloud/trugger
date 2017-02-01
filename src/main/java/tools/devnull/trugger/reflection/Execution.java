package tools.devnull.trugger.reflection;

import java.lang.reflect.Executable;

/**
 * A class that represents an execution of an {@link Executable} object.
 *
 * @since 5.2
 */
public class Execution {

  private final Executable executable;
  private final Object[] args;

  public Execution(Executable executable, Object[] args) {
    this.executable = executable;
    this.args = args;
  }

  public Executable executable() {
    return executable;
  }

  public Object[] args() {
    return args;
  }

}

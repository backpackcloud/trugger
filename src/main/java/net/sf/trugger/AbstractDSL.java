package net.sf.trugger;

import net.sf.trugger.interception.InvocationTrackerInterceptor;
import net.sf.trugger.reflection.Reflection;

/**
 * An abstraction to create DSLs.
 *
 * @author Marcelo Varella Barca Guimarães
 * @since 2.8
 */
public abstract class AbstractDSL<E> {

  /** The proxy object for calling the methods while creating the DSL. */
  protected E obj;
  protected final Class<E> type;
  protected final InvocationTrackerInterceptor tracker;

  protected AbstractDSL() {
    type = Reflection.reflect().genericType("E").in(this);
    tracker = new InvocationTrackerInterceptor();
  }

  protected AbstractDSL(Class<E> clazz) {
    type = clazz;
    tracker = new InvocationTrackerInterceptor();
  }

  /**
   * @return the proxy used for calling the methods while creating the DSL.
   */
  public final E obj() {
    return obj;
  }

}

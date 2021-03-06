package uk.org.ponder.reflect;

import uk.org.ponder.util.UniversalRuntimeException;

/**
 * Owing to a peculiar bug/inconsistency in certain JVMs, this class is
 * necessary to swallow the ClassNotFoundException that otherwise cannot be
 * handled if Class.forName is tried in certain contexts (static/inner)
 */

public class ClassGetter {
  /**
   * Returns the Class object corresponding to the supplied fully-qualified
   * classname, as if looked up by Class.forName().
   * 
   * @param classname The classname to be looked up.
   * @return The Class object corresponding to the classname, or
   *         <code>null</code> if the name cannot be looked up.
   */
  //TODO: initialise array classes as per this posting:
  //http://lists.gnu.org/archive/html/classpath/2003-01/msg00022.html
  public static Class forName(String classname) {
    try {
      return Class.forName(classname);
    }
    catch (ClassNotFoundException cnfe) {
      return null;
    }
  }

  /**
   * Constructs an object of the given class with only a runtime exception in
   * the case of failure.
   * 
   * @param clazz class to construct
   * @exception UniversalRuntimeException runtime exception
   * @return new instance
   */
  // TODO: replace with ReflectiveCache implementation.
  public static Object construct(Class clazz) {
    try {
      return clazz.newInstance();
    }
    catch (Throwable t) {
      throw UniversalRuntimeException.accumulate(t,
          "Could not create instance of " + clazz
              + " using default constructor");
    }
  }
}
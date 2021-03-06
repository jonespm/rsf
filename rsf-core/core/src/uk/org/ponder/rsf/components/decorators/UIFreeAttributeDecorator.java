/*
 * Created on May 16, 2006
 */
package uk.org.ponder.rsf.components.decorators;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows free interception of *all* XML attributes in the rendered output. This
 * decorator is virtually as dangerous as
 * {@link uk.org.ponder.rsf.components.UIVerbatim} and the dire warnings of
 * sufferings for users at the head of that class should be repeated.
 * 
 * The attribute list will be applied <i>on top of</i> attributes inherited
 * from the template, but will be <i>overwritten by</i> any component-specific
 * attributes applied by the renderer for the component to which this decorator
 * is attached.
 * 
 * Please try to think of technology-neutral abstractions rather than using this
 * class, wherever possible.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class UIFreeAttributeDecorator implements UIDecorator {
  /** A map of String attribute names to String attribute values which will be
   * used to override any attributes taken from the template.
   */
  public Map attributes;

  public UIFreeAttributeDecorator() {
  }

  public UIFreeAttributeDecorator(Map attributes) {
    this.attributes = attributes;
  }

  /** Creates a decorator representing the single supplied key/value pair **/
  public UIFreeAttributeDecorator(String key, String value) {
    attributes = new HashMap();
    attributes.put(key, value);
  }
  
  /** Supplied with key and value arrays of equal lengths, which will form the
   * decorator through selecting keys and values pairwise.
   */
  public UIFreeAttributeDecorator(String[] keys, String[] values) {
    attributes = new HashMap();
    for (int i = 0; i < keys.length; ++ i) {
      attributes.put(keys[i], values[i]);
    }
  }
}

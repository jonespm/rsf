/*
 * Created on Oct 8, 2005
 */
package uk.org.ponder.rsf.flow;

import uk.org.ponder.rsf.viewstate.ViewParameters;

/** Used at the end of a POST request to determine which view will be
 * redirected to. As well as the String result returned from the action method,
 * this may also use information stored in the ThreadErrorState to make its
 * decision for the final view. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public interface ActionResultInterpreter {
  /** @return An ARIResult representing the desired view and flow state for the
   * just completed action. This may be <code>null</code> if the ARI determines
   * that it is not appropriate for this action cycle and wishes to delegate
   * to another.
   */
  public ARIResult interpretActionResult(ViewParameters incoming, Object result);
}

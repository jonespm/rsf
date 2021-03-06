package uk.org.ponder.rsf.view;

import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.viewstate.ViewParameters;

// This honorary comment left in honour of Hans Bergsten, who suggested
// as loudly as he could that JSF could be better (see Chapter 15!).
///**
// * This interface must be implemented by classes representing a view for the
// * ClassViewHandler.
// * 
// * @author Hans Bergsten, Gefion Software <hans@gefionsoftware.com>
// * @version 1.0
// */
/** A "static", that is, application scope component producer. The common 
 * arguments are supplied to give the producer as good a chance as possible
 * of not requiring any request-scope dependencies. 
 */
public interface ComponentProducer {
  /** @param tofill The container into which produced components will be inserted.
   *  @param viewparams The view parameters specifying the currently rendering  view
   *  @param checker A ComponentChecker (actually an interface into a ViewTemplate)
   *  that can be used by the producer to "short-circuit" the production of 
   *  potentially expensive components if they are not present in the chosen
   *  template for this view. Since the IKAT algorithm cannot run at this time, it
   *  is currently only economic to check components that are present at the root
   *  level of the template, but these are the most likely to be expensive.
   */
  public void fillComponents(UIContainer tofill, ViewParameters viewparams, 
      ComponentChecker checker);
}
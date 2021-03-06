/*
 * Created on Dec 9, 2004
 */
package uk.org.ponder.webapputil;

import uk.org.ponder.stringutil.StringList;

/**
 * A quick and dirty serialisable object designed to transport a stack
 * trace between webapps. Used in the InformationServlet.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
public class ErrorObject {
  public String message;
  public String handlerID;
  public StringList stacktrace = new StringList();

  public ErrorObject() {}
  
  public ErrorObject(String message, String handlerID, Throwable t) {
    this.message = message;
    this.handlerID = handlerID;
    if (t != null) {
      StackTraceElement[] trace = t.getStackTrace();
      for (int i = 0; i < trace.length; ++i) {
        stacktrace.add(trace[i].toString());
      }
    }
  }
}
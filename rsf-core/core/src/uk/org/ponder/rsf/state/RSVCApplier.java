/*
 * Created on Nov 12, 2005
 */
package uk.org.ponder.rsf.state;

import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.beanutil.PathUtil;
import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.conversion.LeafObjectParser;
import uk.org.ponder.errorutil.TargettedMessage;
import uk.org.ponder.errorutil.TargettedMessageList;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.mapping.BeanInvalidationModel;
import uk.org.ponder.mapping.DARList;
import uk.org.ponder.mapping.DARReshaper;
import uk.org.ponder.mapping.DataAlterationRequest;
import uk.org.ponder.mapping.LeafObjectDARReshaper;
import uk.org.ponder.rsf.request.ActionTarget;
import uk.org.ponder.rsf.request.RequestSubmittedValueCache;
import uk.org.ponder.rsf.request.SubmittedValueEntry;
import uk.org.ponder.rsf.state.guards.BeanGuardProcessor;
import uk.org.ponder.rsf.uitype.UIType;
import uk.org.ponder.rsf.uitype.UITypes;
import uk.org.ponder.util.Logger;

public class RSVCApplier {
  private VersionCheckPolicy versioncheckpolicy;
  private BeanModelAlterer darapplier;
  private WriteableBeanLocator rbl;
  private BeanInvalidationModel bim;
  private BeanGuardProcessor beanGuardProcessor;

  public void setBeanModelAlterer(BeanModelAlterer darapplier) {
    this.darapplier = darapplier;
  }

  public void setVersionCheckPolicy(VersionCheckPolicy versioncheckpolicy) {
    this.versioncheckpolicy = versioncheckpolicy;
  }

  public void setBeanInvalidationModel(BeanInvalidationModel bim) {
    this.bim = bim;
  }
  
  public void setBeanGuardProcessor(BeanGuardProcessor beanGuardProcessor) {
    this.beanGuardProcessor = beanGuardProcessor;
  }
  
  // this will be used to locate request-scope beans.

  public void setRootBeanLocator(WriteableBeanLocator rbl) {
    this.rbl = rbl;
  }

  /** Apply values from this RSVC to the model, and in addition process
   * any validations specified by BeanGuards.
   */
  public void applyValues(RequestSubmittedValueCache rsvc) {
    // TODO: There is scope for a lot of policy here - mainly version checking.
    // Define a VersionCheckPolicy that will compare oldvalue to the model
    // value.
    TargettedMessageList errors = ThreadErrorState.getErrorState().errors;
    try {
    DARList toapply = new DARList();
  
    for (int i = 0; i < rsvc.entries.size(); ++i) {
      SubmittedValueEntry sve = rsvc.entryAt(i);
      if (sve.componentid != null) { // it is a component binding.
        if (sve.oldvalue != null && sve.valuebinding != null) {
          versioncheckpolicy.checkOldVersion(sve); // will blow on error

          UIType type = UITypes.forObject(sve.oldvalue);
          try {
            // TODO: why did we need to hack the value flat like this - should
            // have been taken care of by FixupNewValue in PostDecoder
            Object flattened = darapplier.getFlattenedValue("", sve.newvalue,
                sve.oldvalue.getClass(), null);
            // cull the change from touching the model.
            if (type.valueUnchanged(sve.oldvalue, flattened))
              continue;
          }
          catch (Exception e) {
            Logger.log.warn("Error flattening value" + sve.newvalue + " into "
                + sve.oldvalue.getClass(), e);
          }
        }
      }
      DataAlterationRequest dar = null;
      Object newvalue = sve.newvalue;
      if (sve.isEL) {
        newvalue = darapplier.getBeanValue((String) sve.newvalue, rbl);
      }
      if (sve.isdeletion) {
        dar = new DataAlterationRequest(sve.valuebinding, newvalue,
            DataAlterationRequest.DELETE);
      }
      else {
        dar = new DataAlterationRequest(sve.valuebinding, newvalue);
      }
      if (sve.reshaperbinding != null) {
        Object reshaper = rbl.locateBean(sve.reshaperbinding);
        if (reshaper instanceof LeafObjectParser) {
          reshaper = new LeafObjectDARReshaper((LeafObjectParser) reshaper);
        }
        try {
          dar = ((DARReshaper) reshaper).reshapeDAR(dar);
        }
        catch (Exception e) {
          Logger.log.info("Error reshaping value", e);
          // errors initially accumulated referring to paths
          errors.addMessage(new TargettedMessage(e.getMessage(), e, dar.path));
        }
      }
      toapply.add(dar);
      // Do this INSIDE the loop since fetched values may change
      darapplier.applyAlteration(rbl, dar, errors, bim.iterator());
    }
    }
    finally {
      beanGuardProcessor.processPostGuards(bim, errors, rbl);
    }
   
  }

  public Object invokeAction(String actionbinding, String knownvalue) {
    String totail = PathUtil.getToTailPath(actionbinding);
    String actionname = PathUtil.getTailPath(actionbinding);
    Object penultimatebean = darapplier.getBeanValue(totail, rbl);
    if (penultimatebean instanceof ActionTarget) {
      Object returnvalue = ((ActionTarget) penultimatebean).invokeAction(
          actionname, knownvalue);
      return returnvalue;
    }
    else {
      return darapplier.invokeBeanMethod(actionbinding, rbl);
    }
  }
}

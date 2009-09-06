package org.jquantlib;

import org.jquantlib.util.Date;
import org.jquantlib.util.DateFactory;

import com.sun.org.apache.xerces.internal.impl.dv.xs.DateDV;

/**
 * helper class to temporarily and safely change the settings
 * @author goovy
 */
public class SavedSettings {
	
    private Date evaluationDate_;
    private    boolean enforcesTodaysHistoricFixings_;
   
      public SavedSettings(){
    	  Date d = Configuration.getSystemConfiguration(null).getGlobalSettings().getEvaluationDate();
    	  this.evaluationDate_ = DateFactory.getFactory().getDate(d.getDayOfMonth(), d.getMonth(), d.getYear());
    	  enforcesTodaysHistoricFixings_ = Configuration.getSystemConfiguration(null).isEnforcesTodaysHistoricFixings();
      }

      public void destroy(){
    	  Configuration.getSystemConfiguration(null).getGlobalSettings().setEvaluationDate(this.evaluationDate_);
    	  Configuration.getSystemConfiguration(null).setEnforcesTodaysHistoricFixings(this.enforcesTodaysHistoricFixings_);
      }
      
      @Override
      public void finalize(){
    	destroy();  
      }
}


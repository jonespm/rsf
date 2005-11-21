/*
 * Created on Nov 19, 2005
 */
package uk.org.ponder.rsac.servlet;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import uk.org.ponder.rsac.RSACResourceLocator;

public class ServletRSACResourceLocator implements RSACResourceLocator, 
    ApplicationContextAware {
  private String[] configlocations;
  private ApplicationContext applicationcontext;
  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException {
    this.applicationcontext = applicationContext;
    WebApplicationContext wac = (WebApplicationContext) applicationContext;
    ServletContext context = wac.getServletContext();
    String configlocation = context.getInitParameter("requestContextConfigLocation");
    configlocations = StringUtils.tokenizeToStringArray(configlocation,
        ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS);
  }

  public String[] getConfigLocation() {
    return configlocations;
  }

  public ApplicationContext getApplicationContext() {
    return applicationcontext;
  }

}

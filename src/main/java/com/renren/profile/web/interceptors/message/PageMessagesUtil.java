package com.renren.profile.web.interceptors.message;

import net.paoding.rose.web.Invocation;

public class PageMessagesUtil
{
  public static PageMessages getPageMessages(Invocation inv)
  {
    PageMessages pms = (PageMessages)inv.getModel().get("pageMessages");
    if (pms == null) {
      pms = new PageMessagesImpl(inv);
      inv.getModel().add("pageMessages", pms);
    }
    return pms;
  }
}
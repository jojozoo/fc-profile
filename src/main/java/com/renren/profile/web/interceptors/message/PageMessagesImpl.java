package com.renren.profile.web.interceptors.message;

import java.util.ArrayList;
import java.util.List;
import net.paoding.rose.web.Invocation;

public class PageMessagesImpl
  implements PageMessages
{
  private Invocation invocation;
  private List<PageMessage> messages = new ArrayList<PageMessage>(4);

  public PageMessagesImpl(Invocation invocation) {
    this.invocation = invocation;
  }

  public List<PageMessage> getMessages() {
    return this.messages;
  }

  public PageMessage addMessage(String code)
  {
    return addMessage(code, null);
  }

  public PageMessage addMessage(String code, String defaultMessage)
  {
    PageMessage pm = new PageMessage(this.invocation, "message", code, defaultMessage);
    this.messages.add(pm);
    return pm;
  }

  public PageMessage addWarning(String code)
  {
    return addWarning(code, null);
  }

  public PageMessage addWarning(String code, String defaultMessage)
  {
    PageMessage pm = new PageMessage(this.invocation, "warning", code, defaultMessage);
    this.messages.add(pm);
    return pm;
  }

  public PageMessage addError(String code)
  {
    return addError(code, null);
  }

  public PageMessage addError(String code, String defaultMessage)
  {
    PageMessage pm = new PageMessage(this.invocation, "error", code, defaultMessage);
    this.messages.add(pm);
    return pm;
  }
}

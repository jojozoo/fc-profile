package com.renren.profile.web.interceptors.message;

import net.paoding.rose.web.Invocation;

public class PageMessage
{
  private static Object[] EMPTY = new Object[0];
  private final Invocation inv;
  private final String type;
  private final String code;
  private String defaultMessage;
  private Object[] args = EMPTY;

  public PageMessage(Invocation inv, String type, String code, String defaultMessage) {
    this.type = type;
    this.code = code;
    this.defaultMessage = defaultMessage;
    this.inv = inv;
  }

  public String getType() {
    return this.type;
  }

  public String getCode() {
    return this.code;
  }

  public void setArgs(Object[] args) {
    this.args = args;
  }

  public String getDefaultMessage() {
    return this.defaultMessage;
  }

  public String getMessage() {
    String text = this.inv.getApplicationContext().getMessage(this.code, this.args, this.defaultMessage, this.inv.getResponse().getLocale());

    if (text == null) {
      text = this.defaultMessage = "[" + this.code + "]";
    }
    return text;
  }

  public String toString()
  {
    return getMessage();
  }
}

package com.renren.profile.web.interceptors.message;

import java.util.List;

public abstract interface PageMessages
{
  public static final String MODEL_KEY = "pageMessages";

  public abstract PageMessage addMessage(String paramString);

  public abstract PageMessage addMessage(String paramString1, String paramString2);

  public abstract PageMessage addWarning(String paramString);

  public abstract PageMessage addWarning(String paramString1, String paramString2);

  public abstract PageMessage addError(String paramString);

  public abstract PageMessage addError(String paramString1, String paramString2);

  public abstract List<PageMessage> getMessages();
}
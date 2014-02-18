package com.renren.profile.util.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ProfileLogger implements ILogger {
    public static <T> ILogger getLogger(Class<T> clazz) {
        return new ProfileLogger(clazz);
    }

    protected final Log log;

    protected <T> ProfileLogger(Class<T> clazz) {
        this.log = LogFactory.getLog(clazz);
    }

    /**
     * 是否输出Trace级别的日志
     */
    @Override
    public final boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }

    /**
     * 是否输出Debug级别的日志
     */
    @Override
    public final boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    /**
     * 是否输出Info级别的日志
     */
    @Override
    public final boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    /**
     * 是否输出Warn级别的日志
     */
    @Override
    public final boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    /**
     * 是否输出Error级别的日志
     */
    @Override
    public final boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    /**
     * 是否输出Fetal级别的日志
     */
    @Override
    public final boolean isFatalEnabled() {
        return log.isFatalEnabled();
    }

    /**
     * 使用 Trace 级别输出指定日志。<br/>
     * 格式为${prefix}|message|param1|...|paramN|${suffix}
     */
    @Override
    public final void trace(String message0, Object... messages) {
        if (!isTraceEnabled()) {
            return;
        }
        String logMessage = correctMessage(message0, messages);
        log.trace(logMessage);
    }

    /**
     * 使用 Trace 级别输出指定日志和异常。<br/>
     * 格式为 ${prefix}|t.message|message1|message2|...|messageN|${suffix} 并且抛出异常
     */
    @Override
    public final void trace(Throwable t, Object... messages) {
        if (!isTraceEnabled()) {
            return;
        }
        String logMessage = correctMessage(t, messages);
        log.trace(logMessage, t);
    }

    @Override
    public final void debug(String message0, Object... messages) {
        if (!isDebugEnabled()) {
            return;
        }
        String logMessage = correctMessage(message0, messages);
        log.debug(logMessage);
    }

    @Override
    public final void debug(Throwable t, Object... messages) {
        if (!isDebugEnabled()) {
            return;
        }
        String logMessage = correctMessage(t, messages);
        log.debug(logMessage, t);
    }

    @Override
    public final void info(String message0, Object... messages) {
        if (!isInfoEnabled()) {
            return;
        }
        String logMessage = correctMessage(message0, messages);
        log.info(logMessage);
    }

    @Override
    public final void info(Throwable t, Object... messages) {
        if (!isInfoEnabled()) {
            return;
        }
        String logMessage = correctMessage(t, messages);
        log.info(logMessage, t);
    }

    @Override
    public final void warn(String message0, Object... messages) {
        if (!isWarnEnabled()) {
            return;
        }
        String logMessage = correctMessage(message0, messages);
        log.warn(logMessage);
    }

    @Override
    public final void warn(Throwable t, Object... messages) {
        if (!isWarnEnabled()) {
            return;
        }
        String logMessage = correctMessage(t, messages);
        log.warn(logMessage, t);
    }

    @Override
    public final void error(String message0, Object... messages) {
        if (!isErrorEnabled()) {
            return;
        }
        String logMessage = correctMessage(message0, messages);
        log.error(logMessage);
    }

    @Override
    public final void error(Throwable t, Object... messages) {
        if (!isErrorEnabled()) {
            return;
        }
        String logMessage = correctMessage(t, messages);
        log.error(logMessage, t);
    }

    @Override
    public final void fatal(String message0, Object... messages) {
        if (!isFatalEnabled()) {
            return;
        }
        String logMessage = correctMessage(message0, messages);
        log.fatal(logMessage);
    }

    @Override
    public final void fatal(Throwable t, Object... messages) {
        if (!isFatalEnabled()) {
            return;
        }
        String logMessage = correctMessage(t, messages);
        log.fatal(logMessage, t);
    }

    private String correctMessage(Throwable t, Object... params) {
        return correctMessage(t == null ? "" : t.getMessage(), params);
    }

    private String correctMessage(String message0, Object... messages) {
        StringBuilder result = new StringBuilder(64);
        doPrefixMessage(result);
        result.append("|").append(correctString(message0));
        if (messages != null && messages.length > 0) {
            for (Object param : messages) {
                result.append("|").append(correctString(String.valueOf(param)));
            }
        }
        doSuffixMessage(result);
        return result.toString();
    }

    private String correctString(String input) {
        StringBuilder sb = new StringBuilder();
        doCorrectString(sb, input);
        return sb.toString();
    }

    protected void doPrefixMessage(StringBuilder result) {
        result.append("common");
    };

    /**
     * @param result
     */
    protected void doSuffixMessage(StringBuilder result) {
    };

    private static void doCorrectString(StringBuilder out, String str) {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (str == null) {
            return;
        }
        int sz;
        sz = str.length();
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);

            // handle unicode
            if (ch > 0x7f) {
                out.append(ch);
            } else if (ch < 32) {
                switch (ch) {
                    case '\b':
                        out.append('\\');
                        out.append('b');
                        break;
                    case '\n':
                        out.append('\\');
                        out.append('n');
                        break;
                    case '\t':
                        out.append('\\');
                        out.append('t');
                        break;
                    case '\f':
                        out.append('\\');
                        out.append('f');
                        break;
                    case '\r':
                        out.append('\\');
                        out.append('r');
                        break;
                    default:
                        out.append(' ');
                        break;
                }
            } else {
                switch (ch) {
                    case '|':
                        out.append('\\');
                        out.append('|');
                        break;
                    case '\\':
                        out.append('\\');
                        out.append('\\');
                        break;
                    default:
                        out.append(ch);
                        break;
                }
            }
        }
    }
}

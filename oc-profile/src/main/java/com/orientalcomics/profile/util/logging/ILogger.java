package com.orientalcomics.profile.util.logging;

/**
 * 统一的日志记录接口<br/>
 * 共6种级别，分别为
 * <ol>
 * <li><code>TRACE</code> trace信息
 * <li><code>DEBUG</code> 调试信息
 * <li><code>INFO</code> 正常运行信息
 * <li><code>WARN</code> 警告信息，有错误产生，但并不是严重的
 * <li><code>ERROR</code> 错误信息，有严重的错误产生
 * <li><code>FATAL</code> 致命错误信息，此错误会导致系统崩溃
 * </ol>
 * 
 * 每个级别共有3个方法
 * <ol>
 * <li>isXxxEnabled 判断此级别会不会输出，一般来说，对于TRACE、DEBUG、INFO，在生成日志前，需要判断是否会输出。
 * <li>xxx(String message0, Object... messages)
 * 输出信息，使用“|”来分隔参数值。message0是为了使得本方法和下面的方法能够区分。
 * <li>xxx(Throwable t, Object... messages) 输出信息并且记录异常，使用“|”来分隔各个参数。
 * </ol>
 * 
 * @author 黄兴海(xinghai.huang)
 * @date 2011-12-28 下午3:46:26
 */
public interface ILogger {

    /**
     * 是否输出Trace级别的日志
     */
    boolean isTraceEnabled();

    /**
     * 是否输出Debug级别的日志
     */
    boolean isDebugEnabled();

    /**
     * 是否输出Info级别的日志
     */
    boolean isInfoEnabled();

    /**
     * 是否输出Warn级别的日志
     */
    boolean isWarnEnabled();

    /**
     * 是否输出Error级别的日志
     */
    boolean isErrorEnabled();

    /**
     * 是否输出Fatal级别的日志
     */
    boolean isFatalEnabled();

    /**
     * 使用 Trace 级别输出指定日志。<br/>
     * 格式为${prefix}|message0|message1|...|messageN|${suffix}
     */
    void trace(String message0, Object... messages);

    /**
     * 使用 Trace 级别输出指定日志和异常。<br/>
     * 格式为 ${prefix}|t.message|message1|...|messageN|${suffix} 并且抛出异常
     */
    void trace(Throwable t, Object... messages);

    /**
     * 使用 Debug 级别输出指定日志。<br/>
     * 格式为${prefix}|message0|message1|...|messageN|${suffix}
     */
    void debug(String message0, Object... messages);

    /**
     * 使用 Debug 级别输出指定日志和异常。<br/>
     * 格式为 ${prefix}|t.message|message1|...|messageN|${suffix} 并且抛出异常
     */
    void debug(Throwable t, Object... messages);

    /**
     * 使用 Info 级别输出指定日志。<br/>
     * 格式为${prefix}|message0|message1|...|messageN|${suffix}
     */
    void info(String message0, Object... messages);

    /**
     * 使用 Info 级别输出指定日志和异常。<br/>
     * 格式为 ${prefix}|t.message|message1|...|messageN|${suffix} 并且抛出异常
     */
    void info(Throwable t, Object... messages);

    /**
     * 使用 Warn 级别输出指定日志。<br/>
     * 格式为${prefix}|message0|message1|...|messageN|${suffix}
     */
    void warn(String message0, Object... messages);

    /**
     * 使用 Warn 级别输出指定日志和异常。<br/>
     * 格式为 ${prefix}|t.message|message1|...|messageN|${suffix} 并且抛出异常
     */
    void warn(Throwable t, Object... messages);

    /**
     * 使用 Error 级别输出指定日志。<br/>
     * 格式为${prefix}|message0|message1|...|messageN|${suffix}
     */
    void error(String message0, Object... messages);

    /**
     * 使用 Error 级别输出指定日志和异常。<br/>
     * 格式为 ${prefix}|t.message|message1|...|messageN|${suffix} 并且抛出异常
     */
    void error(Throwable t, Object... messages);

    /**
     * 使用 Fatal 级别输出指定日志。<br/>
     * 格式为${prefix}|message0|message1|...|messageN|${suffix}
     */
    void fatal(String message0, Object... messages);

    /**
     * 使用 Fatal 级别输出指定日志和异常。<br/>
     * 格式为 ${prefix}|t.message|message1|...|messageN|${suffix} 并且抛出异常
     */
    void fatal(Throwable t, Object... messages);
}
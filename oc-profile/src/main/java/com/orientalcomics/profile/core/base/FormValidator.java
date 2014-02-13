package com.orientalcomics.profile.core.base;

import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.util.logging.ILogger;
import com.orientalcomics.profile.util.logging.ProfileLogger;

public class FormValidator {
    private static ILogger LOG = ProfileLogger.getLogger(FormValidator.class);
    private HtmlPage       page;
    private boolean        failed;

    FormValidator(HtmlPage page) {
        this.page = page;
        failed = false;
    }

    public FormValidator assertFalse(boolean condition, String key, String message) {
        _assertTrue(!condition, key, message);
        return this;
    }

    public FormValidator assertTrue(boolean condition, String key, String message) {
        _assertTrue(condition, key, message);
        return this;
    }

    public FormValidator error(String key, String message) {
        _assertTrue(false, key, message);
        return this;
    }

    public FormValidator min(int value, int min, String key, String message) {
        _assertTrue(value >= min, key, message, value, min);
        return this;
    }

    public FormValidator max(int value, int max, String key, String message) {
        _assertTrue(value <= max, key, message, value, max);
        return this;
    }

    public FormValidator range(int value, int min, int max, String key,
            String message) {
        _assertTrue(value >= min && value <= max, key, message, value, min, max);
        return this;
    }

    public FormValidator minLength(String value, int minLength, String key,
            String message) {
        _assertTrue(minLength < 0
                || (value != null && value.length() >= minLength), key,
                message, value, minLength);
        return this;
    }

    public FormValidator maxLength(String value, int maxLength, String key,
            String message) {
        _assertTrue(value == null || value.length() <= maxLength, key, message,
                value, maxLength);
        return this;
    }

    public FormValidator rangeLength(String value, int minLength,
            int maxLength, String key, String message) {
        if (minLength > maxLength) {
            return this;
        }
        if (value == null) {
            _assertTrue(minLength < 0, key, message, value, minLength, maxLength);
        } else {
            _assertTrue(value.length() >= minLength
                    && value.length() <= maxLength, key, message, value,
                    minLength, maxLength);
        }
        return this;
    }

    public FormValidator require(Object value, String key, String message) {
        _assertTrue(value != null, key, message, value);
        return this;
    }

    public FormValidator notEmpty(String value, String key, String message) {
        _assertTrue(StringUtils.isNotEmpty(value), key, message, value);
        return this;
    }

    public FormValidator notBlank(String value, String key, String message) {
        _assertTrue(StringUtils.isNotBlank(value), key, message, value);
        return this;
    }

    public boolean isFailed() {
        return failed;
    }

    private void _assertTrue(boolean condition, String key, String message,
            Object... args) {
        if (!condition) {
            failed = true;
            String _msg;
            try {
                _msg = String.format(message, args);
            } catch (Exception e) {
                LOG.error(e, "message", message, StringUtils.join(args, ","));
                _msg = message;
            }
            String _key = "fv_"
                    + StringUtils.lowerCase(StringUtils.trimToEmpty(key));
            page.error(_key, _msg);
        }
    }
}

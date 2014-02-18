package com.renren.profile.util;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.NumberUtils;

public class Numbers0 {
    public static int[] toIntArray(String[] strings) {
        return toIntArray(strings, 0);
    }

    public static int[] toIntArray(String[] strings, int defaultValue) {
        if (strings == null) {
            return null;
        }
        int length = strings.length;
        if (length == 0) {
            return ArrayUtils.EMPTY_INT_ARRAY;
        }
        int[] res = new int[length];
        for (int i = 0; i < length; i++) {
            res[i] = NumberUtils.toInt(strings[i], defaultValue);
        }
        return res;
    }

    public static long[] toLongArray(String[] strings) {
        return toLongArray(strings, 0);
    }

    public static long[] toLongArray(String[] strings, long defaultValue) {
        if (strings == null) {
            return null;
        }
        int length = strings.length;
        if (length == 0) {
            return ArrayUtils.EMPTY_LONG_ARRAY;
        }
        long[] res = new long[length];
        for (int i = 0; i < length; i++) {
            res[i] = NumberUtils.toLong(strings[i], defaultValue);
        }
        return res;
    }
}

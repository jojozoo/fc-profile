package com.orientalcomics.profile.util.common; 

import net.paoding.rose.web.paramresolver.SafedTypeConverterFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.SimpleTypeConverter;

/** 
 * @author 张浩 E-mail:zhanghao@foundercomics.com 
 * @version 创建时间：2014年2月12日 下午3:14:48 
 * 类说明 :通用的数值转换类
 */
public class NumberUtils0 extends NumberUtils {

    public static Integer[] fromObject(Object object) {
        if (object == null) {
            return null;
        }
        SimpleTypeConverter simpleTypeConverter = SafedTypeConverterFactory.getCurrentConverter();
        try {
            return (Integer[]) simpleTypeConverter.convertIfNecessary(object, Integer[].class);
        } catch (Exception e) {
            return null;
        }
    }

    public static int toInt(final Integer i) {
        return toInt(i, 0);
    }

    public static int toInt(final Integer i, final int def) {
        if (i == null) {
            return def;
        }
        return i;
    }

    public static int sum(final int[] array) {
        if (array == null) {
            return 0;
        }
        int sum = 0;
        for (final int val : array) {
            sum += val;
        }
        return sum;
    }

    public static int toInt(final String str) {
        return toInt(str, 0);
    }

    public static int toInt(final String str, final int defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        return NumberUtils.toInt(str.trim(), defaultValue);
    }

    public static Integer toInteger(final String str) {
        return toInteger(str, null);
    }

    public static Integer toInteger(final String str, final Integer defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (final Exception e) {
            return defaultValue;
        }
    }

    public static int[] splitToInts(final String str, final String sep) {
        return splitToInts(str, sep, 0);
    }

    public static int[] splitToInts(final String str, final String sep, final int defaultValue) {
        if (str == null) {
            return null;
        }
        final String[] strArr = StringUtils.split(str, sep);
        return toInts(strArr, defaultValue);
    }

    public static Integer[] splitToIntegers(final String str, final String sep) {
        return splitToIntegers(str, sep, 0);
    }

    public static Integer[] splitToIntegers(final String str, final String sep, final int defaultValue) {
        if (str == null) {
            return null;
        }
        final String[] strArr = StringUtils.split(str, sep);
        return toIntegers(strArr, defaultValue);
    }

    public static long[] splitToLongs(final String str, final String sep) {
        return splitToLongs(str, sep, 0);
    }

    public static long[] splitToLongs(final String str, final String sep, final long defaultValue) {
        if (str == null) {
            return null;
        }
        final String[] strArr = StringUtils.split(str, sep);
        return toLongs(strArr, defaultValue);
    }

    public static int[] toInts(final String[] array) {
        return toInts(array, 0);
    }

    public static int[] toInts(final String[] array, final int defaultValue) {
        if (array == null) {
            return null;
        }
        final int[] result = new int[array.length];
        for (int i = array.length - 1; i >= 0; --i) {
            result[i] = toInt(array[i], defaultValue);
        }
        return result;
    }

    public static Integer[] toIntegers(final String[] array) {
        return toIntegers(array, null);
    }

    public static Integer[] toIntegers(final String[] array, final Integer defaultValue) {
        if (array == null) {
            return null;
        }
        final Integer[] result = new Integer[array.length];
        for (int i = array.length - 1; i >= 0; --i) {
            result[i] = toInteger(array[i], defaultValue);
        }
        return result;
    }

    public static long[] toLongs(final String[] array) {
        return toLongs(array, 0);
    }

    public static long[] toLongs(final String[] array, final long defaultValue) {
        if (array == null) {
            return null;
        }
        final long[] result = new long[array.length];
        for (int i = array.length - 1; i >= 0; --i) {
            result[i] = toLong(array[i], defaultValue);
        }
        return result;
    }

    public static final int pow2(int exponent) {
        if (exponent < 0 || exponent >= 32) {
            throw new IllegalArgumentException("@exponent must be in [0,32)");
        }
        return POW2[exponent];
    }

    private static final int[] POW2 = {//@f0
         1 << 0,//
         1 << 1,//
         1 << 2,//
         1 << 3,//
         1 << 4,//
         1 << 5,//
         1 << 6,//
         1 << 7,//
         1 << 8,//
         1 << 9,//
         1 << 10,//
         1 << 11,//
         1 << 12,//
         1 << 13,//
         1 << 14,//
         1 << 15,//
         1 << 16,//
         1 << 17,//
         1 << 18,//
         1 << 19,//
         1 << 20,//
         1 << 21,//
         1 << 22,//
         1 << 23,//
         1 << 24,//
         1 << 25,//
         1 << 26,//
         1 << 27,//
         1 << 28,//
         1 << 29,//
         1 << 30,//
         1 << 31//
      };//@f1
}
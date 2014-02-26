package com.orientalcomics.profile.util.common;

public class Arrays0 {
    public static <T> T get(T[] array, int index) {
        if (array == null || index < 0 || index >= array.length) {
            return null;
        }
        return array[index];
    }

    public static int get(int[] array, int index) {
        return get(array, index, 0);
    }

    public static int get(int[] array, int index, int defaultValue) {
        if (array == null || index < 0 || index >= array.length) {
            return defaultValue;
        }
        return array[index];
    }
}

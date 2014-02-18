package com.renren.profile.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.google.common.base.Function;

public class Collections0 {
    public static <T> Set<T> subtract(Set<T> set1, Set<T> set2) {
        final LinkedHashSet<T> result = new LinkedHashSet<T>(set1);
        final Iterator iterator = set2.iterator();

        while (iterator.hasNext()) {
            result.remove(iterator.next());
        }

        return result;
    }

    public static <T> List<T> subtract(List<T> list1, List<T> list2) {
        final ArrayList<T> result = new ArrayList<T>(list1);
        final Iterator iterator = list2.iterator();

        while (iterator.hasNext()) {
            result.remove(iterator.next());
        }

        return result;
    }

    public static <T> boolean isEmpty(List<T> list) {

        return (list == null || list.size() == 0) ? true : false;

    }

    public static <Bean, Field> Map<Field, Bean> packageMapByField(Collection<Bean> collection, Function<Bean, Field> function) {
        if (CollectionUtils.isEmpty(collection)) {
            return Collections.emptyMap();
        }
        Map<Field, Bean> result = new HashMap<Field, Bean>();
        for (Bean bean : collection) {
            result.put(function.apply(bean), bean);
        }
        return result;
    }
}

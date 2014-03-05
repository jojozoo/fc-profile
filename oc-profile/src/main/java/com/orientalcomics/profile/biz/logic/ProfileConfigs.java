package com.orientalcomics.profile.biz.logic;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.orientalcomics.profile.util.common.Numbers0;


public abstract class ProfileConfigs {

    // insert into admin_config(id,name,value,des)
    // values(18,'page_size_for_spammer_list',100,'int:audit spammer list');
    public interface ConfigView<T> {
        int id();

        /**
         * 解析字符串。不允许抛异常。
         * 
         * @param s
         * @return
         */
        T valFromString(String s);

        String valToString(T t);

        /**
         * 如下情况会使用默认值：
         * <ul>
         * <li>配置不存在
         * <li>valFromString返回Null
         * <li>db连接失败
         * </ul>
         * <br/>
         * 不允许抛异常。
         * 
         * @return
         */
        T defaultValue();
    }

    public enum StringConfigView implements ConfigView<String> {
        WeeklyReportQADefaultText(15,""),//
        ;

        private final int    id;
        private final String defValue;

        private StringConfigView(final int id) {
            this.id = id;
            this.defValue = null;
        }

        private StringConfigView(final int id, final String defValue) {
            this.id = id;
            this.defValue = defValue;
        }

        public int id() {
            return id;
        }

        @Override
        public String valFromString(String s) {
            return StringUtils.trimToNull(s);
        }

        @Override
        public String valToString(String t) {
            return t;
        }

        public String defaultValue() {
            return this.defValue;
        }

    }

    public enum IntegerConfigView implements ConfigView<Integer> {
        PHOTO_MAX_SIZE_M(11, 5), // 头像图片的最大大小（单位为M）
        DEPARTMENT_TREE_ROOT_ID(13, 45), // 菜单上的“部门列表”的根部门的ID（一级菜单为跟部门的子部门）
        ;

        public final int     id;

        public final Integer defValue;

        private IntegerConfigView(final int id) {
            this(id, NumberUtils.INTEGER_ZERO);
        }

        private IntegerConfigView(final int id, final Integer defValue) {
            this.id = id;
            this.defValue = defValue;
        }

        public int id() {
            return id;
        }

        @Override
        public Integer valFromString(String s) {
            return Numbers0.toInteger(s, defaultValue());
        }

        @Override
        public String valToString(Integer t) {
            return String.valueOf(t);
        }

        public Integer defaultValue() {
            return defValue;
        }
    }

    public enum PageSizeConfigView implements ConfigView<Integer> {
        DEFAULT(1, 20), //
        // 周报
        WEEKLY_REPORT(2, 20), // 用户的周报列表
        // KPI
        KPI(3, 20), // 用户的Kpi列表
        // Admin
        ADMIN_USER(4, 20), // 【员工管理】种的用户列表
        ADMIN_PROMOTION(6, 20), // 【升职管理】中的列表
        // 绩效
        PERF_USER_RESULT(7, 20), // 【绩效考核】用户的结果列表
        // Search
        SEARCH_RESULT(8, 20), // 【搜索结果】里的用户列表
        // role
        ROLE_USER(9, 20), // 【角色管理】里的用户列表
        //
        PERF_TIME(10, 20), // 【绩效周期管理】里的列表
        
        // 下属周报
        SUBORDINATE_WEEKLY_REPORT(12, 20), // 【下属周报列表】的下属列表页
        // 部门的下属列表
        USER_LIST_IN_DEPARTMENT(14,20),//
        SUBORDINATE_KPI_SCORE(16, 20), // 对下属kpi打分
        SCORE_DETAILS(17,15),
        WHOTEST_ELEMENTS(18,50),//WHOTEST每页显示50条记录
        
        DAILY_REPORT(23, 20), // 用户的日报列表
        
     // 下属周报
        SUBORDINATE_DAILY_REPORT(24, 20), // 【下属日报列表】的下属列表页
        ;

        public final int     id;

        public final Integer defValue;
        public final Integer maxValue;

        private PageSizeConfigView(final int id) {
            this(id, NumberUtils.INTEGER_ZERO);
        }

        private PageSizeConfigView(final int id, final Integer defValue) {
            this(id, defValue, null);
        }

        private PageSizeConfigView(final int id, final Integer defValue, final Integer maxValue) {
            this.id = id;
            this.defValue = defValue;
            this.maxValue = maxValue;
        }

        public int id() {
            return id;
        }

        private static final Map<Integer, PageSizeConfigView> MAP        = new HashMap<Integer, ProfileConfigs.PageSizeConfigView>();
        private static volatile boolean                       hasInitMap = false;

        public static PageSizeConfigView findById(Integer id) {
            PageSizeConfigView result = MAP.get(id);
            if (result != null) {
                return result;
            }
            if (!hasInitMap) {
                synchronized (MAP) {
                    if (!hasInitMap) {
                        hasInitMap = true;
                        for (PageSizeConfigView value : values()) {
                            MAP.put(value.id(), value);
                        }
                    }
                }
                return MAP.get(id);
            }
            return null;
        }

        @Override
        public Integer valFromString(String s) {
            return Numbers0.toInteger(s, defaultValue());
        }

        @Override
        public String valToString(Integer t) {
            return String.valueOf(t);
        }

        @Override
        public Integer defaultValue() {
            return defValue;
        }

        public Integer maxValue() {
            return this.maxValue;
        }
    }

    public enum IntArrConfigView implements ConfigView<int[]> {
        ;

        public final int   id;
        public final int[] defValue;

        private IntArrConfigView(final int id) {
            this.id = id;
            this.defValue = ArrayUtils.EMPTY_INT_ARRAY;
        }

        private IntArrConfigView(final int id, final int[] defValue) {
            this.id = id;
            this.defValue = defValue;
        }

        public int id() {
            return id;
        }

        @Override
        public int[] valFromString(String s) {
            if (StringUtils.isBlank(s)) {
                return defaultValue();
            }
            return Numbers0.toIntArray(StringUtils.split(s, ","));
        }

        @Override
        public String valToString(int[] t) {
            if (t == null) {
                return "";
            }
            return StringUtils.join(ArrayUtils.toObject(t), ",");
        }

        @Override
        public int[] defaultValue() {
            return defValue;
        }
    }

    public enum IntegerSetConfigView implements ConfigView<Set<Integer>> {
        ROOT_USERS(5), //
        ;

        public final int id;

        private IntegerSetConfigView(final int id) {
            this.id = id;
        }

        public int id() {
            return id;
        }

        @Override
        public Set<Integer> valFromString(String s) {
            if (StringUtils.isBlank(s)) {
                return defaultValue();
            }
            String[] cols = StringUtils.split(s, ",");
            if (cols == null || cols.length == 0) {
                return defaultValue();
            }
            Set<Integer> result = new HashSet<Integer>(cols.length);
            for (String col : cols) {
                Integer item = null;
                try {
                    item = NumberUtils.createInteger(col);
                } catch (NumberFormatException e) {
                }
                if (item != null) {
                    result.add(item);
                }
            }
            return result;
        }

        @Override
        public String valToString(Set<Integer> t) {
            if (t == null) {
                return "";
            }
            return StringUtils.join(t, ",");
        }

        @Override
        public Set<Integer> defaultValue() {
            return Collections.emptySet();
        }
    }

    public enum LongConfigView implements ConfigView<Long> {
        ;

        public final int  id;
        public final Long defValue;

        private LongConfigView(final int id) {
            this(id, NumberUtils.LONG_ZERO);
        }

        private LongConfigView(final int id, Long defValue) {
            this.id = id;
            this.defValue = defValue;
        }

        public int id() {
            return id;
        }

        @Override
        public Long valFromString(String s) {
            return NumberUtils.toLong(s, defaultValue());
        }

        @Override
        public String valToString(Long t) {
            return String.valueOf(t);
        }

        public Long defaultValue() {
            return defValue;
        }
    }

    public enum StringArrayConfigView implements ConfigView<String[]> {
        ;
        private final int      id;
        private final String   split;   // 非正则表达式
        private final String[] defValue;

        private StringArrayConfigView(final int id, final String split) {
            this(id, split, ArrayUtils.EMPTY_STRING_ARRAY);
        }

        private StringArrayConfigView(final int id, final String split, String[] defValue) {
            this.id = id;
            this.split = split;
            this.defValue = defValue;
        }

        private StringArrayConfigView(final int id) {
            this(id, ",");
        }

        public int id() {
            return id;
        }

        @Override
        public String[] valFromString(String s) {
            return StringUtils.split(s, split);
        }

        @Override
        public String valToString(String[] t) {
            return StringUtils.join(t, split);
        }

        public String[] defaultValue() {
            return defValue;
        }
    }

    public enum BooleanConfigView implements ConfigView<Boolean> {
        ;

        private final int     id;
        private final boolean defValue;

        private BooleanConfigView(final int id) {
            this.id = id;
            this.defValue = false;
        }

        private BooleanConfigView(final int id, final boolean defValue) {
            this.id = id;
            this.defValue = defValue;
        }

        public int id() {
            return id;
        }

        @Override
        public Boolean valFromString(String s) {
            if (StringUtils.isBlank(s) || "null".equalsIgnoreCase(s)) {
                return null;
            }
            if (NumberUtils.isNumber(s)) {
                int iVal = NumberUtils.toInt(s);
                return iVal != 0;
            } else {
                return BooleanUtils.toBooleanObject(s);
            }
        }

        @Override
        public String valToString(Boolean t) {
            return String.valueOf(t);
        }

        public Boolean defaultValue() {
            return defValue;
        }
    }

    public enum StringMapConfigView implements ConfigView<Map<String, String>> {
        ;

        private final int id;

        private StringMapConfigView(final int id) {
            this.id = id;
        }

        public int id() {
            return id;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Map<String, String> valFromString(String s) {
            if (StringUtils.isBlank(s) || "null".equalsIgnoreCase(s)) {
                return null;
            }
            Object object = JSONValue.parse(s);
            if (object instanceof JSONObject) {
                return (JSONObject) object;
            }
            return null;
        }

        @Override
        public String valToString(Map<String, String> value) {
            return JSONObject.toJSONString(value);
        }

        public Map<String, String> defaultValue() {
            return Collections.emptyMap();
        }
    }
}

package com.orientalcomics.profile.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.orientalcomics.profile.biz.base.Idable;
import com.orientalcomics.profile.biz.base.Nameable;

public enum ProfileAction implements Idable, Nameable {
    // 员工管理
    USER_MANAGE(2, "员工管理", ProfileActionCategory.USER_MANAGE, false), //
    // 绩效管理
    PERF_MANAGE(72, "绩效管理", ProfileActionCategory.PERF_MANAGE, false), //
    // 升职管理
    VIEW_PROMOTEES(12, "查看当前申请人列表", ProfileActionCategory.PROMOTION_MANAGE, false), //
    VIEW_HISTORY_PROMOTEES(14, "查看历史申请人列表", ProfileActionCategory.PROMOTION_MANAGE, false), //
    EDIT_PROMOTION_RESULT(16, "编辑升职考核", ProfileActionCategory.PROMOTION_MANAGE, false), //
    // 个人信息
    VIEW_INFO(22, "查看个人信息", ProfileActionCategory.INFO), //
   // EDIT_INFO(24, "编辑个人信息", ProfileActionCategory.INFO), //
    VIEW_INFO_LEVEL(26, "查看个人级别", ProfileActionCategory.INFO), //
    // 个人简历
    VIEW_RESUME(32, "查看简历", ProfileActionCategory.RESUME), //
    //EDIT_RESUME(34, "编辑简历", ProfileActionCategory.RESUME), //
    // KPI
    VIEW_KPI(42, "查看KPI", ProfileActionCategory.KPI), //
    //EDIT_KPI(44, "编辑KPI", ProfileActionCategory.KPI), //
    // 周报
    VIEW_WEEKLY_REPORT(52, "查看周报", ProfileActionCategory.WEEKLY_REPORT), //
   // EDIT_WEEKLY_REPORT(54, "编辑周报", ProfileActionCategory.WEEKLY_REPORT), //
    // perf
    VIEW_PERF_RECORD_LIST(61, "查看当前绩效考核记录列表", ProfileActionCategory.PERF), //
    VIEW_PERF_RECORD(62, "查看当前绩效考核记录", ProfileActionCategory.PERF), //
    VIEW_PERF_HISTORY_RECORD(64, "查看历史绩效考核记录", ProfileActionCategory.PERF), //
    VIEW_PERF_SCORE(66, "查看当前绩效的考核成绩", ProfileActionCategory.PERF), //
    VIEW_PERF_HISTORY_SCORE(67, "查看历史绩效的考核成绩", ProfileActionCategory.PERF), //
    VIEW_PERF_PROMOTION(68, "查看当前绩效的升职信息", ProfileActionCategory.PERF), //
    VIEW_PERF_HISTORY_PROMOTION(69, "查看历史绩效的升职信息", ProfileActionCategory.PERF), //
    ;
    private final int                   id;
    private final String                name;
    private final ProfileActionCategory category;
    private final boolean               resources; // 表明这是用户创建的资源。如果为false，则不适用关系类型。

    ProfileAction(int id, String name, ProfileActionCategory category) {
        this(id, name, category, true);
    }

    ProfileAction(int id, String name, ProfileActionCategory category, boolean resources) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.resources = resources;
    }

    private static final Map<Integer, ProfileAction>                     PROFILE_ACTION_MAP          = new HashMap<Integer, ProfileAction>();
    private static final Map<String, ProfileAction>                      PROFILE_ACTION_NAME_MAP     = new HashMap<String, ProfileAction>();
    private static final Map<ProfileActionCategory, List<ProfileAction>> PROFILE_ACTION_CATEGORY_MAP = new HashMap<ProfileActionCategory, List<ProfileAction>>();

    static {
        for (ProfileAction item : values()) {
            if (PROFILE_ACTION_MAP.put(item.getId(), item) != null) {
                throw new IllegalStateException("ProfileAction|id conflict|" + item.getId());
            }
            PROFILE_ACTION_NAME_MAP.put(StringUtils.lowerCase(item.name()), item);
            List<ProfileAction> profileActions = PROFILE_ACTION_CATEGORY_MAP.get(item.getCategory());
            if (profileActions == null) {
                profileActions = new ArrayList<ProfileAction>();
                PROFILE_ACTION_CATEGORY_MAP.put(item.getCategory(), profileActions);
            }
            profileActions.add(item);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProfileActionCategory getCategory() {
        return category;
    }

    public boolean isResources() {
        return resources;
    }

    public static final Map<ProfileActionCategory, List<ProfileAction>> getActionCategoryMap() {
        return PROFILE_ACTION_CATEGORY_MAP;
    }

    public static final ProfileAction findById(Integer id) {
        return PROFILE_ACTION_MAP.get(id);
    }

    public static final ProfileAction findByName(String name) {
        return PROFILE_ACTION_NAME_MAP.get(name);
    }
}
package com.renren.profile.constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public enum ProfileAction {
    VIEW_USER_KPI(2, "查看别人的KPI", ProfileActionCategory.KPI, true), //
    VIEW_USER_WEEKLY_REPORT(4, "查看别人的周报", ProfileActionCategory.WEEKLY_REPORT, true), //
    ;
    private final int                   id;
    private final String                name;
    private final ProfileActionCategory category;
    private final boolean               userResource;

    ProfileAction(int id, String name, ProfileActionCategory category) {
        this(id, name, category, false);
    }

    ProfileAction(int id, String name, ProfileActionCategory category, boolean userResource) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.userResource = userResource;
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

    public boolean isUserResource() {
        return userResource;
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
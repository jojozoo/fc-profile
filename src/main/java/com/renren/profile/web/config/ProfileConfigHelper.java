package com.renren.profile.web.config;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.renren.profile.util.sys.ProfileStore;
import com.renren.profile.web.config.ProfileConfigs.ConfigView;
import com.renren.profile.web.config.dao.ProfileConfigDAO;
import com.renren.profile.web.config.model.ProfileConfig;

@Service
public class ProfileConfigHelper implements ApplicationContextAware {
    @Autowired
    ProfileConfigDAO                                        profileConfigDAO;

    private final ProfileStore<Map<Integer, ProfileConfig>> store;
    private static volatile ProfileConfigHelper                    instance = null;

    public static ProfileConfigHelper ins() {
        return instance;
    }

    private ProfileConfigHelper() {
        store = new ProfileStore<Map<Integer, ProfileConfig>>("renren.profile.dbconfig.cache", "数据库配置") {

            @Override
            protected Map<Integer, ProfileConfig> newObject() {
                Map<Integer, ProfileConfig> res = null;
                try {
                    res = profileConfigDAO.queryAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return res;
            }

            @Override
            protected int expireSeconds() {
                return (int) TimeUnit.HOURS.toSeconds(1);
            };
        };
    }

    public <T> T getValue(ConfigView<T> configView) {
        Map<Integer, ProfileConfig> map = this.store.getObject();
        if (map == null) {
            return configView.defaultValue();
        }
        ProfileConfig config = map.get(configView.id());
        if (config == null) {
            return configView.defaultValue();
        }
        T res = configView.valFromString(config.getValue());
        if (res == null) {
            return configView.defaultValue();
        }
        return res;
    }

    public <T> void setValue(ConfigView<T> configView, T value) {
        String string = configView.valToString(value);
        if (string == null) {
            string = "";
        }
        try {
            profileConfigDAO.updateValue(configView.id(), string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        reset();
    }

    public void reset() {
        store.reset();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        instance = this;
    }
}

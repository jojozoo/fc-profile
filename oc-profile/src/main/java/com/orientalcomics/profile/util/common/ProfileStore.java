package com.orientalcomics.profile.util.common;

import java.util.concurrent.atomic.AtomicLong;

import com.orientalcomics.profile.util.logging.ILogger;
import com.orientalcomics.profile.util.logging.ProfileLogger;
import com.orientalcomics.profile.util.time.TimeFetchUtils;


public abstract class ProfileStore<T> {
    private static final ILogger LOG      = ProfileLogger.getLogger(ProfileStore.class);

    private volatile Object      t        = null;

    private volatile AtomicLong  lastTime = new AtomicLong(TimeFetchUtils.INT.nowSeconds());

    private final Object         lock     = new Object();

    private final Object         NULL_OBJ = new Object();

    /**
     * 创建一个新的对象
     */
    protected abstract T newObject();

    private final String name;
    private final String id;

    public ProfileStore(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    /**
     * 获得一个新的对象。
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public T getObject() {
        final long curTime = TimeFetchUtils.INT.nowSeconds();
        long expireTime = this.lastTime.get() + this.expireSeconds();
        if (curTime >= expireTime) {
            this.reset();
        }
        Object res = this.t;
        if (res == null) {
            synchronized (this.lock) {
                res = this.t;
                if (res == null) {
                    try {
                        res = this.newObject(); // != null
                        LOG.info("[Profile Store]" + this.id + "(" + this.name + ")成功获得对象");
                    } catch (Exception e) {
                        LOG.error("[Profile Store]" + this.id + "(" + this.name + ")无法获得对象", e);
                    }
                    this.t = (res == null ? this.NULL_OBJ : res);
                }
                // RES != NULL
            }
        }
        // RES != null
        return res == this.NULL_OBJ ? null : (T) res;
    }

    /**
     * 设置对象
     * 
     * @param object
     */
    public void setObject(final T object) {
        this.lastTime.set(TimeFetchUtils.INT.nowSeconds());
        this.t = object; // 原子的
    }

    /**
     * 重置对象，将会在下次获取时新建一个对象
     */
    public void reset() {
        this.lastTime.set(TimeFetchUtils.INT.nowSeconds());
        this.t = null; // 原子的
        LOG.info("[Profile Store]" + this.id + "(" + this.name + ")被重置");
    }

    protected int expireSeconds() {
        return Integer.MAX_VALUE;
    }
}

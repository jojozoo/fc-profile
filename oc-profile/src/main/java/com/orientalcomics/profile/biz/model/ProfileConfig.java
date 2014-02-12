package com.orientalcomics.profile.biz.model;

/**
 * 配置
 * @author DanyZhang
 *
 */
public class ProfileConfig {
    private int id;
    private String name;
    private String value;
    private String des;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getDes() {
        return des;
    }
    public void setDes(String des) {
        this.des = des;
    }
    
}

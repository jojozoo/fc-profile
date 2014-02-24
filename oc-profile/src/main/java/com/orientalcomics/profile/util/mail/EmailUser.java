package com.orientalcomics.profile.util.mail;

import com.orientalcomics.profile.biz.model.User;

public class EmailUser {
    private final String name;

    private final String email;

    public EmailUser(User user) {
        this(user.getName(), user.getEmail());
    }

    public EmailUser(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return name + ";" + email;
    }

}

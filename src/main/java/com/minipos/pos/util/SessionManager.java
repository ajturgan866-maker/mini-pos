package com.minipos.pos.util;

import com.minipos.pos.model.User;

public class SessionManager {

    private static User user;

    public static void setUser(User u) {
        user = u;
    }

    public static User getUser() {
        return user;
    }

    public static void clear() {
        user = null;
    }
}
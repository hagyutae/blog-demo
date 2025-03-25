package com.sb02.blogdemo.core.user;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public static String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}

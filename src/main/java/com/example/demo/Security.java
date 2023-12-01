package com.example.demo;

import com.lambdaworks.crypto.SCryptUtil;
import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

public class Security {
    static int SCRYPT_N = 16384;
    static int SCRYPT_R = 8;
    static int SCRYPT_P = 1;

    static public String hashPassword(String password) {
        return SCryptUtil.scrypt(password, SCRYPT_N, SCRYPT_R, SCRYPT_P);
    }

    static public boolean checkPassword(String password, String hashedPassword) {
        return SCryptUtil.check(password, hashedPassword);
    }

    static public String normalizeEmail(String email) {
        email = email.trim();
        email = email.toLowerCase();
        int atIndex = email.indexOf("@");
        String address = email.substring(0, atIndex);
        String domain = email.substring(atIndex + 1);
        if (domain.equals("gmail.com")) {
            address = address.replace(".", "");
        }
        int plusIndex = address.indexOf("+");
        if (plusIndex != -1) {
            address = address.substring(0, plusIndex);
        }

        return address + "@" + domain;
    }

    // TODO: implement this
    static public String escapeHtml(String input) {
        return escapeHtml4(input);
    }
}

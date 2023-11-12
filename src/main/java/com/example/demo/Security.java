package com.example.demo;

import com.lambdaworks.crypto.SCryptUtil;

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
}

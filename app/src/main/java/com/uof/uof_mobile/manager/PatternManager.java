package com.uof.uof_mobile.manager;

import com.uof.uof_mobile.Global;

public class PatternManager {
    // 아이디 패턴 및 보안 확인
    public static int checkId(String id) {
        if (id.length() < 8) {
            return Global.Pattern.LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[a-zA-Z0-9@!*#]+$", id)) {
            return Global.Pattern.NOT_ALLOWED_CHARACTER;
        } else {
            return Global.Pattern.OK;
        }
    }

    // 비밀번호 패턴 및 보안 확인
    public static int checkPw(String pw) {
        if (pw.length() < 8) {
            return Global.Pattern.LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[a-zA-Z0-9@!*#]+$", pw)) {
            return Global.Pattern.NOT_ALLOWED_CHARACTER;
        } else {
            return Global.Pattern.OK;
        }
    }

    // 이름 패턴 확인
    public static int checkName(String name) {
        if (name.length() == 0) {
            return Global.Pattern.LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[ㄱ-ㅎ가-힣]+$", name)) {
            return Global.Pattern.NOT_ALLOWED_CHARACTER;
        } else {
            return Global.Pattern.OK;
        }
    }

    // 전화번호 패턴 확인
    public static int checkPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() < 11) {
            return Global.Pattern.LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[0-9]+$", phoneNumber)) {
            return Global.Pattern.NOT_ALLOWED_CHARACTER;
        } else {
            return Global.Pattern.OK;
        }
    }

    // 사업자번호 패턴 확인
    public static int checkLicenseNumber(String licenseNumber) {
        if (licenseNumber.length() < 10) {
            return Global.Pattern.LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[0-9]+$", licenseNumber)) {
            return Global.Pattern.NOT_ALLOWED_CHARACTER;
        } else {
            return Global.Pattern.OK;
        }
    }
}

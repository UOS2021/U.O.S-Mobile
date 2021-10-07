package com.uos.uos_mobile.manager;

import com.uos.uos_mobile.other.Global;

public class PatternManager {
    public static final int OK = 0;
    public static final int LENGTH_SHORT = 1;
    public static final int LENGTH_LONG = 2;
    public static final int NOT_ALLOWED_CHARACTER = 3;

    // 아이디 패턴 및 보안 확인
    public static int checkId(String id) {
        if (id.length() < 8) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[a-zA-Z0-9@!*#]+$", id)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }

    // 비밀번호 패턴 및 보안 확인
    public static int checkPw(String pw) {
        if (pw.length() < 8) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[a-zA-Z0-9@!*#]+$", pw)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }

    // 이름 패턴 확인
    public static int checkName(String name) {
        if (name.length() == 0) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[ㄱ-ㅎ가-힣]+$", name)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }

    // 전화번호 패턴 확인
    public static int checkPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() < 11) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[0-9]+$", phoneNumber)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }

    // 사업자번호 패턴 확인
    public static int checkLicenseNumber(String licenseNumber) {
        if (licenseNumber.length() < 10) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[0-9]+$", licenseNumber)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }

    // 카드번호 패턴 확인
    public static int checkCardNumber(String cardNumber) {
        if (cardNumber.length() < 16) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[0-9]+$", cardNumber)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }

    // 카드 비밀번호 패턴 확인
    public static int checkCardPw(String cardPw) {
        if (cardPw.length() < 4) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[0-9]+$", cardPw)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }

    // 카드 CVC 패턴 확인
    public static int checkCardCvc(String cvc) {
        if (cvc.length() < 3) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[0-9]+$", cvc)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }

    // 카드 CVC 패턴 확인 - 월
    public static int checkCardDueDateMonth(String dueDate) {
        if (dueDate.length() < 2) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[0-9]+$", dueDate)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }

    // 카드 CVC 패턴 확인 - 년
    public static int checkCardDueDateYear(String dueDate) {
        if (dueDate.length() < 2) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[0-9]+$", dueDate)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }
}

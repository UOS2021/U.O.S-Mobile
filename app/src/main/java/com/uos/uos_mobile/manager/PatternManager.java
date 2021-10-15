package com.uos.uos_mobile.manager;

/**
 * 문자열의 패턴을 확인하기 위한 클래스.<br><br>
 * <p>
 * 아이디, 비밀번호, 전화번호와 같은 입력값에 대한 조건을 설정, 해당 조건을 만족하는지에 대해 검사 후 결과를 반환해주
 * 는 함수들이 포함되어있습니다.
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class PatternManager {
    /**
     * 입력 조건 충족.
     */
    public static final int OK = 0;

    /**
     * 입력 조건 미충족 - 입력 길이 미만.
     */
    public static final int LENGTH_SHORT = 1;

    /**
     * 입력 조건 미충족 - 입력 길이 초과.
     */
    public static final int LENGTH_LONG = 2;

    /**
     * 입력 조건 미충족 - 허용되지 않은 문자 포함.
     */
    public static final int NOT_ALLOWED_CHARACTER = 3;

    /**
     * 아이디 입력 조건을 검사하는 함수.
     *
     * @param id 입력 조건을 검사할 아이디.
     * @return int 입력 조건 검사 결과 값.
     */
    public static int checkId(String id) {
        if (id.length() < 8) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[a-zA-Z0-9@!*#]+$", id)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }

    /**
     * 비밀번호 입력 조건을 검사하는 함수.
     *
     * @param pw 입력 조건을 검사할 비밀번호.
     * @return int 입력 조건 검사 결과 값.
     */
    public static int checkPw(String pw) {
        if (pw.length() < 8) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[a-zA-Z0-9@!*#]+$", pw)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }

    /**
     * 이름 입력 조건을 검사하는 함수.
     *
     * @param name 입력 조건을 검사할 이름.
     * @return int 입력 조건 검사 결과 값.
     */
    public static int checkName(String name) {
        if (name.length() == 0) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[ㄱ-ㅎ가-힣]+$", name)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }

    /**
     * 전화번호 입력 조건을 검사하는 함수.
     *
     * @param phoneNumber 입력 조건을 검사할 전화번호.
     * @return int 입력 조건 검사 결과 값.
     */
    public static int checkPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() < 11) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[0-9]+$", phoneNumber)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }

    /**
     * 사업자등록번호 입력 조건을 검사하는 함수.
     *
     * @param licenseNumber 입력 조건을 검사할 사업자등록번호.
     * @return int 입력 조건 검사 결과 값.
     */
    public static int checkLicenseNumber(String licenseNumber) {
        if (licenseNumber.length() < 10) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[0-9]+$", licenseNumber)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }

    /**
     * 카드번호 입력 조건을 검사하는 함수.
     *
     * @param cardNumber 입력 조건을 검사할 카드번호.
     * @return int 입력 조건 검사 결과 값.
     */
    public static int checkCardNumber(String cardNumber) {
        if (cardNumber.length() < 16) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[0-9]+$", cardNumber)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }

    /**
     * 카드 비밀번호 입력 조건을 검사하는 함수.
     *
     * @param cardPw 입력 조건을 검사할 카드 비밀번호.
     * @return int 입력 조건 검사 결과 값.
     */
    public static int checkCardPw(String cardPw) {
        if (cardPw.length() < 4) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[0-9]+$", cardPw)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }

    /**
     * 카드 CVC 입력 조건을 검사하는 함수.
     *
     * @param cardPw 입력 조건을 검사할 카드 CVC.
     * @return int 입력 조건 검사 결과 값.
     */
    public static int checkCardCvc(String cvc) {
        if (cvc.length() < 3) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[0-9]+$", cvc)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }

    /**
     * 카드 유효기간(월) 입력 조건을 검사하는 함수.
     *
     * @param dueDate 입력 조건을 검사할 카드 유효기간(월).
     * @return int 입력 조건 검사 결과 값.
     */
    public static int checkCardDueDateMonth(String dueDate) {
        if (dueDate.length() < 2) {
            return LENGTH_SHORT;
        } else if (!java.util.regex.Pattern.matches("^[0-9]+$", dueDate)) {
            return NOT_ALLOWED_CHARACTER;
        } else {
            return OK;
        }
    }

    /**
     * 카드 유효기간(년) 입력 조건을 검사하는 함수.
     *
     * @param dueDate 입력 조건을 검사할 카드 유효기간(년).
     * @return int 입력 조건 검사 결과 값.
     */
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

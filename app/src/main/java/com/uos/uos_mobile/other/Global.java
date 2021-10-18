package com.uos.uos_mobile.other;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.uos.uos_mobile.manager.BasketManager;

/**
 * U.O.S-Mobile에서 전역적으로 사용되는 변수, 상수를 모아둔 클래스
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class Global extends Application {
    public static BasketManager basketManager;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    /**
     * U.O.S-Mobile내 Notification에 대한 정보를 가지고 있는 정적 클래스.
     */
    public static class Notification {
        /**
         * Notification 채널 아이디.
         */
        public static String CHANNEL_ID = "UOS_MOBILE";

        /**
         * Notification 채널명.
         */
        public static String CHANNEL_NAME = "UOS_MOBILE";
    }

    /**
     * Pos에서 기기로 Notification을 보낼 때 사용하는 fcm_token을 가지고 있는 클래스.
     */
    public static class Firebase {
        /**
         * 사용자 기기에 부여된 FCM TOKEN.
         */
        public static String FCM_TOKEN = "";
    }

    /**
     * 현재 로그인한 사용자 정보를 가지고 있는 클래스.
     */
    public static class User {
        /**
         * 로그인한 사용자 아이디.
         */
        public static String id = "";

        /**
         * 로그인한 사용자 이름.
         */
        public static String name = "";

        /**
         * 로그인한 사용자 전화번호.
         */
        public static String phone = "";

        /**
         * 로그인한 사용자 구분. (일반고객 또는 U.O.S 파트너)
         */
        public static String type = "";

        /**
         * 로그인한 사용자의 회사명.
         */
        public static String companyName = "";
    }

    /**
     * SharePreferences에서 사용하는 상수를 모아놓은 클래스.
     */
    public static class SharedPreference {
        /**
         * 앱 데이터를 모아놓은 SharedPreferences 이름.
         */
        public static final String APP_DATA = "APP_DATA";

        /**
         * 앱이 최초로 실행되었는지에 대한 여부. (true: 최초 실행, false: 최초 실행 아님)
         */
        public static final String IS_FIRST = "IS_FIRST";

        /**
         * 사용자가 앱에 로그인 했는지에 대한 여부. 값이 true일 경우 자동으로 마지막에 로그인했던 사용자로 로그인.
         * U.O.S 서비스 탈퇴, 로그아웃 시에만 해당 값이 false로 변경.
         */
        public static final String IS_LOGINED = "IS_LOGINED";

        /**
         * 앱에 로그인한 사용자 아이디. 추후 자동 로그인시 사용.
         */
        public static final String USER_ID = "USER_ID";

        /**
         * 앱에 로그인한 사용자 비밀번호. 추후 자동 로그인시 사용.
         */
        public static final String USER_PW = "USER_PW";

        /**
         * 앱에 로그인한 사용자 종류(일반고객, U.O.S 파트너). 추후 자동 로그인시 사용.
         */
        public static final String USER_TYPE = "USER_TYPE";

        /**
         * Pos기로부터 불러온 QR코드. U.O.S 파트너 로그인 후 QR코드 전시 시 사용.
         */
        public static final String QR_IMAGE = "QR_IMAGE";

        /**
         * 마지막 Notification의 번호. Notification이 중복되지 않도록 하기 위해 사용.
         */
        public static final String LAST_NOTIFICATION_ID = "LAST_NOTIFICATOIN_ID";

        /**
         * Intent로 넘길 수 있는 데이터가 제한되어있어 이를 해결하기 위해서 사용되는 TEMP_MESSAGE 데이터를
         * SharedPreferences에 TEMP_MESSAGE라는 키값으로 임시로 저장한 후 새로운 Activity에서 이를 불러오는
         * 과정에서 사용.
         */
        public static final String TEMP_MESSAGE = "TEMP_MESSAGE";
    }


    /**
     * 네트워크 통신 간 사용되는 상수를 모아놓은 클래스.<br><br>
     * <p>
     * 네트워크 통신시 사용되는 외부서버의 주소, 클라이언트의 요청코드, POS와 외부서버의 응답코드 클래스가 포함되어
     * 있습니다.
     */
    public static class Network {
        /**
         * 외부서버 주소.
         */
        public static final String EXTERNAL_SERVER_URL = "http://211.217.202.39:8080/post";

        /**
         * Pos, 외부서버로 보내는 요청 코드를 모아놓은 클래스.<br><br>
         * <p>
         * 클라이언트에서 외부서버로 데이터 요청 시 사용되는 요청코드를 모아놓은 클래스입니다.
         */
        public static class Request {
            public static final String REGISTER_CUSTOMER = "0001";
            public static final String REGISTER_UOSPARTNER = "0002";
            public static final String LOGIN = "0003";
            public static final String CHANGE_PW = "0004";
            public static final String CHANGE_PHONE = "0005";
            public static final String WITHDRAWAL = "0006";
            public static final String CARD_INFO = "0007";
            public static final String CARD_ADD = "0008";
            public static final String CARD_REMOVE = "0009";
            public static final String ORDER = "0010";
            public static final String CANCEL_ORDER = "0011";
            public static final String ORDER_LIST = "0012";
            public static final String STORE_PRODUCT_INFO = "0013";
            public static final String ORDER_ACCEPTED_STATE = "0014";
            public static final String WAITING_ORDER_LIST = "0015";
            public static final String CUSTOMER_TOOK_PRODUCT = "0016";
            public static final String CUSTOMER_LOGOUT = "0017";
        }

        /**
         * Pos, 외부서버에서 오는 응답 코드를 모아놓은 클래스.<br><br>
         * <p>
         * Pos, 외부서버로부터 요청에 대한 응답을 수신받을 때 응답결과를 구분하기 위한 응답코드를 모아놓은 클래스입
         * 니다.
         */
        public static class Response {
            public static final String UNKNOWN_ERROR = "-1";
            public static final String SERVER_NOT_ONLINE = "-2";
            public static final String REGISTER_SUCCESS = "0001";
            public static final String REGISTER_FAIL_ID_DUPLICATE = "0002";
            public static final String LOGIN_SUCCESS_CUSTOMER = "0003";
            public static final String LOGIN_SUCCESS_UOSPARTNER = "0004";
            public static final String LOGIN_FAIL_ID_NOT_EXIST = "0005";
            public static final String LOGIN_FAIL_PW_NOT_CORRECT = "0006";
            public static final String STORE_PRODUCT_INFO = "0007";
            public static final String THEATER_PRODUCT_INFO = "0008";
            public static final String ORDER_SUCCESS = "0009";
            public static final String FCM_ORDER_ACCEPT = "0010";
            public static final String FCM_ORDER_REFUSE = "0011";
            public static final String ORDER_LIST = "0012";
            public static final String CHANGE_PW_SUCCESS = "0013";
            public static final String CHANGE_PW_FAIL_PW_NOT_CORRECT = "0014";
            public static final String CHANGE_PHONE_SUCCESS = "0015";
            public static final String WITHDRAWAL_SUCCESS = "0016";
            public static final String WITHDRAWAL_FAIL_PW_NOT_CORRECT = "0017";
            public static final String CARD_ADD_SUCCESS = "0018";
            public static final String CARD_REMOVE_SUCCESS = "0019";
            public static final String CARD_INFO = "0020";
            public static final String CARD_NO_INFO = "0021";
            public static final String CANCEL_ORDER_SUCCESS = "0022";
            public static final String PAY_SUCCESS = "0023";
            public static final String PAY_FAIL_WRONG_PASSWORD = "0024";
            public static final String WAITING_ORDER_LIST = "0025";
            public static final String CHANGE_ORDER_STATE_SUCCESS = "0026";
            public static final String CUSTOMER_LOGOUT_SUCCESS = "0027";
            public static final String FCM_ORDER_PREPARED = "0028";
            public static final String FCM_QUARANTINE_NOTICE = "0029";
            public static final String CANCEL_ORDER_FAIL_ALREADY_ACCEPT = "0029";
        }
    }

    /**
     * 상품 종류가 선언 되어있는 클래스.<br><br>
     * <p>
     * 추가로 생성할 상품 형식이 있을 경우 아래 클래스에 선언합니다.
     */
    public static class ItemType {
        /**
         * 상품의 기본적 형태인 Product Type.
         */
        public static final int PRODUCT = 0;

        /**
         * 여러가지 하위 항목을 가지고 있는 형태인 Set Type.
         */
        public static final int SET = 1;

        /**
         * 좌석예약 기능이 있는 Movie Type.
         */
        public static final int MOVIE_TICKET = 2;
    }

    public static class Order {
        public static final int WAITING_ACCEPT = 0;
        public static final int PREPARING = 1;
        public static final int PREPARED = 2;
        public static final int DONE = 3;
        public static final int CANCELED = 4;
    }

    public static class MovieSeat {
        public static final int NONE = -1;
        public static final int RESERVATION_AVAILABLE = 0;
        public static final int ALREADY_RESERVED = 1;
        public static final int UNRESERVED_SEAT = 2;
        public static final int SELECTED_SEAT = 3;
        public static String[] ROW_ARR = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    }
}

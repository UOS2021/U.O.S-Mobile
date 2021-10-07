package com.uos.uos_mobile.other;

import android.app.Dialog;

import androidx.appcompat.app.AppCompatActivity;

import com.uos.uos_mobile.manager.BasketManager;
import com.uos.uos_mobile.manager.SocketManager;

import java.util.ArrayList;

/**
 * U.O.F-Mobile에서 전역적으로 사용되는 변수, 상수를 모아둔 클래스
 *
 * @author Sohn Young Jin
 * @since 1.0.0
 */
public class Global {
    public static BasketManager basketManager;
    public static ArrayList<AppCompatActivity> activities = new ArrayList<>();
    public static ArrayList<Dialog> dialogs = new ArrayList<>();

    /**
     * U.O.F-Mobile내 Notification에 대한 정보를 가지고 있는 정적 클래스
     */
    public static class Notification {
        /**
         * Notification 채널 아이디
         */
        public static String CHANNEL_ID = "UOS_MOBILE";

        /**
         * Notification 채널명
         */
        public static String CHANNEL_NAME = "UOS_MOBILE";

        /**
         * Notification 그룹 아이디
         */
        public static String GROUP_ID = "com.uos.uos_mobile";
    }

    /**
     * Pos에서 기기로 Notification을 보낼 때 사용하는 fcm_token을 가지고 있는 클래스
     */
    public static class Firebase {
        /**
         * 사용자 기기에 부여된 FCM TOKEN
         */
        public static String FCM_TOKEN = "";
    }

    /**
     * 현재 로그인한 사용자 정보를 가지고 있는 클래스
     */
    public static class User {
        /**
         * 로그인한 사용자 아이디
         */
        public static String id = "";

        /**
         * 로그인한 사용자 이름
         */
        public static String name = "";

        /**
         * 로그인한 사용자 전화번호
         */
        public static String phone = "";

        /**
         * 로그인한 사용자 구분(일반고객 또는 U.O.S 파트너)
         */
        public static String type = "";

        /**
         * 로그인한 사용자의 회사명
         */
        public static String companyName = "";
    }

    /**
     * SQLiteManager에서 사용하는 상수를 모아놓은 클래스
     */
    public static class SQLite {
        /**
         * 데이터베이스 버전
         */
        public static final int DB_VERSION = 1;

        /**
         * 수령 대기중인 상품이 들어있는 데이터베이스 명
         */
        public static final String DB_ORDER_LIST = "WaitingOrderList.db";

        /**
         * 수령 대기중인 상품이 들어있는 테이블 명
         */
        public static final String TB_ORDER_LIST = "WaitingOrderList";

        /**
         * 주문번호
         */
        public static final String CL_ORDER_NUMBER = "number";

        /**
         * 주문 회사명
         */
        public static final String CL_ORDER_COMPANY = "company";

        /**
         * 주문 시간
         */
        public static final String CL_ORDER_TIME = "time";

        /**
         * 주문 정보
         */
        public static final String CL_ORDER_INFO = "info";

        /**
         * 주문 상태
         */
        public static final String CL_ORDER_STATE = "state";

        /**
         * 주문 상태 - 상품 준비중
         */
        public static final String ORDER_STATE_WAIT = "wait";

        /**
         * 주문 상태 - 상품 수령 대기중
         */
        public static final String ORDER_STATE_PREPARED = "prepared";

        /**
         * 주문한 사용자의 아이디
         */
        public static final String CL_ORDER_ID = "id";


        /**
         * 정렬방식 - 오름차순
         */
        public static final String SORT_ASCENDING = "ASC";

        /**
         * 정렬방식 - 내림차순
         */
        public static final String SORT_DECENDING = "DESC";

        /**
         * TB_ORDER_LIST 생성 명령어
         */
        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + SQLite.TB_ORDER_LIST + " ("
                + CL_ORDER_NUMBER + " TEXT PRIMARY KEY,"
                + CL_ORDER_ID + " TEXT, "
                + CL_ORDER_COMPANY + " TEXT, "
                + CL_ORDER_TIME + " TEXT, "
                + CL_ORDER_INFO + " TEXT, "
                + CL_ORDER_STATE + " TEXT)";

        /**
         * TB_ORDER_LIST 제거 명령어
         */
        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "
                + SQLite.TB_ORDER_LIST;
    }

    /**
     * SharePreferences에서 사용하는 상수를 모아놓은 클래스
     */
    public static class SharedPreference {
        /**
         * 앱 데이터를 모아놓은 SharedPreferences 이름
         */
        public static final String APP_DATA = "APP_DATA";

        /**
         * 앱이 최초로 실행되었는지에 대한 여부(true: 최초 실행, false: 최초 실행 아님)
         */
        public static final String IS_FIRST = "IS_FIRST";

        /**
         * 사용자가 앱에 로그인 했는지에 대한 여부
         * 값이 true일 경우 자동으로 마지막에 로그인했던 사용자로 로그인
         * U.O.S 서비스 탈퇴, 로그아웃 시에만 해당 값이 false로 변경
         */
        public static final String IS_LOGINED = "IS_LOGINED";

        /**
         * 앱에 로그인한 사용자 아이디
         * 추후 자동 로그인시사용
         */
        public static final String USER_ID = "USER_ID";

        /**
         * 앱에 로그인한 사용자 비밀번호
         * 추후 자동 로그인시 사용
         */
        public static final String USER_PW = "USER_PW";

        /**
         * 앱에 로그인한 사용자 종류(일반고객, U.O.F 파트너)
         * 추후 자동 로그인시 사용
         */
        public static final String USER_TYPE = "USER_TYPE";

        /**
         * Pos기로부터 불러온 QR코드
         * U.O.S 파트너 로그인 후 QR코드 전시 시 사용
         */
        public static final String QR_IMAGE = "QR_IMAGE";

        /**
         * 마지막 Notification의 번호
         * Notification이 중복되지 않도록 하기 위해 사용
         */
        public static final String LAST_NOTIFICATION_NUMBER = "LAST_NOTIFICATION_NUMBER";

        /**
         *
         */
        public static final String TEMP_MESSAGE = "TEMP_MESSAGE";
    }


    /**
     * Network 통신 간 사용되는 상수를 모아놓은 클래스
     */
    public static class Network {
        /**
         * 외부서버 주소
         */
        public static final String EXTERNAL_SERVER_URL = "http://211.217.202.39:8080/post";

        /**
         * Pos, 외부서버로 보내는 요청 코드를 모아놓은 클래스
         */
        public static class Request {
            public static final String REGISTER_CUSTOMER = "0001";
            public static final String REGISTER_UOSPARTNER = "0002";
            public static final String LOGIN = "0003";
            public static final String CHECK_PW = "0004";
            public static final String CHANGE_PW = "0005";
            public static final String CHANGE_PHONE = "0006";
            public static final String WITHDRAWAL = "0007";
            public static final String CARD_INFO = "0008";
            public static final String CARD_ADD = "0009";
            public static final String CARD_REMOVE = "0010";
            public static final String ORDER = "0011";
            public static final String ORDER_CANCEL = "0012";
            public static final String ORDER_LIST = "0013";
            public static final String STORE_PRODUCT_INFO = "0014";
            public static final String QR_IMAGE = "0015";
            public static final String ORDER_ACCEPTED_STATE = "0016";
        }

        /**
         * Pos, 외부서버에서 오는 응답 코드를 모아놓은 클래스
         */
        public static class Response {
            public static final String UNKNOWN_ERROR = "-1";
            public static final String SERVER_NOT_ONLINE = "-2";
            public static final String REGISTER_SUCCESS = "0001";
            public static final String REGISTER_FAILED_ID_DUPLICATE = "0002";
            public static final String LOGIN_SUCCESS = "0003";
            public static final String CHECKPW_SUCCESS = "0004";
            public static final String LOGIN_FAILED_ID_NOT_EXIST = "0005";
            public static final String LOGIN_CHECKPW_FAILED_PW_NOT_CORRECT = "0006";
            public static final String STORE_PRODUCT_INFO = "0007";
            public static final String THEATER_PRODUCT_INFO = "0008";
            public static final String ORDER_SUCCESS = "0009";
            public static final String ORDER_FAILED = "0010";
            public static final String ORDER_ACCEPT = "0011";
            public static final String ORDER_REFUSE = "0012";
            public static final String ORDER_LIST = "0013";
            public static final String CHANGE_PW_SUCCESS = "0014";
            public static final String CHANGE_PW_FAILED = "0015";
            public static final String CHANGE_PHONE_SUCCESS = "0016";
            public static final String CHANGE_PHONE_FAILED = "0017";
            public static final String WITHDRAWAL_SUCCESS = "0018";
            public static final String WITHDRAWAL_FAILED = "0019";
            public static final String CARD_ADD_SUCCESS = "0020";
            public static final String CARD_ADD_FAILED = "0021";
            public static final String CARD_REMOVE_SUCCESS = "0022";
            public static final String CARD_REMOVE_FAILED = "0023";
            public static final String CARD_INFO = "0024";
            public static final String CARD_NOINFO = "0025";
            public static final String ORDER_CANCEL_SUCCESS = "0026";
            public static final String ORDER_CANCEL_FAILED = "0027";
            public static final String QR_IMAGE_SUCCESS = "0028";
            public static final String QR_IMAGE_FAILED = "0029";
            public static final String PAY_SUCCESS = "0030";
            public static final String PAY_FAILED_WRONG_PASSWORD = "0031";
            public static final String PAY_FAILED_NOT_ENOUGH_MONEY = "0032";
        }
    }

    /**
     * 상품 종류가 선언 되어있는 클래스 <br><br>
     * 추가로 생성할 상품 형식이 있을 경우 아래 클래스에 선언.
     */
    public static class ItemType {
        public static final int PRODUCT = 0;
        public static final int SET = 1;
        public static final int MOVIE_TICKET = 2;
    }

    public static class MovieSeat {
        public static final int NONE = -1;
        public static final int RESERVATION_AVAILABLE = 0;
        public static final int ALREADY_RESERVED = 1;
        public static final int UNRESERVED_SEAT = 2;
        public static final int SELECTED_SEAT = 3;
        public static String[] ROW_ARR = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

        public enum ROW {A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z}
    }
}

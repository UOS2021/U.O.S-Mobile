package com.uof.uof_mobile.other;

import androidx.appcompat.app.AppCompatActivity;

import com.uof.uof_mobile.manager.BasketManager;
import com.uof.uof_mobile.manager.SocketManager;

import java.util.ArrayList;

public class Global {
    public static SocketManager socketManager;
    public static BasketManager basketManager;
    public static ArrayList<AppCompatActivity> activities = new ArrayList<>();

    public static class Notification {
        public static String CHANNEL_ID = "UOF_MOBILE";
        public static String CHANNEL_NAME = "UOF_MOBILE";
        public static String GROUP_ID = "com.uof.uof_mobile";
    }

    public static class Firebase {
        public static String FCM_TOKEN = "";
    }

    public static class User {
        public static String id = "";
        public static String name = "";
        public static String phone = "";
        public static String type = "";
    }

    public static class SQLite {
        public static final String DB_ORDER_LIST = "WaitingOrderList.db";
        public static final String TB_ORDER_LIST = "WaitingOrderList";
        public static final int DB_VERSION = 1;

        public static final String CL_ORDER_NUMBER = "number";
        public static final String CL_ORDER_COMPANY = "company";
        public static final String CL_ORDER_TIME = "time";
        public static final String CL_ORDER_INFO = "info";
        public static final String CL_ORDER_STATE = "state";
        public static final String CL_ORDER_ID = "id";

        public static final String SORT_ASCENDING = "ASC";
        public static final String SORT_DECENDING = "DESC";

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + SQLite.TB_ORDER_LIST + " ("
                + CL_ORDER_NUMBER + " TEXT PRIMARY KEY,"
                + CL_ORDER_ID + " TEXT, "
                + CL_ORDER_COMPANY + " TEXT, "
                + CL_ORDER_TIME + " TEXT, "
                + CL_ORDER_INFO + " TEXT, "
                + CL_ORDER_STATE + " TEXT)";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS "
                + SQLite.TB_ORDER_LIST;
    }

    public static class SharedPreference {
        public static final String APP_DATA = "APP_DATA";
        public static final String IS_FIRST = "IS_FIRST";
        public static final String IS_LOGINED = "IS_LOGINED";
        public static final String USER_ID = "USER_ID";
        public static final String USER_PW = "USER_PW";
        public static final String USER_TYPE = "USER_TYPE";
        public static final String QR_IMAGE = "QR_IMAGE";
        public static final String SP_KEY_LAST_NOTIFICATION_NUMBER = "SP_KEY_LAST_NOTIFICATION_NUMBER";
    }

    public static class Network {
        public static final String EXTERNAL_SERVER_URL = "http://211.217.202.55:8080/post";
        public static final String EXTERNAL_SERVER_IP = "211.217.202.157";
        public static final int EXTERNAL_SERVER_PORT = 8080;

        public static class Request {
            public static final String REGISTER_CUSTOMER = "0001";
            public static final String REGISTER_UOFPARTNER = "0002";
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
        }

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
            public static final String STORE_PRODUCT_INFO_THEATER = "0008";
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

    public static class Pattern {
        public static final int OK = 0;
        public static final int LENGTH_SHORT = 1;
        public static final int LENGTH_LONG = 2;
        public static final int NOT_ALLOWED_CHARACTER = 3;
    }

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

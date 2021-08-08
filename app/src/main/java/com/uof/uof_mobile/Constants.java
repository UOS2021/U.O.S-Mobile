package com.uof.uof_mobile;

public class Constants {
    public class Network {
        public final static int SOCKET_MAX_RECV_SIZE = 1000;
        public final static String EXTERNAL_SERVER_IP = "211.217.202.157";
        public final static int EXTERNAL_SERVER_PORT = 8080;

        public class Request {
            public final static String REGISTER_CUSTOMER = "0001";
            public final static String REGISTER_UOFPARTNER = "0002";
            public final static String LOGIN = "0003";
            public final static String CHECK_PW = "0004";
            public final static String CHANGE_PW = "0005";
            public final static String CHANGE_PHONE = "0006";
            public final static String WITHDRAWAL = "0007";
            public final static String CARD_ADD = "0008";
            public final static String CARD_REMOVE = "0009";
            public final static String ORDER = "0010";
            public final static String ORDER_CANCEL = "0011";
            public final static String ORDER_LIST = "0012";
        }

        public class Response {
            public final static String REGISTER_SUCCESS = "0001";
            public final static String REGISTER_FAILED_ID_DUPLICATE = "0002";
            public final static String LOGIN_SUCCESS = "0003";
            public final static String CHECKPW_SUCCESS = "0004";
            public final static String LOGIN_FAILED_ID_NOT_EXIST = "0005";
            public final static String LOGIN_CHECKPW_FAILED_PW_NOT_CORRECT = "0006";
            public final static String STORE_PRODUCT_LIST = "0007";
            public final static String ORDER_NOTHING = "0008";
            public final static String ORDER_WAITING = "0009";
            public final static String ORDER_ACCEPTED = "0010";
            public final static String ORDER_LIST = "0011";
            public final static String CHANGE_PW_SUCCESS = "0012";
            public final static String CHANGE_PW_FAILED = "0013";
            public final static String CHANGE_PHONE_SUCCESS = "0014";
            public final static String CHANGE_PHONE_FAILED = "0015";
            public final static String WITHDRAWAL_SUCCESS = "0016";
            public final static String WITHDRAWAL_FAILED = "0017";
            public final static String CARD_ADD_SUCCESS = "0018";
            public final static String CARD_ADD_FAILED = "0019";
            public final static String CARD_REMOVE_SUCCESS = "0020";
            public final static String CARD_REMOVE_FAILED = "0021";
            public final static String CARD_INFORMATION = "0022";
            public final static String ORDER_CANCEL_SUCCESS = "0023";
            public final static String ORDER_CANCEL_FAILED = "0024";
            public final static String ORDER_SUCCESS = "0025";
            public final static String ORDER_FAILED = "0026";
        }
    }

    public class Pattern {
        public final static int OK = 0;
        public final static int LENGTH_SHORT = 1;
        public final static int LENGTH_LONG = 2;
        public final static int NOT_ALLOWED_CHARACTER = 3;
    }

    public static class User {
        public static String id = "";
        public static String name = "";
        public static String phone = "";
        public static String type = "";
    }
}

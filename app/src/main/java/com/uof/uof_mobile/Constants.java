package com.uof.uof_mobile;

public class Constants {
    public class Network {
        public final static int SOCKET_MAX_RECV_SIZE = 1000;
        public final static String EXTERNAL_SERVER_IP = "211.217.202.157";
        public final static int EXTERNAL_SERVER_PORT = 8080;

        public class Request {
            public final static String REGISTER_CUSTOMER = "0001";
            public final static String REGISTER_OWNER = "0002";
            public final static String LOGIN = "0003";
            public final static String CHANGE_PW = "0004";
            public final static String CHANGE_PHONE_NUMBER = "0005";
            public final static String WITHDRAWAL = "0006";
            public final static String CARD_ADD = "0007";
            public final static String CARD_REMOVE = "0008";
            public final static String ORDER = "0009";
            public final static String ORDER_CANCEL = "0010";
        }

        public class Response {
            public final static String REGISTER_SUCCESS = "0001";
            public final static String REGISTER_FAILED_ID_DUPLICATE = "0002";
            public final static String LOGIN_SUCCESS = "0003";
            public final static String LOGIN_FAILED_ID_NOT_EXIST = "0004";
            public final static String LOGIN_FAILED_PW_NOT_CORRECT = "0005";
            public final static String STORE_PRODUCT_LIST = "0006";
            public final static String ORDER_NOTHING = "0007";
            public final static String ORDER_WAITING = "0008";
            public final static String ORDER_ACCEPTED = "0009";
            public final static String CHANGE_PW_SUCCESS = "0010";
            public final static String CHANGE_PHONE_NUMBER_SUCCESS = "0011";
            public final static String WITHDRAWAL_SUCCESS = "0012";
            public final static String CARD_INFORMATION = "0014";
            public final static String CARD_REMOVE_SUCCESS = "0015";
            public final static String ORDER_CANCEL_SUCCESS = "0016";
            public final static String ORDER_CANCEL_FAILED = "0017";
            public final static String ORDER_SUCCESS = "0018";
            public final static String ORDER_FAILED = "0019";
        }
    }

    public class Pattern {
        public final static int OK = 0;
        public final static int LENGTH_SHORT = 1;
        public final static int LENGTH_LONG = 2;
        public final static int NOT_ALLOWED_CHARACTER = 3;
    }
}

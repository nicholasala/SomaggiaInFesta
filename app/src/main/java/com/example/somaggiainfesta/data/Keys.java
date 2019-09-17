package com.example.somaggiainfesta.data;

public class Keys {
    public static class ip{
        //kitchen reserved address
        public static final byte[] kitchen = {10,27,0,2};
        public static final String kitchen_string = "10.27.0.2";
        public static final String kitchen_url = "ws://" + kitchen_string;
        public static final int ws_port = 8887;
    }

    public enum kitchenState{
        FOUND,
        NOTFOUND,
        NETERR;
    }

    public static class ConstValues{
        public static final int conLostTimeout = 72000;
    }

    public static class MessageText{
        public static final String close= "End of cashdesk service";
    }

    public static class MessageCode{
        public static final int handShake = 0;
        public static final int menu = 1;
        public static final int command = 2;
        public static final int confirmCommand = 3;
        public static final int close = 4;
    }
}

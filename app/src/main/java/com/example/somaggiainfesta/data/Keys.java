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

    public enum CommandState{
        ACTIVE,
        STATIC;
    }

    public static class MessageCode{
        public static final int menu = 1;
        public static final int command = 2;
        public static final int confirmCommand = 3;
    }
}

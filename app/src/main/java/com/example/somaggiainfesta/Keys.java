package com.example.somaggiainfesta;

public class Keys {
    public static class ip{
        //kitchen reserved address
        public static final byte[] kitchen = {10,27,0,2};
        public static final String kitchen_string = "10.27.0.2";
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
}

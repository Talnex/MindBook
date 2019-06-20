package com.talnex.wrongsbook.Utils;

public  class UUID {
    public static String getUUID(){
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

}

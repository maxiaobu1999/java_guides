package com.norman.guides;

import java.io.UnsupportedEncodingException;

public class Utils {

    /** string 转 byte[] */
    public static byte[] stringToByte(String string) {
        byte[] bytes= new byte[0];
        try {
            bytes = string.getBytes("UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * byte[]转string
     */
    public static String byteToString(byte[] bytes) {
        String string = null;
        try {
            string =new String(bytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }
}

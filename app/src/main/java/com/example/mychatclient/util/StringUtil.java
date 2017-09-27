package com.example.mychatclient.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wzj on 2017/9/21.
 */

public class StringUtil {

    public static String getMd5(String str){
        StringBuilder builder = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] byteArray = digest.digest(str.getBytes());
            for (byte b : byteArray) {
                String s = Integer.toHexString(b & 0xff);
                if(s.length() == 1){
                    builder.append("0");
                }
                builder.append(s);
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }
}

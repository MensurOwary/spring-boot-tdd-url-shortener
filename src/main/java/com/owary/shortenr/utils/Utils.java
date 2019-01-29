package com.owary.shortenr.utils;

import org.apache.commons.validator.routines.UrlValidator;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static com.owary.shortenr.utils.Constants.BASE_URL;

public class Utils {

    private static MessageDigest messageDigest;
    static {
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String shorten(){
//        messageDigest.update(url.getBytes());
//        return new String(messageDigest.digest());
        String random = UUID.randomUUID().toString();
        String[] randomSplit = random.split("\\-");
        return randomSplit[4];
    }

    public static boolean isURLCorrect(String url){
        String[] schemes = {"http","https"};
        return new UrlValidator(schemes).isValid(url);
    }

    public static String decode(String encoded){
        try {
            if(encoded != null && !encoded.isEmpty())
                return URLDecoder.decode(encoded, "UTF-8");
            else
                return null;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String getFullUrl(String id){
        return BASE_URL + "/" + id;
    }

}

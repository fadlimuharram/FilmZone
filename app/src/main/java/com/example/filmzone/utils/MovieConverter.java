package com.example.filmzone.utils;

import android.util.Log;

import java.util.Locale;

public class MovieConverter {
    public static String textSubStr(String text, int subStr){
        if (text.length()>subStr){
            return text.substring(0,subStr);
        }
        return text;
    }

    public static String langApiDetection(){
        Log.d("tessll", Locale.getDefault().getLanguage());
        switch (Locale.getDefault().getLanguage()){
            case "en":
                return "en-US";

            case "in":
                return "id-ID";

            default:
                return "en-US";

        }
    }
}

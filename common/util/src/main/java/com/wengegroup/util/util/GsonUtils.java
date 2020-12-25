package com.wengegroup.util.util;

import com.google.gson.Gson;

/**
 * Gson
 */
public class GsonUtils {

    private static final Gson gson = new Gson();

    public static Gson getGson() {
        return gson;
    }

}

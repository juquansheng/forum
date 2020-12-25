package com.wengegroup.util.util;

import com.google.common.collect.Lists;

import java.util.List;

public class StringListUtils {

    public static List<String> oneStringToList(String s) {
        List<String> result = Lists.newArrayListWithCapacity(1);
        result.add(s);

        return result;
    }


    public static String listToOneString(List list) {
        if (list != null && !list.isEmpty()) {
            return (String) list.get(0);
        }
        return null;
    }
}

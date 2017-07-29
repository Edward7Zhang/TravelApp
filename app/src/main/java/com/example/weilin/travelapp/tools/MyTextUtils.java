package com.example.weilin.travelapp.tools;

/**
 * Created by hulinfa on 2017/1/11.
 */

public class MyTextUtils {
    /**
     * 针对TextView显示中文中出现的排版错乱问题，通过调用此方法得以解决
     * @param str
     * @return 返回全部为全角字符的字符串
     */
    public static String toDBC(String str) {
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }

        }
        return new String(c);
    }
}

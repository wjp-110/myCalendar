package com.werb.mycalendardemo.utils;

import com.werb.mycalendardemo.R;


public class ColorUtils {

    public static int getColorFromStr(String s){
        int colorId = 0;
        switch (s) {
            case "无优先级":
                colorId = R.color.moren;
                break;
            case "中优先级":
                colorId = R.color.yaoyanhuang;
                break;
            case "高优先级":
                colorId = R.color.fanqiehong;
                break;
            case "低优先级":
                colorId = R.color.didiaohui;
                break;
        }
        return colorId;
    }
}

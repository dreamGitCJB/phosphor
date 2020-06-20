package org.linlinjava.litemall.db.common.constans;

import java.util.Random;

public class CouponConstant {
    public static final Integer TYPE_COMMON = 0;
    public static final Integer TYPE_REGISTER = 1;
    public static final Integer TYPE_CODE = 2;

    public static final Integer GOODS_TYPE_ALL = 0;
    public static final Integer GOODS_TYPE_CATEGORY = 1;
    public static final Integer GOODS_TYPE_ARRAY = 2;

    public static final Integer STATUS_NORMAL = 0;
    public static final Integer STATUS_EXPIRED = 1;
    public static final Integer STATUS_OUT = 2;

    public static final Integer TIME_TYPE_DAYS = 0;
    public static final Integer TIME_TYPE_TIME = 1;

    public static String getRandomNum(Integer num) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        base += "0123456789";

        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}

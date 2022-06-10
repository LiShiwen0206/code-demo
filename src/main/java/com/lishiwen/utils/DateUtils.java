/*
 * Copyright By ZATI
 * Copyright By 3a3c88295d37870dfd3b25056092d1a9209824b256c341f2cdc296437f671617
 * All rights reserved.
 *
 * If you are not the intended user, you are hereby notified that any use, disclosure, copying, printing, forwarding or
 * dissemination of this property is strictly prohibited. If you have got this file in error, delete it from your system.
 */
package com.lishiwen.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * ============<br/>
 * Date类相关方法
 * <br/>============
 *
 * @author : za-lishiwen
 * @Class_name : DateUtils
 * @Date : 2022/6/10
 */
public class DateUtils {


    /**
     * ============<br/>
     * 获取日期前一天
     * <br/>============
     *
     * @param date:
     * @return : {@link Date}
     * @author : za-lishiwen
     * @Date : 2022/6/10
     */
    public static Date getYesterday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = calendar.getTime();
        return yesterday;


    }

    /**
     * ============<br/>
     * 获取该日期的前一个月的日期
     * <br/>============
     *
     * @param date:
     * @return : {@link Date}
     * @author : za-lishiwen
     * @Date : 2022/6/10
     */
    public static Date dayOfLastMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    /**
     * ============<br/>
     * 年龄计算
     * <br/>============
     *
     * @param birthDay :
     * @param dateNow:
     * @return : {@link int}
     * @author : za-lishiwen
     * @Date : 2022/5/20
     */
    protected int getAge(Date birthDay, Date dateNow) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateNow);
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        Calendar bir = Calendar.getInstance();
        bir.setTime(birthDay);
        int yearBirth = bir.get(Calendar.YEAR);
        int monthBirth = bir.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = bir.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                //monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }
}

package com.example.madhurarora.treebo.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by madhur.arora on 02/08/16.
 */
public class DateUtils {

    public static String getDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        return format.format(date);
    }
}

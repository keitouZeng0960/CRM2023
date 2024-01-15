package com.workspace.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    //针对date类型进行处理的工具类
    public static String dateToString(Date date){return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);}
}

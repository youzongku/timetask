package com.tomtop.timetask.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.CronExpression;

/**
 * cron 表达式测试类型
 * @author zbc
 */
public class CronUtil {

	public static void main(String[] args) {
//		String cron = "0 0/1 * * * ?";
//		String cron = "*/2 * * * * ?";
//		String cron = "0/1 * * * * ?";
		String cron = "0 */1 * * * ?";
		cronTest(cron);
	}
	
	/**
	 * @return true:表达式正确 false:表达式错误
	 * @param cron 表达式
	 * 打印下次触发事件
	 */
	public static boolean cronTest(String cron) {  
        try {  
            CronExpression exp = new CronExpression(cron);  
            SimpleDateFormat df = new SimpleDateFormat("YYYYMMDD HH:mm:ss");  
            Date d = new Date();  
            int i = 0;  
            // 循环得到接下来n此的触发时间点，供验证  
            while (i < 5) {  
                d = exp.getNextValidTimeAfter(d);  
                System.out.println(df.format(d));  
                ++i;  	
            }  
            return true;
        } catch (ParseException e) {  
            e.printStackTrace();  
            return false;
        }  
    } 
}

package team.redrock.newapi.util;

/**
 * @author: Shiina18
 * @date: 2019/3/22 11:38
 * @description:
 */
public class DateUtil {
    public static String getWeek(int i) {
        switch (i) {
            case 1:
                return "星期一";
            case 2:
                return "星期二";
            case 3:
                return "星期三";
            case 4:
                return "星期四";
            case 5:
                return "星期五";
            case 6:
                return "星期六";
            case 7:
                return "星期天";
            default:
                return "";
        }
    }

    public static String getLesson(int i) {
        switch (i) {
            case 1:
                return "一二节";
            case 2:
                return "三四节";
            case 4:
                return "五六节";
            case 5:
                return "七八节";
            case 7:
                return "九十节";
            case 8:
                return "十一十二节";
            default:
                return "";
        }
    }


}

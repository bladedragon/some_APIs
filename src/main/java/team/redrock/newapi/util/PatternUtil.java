package team.redrock.newapi.util;

import java.util.regex.Pattern;

/**
 * @author: Shiina18
 * @date: 2019/3/25 21:18
 * @description:
 */
public class PatternUtil {
    public static final Pattern TR_PATTERN = Pattern.compile("<tr.*?>(.*?)</tr>");
    public static final Pattern TD_PATTERN = Pattern.compile("<td.*?>(.*?)</td>");
}

package team.redrock.newapi.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: Shiina18
 * @date: 2019/3/24 3:41
 * @description:
 */
public class UnicodeUtil {

    private static final Pattern UNICODE_PATTERN = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

    public static String decode(String str) {

        Matcher matcher = UNICODE_PATTERN.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }

}

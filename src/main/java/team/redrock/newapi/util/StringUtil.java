package team.redrock.newapi.util;

/**
 * @author: Shiina18
 * @date: 2019/3/22 20:52
 * @description:
 */
public class StringUtil {
public static final String arrayToString(String[] array,String split){
    StringBuilder sb = new StringBuilder();
    for(String str:array){
        sb.append(str).append(split);
    }
    sb.delete(sb.length()-split.length(),sb.length());
    return sb.toString();
}

    public static boolean isNotEmpty(String... param) {
    for(String str:param){
        if(str==null||"".equals(str)){
            return false;
        }
    }
    return true;
}
}

package team.redrock.newapi.util;

/**
 * @author: Shiina18
 * @date: 19-3-27 上午11:20
 * @description:
 */
public class RandomUtil {
    public static final int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }
}

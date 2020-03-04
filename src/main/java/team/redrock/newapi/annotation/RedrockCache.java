package team.redrock.newapi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: Shiina18
 * @date: 19-3-26 下午9:12
 * @description:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedrockCache {

    int minDuration() default 15;

    int maxDuration() default 20;

}
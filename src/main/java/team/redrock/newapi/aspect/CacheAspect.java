package team.redrock.newapi.aspect;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import team.redrock.newapi.annotation.RedrockCache;
import team.redrock.newapi.aspect.entity.CacheEntity;
import team.redrock.newapi.exception.NetWorkException;
import team.redrock.newapi.model.response.ResponseEntity;
import team.redrock.newapi.util.RandomUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author: Shiina18
 * @date: 19-3-26 下午9:06
 * @description:
 */
@Aspect
@Component
@Slf4j
public class CacheAspect {

    /**
     * 时间戳长度 十位
     */
    private static final int TIMESTAMP_LENGTH = 10;
    @Autowired
    private RedisTemplate<String, String> stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, CacheEntity> objectRedisTemplate;

    private static final String FORCE_FETCH = "forceFetch";


    @Pointcut("@annotation(team.redrock.newapi.annotation.RedrockCache)")
    public void cache() {
    }

    @Around("cache()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        //组装key
        //className:methodName?param1,param2,......
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String forceFetch = attr.getRequest().getParameter(FORCE_FETCH);

        if (forceFetch != null) {
            Boolean force = Boolean.valueOf(forceFetch);
            if (force) {
                Object[] params = pjp.getArgs();
                Object target = pjp.proceed();

                String clazzName = pjp.getTarget().getClass().getName();
                String methodName = method.getName();

                String key = createKey(clazzName, methodName, params);
                setCache(key, method.getDeclaredAnnotation(RedrockCache.class), target);
                return target;
            }
        }

        return useCache(pjp);

    }

    @AfterThrowing(value = "cache()", throwing = "e")
    public Object afterThrowable(JoinPoint joinPoint, Throwable e) {
        if (e instanceof NetWorkException) {
            String clazzName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] params = joinPoint.getArgs();
            String key = createKey(clazzName, methodName, params);
            CacheEntity entity = objectRedisTemplate.opsForValue().get(key);
            if (entity != null) {
                ResponseEntity responseEntity = (ResponseEntity) entity.getEntity();
                responseEntity.setInfo("network error,use cache");
                return responseEntity;
            }
        }
        return ResponseEntity.NETWORK_ERROR;
    }

    private Object useCache(ProceedingJoinPoint pjp) throws Throwable {
        Object[] params = pjp.getArgs();
        String clazzName = pjp.getTarget().getClass().getName();
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        String methodName = method.getName();

        String key = createKey(clazzName, methodName, params);
        log.info("use cache:[{}]", key);
        ValueOperations<String, CacheEntity> operations = objectRedisTemplate.opsForValue();
        CacheEntity cache = operations.get(key);
        if (cache != null && !cache.isOverTime()) {
            return cache.getEntity();
        }
        Object value = pjp.proceed();
        RedrockCache redrockCache = method.getDeclaredAnnotation(RedrockCache.class);
        setCache(key, redrockCache, value);
        return value;
    }

    private void setCache(String key, RedrockCache redrockCache, Object value) {
        CacheEntity entity = new CacheEntity(RandomUtil.randomInt(redrockCache.minDuration(), redrockCache.maxDuration()), (ResponseEntity) value);
        objectRedisTemplate.opsForValue().set(key, entity);
    }

    private String createKey(String clazzName, String methodName, Object[] params) {
        StringBuilder sb = new StringBuilder(clazzName);
        sb.append(":");
        sb.append(methodName);
        sb.append("?");
        for (Object object : params) {
            if (object!=null) {
                sb.append(",");
                sb.append(object.toString());
            }
        }
        return sb.toString();
    }

}

package team.redrock.newapi.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import team.redrock.newapi.aspect.entity.CacheEntity;
import team.redrock.newapi.model.response.ResponseEntity;

/**
 * @author: Shiina18
 * @date: 2019/3/17 20:19
 * @description:
 */
@Configuration
public class RadisConfig {

    @Bean
    public RedisTemplate<String, CacheEntity> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
            RedisTemplate<String, CacheEntity> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(redisConnectionFactory);

            GsonRedisSerializer gsonRedisSerializer = new GsonRedisSerializer<>(CacheEntity.class);

            // 设置value的序列化规则和 key的序列化规则
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(gsonRedisSerializer);
            redisTemplate.afterPropertiesSet();
            return redisTemplate;
    }
}



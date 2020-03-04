package team.redrock.newapi.config;

import com.google.gson.Gson;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * @author: Shiina18
 * @date: 19-3-26 下午9:54
 * @description:
 */
public class GsonRedisSerializer<T> implements RedisSerializer<T> {

    private Class<T> targetClass;

    public GsonRedisSerializer(Class<T> clazz) {
        this.targetClass = clazz;
    }

    private static final Gson GSON = new Gson();

    @Override
    public byte[] serialize(Object o) throws SerializationException {
        return GSON.toJson(o).getBytes();
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes != null) {
            return GSON.fromJson(new String(bytes), targetClass);
        }
        return null;
    }
}

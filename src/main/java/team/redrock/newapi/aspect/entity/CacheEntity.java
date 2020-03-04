package team.redrock.newapi.aspect.entity;

import lombok.Data;
import team.redrock.newapi.model.response.ResponseEntity;

/**
 * @author: Shiina18
 * @date: 19-3-26 下午9:59
 * @description:
 */
@Data
public class CacheEntity {
    String timestamp;
    int duration;
    ResponseEntity entity;

    public CacheEntity() {
        setTimestampNow();
    }


    public CacheEntity(int duration, ResponseEntity obj) {
        setTimestampNow();
        this.duration = duration;
        this.entity = obj;
    }

    public boolean isOverTime() {
        return System.currentTimeMillis() / 1000 - Long.parseLong(timestamp) > duration * 60;
    }

    public void setTimestampNow() {
        this.timestamp = String.valueOf(System.currentTimeMillis() / 1000);
    }
}

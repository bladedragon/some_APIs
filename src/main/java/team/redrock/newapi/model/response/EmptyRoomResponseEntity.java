package team.redrock.newapi.model.response;

import lombok.Data;

import java.util.Set;

/**
 * @author: Shiina18
 * @date: 19-3-26 下午8:47
 * @description:
 */
@Data
public class EmptyRoomResponseEntity extends ResponseEntity<Set<String>> {

    private int nowWeek;

    public EmptyRoomResponseEntity(int nowWeek) {
        super();
        super.info=SUCCESS;
        this.nowWeek = nowWeek;
    }

}

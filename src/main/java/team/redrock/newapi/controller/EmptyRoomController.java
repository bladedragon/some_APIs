package team.redrock.newapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.redrock.newapi.annotation.RedrockCache;
import team.redrock.newapi.exception.NetWorkException;
import team.redrock.newapi.model.response.EmptyRoomResponseEntity;
import team.redrock.newapi.model.response.ResponseEntity;
import team.redrock.newapi.service.CourseService;
import team.redrock.newapi.service.EmptyRoomService;

import java.util.Set;

/**
 * @author: Shiina18
 * @date: 2019/3/21 17:44
 * @description:
 */
@RestController
@Slf4j
public class EmptyRoomController {

    @Autowired
    private EmptyRoomService emptyRoomService;

    @Autowired
    private CourseService courseService;

    @RedrockCache(minDuration = 60  * 4, maxDuration = 60 * 5)
    @RequestMapping(method = {RequestMethod.GET,RequestMethod.POST},value = "/roomEmpty")
    public ResponseEntity emptyRoom(int weekDayNum, int[] sectionNum, int buildNum, int week) throws NetWorkException {
        log.info("解析：[{}]教，第[{}]周，周[{}]，[{}]节",buildNum,week,weekDayNum,sectionNum);
        EmptyRoomResponseEntity responseEntity = new EmptyRoomResponseEntity(courseService.nowWeek());
        if (checkParam(weekDayNum, sectionNum, buildNum, week)) {
            Set<String> roomSet = emptyRoomService.findEmptyRoom(weekDayNum, sectionNum, buildNum, week);
            responseEntity.setData(roomSet);
        }
        return responseEntity;
    }

    private boolean checkParam(int weekDayNum, int []sectionNum, int buildNum, int week) {
        if( weekDayNum < 8 && weekDayNum > 0 && buildNum >= 2 && buildNum <= 8
                && buildNum != 6 && buildNum != 7 && week >= 1 && week <= 20){
            for(int section:sectionNum){
                if(! (section >= 0 && section <= 5)){
                    return false;
                }
            }
            return true;
        }
        return false;

    }
}


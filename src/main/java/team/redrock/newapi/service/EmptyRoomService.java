package team.redrock.newapi.service;

import team.redrock.newapi.exception.NetWorkException;

import java.util.List;
import java.util.Set;

/**
 * @author: Shiina18
 * @date: 2019/3/25 21:15
 * @description:
 */
public interface EmptyRoomService {

    /**
     *
     * 查找空教室
     *
     * @param weekDayNum 星期几 1~7
     * @param sectionNum 第几节课 0~5 12,34,56,78,910,1112
     * @param buildNum 几教
     * @param week 第几周
     * @return 空教室的set
     * @throws NetWorkException 网络不通畅 应该使用缓存
     */
    Set<String> findEmptyRoom(int weekDayNum, int[] sectionNum, int buildNum, int week) throws NetWorkException;
}

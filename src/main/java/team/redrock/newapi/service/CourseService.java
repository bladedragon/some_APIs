package team.redrock.newapi.service;

import team.redrock.newapi.exception.NetWorkException;
import team.redrock.newapi.model.Course;

import java.util.List;

/**
 * @author: Shiina18
 * @date: 2019/3/21 17:45
 * @description:
 */
public interface CourseService {

    /**
     * 解析教务在线的课表html
     *
     * @param stuNum 学号
     * @return 所有课的list
     * @throws NetWorkException 网络不通畅 应该使用缓存
     */
    List<Course> parseCourseHtml(String stuNum) throws NetWorkException;

    /**
     * 获得现在的周数
     *
     * @return 数量 1~20
     */

    int nowWeek();

    /**
     * 根据课表的list获得ics格式的课表
     *
     * @param courseList 课的list
     * @return ics格式的字符串
     */
    String courseToIcs(List<Course> courseList);
}

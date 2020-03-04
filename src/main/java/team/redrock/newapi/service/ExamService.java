package team.redrock.newapi.service;

import team.redrock.newapi.exception.NetWorkException;
import team.redrock.newapi.model.Grade;

import java.util.List;

/**
 * @author: Shiina18
 * @date: 2019/3/25 19:44
 * @description:
 */
public interface ExamService {

    /**
     * 验证学号和身份证后六位
     *
     * @param stuNum 学号
     * @param idNum  身份证后六位
     * @return 正确与否
     * @throws NetWorkException 网络不通畅 应该使用缓存
     */
    boolean authentication(String stuNum, String idNum) throws NetWorkException;

    /**
     * 查找成绩
     *
     * @param stuNum 学号
     * @return 每门课的成绩
     * @throws NetWorkException 网络不通畅 应该使用缓存
     */
    List<Grade> findGrade(String stuNum) throws NetWorkException;
}

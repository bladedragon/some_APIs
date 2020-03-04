package team.redrock.newapi.model;

import lombok.Data;
import team.redrock.newapi.config.AttributeConfig;

/**
 * @author: Shiina18
 * @date: 2019/3/25 19:50
 * @description:
 */
@Data
public class Grade {
    private String property;
    private String course;
    private String grade;
    private String stuNum;
    private String term = AttributeConfig.getTerm();

    public Grade(String stuNum,String property, String course, String gradeNum) {
        this.stuNum=stuNum;
        this.property = property;
        this.course = course;
        this.grade = gradeNum;
    }
}

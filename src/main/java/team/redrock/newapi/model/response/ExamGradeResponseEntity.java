package team.redrock.newapi.model.response;

import lombok.Data;
import team.redrock.newapi.config.AttributeConfig;
import team.redrock.newapi.model.Grade;

import java.util.List;

/**
 * @author: Shiina18
 * @date: 2019/3/25 19:51
 * @description:
 */
@Data
public class ExamGradeResponseEntity extends ResponseEntity<List<Grade>> {
    private String stuNum;
    private String idNum;
    private String term = AttributeConfig.getTerm();
    private String version = AttributeConfig.getVersion();

    public ExamGradeResponseEntity(String stuNum, String idNum, List<Grade> data) {
        super();
        this.stuNum = stuNum;
        this.idNum = idNum;
        this.data = data;
    }
}
